package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendTicket(String to, Ticket ticket) {
        StringBuilder message = new StringBuilder();
        message.append("Bonjour,\n\n");
        message.append("Merci pour votre achat chez ").append(ticket.getVendor()).append(".\n\n");
        message.append("Voici les détails de votre ticket :\n");
        message.append("-------------------------------------------------\n");
        message.append("Numéro du ticket : ").append(ticket.getTicketNo()).append("\n");
        message.append("Événement : ").append(ticket.getEventName()).append("\n");
        message.append("Lieu : ").append(ticket.getLocation()).append("\n");
        message.append("Prix : $").append(ticket.getTicketPrice()).append("\n");
        message.append("Date et heure d'achat : ").append(ticket.getTimestamp()).append("\n");
        message.append("-------------------------------------------------\n\n");
        message.append("Nous vous souhaitons un excellent événement !\n");
        message.append("Cordialement,\n");
        message.append("L'équipe de RealTimeEventTicketingSystem");

        sendSimpleMessage(to, "Votre ticket d'événement", message.toString());
    }


    public void sendTickets(String to, List<Ticket> tickets) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bonjour,\n\n");
        sb.append("Voici la liste complète de vos tickets achetés :\n\n");

        for (Ticket t : tickets) {
            sb.append("-------------------------------------------------\n");
            sb.append("Numéro du ticket : ").append(t.getTicketNo()).append("\n");
            sb.append("Événement : ").append(t.getEventName()).append("\n");
            sb.append("Lieu : ").append(t.getLocation()).append("\n");
            sb.append("Prix : $").append(t.getTicketPrice()).append("\n");
            sb.append("Date et heure d'achat : ").append(t.getTimestamp()).append("\n");
            sb.append("-------------------------------------------------\n\n");
        }

        sb.append("Merci pour votre confiance.\n");
        sb.append("Nous vous souhaitons de bons événements à venir.\n\n");
        sb.append("Cordialement,\n");
        sb.append("L'équipe de RealTimeEventTicketingSystem");

        sendSimpleMessage(to, "Vos tickets d'événement", sb.toString());
    }

}
