package yevtukh.anton.model.dao.interfaces;

import yevtukh.anton.model.dto.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 14.10.2017.
 */
public interface ProductsDao {

    void insertProduct(Product product) throws SQLException;
    void insertProducts(List<Product> products) throws SQLException;
    List<Product> selectAllProducts() throws SQLException;
    void closeConnection() throws SQLException;

}
