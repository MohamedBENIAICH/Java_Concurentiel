package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

public class Customer implements Runnable {
    private String customerName;
    private String clientAddress;
    private String clientEmail;
    private String clientTel;
    private Configuration selectedEvent;

    public Customer(String customerName, String clientAddress, String clientEmail, String clientTel) {
        this.customerName = customerName;
        this.clientAddress = clientAddress;
        this.clientEmail = clientEmail;
        this.clientTel = clientTel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientTel() {
        return clientTel;
    }

    public void setClientTel(String clientTel) {
        this.clientTel = clientTel;
    }

    public void selectEvent(Configuration event) {
        selectedEvent = event;
    }

    private synchronized void getTickets(Configuration event) throws InterruptedException {
        Object lock = event.getTicketPool().lock;
        synchronized (lock){
            lock.notify();
            while (event.getTicketPool().getTicketList().isEmpty()) {
                lock.wait();
            }
            Ticket ticket = event.getTicketPool().getTicketList().getFirst();
            System.out.println(ticket.ticketNo() + " got purchased by customer" + getCustomerName());
            event.getTicketPool().removeTicket(ticket);
        }
    }


            @Override
            public void run () {
                while (true) {
                    try {
                        getTickets(selectedEvent);
                        Thread.sleep((long) (1000/selectedEvent.getCustomerRetrievalRate()));
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
