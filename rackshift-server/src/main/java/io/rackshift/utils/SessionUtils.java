package io.rackshift.utils;

import io.rackshift.model.UserDTO;
import io.rackshift.mybatis.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class SessionUtils {
    public static UserDTO getUser() {
        Subject subject = SecurityUtils.getSubject();
        Session s = subject.getSession();
        return (UserDTO) s.getAttribute("user");
    }
}
