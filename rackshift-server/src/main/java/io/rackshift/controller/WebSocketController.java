package io.rackshift.controller;

import io.rackshift.model.ResultHolder;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.websocket.Session;

@Controller
public class WebSocketController {

    @MessageMapping("/lifecycle")
    @SendTo("/topic/lifecycle")
    public ResultHolder greeting(String hello, Session session) throws Exception {
        return ResultHolder.success("from server : " + hello);
    }

    @MessageMapping("/discovery")
    @SendTo("/topic/discovery")
    public ResultHolder discovery(String hello, Session session) throws Exception {
        return ResultHolder.success("from server : " + hello);
    }

    @MessageMapping("/taskLifecycle")
    @SendTo("/topic/taskLifecycle")
    public ResultHolder taskLifecycle(String hello, Session session) throws Exception {
        return ResultHolder.success("from server : " + hello);
    }
}
