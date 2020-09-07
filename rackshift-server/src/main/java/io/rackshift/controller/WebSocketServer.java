package io.rackshift.controller;

import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Vector;

@Component
@ServerEndpoint("/webSocket")
public class WebSocketServer {
    private static Vector<Session> sessionPools = new Vector();

    @OnOpen
    public void onOpen(Session session) {
        sessionPools.add(session);
    }

    public boolean sendMessage() {
        sessionPools.forEach(s -> {
            try {
                s.getBasicRemote().sendText("refresh");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
