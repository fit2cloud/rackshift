package io.rackshift.manager;

import io.rackshift.model.BareMetalDTO;
import io.rackshift.model.BareMetalQueryVO;
import io.rackshift.model.MachineEntity;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.*;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerDecorator;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.strategy.statemachine.StateMachine;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class BareMetalManager {
    @Resource
    private BareMetalMapper bareMetalMapper;
    @Resource
    private RackHDService rackHDService;
    @Resource
    private CpuMapper cpuMapper;
    @Resource
    private MemoryMapper memoryMapper;
    @Resource
    private DiskMapper diskMapper;
    @Resource
    private NetworkCardMapper networkCardMapper;
    @Resource
    private OutBandMapper outBandMapper;
    @Resource
    private OutBandManager outBandManager;
    @Resource
    private IPMIHandlerDecorator ipmiHandlerDecorator;
    @Resource
    private StateMachine stateMachine;

    public BareMetal getBareMetalBySn(String sn) {
        if (StringUtils.isBlank(sn)) {
            RSException.throwExceptions("error！ cannt get sn!");
        }
        BareMetalExample example = new BareMetalExample();
        example.createCriteria().andMachineSnEqualTo(sn);
        List<BareMetal> bareMetalList = bareMetalMapper.selectByExample(example);
        if (bareMetalList.size() > 0) {
            return bareMetalList.get(0);
        }
        return null;
    }

    public BareMetal getBareMetalByIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            RSException.throwExceptions("error！ cannt get sn!");
        }
        BareMetalExample example = new BareMetalExample();
        example.createCriteria().andManagementIpEqualTo(ip);
        List<BareMetal> bareMetalList = bareMetalMapper.selectByExample(example);
        if (bareMetalList.size() > 0) {
            return bareMetalList.get(0);
        }
        return null;
    }

    public BareMetal getBareMetalById(String id) {
        return bareMetalMapper.selectByPrimaryKey(id);
    }

    public int delBareMetalById(String id) {
        return bareMetalMapper.deleteByPrimaryKey(id);
    }

    public void update(BareMetal bareMetal, boolean changeStatus) {
        BareMetal oldBareMetal = getBareMetalBySn(bareMetal.getMachineSn());
        bareMetal.setId(oldBareMetal.getId());
        if (!changeStatus) {
            bareMetal.setStatus(oldBareMetal.getStatus());
        }
        BeanUtils.copyBean(oldBareMetal, bareMetal);
        bareMetalMapper.updateByPrimaryKeySelective(oldBareMetal);
    }

    public boolean saveOrUpdateEntity(MachineEntity e) {
        if (StringUtils.isBlank(e.getSerialNo())) {
            return false;
        }
        try {
            BareMetal bareMetal = rackHDService.convertToPm(e);
            BareMetal dbBareMetal = getBareMetalBySn(bareMetal.getMachineSn());
            if (dbBareMetal == null) {
                bareMetalMapper.insertSelective(bareMetal);
            } else {
                //对正在进行中的状态进行查询，如果对应的node没有正在运行中的任务，则该进行中的状态自动到终点
                List<String> nodeIds = rackHDService.getActiveWorkflowNodeIds();
                if (StringUtils.isNotBlank(bareMetal.getServerId()) && !nodeIds.contains(bareMetal.getServerId())) {
                    LifeEvent endEvent = null;
                    if (LifeStatus.discovering.name().equalsIgnoreCase(bareMetal.getStatus())) {
                        endEvent = LifeEvent.POST_DISCOVERY_WORKFLOW_END;
                    } else if (LifeStatus.provisioning.name().equalsIgnoreCase(bareMetal.getStatus())) {
                        endEvent = LifeEvent.POST_OTHER_WORKFLOW_END;
                    } else if (LifeStatus.deploying.name().equalsIgnoreCase(bareMetal.getStatus())) {
                        endEvent = LifeEvent.POST_OS_WORKFLOW_END;
                    }
                    if (endEvent != null) {
                        endEvent.setBareMetalId(dbBareMetal.getId());
                        stateMachine.sendEvent(endEvent);
                    }
                }
                update(bareMetal, false);
            }
            saveOrUpdateHardWare(e);
        } catch (Exception ex) {
            LogUtil.error("转换rack实体出错！" + ExceptionUtils.getExceptionDetail(ex));
        }
        return true;
    }

    private void saveOrUpdateHardWare(MachineEntity e) {

        List<Cpu> cpus = e.getCpus();
        List<Memory> memories = e.getMemories();
        List<Disk> disks = e.getDisks();
        List<NetworkCard> nics = e.getNetworkCards();

        BareMetal bareMetal = getBareMetalBySn(e.getSerialNo());
        long now = System.currentTimeMillis();

        CpuExample cpuExample = new CpuExample();
        cpuExample.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());

        MemoryExample memoryExample = new MemoryExample();
        memoryExample.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());

        DiskExample diskExample = new DiskExample();
        diskExample.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());

        NetworkCardExample networkCardExample = new NetworkCardExample();
        networkCardExample.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());

        if (cpus.size() > 0) {
            cpuMapper.deleteByExample(cpuExample);
            cpus.forEach(c -> {
                c.setBareMetalId(bareMetal.getId());
                c.setSyncTime(now);
                cpuMapper.insertSelective(c);
            });
        }

        if (memories.size() > 0) {
            memoryMapper.deleteByExample(memoryExample);
            memories.forEach(m -> {
                m.setBareMetalId(bareMetal.getId());
                m.setSyncTime(now);
                memoryMapper.insertSelective(m);
            });
        }

        if (disks.size() > 0) {
            diskMapper.deleteByExample(diskExample);
            disks.forEach(d -> {
                d.setBareMetalId(bareMetal.getId());
                d.setSyncTime(now);
                diskMapper.insertSelective(d);
            });
        }

        if (nics.size() > 0) {
            networkCardMapper.deleteByExample(networkCardExample);
            nics.forEach(d -> {
                d.setBareMetalId(bareMetal.getId());
                d.setSyncTime(now);
                networkCardMapper.insertSelective(d);
            });
        }
    }

    public List<BareMetalDTO> list(BareMetalQueryVO queryVO) {
        BareMetalExample bareMetalExample = buildParams(queryVO);
        List<BareMetal> list = bareMetalMapper.selectByExample(bareMetalExample);
        List<BareMetalDTO> r = new LinkedList<>();
        list.forEach(b -> {
            BareMetalDTO bareMetalDTO = new BareMetalDTO();
            BeanUtils.copyBean(bareMetalDTO, b);
            bareMetalDTO.setOutBandList(outBandManager.getByIp(b.getManagementIp()));
            r.add(bareMetalDTO);
        });
        return r;
    }

    private BareMetalExample buildParams(BareMetalQueryVO queryVO) {
        BareMetalExample example = new BareMetalExample();
        if (StringUtils.isNotBlank(queryVO.getSort())) {
            example.setOrderByClause(queryVO.getSort());
        }
        if (StringUtils.isNotBlank(queryVO.getSearchKey())) {
            example.or(example.createCriteria().andMachineModelLike(queryVO.getSearchKey()));
            example.or(example.createCriteria().andMachineSnLike(queryVO.getSearchKey()));
            example.or(example.createCriteria().andManagementIpLike(queryVO.getSearchKey()));
        }
        return example;
    }

}
