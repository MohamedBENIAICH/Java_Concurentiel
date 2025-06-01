package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.TicketDTO;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:5173")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/save/ticket")
    public Ticket saveTicket(Ticket ticket) {
        return ticketService.saveTicket(ticket);
    }

    @GetMapping("/get/tickets")
    public List<TicketDTO> getTickets() {
        return ticketService.getTicketsDTO();
    }

    // Envoi mail ticket unique avec PDF
    @PostMapping("/send/ticket/{transactionId}/customer/{customerId}")
    public ResponseEntity<?> sendTicketToCustomer(
            @PathVariable int transactionId,
            @PathVariable int customerId,
            @RequestBody Map<String, String> request) {
        try {
            String pdfData = request.get("pdfData");
            if (pdfData == null) {
                return ResponseEntity.badRequest().body("PDF data is required");
            }
            ticketService.sendTicketToCustomer(transactionId, customerId, pdfData);
            return ResponseEntity.ok().body("Ticket sent successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send ticket: " + e.getMessage());
        }
    }

    // Envoi mail tous les tickets d'un client avec PDFs
    @PostMapping("/send/tickets/customer/{customerId}")
    public ResponseEntity<?> sendAllTicketsToCustomer(
            @PathVariable int customerId,
            @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> pdfDataArray = (List<Map<String, Object>>) request.get("pdfDataArray");
            if (pdfDataArray == null) {
                return ResponseEntity.badRequest().body("PDF data array is required");
            }
            ticketService.sendAllTicketsToCustomer(customerId, pdfDataArray);
            return ResponseEntity.ok().body("All tickets sent successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send tickets: " + e.getMessage());
        }
    }
}