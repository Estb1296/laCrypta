package com.pluralsight.ui;

import com.pluralsight.data.OrderRepository;

import com.pluralsight.model.*;
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
        System.out.println("\n========== ORDER SUMMARY ==========");
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
        // We use a regular integer now since a sequential stream loop doesn't require concurrency safety
        //I'm passing by reference by using a pointer to be able to compile item as a mutable object which can't be done in streams
        java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger(1);

        //Filter and process using forEach for good optimization
        items.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category)) // Uses my abstract getCategory() method seamlessly!
                .forEach(item -> {
                    // Print the item dynamically
                    System.out.println(" " + counter.getAndIncrement() + ") " + item.getName() + " - $" + String.format("%.2f", item.getPrice()));

                    // Use Java Pattern Matching to cleanly
                    if (item instanceof Sandwich sandwich) {
                        System.out.println(sandwich.getDescription());
                    }
                    if(item instanceof Chips chips){
                        System.out.println(chips.getDescription());
                    }
                    if(item instanceof Drink drink){
                        System.out.println(drink.getDescription());
                    }
                });

        //If the counter never incremented, nothing matched this category
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
