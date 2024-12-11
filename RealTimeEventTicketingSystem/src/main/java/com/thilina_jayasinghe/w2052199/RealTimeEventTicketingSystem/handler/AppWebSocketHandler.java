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

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AppWebSocketHandler extends TextWebSocketHandler {
    private TicketPool ticketPool;
    private final AtomicReference<WebSocketSession> currentSession = new AtomicReference<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Sets the `TicketPool` object for the WebSocket handler enabling real-time updates
     * of ticket pool status and logs through WebSocket messages.
     * @param ticketPool TicketPool object to associate with this web socket handler
     */
    public void setTicketPool(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
        System.out.println("TicketPool set in WebSocketHandler.");
    }

    /**
     * Manages the active WebSocket session by replacing any existing previous session with
     * the new session. If a previous session exists, it is closed and new connection is set.
     * @param session WebSocketSession object representing the newly established session
     */
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

    /**
     * Updates the active session state after connection is closed.
     * @param session session object that was just closed
     * @param status denotes reason for the closure of connection
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        if (session.equals(currentSession.get())) {
            currentSession.set(null);
            System.out.println("Connection closed: " + session.getId() + ", Status: " + status);
        } else {
            System.out.println("Non-active session closed: " + session.getId() + ", Status: " + status);
        }
    }

    /**
     *  Processes messages received from the WebSocket connection.
     * @param session WebSocket session from which message was received
     * @param message Message received
     * @throws Exception handles exceptions thrown when handling messages or sending responses
     */
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

    /**
     * Broadcasts the ticket pool status to the active WebSocket session  every second
     * to provide real-time updates on ticketPoolStatus`
     */
    @Scheduled(fixedRate = 1000)
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

    /**
     * Broadcasts ticket pool logs to the active WebSocket session every second to provide real-time logs of threads
     */
    @Scheduled(fixedRate = 1000)
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

    /**
     * Sends the current status of the ticket pool to the specified WebSocket session by retrieving
     * the ticket pool's status and sending it as a JSON-formatted
     * message through the WebSocket session.
     * @param session target WebSocketSession
     * @throws Exception handles exceptions that occur during message serialization or transmission.
     */
    private void sendTicketPoolStatus(WebSocketSession session) throws Exception {
        if (ticketPool != null) {
            Object statusObj = ticketPool.getStatus();
            if (statusObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> status = (Map<String, Object>) statusObj;
                String statusMessage = objectMapper.writeValueAsString(Map.of("type", "status", "data", status));
                session.sendMessage(new TextMessage(statusMessage));
            } else {
                System.out.println("Invalid ticket pool status format.");
            }
        } else {
            System.out.println("TicketPool is not initialized yet.");
        }
    }

    /**
     * Sends the ticket pool logs to the specified WebSocket session by retrieving logs from the ticket pool
     * and sending them as JSON-formatted
     * messages through the WebSocket session.
     * @param session target WebSocketSession
     * @throws Exception handles exceptions that occur during message serialization or transmission.
     */
    private void sendLogs(WebSocketSession session) throws Exception {
        if (ticketPool != null) {
            List<String> logs = ticketPool.getLogs();
            if (logs != null) {
                String logsMessage = objectMapper.writeValueAsString(Map.of("type", "log", "data", logs));
                session.sendMessage(new TextMessage(logsMessage));

            } else {
                System.out.println("No logs available to send.");
            }
        } else {
            System.out.println("TicketPool is not initialized yet.");
        }
    }

}
