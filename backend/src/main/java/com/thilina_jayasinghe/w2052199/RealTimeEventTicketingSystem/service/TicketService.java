package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.CustomerRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;  // <-- Assure-toi que c'est là

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTickets() {
        List<Ticket> ticketList = new ArrayList<>();
        ticketRepository.findAll().forEach(ticketList::add);
        return ticketList;
    }
    @Autowired
    private EmailService emailService;

    // Envoie un ticket spécifique par email au client
    public void sendTicketToCustomer(int transactionId, int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec cet ID : " + customerId));

        Ticket ticket = ticketRepository.findByTransactionIdAndCustomer(transactionId, customer)
                .orElseThrow(() -> new RuntimeException("Ticket non trouvé pour ce client avec cet ID : " + transactionId));

        emailService.sendTicket(customer.getCustomerEmail(), ticket);
    }

    // Envoie tous les tickets achetés par un client par email
    public void sendAllTicketsToCustomer(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec cet ID : " + customerId));

        List<Ticket> tickets = ticketRepository.findByCustomer(customer);

        if (tickets.isEmpty()) {
            throw new RuntimeException("Aucun ticket trouvé pour ce client.");
        }

        emailService.sendTickets(customer.getCustomerEmail(), tickets);
    }

}
