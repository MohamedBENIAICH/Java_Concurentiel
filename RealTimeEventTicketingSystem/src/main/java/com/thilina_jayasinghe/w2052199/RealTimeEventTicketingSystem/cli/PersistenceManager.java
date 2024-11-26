package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.cli;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersistenceManager {

    protected void saveConfiguration(Configuration configuration, Connection connection) {
        String systemConfiguration = """
              INSERT INTO event (ticketPrice, totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity) VALUES ()
        """
                ;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(systemConfiguration);

            preparedStatement.setDouble(7, configuration.getTicketPrice());
            preparedStatement.setInt(8, configuration.getTotalTickets());
            preparedStatement.setDouble(9, configuration.getTicketReleaseRate());
            preparedStatement.setDouble(10, configuration.getCustomerRetrievalRate());
            preparedStatement.setInt(11, configuration.getMaxTicketCapacity());

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

    protected void saveVendor() {

    }

    protected void saveCustomer() {

    }

    protected void saveTicketSales() {

    }
}
