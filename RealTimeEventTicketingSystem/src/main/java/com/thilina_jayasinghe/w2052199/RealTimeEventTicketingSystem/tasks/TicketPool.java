package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {

    private ConcurrentLinkedQueue<Ticket> ticketPool;
    private static ReentrantLock lock = new ReentrantLock();

    public TicketPool() {
        ticketPool = new ConcurrentLinkedQueue<>();
    }

    private void addTickets(Ticket ticket) {
        ticketPool.add(ticket);
    }

    private Ticket removeTicket() {
        return (ticketPool.remove());
    }

}
