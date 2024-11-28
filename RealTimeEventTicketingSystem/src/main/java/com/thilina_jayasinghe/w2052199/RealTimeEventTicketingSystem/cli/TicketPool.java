package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class TicketPool {
    protected List<Ticket> ticketList;
    private int maxTicketCapacity;
    private AtomicInteger ticketCount;
    private AtomicInteger ticketsToBePurchased;
    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());

    public TicketPool() {
        ticketList = Collections.synchronizedList(new ArrayList<>());
        maxTicketCapacity = Objects.requireNonNull(GsonSerializer.deserializeConfig()).getMaxTicketCapacity();
        ticketCount = new AtomicInteger(0);
        ticketsToBePurchased = new AtomicInteger(0);
    }


    protected synchronized void addTickets(String vendorName, int totalTickets,String eventName, String location, BigDecimal ticketPrice) {
        try {
            while ((ticketList.size() == maxTicketCapacity) && (ticketCount.get() != totalTickets)) {       // if the list is full, it will wait indefinitely until a free spot is available
                logger.warning("Queue is full. Waiting for consumer...");
                wait();
                logger.warning("Vendor got notified from consumer");
            }
            if (ticketCount.get() == totalTickets) {
                Thread.currentThread().interrupt();
                return;
            }
            setTicketCount(ticketCount.incrementAndGet());
            Ticket ticket = new Ticket(ticketCount.get(), vendorName, eventName, location, ticketPrice);
            ticketList.addLast(ticket);
            logger.info(vendorName + " released ticket number " + ticketCount);
            notifyAll();
        } catch (InterruptedException e) {
            System.out.println("Thread got interrupted suddenly");
        }
    }

    protected synchronized Ticket removeTicket(Customer customer, String timestamp) {
        try {
            notifyAll();
            while (ticketList.isEmpty() && (ticketsToBePurchased.get() != 0) && !Thread.currentThread().isInterrupted()) {
                wait();
                logger.warning("Queue is empty. Waiting for Producer...");
            }
            if (ticketsToBePurchased.get() == 0 || Thread.currentThread().isInterrupted()) {
                return null;
            }
            Ticket ticket = ticketList.getFirst();
            ticket.setCustomerName(customer.getName());
            if(customer.getIsVIP()) {
                ticket.setTicketPrice(ticket.getTicketPrice().multiply(new BigDecimal("1.2")));
            }
            ticket.setTimestamp(timestamp);
            ticketList.removeFirst();
            setTicketsToBePurchased(ticketsToBePurchased.decrementAndGet());
            System.out.println("Number of tickets to be purchased now are " + ticketsToBePurchased);
            logger.info(ticket.toString());
            notifyAll();
            return ticket;
        } catch (InterruptedException e) {
            System.out.println("Thread got interrupted suddenly!");
            return null;
        }
    }

    public AtomicInteger getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount.set(ticketCount);
    }

    public AtomicInteger getTicketsToBePurchased() {
        return ticketsToBePurchased;
    }

    public void setTicketsToBePurchased(int ticketsToBePurchased) {
        this.ticketsToBePurchased.set(ticketsToBePurchased);
    }

}
