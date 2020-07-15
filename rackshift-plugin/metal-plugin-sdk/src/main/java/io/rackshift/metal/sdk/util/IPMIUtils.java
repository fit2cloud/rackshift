package io.rackshift.metal.sdk.util;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.model.request.IPMIRequest;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IPMIUtils {
    private static ExecutorService executorService;
    private static final int waitTimes = 10;
    private static Gson gson;

    static {
        executorService = Executors.newFixedThreadPool(20);
        gson = new Gson();
    }

    public static String exeCommand(IPMIRequest account, String commands) throws Exception {
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
                p.getInputStream().close();
                re.close();
                b.close();
                p.destroy();
                return sb.toString();
            }
        };
        Future<String> future = executorService.submit(callable);
        //不同机型 不同硬件数量 不同网络环境情况返回时间可能有不一样的情况 需要在生产上调试这个数值 使得爬虫可以稳定工作
        String r = future.get(waitTimes, TimeUnit.MINUTES);
        if (StringUtils.isBlank(r) && !commands.contains("set password") || r.contains("Error:") && !commands.equalsIgnoreCase("sdr")) {
            throw new RuntimeException("物理机：" + account.getHost() + "带外连接失败！");
        }
        return r;
    }


    public static String exeCommandForUserIndex(String brand, IPMIRequest account) throws Exception {
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

    public static String grep(String string, String sign) {
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(sign);

        if (StringUtils.isBlank(string) || StringUtils.isBlank(sign)) return null;
        Arrays.asList(string.split("\n")).stream().filter(
                s -> p.matcher(s).find()
        ).collect(Collectors.toList()).forEach(s -> sb.append(s + "\n"));
        return sb.toString();
    }

    /**
     * 提取string里的1个或者多个数字，组成一个数
     *
     * @param s
     * @return
     */
    static Pattern pNumber = Pattern.compile("\\d");

    public static int extractStringNumber(String s) {
        if (StringUtils.isBlank(s)) {
            return 0;
        }
        Matcher m = pNumber.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            sb.append(m.group());
        }
        if (sb.toString().startsWith("0")) {
            sb.replace(0, 1, "");
        }
        return Integer.valueOf(sb.toString());
    }

    public static int ipmiComparator(String s1, String s2) {
        if (s1.split(" ")[0].length() < s2.split(" ")[0].length()) {
            return -1;
        } else if (s1.split(" ")[0].length() > s2.split(" ")[0].length()) {
            return 1;
        } else {
            int r = extractStringNumber(s1) - extractStringNumber(s2);
            return r > 0 ? 1 : r == 0 ? 0 : -1;
        }
    }

    public static String extractSdrIndexValue(String string, String splitter, int index, String replaceVal) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isAnyBlank(string, splitter)) return null;
        Arrays.asList(string.split("\n")).stream().sorted(IPMIUtils::ipmiComparator).collect(Collectors.toList()).forEach(s -> sb.append(s + "\n"));
        string = sb.toString();
        sb.delete(0, sb.length());
        Arrays.asList(string.split("\n")).stream().map(
                s -> s.split(splitter)[index].replace(" ", "").replace(replaceVal, "")
        ).collect(Collectors.toList()).forEach(s -> sb.append(s + "\n"));
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

    public static String exeCommandForBrand(String ipmiResetPwdRequest) throws Exception {
        IPMIRequest request = gson.fromJson(ipmiResetPwdRequest, IPMIRequest.class);
        String commandResult = exeCommand(request, "fru");
        JSONObject fruObj = transform(commandResult);
        return fruObj.getString("Product Manufacturer");
    }
}
