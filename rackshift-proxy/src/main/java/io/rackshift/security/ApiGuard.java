package io.rackshift.rackshiftproxy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class ApiGuard {

    private static final String APISECRET = "rackshift-proxy-20200820";
    private static final String SIGNATURE = "signature";

    @Pointcut("execution(io.rackshift.rackshiftproxy.controller..*)")
    private void remoteCallCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (StringUtils.isBlank(request.getHeader(SIGNATURE))) {
            throw new RuntimeException("ilegal call with no apikey");
        }

        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(APISECRET)
                //设置需要解析的jwt
                .parseClaimsJws(request.getHeader(SIGNATURE)).getBody();

        String user = claims.getSubject();
        //:todo
        joinPoint.proceed();
    }
}
