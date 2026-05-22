package model;

import java.util.ArrayList;

public class Sandwich extends BaseMenuItem {
    private String bread;
    private SandwichSize size;
    private String meat;
    private String cheese;
    private final ArrayList<Topping> topping;

    private double sizePriceModifier = 0.0; // Tracks the base price of the size selection
    private double toppingsPriceModifier = 0.0; // Tracks extra costs for additions

    public Sandwich(String name, double price) {
        super(name, price); // Passes the initial values straight up to BaseMenuItem
        this.topping = new ArrayList<>();
    }
    public void setBread(String bread) {
        this.bread = bread;
    }
    public enum SandwichSize {
        FOUR("4\"", 6.99),
        EIGHT("8\"", 8.99),
        TWELVE("12\"", 10.99);

        private final String display;
        private final double price;

        SandwichSize(String display, double price) {
            this.display = display;
            this.price = price;
        }

        public String getDisplay() { return display; }
        public double getPrice() { return price; }
    }


    public void setSize(SandwichSize size) {
        this.size = size;
        this.sizePriceModifier = size.getPrice();
    }

    public void setMeat(String meat) {
        this.meat = meat;
    }
    public void setCheese(String cheese) {
        this.cheese = cheese;
    }

    public void addTopping(Topping topping) {
        this.topping.add(topping);
        this.toppingsPriceModifier += topping.getPrice();
    }
    // public methods return private fields to the user without giving access
    @Override
    public String getName() {
        if (size != null && bread != null) {
            return size + " " + bread + " Sandwich";
        }
        return super.getName(); // Falls back to "Sandwich" if building hasn't started yet
    }
    @Override
    public double getPrice() {
        return this.sizePriceModifier + this.toppingsPriceModifier;
    }
    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sandwich Specifications:\n");
        sb.append("  • Size: ").append(size != null ? size.getDisplay() : "Not Selected").append("\n");
        sb.append("  • Bread: ").append(bread != null ? bread : "Not Selected").append("\n");
        sb.append("  • Meat: ").append(meat != null ? meat : "None").append("\n");
        sb.append("  • Cheese: ").append(cheese != null ? cheese : "None").append("\n");

        // Separate regular and premium
        ArrayList<String> regularNames = new ArrayList<>();
        ArrayList<String> premiumNames = new ArrayList<>();

        for (Topping toppings : topping) {
            if (toppings.isPremium()) {
                premiumNames.add(toppings.getName() + " (+$" + String.format("%.2f", toppings.getPrice()) + ")");
            } else {
                regularNames.add(toppings.getName());
            }
        }

        sb.append("  • Regular Toppings: ")
                .append(regularNames.isEmpty() ? "None" : String.join(", ", regularNames))
                .append("\n");
        sb.append("  • Premium Toppings: ")
                .append(premiumNames.isEmpty() ? "None" : String.join(", ", premiumNames))
                .append("\n");

        return sb.toString().trim();
    }
}