package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EventTicketingSystem {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("", "", "");
            Scanner input = new Scanner(System.in);

            boolean is_Running = true;

            System.out.println("\n                  Welcome to the Event Booking System!\n");

            Configuration.configureSystem(connection);

            while(is_Running) {
                System.out.println(
                        """
                                *******************************************************
                                |    Select an option, type choice number and enter:  |
                                |    Register a vendor -------------[1]               |
                                |    Register a customer -----------[2]               |
                                |    Start -------------------------[3]               |
                                |    Stop --------------------------[Ctrl + D]        |
                                |    Exit Program ------------------[0]               |
                                *******************************************************
                        """
                );
                try {
                    int option = input.nextInt();
                    input.nextLine();
                    switch (option) {
                        case 1:
                            User vendor = User.registerUser("Vendor");
                            break;
                        case 2:
                            User customer = User.registerUser("Customer");
                            break;
                        case 3:
                            start();
                            break;
                        case 4:

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

    public static void start() {

    }

}
