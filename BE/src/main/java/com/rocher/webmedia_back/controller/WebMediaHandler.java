package com.rocher.webmedia_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocher.webmedia_back.model.MessageType;
import com.rocher.webmedia_back.model.StringMessageContainer;
import com.rocher.webmedia_back.service.MessageSender;
import com.rocher.webmedia_back.service.RoomAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebMediaHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final MessageSender messageSender;

    private final Object lockObj;
    private final Map<String, RoomAgent> agentMap;

    public WebMediaHandler(ObjectMapper objectMapper, MessageSender messageSender){
        this.objectMapper = objectMapper;
        this.messageSender = messageSender;

        this.lockObj = new Object();
        this.agentMap = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("Connection established : sessionId={}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        log.debug("Connection closed : sessionId={}, status={}", session.getId(), status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final String payload = message.getPayload();

        try{
            final StringMessageContainer messageContainer = objectMapper.readValue(payload, StringMessageContainer.class);
            RoomAgent agent = null;

            if(MessageType.JoinRequest.equals(messageContainer.getType())){
                final String roomID = messageContainer.getRoomId();

                // 특정 순간 단 하나의 쓰레드만 접근, 실행 가능. Crytical Session으로 만드는 역핧
                synchronized (lockObj){
                    if(agentMap.containsKey(roomID)){
                        agent = agentMap.get(roomID);
                    } else {
                        agent = new RoomAgent(objectMapper, messageSender, roomID);
                        agentMap.put(roomID, agent);
                    }
                }
            } else {
                synchronized (lockObj){
                    agent = agentMap.get(messageContainer.getRoomId());
                }
            }
            agent.handleMessage(session, messageContainer.getMessageId(), messageContainer.getType(), messageContainer.getMessage());
        } catch(Exception e){
            log.debug("handleTextMessage error", e);

            session.close(new CloseStatus(3000, "알 수 없는 에러"));
        }
    }
}
