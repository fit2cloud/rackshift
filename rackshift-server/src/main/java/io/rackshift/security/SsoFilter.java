package io.rackshift.security;

import io.rackshift.utils.LogUtil;
import io.rackshift.utils.SsoSessionHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liqiang on 2018/6/7
 */
public class SsoFilter extends AnonymousFilter {

    public static final String SSO_ERROR_COOKIE_NAME = "rememberme_sso_error";

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            if (isApiKeyCall(WebUtils.toHttp(request))) {
                String userId = ApiKeyHandler.getUser(WebUtils.toHttp(request));
                if (userId == null) {
                    ((HttpServletResponse) response).setHeader("Authentication-Status", "invalid");
                } else {
                    SecurityUtils.getSubject().login(new UsernamePasswordToken(userId, SsoSessionHandler.random));
                }
            }

            if (!SecurityUtils.getSubject().isAuthenticated()) {
                ((HttpServletResponse) response).setHeader("Authentication-Status", "invalid");
            }
        } catch (Exception e) {
            if (isApiKeyCall(WebUtils.toHttp(request))) {
                throw e;
            }
            LogUtil.getLogger().error("failed to handle single sign on..", e);
        }

        return true;
    }

    private boolean isApiKeyCall(HttpServletRequest toHttp) {
        if (toHttp == null) {
            return false;
        }
        if (toHttp.getHeader("accessKey") == null || toHttp.getHeader("signature") == null) {
            return false;
        }
        return true;
    }
}