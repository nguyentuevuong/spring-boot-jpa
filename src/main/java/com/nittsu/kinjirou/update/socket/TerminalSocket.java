package com.nittsu.kinjirou.update.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class TerminalSocket extends TextWebSocketHandler {
    @Autowired
    private TerminalService terminalService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        terminalService.setSession(session).initialize();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        terminalService.execCommand(message.getPayload());
    }
}
