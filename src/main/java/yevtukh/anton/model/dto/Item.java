package yevtukh.anton.model.dto;

/**
 * Created by Anton on 18.10.2017.
 */
public class Item {

    private Product product;
    private int orderId;
    private int count;

    public Item(Product product, int orderId, int count) {
        this.product = product;
        this.orderId = orderId;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCount() {
        return count;
    }

    public boolean isValid() {
        return (product != null && product.getId() >=0 && count > 0);
    }
}
