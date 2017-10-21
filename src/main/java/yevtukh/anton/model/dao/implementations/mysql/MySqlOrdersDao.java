package yevtukh.anton.model.dao.implementations.mysql;

import yevtukh.anton.model.dao.interfaces.OrdersDao;
import yevtukh.anton.model.dto.Client;
import yevtukh.anton.model.dto.Item;
import yevtukh.anton.model.dto.Order;
import yevtukh.anton.model.dto.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 14.10.2017.
 */
public class MySqlOrdersDao implements OrdersDao {

    private final Connection connection;

    public MySqlOrdersDao(Connection connection) {
        this.connection = connection;
    }

    public void insertOrder(Order order)
            throws SQLException {
        connection.setAutoCommit(false);
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Orders (client_id) VALUES (?)", Statement.RETURN_GENERATED_KEYS
        )) {
            int orderId;
            preparedStatement.setInt(1, order.getClient().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                orderId = resultSet.getInt(1);
            } else
                throw new SQLException("Order not inserted");

            insertItems(orderId, order.getItems());
        } catch (SQLException e) {
            connection.rollback();
            System.out.println();
            e.printStackTrace();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void insertItems(int orderId, List<Item> items)
            throws SQLException {
        try(PreparedStatement itemPreparedStatement = connection.prepareStatement(
                "INSERT INTO Items (order_id, product_id, count) VALUES (?, ?, ?)"
        )) {
            for (Item item : items) {
                itemPreparedStatement.setInt(1, orderId);
                itemPreparedStatement.setInt(2, item.getProduct().getId());
                itemPreparedStatement.setInt(3, item.getCount());
                itemPreparedStatement.executeUpdate();
            }
        }
    }

    public void insertOrders(List<Order> orders)
            throws SQLException {
        for (Order order : orders)
            insertOrder(order);
    }

    public List<Order> selectAllOrders()
            throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                //MySQL only
                "SELECT Orders.id, Orders.datetime, Clients.first_name, Clients.last_name, " +
                        "sum(Products.price * Items.count) AS total_price " +
                        "FROM Orders LEFT JOIN Clients ON Orders.client_id = Clients.id LEFT JOIN " +
                        "Items ON Items.order_id = Orders.id LEFT JOIN Products ON Items.product_id = Products.id " +
                        "GROUP BY Orders.id  " +
                        "ORDER BY Orders.datetime DESC;"
        )) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return getOrdersFromResultSet(resultSet);
            }
        }
    }

    public Order selectOrderById(int id) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT Orders.datetime, Clients.first_name, Clients.last_name, " +
                        "sum(Products.price * Items.count) AS total_price " +
                        "FROM Orders LEFT JOIN Clients ON Orders.client_id = Clients.id LEFT JOIN " +
                        "Items ON Items.order_id = Orders.id LEFT JOIN Products ON Items.product_id = Products.id " +
                        "WHERE Orders.id = ? " +
                        "GROUP BY Orders.id  " +
                        "ORDER BY Orders.datetime DESC;"
        )) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return getOrderFromResultSet(resultSet, id);
            }
        }
    }

    private List<Order> getOrdersFromResultSet(ResultSet resultSet)
            throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            Timestamp dateTime = resultSet.getTimestamp(2);
            String clientFirstName = resultSet.getString(3);
            String clientLastName = resultSet.getString(4);
            double totalPrice = resultSet.getDouble(5);
            orders.add(new Order(id, new Client(0, clientFirstName, clientLastName, 0),
                    null, totalPrice, dateTime));
        }
        return orders;
    }

    private Order getOrderFromResultSet(ResultSet resultSet, int orderId)
            throws SQLException {
        if (resultSet.next()) {
            Timestamp dateTime = resultSet.getTimestamp(1);
            String clientFirstName = resultSet.getString(2);
            String clientLastName = resultSet.getString(3);
            double totalPrice = resultSet.getDouble(4);
            List<Item> items = selectItemsByOrderId(orderId);
            return new Order(orderId, new Client(0, clientFirstName, clientLastName, 0),
                    items, totalPrice, dateTime);
        }
        return null;
    }

    private List<Item> selectItemsByOrderId(int id)
            throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT Products.id, Products.name, Products.price, Items.count " +
                        "FROM Items LEFT JOIN Products ON Items.product_id = Products.id " +
                        "WHERE Items.order_id = ?;"
        )) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return getItemsFromResultSet(resultSet, id);
            }
        }
    }

    private List<Item> getItemsFromResultSet(ResultSet resultSet, int orderId)
        throws SQLException {
        List<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            int productId = resultSet.getInt(1);
            String productName = resultSet.getString(2);
            double productPrice = resultSet.getDouble(3);
            int count = resultSet.getInt(4);
            items.add(new Item(new Product(productId, productName, null, productPrice), orderId, count));
        }
        if (items.isEmpty())
            throw new SQLDataException("Empty order");
        return items;
    }

    public void closeConnection()
            throws SQLException {
        if (connection != null)
            connection.close();
    }
}
