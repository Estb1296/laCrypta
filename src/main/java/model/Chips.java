package model;

public class Chips extends MenuItem {
    public Chips(String name, double price, String description) {
        super(name, price, description);
    }

    @Override
    public String getDescription() {
        return "Chips: " + getName() + " ($" + String.format("%.2f", getPrice()) + ")";
    }

    @Override
    public String toReceiptLine() {
        return String.format("%-25s $%6.2f", getName(), getPrice());
    }

    @Override
    public String getCategory() {
        return "Chips";
    }

    @Override
    public double getPrice() {
        return price; // Asks the parent class for the price we sent up
    }
}

