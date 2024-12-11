package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service.TicketService;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class CustomerTask implements Runnable {
    private Customer customer;
    private TicketPool ticketPool;
    private int purchaseQuantity;
    private double retrievalInterval;
    private TicketService ticketService;
    ReentrantLock reentrantLock = new ReentrantLock();

    public CustomerTask(Customer customer, TicketPool ticketPool, double retrievalInterval, TicketService ticketService) {
        this.customer = customer;
        this.ticketPool = ticketPool;
        this.purchaseQuantity = customer.getPurchaseQuantity();
        this.retrievalInterval = retrievalInterval;
        this.ticketService = ticketService;
    }

    @Override
    public void run() {
        try {
            while ((ticketPool.getUnsoldTickets().get() != 0) && (purchaseQuantity>0)) {
                Ticket ticket = ticketPool.removeTicket(customer.getCustomerName(), getUniqueTimestamp());
                purchaseQuantity--;
                if (ticket == null) {
                    break;
                }
                ticketService.saveTicket(ticket);
                Thread.sleep((long) (retrievalInterval*1000));
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public LocalDateTime getUniqueTimestamp() {
        try {
            reentrantLock.lock();
            return LocalDateTime.now();
        } finally {
            reentrantLock.unlock();
        }
    }
}
