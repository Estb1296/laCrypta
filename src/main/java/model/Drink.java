package model;

public class Drink implements MenuItem{
    private String drink;
    private String name;
    private double price;
    private String description;
    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    @Override
    public String getDescription() { return "Cold beverage"; }
}
