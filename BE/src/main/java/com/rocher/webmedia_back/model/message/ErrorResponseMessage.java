package com.rocher.webmedia_back.model.message;

import com.rocher.webmedia_back.model.ErrorCode;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponseMessage {
    private ErrorCode errorCode;
    private String message;
}
