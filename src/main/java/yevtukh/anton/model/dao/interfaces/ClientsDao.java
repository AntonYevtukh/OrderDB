package yevtukh.anton.model.dao.interfaces;

import yevtukh.anton.model.dto.Client;

import java.sql.*;
import java.util.List;

/**
 * Created by Anton on 14.10.2017.
 */
public interface ClientsDao {

    void insertClient(Client client) throws SQLException;
    void insertClients(List<Client> clients) throws SQLException;
    List<Client> selectAllClients() throws SQLException;
    public void closeConnection() throws SQLException;
}
