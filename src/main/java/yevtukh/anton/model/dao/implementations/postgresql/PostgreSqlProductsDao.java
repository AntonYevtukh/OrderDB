package yevtukh.anton.model.dao.implementations.postgresql;

import yevtukh.anton.model.dao.interfaces.ProductsDao;
import yevtukh.anton.model.dto.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 14.10.2017.
 */
public class PostgreSqlProductsDao implements ProductsDao {

    private final Connection connection;

    public PostgreSqlProductsDao(Connection connection) {
        this.connection = connection;
    }

    public void insertProduct(Product product)
            throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Products (name, description, price) VALUES (?, ?, ?)"
        )) {
            preparedStatement.setString(1, product.getName());
            if (product.getDescription() != null)
                preparedStatement.setString(2, product.getDescription());
            else
                preparedStatement.setNull(2, Types.VARCHAR);
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.executeUpdate();
        }
    }

    public void insertProducts(List<Product> products)
            throws SQLException {
        for (Product product : products)
            insertProduct(product);
    }

    public List<Product> selectAllProducts()
            throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Products ")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return getProductsFromResultSet(resultSet);
            }
        }
    }

    private List<Product> getProductsFromResultSet(ResultSet resultSet)
            throws SQLException {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String description = resultSet.getString(3);
            if (resultSet.wasNull())
                description = "--None--";
            double price = resultSet.getDouble(4);

            products.add(new Product(id, name, description, price));
        }
        return products;
    }

    public void closeConnection()
            throws SQLException {
        if (connection != null)
            connection.close();
    }
}
