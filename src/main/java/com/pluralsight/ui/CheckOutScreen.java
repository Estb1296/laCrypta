package com.pluralsight.ui;

import com.pluralsight.data.OrderRepository;

import com.pluralsight.model.*;
import com.pluralsight.ui.util.ConsoleColor;
import com.pluralsight.ui.util.InputValidator;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

public class CheckOutScreen {
    private final Order currentOrder;
    private final Scanner input;
    private final OrderRepository orderRepository;
    private boolean hasUsedPromo = false;

    public CheckOutScreen(Order currentOrder, OrderRepository orderRepository, Scanner input) {
        this.currentOrder = currentOrder;
        this.orderRepository = orderRepository;
        this.input = input;
    }

    public String display() {
        while (true) {
            displayOrderSummary();
            System.out.println("1) Confirm Order");
            System.out.println("2) Continue Shopping");
            System.out.println("3) Remove Item from Cart");
            System.out.println("4) Apply Custom Dollar Coupon");
            System.out.println("0) Cancel Order");

            int choice = InputValidator.getValidIntegerInput(input, "Enter choice: ", 0, 4);


            switch (choice) {
                case 1 -> {
                    completeCheckout();

                    return "HOME";  // ✅ Order complete, go home
                }
                case 2 -> {
                    System.out.println("Returning to shopping...");
                    return "CONTINUE";  // ✅ Continue shopping
                }
                case 3 -> removeItemFromCart();
                case 4 -> handlePromoCodeWorkflow();
                case 0 -> {
                    System.out.println("Order cancelled.");
                    currentOrder.clearCart();

                    return "HOME";  // ✅ Go back home
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void handlePromoCodeWorkflow() {
        // 🛑 Rule Check: Have they already used a code?
        if (hasUsedPromo) {
            System.out.println("\n⚠️ Error: You have already applied a promo code to this session. Only one promo code is allowed per customer purchase!");
            return;
        }

        System.out.print("Enter secret authorization code: ");
        String userEntry = input.nextLine().trim();

        if (currentOrder.isValidPromoCode(userEntry)) {
            System.out.println("\n🔑 Access Granted! Unlocked Variable Discount Mode.");

            double discountValue = 0.0;
            boolean isValidAmount = false;

            while (!isValidAmount) {
                discountValue = InputValidator.getValidDoubleInput(input,
                        "Enter the dollar amount to deduct (Max $10.00): $");

                if (discountValue > 10.00) {
                    System.out.println("⚠️ Error: This promo code only allows a maximum deduction of $10.00. Please try again.");
                } else if (discountValue < 0) {
                    System.out.println("⚠️ Error: Discount cannot be negative.");
                } else {
                    isValidAmount = true;
                }
            }

            currentOrder.setCouponDiscountAmount(discountValue);

            // the flag so they can never enter this block again!
            hasUsedPromo = true;

            System.out.println("🎉 Success! Apply Discount: -$" + String.format("%.2f", discountValue));

        } else {
            System.out.println("\n❌ Access Denied. Invalid authorization code.");
        }

    }

    private void displayOrderSummary() {
        System.out.println(ConsoleColor.CYAN+"\n========== ORDER SUMMARY =========="+ConsoleColor.RESET);
        ArrayList<MenuItem> items = currentOrder.getItems();

        if (items.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }

        ArrayList<MenuItem> reversedItems = new ArrayList<>(items);
        java.util.Collections.reverse(reversedItems);
        displayItemsByCategory(items, "Sandwich");
        displayItemsByCategory(items, "Chips");
        displayItemsByCategory(items, "Drink");
        if (currentOrder.getCouponDiscount() > 0) {
            System.out.println(" Promo Discount: -$" + String.format("%.2f", currentOrder.getCouponDiscount()));
        }

        System.out.println("\n=====================================");
        System.out.println("Total: $" + String.format("%.2f", currentOrder.calculatePrice()));
        System.out.println("=====================================");
    }

    private void displayItemsByCategory(ArrayList<MenuItem> items, String category) {
//        “During development, I prioritized visual feedback in the console to ensure my data filtering and streaming logic worked flawlessly. However, looking at the architecture with an experienced eye, my displayItemsByCategory method currently contains an architectural flaw: it violates the Open/Closed Principle by using instanceof pattern matching to format specific subclasses like Sandwich and Chips.
//        If I were taking this production-ready or scaling it to the JavaFX front end I have planned, I would decouple this by implementing a polymorphic getLineItemDetails() method on the MenuItem base class. This would push the formatting and pricing matrices back down to the domain models, keeping my UI layer completely clean, interchangeable, and open for extension without modification.”
        java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger(1);

        items.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .forEach(item -> {
                    //  Print the item line-item dynamically (Bold name, plain price)
                    System.out.println(" " + counter.getAndIncrement() + ") " + ConsoleColor.BOLD + item.getName() + ConsoleColor.RESET + " - $" + String.format("%.2f", item.calculatePrice()));

                    // Pattern Match for Sandwich to break down Steak & Cheese Costs!
                    if (item instanceof Sandwich sandwich) {
                        // 🥪 Base Sub Layout Price
                        if (sandwich.getSize() != null) {
                            System.out.printf(ConsoleColor.GRAY + "       ◦ Base Sub Layout:           $%5.2f\n" + ConsoleColor.RESET,
                                    sandwich.getSize().getPrice());
                        }

                        // 🥩 Core Protein Dynamic Cost Breakdown
                        if (sandwich.getMeat() != null && !sandwich.getMeat().equalsIgnoreCase("None")) {
                            double meatCost = switch (sandwich.getSize()) {
                                case FOUR -> 1.00;
                                case EIGHT -> 2.00;
                                case TWELVE -> 3.00;
                            };
                            System.out.printf(ConsoleColor.PURPLE + "       ◦ Core Protein: %-13s +$%5.2f\n" + ConsoleColor.RESET,
                                    sandwich.getMeat(), meatCost);
                        }

                        // 🧀 Core Cheese Dynamic Cost Breakdown
                        if (sandwich.getCheese() != null && !sandwich.getCheese().equalsIgnoreCase("None")) {
                            double cheeseCost = switch (sandwich.getSize()) {
                                case FOUR -> 1.00;
                                case EIGHT -> 2.00;
                                case TWELVE -> 3.00;
                            };
                            System.out.printf(ConsoleColor.YELLOW + "       ◦ Core Cheese:  %-13s +$%5.2f\n" + ConsoleColor.RESET,
                                    sandwich.getCheese(), cheeseCost);
                        }

                        // 🚀 Extra Premium Additions
                        if (!sandwich.getExtraToppings().isEmpty()) {
                            sandwich.getExtraToppings().forEach(extra -> {
                                String color = extra.isCheese() ? ConsoleColor.YELLOW : ConsoleColor.PURPLE;
                                System.out.printf(color + "       + Extra %s: %-15s +$%5.2f\n" + ConsoleColor.RESET,
                                        extra.isCheese() ? "Chs" : "Meat", extra.getName(), extra.calculatePrice());
                            });
                        }

                        // 🥬 Free Veggies & Sauces Grouped Cleanly
                        if (!sandwich.getToppings().isEmpty()) {
                            System.out.printf(ConsoleColor.GREEN + "       ◦ Veggies: %s\n" + ConsoleColor.RESET,
                                    String.join(", ", sandwich.getToppings().stream()
                                            .map(Topping::name) // 🚀 Super clean and concise!
                                            .toList()));
                        }
                        if (!sandwich.getSauces().isEmpty()) {
                            System.out.printf(ConsoleColor.CYAN + "       ◦ Sauces:  %s\n" + ConsoleColor.RESET,
                                    String.join(", ", sandwich.getSauces()));
                        }
                    }

                    // Keep your standard descriptions for other items intact
                    if (item instanceof Chips chips) {
                        System.out.println(ConsoleColor.GRAY + "       " + chips.getDescription() + ConsoleColor.RESET);
                    }
                    if (item instanceof Drink drink) {
                        System.out.println(ConsoleColor.GRAY + "       " + drink.getDescription() + ConsoleColor.RESET);
                    }
                });
    }

    private void removeItemFromCart() {
        ArrayList<MenuItem> items = currentOrder.getItems();

        if (items.isEmpty()) {
            System.out.println("❌ Cart is empty, nothing to remove.");
            return;
        }

        // Display items with numbers
        System.out.println("\n📦 ITEMS IN CART:");
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.println("  " + (i + 1) + ") " + item.getName() +
                    " - $" + String.format("%.2f", item.getPrice()));
        }

        int targetIndex = InputValidator.getValidListIndex(input, items.size());

        if (targetIndex == -1) {
            System.out.println("Removal cancelled.");
            return;
        }

        // targetIndex is guaranteed to be safe and already converted to a 0-based index!
        MenuItem removedItem = items.get(targetIndex);
        String removedName = removedItem.getName();
        double removedPrice = removedItem.getPrice();

        // Remove the item using the safe index
        currentOrder.removeItem(targetIndex);

        // Show confirmation
        System.out.println("\n✅ ITEM REMOVED:");
        System.out.println("   Removed: " + removedName + " (-$" + String.format("%.2f", removedPrice) + ")");
        System.out.println("   New Cart Total: $" + String.format("%.2f", currentOrder.calculatePrice()));
        System.out.println("   Items Remaining: " + currentOrder.getItems().size() + "\n");
    }

    private void completeCheckout() {
        double total = currentOrder.calculatePrice();
        System.out.println("\n========== CHECKOUT ==========");
        System.out.println("Total Amount: $" + String.format("%.2f", total));
        boolean userConfirmed = InputValidator.getConfirmation(input, "Are you sure you want to finalize this order?");

        if (!userConfirmed) {
            System.out.println("\n❌ Checkout paused. Returning to the checkout menu with your items intact.");
            return; // 🛑 Early exit! Drops out of this method immediately, leaving the cart exactly as it was.
        }

        try {
            // Use repository to handle receipt + audit logging
            String filename = orderRepository.completeOrder(currentOrder);
            System.out.println("Receipt saved: " + filename);
            System.out.println("Thank you for your order!");

        } catch (IOException e) {
            System.out.println("Error saving receipt: " + e.getMessage());
        }

        currentOrder.clearCart();
    }
}
