package model;

public class ExtraTopping extends MenuItem {
    private final ExtraToppingSize size;
    private final boolean isCheese;
    public ExtraTopping(String name, boolean isCheese, ExtraToppingSize size) {
        super(name, "Extra Topping");
        this.size = size;
        this.isCheese = isCheese;
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
        // 🟢 STEP 3: Customize price based on type if you want to!
        // For example, making extra cheese cost 50% less than extra meat:
        if (isCheese) {
            return size.getPrice() * 0.50;
        }
        return size.getPrice(); // Default flat rate for meats
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
