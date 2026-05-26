package model;

public class ExtraTopping extends MenuItem {
    private String name;
    private double price;
    private ExtraToppingSize size;

    public ExtraTopping(String name, double price, ExtraToppingSize size) {
        super(name, price, "Extra Topping");
        this.name = name;
        this.price = price;
        this.size = size;
    }

    public ExtraTopping(String name, ExtraToppingSize size) {
        super(name, size.getPrice(), "Extra Topping");
        this.name = name;
        this.size = size;
        this.price = size.getPrice();
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
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
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