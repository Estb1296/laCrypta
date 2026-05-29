package com.pluralsight.model;

public abstract class MenuItem implements Priceable {
    private final String name;
    private final String description;

    protected MenuItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getFormattedPrice() {
        return String.format("$%.2f", getPrice());
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return calculatePrice();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public abstract double calculatePrice();

    public abstract String toReceiptLine();

    public abstract String getCategory();
}
