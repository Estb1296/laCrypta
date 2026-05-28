package com.pluralsight.model;

public class BLT extends Sandwich{
    public BLT(){
        super("BLT", SandwichSize.EIGHT, "White", true);
        this.setMeat("Bacon");
        this.setCheese("Cheddar");
        this.addSauce("Ranch");
        this.addTopping(new Topping("Lettuce", 0.0, false));
        this.addTopping(new Topping("Tomato", 0.0, false));
    }
}
