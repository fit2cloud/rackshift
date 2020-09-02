package io.rackshift.config;

import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(RSException.class)
    @ResponseBody
    public ResultHolder resultHolder(RSException e) {
        return ResultHolder.error(e.getMessage());
    }
}
