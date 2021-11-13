package io.rackshift.utils;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.mybatis.domain.OutBand;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class IPMIUtil {
    private static ExecutorService executorService;
    private static final int waitTimes = 10;

    static {
        executorService = Executors.newFixedThreadPool(20);
    }

    public static String exeCommand(Account account, String commands) throws Exception {
        if (StringUtils.isAnyBlank(account.getHost(), account.getUserName(), account.getPwd()) || StringUtils.isAnyBlank(commands)) {
            throw new RuntimeException("运行ipmitool命令失败！参数非法！");
        }
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                List<String> cmds = new LinkedList<>();
                cmds.add("ipmitool");
                cmds.add("-I");
                cmds.add("lanplus");
                cmds.add("-H");
                cmds.add(account.getHost());
                cmds.add("-U");
                cmds.add(account.getUserName());
                cmds.add("-P");
                cmds.add(account.getPwd());
                for (String c : commands.split(" ")) {
                    cmds.add(c);
                }
                ProcessBuilder builder = new ProcessBuilder(cmds);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                InputStreamReader re = new InputStreamReader(p.getInputStream(), "utf-8");
                BufferedReader b = new BufferedReader(re);
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = b.readLine()) != null) {
                    sb.append(line + "\n");
                }
                p.waitFor();
                p.getInputStream().close();
                re.close();
                b.close();
                p.destroy();
                return sb.toString();
            }
        };
        Future<String> future = executorService.submit(callable);
        //不同机型 不同硬件数量返回时间可能有不一样的情况 需要在生产上调试这个数值 使得爬虫可以稳定工作
        String r = future.get(waitTimes, TimeUnit.MINUTES);
        if (StringUtils.isBlank(r) && !commands.contains("set password") || r.contains("Error:") && !commands.equalsIgnoreCase("sdr")) {
            throw new RuntimeException("物理机：" + account.getHost() + "带外连接失败！");
        }
        return r;
    }


    public static String exeCommandForUserIndex(String brand, Account account) throws Exception {
        if (StringUtils.isAnyBlank(account.getHost(), account.getUserName(), account.getPwd())) {
            throw new RuntimeException("运行ipmitool命令失败！参数非法！");
        }
        String commands = "user list";
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                List<String> cmds = new LinkedList<>();
                cmds.add("ipmitool");
                cmds.add("-I");
                cmds.add("lanplus");
                cmds.add("-H");
                cmds.add(account.getHost());
                cmds.add("-U");
                cmds.add(account.getUserName());
                cmds.add("-P");
                cmds.add(account.getPwd());
                for (String c : commands.split(" ")) {
                    cmds.add(c);
                }
                ProcessBuilder builder = new ProcessBuilder(cmds);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                InputStreamReader re = new InputStreamReader(p.getInputStream(), "utf-8");

                BufferedReader b = new BufferedReader(re);
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = b.readLine()) != null && StringUtils.isNotBlank(line)) {
                    sb.append(line + "\n");

                    if (line.contains(account.getUserName()) || (brand.equalsIgnoreCase("HP") && line.toLowerCase().contains(account.getUserName()))) {
                        p.getInputStream().close();
                        re.close();
                        b.close();
                        p.destroy();
                        return line.split(" ")[0];
                    }
                }
                p.getInputStream().close();
                re.close();
                b.close();
                p.destroy();
                return "";
            }
        };

        Future<String> future = executorService.submit(callable);
        String r = future.get(waitTimes, TimeUnit.MINUTES);
        if (StringUtils.isBlank(r)) {
            throw new RuntimeException("物理机：" + account.getHost() + "带外连接失败！");
        }
        return r;
    }

    public static String exeCommandForBrand(Account account) throws Exception {
        String commandResult = IPMIUtil.exeCommand(account, "fru");
        JSONObject fruObj = IPMIUtil.transform(commandResult);
        return fruObj.getString("Product Manufacturer");
    }

    public static String grep(String string, String sign) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isBlank(string) || StringUtils.isBlank(sign)) return null;
        Arrays.asList(string.split("\n")).stream().filter(s -> s.contains(sign)).collect(Collectors.toList()).forEach(s -> sb.append(s + "\n"));
        return sb.toString();
    }

    public static JSONObject transform(String commandResult) {
        JSONObject re = new JSONObject();
        Arrays.asList(commandResult.split("\n")).forEach(r -> {
            int index = r.indexOf(":");
            if (index == -1) {
                return;
            }
            if (r.substring(0, index + 1).replace(":", "").trim().length() == 0) {
                return;
            }
            if (re.get(r.substring(0, index + 1).replace(":", "").trim()) == null) {
                re.put(r.substring(0, index + 1).replace(":", "").trim(), r.substring(index + 1, r.length()).trim());
            }
        });
        return re;
    }

    public static void main(String[] args) {
        System.out.println(transform("Set in Progress         : Set Complete\n" +
                "Auth Type Support       : MD5 \n" +
                "Auth Type Enable        : Callback : MD5 \n" +
                "                        : User     : MD5 \n" +
                "                        : Operator : MD5 \n" +
                "                        : Admin    : MD5 \n" +
                "                        : OEM      : MD5 \n" +
                "IP Address Source       : DHCP Address\n" +
                "IP Address              : 43.14.101.5\n" +
                "Subnet Mask             : 255.255.255.0\n" +
                "MAC Address             : 84:65:69:5c:a2:a4\n" +
                "SNMP Community String   : AMI\n" +
                "IP Header               : TTL=0x40 Flags=0x40 Precedence=0x00 TOS=0x10\n" +
                "BMC ARP Control         : ARP Responses Enabled, Gratuitous ARP Disabled\n" +
                "Gratituous ARP Intrvl   : 0.0 seconds\n" +
                "Default Gateway IP      : 43.14.101.254\n" +
                "Default Gateway MAC     : 3c:d2:e5:36:ca:01\n" +
                "Backup Gateway IP       : 0.0.0.0\n" +
                "Backup Gateway MAC      : 00:00:00:00:00:00\n" +
                "802.1q VLAN ID          : Disabled\n" +
                "802.1q VLAN Priority    : 0\n" +
                "RMCP+ Cipher Suites     : 0,1,2,3,6,7,8,11,12,15,16,17\n" +
                "Cipher Suite Priv Max   : caaaaaaaaaaaXXX\n" +
                "                        :     X=Cipher Suite Unused\n" +
                "                        :     c=CALLBACK\n" +
                "                        :     u=USER\n" +
                "                        :     o=OPERATOR\n" +
                "                        :     a=ADMIN\n" +
                "                        :     O=OEM\n" +
                "Bad Password Threshold  : 0\n" +
                "Invalid password disable: no\n" +
                "Attempt Count Reset Int.: 0\n" +
                "User Lockout Interval   : 0"));
    }

    public static class Account {
        String host;
        String userName;
        String pwd;
        String newIp;
        String newPwd;


        public static Account build(OutBand outBand) {
            return new Account(outBand.getIp(), outBand.getUserName(), outBand.getPwd());
        }

        public static Account build(String ip, String userName, String passowrd) {
            return new Account(ip, userName, passowrd);
        }

        public Account() {
        }

        public Account(String host, String userName, String pwd) {
            this.host = host;
            this.userName = userName;
            this.pwd = pwd;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }

        public String getNewIp() {
            return newIp;
        }

        public void setNewIp(String newIp) {
            this.newIp = newIp;
        }
    }

}
