package com.pluralsight.ui;

import com.pluralsight.data.OrderRepository;
import com.pluralsight.model.*;
import com.pluralsight.ui.util.ConsoleColor;

import java.io.IOException;

import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final OrderRepository orderRepository = new OrderRepository();
    private final SandwichBuilder sandwichBuilder;  // ← NEW: Delegate sandwich logic
    private Order currentOrder = new Order();

    public UserInterface() {
        // Inject the sandwich builder with the scanner
        this.sandwichBuilder = new SandwichBuilder(scanner);
    }

    /**
     * Entry point: Displays home screen and routes user through the app.
     */
    public void runHomeScreen() {
        displayWelcomeBanner();
        while (true) {
            displayHomeMenu();
            String choice = runMenuAndGetChoice("[1-2Xx]");

            if (choice.equalsIgnoreCase("X")) {
                System.out.println("\n🥪 Thank you for eating at La CRYPTA! Goodbye. 🥪");
                break;
            }

            handleHomeSelection(choice);
        }
    }

    private void displayHomeMenu() {
        System.out.println("\n=========================================");
        System.out.println("      🥪 LA CRYPTA SANDWICH SHOP 🥪       ");
        System.out.println("=========================================");
        System.out.println("[1] New Order");
        System.out.println("[2] Admin");
        System.out.println("[X] Exit Application");
        System.out.print("➔ Select an option: ");
    }

    private void handleHomeSelection(String choice) {
        if (choice.equals("1")) {
            try {
                this.orderRepository.logOrderStart();
                System.out.println(ConsoleColor.YELLOW + "🚀 New session initialized. Token logged to audit registry." + ConsoleColor.RESET);
                this.currentOrder = new Order();
                runOrderMenuScreen();
            } catch (IOException e) {
                System.out.println(ConsoleColor.RED + "⚠️ System Error: " + e.getMessage() + ConsoleColor.RESET);
            }
        } else if (choice.equals("2")) {
            new AdminUserInterface(this.orderRepository);
        }
    }

    /**
     * Order workflow: Allows customer to add items and proceed to checkout.
     */
    private void runOrderMenuScreen() {
        this.currentOrder.clearCart();

        while (true) {
            displayOrderMenu();
            String choice = runMenuAndGetChoice("[0-5]");

            // Handle cancellation
            if (choice.equals("0")) {
                if (!this.currentOrder.getItems().isEmpty()) {
                    this.orderRepository.logCancelledOrder(this.currentOrder);
                    System.out.println("📉 Cart abandoned. Cancellation logged to audit panel.");
                } else {
                    System.out.println("Order cancelled. Returning to home screen.");
                }
                break;
            }

            // Handle checkout
            if (choice.equals("5")) {  // ✅ FIXED: Now checks for [5] Checkout & Pay
                if (currentOrder.getItems().isEmpty()) {
                    System.out.println(ConsoleColor.BOLD + ConsoleColor.RED + "\n❌ Your cart is empty! Add items before checkout.\n" + ConsoleColor.RESET);
                    continue;  // Stay in order menu
                }

                CheckOutScreen checkout = new CheckOutScreen(
                        this.currentOrder,
                        this.orderRepository,
                        this.scanner
                );
                String navigationState = checkout.display();
                if (navigationState.equalsIgnoreCase("HOME")) {
                    break;  // Exit order menu, return to home
                }
                continue;  // Stay in order menu, allow more shopping
            }

            handleOrderSelection(choice);
        }
    }

    private void displayOrderMenu() {
        System.out.println("\n=========================================");
        System.out.println("               ORDER MENU                ");
        System.out.println("=========================================");
        System.out.println("[1] Add Custom Sandwich");
        System.out.println("[2] Signature Sandwich");
        System.out.println("[3] Add Drink");
        System.out.println("[4] Add Chips");
        System.out.println("[5] Checkout & Pay");
        System.out.println("[0] Cancel Order");
        System.out.print("➔ Select an option: ");
    }

    private void handleOrderSelection(String choice) {
        switch (choice) {
            case "1" -> handleCustomSandwichWorkflow();
            case "2" -> handleSignatureSandwichWorkflow();
            case "3" -> runDrinkScreen();
            case "4" -> runChipsScreen();
        }
    }

    /**
     * Route 1: Custom Sandwich
     * Delegates to SandwichBuilder, gets back a Sandwich, confirms with user, adds to order.
     */
    private void handleCustomSandwichWorkflow() {
        // Step 1: Let builder do all the construction work
        Sandwich builtSandwich = sandwichBuilder.buildCustomSandwich();

        // Step 2: Show preview
        sandwichBuilder.displayLiveItemizedBuildProgress();

        // Step 3: Confirm with user before adding
        System.out.println("[1] Confirm & Add to Cart\n[0] Scrap Build & Cancel");
        System.out.print("➔ Choice: ");
        String finalDecision = runMenuAndGetChoice("[0-1]");

        // Step 4: Add to order OR discard
        if (finalDecision.equals("1")) {
            currentOrder.addItem(builtSandwich);
            System.out.println("\n" + ConsoleColor.GREEN + "✅ Custom Sandwich successfully added to your running order tray!" + ConsoleColor.RESET);
        } else {
            System.out.println("\n" + ConsoleColor.RED + "❌ Sandwich configuration discarded." + ConsoleColor.RESET);
        }
    }

    /**
     * Route 2: Signature Sandwich
     * Delegates to SandwichBuilder, gets back a modified Sandwich (or null if canceled).
     */
    private void handleSignatureSandwichWorkflow() {
        // Step 1: Let builder handle signature selection and optional customization
        Sandwich builtSandwich = sandwichBuilder.buildSignatureSandwich();

        // Step 2: If user canceled, return early
        if (builtSandwich == null) {
            System.out.println("Signature sandwich selection cancelled.");
            return;
        }

        // Step 3: Show preview
        sandwichBuilder.displayLiveItemizedBuildProgress();

        // Step 4: Confirm with user
        System.out.println("[1] Confirm & Add Signature to Cart\n[0] Scrap Build & Cancel");
        System.out.print("➔ Choice: ");
        String finalDecision = runMenuAndGetChoice("[0-1]");

        // Step 5: Add to order OR discard
        if (finalDecision.equals("1")) {
            currentOrder.addItem(builtSandwich);
            System.out.println("\n✅ " + builtSandwich.getName() + " added directly to your order!");
        } else {
            System.out.println("\n❌ Signature sandwich selection cancelled.");
        }
    }

    /**
     * Add a drink to the order.
     * Simplified version — no complex builder needed.
     */
    private void runDrinkScreen() {
        String[] flavors = {"Cola", "Diet Cola", "Sprite", "Root Beer", "Mountain Dew", "Iced Tea"};

        runSideSelectionScreen("Drink", flavors, 2.50);
    }

    /**
     * Add chips to the order.
     * Simplified version — no complex builder needed.
     */
    private void runChipsScreen() {
        String[] brands = {"Lays Classic", "Doritos Nacho Cheese", "Cheetos Flamin' Hot", "BBQ Kettle Chips"};
        runSideSelectionScreen("Chips", brands, 1.50);
    }

    /**
     * Generic side item selection pipeline.
     * Handles both drinks and chips with a single reusable method.
     */
    private void runSideSelectionScreen(String itemType, String[] options, double price) {
        System.out.println("\n--- Available " + itemType + " Menu ---");
        for (int i = 0; i < options.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, options[i]);
        }
        System.out.println("[0] Cancel / Go Back");
        System.out.print("➔ Choice: ");

        String choice = runMenuAndGetChoice("[0-" + options.length + "]");
        if (choice.equals("0")) return;  // Guard clause: user canceled

        String selectedName = options[Integer.parseInt(choice) - 1];

        MenuItem sideItem;

        // Handle Size Selection conditionally
        if (itemType.equals("Drink")) {
            // 1. Ask for the size first
            String selectedSize = askForSize();
            if (selectedSize == null) return; // User canceled out of the size menu

            // 2. Pass both name AND size to your Drink constructor
            sideItem = new Drink(selectedName, selectedSize);
        } else {
            // Handle Chips or other items
            sideItem = new Chips(selectedName, price, "");
        }

        currentOrder.addItem(sideItem);
        System.out.println(ConsoleColor.GREEN + "✅ Added " + selectedName + " to cart!" + ConsoleColor.RESET);
    }

    /**
     * Utility: Get validated menu input based on regex pattern.
     */
    private String runMenuAndGetChoice(String regexBounds) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches(regexBounds)) {
                return input;
            }
            System.out.print(ConsoleColor.BOLD + ConsoleColor.RED + "⚠️ Invalid entry. Please read options and retry: " + ConsoleColor.RESET);
        }
    }

    private String askForSize() {
        System.out.println("\n--- Select Size ---");
        System.out.println("[1] Small");
        System.out.println("[2] Medium");
        System.out.println("[3] Large");
        System.out.println("[0] Cancel");
        System.out.print("➔ Choice: ");

        String choice = runMenuAndGetChoice("[0-3]");

        switch (choice) {
            case "1" -> {
                return "Small";
            }
            case "2" -> {
                return "Medium";
            }
            case "3" -> {
                return "Large";
            }
            default -> {
                return null; // User typed 0 or canceled
            }
        }
    }

    private void displayWelcomeBanner() {
        System.out.println(ConsoleColor.BOLD + ConsoleColor.CYAN);
        System.out.println("  _______________________________________________  ");
        System.out.println(" /                                               \\ ");
        System.out.println(" |   _     _____   ____   ____  _   _ _____ _____ | ");
        System.out.println(" |  | |   |  _  | |  __| |  _ \\| | | |_   _|  _  || ");
        System.out.println(" |  | |___|     | | |___ |     | |_| | | | |     || ");
        System.out.println(" |  |_____|_|\\__| |____| |_|\\_\\|_____| |_| |_|\\_||| ");
        System.out.println(" \\_______________________________________________/ ");
        System.out.println("     (@@)  (@@)  (@@)  (@@)  (@@)  (@@)  (@@)      ");
        System.out.println("   =============================================   ");
        System.out.println("   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   ");
        System.out.println("   [===========================================]   ");
        System.out.println("               ~ Underground Subs ~                ");
        System.out.print(ConsoleColor.RESET);
    }
}