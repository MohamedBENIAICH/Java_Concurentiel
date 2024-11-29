package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GsonSerializer {

    static File file = new File("system_config_settings.json");
    static Gson gson = new Gson();

    /**
     * Write initialization settings of system to a json file
     * @param configuration object containing all the parameters for total tickets, ticket sell rate and purchase rate
     *                      and size of the ticket buffer
     */
    protected static void serializeConfig(Configuration configuration) {

        JsonArray jsonArray;

        try {
            jsonArray = new JsonArray();

            jsonArray.add(gson.toJsonTree(configuration));

            // Write the updated array back to the file
            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(jsonArray, fileWriter);
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("Error in saving to file: " + e.getMessage());
        }
    }

    /**
     * Read system configuration details from json file
     * @return configuration object or null if deserialization fails
     */
    protected static Configuration deserializeConfig() {

        try (FileReader fileReader = new FileReader(file)) {
            // Parse the JSON array from the file
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            if (!jsonArray.isEmpty()) {
                return gson.fromJson(jsonArray.get(0), Configuration.class);
            }

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }

        return null; // Return null if deserialization fails
    }

    /**
     * Append ticket to a json file containing all ticket objects
     * @param ticket object containing vendor, customer, event details and time of ticket purchase
     */
    protected static void appendToJsonFile(Ticket ticket) {
        File file = new File("tickets.json");
        List<Ticket> ticketPurchases = new ArrayList<>();

        // Read existing data if the file already exists
        if (file.exists() && file.length() > 0) {
            try {
                FileReader reader = new FileReader(file);
                JsonArray existingArray = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement ticketObject : existingArray) {
                    ticketPurchases.add(gson.fromJson(ticketObject, Ticket.class));
                }
                reader.close();
            } catch (JsonIOException | JsonSyntaxException | IOException e) {
                System.out.println(e.getMessage());;
            }
        }

        // Add the new object to the list
        ticketPurchases.add(ticket);

        // Write the updated list back to the JSON file
        try  {
            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(ticketPurchases, fileWriter);
            fileWriter.close();
        } catch (IOException | JsonIOException e) {
            System.out.println(e.getMessage());;
        }
    }

}
