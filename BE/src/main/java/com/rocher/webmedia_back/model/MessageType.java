package com.rocher.webmedia_back.model;

public enum MessageType {
    JoinRequest,

    JoinResponse,
    ErrorResponse,

    UserPublishedChangeReport,

    UserJoinedEvent,
    UserLeftEvent,
    UserStateChangedEvent;
}
