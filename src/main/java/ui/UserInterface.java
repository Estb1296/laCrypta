package ui;
import data.OrderRepository;

import model.*;
import ui.util.InputValidator;


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
        1) Select Bread
        2) Select Size
        3) Toppings & Customizations
        4) Would you like Toast?
        5) Finish & Add to Cart
        0) Back to Order Screen""");

            int choice = InputValidator.getValidIntegerInput(input, "Enter a valid choice (0-5): ", 0, 5);
            switch (choice) {
                case 1 -> breadScreen(sandwich);
                case 2 -> sizeScreen(sandwich);
                case 3 -> toppingsMenuScreen(sandwich);  // ← OPENS TOPPINGS MENU
                case 4 -> toastScreen(sandwich);
                case 5 -> {
                    currentOrder.addItem(sandwich);
                    System.out.println("Sandwich added to cart! 🛒");
                    isSandwichScreen = false;
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
    private void toppingsMenuScreen(Sandwich sandwich) {
        boolean inToppingsMenu = true;
        while (inToppingsMenu) {
            System.out.println("\n════════════════════════════════════");
            System.out.println("TOPPINGS & CUSTOMIZATIONS");
            System.out.println("════════════════════════════════════");
            System.out.println("""
        1) Select Meat & Extras
        2) Select Cheese & Extras
        3) Add Toppings & Sauces
        4) View Customizations
        0) Back to Sandwich Menu""");

            int choice = InputValidator.getValidIntegerInput(input, "Enter a valid choice (0-4): ", 0, 4);
            switch (choice) {
                case 1 -> meatAndExtraScreen(sandwich);
                case 2 -> cheeseAndExtraScreen(sandwich);
                case 3 -> toppingsAndSaucesScreen(sandwich);
                case 4 -> viewCustomizations(sandwich);
                case 0 -> inToppingsMenu = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }
    private void meatAndExtraScreen(Sandwich sandwich) {
        boolean inMeatScreen = true;
        while (inMeatScreen) {
            System.out.println("\n════════════════════════════════════");
            System.out.println("SELECT MEAT");
            System.out.println("════════════════════════════════════");

            if (sandwich.getSize() == null) {
                System.out.println("\n❌ Action Blocked: Please select sandwich size first!");
                return;
            }

            System.out.println("Pricing: +$2.00 per meat");
            System.out.println("""
        A) Chicken
        B) Turkey
        C) Roast Beef
        D) Ham
        E) Tuna
        F) No Meat
        
        Y) Remove Extra Meat
        Z) View Current Meat & Extras
        0) Back""");

            String choice = InputValidator.getValidCharChoice(input, "ABCDEFYZ0");
            switch (choice) {
                case "A" -> handleMeatSelection("Chicken", sandwich);
                case "B" -> handleMeatSelection("Turkey", sandwich);
                case "C" -> handleMeatSelection("Roast Beef", sandwich);
                case "D" -> handleMeatSelection("Ham", sandwich);
                case "E" -> handleMeatSelection("Tuna", sandwich);
                case "F" -> handleMeatSelection("None", sandwich);
                case "Y" -> removeExtraMeatScreen(sandwich);
                case "Z" -> viewMeatAndExtras(sandwich);
                case "0" -> inMeatScreen = false;
            }
        }
    }

    /**
     * Smart meat selection handler:
     * - First selection: Set as primary meat
     * - Same meat selected again: Ask to add as extra
     * - Different meat selected: Automatically add as extra
     */
    private void handleMeatSelection(String selectedMeat, Sandwich sandwich) {
        String currentMeat = sandwich.getMeat();

        // First meat selection
        if (currentMeat == null) {
            sandwich.setMeat(selectedMeat);
            System.out.println("✅ Selected " + selectedMeat);
        }
        // Same meat selected again - ask for confirmation
        else if (currentMeat.equals(selectedMeat)) {
            System.out.println("⚠️  " + selectedMeat + " is already selected.");
            System.out.print("Add as extra? (Y/N): ");
            String response = InputValidator.getValidCharChoice(input, "YN");
            if (response.equals("Y")) {
                addExtraMeat(selectedMeat, sandwich);
            }
        }
        // Different meat selected - automatically add as extra
        else {
            addExtraMeat(selectedMeat, sandwich);
        }
    }

    /**
     * Creates and adds an extra meat topping with specific meat type
     */
    private void addExtraMeat(String meatType, Sandwich sandwich) {
        ExtraTopping extraMeat = new ExtraTopping(
                "Extra Meat - " + meatType,
                sandwich.getSize().getExtraSize()
        );
        sandwich.addExtraTopping(extraMeat);
        System.out.println("✅ Added extra " + meatType + " - $" + String.format("%.2f", extraMeat.getPrice()));
    }

    /**
     * Remove extra meats from a numbered list
     * Shows all current extra meats and lets user pick which one to remove
     */
    private void removeExtraMeatScreen(Sandwich sandwich) {
        ArrayList<ExtraTopping> extras = sandwich.getExtraTopping();
        ArrayList<ExtraTopping> meatExtras = new ArrayList<>();

        // Filter only extra meat items
        for (ExtraTopping extra : extras) {
            if (extra.getName().contains("Extra Meat")) {
                meatExtras.add(extra);
            }
        }

        if (meatExtras.isEmpty()) {
            System.out.println("❌ No extra meat to remove.");
            return;
        }

        System.out.println("\n════════════════════════════════════");
        System.out.println("REMOVE EXTRA MEAT");
        System.out.println("════════════════════════════════════");
        for (int i = 0; i < meatExtras.size(); i++) {
            System.out.println((i + 1) + ") " + meatExtras.get(i).getName() +
                    " (-$" + String.format("%.2f", meatExtras.get(i).getPrice()) + ")");
        }
        System.out.println("0) Cancel");

        int targetIndex = InputValidator.getValidListIndex(input, meatExtras.size());
        if (targetIndex != -1) {
            ExtraTopping toRemove = meatExtras.get(targetIndex);
            sandwich.removeExtraTopping(extras.indexOf(toRemove));
            System.out.println("✅ Removed extra " + toRemove.getName());
        }
    }

    /**
     * Display primary meat and all extra meats with pricing
     */
    private void viewMeatAndExtras(Sandwich sandwich) {
        System.out.println("\n════════════════════════════════════");
        System.out.println("CURRENT MEAT SELECTION");
        System.out.println("════════════════════════════════════");
        System.out.println("Primary Meat: " + (sandwich.getMeat() != null ? sandwich.getMeat() : "None"));

        ArrayList<ExtraTopping> extras = sandwich.getExtraTopping();
        ArrayList<ExtraTopping> meatExtras = new ArrayList<>();

        // Filter and display only extra meats
        for (ExtraTopping extra : extras) {
            if (extra.getName().contains("Extra Meat")) {
                meatExtras.add(extra);
            }
        }

        if (meatExtras.isEmpty()) {
            System.out.println("Extra Meats: None");
        } else {
            System.out.println("Extra Meats:");
            for (int i = 0; i < meatExtras.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + meatExtras.get(i).getName() +
                        " - $" + String.format("%.2f", meatExtras.get(i).getPrice()));
            }
        }
        double totalExtrasPrice = meatExtras.stream()
                .mapToDouble(ExtraTopping::getPrice)
                .sum();

        System.out.println("\nExtra Meats Total: $" + String.format("%.2f", totalExtrasPrice));
        System.out.println("════════════════════════════════════\n");
    }

// ============================================================
// SAME IMPROVEMENTS FOR CHEESE - DO THE SAME PATTERN
// ============================================================

    private void cheeseAndExtraScreen(Sandwich sandwich) {
        boolean inCheeseScreen = true;
        while (inCheeseScreen) {
            System.out.println("\n════════════════════════════════════");
            System.out.println("SELECT CHEESE");
            System.out.println("════════════════════════════════════");

            if (sandwich.getSize() == null) {
                System.out.println("\n❌ Action Blocked: Please select sandwich size first!");
                return;
            }

            System.out.println("Pricing: +$1.00 per cheese");
            System.out.println("""
        A) Cheddar
        B) Swiss
        C) Provolone
        D) American
        E) Pepper Jack
        F) No Cheese
        
        Y) Remove Extra Cheese
        Z) View Current Cheese & Extras
        0) Back""");

            String choice = InputValidator.getValidCharChoice(input, "ABCDEFYZ0");
            switch (choice) {
                case "A" -> handleCheeseSelection("Cheddar", sandwich);
                case "B" -> handleCheeseSelection("Swiss", sandwich);
                case "C" -> handleCheeseSelection("Provolone", sandwich);
                case "D" -> handleCheeseSelection("American", sandwich);
                case "E" -> handleCheeseSelection("Pepper Jack", sandwich);
                case "F" -> handleCheeseSelection("None", sandwich);
                case "Y" -> removeExtraCheeseScreen(sandwich);
                case "Z" -> viewCheeseAndExtras(sandwich);
                case "0" -> inCheeseScreen = false;
            }
        }
    }

    /**
     * Smart cheese selection handler (same logic as meat)
     */
    private void handleCheeseSelection(String selectedCheese, Sandwich sandwich) {
        String currentCheese = sandwich.getCheese();

        // First cheese selection
        if (currentCheese == null) {
            sandwich.setCheese(selectedCheese);
            System.out.println("✅ Selected " + selectedCheese);
        }
        // Same cheese selected again - ask for confirmation
        else if (currentCheese.equals(selectedCheese)) {
            System.out.println("⚠️  " + selectedCheese + " is already selected.");
            System.out.print("Add as extra? (Y/N): ");
            String response = InputValidator.getValidCharChoice(input, "YN");
            if (response.equals("Y")) {
                addExtraCheese(selectedCheese, sandwich);
            }
        }
        // Different cheese selected - automatically add as extra
        else {
            addExtraCheese(selectedCheese, sandwich);
        }
    }

    /**
     * Creates and adds an extra cheese topping with specific cheese type
     */
    private void addExtraCheese(String cheeseType, Sandwich sandwich) {
        ExtraTopping extraCheese = new ExtraTopping(
                "Extra Cheese - " + cheeseType,
                sandwich.getSize().getExtraSize()
        );
        sandwich.addExtraTopping(extraCheese);
        System.out.println("✅ Added extra " + cheeseType + " - $" + String.format("%.2f", extraCheese.getPrice()));
    }

    /**
     * Remove extra cheeses from a numbered list
     */
    private void removeExtraCheeseScreen(Sandwich sandwich) {
        ArrayList<ExtraTopping> extras = sandwich.getExtraTopping();
        ArrayList<ExtraTopping> cheeseExtras = new ArrayList<>();

        // Filter only extra cheese items
        for (ExtraTopping extra : extras) {
            if (extra.getName().contains("Extra Cheese")) {
                cheeseExtras.add(extra);
            }
        }

        if (cheeseExtras.isEmpty()) {
            System.out.println("❌ No extra cheese to remove.");
            return;
        }

        System.out.println("\n════════════════════════════════════");
        System.out.println("REMOVE EXTRA CHEESE");
        System.out.println("════════════════════════════════════");
        for (int i = 0; i < cheeseExtras.size(); i++) {
            System.out.println((i + 1) + ") " + cheeseExtras.get(i).getName() +
                    " (-$" + String.format("%.2f", cheeseExtras.get(i).getPrice()) + ")");
        }
        System.out.println("0) Cancel");

        int targetIndex = InputValidator.getValidListIndex(input, cheeseExtras.size());
        if (targetIndex != -1) {
            ExtraTopping toRemove = cheeseExtras.get(targetIndex);
            sandwich.removeExtraTopping(extras.indexOf(toRemove));
            System.out.println("✅ Removed extra " + toRemove.getName());
        }
    }

    /**
     * Display primary cheese and all extra cheeses with pricing
     */
    private void viewCheeseAndExtras(Sandwich sandwich) {
        System.out.println("\n════════════════════════════════════");
        System.out.println("CURRENT CHEESE SELECTION");
        System.out.println("════════════════════════════════════");
        System.out.println("Primary Cheese: " + (sandwich.getCheese() != null ? sandwich.getCheese() : "None"));

        ArrayList<ExtraTopping> extras = sandwich.getExtraTopping();
        ArrayList<ExtraTopping> cheeseExtras = new ArrayList<>();

        // Filter and display only extra cheeses
        for (ExtraTopping extra : extras) {
            if (extra.getName().contains("Extra Cheese")) {
                cheeseExtras.add(extra);
            }
        }

        if (cheeseExtras.isEmpty()) {
            System.out.println("Extra Cheeses: None");
        } else {
            System.out.println("Extra Cheeses:");
            for (int i = 0; i < cheeseExtras.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + cheeseExtras.get(i).getName() +
                        " - $" + String.format("%.2f", cheeseExtras.get(i).getPrice()));
            }
        }

        // Calculate total cheese cost
        double totalExtrasPrice = cheeseExtras.stream()
                .mapToDouble(ExtraTopping::getPrice)
                .sum();

        System.out.println("\nExtra Cheeses Total: $" + String.format("%.2f", totalExtrasPrice));
        System.out.println("════════════════════════════════════\n");
    }
    private void toppingsAndSaucesScreen(Sandwich sandwich) {
        boolean inToppingsAndSauces = true;
        while (inToppingsAndSauces) {
            System.out.println("\n════════════════════════════════════");
            System.out.println("TOPPINGS & SAUCES");
            System.out.println("════════════════════════════════════");
            System.out.println("""
        1) Add Regular Toppings
        2) Remove Regular Toppings
        3) Add Sauces
        4) Remove Sauces
        5) View Current Toppings & Sauces
        0) Back""");

            int choice = InputValidator.getValidIntegerInput(input, "Enter a valid choice (0-5): ", 0, 5);
            switch (choice) {
                case 1 -> toppingsScreen(sandwich);
                case 2 -> removeToppingScreen(sandwich);
                case 3 -> saucesScreen(sandwich);
                case 4 -> removeSaucesScreen(sandwich);
                case 5 -> viewToppingsAndSauces(sandwich);
                case 0 -> inToppingsAndSauces = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void viewToppingsAndSauces(Sandwich sandwich) {
        System.out.println("\n--- Toppings & Sauces ---");

        ArrayList<Topping> toppings = sandwich.getToppings();
        if (toppings.isEmpty()) {
            System.out.println("Toppings: None");
        } else {
            System.out.println("Toppings:");
            for (Topping topping : toppings) {
                System.out.println("  • " + topping.name());
            }
        }

        ArrayList<String> sauces = sandwich.getSauces();
        if (sauces.isEmpty()) {
            System.out.println("Sauces: None");
        } else {
            System.out.println("Sauces:");
            for (String sauce : sauces) {
                System.out.println("  • " + sauce);
            }
        }
        System.out.println();
    }
    private void viewCustomizations(Sandwich sandwich) {
        System.out.println("\n════════════════════════════════════");
        System.out.println("CURRENT CUSTOMIZATIONS");
        System.out.println("════════════════════════════════════");

        System.out.println("Meat: " + (sandwich.getMeat() != null ? sandwich.getMeat() : "None"));
        System.out.println("Cheese: " + (sandwich.getCheese() != null ? sandwich.getCheese() : "None"));

        ArrayList<Topping> toppings = sandwich.getToppings();
        System.out.println("Toppings: " + (toppings.isEmpty() ? "None" : toppings.size() + " added"));

        ArrayList<ExtraTopping> extras = sandwich.getExtraTopping();
        ArrayList<ExtraTopping> meatExtras = new ArrayList<>();
        ArrayList<ExtraTopping> cheeseExtras = new ArrayList<>();

        for (ExtraTopping extra : extras) {
            if (extra.getName().contains("Extra Meat")) {
                meatExtras.add(extra);
            } else if (extra.getName().contains("Extra Cheese")) {
                cheeseExtras.add(extra);
            }
        }

        System.out.println("Extra Meats: " + (meatExtras.isEmpty() ? "None" : meatExtras.size() + " added"));
        System.out.println("Extra Cheeses: " + (cheeseExtras.isEmpty() ? "None" : cheeseExtras.size() + " added"));

        ArrayList<String> sauces = sandwich.getSauces();
        System.out.println("Sauces: " + (sauces.isEmpty() ? "None" : String.join(", ", sauces)));

        System.out.println("\nCurrent Price: $" + String.format("%.2f", sandwich.getPrice()));
        System.out.println("════════════════════════════════════\n");
    }
    private String getPricingBySize(Sandwich.SandwichSize size) {
        if (size == null) return "Not selected";
        return switch (size) {
            case FOUR -> "+$1.00";
            case EIGHT -> "+$2.00";
            case TWELVE -> "+$3.00";
        };
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
3) Add Toppings & Sauces
4) Confirm and Add to Order
0) Cancel Item""");

            int choice = InputValidator.getValidIntegerInput(input, "Pick an option:", 0, 4);
            switch(choice) {
                case 1 -> meatAndExtraScreen(sandwich);
                case 2 -> cheeseAndExtraScreen(sandwich);
                case 3 -> toppingsAndSaucesScreen(sandwich);  // ← This has toppings + sauces
                case 4 -> {
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