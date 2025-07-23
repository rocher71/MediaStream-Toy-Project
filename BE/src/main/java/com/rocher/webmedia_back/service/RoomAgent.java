package com.rocher.webmedia_back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocher.webmedia_back.model.ErrorCode;
import com.rocher.webmedia_back.model.MessageType;
import com.rocher.webmedia_back.model.RoomUser;
import com.rocher.webmedia_back.model.message.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoomAgent {
    private static final int MaxUsers = 2;
    private static final String ApiUrl = "http://localhost:1985";
    private static final String StreamUrl = "webrtc://localhost";

    private final ObjectMapper objectMapper;
    private final MessageSender messageSender;
    private final String roomId;

    private final Object lockObj;
    private final Map<String, RoomUser> roomUserMap;
    private int userIdCounter;

    public RoomAgent(ObjectMapper objectMapper, MessageSender messageSender, String roomId) {
        this.objectMapper = objectMapper;
        this.messageSender = messageSender;
        this.roomId = roomId;

        this.lockObj = new Object();
        this.roomUserMap = new HashMap<>();
        this.userIdCounter = 0;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getUserCount(){
        synchronized (lockObj){
            return roomUserMap.size();
        }
    }

    public void handleUserLeft(WebSocketSession session)throws Exception{

    }

    public void handleMessage(WebSocketSession session, String messageId, MessageType type, String messageStr) throws Exception {
        synchronized (lockObj){
            if(MessageType.JoinRequest.equals(type)){
                final JoinRequestMessage message = objectMapper.readValue(messageStr, JoinRequestMessage.class);

                handleUserJoined(session, messageId, message);
            } else if(MessageType.UserPublishedChangeReport.equals(type)){
                final UserPublishedChangeReport message = objectMapper.readValue(messageStr, UserPublishedChangeReport.class);
            } else {
                final ErrorResponseMessage response = ErrorResponseMessage.builder()
                        .errorCode(ErrorCode.BadRequest)
                        .message("잘못된 요청입니다")
                        .build();

                messageSender.sendTransactionMessage(session, roomId, "guest", messageId, MessageType.ErrorResponse, response);
            }
        }
    }

    private void handleUserJoined(WebSocketSession session, String messageId, JoinRequestMessage message) throws Exception {
        if(!roomId.equals(message.getRoomId())){
            final ErrorResponseMessage response = ErrorResponseMessage.builder()
                    .errorCode(ErrorCode.BadRequest)
                    .message("잘못된 요청입니다")
                    .build();

            messageSender.sendTransactionMessage(session, roomId, "guest", messageId, MessageType.ErrorResponse, response);
            return;
        }

        if(roomUserMap.size() >= MaxUsers){
            final ErrorResponseMessage response = ErrorResponseMessage.builder()
                    .errorCode(ErrorCode.TooManyUsers)
                    .message("방에 인원이 너무 많습니다")
                    .build();

            messageSender.sendTransactionMessage(session, roomId, "guest", messageId, MessageType.ErrorResponse, response);
            return;
        }

        final String userID = "user" + this.userIdCounter++;
        final RoomUser user = RoomUser.builder()
                .roomId(roomId)
                .userId(userID)
                .published(false)
                .session(session)
                .build();
        final RoomUser anotherUser = this.getAnotherUser(user);
        final JoinResponseMessage response = JoinResponseMessage.builder()
                .apiUrl(ApiUrl)
                .streamUrl(StreamUrl)
                .roomId(roomId)
                .user(user)
                .anotherUser(anotherUser)
                .build();
        roomUserMap.put(session.getId(), user);

        messageSender.sendTransactionMessage(session, roomId, userID, messageId, MessageType.JoinResponse, response);

        if(anotherUser != null){
            final UserJoinedEventMessage eventMessage = UserJoinedEventMessage.builder()
                    .user(user)
                    .build();

            messageSender.sendEventMessage(anotherUser.getSession(), roomId, MessageType.UserJoinedEvent, eventMessage);
        }
    }

    private RoomUser getAnotherUser(RoomUser user){
        final Optional<RoomUser> anotherUserOptional = roomUserMap.values().stream()
                .filter(u -> !u.getUserId().equals(user.getUserId()))
                .findFirst();

        return  anotherUserOptional.orElse(null);
    }
}
