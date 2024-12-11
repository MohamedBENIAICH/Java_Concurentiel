package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.event.TicketPoolInitializedEvent;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.handler.AppWebSocketHandler;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TaskManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, ApplicationListener<TicketPoolInitializedEvent> {

    private final TaskManager taskManager;
    private final AppWebSocketHandler appWebSocketHandler;
    private boolean isInitialized = false;

    public WebSocketConfig(TaskManager taskManager, AppWebSocketHandler appWebSocketHandler) {
        this.taskManager = taskManager;
        this.appWebSocketHandler = appWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Register the WebSocket endpoint with a placeholder handler initially
        registry.addHandler(appWebSocketHandler, "/ws-native").setAllowedOrigins("*");
    }

    @Override
    public void onApplicationEvent(@NonNull TicketPoolInitializedEvent event) {
        if (!isInitialized) {
            isInitialized = true;

            // Update AppWebSocketHandler with the ticket pool
            appWebSocketHandler.setTicketPool(taskManager.getTicketPool());
        }
    }


}
