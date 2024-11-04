package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    protected static List<Ticket> ticketList = Collections.synchronizedList(new ArrayList<>());
    public Object lock = new Object();

    protected void addTickets(Ticket ticket) {
        ticketList.addLast(ticket);
    }

    protected void removeTicket(Ticket ticket) {
        ticketList.remove(ticket);
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }
}
