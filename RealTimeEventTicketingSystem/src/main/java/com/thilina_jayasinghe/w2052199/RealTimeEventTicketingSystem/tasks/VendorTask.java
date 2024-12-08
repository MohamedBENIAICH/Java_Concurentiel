package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Vendor;

import java.math.BigDecimal;

public class VendorTask implements Runnable {
    private TicketPool ticketPool;
    private double releaseInterval;
    private int totalTickets;
    private Vendor vendor;
    private BigDecimal ticketPrice;

    public VendorTask(Vendor vendor, TicketPool ticketPool, double releaseInterval) {
        this.vendor = vendor;
        this.ticketPrice = vendor.getTicketPrice();
        this.ticketPool = ticketPool;
        this.totalTickets = ticketPool.getTotalTickets();
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        try {
            while (ticketPool.getTicketCount().get()<totalTickets && !Thread.currentThread().isInterrupted()) {
                for (int i = 0; i < vendor.getTicketsPerRelease(); i++) {
                    Ticket ticket = new Ticket();
                    ticket.setVendor(vendor.getVendorName());
                    ticket.setTicketPrice(ticketPrice);
                    ticket.setEventName(vendor.getEventName());
                    ticket.setLocation(vendor.getLocation());
                    ticketPool.addTickets(ticket);
                }
                Thread.sleep((long) (releaseInterval*1000));
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
