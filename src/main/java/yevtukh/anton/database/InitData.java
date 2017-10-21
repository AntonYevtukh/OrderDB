package yevtukh.anton.database;


import yevtukh.anton.model.dto.Client;
import yevtukh.anton.model.dto.Item;
import yevtukh.anton.model.dto.Order;
import yevtukh.anton.model.dto.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton on 18.10.2017.
 */
public class InitData {

    public final static List<Client> INITIAL_CLIENTS = initClients();
    public final static List<Product> INITIAL_PRODUCTS = initProducts();
    public final static List<Order> INITIAL_ORDERS = initOrders();
    public final static int MAX_ITEMS = 5;

    private static List<Client> initClients() {
        List<Client> clients = new ArrayList<>();
        Client client1 = new Client(1, "Anton", "Yevtukh", 26);
        Client client2 = new Client(2, "Pavel", "Dobrovolsky", 28);
        Client client3 = new Client(3, "Nikolay", "Yevtukh", 48);
        clients.addAll(Arrays.asList(client1, client2, client3));
        return clients;
    }

    private static List<Product> initProducts() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product(1, "Asus A55V", "Notebook", 10000.00);
        Product product2 = new Product(2, "iPhone 7s", "Smartphone", 15000.00);
        Product product3 = new Product(3, "Asus Google Nexus 7", "Tablet", 3000.00);
        products.addAll(Arrays.asList(product1, product2, product3));
        return products;
    }

    private static List<Order> initOrders() {

        List<Item> items1 = new ArrayList(Arrays.asList(
                new Item(INITIAL_PRODUCTS.get(0), 1, 2),
                new Item(INITIAL_PRODUCTS.get(1), 1, 1),
                new Item(INITIAL_PRODUCTS.get(2), 1, 1)
        ));

        List<Item> items2 = new ArrayList(Arrays.asList(
                new Item(INITIAL_PRODUCTS.get(0), 2, 1),
                new Item(INITIAL_PRODUCTS.get(1), 2, 2),
                new Item(INITIAL_PRODUCTS.get(2), 2, 3)
        ));

        List<Item> items3 = new ArrayList(Arrays.asList(
                new Item(INITIAL_PRODUCTS.get(2), 3, 3),
                new Item(INITIAL_PRODUCTS.get(1), 3, 3),
                new Item(INITIAL_PRODUCTS.get(0), 3, 1)
        ));

        List<Item> items4 = new ArrayList(Arrays.asList(
                new Item(INITIAL_PRODUCTS.get(1), 4, 2),
                new Item(INITIAL_PRODUCTS.get(2), 4, 2),
                new Item(INITIAL_PRODUCTS.get(0), 4, 3)
        ));

        Order order1 = new Order(0, INITIAL_CLIENTS.get(1), items1, 0, null);
        Order order2 = new Order(0, INITIAL_CLIENTS.get(0), items2, 0, null);
        Order order3 = new Order(0, INITIAL_CLIENTS.get(2), items3, 0, null);
        Order order4 = new Order(0, INITIAL_CLIENTS.get(1), items4, 0, null);
        Order order5 = new Order(0, INITIAL_CLIENTS.get(2), items2, 0, null);
        Order order6 = new Order(0, INITIAL_CLIENTS.get(2), items4, 0, null);

        return new ArrayList<>(Arrays.asList(order1, order2, order3, order4, order5, order6));
    }
}
