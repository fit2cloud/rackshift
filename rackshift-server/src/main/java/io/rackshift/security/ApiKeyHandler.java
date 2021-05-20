package io.rackshift.security;

import io.rackshift.mybatis.domain.UserKey;
import io.rackshift.service.UserKeysService;
import io.rackshift.utils.CodingUtil;
import io.rackshift.utils.CommonBeanFactory;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liqiang on 2018/7/19.
 */
public class ApiKeyHandler {

    public static final String API_ACCESS_KEY = "accessKey";

    public static final String API_SIGNATURE = "signature";

    public static String getUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return getUser(request.getHeader(API_ACCESS_KEY), request.getHeader(API_SIGNATURE));
    }

    public static Boolean isApiKeyCall(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        if (StringUtils.isBlank(request.getHeader(API_ACCESS_KEY)) || StringUtils.isBlank(request.getHeader(API_SIGNATURE))) {
            return false;
        }
        return true;
    }

    public static String getUser(String accessKey, String signature) {
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(signature)) {
            return null;
        }
        UserKey userKey = CommonBeanFactory.getBean(UserKeysService.class).getUserKey(accessKey);
        if (userKey == null) {
            throw new RuntimeException("invalid accessKey");
        }
        String signatureDecrypt;
        try {
            signatureDecrypt = CodingUtil.aesDecrypt(signature, userKey.getSecretKey(), accessKey);
        } catch (Throwable t) {
            throw new RuntimeException("invalid signature");
        }
        String[] signatureArray = StringUtils.split(StringUtils.trimToNull(signatureDecrypt), "|");
        if (signatureArray.length < 2) {
            throw new RuntimeException("invalid signature");
        }
        if (!StringUtils.equals(accessKey, signatureArray[0])) {
            throw new RuntimeException("invalid signature");
        }
        long signatureTime = 0l;
        try {
            signatureTime = Long.valueOf(signatureArray[signatureArray.length - 1]).longValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Math.abs(System.currentTimeMillis() - signatureTime) > 1800000) {
            //签名30分钟超时
            throw new RuntimeException("expired signature");
        }
        return userKey.getUserId();
    }
}
