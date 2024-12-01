package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class TicketPool {

    private ConcurrentLinkedQueue<Ticket> ticketPool;
    private static int maxTicketCapacity;
    private static int totalTickets;
    private AtomicInteger ticketCount = new AtomicInteger(0);
    private AtomicInteger unpurchasedTickets = new AtomicInteger(totalTickets);
    ReentrantLock reentrantLock = new ReentrantLock();
    Condition condition = reentrantLock.newCondition();
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());

    public TicketPool() {
        ticketPool = new ConcurrentLinkedQueue<>();
        // need to get total tickets, maximum ticket capacity through a web socket connection
    }

    private void addTickets(Ticket ticket) {
        try {
            reentrantLock.lock();
            while ((ticketPool.size() == maxTicketCapacity) && ticketCount.get() != totalTickets) {
                logger.warning("Queue is full. Waiting for consumer...");
                condition.await();
                logger.warning("Vendor got notified from consumer");
            }
            if (ticketCount.get() == totalTickets) {
                return;
            }
            ticketCount.incrementAndGet();
            ticketPool.add(ticket);
            logger.info(ticket.getVendor() + " released ticket number " + ticketCount);
            condition.signalAll();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            reentrantLock.unlock();
        }
    }

    private Ticket removeTicket(String customerName, LocalDateTime timestamp) {
        try {
            reentrantLock.lock();
            condition.signalAll();
            while (ticketPool.isEmpty() && unpurchasedTickets.get() != 0 && !Thread.currentThread().isInterrupted()) {
                condition.await();
                logger.warning("Queue is empty. Waiting for Producer...");
            }
            if (unpurchasedTickets.get() == 0 || Thread.currentThread().isInterrupted()) {
                return null;
            }
            Ticket ticket = ticketPool.peek();
            if (ticket != null) {
                ticket.setCustomer(customerName);
                ticket.setTimestamp(Timestamp.valueOf(timestamp));
                ticketPool.remove();
                unpurchasedTickets.decrementAndGet();
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
}
