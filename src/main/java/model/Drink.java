package model;
public class Drink extends BaseMenuItem {
        private String size; // Tracks "Small", "Medium", "Large"
        private final String flavor; // Tracks "Coca-Cola", "Sprite", etc.

        public Drink(String flavor) {
            // We pass the flavor name up, and initialize the price to 0.0
            // because the size selection screen will calculate the final cost.
            super(flavor, 0.0);
            this.flavor = flavor;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getSize() {
            return size;
        }

        @Override
        public String getName() {
            if (size != null) {
                return size + " " + flavor;
            }
            return flavor; // Fallback if size hasn't been set yet
        }

        // Pulls pricing matrix out of variables and computes on demand
        @Override
        public double getPrice() {
            if (size == null) return 0.0;

            return switch (size) {
                case "Small" -> 2.00;
                case "Medium" -> 2.50;
                case "Large" -> 3.00;
                default -> 0.0;
            };
        }

        // Generates fresh description strings whenever checked out
        @Override
        public String getDescription() {
            return "Drink: " + getName() + " ($" + String.format("%.2f", getPrice()) + ")";
        }


    }
