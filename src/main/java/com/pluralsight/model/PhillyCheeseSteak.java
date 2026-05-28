package com.pluralsight.model;

public class PhillyCheeseSteak extends Sandwich{
    public PhillyCheeseSteak(){
        super("Philly Cheese Steak", Sandwich.SandwichSize.EIGHT, "White", true);
        this.setMeat("Steak");
        this.setCheese("American");
        this.addSauce("Mayo");
        this.addTopping(new Topping("Peppers", 0.0, false));
    }
}
