package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        super("Custom Sandwich", "Dynamic Specs");
        this.toppings = new ArrayList<>();
    }

    public Sandwich(String name, SandwichSize size, String bread, boolean isToasted) {
        super(name, "Dynamic Specs");
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


        public ExtraTopping.ExtraToppingSize getExtraSize() {
            return extraSize;
        }

        public double getPrice() {
            return price;
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
        if (this.size == null) return 0.0; // Guard clause against null pointer exception
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
        if (this.size == null) return 0.0;
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

    public List<Topping> getToppings() {
        return Collections.unmodifiableList(toppings);
    }

    public List<ExtraTopping> getExtraTopping() {
        return Collections.unmodifiableList(extraToppings);
    }

    public List<String> getSauces() {
        return Collections.unmodifiableList(sauces);
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
    /**
     * NEW UTILITY: Toggles a topping by name.
     * If it's already on the signature template, it removes it.
     * If it's missing, it adds it.
     */
    public void toggleTopping(Topping topping) {
        // Looks through your toppings list to see if a topping with this name already exists
        boolean removed = this.toppings.removeIf(t -> t.name().equalsIgnoreCase(topping.name()));

        if (removed) {
            System.out.println("❌ Removed: " + topping.name() + " from template.");
        } else {
            this.toppings.add(topping);
            System.out.println("➕ Added: " + topping.name() + " to template.");
        }
    }
    // Clear all sauces so the user can rewrite them from scratch
    public void clearSauces(){
        this.sauces.clear();
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
        return getName() + toastStatus + " - " + getFormattedPrice();
    }

    @Override
    public String getCategory() {
        return "Sandwich";
    }
}