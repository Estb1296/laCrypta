package model;

public abstract class BaseMenuItem implements MenuItem {

        protected final String name;
        protected final double basePrice;

        protected BaseMenuItem(String name, double basePrice) {
            this.name = name;
            this.basePrice = basePrice;
        }

        @Override public String getName() {
            return name;
        }
        @Override public abstract double getPrice();
}
