package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TaskManager;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:5173")
public class ConcurrencyLogController {
    @Autowired
    private TaskManager taskManager;

    // Endpoint pour les logs détaillés (par thread/vendeur)
    @GetMapping("/api/logs")
    public ResponseEntity<List<Map<String, Object>>> getLogs() {
        TicketPool pool = taskManager.getTicketPool();
        if (pool == null) return ResponseEntity.ok(Collections.emptyList());
        List<String> rawLogs = pool.getLogs();
        // On suppose que chaque log suit le format "[HH:mm:ss] [Thread] action status"
        // ou "Vendeur-X a publié le billet numéro Y"
        List<Map<String, Object>> logs = new ArrayList<>();
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        for (String line : rawLogs) {
            Map<String, Object> log = new HashMap<>();
            // Extraction simple : à adapter selon le format réel de tes logs
            String time = LocalDateTime.now().format(timeFmt);
            log.put("time", time);
            if (line.contains("a publié le billet numéro")) {
                String[] parts = line.split(" ");
                log.put("thread", parts[0]); // ex: Vendeur-1
                log.put("action", "Publication billet " + parts[5]);
                log.put("status", "SUCCESS");
            } else if (line.contains("File d'attente pleine")) {
                log.put("thread", "Système");
                log.put("action", "File pleine");
                log.put("status", "WAIT (verrou)");
            } else if (line.contains("Le vendeur a été notifié")) {
                log.put("thread", "Système");
                log.put("action", "Vendeur notifié");
                log.put("status", "NOTIFY");
            } else if (line.contains("File d'attente vide")) {
                log.put("thread", "Système");
                log.put("action", "File vide");
                log.put("status", "WAIT (vide)");
            } else {
                log.put("thread", "Système");
                log.put("action", line);
                log.put("status", "INFO");
            }
            logs.add(log);
        }
        return ResponseEntity.ok(logs);
    }

    // Endpoint pour les stats de concurrence (pour les graphes)
    @GetMapping("/api/concurrency-stats")
    public ResponseEntity<List<Map<String, Object>>> getConcurrencyStats() {
        TicketPool pool = taskManager.getTicketPool();
        if (pool == null) return ResponseEntity.ok(Collections.emptyList());
        List<String> rawLogs = pool.getLogs();
        // On va agréger par minute et compter le nombre d'actions "Publication billet" et le nombre de vendeurs actifs
        Map<String, Integer> minuteToTickets = new LinkedHashMap<>();
        Map<String, Set<String>> minuteToThreads = new LinkedHashMap<>();
        DateTimeFormatter minuteFmt = DateTimeFormatter.ofPattern("HH:mm");
        for (String line : rawLogs) {
            String minute = LocalDateTime.now().format(minuteFmt);
            if (line.contains("a publié le billet numéro")) {
                minuteToTickets.put(minute, minuteToTickets.getOrDefault(minute, 0) + 1);
                String thread = line.split(" ")[0];
                minuteToThreads.computeIfAbsent(minute, k -> new HashSet<>()).add(thread);
            }
        }
        List<Map<String, Object>> stats = new ArrayList<>();
        for (String minute : minuteToTickets.keySet()) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("minute", minute);
            stat.put("concurrents", minuteToThreads.getOrDefault(minute, Collections.emptySet()).size());
            stat.put("tickets", minuteToTickets.getOrDefault(minute, 0));
            stats.add(stat);
        }
        return ResponseEntity.ok(stats);
    }
}
