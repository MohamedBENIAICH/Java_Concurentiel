package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TicketPool;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class AppWebSocketHandler extends TextWebSocketHandler {
    private TicketPool ticketPool;
    private final AtomicReference<WebSocketSession> currentSession = new AtomicReference<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AppWebSocketHandler() {
        // Default constructor
    }

    public void setTicketPool(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        WebSocketSession previousSession = currentSession.getAndSet(session);
        if (previousSession != null && previousSession.isOpen()) {
            try {
                previousSession.close();
                System.out.println("Previous session closed.");
            } catch (Exception e) {
                System.out.println("Failed to close previous session: " + e.getMessage());
            }
        }
        System.out.println("New session established: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        if (session.equals(currentSession.get())) {
            currentSession.set(null);
            System.out.println("Connection closed: " + session.getId() + ", Status: " + status);
        } else {
            System.out.println("Non-active session closed: " + session.getId() + ", Status: " + status);
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        if (session.equals(currentSession.get())) {
            System.out.println("Received message: " + message.getPayload());
            if ("status".equalsIgnoreCase(message.getPayload())) {
                sendTicketPoolStatus(session);
            } else if ("logs".equalsIgnoreCase(message.getPayload())) {
                sendLogs(session);
            }
        } else {
            System.out.println("Message received from non-active session: " + session.getId());
        }
    }

    @Scheduled(fixedRate = 1000) // Update every second
    public void broadcastTicketPoolStatus() {
        WebSocketSession session = currentSession.get();
        if (session != null && session.isOpen()) {
            try {
                sendTicketPoolStatus(session);
            } catch (Exception e) {
                System.out.println("Failed to broadcast ticket pool status: " + e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 1000) // Update logs every second
    public void broadcastLogs() {
        WebSocketSession session = currentSession.get();
        if (session != null && session.isOpen()) {
            try {
                sendLogs(session);
            } catch (Exception e) {
                System.out.println("Failed to broadcast logs: " + e.getMessage());
            }
        }
    }

    private void sendTicketPoolStatus(WebSocketSession session) throws Exception {
        Object statusObj = ticketPool.getStatus();
        if (statusObj instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> status = (java.util.Map<String, Object>) statusObj;
            String statusMessage = objectMapper.writeValueAsString(java.util.Map.of("type", "status", "data", status));
            session.sendMessage(new TextMessage(statusMessage));
        } else {
            System.out.println("Invalid ticket pool status format.");
        }
    }

    private void sendLogs(WebSocketSession session) throws Exception {
        java.util.List<String> logs = ticketPool.getLogs(); // Assuming TicketPool has a `getLogs` method
        if (logs != null) {
            String logsMessage = objectMapper.writeValueAsString(java.util.Map.of("type", "log", "data", logs));
            session.sendMessage(new TextMessage(logsMessage));
        } else {
            System.out.println("No logs available to send.");
        }
    }

}
