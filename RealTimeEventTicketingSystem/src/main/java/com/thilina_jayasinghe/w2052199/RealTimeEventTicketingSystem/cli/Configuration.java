package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Configuration {
    private int totalTickets;
    private double ticketReleaseRate;
    private double customerRetrievalRate;
    private int maxTicketCapacity;
    private static double ticketPrice;
    private TicketPool ticketPool;

    static Scanner input = new Scanner(System.in);

    Configuration(int totalTickets, double ticketReleaseRate, double customerRetrievalRate, int maxTicketCapacity, double ticketPrice) {
        setTotalTickets(totalTickets);
        setTicketReleaseRate(ticketReleaseRate);
        setCustomerRetrievalRate(customerRetrievalRate);
        setMaxTicketCapacity(maxTicketCapacity);
        setTicketPrice(ticketPrice);
        serializeConfig();
        ticketPool = new TicketPool();
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public TicketPool getTicketPool() {
        return ticketPool;
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

    public static void configureSystem(Connection connection) {
        int totTickets = 0;
        double sellRate = 0;
        double buyRate = 0;
        int maxTickets = 0;
        double price = 0.0;
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


                System.out.println("What is the price of a ticket?");
                price = input.nextDouble();
                input.nextLine();
                isConfiguring = false;
            } catch (InputMismatchException exception) {
                System.out.println("Enter valid input.");
            }

        }
        Configuration configuration = new Configuration(totTickets, sellRate, buyRate, maxTickets, price);
        configuration.saveConfiguration(connection);
    }



    /**
     * Write configuration settings of events in a json file
     */
    private void serializeConfig() {
        File file = new File("system_configuration_settings.json");
        Gson gson = new Gson();
        JsonArray jsonArray;

        try {
            // Check if file exists and has data
            if (file.exists() && file.length() != 0) {
                // Load existing array from file
                FileReader fileReader = new FileReader(file);
                JsonElement jsonElement = JsonParser.parseReader(fileReader);
                jsonArray = jsonElement.getAsJsonArray();
                fileReader.close();
            } else {
                // Create new array if file is empty or does not exist
                jsonArray = new JsonArray();
            }

            // Add the current object to the array
            jsonArray.add(gson.toJsonTree(this));

            // Write the updated array back to the file
            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(jsonArray, fileWriter);
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("Error in saving to file: " + e.getMessage());
        }
    }

    public void saveConfiguration(Connection connection) {
        String systemConfiguration = """
              INSERT INTO event (ticketPrice, totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity) VALUES ()
        """
                ;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(systemConfiguration);

            preparedStatement.setDouble(7, ticketPrice);
            preparedStatement.setInt(8, totalTickets);
            preparedStatement.setDouble(9, ticketReleaseRate);
            preparedStatement.setDouble(10, customerRetrievalRate);
            preparedStatement.setInt(11, maxTicketCapacity);

            int numOfRows = preparedStatement.executeUpdate();

            if (numOfRows > 0) {
                System.out.println("Event saved successfully");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
