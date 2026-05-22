package model;

import java.util.ArrayList;

public class Order {
    private final ArrayList<MenuItem> items;
    private double total;

    public Order() {
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

    public double getTotal() {
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
    public double calculateTotal() {
        double grandTotal = 0.0;
        for (MenuItem item : items) {
            // Polymorphically checks the specific subclass calculation on the fly
            grandTotal += item.getPrice();
        }
        return grandTotal;
    }
}