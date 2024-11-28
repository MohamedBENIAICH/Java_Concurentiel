package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Configuration {
    private int totalTickets;
    private double ticketReleaseRate;
    private double customerRetrievalRate;
    private int maxTicketCapacity;

    static Scanner input = new Scanner(System.in);

    Configuration(int totalTickets, double ticketReleaseRate, double customerRetrievalRate, int maxTicketCapacity) {
        setTotalTickets(totalTickets);
        setTicketReleaseRate(ticketReleaseRate);
        setCustomerRetrievalRate(customerRetrievalRate);
        setMaxTicketCapacity(maxTicketCapacity);
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        if (
            (totalTickets > 0) && (totalTickets < 1000)
        ){
            this.totalTickets = totalTickets;
        }
    }

    public double getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(double ticketReleaseRate) {
        if (
                (ticketReleaseRate > 0.0) && (ticketReleaseRate < totalTickets)
        ){
            this.ticketReleaseRate = ticketReleaseRate;
        }
    }

    public double getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(double customerRetrievalRate) {
        if (
                (customerRetrievalRate > 0.0) && (customerRetrievalRate < ticketReleaseRate)
        ){
            this.customerRetrievalRate = customerRetrievalRate;
        }
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        if (
                (maxTicketCapacity < totalTickets) && (maxTicketCapacity > 0)
        ){
            this.maxTicketCapacity = maxTicketCapacity;
        }
    }

    public static void configureSystem() {
        int totTickets = 0;
        double sellRate = 0;
        double buyRate = 0;
        int maxTickets = 0;
        boolean isConfiguring = true;
        while (isConfiguring) {
            try {
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

                isConfiguring = false;
            } catch (InputMismatchException exception) {
                System.out.println("Enter valid input.");
            }
            GsonSerializer.serializeConfig(new Configuration(totTickets, buyRate, sellRate, maxTickets));
            TicketPool.setTicketsToBePurchased(totTickets);
        }
    }

}
