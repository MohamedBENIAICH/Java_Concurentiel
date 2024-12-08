package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.handler.AppWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new AppWebSocketHandler(), "/ws-native")
                .setAllowedOrigins("http://localhost:4200");
    }
}
