package io.rackshift.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.HashMap;
import java.util.Map;

public class ProxyUtil {
    private static final String APISECRET = "rackshift-proxy-20200820";

    public static Map<String, String> getHeaders() {
        Map<String, String> header = new HashMap<>();
        String signature = JWT.create().withClaim("time", System.currentTimeMillis()).sign(Algorithm.HMAC256(APISECRET));
        header.put("signature", signature);
        return header;
    }
}
