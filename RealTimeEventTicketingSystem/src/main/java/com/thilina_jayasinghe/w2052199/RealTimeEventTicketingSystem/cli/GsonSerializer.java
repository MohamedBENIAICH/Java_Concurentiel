package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class GsonSerializer extends Formatter implements Filter {

    static File file = new File("system_config_settings.json");
    static Gson gson = new Gson();
    private Level level = Level.INFO;

    /**
     * Write initialization settings of system in a json file
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

    @Override
    public String format(LogRecord record) {
        Ticket ticket = (Ticket) record.getParameters()[0];
        return gson.toJson(ticket) + System.lineSeparator();
    }


    @Override
    public boolean isLoggable(LogRecord record) {
        return record.getLevel().equals(level);
    }
}
