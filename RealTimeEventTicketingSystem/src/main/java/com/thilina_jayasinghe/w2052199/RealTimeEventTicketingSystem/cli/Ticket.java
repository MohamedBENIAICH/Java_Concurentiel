package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

public class Ticket {
    private int ticketNo;
    private Vendor vendor;
    private double ticketPrice;

    public Ticket(int ticketNo, Vendor vendor, double ticketPrice) {
        this.vendor = vendor;
        this.ticketNo = ticketNo;
        this.ticketPrice = ticketPrice;
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}