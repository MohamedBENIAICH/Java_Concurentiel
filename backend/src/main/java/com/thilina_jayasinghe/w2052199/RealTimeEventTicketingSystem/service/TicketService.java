package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.TicketDTO;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.CustomerRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<TicketDTO> getTicketsDTO() {
        List<TicketDTO> ticketDTOList = new ArrayList<>();
        ticketRepository.findAll().forEach(ticket -> {
            ticketDTOList.add(new TicketDTO(
                ticket.getTransactionId(),
                ticket.getTicketNo(),
                ticket.getVendor(),
                ticket.getEventName(),
                ticket.getLocation(),
                ticket.getCustomerId(),
                ticket.getCustomer() != null ? ticket.getCustomer().getCustomerName() : "",
                ticket.getTicketPrice(),
                ticket.getTimestamp()
            ));
        });
        return ticketDTOList;
    }

    public List<Ticket> getTickets() {
        List<Ticket> ticketList = new ArrayList<>();
        ticketRepository.findAll().forEach(ticketList::add);
        return ticketList;
    }

    // Envoi d'un ticket avec PDF
    public void sendTicketToCustomer(int transactionId, int customerId, String pdfData) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec cet ID : " + customerId));

        Ticket ticket = ticketRepository.findByTransactionIdAndCustomer(transactionId, customer)
                .orElseThrow(() -> new RuntimeException("Ticket non trouvé pour ce client avec cet ID : " + transactionId));

        emailService.sendTicketWithPDF(customer.getCustomerEmail(), ticket, pdfData);
    }

    // Envoi de tous les tickets avec PDFs
    public void sendAllTicketsToCustomer(int customerId, List<Map<String, Object>> pdfDataArray) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec cet ID : " + customerId));

        List<Ticket> customerTickets = ticketRepository.findByCustomer(customer);

        if (customerTickets.isEmpty()) {
            throw new RuntimeException("Aucun ticket trouvé pour ce client.");
        }

        if (customerTickets.size() != pdfDataArray.size()) {
            throw new RuntimeException("Le nombre de tickets ne correspond pas au nombre de PDFs fournis.");
        }

        // Préparer les données pour l'envoi
        List<Map<String, Object>> ticketsWithPDFs = new ArrayList<>();
        for (int i = 0; i < customerTickets.size(); i++) {
            Map<String, Object> ticketData = new HashMap<>();
            ticketData.put("ticket", customerTickets.get(i));
            ticketData.put("pdfData", pdfDataArray.get(i).get("pdfData"));
            ticketsWithPDFs.add(ticketData);
        }

        emailService.sendTicketsWithPDFs(customer.getCustomerEmail(), ticketsWithPDFs);
    }

    // Méthodes existantes pour la rétrocompatibilité
    public void sendTicketToCustomer(int transactionId, int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec cet ID : " + customerId));

        Ticket ticket = ticketRepository.findByTransactionIdAndCustomer(transactionId, customer)
                .orElseThrow(() -> new RuntimeException("Ticket non trouvé pour ce client avec cet ID : " + transactionId));

        emailService.sendTicket(customer.getCustomerEmail(), ticket);
    }

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