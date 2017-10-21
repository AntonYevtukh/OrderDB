package yevtukh.anton.model.dto;

/**
 * Created by Anton on 18.10.2017.
 */
public class Product {

    private final int id;
    private final String name;
    private final String description;
    private final double price;

    public Product(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = (description == null || description.isEmpty()) ? null : description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isValid() {
        if (name == null || name.isEmpty() || price <= 0)
            return false;
        return true;
    }
}
