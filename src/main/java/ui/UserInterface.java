package ui;
import data.OrderRepository;
import model.*;


import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    static Scanner input = new Scanner(System.in);
    private final Order currentOrder = new Order();
    private final CheckOutScreen checkoutScreen;
    public UserInterface() {
        OrderRepository orderRepository = new OrderRepository();
        this.checkoutScreen = new CheckOutScreen(currentOrder, orderRepository, input);
    }
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
                case 4 -> checkoutScreen.display();  // Call checkout screen
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
            displaySandwichSummary(sandwich);
            System.out.println("""
        1) Select your bread
        2) Select sandwich size
        3) Select meat
        4) Select cheese
        5) Select toppings
        6) Finish & Add to Cart
        7) Remove topping
        8) Toast Sandwich
        9) Sauces
        10) Extra Toppings(Cheese,Meat)
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
                case 7-> removeToppingScreen(sandwich);
                case 8-> toastScreen(sandwich);
                case 9-> saucesScreen(sandwich);
                case 10->extraMeatCheeseScreen(sandwich);
                case 11 -> {
                    displaySandwichSummary(sandwich);  // Final review before adding
                    System.out.println("\nAdd this sandwich to cart? (Y/N)");
                    String confirm = input.nextLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        currentOrder.addItem(sandwich);
                        System.out.println("Sandwich added to cart!");
                        isSandwichScreen = false;
                    }
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
    A) 4" - $6.99
    B) 8" - $8.99
    C) 12" - $10.99
    0) Back""");
            String choice = input.nextLine();
            switch(choice) {
                case "A","a":
                    sandwich.setSize(Sandwich.SandwichSize.FOUR);
                    System.out.println("Selected " + Sandwich.SandwichSize.FOUR.getDisplay());
                    isSelectingSize = false;
                    break;
                case "B","b":
                    sandwich.setSize(Sandwich.SandwichSize.EIGHT);
                    System.out.println("Selected " + Sandwich.SandwichSize.EIGHT.getDisplay());
                    isSelectingSize = false;
                    break;
                case "C","c":
                    sandwich.setSize(Sandwich.SandwichSize.TWELVE);
                    System.out.println("Selected " + Sandwich.SandwichSize.TWELVE.getDisplay());
                    isSelectingSize = false;
                    break;
                case "0":
                    isSelectingSize = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            displaySandwichSummary(sandwich);
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
    //Cheese selection screen
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
    //Topping selection here
    private void toppingsScreen(Sandwich sandwich) {
        boolean isSelectingToppings = true;
        while (isSelectingToppings) {
            System.out.println("""
    Select toppings (select multiple or 0 to finish):
    Regular Toppings ($0.50 each):
    A) Lettuce
    B) Tomato
    C) Onion
    D) Pickles
    E) Jalapeños
    
    Premium Toppings:
    F) Bacon (+$1.50)
    G) Cheese (+$1.00)
    0) Done with toppings""");

            String choice = input.nextLine();
            switch(choice) {
                case "A","a":
                    sandwich.addTopping(new Topping("Lettuce", 0.50, false));
                    System.out.println("Added Lettuce (+$0.50)");
                    break;
                case "B","b":
                    sandwich.addTopping(new Topping("Tomato", 0.50, false));
                    System.out.println("Added Tomato (+$0.50)");
                    break;
                case "C","c":
                    sandwich.addTopping(new Topping("Onion", 0.50, false));
                    System.out.println("Added Onion (+$0.50)");
                    break;
                case "D","d":
                    sandwich.addTopping(new Topping("Pickles", 0.50, false));
                    System.out.println("Added Pickles (+$0.50)");
                    break;
                case "E","e":
                    sandwich.addTopping(new Topping("Jalapeños", 0.50, false));
                    System.out.println("Added Jalapeños (+$0.50)");
                    break;
                case "F","f":
                    sandwich.addTopping(new Topping("Bacon", 1.50, true));
                    System.out.println("Added Bacon (+$1.50)");
                    break;
                case "G","g":
                    sandwich.addTopping(new Topping("Cheese", 1.00, true));
                    System.out.println("Added Cheese (+$1.00)");
                    break;
                case "0":
                    isSelectingToppings = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    private void removeToppingScreen(Sandwich sandwich) {
        boolean isRemovingTopping = true;
        while (isRemovingTopping) {
            System.out.println("""
        Remove Topping:
        """);

            ArrayList<Topping> toppings = sandwich.getToppings();  // You need this getter

            if (toppings.isEmpty()) {
                System.out.println("No toppings to remove");
                isRemovingTopping = false;
            }

            // Display toppings with numbers
            for (int i = 0; i < toppings.size(); i++) {
                Topping topping = toppings.get(i);
                System.out.println((i + 1) + ") " + topping.name() +
                        " (-$" + String.format("%.2f", topping.price()) + ")");
            }
            System.out.println("0) Back");

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 0) {
                isRemovingTopping = false;
            } else if (choice > 0 && choice <= toppings.size()) {
                sandwich.removeTopping(choice - 1);  // Call the method
                System.out.println("Topping removed");
                isRemovingTopping = false;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }
    private void toastScreen(Sandwich sandwich) {
        System.out.println("""
    Would you like your sandwich toasted?
    A) Yes
    B) No""");

        String choice = input.nextLine().toUpperCase();
        switch(choice) {
            case "A":
                sandwich.setToasted(true);
                System.out.println("Sandwich will be toasted");
                break;
            case "B":
                sandwich.setToasted(false);
                System.out.println("Sandwich will not be toasted");
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
    private void saucesScreen(Sandwich sandwich) {
        boolean isSelectingSauces = true;
        while (isSelectingSauces) {
            System.out.println("""
        Select sauces (select multiple or 0 to finish):
        A) Mayo
        B) Mustard
        C) Ketchup
        D) Ranch
        E) Thousand Islands
        F) Vinaigrette
        G) Remove sauce
        0) Done with sauces""");

            String choice = input.nextLine().toUpperCase();
            switch(choice) {
                case "A":
                    sandwich.addSauce("Mayo");
                    System.out.println("Added Mayo");
                    break;
                case "B":
                    sandwich.addSauce("Mustard");
                    System.out.println("Added Mustard");
                    break;
                case "C":
                    sandwich.addSauce("Ketchup");
                    System.out.println("Added Ketchup");
                    break;
                case "D":
                    sandwich.addSauce("Ranch");
                    System.out.println("Added Ranch");
                    break;
                case "E":
                    sandwich.addSauce("Thousand Islands");
                    System.out.println("Added Thousand Islands");
                    break;
                case "F":
                    sandwich.addSauce("Vinaigrette");
                    System.out.println("Added Vinaigrette");
                    break;
                case "G":
                    removeSaucesScreen(sandwich);
                    break;
                case "0":
                    isSelectingSauces = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
        }
    private void removeSaucesScreen(Sandwich sandwich) {
        boolean isRemovingSauces = true;
        while (isRemovingSauces) {
            ArrayList<String> sauces = sandwich.getSauces();  // Need this getter

            if (sauces.isEmpty()) {
                System.out.println("No sauces to remove");
                isRemovingSauces = false;
            }
            System.out.println("Remove Sauce:");
            for (int i = 0; i < sauces.size(); i++) {
                System.out.println((i + 1) + ") " + sauces.get(i));
            }
            System.out.println("0) Back");

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 0) {
                isRemovingSauces = false;
            } else if (choice > 0 && choice <= sauces.size()) {
                String sauceToRemove = sauces.get(choice - 1);
                sandwich.removeSauce(sauceToRemove);
                System.out.println("Removed " + sauceToRemove);
                isRemovingSauces = false;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }
    private void extraMeatCheeseScreen(Sandwich sandwich) {
        boolean isSelectingExtra = true;
        while (isSelectingExtra) {
            System.out.println("""
        Add Extra Meat or Cheese?
        A) Extra Meat
        B) Extra Cheese
        C) Remove Extra Topping
        0) Done with extras""");

            String choice = input.nextLine().toUpperCase();
            switch(choice) {
                case "A":
                    ExtraTopping extraMeat = new ExtraTopping("Extra Meat",
                            getSizeForExtra(sandwich));
                    sandwich.addExtraTopping(extraMeat);
                    System.out.println("Added Extra Meat (+$" + String.format("%.2f", extraMeat.getPrice()) + ")");
                    displaySandwichSummary(sandwich);
                    break;
                case "B":
                    ExtraTopping extraCheese = new ExtraTopping("Extra Cheese",
                            getSizeForExtra(sandwich));
                    sandwich.addExtraTopping(extraCheese);
                    System.out.println("Added Extra Cheese (+$" + String.format("%.2f", extraCheese.getPrice()) + ")");
                    displaySandwichSummary(sandwich);
                    break;
                case "C":
                    removeExtraToppingScreen(sandwich);
                    break;
                case "0":
                    isSelectingExtra = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    private ExtraTopping.ExtraToppingSize getSizeForExtra(Sandwich sandwich) {
        Sandwich.SandwichSize sandwichSize = sandwich.getSize();

        if (sandwichSize == null) {
            return ExtraTopping.ExtraToppingSize.EIGHT;  // Default
        }

        switch (sandwichSize) {
            case FOUR -> {
                return ExtraTopping.ExtraToppingSize.FOUR;
            }
            case EIGHT -> {
                return ExtraTopping.ExtraToppingSize.EIGHT;
            }
            case TWELVE -> {
                return ExtraTopping.ExtraToppingSize.TWELVE;
            }
            default -> {
                return ExtraTopping.ExtraToppingSize.EIGHT;
            }
        }
    }
    private void removeExtraToppingScreen(Sandwich sandwich) {
        boolean isRemovingExtra = true;
        while (isRemovingExtra) {
            ArrayList<ExtraTopping> extraToppings = sandwich.getExtraTopping();  // Need getter

            if (extraToppings.isEmpty()) {
                System.out.println("No extra toppings to remove");
                isRemovingExtra = false;
            }

            System.out.println("Remove Extra Topping:");
            for (int i = 0; i < extraToppings.size(); i++) {
                ExtraTopping extra = extraToppings.get(i);
                System.out.println((i + 1) + ") " + extra.getName() +
                        " (-$" + String.format("%.2f", extra.getPrice()) + ")");
            }
            System.out.println("0) Back");

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 0) {
                isRemovingExtra = false;
            } else if (choice > 0 && choice <= extraToppings.size()) {
                sandwich.removeExtraTopping(choice - 1);  // Call your method
                System.out.println("Extra topping removed");
                isRemovingExtra = false;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }
    private void displaySandwichSummary(Sandwich sandwich) {
        System.out.println("\n========== SANDWICH SUMMARY ==========");
        System.out.println("  Size: " + (sandwich.getSize() != null ? sandwich.getSize().getDisplay() : "Not Selected"));
        System.out.println("  Toasted: " + (sandwich.isToasted() ? "Yes" : "No"));
        System.out.println("  Price: $" + String.format("%.2f", sandwich.getPrice()));
        System.out.println("=====================================\n");
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
            Drink drink;
            switch(choice) {
                case "A","a":
                    drink = new Drink("Coca-Cola");
                    drinkSizeScreen(drink);
                    currentOrder.addItem(drink);
                    System.out.println("Added " + drink.getName() + " to cart!");
                    break;
                case "B","b":
                    drink = new Drink("Sprite");
                    drinkSizeScreen(drink);
                    currentOrder.addItem(drink);
                    System.out.println("Added " + drink.getName() + " to cart!");
                    break;
                case "C","c":
                    drink = new Drink("Orange Fanta");
                    drinkSizeScreen(drink);
                    currentOrder.addItem(drink);
                    System.out.println("Added " + drink.getName() + " to cart!");
                    break;
                case "D","d":
                    drink = new Drink("Iced Tea");
                    drinkSizeScreen(drink);
                    currentOrder.addItem(drink);
                    System.out.println("Added " + drink.getName() + " to cart!");
                    break;
                case "E","e":
                    drink = new Drink("Lemonade");
                    drinkSizeScreen(drink);
                    currentOrder.addItem(drink);
                    System.out.println("Added " + drink.getName() + " to cart!");
                    break;
                case "F","f":
                    drink = new Drink("Bottled Water");
                    drinkSizeScreen(drink);
                    currentOrder.addItem(drink);
                    System.out.println("Added " + drink.getName() + " to cart!");
                    break;
                case "G","g":
                    drink = new Drink("Coffee");
                    drinkSizeScreen(drink);
                    currentOrder.addItem(drink);
                    System.out.println("Added " + drink.getName() + " to cart!");
                    break;
                case "H","h":
                    isDrinkScreen = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    private void drinkSizeScreen(Drink drink) {
        System.out.println("""
    Select drink size:
    A) Small - $1.99
    B) Medium - $2.50
    C) Large - $3.00""");

        String choice = input.nextLine();
        switch(choice) {
            case "A","a":
                drink.setSize("Small");
                System.out.println("Selected " + drink.getSize());
                break;
            case "B","b":
                drink.setSize("Medium");
                System.out.println("Selected " + drink.getSize());
                break;
            case "C","c":
                drink.setSize("Large");
                System.out.println("Selected " + drink.getSize());
                break;
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
            Chips chips=null;
            switch(choice) {
                case "A","a":
                    chips = new Chips("Lay's Classic", 1.50, "");
                    break;
                case "B","b":
                    chips = new Chips("Doritos Nacho Cheese", 1.50, "");
                    break;
                case "C","c":
                    chips = new Chips("Cheetos Flamin' Hot", 1.50, "");
                    break;
                case "D","d":
                    System.out.println("Fritos Original");
                    chips = new Chips("Added Fritos Original", 1.50, "");
                    break;
                case "E","e":
                    chips = new Chips("Pringles Sour Cream & Onion", 1.50, "Snack");
                    break;
                case "F","f":
                    System.out.println("Added Kettle Cooked");
                    chips =new Chips("Kettle Cooked", 1.50, "");
                    break;
                case "G","g":
                    isChipsScreen = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            if (chips != null) {
                currentOrder.addItem(chips); // Replace 'currentOrder' with your actual Order tracking variable name
                System.out.println("Added " + chips.getName() + " to cart!");
                isChipsScreen = false; // Kick back to order screen after adding an item
            }
        }
    }
    }