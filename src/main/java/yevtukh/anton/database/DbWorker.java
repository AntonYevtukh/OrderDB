package yevtukh.anton.database;

import yevtukh.anton.model.dao.ClientsDao;
import yevtukh.anton.model.dao.OrdersDao;
import yevtukh.anton.model.dao.ProductsDao;
import yevtukh.anton.model.dto.Client;
import yevtukh.anton.model.dto.Order;
import yevtukh.anton.model.dto.Product;

import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by Anton on 14.10.2017.
 */
public class DbWorker {

    private final String DB_CONNECTION;
    private final String DB_USER;
    private final String DB_PASSWORD;
    private final String DBMS_NAME;
    private final boolean DROP;
    private static DbWorker instance;

    public static DbWorker getInstance() {
        if (instance == null) {
            try {
                instance = new DbWorker();
            } catch (Exception e) {
                System.out.println("Can't get instance of DbWorker");
                e.printStackTrace();
            }
        }
        return instance;
    }

    private DbWorker()
            throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        DB_CONNECTION = properties.getProperty("db.url");
        DB_USER = properties.getProperty("db.user");
        DB_PASSWORD = properties.getProperty("db.password");
        DBMS_NAME = properties.getProperty("dbms.name");

        boolean drop;
        try {
            drop = Integer.parseInt(properties.getProperty("db.drop")) == 1 ? true : false;
        } catch (IllegalArgumentException | NullPointerException e) {
            drop = true;
        }
        DROP = drop;
    }

    public void initDB()
        throws SQLException, ClassNotFoundException {
        getDbInitializer().initDB(DROP);
    }

    public Connection getConnection()
            throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
    }

    public ClientsDao createClientsDao()
            throws SQLException {
        return new ClientsDao(getConnection());
    }

    public ProductsDao createProductsDao()
            throws SQLException {
        return new ProductsDao(getConnection());
    }

    public OrdersDao createOrdersDao()
            throws SQLException {
        return new OrdersDao(getConnection());
    }

    public void fillDb()
            throws SQLException {
        fillClients(InitData.INITIAL_CLIENTS);
        fillProducts(InitData.INITIAL_PRODUCTS);
        fillOrders(InitData.INITIAL_ORDERS);
    }

    private void fillClients(List<Client> clients)
            throws SQLException {
        if (clients != null) {
            ClientsDao clientsDao = createClientsDao();
            clientsDao.insertClients(clients);
            clientsDao.closeConnection();
        }
    }

    private void fillProducts(List<Product> products)
            throws SQLException {
        if (products != null) {
            ProductsDao productsDao = createProductsDao();
            productsDao.insertProducts(products);
            productsDao.closeConnection();
        }
    }

    private void fillOrders(List<Order> orders)
            throws SQLException {
        if (orders != null) {
            OrdersDao ordersDao = createOrdersDao();
            ordersDao.insertOrders(orders);
            ordersDao.closeConnection();
        }
    }

    public DbInitialiser getDbInitializer()
            throws SQLException, ClassNotFoundException {
        switch (DBMS_NAME) {
            case "mysql":
                return new MySqlInitializer();
            case "postgresql":
                return new PostgreSqlInitializer();
            default:
                throw new RuntimeException("Database management system is not supported");
        }
    }
}
