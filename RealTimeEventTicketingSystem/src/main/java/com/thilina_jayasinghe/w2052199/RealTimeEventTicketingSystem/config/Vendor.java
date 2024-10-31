package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Vendor implements Runnable {
    private int id;
    private String companyName;
    private String address;
    private String email;
    private TicketPool ticketPool;
    private List<Event> eventSeries;
    private volatile boolean isActive=true;

    public Vendor(int id, String companyName, String address, String email, TicketPool ticketPool) {
        this.id = id;
        this.companyName = companyName;
        this.address = address;
        this.email = email;
        this.ticketPool = ticketPool;
    }
    Random random = new Random();
    Scanner input = new Scanner(System.in);

    public int getId() {
        return id;
    }

    public Random getRandom() {
        return random;
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

    public void stop() {
        isActive = false;
    }

    @Override
    public void run() {
        while (isActive) {
            System.out.println("Enter option num");
            int option = input.nextInt();

            switch (option) {
                case 0:
                    stop();
                    break;
                case 1:
                    try {
                        createEvent();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    removeEvent();
                    break;
                case 3:
                    releaseTickets();
                    break;
                default:
                    System.out.println("Try again");
            }

        }
    }

    private void releaseTickets() {

    }

    private void removeEvent() {
    }

    private void createEvent() throws ParseException {

        String eventID = String.valueOf(ticketPool.getTicketList().size());

        System.out.println("Enter the location of the event.");
        String venue = input.nextLine();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Enter the planned date for the event.");
        String date = input.nextLine();
        Date eventDate = dateFormat.parse(date);


        String startTime = input.nextLine();
        String duration = input.nextLine();
        EventType eventType = EventType.valueOf(input.nextLine());

        Event event = new Event(eventID, venue, eventDate, startTime, duration, eventType);
        eventSeries.add(event);
    }

}
