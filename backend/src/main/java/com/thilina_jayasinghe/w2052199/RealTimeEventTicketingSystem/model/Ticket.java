package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(int ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Ticket(int ticketNo, int transactionId, String vendor, String eventName, String location, Customer customer, BigDecimal ticketPrice, Timestamp timestamp) {
        this.ticketNo = ticketNo;
        this.transactionId = transactionId;
        this.vendor = vendor;
        this.eventName = eventName;
        this.location = location;
        this.customer = customer;
        this.ticketPrice = ticketPrice;
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Ticket() {
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    private int ticketNo;

    private String vendor;

    private String eventName;

    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    private BigDecimal ticketPrice;

    private Timestamp timestamp;

    public Integer getCustomerId() {
        return customer != null ? customer.getCustomerId() : null;
    }
    @Override
    public String toString() {
        return "Ticket No." + ticketNo +
                " released by " + vendor +
                " for " + eventName +
                " event at " + location +
                " was purchased by " + (customer != null ? customer.getCustomerName() : "unknown") +
                " for $" + ticketPrice +
                " on " + timestamp;
    }
}
