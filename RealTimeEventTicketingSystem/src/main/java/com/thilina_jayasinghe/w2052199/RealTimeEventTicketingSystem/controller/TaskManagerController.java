package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
public class TaskManagerController {

    @Autowired
    private TaskManager taskManager;

    @PostMapping("/start")
    public ResponseEntity<String> startThreads() {
        taskManager.startThreads();
        return ResponseEntity.ok("Threads started.");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopThreads() {
        taskManager.stopThreads();
        return ResponseEntity.ok("Threads stopped.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetLogs() {
        taskManager.getTicketPool().clearLogs();
        return ResponseEntity.ok("Logs reset.");
    }

}
