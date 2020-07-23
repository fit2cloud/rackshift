package io.rackshift.utils;

import io.rackshift.model.UserDTO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class SessionUtil {
    public static UserDTO getUser() {
        try {
            Subject subject = SecurityUtils.getSubject();
            UserDTO userDTO = (UserDTO) subject.getSession().getAttribute("user");
            return userDTO;
        } catch (Exception e) {
            return null;
        }
    }
}
