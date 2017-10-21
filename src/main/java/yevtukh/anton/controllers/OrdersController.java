package yevtukh.anton.controllers;

import yevtukh.anton.database.DbWorker;
import yevtukh.anton.database.InitData;
import yevtukh.anton.model.dao.interfaces.ClientsDao;
import yevtukh.anton.model.dao.interfaces.OrdersDao;
import yevtukh.anton.model.dao.interfaces.ProductsDao;
import yevtukh.anton.model.dto.Client;
import yevtukh.anton.model.dto.Item;
import yevtukh.anton.model.dto.Order;
import yevtukh.anton.model.dto.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 14.10.2017.
 */
public class OrdersController extends HttpServlet {

    private static final DbWorker DB_WORKER = DbWorker.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("action")) {
            case "add":
                addOrder(req, resp);
                break;
            case "get_all":
                getOrders(req, resp);
                break;
            case "get":
                getOrder(req, resp);
                break;
            default:
                resp.setStatus(400);
                resp.sendRedirect("");
        }
    }

    private void addOrder(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            Order order = parseOrder(req);
            setPreviousRequestParameters(req);
            if (order == null) {
                req.setAttribute("error_message", "Invalid/inconsistent order data, please try again");
                getOrders(req, resp);
                return;
            }
            OrdersDao OrdersDao = DB_WORKER.createOrdersDao();
            OrdersDao.insertOrder(order);
            OrdersDao.closeConnection();
            req.setAttribute("success_message", "Order successfully added");
        } catch (SQLException e) {
            req.setAttribute("error_message", "Internal SQL error");
            System.err.println("Unable to insert the order into table");
            e.printStackTrace();
        } finally {
            getOrders(req, resp);
        }
    }

    private void getOrders(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            ClientsDao clientsDao = DB_WORKER.createClientsDao();
            List<Client> clients = clientsDao.selectAllClients();
            clientsDao.closeConnection();

            ProductsDao productsDao = DB_WORKER.createProductsDao();
            List<Product> products = productsDao.selectAllProducts();
            productsDao.closeConnection();

            OrdersDao ordersDao = DB_WORKER.createOrdersDao();
            List<Order> orders = ordersDao.selectAllOrders();
            ordersDao.closeConnection();
            req.setAttribute("clients", clients);
            req.setAttribute("products", products);
            req.setAttribute("orders", orders);
            req.setAttribute("max_items_count", InitData.MAX_ITEMS - 1);
        } catch (SQLException e) {
            req.setAttribute("error_message", "Internal SQL error");
            System.err.println("Unable to get orders");
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, resp);
    }

    private void getOrder(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            OrdersDao ordersDao = DB_WORKER.createOrdersDao();
            Order order = ordersDao.selectOrderById(id);
            req.setAttribute("order", order);
        } catch (IllegalArgumentException e) {
            System.out.println("Can't parse order id");
        } catch (SQLException e) {
            req.setAttribute("error_message", "Internal SQL error");
            System.err.println("Can't get order from database");
        }

        req.getRequestDispatcher("/WEB-INF/views/order.jsp").forward(req, resp);
    }

    private Order parseOrder(HttpServletRequest req) {

        Order order = null;

        try {
            int clientId = Integer.parseInt(req.getParameter("client_id"));
            if (clientId < 0)
                return null;
            Client client = new Client(clientId, null, null, 0);
            List<Item> items = parseItems(req);
            order = new Order(0, client, items, 0, null);
            if (!order.isValid())
                return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Unable to parse request parameter(s)");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("One or more of parameters is empty");
            e.printStackTrace();
        }

        return order;
    }

    private List<Item> parseItems(HttpServletRequest req) {
        List<Item> items = new ArrayList<>();
        int productId;
        int count;
        try {
            for (int i = 0; i < InitData.MAX_ITEMS; i++) {
                String productIdStr = req.getParameter("product_id_" + i);
                productId = Integer.parseInt(productIdStr);
                if (productId >= 0) {
                    count = Integer.parseInt(req.getParameter("count_" + i));
                    if (count > 0) {
                        Product product = new Product(productId, null, null, 0);
                        items.add(new Item(product, 0, count));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Unable to parse request parameter(s)");
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            System.err.println("One or more of parameters is empty");
            e.printStackTrace();
            return null;
        }
        return items.isEmpty() ? null : items;
    }

    private void setPreviousRequestParameters(HttpServletRequest req) {
        req.setAttribute("client_id", req.getParameter("client_id"));
        List<Integer> productIds = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (int i = 0; i < InitData.MAX_ITEMS; i++) {
            productIds.add(Integer.parseInt(req.getParameter("product_id_" + i)));
            counts.add(Integer.parseInt(req.getParameter("count_" + i)));
        }
        req.setAttribute("product_ids", productIds);
        req.setAttribute("counts", counts);
    }
}
