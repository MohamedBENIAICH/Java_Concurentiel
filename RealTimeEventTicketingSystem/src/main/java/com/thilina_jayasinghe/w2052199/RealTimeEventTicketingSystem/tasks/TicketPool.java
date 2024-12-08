package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;


import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;


public class TicketPool {

    private ConcurrentLinkedQueue<Ticket> ticketList;
    private int maxTicketCapacity;
    private int totalTickets;
    private AtomicInteger ticketCount = new AtomicInteger(0);
    private AtomicInteger unsoldTickets;
    ReentrantLock reentrantLock = new ReentrantLock();
    Condition condition = reentrantLock.newCondition();
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());

    public TicketPool(int totalTickets, int maxTicketCapacity) {
        ticketList = new ConcurrentLinkedQueue<>();
        this.totalTickets = totalTickets;
        this.maxTicketCapacity = maxTicketCapacity;
        this.unsoldTickets = new AtomicInteger(totalTickets);
        logger.info("TicketPool initialized with totalTickets: " + totalTickets + " and maxTicketCapacity: " + maxTicketCapacity);
    }

    protected void addTickets(Ticket ticket) {
        try {
            reentrantLock.lock();
            while ((ticketList.size() == maxTicketCapacity) && ticketCount.get() != totalTickets) {
                logger.warning("Queue is full. Waiting for consumer...");
                condition.await();
                logger.warning("Vendor got notified from consumer");
            }
            if (ticketCount.get() == totalTickets) {
                return;
            }
            ticketCount.incrementAndGet();
            ticketList.add(ticket);
            logger.info(ticket.getVendor() + " released ticket number " + ticketCount);
            condition.signalAll();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            reentrantLock.unlock();
        }
    }

    protected Ticket removeTicket(String customerName, LocalDateTime timestamp) {
        try {
            reentrantLock.lock();
            condition.signalAll();
            while (ticketList.isEmpty() && unsoldTickets.get() != 0 && !Thread.currentThread().isInterrupted()) {
                condition.await();
                logger.warning("Queue is empty. Waiting for Producer...");
            }
            if (unsoldTickets.get() == 0 || Thread.currentThread().isInterrupted()) {
                return null;
            }
            Ticket ticket = ticketList.peek();
            if (ticket != null) {
                ticket.setCustomer(customerName);
                ticket.setTimestamp(Timestamp.valueOf(timestamp));
                ticketList.remove();
                unsoldTickets.decrementAndGet();
                logger.info(ticket.toString());
            }
            condition.signalAll();
            return ticket;
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            reentrantLock.unlock();
        }
    }

    public AtomicInteger getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(AtomicInteger ticketCount) {
        this.ticketCount = ticketCount;
    }

    public AtomicInteger getUnsoldTickets() {
        return unsoldTickets;
    }

    public void setUnsoldTickets(AtomicInteger unsoldTickets) {
        this.unsoldTickets = unsoldTickets;
    }

    public Object getStatus() {
        Map<String, Object> status = new HashMap<>();

        // Validate and add totalTickets
        if (totalTickets > 0) {
            status.put("totalTickets", totalTickets);
        }

        // Validate and add unsoldTickets
        if (unsoldTickets != null) {
            status.put("unsoldTickets", unsoldTickets.get());
        }

        // Validate and add currentQueueSize
        if (ticketList != null) {
            status.put("currentQueueSize", ticketList.size());
        }

        // Validate and add maxTicketCapacity
        if (maxTicketCapacity > 0) {
            status.put("maxTicketCapacity", maxTicketCapacity);
        }

        return status;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
}
