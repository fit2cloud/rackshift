package io.rackshift.service;

import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.BareMetalDTO;
import io.rackshift.model.BareMetalQueryVO;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.*;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerDecorator;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map r = new HashMap();
        CpuExample cpuExample = new CpuExample();
        cpuExample.createCriteria().andBareMetalIdEqualTo(bareId);

        MemoryExample memoryExample = new MemoryExample();
        memoryExample.createCriteria().andBareMetalIdEqualTo(bareId);

        DiskExample diskExample = new DiskExample();
        diskExample.createCriteria().andBareMetalIdEqualTo(bareId);

        NetworkCardExample networkCardExample = new NetworkCardExample();
        networkCardExample.createCriteria().andBareMetalIdEqualTo(bareId);

        List<Cpu> cpus = cpuMapper.selectByExample(cpuExample);
        List<Memory> memories = memoryMapper.selectByExample(memoryExample);
        List<Disk> disks = diskMapper.selectByExample(diskExample);
        List<NetworkCard> nics = networkCardMapper.selectByExample(networkCardExample);


        r.put("cpus", cpus);
        r.put("memories", memories);
        r.put("disks", disks);
        r.put("nics", nics);

        return ResultHolder.success(r);
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
        }
        return true;
    }

    public boolean addToBareMetal(BareMetalDTO bareMetalDTO) {
        if (StringUtils.isAnyBlank(bareMetalDTO.getMachineSn(), bareMetalDTO.getMachineModel(), bareMetalDTO.getRuleId())) {
            RSException.throwExceptions(Translator.get("i18n_param_error"));
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
}
