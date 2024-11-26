package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;


import java.time.LocalDateTime;

public class Customer extends User implements Runnable {
    private boolean isVIP = false;
    private Configuration configuration;

    public Customer(String customerName, String clientAddress, String clientEmail, String clientTel, boolean isVIP, Configuration configuration) {
        super(customerName, clientAddress, clientEmail, clientTel);
        this.isVIP = isVIP;
        this.configuration = configuration;
    }

    public boolean getIsVIP() {
        return isVIP;
    }



    @Override
    public void run () {
        try {
            while (TicketPool.getTicketsToBePurchased().get() != 0) {
                Ticket ticket = configuration.getTicketPool().removeTicket(this, LocalDateTime.now().toString());
                if (ticket == null) {
                    break;
                }
                Thread.sleep((long) (1000/configuration.getCustomerRetrievalRate()));
            }
//            System.out.println("It has reached the interrupt stage");
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " interrupted.");
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
}
