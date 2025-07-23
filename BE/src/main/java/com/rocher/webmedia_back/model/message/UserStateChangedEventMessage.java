package com.rocher.webmedia_back.model.message;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserStateChangedEventMessage {
    private String userId;
    private boolean published;
}
