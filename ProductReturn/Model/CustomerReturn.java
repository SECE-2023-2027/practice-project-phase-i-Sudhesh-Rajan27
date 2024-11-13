package Model;

import java.util.Date;

public class CustomerReturn extends Return {
    public CustomerReturn(int returnId, int customerId, int productId, int quantity, String returnReason, Date localDateTime) {
        super(returnId, customerId, productId, quantity, returnReason, localDateTime);
    }
}
