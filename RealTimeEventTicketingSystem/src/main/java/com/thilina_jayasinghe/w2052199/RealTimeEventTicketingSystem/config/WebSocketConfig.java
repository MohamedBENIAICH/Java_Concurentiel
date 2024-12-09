package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.event.TicketPoolInitializedEvent;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.handler.AppWebSocketHandler;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TaskManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements ApplicationListener<TicketPoolInitializedEvent> {

    private final TaskManager taskManager;
    private final AppWebSocketHandler appWebSocketHandler; // Use a bean for the handler
    private final SimpleUrlHandlerMapping handlerMapping;

    private boolean isInitialized = false;

    public WebSocketConfig(TaskManager taskManager, AppWebSocketHandler appWebSocketHandler) {
        this.taskManager = taskManager;
        this.appWebSocketHandler = appWebSocketHandler;

        // Initialize the handler mapping
        this.handlerMapping = new SimpleUrlHandlerMapping();
        this.handlerMapping.setOrder(1); // Define order if needed
    }

    @Override
    public void onApplicationEvent(@NonNull TicketPoolInitializedEvent event) {
        System.out.println("TicketPool initialized. Dynamically registering WebSocket handler.");

        if (!isInitialized) {
            isInitialized = true;
            // Update AppWebSocketHandler with the ticket pool
            appWebSocketHandler.setTicketPool(taskManager.getTicketPool());

            // Dynamically add the WebSocket handler
            Map<String, WebSocketHandler> urlMap = new HashMap<>();
            urlMap.put("/ws-native", appWebSocketHandler);
            handlerMapping.setUrlMap(urlMap);

            System.out.println("WebSocket handler registered at /ws-native.");
        }
    }

    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        return handlerMapping; // Expose the dynamic mapping as a bean
    }


}
