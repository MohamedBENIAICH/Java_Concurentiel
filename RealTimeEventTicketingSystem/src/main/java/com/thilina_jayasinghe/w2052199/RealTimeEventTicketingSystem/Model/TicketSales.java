package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "ticket_sales")
public class TicketSales {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;

    @OneToMany
    @JoinColumn(name = "vendorId")
    private Vendor vendor;

    private int ticketNo;

    @OneToMany
    @JoinColumn(name = "customerId")
    private Customer customer;
    private double ticketPrice;
    private Timestamp timestamp;

}
