package com.rocher.webmedia_back.model.message;

import com.rocher.webmedia_back.model.RoomUser;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserJoinedEventMessage {
    private RoomUser user;

}
