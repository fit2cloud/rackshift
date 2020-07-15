package io.rackshift.plugin.dell.utils;

import io.rackshift.metal.sdk.util.LogUtil;

public class DellUtils {

    public static Result login(String ip, String user, String password) {
        String[] loginHtml = {"curl", "-i", "-k", "-X", "GET", "https://" + ip + "/login.html"};
        Result result = CURLUtils.execCurl(loginHtml);
        String cookie = result.getHeader().get("set-cookie");
        LogUtil.info(String.format("IDrac6:%s , user:%s, password:%s 登录页面返回结果cookie:%s", ip, user, password, cookie));
        String[] login = {"curl", "-i", "-k", "-X", "POST", "-b", cookie, "-H", "Content-Type:application/x-www-form-urlencoded", "-d", "user=" + user + "&password=" + password, "https://" + ip + "/data/login"};
        Result result1 = CURLUtils.execCurl(login);
        LogUtil.info(String.format("IDrac6:%s , user:%s, password:%s 登录返回结果:%s", ip, user, password, result1.getResultStr()));
        result.setResultStr(result1.getResultStr());
        return result;
    }

    public static Result getInfo(String ip, String cookie) {
        String[] cmds = {"curl", "-i", "-k", "-X", "GET", "-b", cookie, "-H", "Content-Type:application/x-www-form-urlencoded", "https://" + ip + "/data?get=GetInv"};
        Result result = CURLUtils.execCurl(cmds);
        LogUtil.info(String.format("IDrac6:%s , cookie: %s,获取硬件返回结果:%s", ip, cookie, result.getResultStr()));
        return result;
    }

    public static void logout(String ip, String cookie) {
        String[] cmds = {"curl", "-i", "-k", "-X", "GET", "-b", cookie, "https://" + ip + "/data/logout"};
        CURLUtils.execCurl(cmds);
    }
}
