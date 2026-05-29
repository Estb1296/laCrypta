package com.pluralsight.model;

import com.pluralsight.ui.util.PromoConfiguration;

import java.util.ArrayList;

public class Order extends MenuItem implements Priceable {
    private final ArrayList<MenuItem> items;

    private double couponDiscount = 0.0;

    public Order() {
        super("Order", "Customer Order");
        items = new ArrayList<>();

    }

    public void addItem(MenuItem item) {
        items.add(item);

    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    @Override
    public double getPrice() {
        return calculatePrice();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public void clearCart() {
        items.clear();
        this.couponDiscount = 0.0;
    }

    // Hidden matching key
    public boolean isValidPromoCode(String userCode) {
        return userCode.equals(PromoConfiguration.PROMO_CODE);
    }

    public void setCouponDiscountAmount(double amount) {
        this.couponDiscount = amount;
    }

    public double getCouponDiscount() {
        return this.couponDiscount;
    }

    @Override
    public double calculatePrice() {
        double subtotal = items.stream()
                .mapToDouble(MenuItem::getPrice)
                .sum();
        double finalTotal = subtotal - couponDiscount;
        return Math.max(0.0, finalTotal); // Keeps total at or above $0.00
    }

    @Override
    public String toReceiptLine() {
        return "Order Total: $" + String.format("%.2f", getPrice());
    }

    @Override
    public String getCategory() {
        return "Order";
    }


}