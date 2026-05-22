package model;


public class Topping {
    private final String name;
    private final double price;
    private final boolean isPremium;

    public Topping(String name, double price, boolean isPremium) {
        this.name = name;
        this.price = price;
        this.isPremium = isPremium;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isPremium() {
        return isPremium;
    }
}
