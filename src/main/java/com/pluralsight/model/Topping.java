package com.pluralsight.model;



public record Topping(String name, double price, boolean isPremium) {
    public Topping(String name, boolean isPremium) {
        this(name, 0.0, isPremium);
    }

}
