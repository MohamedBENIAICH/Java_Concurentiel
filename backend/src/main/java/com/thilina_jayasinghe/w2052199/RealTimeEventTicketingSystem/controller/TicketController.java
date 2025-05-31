package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Ticket> getTickets() {
        return ticketService.getTickets();
    }
    // Envoi mail ticket unique
    @PostMapping("/send/ticket/{transactionId}/customer/{customerId}")
    public void sendTicketToCustomer(@PathVariable int transactionId, @PathVariable int customerId) {
        ticketService.sendTicketToCustomer(transactionId, customerId);
    }

    // Envoi mail tous les tickets d’un client
    @PostMapping("/send/tickets/customer/{customerId}")
    public void sendAllTicketsToCustomer(@PathVariable int customerId) {
        ticketService.sendAllTicketsToCustomer(customerId);
    }

}
