package Model;

import java.util.Date;

public abstract class Return {
    private int returnId;
    private int customerId;
    private int productId;
    private int quantity;
    private String returnReason;
    private Date returnDate;

    // Constructors
    public Return(int returnId, int customerId, int productId, int quantity, String returnReason, Date returnDate) {
        this.returnId = returnId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.returnReason = returnReason;
        this.returnDate = returnDate;
    }

    // Getters and Setters
    public int getReturnId() { return returnId; }
    public void setReturnId(int returnId) { this.returnId = returnId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
}
