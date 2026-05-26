package model;

import java.util.ArrayList;

public class Sandwich extends MenuItem {
    //Static constants
    private static final double MEAT_PREMIUM_FOUR = 1.00;
    private static final double MEAT_PREMIUM_EIGHT = 2.00;
    private static final double MEAT_PREMIUM_TWELVE = 3.00;

    private static final double CHEESE_PREMIUM_FOUR = 1.00;
    private static final double CHEESE_PREMIUM_EIGHT = 2.00;
    private static final double CHEESE_PREMIUM_TWELVE = 3.00;

    private String bread;
    private SandwichSize size;
    private String meat;
    private String cheese;
    private final ArrayList<Topping> toppings;
    private boolean isToasted = false;
    private final ArrayList<String> sauces = new ArrayList<>();
    private final ArrayList<ExtraTopping> extraToppings = new ArrayList<>();

    public Sandwich() {
        super("Custom Sandwich", "");
        this.toppings = new ArrayList<>();
    }

    public Sandwich(String name, SandwichSize size, String bread, boolean isToasted) {
        super(name, "Signature Menu Item");
        this.size = size;
        this.bread = bread;
        this.isToasted = isToasted;
        this.toppings = new ArrayList<>();
    }

    public Sandwich(SandwichSize size, String bread, boolean isToasted) {
        this("Signature Menu Item", size, bread, isToasted);
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

        public String getDisplay() {
            return display;
        }

        public double getPrice() {
            return price;
        }

        public ExtraTopping.ExtraToppingSize getExtraSize() {
            return extraSize;
        }
    }

    // ====== Price Calculation (All Price Logic Here) ======
    @Override
    public double calculatePrice() {
        double total = 0;

        // Base price from size
        if (size != null) {
            total += size.getPrice();
        }

        // Meat premium
        if (meat != null && !meat.equalsIgnoreCase("None")) {
            total += getMeatPremium();
        }

        // Cheese premium
        if (cheese != null && !cheese.equalsIgnoreCase("None")) {
            total += getCheesePremium();
        }

        // Regular and premium toppings
        for (Topping t : toppings) {
            total += t.price();
        }

        // Extra toppings
        for (ExtraTopping e : extraToppings) {
            total += e.getPrice();
        }

        return total;
    }

    /**
     * Helper: Get meat premium based on current size.
     * Extracted to eliminate duplicate switch blocks.
     */
    private double getMeatPremium() {
        return switch (this.size) {
            case FOUR -> MEAT_PREMIUM_FOUR;
            case EIGHT -> MEAT_PREMIUM_EIGHT;
            case TWELVE -> MEAT_PREMIUM_TWELVE;
        };
    }

    /**
     * Helper: Get cheese premium based on current size.
     * Extracted to eliminate duplicate switch blocks.
     */
    private double getCheesePremium() {
        return switch (this.size) {
            case FOUR -> CHEESE_PREMIUM_FOUR;
            case EIGHT -> CHEESE_PREMIUM_EIGHT;
            case TWELVE -> CHEESE_PREMIUM_TWELVE;
        };
    }

    // ====== Setters (No Price Logic) ======
    public void setBread(String bread) {
        this.bread = bread;
    }

    public void setSize(SandwichSize size) {
        this.size = size;
        // No price mutation here — calculatePrice() handles it
    }

    public void setMeat(String newMeat) {
        this.meat = newMeat;
        // No price mutation here — calculatePrice() handles it
    }

    public void setCheese(String newCheese) {
        this.cheese = newCheese;
        // No price mutation here — calculatePrice() handles it
    }

    public void setToasted(boolean toasted) {
        this.isToasted = toasted;
    }

    // ====== Getters ======
    public String getBread() {
        return bread;
    }

    public SandwichSize getSize() {
        return size;
    }

    public String getMeat() {
        return meat;
    }

    public String getCheese() {
        return cheese;
    }

    public boolean isToasted() {
        return isToasted;
    }

    public ArrayList<Topping> getToppings() {
        return toppings;
    }

    public ArrayList<String> getSauces() {
        return sauces;
    }

    public ArrayList<ExtraTopping> getExtraTopping() {
        return extraToppings;
    }

    // ====== Topping Management (No Price Logic) ======
    public void addTopping(Topping topping) {
        toppings.add(topping);
        // No price mutation here — calculatePrice() handles it
    }

    public void removeTopping(int index) {
        if (index >= 0 && index < toppings.size()) {
            toppings.remove(index);
            // No price mutation here — calculatePrice() handles it
        }
    }

    // ====== Sauce Management ======
    public void addSauce(String sauce) {
        if (!sauces.contains(sauce)) {
            sauces.add(sauce);
        }
    }

    public void removeSauce(String sauce) {
        sauces.remove(sauce);
    }

    // ====== Extra Topping Management (No Price Logic) ======
    public void addExtraTopping(ExtraTopping extra) {
        extraToppings.add(extra);
        // No price mutation here — calculatePrice() handles it
    }

    public void removeExtraTopping(int index) {
        if (index >= 0 && index < extraToppings.size()) {
            extraToppings.remove(index);
            // No price mutation here — calculatePrice() handles it
        }
    }

    // ====== Display Methods ======
    @Override
    public String getName() {
        if (size != null && bread != null) {
            return size.getDisplay() + " " + bread + " Sandwich" + (isToasted() ? " (Toasted)" : "");
        }
        return "Custom Sandwich";
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

        // Separate regular and premium toppings
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