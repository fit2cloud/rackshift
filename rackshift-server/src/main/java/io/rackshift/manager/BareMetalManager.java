package io.rackshift.manager;

import io.rackshift.engine.util.CatalogParser;
import io.rackshift.model.*;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.*;
import io.rackshift.mybatis.mapper.ext.ExtNetworkCardMapper;
import io.rackshift.service.EndpointService;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.LogUtil;
import io.rackshift.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BareMetalManager {
    @Resource
    private BareMetalMapper bareMetalMapper;
    @Resource
    private CatalogParser catalogParser;
    @Resource
    private CpuMapper cpuMapper;
    @Resource
    private MemoryMapper memoryMapper;
    @Resource
    private DiskMapper diskMapper;
    @Resource
    private NetworkCardMapper networkCardMapper;
    @Resource
    private OutBandManager outBandManager;
    @Autowired
    private SimpMessagingTemplate template;
    @Resource
    private ExtNetworkCardMapper extNetworkCardMapper;
    @Resource
    private EndpointService endpointService;

    public BareMetal getBareMetalByPXEMac(String mac) {
        if (StringUtils.isBlank(mac)) {
            return null;
        }
        BareMetalExample example = new BareMetalExample();
        example.createCriteria().andPxeMacEqualTo(mac);
        List<BareMetal> bareMetalList = bareMetalMapper.selectByExample(example);
        if (bareMetalList.size() > 0) {
            return bareMetalList.get(0);
        }
        return null;
    }

    public BareMetal getBareMetalBySn(String sn) {
        if (StringUtils.isBlank(sn)) {
            return null;
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
            RSException.throwExceptions("error！ cannt get ip!");
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
        CpuExample c = new CpuExample();
        c.createCriteria().andBareMetalIdEqualTo(id);
        cpuMapper.deleteByExample(c);

        MemoryExample m = new MemoryExample();
        m.createCriteria().andBareMetalIdEqualTo(id);
        memoryMapper.deleteByExample(m);

        DiskExample d = new DiskExample();
        d.createCriteria().andBareMetalIdEqualTo(id);
        diskMapper.deleteByExample(d);

        NetworkCardExample n = new NetworkCardExample();
        n.createCriteria().andBareMetalIdEqualTo(id);
        networkCardMapper.deleteByExample(n);

        return bareMetalMapper.deleteByPrimaryKey(id);
    }

    public void update(BareMetal bareMetal, boolean changeStatus) {

        BareMetal oldBareMetal = getBareMetalById(bareMetal.getId());
        bareMetal.setId(oldBareMetal.getId());
        if (!changeStatus) {
            bareMetal.setStatus(oldBareMetal.getStatus());
        }
        BeanUtils.copyBean(oldBareMetal, bareMetal);
        bareMetalMapper.updateByPrimaryKeySelective(oldBareMetal);
        if (changeStatus) {
            template.convertAndSend("/topic/lifecycle", "");
        }
    }

    public void update(BareMetal bareMetal) {
        bareMetalMapper.updateByPrimaryKey(bareMetal);
    }

    public void updateByPrimaryKeySelective(BareMetal bareMetal) {
        bareMetalMapper.updateByPrimaryKeySelective(bareMetal);
    }

    public BareMetal saveOrUpdateEntity(MachineEntity e) {
        if (StringUtils.isAllBlank(e.getPxeMac(), e.getSerialNo())) {
            return null;
        }
        BareMetal bareMetal = null;
        try {
            bareMetal = catalogParser.convertToPm(e);
            BareMetal dbBareMetal = getBareMetalByPXEMac(bareMetal.getPxeMac());
            if (dbBareMetal == null) {
                dbBareMetal = getBareMetalBySn(bareMetal.getMachineSn());
            }
            if (dbBareMetal == null) {
                bareMetalMapper.insertSelective(bareMetal);
            } else {
                bareMetal.setId(dbBareMetal.getId());
                removeDuplicate(bareMetal);
                update(bareMetal, true);
            }
            saveOrUpdateHardWare(e);
        } catch (Exception ex) {
            LogUtil.error("转换rack实体出错！" + ExceptionUtils.getExceptionDetail(ex));
        }
        return bareMetal;
    }

    private void removeDuplicate(BareMetal bareMetal) {
        if (StringUtils.isNotBlank(bareMetal.getMachineSn())) {
            BareMetal bareMetal1 = getBareMetalBySn(bareMetal.getMachineSn());
            if (bareMetal1 != null && !StringUtils.equals(bareMetal1.getId(), bareMetal.getId())) {
                mergeTwoRecord(bareMetal, bareMetal1);
            }
        }
    }

    private void mergeTwoRecord(BareMetal bareMetal, BareMetal dbBareMetal) {
        List<OutBand> outBands = outBandManager.getByBareMetalId(dbBareMetal.getId());
        if (outBands.size() > 0) {
            OutBand outBand = new OutBand();
            BeanUtils.copyBean(outBand, outBands.get(0));
            outBand.setBareMetalId(bareMetal.getId());
            outBand.setId(UUIDUtil.newUUID());
            outBandManager.deleteById(dbBareMetal.getId());
            outBandManager.save(outBand);
        }
        bareMetal.setRuleId(dbBareMetal.getRuleId());
        bareMetal.setPower(dbBareMetal.getPower());
        bareMetalMapper.deleteByPrimaryKey(dbBareMetal.getId());
    }

    private void saveOrUpdateHardWare(MachineEntity e) {

        List<Cpu> cpus = e.getCpus();
        List<Memory> memories = e.getMemories();
        List<Disk> disks = e.getDisks();
        List<NetworkCard> nics = e.getNetworkCards();

        BareMetal bareMetal = getBareMetalByPXEMac(e.getPxeMac());
        if (bareMetal == null)
            bareMetal = getBareMetalBySn(e.getSerialNo());
        long now = System.currentTimeMillis();

        CpuExample cpuExample = new CpuExample();
        cpuExample.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());

        MemoryExample memoryExample = new MemoryExample();
        memoryExample.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());

        DiskExample diskExample = new DiskExample();
        diskExample.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());

        NetworkCardExample networkCardExample = new NetworkCardExample();
        networkCardExample.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());

        BareMetal finalBareMetal = bareMetal;
        if (cpus.size() > 0) {
            cpuMapper.deleteByExample(cpuExample);
            cpus.forEach(c -> {
                c.setBareMetalId(finalBareMetal.getId());
                c.setSyncTime(now);
                cpuMapper.insertSelective(c);
            });
        }

        if (memories.size() > 0) {
            memoryMapper.deleteByExample(memoryExample);
            memories.forEach(m -> {
                m.setBareMetalId(finalBareMetal.getId());
                m.setSyncTime(now);
                memoryMapper.insertSelective(m);
            });
        }

        if (disks.size() > 0) {
            diskMapper.deleteByExample(diskExample);
            disks.forEach(d -> {
                d.setBareMetalId(finalBareMetal.getId());
                d.setSyncTime(now);
                diskMapper.insertSelective(d);
            });
        }

        if (nics.size() > 0) {
            networkCardMapper.deleteByExample(networkCardExample);
            nics.forEach(d -> {
                d.setBareMetalId(finalBareMetal.getId());
                if (!StringUtils.isAnyBlank(e.getPxeMac(), d.getMac()) && d.getMac().equalsIgnoreCase(e.getPxeMac())) {
                    d.setPxe(true);
                }
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
            bareMetalDTO.setOutBandList(outBandManager.getByBareMetalId(b.getId()));
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
            String likeClause = "%" + queryVO.getSearchKey() + "%";
            example.or(example.createCriteria().andMachineModelLike(likeClause));
            example.or(example.createCriteria().andMachineSnLike(likeClause));
            example.or(example.createCriteria().andManagementIpLike(likeClause));
            example.or(example.createCriteria().andRuleIdEqualTo(queryVO.getSearchKey()));
            example.or(example.createCriteria().andRemarkLike(likeClause));
        }
        return example;
    }

    public boolean addToBareMetal(BareMetal bareMetal) {
        String sn = bareMetal.getMachineSn();
        BareMetal bareMetal1 = getBareMetalBySn(sn);
        if (bareMetal1 == null) {
            bareMetalMapper.insertSelective(bareMetal);
            return true;
        }
        return false;
    }

    public List<BareMetal> getByIds(String[] bareMetalIds) {
        if (bareMetalIds == null || bareMetalIds.length == 0) {
            return new ArrayList<>();
        }
        BareMetalExample e = new BareMetalExample();
        e.createCriteria().andIdIn(Arrays.asList(bareMetalIds));
        return bareMetalMapper.selectByExample(e);
    }

    public List<BareMetalDTO> all() {
        BareMetalExample bareMetalExample = buildParams(new BareMetalQueryVO());
        List<BareMetal> list = bareMetalMapper.selectByExample(bareMetalExample);
        List<BareMetalDTO> r = new LinkedList<>();
        list.forEach(b -> {
            BareMetalDTO bareMetalDTO = new BareMetalDTO();
            BeanUtils.copyBean(bareMetalDTO, b);
            bareMetalDTO.setOutBandList(outBandManager.getByBareMetalId(b.getId()));
            bareMetalDTO.setHardware(hardwares(b.getId()));
            r.add(bareMetalDTO);
        });

        return r;
    }

    public Map hardwares(String bareId) {
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
        List<NetworkCardDTO> nics = extNetworkCardMapper.getNicsById(bareId);

        r.put("cpus", cpus);
        r.put("memories", memories);
        r.put("disks", disks);
        r.put("nics", nics);

        return r;
    }

    public BareMetal createNewFromPXE(String macs) {
        BareMetal newNode = new BareMetal();
        newNode.setId(UUIDUtil.newUUID());
        newNode.setEndpointId(endpointService.getMainEndpoint().getId());
        newNode.setPxeMac(macs);
        newNode.setStatus(LifeStatus.discovering.name());
        bareMetalMapper.insertSelective(newNode);
        return newNode;
    }
}
