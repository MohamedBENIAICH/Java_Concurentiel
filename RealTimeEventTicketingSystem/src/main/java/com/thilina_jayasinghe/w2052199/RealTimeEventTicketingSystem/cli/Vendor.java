package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Vendor extends User implements Runnable {

    public Vendor(String companyName, String address, String email, String telNo) {
        super(companyName, address, email, telNo);
    }

    @Override
    public void run() {


    }

    public synchronized void addTickets(Configuration sysConfig, int ticketNum) throws InterruptedException {
        Object lock = sysConfig.getTicketPool().lock;
        synchronized (lock) {
            while (sysConfig.getTicketPool().getTicketList().size() == sysConfig.getMaxTicketCapacity()) {
                System.out.println("Pool is at maximum capacity. Waiting for customers...");
                lock.wait();
                System.out.println("Pool is at maximum capacity. Waiting for customers...");
            }
            Ticket ticket = new Ticket(ticketNum, this, sysConfig.getTicketPrice());
            sysConfig.getTicketPool().add(ticket);
            System.out.println("Ticket " + ticket.getTicketNo() + " is available now.");
            lock.notify();
        }
    }

    public void saveVendor(Connection connection) {

    }

}
