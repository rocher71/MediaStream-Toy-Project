package com.rocher.webmedia_back.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObjectMessageContainer {
    // 서버 -> 클라 보내는 메시지

    private String roomId;
    private String from;
    private String to;
    private MessageType type;
    private String messageId;

    private Object message; // 메시지에 any message 객체가 들어갈 수 있기에 Object type
}
