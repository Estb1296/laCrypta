package model;

public class Chips extends MenuItem {
    public Chips(String name, double price, String description) {
        super(name, price, description);
    }
    public String getDescription() {
        return "Chips: " + getName() + " ($" + String.format("%.2f", getPrice()) + ")";
    }

    @Override
    public String toReceiptLine() {
        return "";
    }

    @Override
    public String getCategory() {
        return "";
    }

    @Override
    public double getPrice() {
        return price; // Asks the parent class for the price we sent up
    }
}

