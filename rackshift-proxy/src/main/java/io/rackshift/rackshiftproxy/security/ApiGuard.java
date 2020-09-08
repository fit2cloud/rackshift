package io.rackshift.rackshiftproxy.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class ApiGuard {

    private static final String APISECRET = "rackshift-proxy-20200820";
    private static final String SIGNATURE = "signature";

    @Pointcut("execution(* io.rackshift.rackshiftproxy.controller.DHCPController.*(..))")
    public void pointCut() {

    }

    @Before("pointCut()")
    private void remoteCallCheck() throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (StringUtils.isBlank(request.getHeader(SIGNATURE))) {
            throw new RuntimeException("ilegal call with no apikey");
        }
        String timeStr = JWT.decode(request.getHeader(SIGNATURE)).getClaims().get("time").asString();
        if (StringUtils.isBlank(timeStr)) {
            throw new RuntimeException("ilegal call with no apikey");
        }
        long time = Long.valueOf(timeStr);
        if (System.currentTimeMillis() - time > 5 * 60 * 1000) {
            throw new RuntimeException("signature timeout!");
        }
    }
}
