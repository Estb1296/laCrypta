package model;
public class Drink extends MenuItem {
        private String size; // Tracks "Small", "Medium", "Large"

    public Drink(String name) {
        super(name, 2.50, "Beverage");
    }
    @Override
    public String getDescription() {
        return "Drink: " + getName() + " ($" + String.format("%.2f", getPrice()) + ")";
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
                case "Small" -> 2.00;
                case "Medium" -> 2.50;
                case "Large" -> 3.00;
                default -> 0.0;
            };
        }

        // Generates fresh description strings whenever checked out

    @Override
    public String toReceiptLine() {
        return "";
    }

    @Override
    public String getCategory() {
        return "";
    }

    }
