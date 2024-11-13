package Dao;

import Model.CustomerReturn;
import Model.InvalidReturnException;
import Model.Return;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnDao implements Returnable {
    private Connection connection;

    public ReturnDao(Connection connection) {
        this.connection = connection;
    }

    // Process a return request
    @Override
    public void processReturn(Return returnItem) throws SQLException, InvalidReturnException {
        int stockLevel = getStockLevel(returnItem.getProductId());  // Check stock level
        if (stockLevel < returnItem.getQuantity()) {
            throw new InvalidReturnException("Insufficient stock to process the return.");
        }

        String query = "CALL ProcessReturn(?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, returnItem.getCustomerId());
            stmt.setInt(2, returnItem.getProductId());
            stmt.setInt(3, returnItem.getQuantity());
            stmt.setString(4, returnItem.getReturnReason());
            stmt.executeUpdate();

            // Log the return to a file for auditing
            logReturnToFile(returnItem);
        }
    }

    // Update stock levels for a specific product
    @Override
    public void updateStock(int productId, int quantity) throws SQLException {
        String query = "UPDATE Product SET stock_level = stock_level + ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }

    // Generate return report
    @Override
    public List<Return> generateReturnReport() throws SQLException {
        List<Return> report = new ArrayList<>();
        String query = "SELECT * FROM ReturnHistory";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Create Return object from each record in the result set
                int returnId = rs.getInt("return_id");
                int customerId = rs.getInt("customer_id");  // Assuming there's a customer_id column
                int productId = rs.getInt("product_id");    // Assuming there's a product_id column
                int quantity = rs.getInt("quantity");
                String returnReason = rs.getString("return_reason");
                Date returnDate = rs.getDate("return_date");  // Assuming return_date is of type Date in DB

                // Create a Return object
                Return returnRecord = new CustomerReturn(returnId, customerId, productId, quantity, returnReason, returnDate);
                
                // Add the Return object to the report list
                report.add(returnRecord);
            }
        }
        return report;
    }


    // Log return to file for auditing
    private void logReturnToFile(Return returnItem) {
        String log = "Customer ID: " + returnItem.getCustomerId() +
                     ", Product ID: " + returnItem.getProductId() +
                     ", Quantity: " + returnItem.getQuantity() +
                     ", Reason: " + returnItem.getReturnReason() +
                     ", Return Date: " + returnItem.getReturnDate() + "\n";  // Added return date
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("return_log.txt", true))) {
            writer.write(log);
        } catch (IOException e) {
            System.out.println("Failed to log return: " + e.getMessage());
        }
    }

    // Get stock level for a product
    private int getStockLevel(int productId) throws SQLException {
        String query = "SELECT stock_level FROM Product WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock_level");
            } else {
                throw new SQLException("Product not found with ID: " + productId);
            }
        }
    }
}
