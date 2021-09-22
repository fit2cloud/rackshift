package io.rackshift.config;

import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResultHolder resultHolder(RuntimeException e) {
        if (e instanceof UnauthenticatedException) {
            return null;
        }
        return ResultHolder.error(e.getMessage());
    }
}
