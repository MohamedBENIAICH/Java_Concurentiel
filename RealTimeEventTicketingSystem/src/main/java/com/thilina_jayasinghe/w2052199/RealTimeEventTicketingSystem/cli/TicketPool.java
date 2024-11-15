package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    protected static List<Ticket> ticketList = Collections.synchronizedList(new ArrayList<>());
    public Object lock = new Object();

    protected void add(Ticket ticket) {
        ticketList.addLast(ticket);
    }

    protected void remove(Ticket ticket) {
        ticketList.remove(ticket);
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }
}
