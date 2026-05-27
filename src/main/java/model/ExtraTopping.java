package model;

public class ExtraTopping extends MenuItem {
    private final ExtraToppingSize size;

    public ExtraTopping(String name, boolean isCheese, ExtraToppingSize size) {
        super(name, "Extra Topping");
        this.size = size;
    }
    public enum ExtraToppingSize {
        FOUR(0.50),
        EIGHT(1.00),
        TWELVE(1.50);

        private final double price;

        ExtraToppingSize(double price) {
            this.price = price;
        }

        public double getPrice() {
            return price;
        }
    }
    @Override
    public double calculatePrice() {
        if (size == null) return 0.0;
        return size.getPrice();
    }

    public ExtraToppingSize getSize() {
        return size;
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
