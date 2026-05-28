package com.pluralsight.model;
public class Drink extends MenuItem {
    private static final double PRICE_SMALL = 2.00;
    private static final double PRICE_MEDIUM = 2.50;
    private static final double PRICE_LARGE = 3.00;
    private String size; // Tracks "Small", "Medium", "Large"


    public Drink(String name) {
        super(name, "Drink");
    }
    public Drink(String name, String size) {
        super(name, "Drink"); // Pass name and type to the parent class
        this.size = size;     // Save the size in this Drink object
    }
    @Override
    public double getPrice() {
        if (size == null) return 0.0;

        return switch (size) {
            case "Small"  -> 1.99; // Matches your UI Drink pricing ($1.99)
            case "Medium" -> 2.50;
            case "Large"  -> 3.00;
            default       -> 0.0;
        };
    }// Pulls pricing matrix out of variables and computes on demand
    @Override
    public double calculatePrice() {
        if (size == null) return 0.0;
        return switch (size) {
            case "Small" -> PRICE_SMALL;
            case "Medium" -> PRICE_MEDIUM;
            case "Large" -> PRICE_LARGE;
            default -> 0.0;
        };
    }


    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }



    @Override
    public String getName() {
        String baseName = super.getName(); // Grabs the name sent to the constructor (e.g. "Coca-Cola")
        if (this.size == null) {
            return baseName;
        }
        return this.size + " " + baseName; // Returns "Medium Coca-Cola"
    }
    @Override
    public String getDescription() {
        return "Drink: " + getName();
    }// Generates fresh description strings whenever checked out



    @Override
    public String toReceiptLine() {
        return getName() + " - " + getFormattedPrice();
    }

    @Override
    public String getCategory() {
        return "Drink";
    }

    }
