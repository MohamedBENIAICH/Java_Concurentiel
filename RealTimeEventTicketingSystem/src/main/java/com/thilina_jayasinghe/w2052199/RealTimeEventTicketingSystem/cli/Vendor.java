package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.logging.Logger;

public class Vendor extends User implements Runnable {
    private TicketPool ticketPool;
    private static double ticketReleaseRate = Objects.requireNonNull(GsonSerializer.deserializeConfig()).getTicketReleaseRate();
    private static int totalTickets = Objects.requireNonNull(GsonSerializer.deserializeConfig()).getTotalTickets();
    private String eventName;
    private String location;
    private BigDecimal ticketPrice;
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());

    public Vendor(String companyName, String address, String email, String telNo, String eventName, String location, BigDecimal ticketPrice, TicketPool ticketPool) {
        super(companyName, address, email, telNo);
        this.ticketPool = ticketPool;
        this.eventName = eventName;
        this.location = location;
        this.ticketPrice = ticketPrice;
    }

    @Override
    public void run() {
        try {
            while (ticketPool.getTicketCount().get()<totalTickets && !Thread.currentThread().isInterrupted()) {
                ticketPool.addTickets(getName(), totalTickets, eventName, location, ticketPrice);
                Thread.sleep((long) (1000/ticketReleaseRate));
            }
        } catch (InterruptedException e) {
            logger.info(Thread.currentThread().getName() + " interrupted.");
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
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

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
