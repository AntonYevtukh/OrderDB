package yevtukh.anton.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Anton on 17.10.2017.
 */
public class MySqlInitializer implements DbInitialiser {

    @Override
    public void initDB(boolean drop)
            throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DbWorker.getInstance().getConnection();
        try(Statement statement = connection.createStatement()){
            if (drop) {
                statement.execute("DROP TABLE IF EXISTS Items");
                statement.execute("DROP TABLE IF EXISTS Orders");
                statement.execute("DROP TABLE IF EXISTS Clients");
                statement.execute("DROP TABLE IF EXISTS Products");
            }

            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Clients (" +
                            "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                            "first_name VARCHAR(30) NOT NULL," +
                            "last_name VARCHAR(30) NOT NULL," +
                            "age INT NULL" +
                            ");");

            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Products (" +
                            "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                            "name VARCHAR(30) NOT NULL," +
                            "description VARCHAR(512) NULL," +
                            "price REAL NULL" +
                            ");");

            statement.execute("CREATE TABLE IF NOT EXISTS Orders (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "client_id INT NOT NULL," +
                    "datetime TIMESTAMP,\n" +
                    "FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS Items (" +
                    "order_id INT NOT NULL," +
                    "product_id INT NOT NULL, \n" +
                    "count INT NOT NULL,\n" +
                    "FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (product_id) REFERENCES Products(id)" +
                    ");");
        }

        if (drop) {
            DbWorker.getInstance().fillDb();
        }
    }
}
