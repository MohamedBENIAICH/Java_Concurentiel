package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.util.Scanner;

public class Customer extends User implements Runnable {
    private boolean isVIP = false;

    public Customer(String customerName, String clientAddress, String clientEmail, String clientTel, boolean isVIP) {
        super(customerName, clientAddress, clientEmail, clientTel);
        this.isVIP = isVIP;
    }

    public boolean getIsVIP() {
        return isVIP;
    }


    private synchronized void removeTickets(Configuration configuration) throws InterruptedException {
        Object lock = configuration.getTicketPool().lock;
        synchronized (lock){
            lock.notify();
            while (configuration.getTicketPool().getTicketList().isEmpty()) {
                lock.wait();
            }
            Ticket ticket = configuration.getTicketPool().getTicketList().getFirst();
            if (isVIP) {
                ticket.setTicketPrice(1.2* ticket.getTicketPrice());
            }
            System.out.println(ticket.getTicketNo() + " got purchased by customer" + getName() + " for " + ticket.getTicketPrice());
            configuration.getTicketPool().remove(ticket);
        }
    }


            @Override
            public void run () {

            }
        }
