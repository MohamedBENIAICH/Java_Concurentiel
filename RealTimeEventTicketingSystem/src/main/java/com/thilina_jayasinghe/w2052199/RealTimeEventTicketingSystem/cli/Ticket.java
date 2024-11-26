package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

public class Ticket {
    private final int ticketNo;
    private final String vendorName;
    private double ticketPrice;
    private String timestamp;
    private String customerName;

    public Ticket(int ticketNo, String vendorName, double ticketPrice) {
        this.vendorName = vendorName;
        this.ticketNo = ticketNo;
        this.ticketPrice = ticketPrice;
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public String getVendorName() {
        return vendorName;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Ticket number " + getTicketNo() + ", released by " + getVendorName() + " was bought by " + getCustomerName() + " for a price of " + getTicketPrice() + ", on " + getTimestamp();
    }
}