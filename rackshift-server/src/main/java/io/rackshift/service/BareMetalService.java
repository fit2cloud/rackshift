package io.rackshift.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.*;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.*;
import io.rackshift.mybatis.mapper.ext.ExtNetworkCardMapper;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerDecorator;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.*;
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
    private OutBandMapper outBandMapper;
    @Resource
    private IPMIHandlerDecorator ipmiHandlerDecorator;
    @Resource
    private CpuMapper cpuMapper;
    @Resource
    private MemoryMapper memoryMapper;
    @Resource
    private DiskMapper diskMapper;
    @Resource
    private NetworkCardMapper networkCardMapper;
    @Resource
    private RackHDService rackHDService;
    @Resource
    private EndpointService endpointService;
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
    private Set<String> portSet = new HashSet<>();
    private Random random = new Random();

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
            BareMetal bareMetal = bareMetalManager.getBareMetalById(id);
            if (endpointService.getById(bareMetal.getEndpointId()) != null) {
                if (StringUtils.isNotBlank(bareMetal.getServerId())) {
                    try {
                        rackHDService.cancelWorkflow(bareMetal);
                        rackHDService.deleteNode(bareMetal);
                    } catch (Exception e) {
                        LogUtil.info("删除RackHD节点失败！", ExceptionUtils.getExceptionDetail(e));
                    }
                }
            }
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
        SystemParameter kvmImage = systemParameterMapper.selectByPrimaryKey(bareMetal.getMachineBrand() + ".kvm.image");

        if (kvmImage == null) {
            return ResultHolder.error(Translator.get("kvm_image_not_exists"));
        }
        //每次打开都重启容器
        if (e != null) {
            KVMInfo info = (KVMInfo) e.getObjectValue();
            dockerClientService.stopAndRemoveContainer(info.getContainerId());
        }
        List<String> envs = new LinkedList<>();
        envs.add(String.format("HOST=%s", ob.getIp()));
        envs.add(String.format("USER=%s", ob.getUserName()));
        envs.add(String.format("PASSWD=%s", ob.getPwd()));
        envs.add(String.format("APP_NAME=%s", bareMetal.getMachineModel() + " " + bareMetal.getMachineSn() + " " + bareMetal.getManagementIp()));
        String exposedPort = chooseSinglePort();
        CreateContainerResponse r = dockerClientService.createContainer(kvmImage.getParamValue(), "5800", exposedPort, envs);
        dockerClientService.startContainer(r.getId());
        KVMInfo info = new KVMInfo(id, ob, exposedPort, r.getId());
        e = new Element(id, info);
        cache.put(e);
        if (host.replace("://", "").contains(":"))
            host = host.substring(0, host.lastIndexOf(":"));
        return ResultHolder.success(host + ":" + exposedPort);
    }

    private synchronized String chooseSinglePort() {
        String port = "";
        do {
            port = String.valueOf(10000 + random.nextInt(10000));
        } while (portSet.contains(port));
        portSet.add(port);
        return port;
    }
}
