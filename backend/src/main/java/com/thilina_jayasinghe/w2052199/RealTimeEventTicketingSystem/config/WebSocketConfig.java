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

    /**
     * Registers WebSocket endpoints with the application by configuring a WebSocket endpoint
     * for the application by registering it with a web socket handler.
     * appWebSocketHandler is responsible for managing connections and messaging.
     * @param registry WebSocketHandlerRegistry object used to register WebSocket handlers
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Register the WebSocket endpoint with a placeholder handler initially
        registry.addHandler(appWebSocketHandler, "/ws-native").setAllowedOrigins("http://localhost:4200");
    }

    /**
     * Handles the initialization of the WebSocket handler on the event of ticket pool initialization.
     * This method listens for `TicketPoolInitializedEvent` and upon receiving this event,
     * it sets the newly created ticketPool object to the web socket handler
     * @param event an object representing the event triggered when the ticket pool is initialized.
     */
    @Override
    public void onApplicationEvent(@NonNull TicketPoolInitializedEvent event) {
        if (!isInitialized) {
            isInitialized = true;

            // Update AppWebSocketHandler with the ticket pool
            appWebSocketHandler.setTicketPool(taskManager.getTicketPool());
        }
    }


}
