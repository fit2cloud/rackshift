package io.rackshift.rackshiftproxy.controller;

import io.rackshift.rackshiftproxy.model.DHCPConfig;
import io.rackshift.rackshiftproxy.model.R;
import io.rackshift.rackshiftproxy.service.DHCPService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("dhcp")
public class DHCPController {
    @Resource
    private DHCPService dhcpService;

    @RequestMapping("leases/{mac}")
    public R readDHCPLeases(@PathVariable String mac) throws IOException {
        return R.successWithData(dhcpService.queryIpByMac(mac));
    }

    @RequestMapping("configFile")
    public R readDHCPConfigFile() throws IOException {
        return R.successWithData(dhcpService.readDHCPConfigFile());
    }

    @RequestMapping("addDHCPConfig")
    public R addDHCPConfig(@RequestBody DHCPConfig config) throws IOException {
        return R.successWithData(dhcpService.addDHCPConfig(config));
    }

    @RequestMapping("saveDHCPConfig")
    public R addDHCPConfig(@RequestBody List<DHCPConfig> configs) throws IOException {
        return R.successWithData(dhcpService.saveDHCPConfig(configs));
    }

    @RequestMapping("delDHCPConfig")
    public R delDHCPConfig(@RequestBody DHCPConfig config) throws IOException {
        return R.successWithData(dhcpService.delDHCPConfig(config));
    }
}
