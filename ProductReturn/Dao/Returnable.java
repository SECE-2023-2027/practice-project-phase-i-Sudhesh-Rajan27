package Dao;

import Model.InvalidReturnException;
import Model.Return;
import Model.StockUpdateFailureException;

import java.sql.SQLException;
import java.util.List;

public interface Returnable {
    void updateStock(int productId, int quantity) throws SQLException, StockUpdateFailureException;
    List<Return> generateReturnReport() throws SQLException;
	void processReturn(Return returnItem) throws SQLException, InvalidReturnException;
}
