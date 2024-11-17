package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model;

import jakarta.persistence.*;


@Entity
@Table(name = "config")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int eventId;
    private int totalTickets;
    private double ticketReleaseRate;
    private double customerRetrievalRate;
    private int maxTicketCapacity;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

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

    public double getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(double customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
}
