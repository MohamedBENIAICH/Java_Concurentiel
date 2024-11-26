package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EventTicketingSystem {
    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("", "", "");
            Scanner input = new Scanner(System.in);

            boolean is_Running = true;

            System.out.println("\n                  Welcome to the Event Booking System!\n");

            Configuration configuration = Configuration.configureSystem(connection);

            while(is_Running) {
                System.out.println(
                        """
                                *******************************************************
                                |    Select an option, type choice number and enter:  |
                                |    Register a vendor -------------[1]               |
                                |    Register a customer -----------[2]               |
                                |    Start -------------------------[3]               |
                                |    Stop --------------------------[Ctrl + C]        |
                                |    Exit Program ------------------[0]               |
                                *******************************************************
                        """
                );
                try {
                    int option = input.nextInt();
                    input.nextLine();
                    switch (option) {
                        case 1:
                            Vendor vendor = (Vendor) User.registerUser("Vendor", configuration);
                            threads.add(new Thread(vendor, vendor.getName()));
                            break;
                        case 2:
                            Customer customer = (Customer) User.registerUser("Customer", configuration);
                            Thread customerThread = new Thread(customer, customer.getName());
                            if (customer.getIsVIP()) {
                                customerThread.setPriority(Thread.MAX_PRIORITY);
                            }
                            threads.add(customerThread);
                            break;
                        case 3:
                            start(threads);
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void start(List<Thread> threads) {
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
                System.out.println(Thread.currentThread().getName() + "thread has joined");
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void stop(List<Thread> threads) {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

}
