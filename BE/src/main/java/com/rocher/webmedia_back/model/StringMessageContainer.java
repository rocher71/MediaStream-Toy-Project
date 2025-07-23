package com.rocher.webmedia_back.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rocher.webmedia_back.service.JsonStringMessageDeserializer;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 어플리케이션 프로토콜을 나타내는 class
public class StringMessageContainer {
    // application protocol의 header
    // 클라, 서버 간 소통시 원활한 작동을 위해 필요한 부가적인 정보
    private String roomId;
    private String from;
    private String to;
    private String messageId;

    // application protocol의 body
    // 실제 받고자 하는 데이터(메시지)
    @JsonDeserialize(using = JsonStringMessageDeserializer.class)
    private String message;
}
