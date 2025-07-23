package com.rocher.webmedia_back.model.message;

import com.rocher.webmedia_back.model.RoomUser;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinResponseMessage {
    private String apiUrl;
    private String streamUrl;
    private String roomId;
    private RoomUser user;
    private RoomUser anotherUser;
}
