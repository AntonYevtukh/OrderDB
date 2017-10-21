package yevtukh.anton.model.dao.interfaces;

import yevtukh.anton.model.dto.Order;

import java.sql.*;
import java.util.List;

/**
 * Created by Anton on 14.10.2017.
 */
public interface OrdersDao {

    void insertOrder(Order order) throws SQLException;
    void insertOrders(List<Order> orders) throws SQLException;
    List<Order> selectAllOrders() throws SQLException;
    Order selectOrderById(int id) throws SQLException;
    void closeConnection() throws SQLException;
}
