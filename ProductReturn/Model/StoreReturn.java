package Model;

import java.util.Date;

public class StoreReturn extends Return {
    public StoreReturn(int returnId, int customerId, int productId, int quantity, String returnReason, Date returnDate) {
        super(returnId, customerId, productId, quantity, returnReason, returnDate);
    }
}
