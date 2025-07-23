package com.rocher.webmedia_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RoomUser {
    private String roomId;
    private String userId;

    private boolean published;
    
    @JsonIgnore // json 변환 시 무시
    private WebSocketSession session;
}
