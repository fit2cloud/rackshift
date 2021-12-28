package io.rackshift.job;

import io.rackshift.constants.RackHDConstants;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.BareMetalExample;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.domain.OutBandExample;
import io.rackshift.mybatis.mapper.BareMetalMapper;
import io.rackshift.mybatis.mapper.OutBandMapper;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.LogUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ipmi定时扫描任务 更新物理机开关机状态 已经新添加物理机到ipmi被动发现
 */
@Service
public class SyncOutBandJob {
    @Resource
    private OutBandMapper outBandMapper;
    @Resource
    private BareMetalMapper bareMetalMapper;

    @Scheduled(fixedDelay = 300 * 1000)
    public void updatePowerOutBandStatusScheduler() {
        //测试每一个自发现机器带外的连通性，并且查看是否有物理机关联该带外ip（手动导入，不存在mac地址的只是视为信息纳管和基本开关机流程控制，因此不需要同步至物理机和RackHD）
        LocalDateTime before = LocalDateTime.now();
        LogUtil.info(String.format("开始带外物理机状态更新 标记%s：时间：%s", before.getDayOfMonth(), before.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS"))));
        OutBandExample pmOutBandExample = new OutBandExample();
        List<OutBand> pmOutBandList = outBandMapper.selectByExample(pmOutBandExample);
        if (pmOutBandList.size() == 0) {
            return;
        }
        updatePowerOutBandStatus(pmOutBandList);

        LocalDateTime now = LocalDateTime.now();
        LogUtil.info(String.format("结束开始带外物理机状态更新 标记%s：时间：%s,总共耗时:%s秒", before.getDayOfMonth(), now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS")), Duration.between(before, now).getSeconds()));
    }

    public void updatePowerOutBandStatus(List<OutBand> pmOutBandList) {
        if (CollectionUtils.isNotEmpty(pmOutBandList)) {
            pmOutBandList.forEach(o -> {
                IPMIUtil.Account account = new IPMIUtil.Account(o.getIp(), o.getUserName(), o.getPwd());
                //找出对应ip的mac地址的物理机把mac地址回写到带外
                BareMetalExample pmExample = new BareMetalExample();
                pmExample.createCriteria().andIdEqualTo(o.getBareMetalId());
                List<BareMetal> pms = bareMetalMapper.selectByExample(pmExample);
                try {

                    //ip存在但是账号密码为空
                    if (StringUtils.isAnyBlank(account.getHost(), account.getUserName(), account.getPwd())) {
                        setStatusBeforeIPMI(o, pms, RackHDConstants.PM_OUT_BAND_UNKNOW);
                        return;
                    }

                    String ipmiResult = IPMIUtil.exeCommand(account, "power status");
                    o.setStatus(RackHDConstants.PM_OUT_BAND_ON);
                    outBandMapper.updateByPrimaryKeySelective(o);

                    if (pms.size() > 0) {
                        //根据ipmi请求的返回结果判断物理机是开机还是关机状态
                        pms.stream().forEach(p -> {
                            //同时更新物理机的mac地址到带外列表
                            o.setMac(p.getBmcMac());
                            outBandMapper.updateByPrimaryKey(o);
                            updatePmStatusInfo(ipmiResult, p);
                        });
                    }
                } catch (Exception e) {
                    setStatusBeforeIPMI(o, pms, RackHDConstants.PM_OUT_BAND_OFF);
                }
            });
        }
    }

    private void setStatusBeforeIPMI(OutBand o, List<BareMetal> pms, String outBandStatus) {
        o.setStatus(outBandStatus);
        outBandMapper.updateByPrimaryKeySelective(o);
        pms.stream().forEach(p -> {
            updatePmStatusInfo(RackHDConstants.PM_POWER_UNKNOWN, p);
        });
        return;
    }

    private void updatePmStatusInfo(String ipmiResult, BareMetal machine) {
        //返回结果一般是 Chassis power on/off
        if (StringUtils.isNotBlank(ipmiResult) && ipmiResult.contains(RackHDConstants.PM_POWER_ON)) {
            machine.setPower(RackHDConstants.PM_POWER_ON);
        } else if (StringUtils.isNotBlank(ipmiResult) && ipmiResult.contains(RackHDConstants.PM_POWER_OFF)) {
            machine.setPower(RackHDConstants.PM_POWER_OFF);
        } else {
            machine.setPower(RackHDConstants.PM_POWER_UNKNOWN);
        }
        bareMetalMapper.updateByPrimaryKeySelective(machine);
    }

}
