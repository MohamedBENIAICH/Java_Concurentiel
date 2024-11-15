package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

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

    public static User registerUser(String userType) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter name.");
        String name = input.nextLine();
        System.out.println("Enter address.");
        String address = input.nextLine();
        System.out.println("Enter email.");
        String email = input.nextLine();
        System.out.println("Enter mobile number.");
        String tel = input.nextLine();
        if (userType.equals("Vendor")) {
            return new Vendor(name, address, email, tel);
        } else if (userType.equals("Customer")) {
            boolean isVIP = false;
            System.out.println("Are you interested in subscribing to a premium account?(Y/N)");
            String response = input.nextLine();
            if (response.equalsIgnoreCase("y")) {
                isVIP = true;
            } else if (response.equalsIgnoreCase("n")) {
                isVIP = false;
            } else {
                System.out.println("Invalid response. Premium features are unavailable at this time:)");
            }
            return new Customer(name, address, email, tel, isVIP);
        } else {
            return null;
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
