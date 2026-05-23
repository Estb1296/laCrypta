package data;

import model.Order;
import java.io.IOException;
import java.util.ArrayList;

public class OrderRepository {
    private final ArrayList<Order> orders;
    private final ReceiptWriter receiptWriter;

    /**
     * Constructor - initializes the repository and receipt writer
     */
    public OrderRepository() {
        this.orders = new ArrayList<>();
        this.receiptWriter = new ReceiptWriter();
    }
    public String completeOrder(Order order) throws IOException {
        // Generate and save receipt
        String receiptFilename = receiptWriter.saveReceipt(order);

        // Store the order in history
        orders.add(order);

        return receiptFilename;
    }
    public ArrayList<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
    public int getOrderCount() {
        return orders.size();
    }
    public Order getOrder(int index) {
        if (index >= 0 && index < orders.size()) {
            return orders.get(index);
        }
        return null;
    }
    public void clearAllOrders() {
        orders.clear();
    }
}
