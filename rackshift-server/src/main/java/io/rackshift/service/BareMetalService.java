package io.rackshift.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.*;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.BareMetalMapper;
import io.rackshift.mybatis.mapper.OutBandMapper;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerDecorator;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.Translator;
import io.rackshift.utils.UUIDUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BareMetalService {
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private BareMetalMapper bareMetalMapper;
    @Resource
    private OutBandMapper outBandMapper;
    @Resource
    private IPMIHandlerDecorator ipmiHandlerDecorator;
    @Resource
    private TaskService taskService;
    @Resource
    private ExecutionLogService executionLogService;
    @Resource
    private OutBandService outBandService;
    @Resource
    private InstructionLogService instructionLogService;
    @Resource
    private Cache cache;
    @Resource
    private DockerClientService dockerClientService;
    @Resource
    private SystemParameterMapper systemParameterMapper;
    private Set<Integer> portSet = new HashSet<>();
    private Random random = new Random();
    private int novncPort = 5800;

    public List<BareMetalDTO> list(BareMetalQueryVO queryVO) {
        return bareMetalManager.list(queryVO);
    }

    public ResultHolder power(String id, String opt) {

        BareMetal pm = bareMetalManager.getBareMetalById(id);

        if (pm == null) {
            throw new RuntimeException("物理机不存在！或者RackHD nodeid没有设置！");
        }

        //改用IPMI直接开关机重启
        OutBandExample outbandExample = new OutBandExample();
        outbandExample.createCriteria().andBareMetalIdEqualTo(id);

        List<OutBand> outBands = outBandMapper.selectByExample(outbandExample);
        if (outBands.size() < 1) {
            RSException.throwExceptions("请先配置带外信息！");
        }

        IPMIUtil.Account account = IPMIUtil.Account.build(outBands.get(0));
        ResultHolder resultHolder = null;
        if (!(resultHolder = ipmiHandlerDecorator.execute(opt, account, pm, outBands.get(0))).isSuccess()) {
            return resultHolder.error(resultHolder.getMessage());
        }

        if ("pxe".equalsIgnoreCase(opt) && LifeStatus.onrack.name().equalsIgnoreCase(pm.getStatus())) {
            pm.setStatus(LifeStatus.discovering.name());
            bareMetalManager.update(pm, true);
        }

        return ResultHolder.success("");
    }

    public ResultHolder hardwares(String bareId) {
        return ResultHolder.success(bareMetalManager.hardwares(bareId));
    }

    public boolean del(String[] ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        for (String id : ids) {
            bareMetalManager.delBareMetalById(id);
            outBandService.delBareMetalById(id);
            taskService.delByBareMetalId(id);
            executionLogService.delDetailsByBareMetalId(id);
            instructionLogService.delDetailsByBareMetalId(id);
        }
        return true;
    }

    public boolean addToBareMetal(BareMetalDTO bareMetalDTO) {
        if (StringUtils.isAnyBlank(bareMetalDTO.getMachineSn(), bareMetalDTO.getMachineModel(), bareMetalDTO.getRuleId())) {
            RSException.throwExceptions(Translator.get("parameter_error"));
        }
        BareMetal bareMetal = new BareMetal();
        BeanUtils.copyBean(bareMetal, bareMetalDTO);
        bareMetal.setStatus(LifeStatus.onrack.name());

        bareMetalManager.addToBareMetal(bareMetal);
        return true;
    }

    public ResultHolder powerBatch(String[] ids, String power) {
        for (String id : ids) {
            power(id, power);
        }
        return ResultHolder.success("");
    }

    public Object all() {
        return bareMetalManager.all();
    }

    public ResultHolder webkvm(String id, String host) {
        if (host.replace("://", "").contains(":"))
            host = host.substring(0, host.lastIndexOf(":"));

        BareMetal bareMetal = bareMetalManager.getBareMetalById(id);
        if (bareMetal == null) {
            return ResultHolder.error(Translator.get("BareMetal_Server_Not_exists"));
        }
        OutBand ob = outBandService.getByBareMetalId(id);

        if (ob == null) {
            return ResultHolder.error(Translator.get("OB_Not_exists"));
        }

        IPMIUtil.Account account = IPMIUtil.Account.build(ob);
        try {
            IPMIUtil.exeCommand(account, "power status");
        } catch (Exception e) {
            return ResultHolder.error(e.getMessage());
        }

        Element e = cache.get(id);
        SystemParameter kvmImage = systemParameterMapper.selectByPrimaryKey("kvm.image");

        if (kvmImage == null) {
            return ResultHolder.error(Translator.get("kvm_image_not_exists"));
        }

        //曾经打开的容器
        if (StringUtils.isNotBlank(bareMetal.getContainerId())) {
            if (dockerClientService.isRunning(bareMetal.getContainerId())) {
                return ResultHolder.success(host + ":" + dockerClientService.getExposedPort(bareMetal.getContainerId(), novncPort));
            } else {
                cleanContainer(bareMetal.getContainerId(), bareMetal);
            }
        }
        //每次打开都重启容器
        if (e != null) {
            KVMInfo info = (KVMInfo) e.getObjectValue();
            if (dockerClientService.isRunning(info.getContainerId())) {
                return ResultHolder.success(host + ":" + info.getPort());
            } else {
                cleanContainer(info.getContainerId(), bareMetal);
            }
        }
        List<String> envs = new LinkedList<>();
        envs.add(String.format("VENDOR=%s", bareMetal.getMachineBrand()));
        envs.add(String.format("HOST=%s", ob.getIp()));
        envs.add(String.format("USER=%s", ob.getUserName()));
        envs.add(String.format("PASSWD=%s", ob.getPwd()));
        envs.add(String.format("APP_NAME=%s", bareMetal.getMachineModel() + " " + bareMetal.getMachineSn() + " " + bareMetal.getManagementIp()));
        int exposedPort = chooseSinglePort();
        String src = "/opt/rackshift/rackhd/files/mount/common";
        CreateContainerResponse r = dockerClientService.createContainer(kvmImage.getParamValue(), novncPort, exposedPort, envs, src, "/vmedia");
        dockerClientService.startContainer(r.getId());
        KVMInfo info = new KVMInfo(id, ob, exposedPort, r.getId());
        e = new Element(id, info);
        cache.put(e);
        bareMetal.setContainerId(r.getId());
        bareMetalManager.update(bareMetal);
        try {
            Thread.sleep(2500);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        return ResultHolder.success(host + ":" + exposedPort);
    }

    private void cleanContainer(String containerId, BareMetal bareMetal) {
        try {
            if (dockerClientService.exist(containerId)) {
                dockerClientService.removeContainer(containerId);
            }
        } catch (Exception e) {
        }
        cache.remove(bareMetal.getId());
        bareMetal.setContainerId(null);
        bareMetalManager.update(bareMetal);
    }

    private synchronized int chooseSinglePort() {
        int port = 0;
        do {
            port = 10000 + random.nextInt(10000);
        } while (portSet.contains(port));
        portSet.add(port);
        return port;
    }

    public ResultHolder refreshPower(String id) {
        OutBand o = outBandService.getByBareMetalId(id);
        if (o == null) {
            return ResultHolder.error("没有配置带外信息!");
        }
        IPMIUtil.Account account = IPMIUtil.Account.build(o);
        BareMetal b = bareMetalManager.getBareMetalById(id);
        try {
            String status = IPMIUtil.exeCommand(account, "power status");
            if (status.contains(RackHDConstants.PM_POWER_ON)) {
                b.setPower(RackHDConstants.PM_POWER_ON);
            } else if (status.contains(RackHDConstants.PM_POWER_OFF)) {
                b.setPower(RackHDConstants.PM_POWER_OFF);
            } else {
                b.setPower(RackHDConstants.PM_POWER_UNKNOWN);
            }
            bareMetalManager.update(b);
        } catch (Exception e) {
            return ResultHolder.error("与服务器得带外连接失败！");
        }
        return ResultHolder.success("");
    }

    public ResultHolder allBrands() {
        SystemParameter systemParameter = systemParameterMapper.selectByPrimaryKey(ServiceConstants.PARAM_ALL_BRANDS);

        if (systemParameter == null || StringUtils.isBlank(systemParameter.getParamValue())) {
            RSException.throwExceptions(Translator.get("no_brands_key_exists"));
        }

        try {
            systemParameter.getParamValue().split("\\|");
        } catch (Exception e) {
            RSException.throwExceptions(Translator.get("no_brands_key_exists"));
        }

        return ResultHolder.success(systemParameter.getParamValue().split("\\|"));
    }

    public BareMetal getByPXEMAC(String macs) {
        BareMetalExample e = new BareMetalExample();
        e.createCriteria().andPxeMacEqualTo(macs);
        List<BareMetal> bareMetals = bareMetalMapper.selectByExample(e);
        if (bareMetals.size() == 1) {
            return bareMetals.get(0);
        }
        return null;
    }

    public BareMetal getById(String nodeId) {
        return bareMetalManager.getBareMetalById(nodeId);
    }

    public ResultHolder add(BareMetalDTO request) {
        if (StringUtils.isBlank(request.getPxeMac())) {
            return ResultHolder.error("add error! no pxe mac");
        }
        request.setId(UUIDUtil.newUUID());
        request.setStatus(LifeStatus.onrack.name());
        if (bareMetalManager.getBareMetalByPXEMac(request.getPxeMac()) != null) {
            return ResultHolder.error("add error! pxe mac :" + request.getPxeMac() + "exists !");
        }
        if (bareMetalManager.addToBareMetal(request)) {
            return ResultHolder.success("");
        }
        return ResultHolder.error("opt error");
    }

    public ResultHolder remark(BareMetalDTO request) {
        if (StringUtils.isBlank(request.getId())) {
            return ResultHolder.error(Translator.get("error"));
        }
        BareMetal bareMetal = new BareMetal();
        bareMetal.setId(request.getId());
        bareMetal.setRemark(request.getRemark());
        bareMetalManager.updateByPrimaryKeySelective(bareMetal);
        return ResultHolder.success("");
    }
}
