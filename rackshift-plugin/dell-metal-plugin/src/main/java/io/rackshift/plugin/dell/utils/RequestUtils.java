package io.rackshift.plugin.dell.utils;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;

import java.util.Map;

public class RequestUtils {
    public static void idrac6FormatHeaders(String ip, Map<String, String> headers) {
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Connection", "keep-alive");
        headers.put("Referer", String.format("https://%s/sysinventory.html?cat=C00&tab=T00&id=P02", ip));
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36");
        headers.put("Host", ip);
        headers.put("Origin", "https://" + ip);
    }

    public static void idrac6SetHeaders(String ip, Header[] cookies, Map<String, String> headers) {
        LogUtil.info(String.format("爬取Dell idrac6 %s登录接口返回Header：%s", JSONObject.toJSONString(cookies)));
        String responseCookie = getCookie(cookies, "Set-Cookie", "SET-COOKIE");
        LogUtil.info(String.format("爬取Dell idrac6 %s登录接口最终Cookie：%s", ip, responseCookie));
        if (StringUtils.isBlank(responseCookie)) {
            return;
        }
        headers.put("Cookie", responseCookie);
    }

    static String getCookie(Header[] header, String key1, String key2) {
        for (Header h : header) {
            if (h.getName().equalsIgnoreCase(key1) || h.getName().equalsIgnoreCase(key2)) {
                return h.getValue();
            }
        }
        return null;
    }
}
