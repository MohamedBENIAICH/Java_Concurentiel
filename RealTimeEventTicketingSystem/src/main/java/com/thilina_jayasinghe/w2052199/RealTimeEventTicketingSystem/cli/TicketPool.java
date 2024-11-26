package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;

public class TicketPool {
    protected static List<Ticket> ticketList = Collections.synchronizedList(new ArrayList<>());
    private final Configuration configuration;
    private static AtomicInteger ticketCount = new AtomicInteger(0);
    private static AtomicInteger ticketsToBePurchased = new AtomicInteger(0);
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());

    public TicketPool(Configuration configuration) {
        this.configuration = configuration;
    }

    protected synchronized void addTickets(String vendorName) {
        try {
            while ((ticketList.size() == configuration.getMaxTicketCapacity()) && (ticketCount.get() != configuration.getTotalTickets())) {       // if the list is full, it will wait indefinitely until a free spot is available
                logger.warning("Queue is full. Waiting for consumer...");
                wait();
                logger.info("Vendor got notified from consumer");
            }
            if (ticketCount.get() == configuration.getTotalTickets()) {
                Thread.currentThread().interrupt();
                return;
            }
            setTicketCount(ticketCount.incrementAndGet());
            Ticket ticket = new Ticket(ticketCount.get(), vendorName, configuration.getTicketPrice());
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
                ticket.setTicketPrice(ticket.getTicketPrice()*1.2);
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

    public static AtomicInteger getTicketCount() {
        return ticketCount;
    }

    public static void setTicketCount(int ticketCount) {
        TicketPool.ticketCount.set(ticketCount);
    }

    public static AtomicInteger getTicketsToBePurchased() {
        return ticketsToBePurchased;
    }

    public static void setTicketsToBePurchased(int ticketsToBePurchased) {
        TicketPool.ticketsToBePurchased.set(ticketsToBePurchased);
    }

}
