package main;

import Dao.DatabaseConnection;
import Dao.ReturnDao;
import Model.CustomerReturn;
import Model.InvalidReturnException;
import Model.Return;

import java.sql.Connection;
import java.util.Date;

public class ProductReturnSystem {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            ReturnDao returnDao = new ReturnDao(connection);

            // Example return processing with multithreading
            int customerId = 1;
            int productId = 2;
            int quantity = 1;
            String reason = "Defective item";

            // Use multiple threads to handle concurrent return processing
            Thread returnThread1 = new Thread(() -> processReturn(returnDao, customerId, productId, quantity, reason));
            Thread returnThread2 = new Thread(() -> processReturn(returnDao, customerId + 1, productId + 1, quantity, "Not as described"));

            returnThread1.start();
            returnThread2.start();

            // Wait for threads to complete
            returnThread1.join();
            returnThread2.join();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void processReturn(ReturnDao returnDao, int customerId, int productId, int quantity, String reason) {
        try {
            // Create a Return object (CustomerReturn in this case)
            Date returnDate = new Date();  // Use the current date for the return
            Return returnRecord = new CustomerReturn(0, customerId, productId, quantity, reason, returnDate);  // Assuming 0 for returnId (will be set later)

            returnDao.processReturn(returnRecord);
            System.out.println("Return processed successfully for Customer ID: " + customerId);
        } catch (InvalidReturnException e) {
            System.out.println("Invalid return: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("General error: " + e.getMessage());
        }
    }
}
