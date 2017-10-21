package yevtukh.anton.model.dao.implementations.postgresql;

import yevtukh.anton.model.dao.interfaces.ClientsDao;
import yevtukh.anton.model.dto.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 14.10.2017.
 */
public class PostgreSqlClientsDao implements ClientsDao {

    private final Connection connection;

    public PostgreSqlClientsDao(Connection connection) {
        this.connection = connection;
    }

    public void insertClient(Client client)
            throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Clients (first_name, last_name, age) VALUES (?, ?, ?)"
        )) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            if (client.getAge() != null)
                preparedStatement.setInt(3, client.getAge());
            else
                preparedStatement.setNull(3, Types.INTEGER);
            preparedStatement.executeUpdate();
        }
    }

    public void insertClients(List<Client> clients)
            throws SQLException {
        for (Client client : clients)
            insertClient(client);
    }

    public List<Client> selectAllClients()
            throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Clients ")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return getClientsFromResultSet(resultSet);
            }
        }
    }

    private List<Client> getClientsFromResultSet(ResultSet resultSet)
            throws SQLException {
        List<Client> clients = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            Integer age = resultSet.getInt(4);
            if (resultSet.wasNull()) {
                age = null;
            }
            clients.add(new Client(id, firstName, lastName, age));
        }
        return clients;
    }

    public void closeConnection()
            throws SQLException {
        if (connection != null)
            connection.close();
    }
}
