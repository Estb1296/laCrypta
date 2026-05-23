package model;

import java.util.ArrayList;

public class Order extends MenuItem implements Priceable{
    private final ArrayList<MenuItem> items;

    private double total;

    public Order() {
        super("Order", 0, "Customer Order");
        items = new ArrayList<>();
        total = 0.0;
    }

    public void addItem(MenuItem item) {
        items.add(item);
        total += item.getPrice();
    }
    public ArrayList<MenuItem> getItems() {
        return items;
    }
    @Override
    public double getPrice() {
        return total;
    }
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            total -= items.get(index).getPrice();
            items.remove(index);
        }
    }

    public void clearCart() {
        items.clear();
        total = 0.0;
    }
    @Override
    public double calculatePrice() {
        return total;
    }
    @Override
    public String toReceiptLine() {
        return "Order Total: $" + String.format("%.2f", total);
    }

    @Override
    public String getCategory() {
        return "Order";
    }


}