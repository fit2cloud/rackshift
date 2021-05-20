package io.rackshift.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by liqiang on 2018/6/7.
 */
public class SsoSessionHandler {

    private static Logger logger = LoggerFactory.getLogger(SsoSessionHandler.class);

    public static final String SSO_HEADER_AUTH_NAME = "RS_SESSION_ID";

    public static final String SSO_SOURCE_ID = "sourceId";

    public static String random = UUID.randomUUID().toString() + UUID.randomUUID().toString();

    public static String generateId(String authInfo) {
        return SessionGenerator.generateId(authInfo);
    }

    public static String validate(HttpServletRequest request) {
        try {
            String v = request.getHeader(SSO_HEADER_AUTH_NAME);
            if (StringUtils.isNotBlank(v)) {
                return SessionGenerator.fromId(v);
            }
        } catch (Exception e) {
            logger.error("failed to validate", e);
        }

        return null;
    }



    public static class SessionGenerator {
        public SessionGenerator() {
        }

        public static String generateId(String authInfo) {
            return EncryptUtils.aesEncrypt(parse2Str(authInfo)).toString();
        }

        public static String fromId(String sessionId) {
            String authInfoString = EncryptUtils.aesDecrypt(sessionId).toString();
            return fromStr(authInfoString);
        }

        private static String parse2Str(String authInfo) {
            return UUID.randomUUID().toString() + "|" + authInfo + "|" + System.currentTimeMillis();
        }

        private static String fromStr(String authInfoString) {
            String[] sp = authInfoString.split("\\|");
            return sp[1];
        }
    }
}
