package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Vendor implements Runnable {
    private String companyName;
    private String address;
    private String email;
    private String telNo;
    private List<Configuration> eventSeries;

    public Vendor(String companyName, String address, String email, String telNo) {
        this.companyName = companyName;
        this.address = address;
        this.email = email;
        this.telNo = telNo;
        eventSeries = new ArrayList<>();
    }

    Scanner input = new Scanner(System.in);

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public void run() {
        for (Configuration event : eventSeries) {
            try {
                releaseTickets(event);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());;
            }
        }
    }

    public void createEvent() {
        int totTickets = 0;
        double sellRate = 0;
        double buyRate = 0;
        int maxTickets = 0;
        String name = "";
        String venue = "";
        Date eventDate = null;
        String duration = "";
        EventType eventType = null;
        double price = 0.0;
        boolean isConfiguring = true;
        while (isConfiguring) {
            try {
                System.out.println("Pick a name for the event.");
                name = input.nextLine();

                System.out.println("Enter total number of tickets available.");
                totTickets = input.nextInt();
                input.nextLine();

                System.out.println("Enter ticket release rate.");
                sellRate = input.nextDouble();
                input.nextLine();

                System.out.println("Enter allowable ticket purchase rate of customers.");
                buyRate = input.nextDouble();
                input.nextLine();

                System.out.println("Enter maximum number of tickets available at any given instance.");
                maxTickets = input.nextInt();
                input.nextLine();

                System.out.println("Enter the location of the event.");
                venue = input.nextLine();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("Enter the planned date and starting time of the event.(yyyy-mm-dd HH:mm:ss)");
                String date = input.nextLine();
                eventDate = dateFormat.parse(date);

                System.out.println("Enter the estimated duration of the event.");
                duration = input.nextLine();
                System.out.println(
                        "Select and input event type. " +
                                "{ CONCERT | " +
                                "TOURNAMENT | " +
                                "CONFERENCE | " +
                                "THEATRE | " +
                                "CINEMA | " +
                                "OPERA | " +
                                "EXHIBITION | " +
                                "CEREMONY | " +
                                "GALA | " +
                                "AUCTION }"
                );

                eventType = EventType.valueOf(input.nextLine().toUpperCase());

                System.out.println("What is the price of a ticket?");
                price = input.nextDouble();
                input.nextLine();
                isConfiguring=false;
            } catch (InputMismatchException | ParseException exception){
                System.out.println("Enter valid input.");
            }

        }

        Configuration configuration = new Configuration(totTickets, sellRate, buyRate, maxTickets, name, venue, eventDate, duration, eventType, price);
        eventSeries.add(configuration);
    }

    public synchronized void releaseTickets(Configuration event) throws InterruptedException {
        if(event.getTicketPool().getTicketList().size() == event.getMaxTicketCapacity()){
            try {
                System.out.println("Pool is at maximum capacity. Waiting for customers...");
                wait();
                System.out.println("Pool is at maximum capacity. Waiting for customers...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        int ticketNum = 1;
        Ticket ticket = new Ticket(ticketNum, event, event.getTicketPrice());
        event.getTicketPool().addTickets(ticket);
        ticketNum++;
        Thread.sleep((long) (1000/event.getTicketReleaseRate()));
        System.out.println("Ticket " + ticket.ticketNo() + " is available now.");
        notify();
    }

}
