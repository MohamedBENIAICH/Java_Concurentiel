package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class TicketController {

    @Autowired
    private TicketService ticketSalesService;

    @PostMapping("/save/ticket")
    public Ticket saveTicketSales(Ticket ticket) {
        return ticketSalesService.saveTicketSales(ticket);
    }

    @GetMapping("/get/tickets")
    public List<Ticket> getTickets() {
        return ticketSalesService.getTickets();
    }

}
