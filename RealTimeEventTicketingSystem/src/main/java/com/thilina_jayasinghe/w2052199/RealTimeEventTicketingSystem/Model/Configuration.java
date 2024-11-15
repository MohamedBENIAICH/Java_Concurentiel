package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model;

import jakarta.persistence.*;


@Entity
@Table(name = "config")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int eventId;
    private int totalTickets;
    private double ticketReleaseRate;
    private double customerRetrievalRate;
    private int maxTicketCapacity;

}
