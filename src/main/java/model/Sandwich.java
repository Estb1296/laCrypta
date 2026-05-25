package model;

import java.util.ArrayList;

public class Sandwich extends MenuItem {
    private String bread;
    private SandwichSize size;
    private String meat;
    private String cheese;
    private final ArrayList<Topping> toppings;
    private boolean isToasted = false;
    private final ArrayList<String> sauces = new ArrayList<>();
    private final ArrayList<ExtraTopping> extraToppings = new ArrayList<>();

    public Sandwich() {
        super("Custom Sandwich", 0, "");
        this.toppings = new ArrayList<>();
    }
    public Sandwich(String name, SandwichSize size, String bread, boolean isToasted) {
        super(name, size.getPrice(), "Signature Menu Item");
        this.size = size;
        this.bread = bread;
        this.isToasted = isToasted;
        this.toppings = new ArrayList<>();
    }
    public void setBread(String bread) {
        this.bread = bread;
    }
    public String getBread(){
        return bread;
    }

    public ArrayList<String> getSauces() {
        return sauces;
    }

    public ArrayList<ExtraTopping> getExtraTopping() {
        return extraToppings;
    }

    public enum SandwichSize {
        FOUR("4\"", 5.50, ExtraTopping.ExtraToppingSize.FOUR),
        EIGHT("8\"", 7.00, ExtraTopping.ExtraToppingSize.EIGHT),
        TWELVE("12\"", 8.50, ExtraTopping.ExtraToppingSize.TWELVE);

        private final String display;
        private final double price;
        private final ExtraTopping.ExtraToppingSize extraSize;

        SandwichSize(String display, double price, ExtraTopping.ExtraToppingSize extraSize) {
            this.display = display;
            this.price = price;
            this.extraSize = extraSize;
        }

        public String getDisplay() { return display; }
        public double getPrice() { return price; }
        public ExtraTopping.ExtraToppingSize getExtraSize() { return extraSize; }
    }


    public void setSize(SandwichSize size) {
        this.size = size;
        this.price = size.getPrice();  // Set base price
    }
    public SandwichSize getSize() {
        return size;
    }

    public void setMeat(String meat) {
        this.meat = meat;
        if (this.size != null) {
            double meatPrice = switch(this.size) {
                case FOUR -> 1.00;
                case EIGHT -> 2.00;
                case TWELVE -> 3.00;
            };
            this.addToPrice(meatPrice);
        }
    }
    public void setCheese(String cheese) {
        this.cheese = cheese;
    }

    public void addTopping(Topping topping) {
        this.toppings.add(topping);
        this.addToPrice(topping.price());  // Interface method
    }

    public void removeTopping(int index) {
        if (index >= 0 && index < toppings.size()) {
            this.subtractFromPrice(toppings.get(index).price());
            toppings.remove(index);
        }
    }
    public ArrayList<Topping> getToppings() {
        return toppings;
    }
    public void setToasted(boolean toasted) {
        this.isToasted = toasted;
    }

    public boolean isToasted() {
        return isToasted;
    }
    public void addSauce(String sauce) {
        if (!sauces.contains(sauce)) {
            sauces.add(sauce);
        }
    }
    public void removeSauce(String sauce) {
        sauces.remove(sauce);
    }
    public void addExtraTopping(ExtraTopping extra) {
        extraToppings.add(extra);
        this.addToPrice(extra.getPrice());
    }

    public void removeExtraTopping(int index) {
        if (index >= 0 && index < extraToppings.size()) {
            this.subtractFromPrice(extraToppings.get(index).getPrice());
            extraToppings.remove(index);
        }
    }
    @Override
    public double calculatePrice() {
        return this.price;  // Return current price
    }
    @Override
    public String getName() {
        if (size != null && bread != null) {
            return size.getDisplay() + " " + bread + " Sandwich" + (isToasted() ? " (Toasted)" : "");
        }
        return "Custom Sandwich";
    }
    // public methods return private fields to the user without giving access
    @Override
    public double getPrice() {
        return this.price;  // Return current price
    }
    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sandwich Specifications:\n");
        sb.append("  • Size: ").append(size != null ? size.getDisplay() : "Not Selected").append("\n");
        sb.append("  • Bread: ").append(bread != null ? bread : "Not Selected").append("\n");
        sb.append("  • Meat: ").append(meat != null ? meat : "None").append("\n");
        sb.append("  • Cheese: ").append(cheese != null ? cheese : "None").append("\n");
        sb.append("  • Toasted: ").append(isToasted() ? "Yes" : "No").append("\n");

        // Separate regular and premium
        ArrayList<String> regularNames = new ArrayList<>();
        ArrayList<String> premiumNames = new ArrayList<>();

        for (Topping topping : toppings) {
            if (topping.isPremium()) {
                premiumNames.add(topping.name() + " (+$" + String.format("%.2f", topping.price()) + ")");
            } else {
                regularNames.add(topping.name());
            }
        }

        sb.append("  • Regular Toppings: ")
                .append(regularNames.isEmpty() ? "None" : String.join(", ", regularNames))
                .append("\n");
        sb.append("  • Premium Toppings: ")
                .append(premiumNames.isEmpty() ? "None" : String.join(", ", premiumNames))
                .append("\n");
        sb.append("  • Sauces: ")
                .append(sauces.isEmpty() ? "None" : String.join(", ", sauces))
                .append("\n");

        return sb.toString().trim();
    }

    @Override
    public String toReceiptLine() {
        String toastStatus = isToasted() ? " - TOASTED" : "";
        return getName() + toastStatus + " - $" + String.format("%.2f", getPrice());
    }
    @Override
    public String getCategory() {
        return "Sandwich";
    }
}