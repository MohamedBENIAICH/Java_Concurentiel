package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:4200")
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TaskManager taskManager;

    @Scheduled(fixedRate = 1000) // Update every second
    public void sendStatusUpdates() {
        Map<String, Object> status = new HashMap<>();
        status.put("isRunning", taskManager.isRunning());
        status.put("vendorThreads", taskManager.getRunningVendorThreads());
        status.put("customerThreads", taskManager.getRunningCustomerThreads());
        status.put("ticketPoolStatus", taskManager.getTicketPool().getStatus()); // Assume `getStatus` gives ticket details

        messagingTemplate.convertAndSend("/topic/status", status);
    }
}
