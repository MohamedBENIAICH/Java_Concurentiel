package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;

import java.time.LocalDateTime;

public class CustomerTask implements Runnable {
    private Customer customer;
    private TicketPool ticketPool;
    private int purchaseQuantity;
    private static double retrievalInterval;

    public CustomerTask(Customer customer, TicketPool ticketPool) {
        this.customer = customer;
        this.ticketPool = ticketPool;
        this.purchaseQuantity = customer.getPurchaseQuantity();
    }

    @Override
    public void run() {
        try {
            while ((ticketPool.getUnpurchasedTickets().get() != 0) && (purchaseQuantity>0)) {
                Ticket ticket = ticketPool.removeTicket(customer.getCustomerName(), LocalDateTime.now());
                purchaseQuantity--;
                if (ticket == null) {
                    break;
                }
                Thread.sleep((long) (retrievalInterval*1000));
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
