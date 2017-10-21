package yevtukh.anton.controllers;

import yevtukh.anton.database.DbWorker;
import yevtukh.anton.model.dao.ProductsDao;
import yevtukh.anton.model.dto.Product;

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
public class ProductsController extends HttpServlet {

    private static final DbWorker DB_WORKER = DbWorker.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("action")) {
            case "add":
                addProduct(req, resp);
                break;
            case "get":
                getProducts(req, resp);
                break;
            default:
                resp.setStatus(400);
                resp.sendRedirect("");
        }
    }

    private void addProduct(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            Product product = parseProduct(req);
            setPreviousRequestParameters(req);
            if (product == null) {
                req.setAttribute("error_message", "Invalid/inconsistent product data, please try again");
                getProducts(req, resp);
                return;
            }
            ProductsDao ProductsDao = DB_WORKER.createProductsDao();
            ProductsDao.insertProduct(product);
            ProductsDao.closeConnection();
            req.setAttribute("success_message", "Product successfully added");
        } catch (SQLException e) {
            req.setAttribute("error_message", "Internal SQL error");
            System.err.println("Unable to insert the Flat into table");
            e.printStackTrace();
        } finally {
            getProducts(req, resp);
        }
    }

    private void getProducts(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            ProductsDao ProductsDao = DB_WORKER.createProductsDao();
            List<Product> products = ProductsDao.selectAllProducts();
            req.setAttribute("products", products);
        } catch (SQLException e) {
            req.setAttribute("error_message", "Internal SQL error");
            System.err.println("Unable to get Flats");
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
    }

    private Product parseProduct(HttpServletRequest req) {

        Product product = null;

        try {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            double price = Double.parseDouble(req.getParameter("price"));
            product = new Product(0, name, description, price);
            if (!product.isValid())
                product = null;
        } catch (IllegalArgumentException e) {
            System.err.println("Unable to parse request parameter(s)");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("One or more of parameters is empty");
            e.printStackTrace();
        }

        return product;
    }

    private void setPreviousRequestParameters(HttpServletRequest req) {
        req.setAttribute("name", req.getParameter("name"));
        req.setAttribute("description", req.getParameter("description"));
        req.setAttribute("price", req.getParameter("price"));
    }
}
