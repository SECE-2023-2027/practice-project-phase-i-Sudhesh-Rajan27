package main;

import Dao.ReturnDao;
import Model.CustomerReturn;
import Model.InvalidReturnException;

import java.sql.SQLException;
import java.util.Date;

public class ReturnProcessor implements Runnable {
    private ReturnDao returnDao;
    private int returnId;
    private int customerId;
    private int productId;
    private int quantity;
    private String reason;

    public ReturnProcessor(ReturnDao returnDao, int returnId, int customerId, int productId, int quantity, String reason) {
        this.returnDao = returnDao;
        this.returnId = returnId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.reason = reason;
    }

    @Override
    public void run() {
        try {
            Date returnDate = new Date();  // Get current date
            CustomerReturn returnRecord = new CustomerReturn(returnId, customerId, productId, quantity, reason, returnDate);
            returnDao.processReturn(returnRecord);

            System.out.println("Return processed for Customer ID: " + customerId);
        } catch (InvalidReturnException | SQLException e) {
            System.out.println("Error processing return: " + e.getMessage());
        }
    }
}
