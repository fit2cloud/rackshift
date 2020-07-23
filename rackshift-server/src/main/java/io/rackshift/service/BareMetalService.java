package io.rackshift.service;

import io.rackshift.model.BareMetalDTO;
import io.rackshift.model.MachineEntity;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.*;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerDecorator;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BareMetalService {
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
    private IPMIHandlerDecorator ipmiHandlerDecorator;

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

    public void update(BareMetal bareMetal) {
        BareMetal oldBareMetal = getBareMetalBySn(bareMetal.getMachineSn());
        bareMetal.setId(oldBareMetal.getId());
        BeanUtils.copyBean(oldBareMetal, bareMetal);
        bareMetalMapper.updateByPrimaryKeySelective(oldBareMetal);
    }

    public boolean saveOrUpdateEntity(MachineEntity e) {
        if (StringUtils.isBlank(e.getSerialNo())) {
            return false;
        }
        try {
            BareMetal bareMetal = rackHDService.convertToPm(e);
            if (getBareMetalBySn(bareMetal.getMachineSn()) == null) {
                bareMetalMapper.insertSelective(bareMetal);
            } else {
                update(bareMetal);
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

    public List<BareMetal> list(BareMetalDTO queryVO) {
        BareMetalExample bareMetalExample = buildParams(queryVO);
        return bareMetalMapper.selectByExample(bareMetalExample);
    }

    private BareMetalExample buildParams(BareMetalDTO queryVO) {
        return new BareMetalExample();
    }

    public ResultHolder power(String id, String opt) {

        BareMetal pm = bareMetalMapper.selectByPrimaryKey(id);
//        if (pm == null || StringUtils.isBlank(pm.getServerId())) {
//            throw new RuntimeException("物理机不存在！或者RackHD nodeid没有设置！");
//        }
//
        if (pm == null) {
            throw new RuntimeException("物理机不存在！或者RackHD nodeid没有设置！");
        }

        /**1.获取对应物理的RackHD地址和验证信息 执行RESTfull调用
         * 2.RackHD的nodeId直接执行workflow
         **/
//        String discoveryId = pm.getRuleId();
//        BareMetalRule rule = physicalMachineRuleMapper.selectByPrimaryKey(discoveryId);
//        if (rule == null) {
//            throw new RuntimeException("物理机发现规则不存在！");
//        }
//        String url = JSONObject.parseObject(rule.getCredentialParam()).getString("rackHost");
//        if (url.endsWith("/")) {
//            url = url.substring(0, url.length() - 1);
//        }
        //改用IPMI直接开关机重启
        OutBandExample outbandExample = new OutBandExample();
        outbandExample.createCriteria().andIpEqualTo(pm.getManagementIp());

        List<OutBand> outBands = outBandMapper.selectByExample(outbandExample);
        if (outBands.size() < 1) {
            RSException.throwExceptions("请先配置带外信息！");
        }

        IPMIUtil.Account account = IPMIUtil.Account.build(outBands.get(0));
        ResultHolder resultHolder = null;
        if (!(resultHolder = ipmiHandlerDecorator.execute(opt, account, pm, outBands.get(0))).isSuccess()) {
            RSException.throwExceptions(resultHolder.getMessage());
        }
        return ResultHolder.success("");
    }
}
