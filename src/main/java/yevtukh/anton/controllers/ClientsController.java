package yevtukh.anton.controllers;

import yevtukh.anton.database.DbWorker;
import yevtukh.anton.model.dao.interfaces.ClientsDao;
import yevtukh.anton.model.dto.Client;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Anton on 14.10.2017.
 */
public class ClientsController extends HttpServlet {

    private static final DbWorker DB_WORKER = DbWorker.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("action")) {
            case "add":
                addClient(req, resp);
                break;
            case "get":
                getClients(req, resp);
                break;
            default:
                resp.setStatus(400);
                resp.sendRedirect("");
        }
    }

    private void addClient(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            Client client = parseClient(req);
            setPreviousRequestParameters(req);
            if (client == null) {
                req.setAttribute("error_message", "Invalid/inconsistent client data, please try again");
                getClients(req, resp);
                return;
            }
            ClientsDao clientsDao = DB_WORKER.createClientsDao();
            clientsDao.insertClient(client);
            clientsDao.closeConnection();
            req.setAttribute("success_message", "Client successfully added");
        } catch (SQLException e) {
            req.setAttribute("error_message", "Internal SQL error");
            System.err.println("Unable to insert the client into table");
            e.printStackTrace();
        } finally {
            getClients(req, resp);
        }
    }

    private void getClients(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            ClientsDao clientsDao = DB_WORKER.createClientsDao();
            List<Client> clients = clientsDao.selectAllClients();
            req.setAttribute("clients", clients);
        } catch (SQLException e) {
            req.setAttribute("error_message", "Internal SQL error");
            System.err.println("Unable to get clients");
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/views/clients.jsp").forward(req, resp);
    }

    private Client parseClient(HttpServletRequest req) {

        Client client = null;

        try {
            String firstName = req.getParameter("first_name");
            String lastName = req.getParameter("last_name");
            String ageString = req.getParameter("age");
            Integer age = (ageString == null || ageString.isEmpty()) ?
                    null : Integer.parseInt(ageString);
            client = new Client(0, firstName, lastName, age);
            if (!client.isValid())
                client = null;
        } catch (IllegalArgumentException e) {
            System.err.println("Unable to parse request parameter(s)");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("One or more of parameters is empty");
            e.printStackTrace();
        }

        return client;
    }

    private void setPreviousRequestParameters(HttpServletRequest req) {
        req.setAttribute("first_name", req.getParameter("first_name"));
        req.setAttribute("last_name", req.getParameter("last_name"));
        req.setAttribute("age", req.getParameter("age"));
    }
}
