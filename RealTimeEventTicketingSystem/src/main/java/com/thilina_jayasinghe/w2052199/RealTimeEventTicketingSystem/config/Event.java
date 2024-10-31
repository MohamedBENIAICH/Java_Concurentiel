package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import java.util.Date;

public class Event {
    private String eventID;
    private String venue;
    private Date eventDate;
    private String startTime;
    private String duration;
    private EventType eventType;

    public Event(String eventID, String venue, Date eventDate, String startTime, String duration, EventType eventType) {
        this.eventID = eventID;
        this.venue = venue;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.duration = duration;
        this.eventType = eventType;
    }

    public String getEventID() {
        return eventID;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    };

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {}

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {}

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    };
}
