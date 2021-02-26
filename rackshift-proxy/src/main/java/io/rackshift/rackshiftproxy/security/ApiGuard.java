package io.rackshift.rackshiftproxy.security;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    private static final String SIGNATURE = "signature";

    @Pointcut("execution(* io.rackshift.rackshiftproxy.controller.*.*(..))")
    public void pointCut() {

    }

    @Before("pointCut()")
    private void remoteCallCheck() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (StringUtils.isBlank(request.getHeader(SIGNATURE))) {
            throw new RuntimeException("ilegal call with no apikey");
        }
        long time = JWT.decode(request.getHeader(SIGNATURE)).getClaims().get("time").asLong();

        if (System.currentTimeMillis() - time > 5 * 60 * 1000) {
            throw new RuntimeException("signature timeout!");
        }
    }
}
