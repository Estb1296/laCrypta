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
    public void display() {
        boolean inCheckout = true;
        while (inCheckout) {
            displayOrderSummary();

            System.out.println("1) Confirm Order");
            System.out.println("2) Continue Shopping");
            System.out.println("0) Cancel Order");

            int choice = input.nextInt();
            input.nextLine();
                switch (choice) {
                   case 1 -> {
                        completeCheckout();
                        inCheckout = false;  // EXIT LOOP HERE cause once a person confirms they don't need to stay in the checkout screen
                    }
                    case 2 -> removeItemFromCart();
                    case 3 -> inCheckout = false;
                    case 0 -> {
                        currentOrder.clearCart();
                        inCheckout = false;
                    }
                    default -> System.out.println("Invalid choice");
                }

    }
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
        System.out.print("Enter item number to remove: ");
        int index = input.nextInt() - 1;
        input.nextLine();
        currentOrder.removeItem(index);
        System.out.println("Item removed");
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
