package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.math.BigDecimal;
import java.util.InputMismatchException;
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

    /**
     * Method common to vendors and customers to get registered in the system
     * @param userType type of user being either vendor or customer
     * @param ticketPool Instance of TicketPool to be passed on to the Vendor or Customer instance
     * @return User object that is either a vendor or a customer
     */
    protected static User registerUser(String userType, TicketPool ticketPool) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String phonePattern = "^[0-9]{10}$";
        Scanner input = new Scanner(System.in);
        while(true) {

            try {
                System.out.println("Enter name.");
                String name = input.nextLine();
                System.out.println("Enter address.");
                String address = input.nextLine();
                System.out.println("Enter email.");   // need to verify
                String email;
                do {
                    email = input.nextLine();
                    if (!email.matches(emailPattern)) {
                        System.out.println("Invalid email format. Please enter a valid email address.");
                    }
                } while (!email.matches(emailPattern));

                System.out.println("Enter mobile number.");    // need to verify
                String tel;
                do {
                    tel = input.nextLine();
                    if (!tel.matches(phonePattern)) {
                        System.out.println("Invalid phone number format. Please enter a 10-digit mobile number.");
                    }
                } while (!tel.matches(phonePattern));

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
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("Invalid input format. Please try again.");
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
