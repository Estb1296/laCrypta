package model;

import java.util.ArrayList;

public class Sandwich implements MenuItem {
    private final String name;
    private double price;
    private final String description;
    private String bread;
    private String size;
    private String meat;
    private String cheese;
    private final ArrayList<String> toppings;

    public Sandwich() {
        this.name = "Custom Sandwich";
        this.price = 8.99;  // Base price
        this.description = getDescription();
        this.toppings = new ArrayList<>();
    }
    public void setBread(String bread) {
        this.bread = bread;
    }

    public void setSize(String size) {
        this.size = size;
        // Adjust price based on size
        switch (size) {
            case "Large" -> this.price = 10.99;
            case "Medium" -> this.price = 8.99;
            case "Small" -> this.price = 6.99;
        }
    }
    public void setMeat(String meat) {
        this.meat = meat;
    }
    public void setCheese(String cheese) {
        this.cheese = cheese;
    }

    public void addTopping(String topping) {
        toppings.add(topping);
        this.price += 0.50;  // Each topping costs extra
    }
    // public methods return private fields to the user without giving access
    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return "Size" +size+"Bread: " + bread + ", Meat: " + meat +
                ", Cheese: " + cheese + ", Toppings: " + toppings;
    }
}