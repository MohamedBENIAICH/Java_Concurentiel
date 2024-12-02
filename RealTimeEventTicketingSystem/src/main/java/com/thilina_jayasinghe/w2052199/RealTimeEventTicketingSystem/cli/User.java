package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.math.BigDecimal;
import java.util.Scanner;

public class User {
    private String name;
    private String address;
    private String email;
    private String telNo;

    public User(String name, String address, String email, String telNo) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.telNo = telNo;
    }

    protected static User registerUser(String userType, TicketPool ticketPool) {
        while(true) {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter name.");
            String name = input.nextLine();
            System.out.println("Enter address.");
            String address = input.nextLine();
            System.out.println("Enter email.");   // need to verify
            String email = input.nextLine();
            System.out.println("Enter mobile number.");    // need to verify
            String tel = input.nextLine();
            if (userType.equals("Vendor")) {
                System.out.println("Enter the name of the event.");
                String event = input.nextLine();
                System.out.println("Enter the location of the event.");
                String location = input.nextLine();
                System.out.println("Enter the price of a ticket.");
                BigDecimal price = input.nextBigDecimal();
                input.nextLine();
                return new Vendor(name, address, email, tel, event, location, price, ticketPool);
            } else if (userType.equals("Customer")) {
                boolean isVIP = false;
                System.out.println("Would you be interested in a premium account to enjoy early access to ticket purchases for only 20% more?(Y/N)");
                String response = input.nextLine();
                if (response.equalsIgnoreCase("y")) {
                    isVIP = true;
                    System.out.println("Congratulations, you have registered as a VIP customer.");
                } else if (response.equalsIgnoreCase("n")) {
                    System.out.println("Got it!");
                } else {
                    System.out.println("Invalid response. Premium features are unavailable at this time:)");
                }
                return new Customer(name, address, email, tel, isVIP, ticketPool);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getTelNo() {
        return telNo;
    }
}
