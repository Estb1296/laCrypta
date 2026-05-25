package model;
public class Drink extends MenuItem {
        private String size; // Tracks "Small", "Medium", "Large"

    public Drink(String name) {
        super(name, 2.50, "Drink");
    }

    @Override
    public String getDescription() {
        return "Drink: " + getName();
    }

    public void setSize(String size) {
            this.size = size;
        }

        public String getSize() {
            return size;
        }
        // Pulls pricing matrix out of variables and computes on demand
        @Override
        public double getPrice() {
            if (size == null) return 0.0;

            return switch (size) {
                case "Small"  -> 1.99; // Matches your UI Drink pricing ($1.99)
                case "Medium" -> 2.50;
                case "Large"  -> 3.00;
                default       -> 0.0;
            };
        }
    @Override
    public String getName() {
        String baseName = super.getName(); // Grabs the name sent to the constructor (e.g. "Coca-Cola")
        if (this.size == null) {
            return baseName;
        }
        return this.size + " " + baseName; // Returns "Medium Coca-Cola"
    }

        // Generates fresh description strings whenever checked out

    @Override
    public String toReceiptLine() {
        return String.format("%s (%s) - $%.2f", getName(), this.size, calculatePrice());
    }

    @Override
    public String getCategory() {
        return "Drink";
    }

    }
