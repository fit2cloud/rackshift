package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.OutBandDTO;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.domain.OutBandExample;
import io.rackshift.mybatis.mapper.OutBandMapper;
import io.rackshift.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class OutBandService {
    @Resource
    private OutBandMapper outBandMapper;
    @Resource
    private BareMetalManager bareMetalManager;

    public void saveOrUpdate(OutBand o, boolean modifyIp) {
        if (StringUtils.isBlank(o.getBareMetalId())) {
            RSException.throwExceptions("保存带外信息失败！无裸金属服务ID");
        }
        if (o.getId() != null) {
            OutBand dbOutBand = outBandMapper.selectByPrimaryKey(o.getId());
            o.setId(dbOutBand.getId());
            BeanUtils.copyBean(dbOutBand, o);
            outBandMapper.updateByPrimaryKeySelective(o);
            return;
        }

        OutBandExample example = new OutBandExample();
        example.createCriteria().andIpEqualTo(o.getIp()).andBareMetalIdNotEqualTo(o.getBareMetalId());
        if (outBandMapper.selectByExample(example).size() > 0) {
            outBandMapper.deleteByExample(example);
        }
        example.clear();
        example.createCriteria().andBareMetalIdEqualTo(o.getBareMetalId());

        List<OutBand> outBands = outBandMapper.selectByExample(example);
        if (outBands.size() > 0) {
            outBands.forEach(o1 -> {
                if (modifyIp) {
                    o1.setIp(o.getIp());
                }
                o1.setBareMetalId(o.getBareMetalId());
                o1.setUserName(o.getUserName());
                o1.setPwd(o.getPwd());
                outBandMapper.updateByPrimaryKey(o1);
            });
        } else {
            o.setId(UUIDUtil.newUUID());
            outBandMapper.insertSelective(o);
        }
    }

    public void fillOBMS(String bareMetalId, OutBand outBand) {
        outBand.setBareMetalId(bareMetalId);
        saveOrUpdate(outBand, true);
    }

    public Object add(OutBandDTO queryVO) {
        OutBand image = new OutBand();
        BeanUtils.copyBean(image, queryVO);
        BareMetal bareMetal = bareMetalManager.getBareMetalByIp(queryVO.getIp());
        if (bareMetal == null) {
            return null;
        }
        image.setBareMetalId(bareMetal.getId());
        outBandMapper.insertSelective(image);
        return true;
    }

    public Object update(OutBandDTO queryVO) {
        OutBand image = new OutBand();
        BeanUtils.copyBean(image, queryVO);
        outBandMapper.updateByPrimaryKeySelective(image);

        BareMetal bareMetal = bareMetalManager.getBareMetalByIp(queryVO.getIp());
        if (bareMetal == null) {
            return null;
        }
        return true;
    }

    public Object del(String id) {
        outBandMapper.deleteByPrimaryKey(id);
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<OutBand> list(OutBandDTO queryVO) {
        OutBandExample example = buildExample(queryVO);
        return outBandMapper.selectByExample(example);
    }

    private OutBandExample buildExample(OutBandDTO queryVO) {
        return new OutBandExample();
    }

    public OutBand getByIp(String ip) {
        OutBandExample e = new OutBandExample();
        e.createCriteria().andIpEqualTo(ip);
        List<OutBand> outBands = outBandMapper.selectByExample(e);
        return outBands.size() > 0 ? outBands.get(0) : null;
    }

    public void fillOBMS(OutBandDTO outBand) {
        OutBand obm = outBand.getObm();
        if (StringUtils.isAnyBlank(obm.getUserName(), obm.getPwd())) {
            RSException.throwExceptions(Translator.get("error"));
            return;
        }
        for (String id : outBand.getIds()) {
            BareMetal bareMetal = bareMetalManager.getBareMetalById(id);
            if (bareMetal == null) {
                continue;
            }
            OutBandExample e = new OutBandExample();
            e.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());
            List<OutBand> outBands = outBandMapper.selectByExample(e);
            if (outBands.size() > 0) {
                outBands.forEach(o -> {
                    o.setUserName(obm.getUserName());
                    o.setPwd(obm.getPwd());
                    fillOBMS(bareMetal.getId(), o);
                });
            } else {
                obm.setId(null);
                obm.setIp(bareMetal.getManagementIp());
                obm.setBareMetalId(id);
                fillOBMS(bareMetal.getId(), obm);
            }
        }
    }

    public List<OutBand> getByBareMetalIds(String[] bareMetalIds) {
        OutBandExample e = new OutBandExample();
        e.createCriteria().andBareMetalIdIn(Arrays.asList(bareMetalIds));
        return outBandMapper.selectByExample(e);
    }

    public OutBand getByBareMetalId(String id) {
        OutBandExample e = new OutBandExample();
        e.createCriteria().andBareMetalIdEqualTo(id);
        List<OutBand> o = outBandMapper.selectByExample(e);
        if (o.size() > 0) {
            return o.get(0);
        }
        return null;
    }

    public void delBareMetalById(String id) {
        OutBandExample e = new OutBandExample();
        e.createCriteria().andBareMetalIdEqualTo(id);
        outBandMapper.deleteByExample(e);
    }

    public ResultHolder changePwd(String[] ids, String pwd) throws Exception {
        int success = 0;
        ResultHolder resultHolder = null;
        for (String id : ids) {
            BareMetal pm = bareMetalManager.getBareMetalById(id);

            if (pm == null) {
                throw new RuntimeException("物理机不存在！或者RackHD nodeid没有设置！");
            }

            OutBandExample outbandExample = new OutBandExample();
            outbandExample.createCriteria().andBareMetalIdEqualTo(id);

            List<OutBand> outBands = outBandMapper.selectByExample(outbandExample);
            if (outBands.size() < 1) {
                RSException.throwExceptions("请先配置带外信息！");
            }

            IPMIUtil.Account account = IPMIUtil.Account.build(outBands.get(0));
            account.setNewPwd(pwd);
            resultHolder = changePwd(account, outBands.get(0), pm);
            if (resultHolder.isSuccess())
                success++;
        }
        return ResultHolder.success("执行结果:成功" + success + "台，失败" + (ids.length - success) + "台!");
    }

    private ResultHolder changePwd(IPMIUtil.Account account, OutBand outBand, BareMetal bm) throws Exception {
        try {
            String brand = IPMIUtil.exeCommandForBrand(account);
            String userIndex = IPMIUtil.exeCommandForUserIndex(brand, account);
            int tryTimes = 0;
            do {
                if (userIndex.contains("Error")) {
                    userIndex = IPMIUtil.exeCommandForUserIndex(bm.getMachineBrand(), account);
                } else {
                    break;
                }
                tryTimes++;
            } while (tryTimes < 5);
            String r = IPMIUtil.exeCommand(account, String.format("user set password %s %s", userIndex, account.getNewPwd()));
            IPMIUtil.Account a = new IPMIUtil.Account();
            BeanUtils.copyBean(a, account);
            a.setPwd("******");
            LogUtil.info(String.format("ipmitool result:【%s】,account:【%s】", r, JSONObject.toJSONString(a)));
        } catch (Exception e) {
        } finally {
            try {
                account.setPwd(account.getNewPwd());
                String powerResult = IPMIUtil.exeCommand(account, "power status");
                if (powerResult.contains(RackHDConstants.PM_POWER_ON) || powerResult.contains(RackHDConstants.PM_POWER_OFF)) {
                    outBand.setPwd(account.getNewPwd());
                    fillOBMS(bm.getId(), outBand);
                    return ResultHolder.success("修改密码成功！");
                } else {
                    return ResultHolder.error("修改密码失败！合法的密码应该包含大小写字母数字特殊字符，总共不少于8个");
                }
            } catch (Exception e) {
                return ResultHolder.error("修改密码失败！");
            }
        }
    }
}
