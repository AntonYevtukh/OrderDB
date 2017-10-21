package yevtukh.anton.database;

import yevtukh.anton.model.dao.implementations.mysql.MySqlClientsDao;
import yevtukh.anton.model.dao.implementations.mysql.MySqlOrdersDao;
import yevtukh.anton.model.dao.implementations.mysql.MySqlProductsDao;
import yevtukh.anton.model.dao.implementations.postgresql.PostgreSqlClientsDao;
import yevtukh.anton.model.dao.implementations.postgresql.PostgreSqlOrdersDao;
import yevtukh.anton.model.dao.implementations.postgresql.PostgreSqlProductsDao;
import yevtukh.anton.model.dao.interfaces.ClientsDao;
import yevtukh.anton.model.dao.interfaces.OrdersDao;
import yevtukh.anton.model.dao.interfaces.ProductsDao;
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

        DBMS_NAME = properties.getProperty("dbms.name");

        boolean drop;
        try {
            drop = Integer.parseInt(properties.getProperty("db.drop")) == 1 ? true : false;
        } catch (IllegalArgumentException | NullPointerException e) {
            drop = true;
        }
        DROP = drop;

        boolean environmentConfig;
        try {
            environmentConfig = Integer.parseInt(properties.getProperty("db.environment_config")) == 1 ? true : false;
        } catch (IllegalArgumentException | NullPointerException e) {
            environmentConfig = false;
        }

        if (!environmentConfig) {
            DB_CONNECTION = properties.getProperty("db.url");
            DB_USER = properties.getProperty("db.user");
            DB_PASSWORD = properties.getProperty("db.password");
        } else {
            DB_CONNECTION = System.getenv("JDBC_DATABASE_URL");
            DB_USER = System.getenv("JDBC_DATABASE_USERNAME");
            DB_PASSWORD = System.getenv("JDBC_DATABASE_PASSWORD");
        }
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
        switch (DBMS_NAME) {
            case "mysql":
                return new MySqlClientsDao(getConnection());
            case "postgresql":
                return new PostgreSqlClientsDao(getConnection());
            default:
                throw new RuntimeException("Database management system is not supported");
        }
    }

    public ProductsDao createProductsDao()
            throws SQLException {
        switch (DBMS_NAME) {
            case "mysql":
                return new MySqlProductsDao(getConnection());
            case "postgresql":
                return new PostgreSqlProductsDao(getConnection());
            default:
                throw new RuntimeException("Database management system is not supported");
        }
    }

    public OrdersDao createOrdersDao()
            throws SQLException {
        switch (DBMS_NAME) {
            case "mysql":
                return new MySqlOrdersDao(getConnection());
            case "postgresql":
                return new PostgreSqlOrdersDao(getConnection());
            default:
                throw new RuntimeException("Database management system is not supported");
        }
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
