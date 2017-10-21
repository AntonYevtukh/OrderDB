package yevtukh.anton.model.dto;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Anton on 18.10.2017.
 */
public class Order {

    private final int id;
    private final Client client;
    private final List<Item> items;
    private final double totalPrice;
    private final Timestamp timestamp;

    public Order(int id, Client client, List<Item> items, double totalPrice, Timestamp timestamp) {
        this.id = id;
        this.client = client;
        this.items = items;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isValid() {
        return (client != null && client.getId() >= 0 && items != null && !items.isEmpty());
    }
}
