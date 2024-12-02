package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;


import java.time.LocalDateTime;
import java.util.Objects;

public class Customer extends User implements Runnable {
    private boolean isVIP;
    private TicketPool ticketPool;
    private static double customerRetrievalRate = Objects.requireNonNull(GsonSerializer.deserializeConfig()).getCustomerRetrievalRate();

    public Customer(String customerName, String clientAddress, String clientEmail, String clientTel, boolean isVIP, TicketPool ticketPool) {
        super(customerName, clientAddress, clientEmail, clientTel);
        this.isVIP = isVIP;
        this.ticketPool = ticketPool;
    }

    public boolean getIsVIP() {
        return isVIP;
    }

    public double getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    @Override
    public void run () {
        try {
            while (ticketPool.getTicketsToBePurchased().get() != 0) {
                Ticket ticket = ticketPool.removeTicket(this, LocalDateTime.now().toString());
                if (ticket == null) {
                    break;
                }
                Thread.sleep((long) (1000/customerRetrievalRate));
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " interrupted.");
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
}
