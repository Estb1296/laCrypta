package model;

public class ExtraTopping extends MenuItem {

    public enum ExtraToppingSize {
        FOUR(0.50),
        EIGHT(1.00),
        TWELVE(1.50);

        private final double price;

        ExtraToppingSize(double price) {
            this.price = price;
        }

        public double getPrice() { return price; }
    }

    public ExtraTopping(String name, ExtraToppingSize size) {
        super(name, size.getPrice(), "Extra Topping");
        // "Extra Meat" or "Extra Cheese"
    }

    @Override
    public String toReceiptLine() {
        return getName() + " - $" + String.format("%.2f", getPrice());
    }

    @Override
    public String getCategory() {
        return "Extra Topping";
    }
}