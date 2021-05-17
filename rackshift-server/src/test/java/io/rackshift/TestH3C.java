package io.rackshift;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.PluginConstants;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.job.model.ProtocolRequest;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.service.OutBandService;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.LogUtil;
import io.rackshift.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestH3C {

    @Resource
    BareMetalManager bareMetalManager;
    @Resource
    OutBandService outBandService;

    @Test
    public void testDiscoveryIPMI() {
        try {
            ProtocolRequest request = new ProtocolRequest();
            IPMIUtil.Account account = IPMIUtil.Account.build(request.getHost(), request.getUserName(), request.getPwd());
            JSONObject fruObj = JSONObject.parseObject("{\"Chassis Type\":\"Rack Mount Chassis\",\"Board Product\":\"RS33M2C9S\",\"Board Mfg Date\":\"Wed Jan 27 16:00:00 2021\",\"Board Extra\":\"210235A2CR6212F00041\",\"Product Name\":\"UniServer R4900 G3\",\"Product Serial\":\"210235A2CR6212F00041\",\"FRU Device Description\":\"Builtin FRU Device (ID 0)\",\"Board Mfg\":\"H3C\",\"Board Serial\":\"02A3FQ6211F0070Z\",\"Product Part Number\":\"0235A2CR\",\"Product Version\":\"2.00.47\",\"Chassis Part Number\":\"0235A2CR\",\"Chassis Extra\":\"210200A00Q6212F00075\",\"Chassis Serial\":\"210235A2CR6212F00041\",\"Board Part Number\":\"0302A3FQ\",\"Product Manufacturer\":\"New H3C Technologies Co., Ltd.\"}");
            JSONObject lanObj = JSONObject.parseObject("{\"SNMP Community String\":\"AMI\",\"BMC ARP Control\":\"ARP Responses Enabled, Gratuitous ARP Disabled\",\"RMCP+ Cipher Suites\":\"0,1,2,3,6,7,8,11,12,15,16,17\",\"IP Header\":\"TTL=0x40 Flags=0x40 Precedence=0x00 TOS=0x10\",\"802.1q VLAN ID\":\"Disabled\",\"Auth Type Support\":\"MD5\",\"IP Address Source\":\"DHCP Address\",\"Gratituous ARP Intrvl\":\"0.0 seconds\",\"Default Gateway IP\":\"43.14.101.254\",\"802.1q VLAN Priority\":\"0\",\"User Lockout Interval\":\"0\",\"MAC Address\":\"84:65:69:5c:a2:a4\",\"Backup Gateway MAC\":\"00:00:00:00:00:00\",\"Invalid password disable\":\"no\",\"IP Address\":\"43.14.101.5\",\"Subnet Mask\":\"255.255.255.0\",\"Backup Gateway IP\":\"0.0.0.0\",\"Attempt Count Reset Int.\":\"0\",\"Auth Type Enable\":\"Callback : MD5\",\"Set in Progress\":\"Set Complete\",\"Default Gateway MAC\":\"3c:d2:e5:36:ca:01\",\"Bad Password Threshold\":\"0\",\"Cipher Suite Priv Max\":\"caaaaaaaaaaaXXX\"}");

            String powerResult = "power is on";

            String machineBrand = fruObj.getString("Product Manufacturer");
            String machineSn = fruObj.getString("Product Serial");
            String name = machineBrand + " " + fruObj.getString("Product Name");

            BareMetal physicalMachine = new BareMetal();
            if (StringUtils.isNotBlank(name)) {
                physicalMachine.setMachineModel(name);
            }
            physicalMachine.setId(UUIDUtil.newUUID());
            if (powerResult.contains(RackHDConstants.PM_POWER_ON)) {
                physicalMachine.setPower(RackHDConstants.PM_POWER_ON);
            } else if (powerResult.contains(RackHDConstants.PM_POWER_OFF)) {
                physicalMachine.setPower(RackHDConstants.PM_POWER_OFF);
            } else {
                physicalMachine.setPower(RackHDConstants.PM_POWER_UNKNOWN);
            }
            physicalMachine.setRuleId("");
            physicalMachine.setManagementIp(account.getHost());
            physicalMachine.setMachineSn(machineSn);
            if (PluginConstants.DELL.equalsIgnoreCase(machineBrand)) {
                physicalMachine.setMachineModel(PluginConstants.DELL + " " + fruObj.getString("Board Product"));
            }
            physicalMachine.setMachineBrand(machineBrand);
            physicalMachine.setBmcMac(lanObj.getString("MAC Address"));
            physicalMachine.setProviderId("");
            physicalMachine.setStatus(LifeStatus.onrack.name());
            boolean r = bareMetalManager.addToBareMetal(physicalMachine);
            saveOutBand(account, physicalMachine, r);
        } catch (Exception e) {
            LogUtil.error("ss", ExceptionUtils.getExceptionDetail(e));
        }
    }

    private void saveOutBand(IPMIUtil.Account account, BareMetal bareMetal, boolean r) {
        if (!r) return;
        OutBand o = new OutBand();
        o.setIp(account.getHost());
        o.setBareMetalId(bareMetal.getId());
        o.setUserName(account.getUserName());
        o.setPwd(account.getPwd());
        outBandService.saveOrUpdate(o, false);
    }
}
