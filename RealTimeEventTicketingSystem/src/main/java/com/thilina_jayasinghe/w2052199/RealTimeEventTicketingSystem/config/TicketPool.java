package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    private List<Ticket> ticketList = Collections.synchronizedList(new ArrayList<Ticket>());

    public void addTickets(Ticket ticket) {
        ticketList.addLast(ticket);
    }

    public void removeTicket(Ticket ticket) {
        ticketList.remove(ticket);
    }

    public List<Ticket> getTicketList() {
        return new ArrayList<>(ticketList);
    }
}
