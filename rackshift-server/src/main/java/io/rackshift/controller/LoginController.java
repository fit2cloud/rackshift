package io.rackshift.controller;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping("/signin")
    public ResultHolder login(@RequestBody JSONObject request) {
        String userName = request.getString("userName");
        String password = request.getString("password");
        if (StringUtils.isAnyBlank(userName, password)) {
            RSException.throwExceptions(Translator.get("parameter_error"));
        }
        AuthenticationToken authenticationToken = new UsernamePasswordToken(userName, password);
        Subject subject = SecurityUtils.getSubject();
        String msg = null;
        try {
            subject.login(authenticationToken);
            if (subject.isAuthenticated()) {
                return ResultHolder.success(subject.getSession().getAttribute("user"));
            } else {
                return ResultHolder.error(Translator.get("login_fail"));
            }
        } catch (ExcessiveAttemptsException e) {
            msg = Translator.get("excessive_attempts");
        } catch (LockedAccountException e) {
            msg = Translator.get("user_locked");
        } catch (DisabledAccountException e) {
            msg = Translator.get("user_has_been_disabled");
        } catch (ExpiredCredentialsException e) {
            msg = Translator.get("user_expires");
        } catch (AuthenticationException e) {
            msg = e.getMessage();
        } catch (UnauthorizedException e) {
            msg = Translator.get("not_authorized") + e.getMessage();
        }
        return ResultHolder.error(msg);
    }

    @RequestMapping("/isLogin")
    public ResultHolder isLogin() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return ResultHolder.success(LocaleContextHolder.getLocale());
        }
        return ResultHolder.error("");
    }

    @RequestMapping("/logout")
    public ResultHolder logout() {
        SecurityUtils.getSubject().logout();
        return ResultHolder.success("");
    }
}
