package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model;

import jakarta.persistence.*;


@Entity
@Table(name = "config")
public class Configuration {
    @Id
    private final int configId = 1;
    private int totalTickets;
    private double ticketReleaseRate;
    private double customerRetrievalInterval;
    private int maxTicketCapacity;

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public double getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(double ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public double getCustomerRetrievalInterval() {
        return customerRetrievalInterval;
    }

    public void setCustomerRetrievalInterval(double customerRetrievalInterval) {
        this.customerRetrievalInterval = customerRetrievalInterval;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
}
