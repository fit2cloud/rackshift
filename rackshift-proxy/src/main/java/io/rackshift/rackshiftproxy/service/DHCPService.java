package io.rackshift.rackshiftproxy.service;

import io.rackshift.rackshiftproxy.model.DHCPConfig;
import io.rackshift.rackshiftproxy.model.DHCPLease;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DHCPService {
    @Value("${dhcpd.leases.pos:/opt/rackshift/rackhd/dhcp/config/dhcpd.leases}")
    private String leasPpos;
    @Value("${dhcpd.leases.pos:/opt/rackshift/rackhd/dhcp/config/dhcpd.conf}")
    private String configPos;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public List<DHCPLease> queryIpByMac(String mac) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(leasPpos)));
        String line = null;
        boolean start = false;
        DHCPLease curObj = new DHCPLease();
        List<DHCPLease> arr = new ArrayList<DHCPLease>();
        while ((line = reader.readLine()) != null) {
            if (line.contains("lease") && !line.contains("leases") && !line.contains("dhcp")) {
                start = true;
                curObj.setIp(line.split("lease")[1].split("\\{")[0].trim());
            }
            if (start && line.contains("hardware ethernet")) {
                curObj.setMac(line.split(" ")[4].split(";")[0].trim());
            }
            if (start && line.contains("starts")) {
                String t[] = line.split(" ");
                curObj.setStartTime(LocalDateTime.parse(t[4] + " " + t[5].split(";")[0], dateTimeFormatter));
            }
            if (start && line.contains("}")) {
                start = false;
                arr.add(curObj);
                curObj = new DHCPLease();
            }
        }

        if (StringUtils.isEmpty(mac)) {
            return arr.stream().sorted(Comparator.comparing(DHCPLease::getStartTime, (s1, s2) ->
                    s1.isAfter(s2) ? -1 : 1
            )).collect(Collectors.toList());
        } else {
            return arr.stream().filter(s -> s.getMac().equalsIgnoreCase(mac)).sorted(Comparator.comparing(DHCPLease::getStartTime, (s1, s2) ->
                    s1.isAfter(s2) ? -1 : 1
            )).collect(Collectors.toList());
        }
    }

    public List<DHCPConfig> readDHCPConfigFile() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configPos)));
        String line = null;

        boolean start = false;
        DHCPConfig config = null;

        Pattern p = Pattern.compile("^\\s*range\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s*;$");
        Matcher m = null;
        List<DHCPConfig> configs = new LinkedList<>();
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("subnet") || line.contains("subnet")) {
                start = true;
                config = new DHCPConfig();
                config.setNetmask(line.split(" ")[3].trim());
            }
            if (start && line.contains("range")) {
                m = p.matcher(line);
                int i = 0;
                while (m.find()) {
                    if (i == 0) {
                        config.setStartIp(m.group(1));
                        i++;
                    }
                    if (i == 1) {
                        config.setEndIp(m.group(2));
                    }
                }
            }
            if (start && line.contains("PXEClient")) {
                config.setPxeEnabled(true);
            }
            if (line.startsWith("}") || line.contains("}")) {
                start = false;
                configs.add(config);
            }
        }
        return configs;
    }

    public boolean addDHCPConfig(DHCPConfig config) throws IOException {
        if (StringUtils.isAllBlank(config.getStartIp(), config.getEndIp(), config.getNetmask())) {
            return false;
        }
        List<DHCPConfig> configs = readDHCPConfigFile();
        configs.add(config);
        configs = configs.size() > 1 ? configs.stream().distinct().collect(Collectors.toList()) : configs;
        return updateDHCPConfig(configs);
    }

    private boolean updateDHCPConfig(List<DHCPConfig> configs) throws IOException {
        Template template = Velocity.getTemplate("static/dhcpd.config");
        VelocityContext context = new VelocityContext();
        context.internalPut("dhcpConfig", configs);
        FileWriter fw = new FileWriter(new File(configPos));
        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        sw.toString();
        fw.write(sw.toString());
        fw.close();
        sw.close();
        return true;
    }

    public boolean delDHCPConfig(DHCPConfig config) throws IOException {
        if (StringUtils.isAllBlank(config.getStartIp(), config.getEndIp(), config.getNetmask())) {
            return false;
        }
        List<DHCPConfig> configs = readDHCPConfigFile();
        configs.remove(config);
        configs = configs.stream().distinct().collect(Collectors.toList());
        return updateDHCPConfig(configs);
    }

    public boolean saveDHCPConfig(List<DHCPConfig> addConfigs) throws IOException {
        List<DHCPConfig> configs = readDHCPConfigFile();
        if (configs.size() > 0) {
            configs.remove(addConfigs.get(0));
            configs.add(addConfigs.get(1));
        } else {
            configs = addConfigs;
        }
        return updateDHCPConfig(configs);
    }


}
