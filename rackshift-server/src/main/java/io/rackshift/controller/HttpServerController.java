package io.rackshift.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HttpServerController {

    @RequestMapping("nocloud/{macs}/user-data")
    public void getUserDataByMac(@PathVariable String macs, HttpServletResponse response) throws IOException {
        response.getWriter().write("#cloud-config\n" +
                "\n" +
                "autoinstall:\n" +
                "  version: 1\n" +
                "  identity:\n" +
                "    hostname: pxe-client\n" +
                "    password: $6$FhcddHFVZ7ABA4Gi$9l4yURWASWe8xEa1jzI0bacVLvhe3Yn4/G3AnU11K3X0yu/mICVRxfo6tZTB2noKljlIRzjkVZPocdf63MtzC0\n" +
                "    realname: pxe\n" +
                "    username: pxe\n" +
                "  keyboard: {layout: hr, toggle: toggle, variant: \"\"}\n" +
                "  locale: en_US\n" +
                "  network:\n" +
                "    network:\n" +
                "      version: 2\n" +
                "      ethernets:\n" +
                "        eth0:\n" +
                "          dhcp4: yes\n" +
                "          dhcp6: no");
    }

    @RequestMapping("nocloud/{macs}/meta-data")
    public void getUserDataByMacMeta(@PathVariable String macs,HttpServletResponse response) throws IOException {
        response.getWriter().write("");
    }
}
