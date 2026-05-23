package model;

public interface Priceable {
    double calculatePrice();
    void addToPrice(double amount);
    void subtractFromPrice(double amount);
}
