package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "ticket_sales")
public class TicketSales {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;

    @ManyToOne
    @JoinColumn(name = "vendorId")
    private Vendor vendor;

    private int ticketNo;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    private double ticketPrice;
    private Timestamp timestamp;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(int ticketNo) {
        this.ticketNo = ticketNo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
