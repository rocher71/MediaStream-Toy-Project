package com.rocher.webmedia_back.model.message;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLeftEventMessage {
    private String userId;
}
