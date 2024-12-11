package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;


import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    // Stores log messages in memory for retrieval
    private List<String> logs = new ArrayList<>();

    public TicketPool(int totalTickets, int maxTicketCapacity) {
        ticketList = new ConcurrentLinkedQueue<>();
        this.totalTickets = totalTickets;
        this.maxTicketCapacity = maxTicketCapacity;
        this.unsoldTickets = new AtomicInteger(totalTickets);
    }

    protected void addTickets(Ticket ticket) {
        try {
            reentrantLock.lock();
            while ((ticketList.size() == maxTicketCapacity) && ticketCount.get() != totalTickets) {
                logMessages("Queue is full. Waiting for consumer...", "WARNING");
                condition.await();
                logMessages("Vendor got notified from consumer", "WARNING");
            }
            if (ticketCount.get() == totalTickets) {
                return;
            }
            ticketCount.incrementAndGet();
            ticket.setTicketNo(ticketCount.get());
            ticketList.add(ticket);
            logMessages(ticket.getVendor() + " released ticket number " + ticketCount, "INFO");
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
                logMessages("Queue is empty. Waiting for Producer...", "WARNING");
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
                logMessages(ticket.toString(), "INFO");
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

    public void logMessages(String log, String level) {
        if (level.equals("INFO")) {
            logger.info(log);
        } else {
            logger.warning(log);
        }

        try {
            reentrantLock.lock();
            logs.add(log);
        } finally {
            reentrantLock.unlock();
        }
    }

    public List<String> getLogs() {
        try {
            reentrantLock.lock();
            return new ArrayList<>(logs);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void clearLogs() {
        try {
            reentrantLock.lock();
            logs.clear();
        } finally {
            reentrantLock.unlock();
        }
    }
}
