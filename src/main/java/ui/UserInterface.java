package ui;
import model.Order;
import model.Sandwich;
import java.util.ArrayList;
import model.MenuItem;

import java.util.Scanner;

public class UserInterface {
    static Scanner input = new Scanner(System.in);
    private final Order currentOrder = new Order();
    public void display(){
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("""
              1) New Order
              0) Exit - exit the application""");
            int choice = input.nextInt();
            input.nextLine(); // Clear the buffer
            switch (choice) {
                case 1 -> OrderScreen();
                case 0 -> isRunning = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }

    // Order Screen
    private void OrderScreen() {
        boolean isOrderScreen = true;
        while (isOrderScreen) {
            System.out.println("""
            1) Add Sandwich
            2) Add Drink
            3) Add Chips
            4) Checkout
            0) Cancel Order""");
            int choice = input.nextInt();
            input.nextLine(); // Clear the buffer
            switch (choice) {
                case 1 -> sandwichScreen();      // Sandwich customization
                case 2 -> drinkScreen();           // Drink selection
                case 3 -> chipsScreen();           // Chips selection
                case 4 -> displayCart();
                case 0 -> isOrderScreen = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }

    // Sandwich customization screen
    private void sandwichScreen() {
        Sandwich sandwich = new Sandwich();
        boolean isSandwichScreen = true;
        while (isSandwichScreen) {
            System.out.println("""
        1) Select your bread
        2) Select sandwich size
        3) Select meat
        4) Select cheese
        5) Select toppings
        6) Finish & Add to Cart
        0) Back to Order Screen""");
            int choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1 -> breadScreen(sandwich);
                case 2 -> sizeScreen(sandwich);
                case 3 -> meatScreen(sandwich);
                case 4 -> cheeseScreen(sandwich);
                case 5 -> toppingsScreen(sandwich);
                case 6 -> {
                    currentOrder.addItem(sandwich);
                    System.out.println("Sandwich added to cart!");
                    isSandwichScreen = false;
                }
                case 0 -> isSandwichScreen = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void breadScreen(Sandwich sandwich) {
        System.out.println("""
    A) White Bread
    B) Wheat Bread
    C) Sourdough""");
        String choice = input.nextLine().toUpperCase();
        switch(choice) {
            case "A" -> sandwich.setBread("White");
            case "B" -> sandwich.setBread("Wheat");
            case "C" -> sandwich.setBread("Sourdough");
        }
    }
    private void sizeScreen(Sandwich sandwich) {
        boolean isSelectingSize = true;
        while (isSelectingSize) {
            System.out.println("""
        Select sandwich size:
        A) Small - $6.99
        B) Medium - $8.99
        C) Large - $10.99
        0) Back""");

            String choice = input.nextLine();
            switch(choice) {
                case "A","a":
                    sandwich.setSize("Small");
                    System.out.println("Selected Small");
                    isSelectingSize = false;
                    break;
                case "B","b":
                    sandwich.setSize("Medium");
                    System.out.println("Selected Medium");
                    isSelectingSize = false;
                    break;
                case "C","c":
                    sandwich.setSize("Large");
                    System.out.println("Selected Large");
                    isSelectingSize = false;
                    break;
                case "0":
                    isSelectingSize = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void meatScreen(Sandwich sandwich) {
        boolean isSelectingMeat = true;
        while (isSelectingMeat) {
            System.out.println("""
        Select meat:
        A) Chicken
        B) Turkey
        C) Roast Beef
        D) Ham
        E) Tuna
        0) Back""");

            String choice = input.nextLine();
            switch(choice) {
                case "A","a":
                    sandwich.setMeat("Chicken");
                    System.out.println("Selected Chicken");
                    isSelectingMeat = false;
                    break;
                case "B","b":
                    sandwich.setMeat("Turkey");
                    System.out.println("Selected Turkey");
                    isSelectingMeat = false;
                    break;
                case "C","c":
                    sandwich.setMeat("Roast Beef");
                    System.out.println("Selected Roast Beef");
                    isSelectingMeat = false;
                    break;
                case "D","d":
                    sandwich.setMeat("Ham");
                    System.out.println("Selected Ham");
                    isSelectingMeat = false;
                    break;
                case "E","e":
                    sandwich.setMeat("Tuna");
                    System.out.println("Selected Tuna");
                    isSelectingMeat = false;
                    break;
                case "0":
                    isSelectingMeat = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void cheeseScreen(Sandwich sandwich) {
        boolean isSelectingCheese = true;
        while (isSelectingCheese) {
            System.out.println("""
        Select cheese:
        A) Cheddar
        B) Swiss
        C) Provolone
        D) American
        E) Pepper Jack
        F) No Cheese
        0) Back""");

            String choice = input.nextLine();
            switch(choice) {
                case "A","a":
                    sandwich.setCheese("Cheddar");
                    System.out.println("Selected Cheddar");
                    isSelectingCheese = false;
                    break;
                case "B","b":
                    sandwich.setCheese("Swiss");
                    System.out.println("Selected Swiss");
                    isSelectingCheese = false;
                    break;
                case "C","c":
                    sandwich.setCheese("Provolone");
                    System.out.println("Selected Provolone");
                    isSelectingCheese = false;
                    break;
                case "D","d":
                    sandwich.setCheese("American");
                    System.out.println("Selected American");
                    isSelectingCheese = false;
                    break;
                case "E","e":
                    sandwich.setCheese("Pepper Jack");
                    System.out.println("Selected Pepper Jack");
                    isSelectingCheese = false;
                    break;
                case "F","f":
                    sandwich.setCheese("None");
                    System.out.println("No cheese");
                    isSelectingCheese = false;
                    break;
                case "0":
                    isSelectingCheese = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void toppingsScreen(Sandwich sandwich) {
        boolean isSelectingToppings = true;
        while (isSelectingToppings) {
            System.out.println("""
        Select toppings (select multiple or 0 to finish):
        A) Lettuce
        B) Tomato
        C) Onion
        D) Pickles
        E) Jalapeños
        F) Bacon
        0) Done with toppings""");

            String choice = input.nextLine();
            switch(choice) {
                case "A","a":
                    sandwich.addTopping("Lettuce");
                    System.out.println("Added Lettuce (+$0.50)");
                    break;
                case "B","b":
                    sandwich.addTopping("Tomato");
                    System.out.println("Added Tomato (+$0.50)");
                    break;
                case "C","c":
                    sandwich.addTopping("Onion");
                    System.out.println("Added Onion (+$0.50)");
                    break;
                case "D","d":
                    sandwich.addTopping("Pickles");
                    System.out.println("Added Pickles (+$0.50)");
                    break;
                case "E","e":
                    sandwich.addTopping("Jalapeños");
                    System.out.println("Added Jalapeños (+$0.50)");
                    break;
                case "F","f":
                    sandwich.addTopping("Bacon");
                    System.out.println("Added Bacon (+$0.50)");
                    break;
                case "0":
                    isSelectingToppings = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    // Drink selection screen
    private void drinkScreen(){
        boolean isDrinkScreen = true;
        while(isDrinkScreen){
            System.out.println("""
            What kind of drink do you want?
            A) Coca-Cola
            B) Sprite
            C) Orange Fanta
            D) Iced Tea
            E) Lemonade
            F) Bottled Water
            G) Coffee
            H) Back to Order Screen""");

            String choice = input.nextLine();

            switch(choice) {
                case "A","a":
                    System.out.println("Added Coca-Cola");
                    break;
                case "B","b":
                    System.out.println("Added Sprite");
                    break;
                case "C","c":
                    System.out.println("Added Orange Fanta");
                    break;
                case "D","d":
                    System.out.println("Added Iced Tea");
                    break;
                case "E","e":
                    System.out.println("Added Lemonade");
                    break;
                case "F","f":
                    System.out.println("Added Bottled Water");
                    break;
                case "G","g":
                    System.out.println("Added Coffee");
                    break;
                case "H","h":
                    isDrinkScreen = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    // Chips selection screen
    private void chipsScreen(){
        boolean isChipsScreen = true;
        while(isChipsScreen){
            System.out.println("""
            What kind of chips do you want?
            A) Lay's Classic
            B) Doritos Nacho Cheese
            C) Cheetos Flamin' Hot
            D) Fritos Original
            E) Pringles Sour Cream & Onion
            F) Kettle Cooked
            G) Back to Order Screen""");

            String choice = input.nextLine();
            switch(choice) {
                case "A","a":
                    System.out.println("Added Lay's Classic");
                    break;
                case "B","b":
                    System.out.println("Added Doritos Nacho Cheese");
                    break;
                case "C","c":
                    System.out.println("Added Cheetos Flamin' Hot");
                    break;
                case "D","d":
                    System.out.println("Added Fritos Original");
                    break;
                case "E","e":
                    System.out.println("Added Pringles Sour Cream & Onion");
                    break;
                case "F","f":
                    System.out.println("Added Kettle Cooked");
                    break;
                case "G","g":
                    isChipsScreen = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    private void displayCart() {
        boolean inCart = true;
        while (inCart) {
            System.out.println("\n========== YOUR CART ==========");

            ArrayList<MenuItem> items = currentOrder.getItems();
            if (items.isEmpty()) {
                System.out.println("Cart is empty");
            } else {
                for (int i = 0; i < items.size(); i++) {
                    MenuItem item = items.get(i);
                    System.out.println((i + 1) + ") " + item.getName() + " - $" + item.getPrice());
                }
                System.out.println("==============================");
                System.out.println("Total: $" + currentOrder.getTotal());
            }

            System.out.println("\n1) Continue Shopping");
            System.out.println("2) Remove Item");
            System.out.println("3) Checkout");
            System.out.println("0) Cancel Order");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> OrderScreen();
                case 2 -> removeItemFromCart();
                case 3 -> completeCheckout();
                case 0 -> {
                    currentOrder.clearCart();
                    inCart = false;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void removeItemFromCart() {
        System.out.print("Enter item number to remove: ");
        int index = input.nextInt() - 1;
        input.nextLine();
        currentOrder.removeItem(index);
        System.out.println("Item removed");
    }

    private void completeCheckout() {
        double total = currentOrder.getTotal();
        System.out.println("\n========== CHECKOUT ==========");
        System.out.println("Total Amount: $" + total);
        System.out.println("Thank you for your order!");
        currentOrder.clearCart();
    }
}