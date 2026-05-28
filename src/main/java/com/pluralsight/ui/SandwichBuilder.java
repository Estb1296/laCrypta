package com.pluralsight.ui;

import com.pluralsight.model.*;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SandwichBuilder {
    private final Scanner scanner;
    private Sandwich currentSandwich;

    public SandwichBuilder(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Guide user through building a custom sandwich from scratch.
     * Returns the completed Sandwich object.
     */
    public Sandwich buildCustomSandwich() {
        System.out.println("\n--- Initiating Custom Sandwich Builder ---");
        this.currentSandwich = new Sandwich();

        // Sequential pipeline — each step modifies currentSandwich
        runSizeSelectionScreen();
        runBreadSelectionScreen();
        runMeatSelectionScreen();
        runCheeseSelectionScreen();
        runRegularToppingSelectionScreen();
        runSauceSelectionScreen();
        runToastSelectionScreen();

        return currentSandwich;  // ← Return, don't add to order
    }

    /**
     * Guide user through building a signature sandwich template with optional modifications.
     * Returns the completed Sandwich object, or null if user cancelled.
     */
    public Sandwich buildSignatureSandwich() {
        System.out.println("\n--- 🌟 La Crypta Signature Sandwiches 🌟 ---");
        System.out.println("[1] Classic BLT (Bacon, Cheddar, Ranch, Lettuce, Tomato on 8\" White)");
        System.out.println("[2] Philly Cheese Steak (Steak, American, Mayo, Peppers on 8\" White)");
        System.out.println("[0] Cancel / Go Back");
        System.out.print("➔ Choose a Signature Sandwich: ");

        String choice = runMenuAndGetChoice("[0-2]");

        if (choice.equals("0")) {
            return null;  // ← User cancelled, caller will detect this
        }

        Sandwich signatureSandwich = switch (choice) {
            case "1" -> new BLT();
            case "2" -> new PhillyCheeseSteak();
            default  -> null;
        };

        if (signatureSandwich == null) {
            return null;
        }

        // Point the builder's workspace to this signature instance
        this.currentSandwich = signatureSandwich;

        // Ask if they want to modify the template
        System.out.println("\nWould you like to customize this signature sandwich template?");
        System.out.println("[1] Yes, modify ingredients (Add/Remove)");
        System.out.println("[2] No, keep it exactly as advertised");
        System.out.print("➔ Choice: ");

        if (runMenuAndGetChoice("[1-2]").equals("1")) {
            runSignatureEditWorkflow();
        }

        return currentSandwich;  // ← Return the possibly-modified signature
    }

    /**
     * Dedicated editing loop for signature sandwich modifications.
     * Modifies currentSandwich in place.
     */
    private void runSignatureEditWorkflow() {
        while (true) {
            System.out.println("\n--- 🛠️ Template Customization: " + currentSandwich.getName() + " ---");
            System.out.println("[1] Customize/Toggle Vegetables (Adds if missing, Removes if present)");
            System.out.println("[2] Rewrite Sauces (Clears current sauces and lets you choose fresh)");
            System.out.println("[3] Add Extra Premium Protein");
            System.out.println("[4] Add Extra Premium Cheese");
            System.out.println("[5] Change Toasting Preference");
            System.out.println("[0] Done Customizing & Save Sandwich");
            System.out.print("➔ Select modification section: ");

            String choice = runMenuAndGetChoice("[0-5]");
            if (choice.equals("0")) {
                break;  // Done editing, proceed to confirmation
            }

            switch (choice) {
                case "1" -> runSignatureToppingToggleScreen();
                case "2" -> {
                    currentSandwich.clearSauces();
                    System.out.println("🍾 Current sauces wiped clean. Please select your new sauce layout:");
                    runSauceSelectionScreen();
                }
                case "3" -> runExtraPremiumSelection("Meat");
                case "4" -> runExtraPremiumSelection("Cheese");
                case "5" -> runToastSelectionScreen();
            }
        }
    }

    /**
     * Display sandwich preview to user.
     * Static method so caller can display before/after building.
     */
    public void displaySandwichPreview() {
        System.out.println("\n=========================================");
        System.out.println("       🥪 SANDWICH BUILD PREVIEW 🥪      ");
        System.out.println("=========================================");
        System.out.println("   Name:        " + currentSandwich.getName());
        System.out.println("   Size:        " + currentSandwich.getSize());
        System.out.println("   Bread:       " + currentSandwich.getBread());
        System.out.println("   Toasted:     " + (currentSandwich.isToasted() ? "Yes 🔥" : "No ❄️"));
        System.out.println("   Base Meat:   " + currentSandwich.getMeat());
        System.out.println("   Base Cheese: " + currentSandwich.getCheese());

        System.out.print("   Premium Extras: ");
        if (currentSandwich.getExtraTopping().isEmpty()) {
            System.out.println("None");
        } else {
            System.out.println();
            currentSandwich.getExtraTopping().forEach(extra ->
                    System.out.println("     • Extra " + extra.getName())
            );
        }

        System.out.print("   Vegetables:  ");
        if (currentSandwich.getToppings().isEmpty()) {
            System.out.println("None");
        } else {
            System.out.println();
            currentSandwich.getToppings().forEach(veg ->
                    System.out.println("     • " + veg.name())
            );
        }

        System.out.print("   Sauces:      ");
        if (currentSandwich.getSauces().isEmpty()) {
            System.out.println("None");
        } else {
            System.out.println();
            currentSandwich.getSauces().forEach(sauce ->
                    System.out.println("     • " + sauce)
            );
        }

        System.out.println("-----------------------------------------");
        System.out.printf("   Current Item Price: $%.2f%n", currentSandwich.calculatePrice());
        System.out.println("=========================================");
    }

    // ====== PRIVATE SELECTION SCREENS ======

    private void runSizeSelectionScreen() {
        System.out.println("\n➔ Select Sandwich Size:");
        System.out.println("[1] 4\" Sub\n[2] 8\" Sub\n[3] 12\" Sub");
        System.out.print("➔ Choice: ");
        String choice = runMenuAndGetChoice("[1-3]");

        switch (choice) {
            case "1" -> currentSandwich.setSize(Sandwich.SandwichSize.FOUR);
            case "2" -> currentSandwich.setSize(Sandwich.SandwichSize.EIGHT);
            case "3" -> currentSandwich.setSize(Sandwich.SandwichSize.TWELVE);
        }
    }

    private void runBreadSelectionScreen() {
        System.out.println("\n➔ Select Choice of Bread:");
        System.out.println("[1] White\n[2] Wheat\n[3] Rye\n[4] Wrap");
        System.out.print("➔ Choice: ");
        String choice = runMenuAndGetChoice("[1-4]");

        currentSandwich.setBread(switch (choice) {
            case "1" -> "White";
            case "3" -> "Rye";
            case "4" -> "Wrap";
            default -> "Wheat";
        });
    }

    private void runMeatSelectionScreen() {
        System.out.println("\n➔ Select Premium Protein:");
        System.out.println("[1] Steak\n[2] Roast Beef\n[3] Turkey\n[4] Ham\n[5] Chicken\n[0] No Meat (Veggie)");
        System.out.print("➔ Choice: ");
        String choice = runMenuAndGetChoice("[0-5]");

        if (choice.equals("0")) {
            currentSandwich.setMeat("None");
            return;
        }

        currentSandwich.setMeat(switch (choice) {
            case "1" -> "Steak";
            case "2" -> "Roast Beef";
            case "3" -> "Turkey";
            case "4" -> "Ham";
            case "5" -> "Chicken";
            default -> "None";
        });

        runExtraPremiumSelection("Meat");
    }

    private void runCheeseSelectionScreen() {
        System.out.println("\n➔ Select Premium Cheese:");
        System.out.println("[1] American\n[2] Provolone\n[3] Cheddar\n[4] Swiss\n[0] No Cheese");
        System.out.print("➔ Choice: ");
        String choice = runMenuAndGetChoice("[0-4]");

        if (choice.equals("0")) {
            currentSandwich.setCheese("None");
            return;
        }

        currentSandwich.setCheese(switch (choice) {
            case "1" -> "American";
            case "2" -> "Provolone";
            case "3" -> "Cheddar";
            case "4" -> "Swiss";
            default -> "None";
        });

        runExtraPremiumSelection("Cheese");
    }

    private void runExtraPremiumSelection(String type) {
        System.out.println("\n➔ Would you like to add Extra " + type + "?");
        System.out.println("[1] Yes\n[2] No");
        System.out.print("➔ Choice: ");
        String choice = runMenuAndGetChoice("[1-2]");
        if (choice.equals("2")) {
            return;
        }

        boolean isCheese = type.equalsIgnoreCase("Cheese");
        String extraIngredientName;

        if (isCheese) {
            System.out.println("\n➔ Select Type of Extra Cheese:");
            System.out.println("[1] American\n[2] Provolone\n[3] Cheddar\n[4] Swiss");
            String cheeseChoice = runMenuAndGetChoice("[1-4]");
            extraIngredientName = switch (cheeseChoice) {
                case "1" -> "American";
                case "2" -> "Provolone";
                case "3" -> "Cheddar";
                default  -> "Swiss";
            };
        } else {
            System.out.println("\n➔ Select Type of Extra Meat:");
            System.out.println("[1] Steak\n[2] Roast Beef\n[3] Turkey\n[4] Ham\n[5] Chicken");
            String meatChoice = runMenuAndGetChoice("[1-5]");
            extraIngredientName = switch (meatChoice) {
                case "1" -> "Steak";
                case "2" -> "Roast Beef";
                case "3" -> "Turkey";
                case "4" -> "Ham";
                default  -> "Chicken";
            };
        }

        ExtraTopping extra = new ExtraTopping(
                extraIngredientName,
                isCheese,
                currentSandwich.getSize().getExtraSize()
        );
        currentSandwich.addExtraTopping(extra);
        System.out.println("➕ Extra " + extraIngredientName + " successfully appended.");
    }

    private void runRegularToppingSelectionScreen() {
        List<Topping> standardVeggies = Arrays.asList(
                new Topping("Lettuce", false), new Topping("Peppers", false),
                new Topping("Tomatoes", false), new Topping("Onions", false),
                new Topping("Cucumbers", false), new Topping("Pickles", false),
                new Topping("Jalapenos", false), new Topping("Olives", false)
        );
        runToppingAdditionScreen(standardVeggies);
    }

    private void runToppingAdditionScreen(List<Topping> toppingsCatalog) {
        System.out.println("\n--- Vegetable Selection ---");
        for (int i = 0; i < toppingsCatalog.size(); i++) {
            System.out.printf("[%d] Add %s%n", i + 1, toppingsCatalog.get(i).name());
        }
        System.out.println("[0] Done with standard toppings selections");

        while (true) {
            System.out.print("➔ Select a code: ");
            String choice = runMenuAndGetChoice("[0-" + toppingsCatalog.size() + "]");
            if (choice.equals("0")) break;

            Topping chosen = toppingsCatalog.get(Integer.parseInt(choice) - 1);
            currentSandwich.addTopping(chosen);
            System.out.println("➕ Dropped: " + chosen.name());
        }
    }

    private void runSignatureToppingToggleScreen() {
        List<Topping> standardVeggies = Arrays.asList(
                new Topping("Lettuce", false), new Topping("Peppers", false),
                new Topping("Tomatoes", false), new Topping("Onions", false),
                new Topping("Cucumbers", false), new Topping("Pickles", false),
                new Topping("Jalapenos", false), new Topping("Olives", false)
        );

        System.out.println("\n--- 🥬 Vegetable Toggle Station 🥬 ---");
        System.out.println("Selecting an item already on the template will REMOVE it.");
        for (int i = 0; i < standardVeggies.size(); i++) {
            System.out.printf("[%d] Toggle %s%n", i + 1, standardVeggies.get(i).name());
        }
        System.out.println("[0] Done with vegetable modifications");

        while (true) {
            System.out.print("➔ Select a topping code to toggle: ");
            String choice = runMenuAndGetChoice("[0-" + standardVeggies.size() + "]");
            if (choice.equals("0")) break;

            Topping chosen = standardVeggies.get(Integer.parseInt(choice) - 1);
            currentSandwich.toggleTopping(chosen);
        }
    }

    private void runSauceSelectionScreen() {
        List<String> availableSauces = Arrays.asList(
                "Mayo", "Mustard", "Ketchup", "Ranch", "Thousand Island", "Vinaigrette"
        );

        System.out.println("\n--- Sauce Configuration Tray ---");
        for (int i = 0; i < availableSauces.size(); i++) {
            System.out.printf("[%d] Add %s%n", i + 1, availableSauces.get(i));
        }
        System.out.println("[0] Finished with Sauces");

        while (true) {
            System.out.print("➔ Choose a sauce code: ");
            String choice = runMenuAndGetChoice("[0-" + availableSauces.size() + "]");
            if (choice.equals("0")) break;

            String selectedSauce = availableSauces.get(Integer.parseInt(choice) - 1);
            currentSandwich.addSauce(selectedSauce);
            System.out.println("🍾 Splashed: " + selectedSauce);
        }
    }

    private void runToastSelectionScreen() {
        System.out.println("\n➔ Do you want the sandwich toasted?");
        System.out.println("[1] Toast It!\n[2] Leave it Fresh/Cold");
        System.out.print("➔ Choice: ");
        String choice = runMenuAndGetChoice("[1-2]");
        currentSandwich.setToasted(choice.equals("1"));
    }

    // ====== UTILITY ======

    private String runMenuAndGetChoice(String regexBounds) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches(regexBounds)) {
                return input;
            }
            System.out.print("⚠️ Invalid entry. Please read options and retry: ");
        }
    }
}
