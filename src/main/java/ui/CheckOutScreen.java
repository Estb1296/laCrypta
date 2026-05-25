package ui;

import data.OrderRepository;

import model.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

public class CheckOutScreen {
    private final Order currentOrder;
    private final Scanner input;
    private final OrderRepository orderRepository;
    public CheckOutScreen(Order currentOrder, OrderRepository orderRepository, Scanner input) {
        this.currentOrder = currentOrder;
        this.orderRepository = orderRepository;
        this.input = input;
    }
    public String display() {

            displayOrderSummary();

            System.out.println("1) Confirm Order");
            System.out.println("2) Continue Shopping");
            System.out.println("3) Remove Item from Cart");
            System.out.println("0) Cancel Order");

            int choice = input.nextInt();
            input.nextLine();

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
                case 0 -> {
                    System.out.println("Order cancelled.");
                    currentOrder.clearCart();

                    return "HOME";  // ✅ Go back home
                }
                default -> System.out.println("Invalid choice");
            }
        return "HOME";  // Default fallback
    }

    private void displayOrderSummary() {
        System.out.println("\n========== ORDER SUMMARY ==========");
        ArrayList<MenuItem> items = currentOrder.getItems();

        if (items.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }

        displayItemsByCategory(items, "Sandwich");
        displayItemsByCategory(items, "Drink");
        displayItemsByCategory(items, "Chips");

        System.out.println("\n=====================================");
        System.out.println("Total: $" + String.format("%.2f", currentOrder.calculatePrice()));
        System.out.println("=====================================");
    }

    private void displayItemsByCategory(ArrayList<MenuItem> items, String category) {
        // We use a regular integer now since a sequential stream loop doesn't require concurrency safety
        java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger(1);

        // 1. Filter and process using forEach for good optimization
        items.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category)) // Uses your abstract getCategory() method seamlessly!
                .forEach(item -> {
                    // Print the item dynamically
                    System.out.println(" " + counter.getAndIncrement() + ") " + item.getName() + " - $" + String.format("%.2f", item.getPrice()));

                    // Use Java Pattern Matching to cleanly
                    if (item instanceof Sandwich sandwichObj) {
                        System.out.println(sandwichObj.getDescription());
                    }
                });

        //If the counter never incremented, nothing matched this category
        if (counter.get() == 1) {
            System.out.println("  (None selected)");
        }
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

        System.out.print("\n🗑️  Enter item number to remove (0 to cancel): ");
        int choice = input.nextInt();
        input.nextLine();

        if (choice == 0) {
            System.out.println("Removal cancelled.");
            return;
        }

        if (choice > 0 && choice <= items.size()) {
            // Store info before removing
            MenuItem removedItem = items.get(choice - 1);
            String removedName = removedItem.getName();
            double removedPrice = removedItem.getPrice();

            // Remove the item
            currentOrder.removeItem(choice - 1);

            // Show confirmation
            System.out.println("\n✅ ITEM REMOVED:");
            System.out.println("   Removed: " + removedName + " (-$" +
                    String.format("%.2f", removedPrice) + ")");
            System.out.println("   New Cart Total: $" +
                    String.format("%.2f", currentOrder.calculatePrice()));
            System.out.println("   Items Remaining: " + currentOrder.getItems().size() + "\n");
        } else {
            System.out.println("❌ Invalid item number.");
        }
    }
    private void completeCheckout() {
        double total = currentOrder.calculatePrice();
        System.out.println("\n========== CHECKOUT ==========");
        System.out.println("Total Amount: $" + String.format("%.2f", total));

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
