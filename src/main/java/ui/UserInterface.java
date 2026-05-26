package ui;
import data.OrderRepository;

import model.*;
import util.InputValidator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    static Scanner input = new Scanner(System.in);
    private final Order currentOrder = new Order();
    private final OrderRepository orderRepository = new OrderRepository();
    private CheckOutScreen checkoutScreen;
    public UserInterface() {
        this.checkoutScreen = new CheckOutScreen(currentOrder, orderRepository, input);
    }
    public void display(){
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("""
              1) New Order
              0) Exit - exit the application
              7) Admin
              """);
            int choice = InputValidator.getValidMenuChoice(input, 0, 7);
            switch (choice) {
                case 1 -> OrderScreen();
                case 0 -> isRunning = false;
                case 7 -> {
                    System.out.println("\n════════════════════════════════");
                    new AdminUserInterface(orderRepository);
                    System.out.println("════════════════════════════════\n");
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    // Order Screen
    private void OrderScreen() {
        boolean isOrderScreen = true;
        // Log order start when entering this screen
        try {
            orderRepository.logOrderStart();
        } catch (IOException e) {
            System.out.println("Audit logging error: " + e.getMessage());
        }
        while (isOrderScreen) {
            System.out.println("""
            1) Add Sandwich
            2) Signature Sandwich
            3) Add Drink
            4) Add Chips
            5) Checkout
            0) Cancel Order""");
            int choice =InputValidator.getValidIntegerInput(input,"Enter a valid choice:",0,5);
            switch (choice) {
                case 1 -> sandwichScreen();      // Sandwich customization
                case 2->  signatureSandwichScreen();
                case 3 -> drinkScreen();           // Drink selection
                case 4 -> chipsScreen();           // Chips selection
                case 5 -> {
                    if (currentOrder.getItems().isEmpty()) {
                        System.out.println("\n❌ Your cart is empty! You must add at least a Sandwich, Drink, or Chips to check out.\n");
                    } else {
                        // Only allow user to check out if they have ordered something!
                        checkoutScreen = new CheckOutScreen(currentOrder, orderRepository, input);
                        String nextDestination = checkoutScreen.display();
                        if (nextDestination.equals("HOME")) {
                            isOrderScreen = false;
                        }
                    }
                }
                case 0 ->{
                    handleOrderCancellation();
                    isOrderScreen=false;
                }
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
        11) Sandwich Summary
        0) Back to Order Screen""");
            int choice = InputValidator.getValidIntegerInput(input,"Enter a valid choice (0-11): ",0,11);
            switch (choice) {
                case 1 -> breadScreen(sandwich);
                case 2 -> sizeScreen(sandwich);
                case 3 -> meatScreen(sandwich);
                case 4 -> cheeseScreen(sandwich);
                case 5 -> toppingsScreen(sandwich);
                case 6 -> {
                    currentOrder.addItem(sandwich);
                    System.out.println("Sandwich added to cart! 🛒");
                    isSandwichScreen = false;
                }
                case 7-> removeToppingScreen(sandwich);
                case 8-> toastScreen(sandwich);
                case 9-> saucesScreen(sandwich);
                case 10->extraMeatCheeseScreen(sandwich);
                case 11 -> {
                    displaySandwichSummary(sandwich);
                    System.out.print("\nAdd this customized sandwich to your cart? (Y/N): ");
                    String confirm = input.nextLine().trim().toUpperCase();
                    if (confirm.equals("Y")) {
                        currentOrder.addItem(sandwich);
                        System.out.println("Sandwich added to cart! 🛒");
                        isSandwichScreen = false;
                    }
                }
                case 0 -> {
                    if (sandwich.getBread() != null || !sandwich.getToppings().isEmpty()) {
                        System.out.print("\n⚠️ You are leaving without adding to your cart! Save progress? (Y/N): ");
                        String confirm = input.nextLine().trim().toUpperCase();

                        if (confirm.equals("Y")) {
                            currentOrder.addItem(sandwich);
                            System.out.println("Sandwich successfully saved to cart! 🛒");
                        } else {
                            System.out.println("Sandwich changes discarded.");
                        }
                    } else {
                        System.out.println("Returned to order screen.");
                    }
                    isSandwichScreen = false;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }
    private void signatureSandwichScreen() {
        System.out.println("""
Select a Signature Sandwich:
A) Classic BLT (8" White, Bacon, Cheddar, Veggies, Ranch - Toasted)
B) Philly Cheese Steak (8" White, Steak, American, Peppers, Mayo - Toasted)
0) Cancel""");

        String choice = InputValidator.getValidCharChoice(input, "AB0");
        Sandwich signatureSandwich = null;

        switch(choice) {
            case "A" -> {
                signatureSandwich = new BLT();
                System.out.println("✅ Added BLT to your workspace!");
            }
            case "B" -> {
                signatureSandwich = new PhillyCheeseSteak();
                System.out.println("✅ Added Philly Cheese Steak to your workspace!");
            }
            case "0" -> {
                return; // Go back to the order menu
            }
        }

        // Now, send this signature sandwich to the modification screen!
        handleSandwichCustomization(signatureSandwich);
    }
    private void handleSandwichCustomization(Sandwich sandwich) {
        boolean customizing = true;
        while (customizing) {
            System.out.println("\n--- Current Sandwich State ---");
            System.out.println(sandwich.getDescription());
            System.out.println("Current Price: $" + String.format("%.2f", sandwich.getPrice()));
            System.out.println("------------------------------");

            System.out.println("""
Would you like to customize this sandwich?
1) Change Meat
2) Change Cheese
3) Add Regular Toppings
4) Add Sauces
5) Extra Meat/Cheese
6) Confirm and Add to Order
0) Cancel Item""");

            int choice = InputValidator.getValidIntegerInput(input, "Pick an option:", 0,6);
            switch(choice) {
                case 1 -> meatScreen(sandwich);     // Uses your existing meat screen!
                case 2 -> cheeseScreen(sandwich);   // Uses your existing cheese screen!
                case 3 -> toppingsScreen(sandwich);
                case 4 -> saucesScreen(sandwich);
                case 5->  extraMeatCheeseScreen(sandwich);
                case 6 -> {
                    // Saves the sandwich to my Order cart list
                    currentOrder.addItem(sandwich);
                    System.out.println("🛒 Sandwich added to your order cart!");
                    customizing = false;
                }
                case 0 -> customizing = false;
            }
        }
    }
    private void breadScreen(Sandwich sandwich) {
        System.out.println("""
    A) White Bread
    B) Wheat Bread
    C) Sourdough
    D) Rye
    E) Wrap""");
        String choice = InputValidator.getValidCharChoice(input, "ABCDE");
        switch(choice) {
            case "A" -> sandwich.setBread("White");
            case "B" -> sandwich.setBread("Wheat");
            case "C" -> sandwich.setBread("Sourdough");
            case "D" -> sandwich.setBread("Rye");
            case "E" -> sandwich.setBread("Wrap");
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
            String choice = InputValidator.getValidStringInput(input, "Enter a valid choice: ");
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
        if (sandwich.getSize() == null) {
            System.out.println("\n❌ Action Blocked: Pricing rules depend on sandwich size.");
            System.out.println("👉 Please select your sandwich size (Option 2) first!");
            return; // Exits the screen entirely and safely returns to your main menu
        }
        System.out.println("""
    Select meat (by sandwich size):
    A) Chicken
    B) Turkey
    C) Roast Beef
    D) Ham
    E) Tuna
    0) Back""");

        String choice = InputValidator.getValidCharChoice(input, "ABCDE0");
        switch(choice) {
            case "A" -> {
                sandwich.setMeat("Chicken");
                System.out.println("✅ Selected Chicken");
            }
            case "B" -> {
                sandwich.setMeat("Turkey");
                System.out.println("✅ Selected Turkey");
            }
            case "C" -> {
                sandwich.setMeat("Roast Beef");
                System.out.println("✅ Selected Roast Beef");
            }
            case "D" -> {
                sandwich.setMeat("Ham");
                System.out.println("✅ Selected Ham");
            }
            case "E" -> {
                sandwich.setMeat("Tuna");
                System.out.println("✅ Selected Tuna");
            }
            case "0" -> {} // Back
        }
    }


    //Cheese selection screen
    private void cheeseScreen(Sandwich sandwich) {
        if (sandwich.getSize() == null) {
            System.out.println("\n❌ Action Blocked: Pricing rules depend on sandwich size.");
            System.out.println("👉 Please select your sandwich size (Option 2) first!");
            return; // Exits the screen entirely and safely returns to your main menu
        }
        System.out.println("""
    Select cheese (by sandwich size):
    A) Cheddar
    B) Swiss
    C) Provolone
    D) American
    E) Pepper Jack
    F) No Cheese
    0) Back""");

        String choice = InputValidator.getValidCharChoice(input, "ABCDEF0");

        switch(choice) {
            case "A" -> {
                sandwich.setCheese("Cheddar");
                System.out.println("✅ Selected Cheddar");
            }
            case "B" -> {
                sandwich.setCheese("Swiss");
                System.out.println("✅ Selected Swiss");
            }
            case "C" -> {
                sandwich.setCheese("Provolone");
                System.out.println("✅ Selected Provolone");
            }
            case "D" -> {
                sandwich.setCheese("American");
                System.out.println("✅ Selected American");
            }
            case "E" -> {
                sandwich.setCheese("Pepper Jack");
                System.out.println("✅ Selected Pepper Jack");
            }
            case "F" -> {
                sandwich.setCheese("None");
                System.out.println("✅ No cheese selected");
            }
            case "0" -> {} // Back
        }
    }

    //Topping selection here
    private void toppingsScreen(Sandwich sandwich) {
        boolean isSelectingToppings = true;
        while (isSelectingToppings) {
            System.out.println("""
    Select toppings:
    REGULAR TOPPINGS (Included):
    A) Lettuce
    B) Peppers
    C) Onions
    D) Tomatoes
    E) Jalapeños
    F) Cucumbers
    G) Pickles
    H) Guacamole
    I) Mushrooms
    0) Done with toppings""");
    String choice = InputValidator.getValidCharChoice(input, "ABCDEFGHI0");
            switch(choice) {
                case "A" -> {sandwich.addTopping(new Topping("Lettuce", 0, false));System.out.println("✅ Added Lettuce (Included)");}
                case "B" -> {sandwich.addTopping(new Topping("Peppers", 0, false));System.out.println("✅ Added Peppers (Included)");}
                case "C" -> {sandwich.addTopping(new Topping("Onions", 0, false));System.out.println("✅ Added Onions (Included)");}
                case "D" -> {sandwich.addTopping(new Topping("Tomatoes", 0, false));System.out.println("✅ Added Tomatoes (Included)");}
                case "E" -> {sandwich.addTopping(new Topping("Jalapeños", 0, false));System.out.println("✅ Added Jalapeños (Included)");}
                case "F" -> {sandwich.addTopping(new Topping("Cucumbers", 0, false));System.out.println("✅ Added Cucumbers (Included)");}
                case "G" -> {sandwich.addTopping(new Topping("Pickles", 0, false));System.out.println("✅ Added Pickles (Included)");}
                case "H" -> {sandwich.addTopping(new Topping("Guacamole", 0, false));System.out.println("✅ Added Guacamole (Included)");}
                case "I" -> {sandwich.addTopping(new Topping("Mushrooms", 0, false));System.out.println("✅ Added Mushrooms (Included)");}
                case "0" -> isSelectingToppings = false;
            }
        }
    }
    private void removeToppingScreen(Sandwich sandwich) {
        boolean isRemovingTopping = true;
        while (isRemovingTopping) {
            ArrayList<Topping> toppings = sandwich.getToppings();
            if (toppings.isEmpty()) {
                System.out.println("❌ No toppings on this sandwich to remove.");
                return; // Instant clean break
            }
            System.out.println("---Remove Topping:---");

            // Display toppings with numbers
            int menuNumber = 1;
            for (Topping topping : toppings) {
                System.out.println(menuNumber + ") " + topping.name() +
                        " (-$" + String.format("%.2f", topping.price()) + ")");
                menuNumber++;
            }
            System.out.println("0) Back");

            // Leveraged my structured list index checker tool
            int targetIndex = InputValidator.getValidListIndex(input, toppings.size());

            if (targetIndex == -1) {
                isRemovingTopping = false;
            } else {
                sandwich.removeTopping(targetIndex);
                System.out.println("✅ Topping removed.");
            }
        }
    }
    private void toastScreen(Sandwich sandwich) {
        System.out.println("""
    Would you like your sandwich toasted?
    A) Yes
    B) No""");

        String choice = InputValidator.getValidCharChoice(input, "AB");
        switch(choice) {
            case "A":
                sandwich.setToasted(true);
                System.out.println("Sandwich will be toasted 🔥");
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
                A) Mayo      B) Mustard          C) Ketchup
                D) Ranch     E) Thousand Islands F) Vinaigrette
                G) Remove sauce                  0) Done with sauces""");

            String choice = InputValidator.getValidCharChoice(input, "ABCDEFG0");
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
                System.out.println("❌ No sauces on this sandwich to remove.");
                return; //STOP EXECUTION INSTANTLY and go back to the sub-menu
            }
            System.out.println("Remove Sauce:");
            for (int i = 0; i < sauces.size(); i++) {
                System.out.println((i + 1) + ") " + sauces.get(i));
            }
            System.out.println("0) Back");
            int targetIndex = InputValidator.getValidListIndex(input, sauces.size());

            if (targetIndex == -1) {
                isRemovingSauces = false;
            } else {
                String sauceToRemove = sauces.get(targetIndex);
                sandwich.removeSauce(sauceToRemove);
                System.out.println("Removed " + sauceToRemove);
            }
        }
    }
    private void extraMeatCheeseScreen(Sandwich sandwich) {
        if (sandwich.getSize() == null) {
            System.out.println("\n❌ Action Blocked: Premium extra costs scale by sandwich size.");
            System.out.println("👉 Please select your sandwich size (Option 2) first!");
            return; // Exits the screen entirely and safely returns to your main menu
        }
        boolean isSelectingExtra = true;
        while (isSelectingExtra) {
            System.out.println("""
        Add Extra Meat or Cheese?
        A) Extra Meat
        B) Extra Cheese
        C) Remove Extra Topping
        0) Done with extras""");

            String choice = InputValidator.getValidCharChoice(input,"AB0");
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
            }
        }
    }
    private ExtraTopping.ExtraToppingSize getSizeForExtra(Sandwich sandwich) {
        Sandwich.SandwichSize size = sandwich.getSize();
        return (size != null) ? size.getExtraSize() : ExtraTopping.ExtraToppingSize.EIGHT;
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

            int targetIndex = InputValidator.getValidListIndex(input, extraToppings.size());

            if (targetIndex == -1) {
                isRemovingExtra = false; // User chose 0 to go back
            } else {
                // targetIndex is already completely safe and 0-indexed!
                sandwich.removeExtraTopping(targetIndex);
                System.out.println("✅ Extra topping removed.");
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
            A) Coca-Cola        B) Sprite           C) Orange Fanta
            D) Iced Tea         E) Lemonade         F) Bottled Water
            G) Coffee
            H) Back to Order Screen""");

            String choice = InputValidator.getValidCharChoice(input, "ABCDEFGH");
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
            }
        }
    }
    private void drinkSizeScreen(Drink drink) {
        System.out.println("""
    Select drink size:
    A) Small - $1.99
    B) Medium - $2.50
    C) Large - $3.00""");

        String choice =  InputValidator.getValidCharChoice(input, "ABC");
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

            String choice = InputValidator.getValidCharChoice(input, "ABCDEFG");
            Chips chips=null;
            switch(choice) {
                case "A","a" -> chips = new Chips("Lay's Classic", 1.50, "");

                case "B","b" -> chips = new Chips("Doritos Nacho Cheese", 1.50, "");

                case "C","c" -> chips = new Chips("Cheetos Flamin' Hot", 1.50, "");

                case "D","d" -> chips = new Chips("Fritos Original", 1.50, "");

                case "E","e" -> chips = new Chips("Pringles Sour Cream & Onion", 1.50, "Snack");

                case "F","f" -> chips = new Chips("Kettle Cooked", 1.50, "");

                case "G","g" -> isChipsScreen = false;

            }
            if (chips != null) {
                currentOrder.addItem(chips); // Replace 'currentOrder' with your actual Order tracking variable name
                System.out.println("Added " + chips.getName() + " to cart!");
                isChipsScreen = false; // Kick back to order screen after adding an item
            }
        }
    }
    /**
     * Handle order cancellation with confirmation warning
     * Shows warning if cart has items, asks for confirmation
     */
    private void handleOrderCancellation() {
        if (!currentOrder.getItems().isEmpty()) {
            // User has items in cart - show warning
            System.out.println("\n⚠️  WARNING: You have items in your cart!");
            System.out.println("Items: " + currentOrder.getItems().size());
            System.out.println("Total: $" + String.format("%.2f", currentOrder.calculatePrice()));

            boolean confirmCancel = InputValidator.getConfirmation(input, "\nAre you sure you want to cancel this order?");

            if (confirmCancel) {
                try {
                    // Attempting to log the cancellation in the repository
                    orderRepository.logCancelledOrder(currentOrder);
                    System.out.println("❌ Order cancelled.");

                } catch (RuntimeException e) {
                    /* * WHY RUNTIME EXCEPTION?
                     * The repository method does not explicitly 'throw' a checked exception like IOException.
                     * Instead, modern frameworks throw Unchecked (Runtime) exceptions when database/file
                     * writes fail. Catching RuntimeException allows us to intercept these unexpected data
                     * layer errors gracefully without causing a compiler error.
                     */
                    System.out.println("⚠️ Error saving cancellation log: " + e.getMessage());

                } finally {
                    /* * WHY A FINALLY BLOCK?
                     * The finally block is guaranteed to run whether the try block succeeds OR the catch
                     * block triggers. We put clearCart() here so the user's cart is ALWAYS safely emptied,
                     * even if the background logging system crashes.
                     */
                    currentOrder.clearCart();
                    System.out.println("🛒 Cart cleared.");
                }
            } else {
                System.out.println("✅ Continuing with your order...");
            }
        } else {
            System.out.println("Returned to home screen.");
        }
    }
    }