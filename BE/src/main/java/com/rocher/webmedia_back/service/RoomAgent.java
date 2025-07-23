package com.rocher.webmedia_back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocher.webmedia_back.model.MessageType;
import org.springframework.web.socket.WebSocketSession;

public class RoomAgent {
    public RoomAgent(ObjectMapper objectMapper, MessageSender messageSender, String roomId) {

    }

    public String getRoomId() {
        return null;
    }

    public int getUserCount(){
        return 0;
    }

    public void handleUserLeft(WebSocketSession session)throws Exception{

    }

    public void handleMessage(WebSocketSession session, String messageId, MessageType type, String messageStr) throws Exception {

    }
}
