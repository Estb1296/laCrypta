package ui;

import model.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CheckOutScreen {
    private final Order currentOrder;
    private final Scanner input;

    public CheckOutScreen(Order currentOrder, Scanner input) {
        this.currentOrder = currentOrder;
        this.input = input;
    }

    public void display() {
        boolean inCheckout = true;
        while (inCheckout) {
            displayOrderSummary();

            System.out.println("\n1) Confirm Order");
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

        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.println("\n" + (i + 1) + ") " + item.toReceiptLine());
            System.out.println(item.getDescription());
        }

        System.out.println("\n=====================================");
        System.out.println("Total: $" + String.format("%.2f", currentOrder.getPrice()));
        System.out.println("=====================================");
    }
    private void removeItemFromCart() {
        System.out.print("Enter item number to remove: ");
        int index = input.nextInt() - 1;
        input.nextLine();
        currentOrder.removeItem(index);
        System.out.println("Item removed");
    }
    private void completeCheckout() {
        double total = currentOrder.getPrice();
        System.out.println("Total Amount: $" + String.format("%.2f", total));
        System.out.println("Thank you for your order!");
        currentOrder.clearCart();
    }
}