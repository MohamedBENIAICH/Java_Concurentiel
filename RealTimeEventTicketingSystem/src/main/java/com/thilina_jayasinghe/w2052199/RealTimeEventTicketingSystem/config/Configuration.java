package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Configuration {
    protected int totalTickets;
    protected double ticketReleaseRate;
    protected double customerRetrievalRate;
    protected int maxTicketCapacity;
    private String eventName;
    private String venue;
    private Date eventDateTime;
    private String duration;
    private EventType eventType;
    private double ticketPrice;
    private TicketPool ticketPool;

    Configuration(int totalTickets, double ticketReleaseRate, double customerRetrievalRate, int maxTicketCapacity, String eventName, String venue, Date eventDate, String duration, EventType eventType, double ticketPrice) {
        setTotalTickets(totalTickets);
        setTicketReleaseRate(ticketReleaseRate);
        setCustomerRetrievalRate(customerRetrievalRate);
        setMaxTicketCapacity(maxTicketCapacity);
        setEventName(eventName);
        setVenue(venue);
        setEventDateTime(eventDate);
        setDuration(duration);
        setEventType(String.valueOf(eventType));
        setTicketPrice(ticketPrice);
        serializeConfig();
        ticketPool = new TicketPool();
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }

    public void setTotalTickets(int totalTickets) {
        if (
            (totalTickets > 0)
        ){
            this.totalTickets = totalTickets;
        }
    }

    public double getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(double ticketReleaseRate) {
        if (
                ticketReleaseRate > 0.0
        ){
            this.ticketReleaseRate = ticketReleaseRate;
        }
    }

    public double getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(double customerRetrievalRate) {
        if (
                (customerRetrievalRate > 0.0) && (customerRetrievalRate < ticketReleaseRate)
        ){
            this.customerRetrievalRate = customerRetrievalRate;
        }
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        if (
                (maxTicketCapacity < totalTickets) && (maxTicketCapacity > 0)
        ){
            this.maxTicketCapacity = maxTicketCapacity;
        }
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        switch (eventType) {
            case "CONCERT":
            case "TOURNAMENT":
            case "CONFERENCE":
            case "THEATRE":
            case "CINEMA":
            case "OPERA":
            case "EXHIBITION":
            case "CEREMONY":
            case "GALA":
            case "AUCTION":
                this.eventType = EventType.valueOf(eventType);
                break;
            default:
                System.out.println("Invalid event category.");
        }
    }



    /**
     * Write configuration settings in a json file
     */
    private void serializeConfig() {

        try {
            FileWriter fileWriter = new FileWriter("system_configuration_settings.json", true);
            Gson gson = new Gson();
            gson.toJson(this, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error in saving to file: " + e.getMessage());;
        }
    }
}
