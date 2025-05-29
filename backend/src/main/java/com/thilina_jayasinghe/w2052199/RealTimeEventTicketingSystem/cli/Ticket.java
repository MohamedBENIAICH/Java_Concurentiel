package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.math.BigDecimal;

public class Ticket {
    private final int ticketNo;
    private final String vendorName;
    private String eventName;
    private String location;
    private BigDecimal ticketPrice;
    private String timestamp;
    private String customerName;

    public Ticket(int ticketNo, String vendorName, String eventName, String location, BigDecimal ticketPrice) {
        this.vendorName = vendorName;
        this.ticketNo = ticketNo;
        this.eventName = eventName;
        this.location = location;
        this.ticketPrice = ticketPrice;
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public String getVendorName() {
        return vendorName;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    /**
     * Method that returns details of ticket object
     * @return String value containing Ticket instance attributes arranged meaningfully
     */
    @Override
    public String toString() {
        return "Ticket number " + getTicketNo() + ", released by " + getVendorName() + " was bought by " + getCustomerName() + " for a price of " + getTicketPrice() + ", on " + getTimestamp();
    }
}