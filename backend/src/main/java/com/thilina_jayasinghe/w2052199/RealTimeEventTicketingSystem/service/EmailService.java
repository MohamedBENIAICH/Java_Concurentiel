package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Méthode simple pour les messages basiques
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email: " + e.getMessage(), e);
        }
    }

    // Envoi d'un ticket avec PDF
    public void sendTicketWithPDF(String to, Ticket ticket, String pdfBase64) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Votre ticket d'événement - " + ticket.getEventName());

            // Corps du message
            StringBuilder messageContent = new StringBuilder();
            messageContent.append("Bonjour,\n\n");
            messageContent.append("Merci pour votre achat chez ").append(ticket.getVendor()).append(".\n\n");
            messageContent.append("Voici les détails de votre ticket :\n");
            messageContent.append("-------------------------------------------------\n");
            messageContent.append("Numéro du ticket : ").append(ticket.getTicketNo()).append("\n");
            messageContent.append("Événement : ").append(ticket.getEventName()).append("\n");
            messageContent.append("Lieu : ").append(ticket.getLocation()).append("\n");
            messageContent.append("Prix : $").append(ticket.getTicketPrice()).append("\n");
            messageContent.append("Date et heure d'achat : ").append(ticket.getTimestamp()).append("\n");
            messageContent.append("-------------------------------------------------\n\n");
            messageContent.append("Veuillez trouver votre ticket en pièce jointe.\n\n");
            messageContent.append("Nous vous souhaitons un excellent événement !\n");
            messageContent.append("Cordialement,\n");
            messageContent.append("L'équipe de RealTimeEventTicketingSystem");

            helper.setText(messageContent.toString());

            // Attacher le PDF
            byte[] pdfBytes = Base64.getDecoder().decode(pdfBase64);
            helper.addAttachment(
                    "ticket_" + ticket.getTicketNo() + ".pdf",
                    new ByteArrayResource(pdfBytes)
            );

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi du ticket avec PDF: " + e.getMessage(), e);
        }
    }

    // Envoi de plusieurs tickets avec PDFs
    public void sendTicketsWithPDFs(String to, List<Map<String, Object>> ticketsWithPDFs) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Vos tickets d'événement");

            StringBuilder messageContent = new StringBuilder();
            messageContent.append("Bonjour,\n\n");
            messageContent.append("Voici la liste complète de vos tickets achetés :\n\n");

            for (Map<String, Object> ticketData : ticketsWithPDFs) {
                Ticket ticket = (Ticket) ticketData.get("ticket");
                String pdfBase64 = (String) ticketData.get("pdfData");

                messageContent.append("-------------------------------------------------\n");
                messageContent.append("Numéro du ticket : ").append(ticket.getTicketNo()).append("\n");
                messageContent.append("Événement : ").append(ticket.getEventName()).append("\n");
                messageContent.append("Lieu : ").append(ticket.getLocation()).append("\n");
                messageContent.append("Prix : $").append(ticket.getTicketPrice()).append("\n");
                messageContent.append("Date et heure d'achat : ").append(ticket.getTimestamp()).append("\n");
                messageContent.append("-------------------------------------------------\n\n");

                // Attacher le PDF
                byte[] pdfBytes = Base64.getDecoder().decode(pdfBase64);
                helper.addAttachment(
                        "ticket_" + ticket.getTicketNo() + ".pdf",
                        new ByteArrayResource(pdfBytes)
                );
            }

            messageContent.append("Merci pour votre confiance.\n");
            messageContent.append("Nous vous souhaitons de bons événements à venir.\n\n");
            messageContent.append("Cordialement,\n");
            messageContent.append("L'équipe de RealTimeEventTicketingSystem");

            helper.setText(messageContent.toString());
            mailSender.send(message);
        } catch (MessagingException e) {  // Corrigé ici : un seul catch pour MessagingException
            throw new RuntimeException("Erreur lors de l'envoi des tickets avec PDFs: " + e.getMessage(), e);
        }
    }

    // Méthodes existantes pour la rétrocompatibilité
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