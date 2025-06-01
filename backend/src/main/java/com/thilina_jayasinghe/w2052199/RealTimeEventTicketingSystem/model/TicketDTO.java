package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TicketDTO {
    private int transactionId;
    private int ticketNo;
    private String vendor;
    private String eventName;
    private String location;
    private Integer customerId;
    private String customer;
    private BigDecimal ticketPrice;
    private Timestamp timestamp;

    public TicketDTO(int transactionId, int ticketNo, String vendor, String eventName, String location, Integer customerId, String customer, BigDecimal ticketPrice, Timestamp timestamp) {
        this.transactionId = transactionId;
        this.ticketNo = ticketNo;
        this.vendor = vendor;
        this.eventName = eventName;
        this.location = location;
        this.customerId = customerId;
        this.customer = customer;
        this.ticketPrice = ticketPrice;
        this.timestamp = timestamp;
    }

    public int getTransactionId() { return transactionId; }
    public int getTicketNo() { return ticketNo; }
    public String getVendor() { return vendor; }
    public String getEventName() { return eventName; }
    public String getLocation() { return location; }
    public Integer getCustomerId() { return customerId; }
    public String getCustomer() { return customer; }
    public BigDecimal getTicketPrice() { return ticketPrice; }
    public Timestamp getTimestamp() { return timestamp; }
}
