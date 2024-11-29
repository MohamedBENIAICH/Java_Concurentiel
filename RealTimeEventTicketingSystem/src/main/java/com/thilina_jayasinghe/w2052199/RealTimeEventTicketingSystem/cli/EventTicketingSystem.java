package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.util.*;

public class EventTicketingSystem {

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        boolean is_Running = true;

        try {
            System.out.println("\n                  Welcome to the Event Booking System!\n");

            Configuration.configureSystem();

            TicketPool ticketPool = new TicketPool();

            while(is_Running) {
                System.out.println(
                        """
                                *******************************************************
                                |    Enter the number corresponding to your selection.|
                                |    Register a vendor -------------[1]               |
                                |    Register a customer -----------[2]               |
                                |    Start -------------------------[3]               |
                                |    Reconfigure system ------------[4]               |
                                |    Exit Program ------------------[0]               |
                                *******************************************************
                        """
                );
                try {
                    int option = input.nextInt();
                    input.nextLine();
                    switch (option) {
                        case 1:
                            Vendor vendor = (Vendor) User.registerUser("Vendor", ticketPool);
                            threads.add(new Thread(vendor, vendor.getName()));
                            break;
                        case 2:
                            Customer customer = (Customer) User.registerUser("Customer", ticketPool);
                            Thread customerThread = new Thread(customer, customer.getName());
                            if (customer.getIsVIP()) {
                                customerThread.setPriority(Thread.MAX_PRIORITY);
                            }
                            threads.add(customerThread);
                            break;
                        case 3:
                            start(threads);
                            break;
                        case 4:
                            Configuration.configureSystem();
                            ticketPool = new TicketPool();
                            break;
                        case 0:
                            System.out.println("\nThank you for using this system :)");
                            is_Running = false;
                            break;
                        default:
                            System.out.println("Invalid option number entered. Try again.");
                    }
                } catch (InputMismatchException ex) {
                    input.nextLine();
                    System.out.println("Invalid input. Enter a valid option number\n");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void start(List<Thread> threads) {
        for (Thread thread : threads) {
            System.out.println(thread.getName());
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
            threads.clear();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}
