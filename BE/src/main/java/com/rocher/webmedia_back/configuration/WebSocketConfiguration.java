package com.rocher.webmedia_back.configuration;

import com.rocher.webmedia_back.controller.WebMediaHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final WebMediaHandler webMediaHandler;

    @Autowired
    public WebSocketConfiguration(WebMediaHandler webMediaHandler) {
        this.webMediaHandler = webMediaHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webMediaHandler, "/webmedia-ws").setAllowedOrigins("*");
    }
}
