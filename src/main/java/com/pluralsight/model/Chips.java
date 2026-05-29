package com.pluralsight.model;

public class Chips extends MenuItem {
    private final double price;

    public Chips(String name, double price, String description) {
        super(name, description);
        this.price = price;
    }

    @Override
    public double calculatePrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return "Chips: " + getName() + " ($" + String.format("%.2f", getPrice()) + ")";
    }


    @Override
    public String toReceiptLine() {
        return getName() + " - " + getFormattedPrice();
    }

    @Override
    public String getCategory() {
        return "Chips";
    }
}

