package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.util.Scanner;

public class Customer extends User implements Runnable {
    private Configuration selectedEvent;
    private boolean isVIP = false;

    Scanner input = new Scanner(System.in);

    public Customer(String customerName, String clientAddress, String clientEmail, String clientTel, boolean isVIP) {
        super(customerName, clientAddress, clientEmail, clientTel);
        this.isVIP = isVIP;
    }

    public void selectEvent(Configuration event) {
        selectedEvent = event;
    }

    private synchronized void removeTickets(Configuration event) throws InterruptedException {
        Object lock = event.getTicketPool().lock;
        synchronized (lock){
            lock.notify();
            while (event.getTicketPool().getTicketList().isEmpty()) {
                lock.wait();
            }
            Ticket ticket = event.getTicketPool().getTicketList().getFirst();
            System.out.println(ticket.getTicketNo() + " got purchased by customer" + getName() + " for " + ticket.getTicketPrice());
            event.getTicketPool().remove(ticket);
        }
    }


            @Override
            public void run () {
                while (true) {
                    try {
                        removeTickets(selectedEvent);
                        Thread.sleep((long) (1000/selectedEvent.getCustomerRetrievalRate()));
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
