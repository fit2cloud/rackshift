package io.rackshift.controller;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
        subject.login(authenticationToken);
        if (subject.isAuthenticated()) {
            return ResultHolder.success(subject.getSession().getAttribute("user"));
        } else {
            return ResultHolder.error(Translator.get("login_fail"));
        }
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
