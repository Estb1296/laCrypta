package com.pluralsight.model;

import java.util.ArrayList;

public class Order extends MenuItem implements Priceable {
    private final ArrayList<MenuItem> items;

    private double total;
    private double couponDiscount = 0.0;

    public Order() {
        super("Order", "Customer Order");
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
        this.couponDiscount = 0.0;
    }

    // Hidden matching key
    public boolean isValidPromoCode(String userCode) {
        return userCode.equals("Craig26@");
    }

    public void setCouponDiscountAmount(double amount) {
        this.couponDiscount = amount;
    }

    public double getCouponDiscount() {
        return this.couponDiscount;
    }

    @Override
    public double calculatePrice() {
        double subtotal = 0.0;
        for (MenuItem item : items) {
            subtotal += item.getPrice();
        }
        double finalTotal = subtotal - couponDiscount;
        return Math.max(0.0, finalTotal); // Keeps total at or above $0.00
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