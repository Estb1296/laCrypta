package model;

public class Chips extends BaseMenuItem{
    private final String flavor;
    public Chips(String flavor) {
        super(flavor, 1.50); // Sends the flavor as the item name and 1.50 as the price up to BaseMenuItem
        this.flavor = flavor;
    }
    @Override
    public String getDescription() {
        return "Chips: " + flavor + " ($" + String.format("%.2f", getPrice()) + ")";
    }

    @Override
    public double getPrice() {
        return basePrice; // Asks the parent class for the price we sent up
    }
}

