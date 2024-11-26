package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.util.logging.Logger;

public class Vendor extends User implements Runnable {
    private Configuration configuration;
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());

    public Vendor(String companyName, String address, String email, String telNo, Configuration configuration) {
        super(companyName, address, email, telNo);
        this.configuration = configuration;
    }

    @Override
    public void run() {
        try {
            while (TicketPool.getTicketCount().get()<configuration.getTotalTickets() && !Thread.currentThread().isInterrupted()) {
                configuration.getTicketPool().addTickets(getName());
                Thread.sleep((long) (1000/configuration.getTicketReleaseRate()));
            }
        } catch (InterruptedException e) {
            logger.info(Thread.currentThread().getName() + " interrupted.");
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }

}
