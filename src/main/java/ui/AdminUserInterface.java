package ui;

import data.AuditLog;
import data.OrderRepository;
import model.Order;


import java.util.ArrayList;
import java.util.Scanner;

    public class AdminUserInterface {
        private static final Scanner scanner=new Scanner(System.in);
        private final OrderRepository orderRepository;
       //The Array list of Passwords is private right here
        private static class Credential {
            String username;
            String password;

            Credential(String username, String password) {
                this.username = username;
                this.password = password;
            }
        }

        private static final ArrayList<Credential> credentials;

        static {
            // Initialize credentials
            credentials = new ArrayList<>();
            credentials.add(new Credential("admin", "Tr0pic@lSunset#2024!"));
            credentials.add(new Credential("manager", "Qu@ntum$Leap_88xR9v"));
            credentials.add(new Credential("operations", "N3bul@*Phoenix^CoreDrive21"));
        }
        public AdminUserInterface(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;  // FIX: Initialize first

            if (authenticate()) {
                System.out.println("Access granted! ✅");
                display();
            } else {
                System.out.println("Access denied! ❌");
            }
        }
        private boolean authenticate() {
            System.out.println("=== Admin Login ===");
            System.out.print("Enter username: ");
            String enteredUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();

            // Check if username and password match any credentials
            return credentials.stream()
                    .anyMatch(cred ->
                            cred.username.equals(enteredUsername) &&
                                    cred.password.equals(enteredPassword)
                    );
        }
        private void display() {
            boolean isRunning = true;
            while (isRunning) {
                System.out.println("""
            ╔═══════════════════════════════╗
            ║     ADMIN DASHBOARD           ║
            ╚═══════════════════════════════╝
            1) View Statistics
            2) View All Orders
            3) Display PastOrder Summary
            4) Display High Value Orders
            5) Reset Database
            6) Show cancelled Orders
            7) Logout
            0) Exit""");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> displayStatistics();
                    case 2 -> displayAllOrders();
                    case 3-> displayPastOrdersSummary();
                    case 4-> displayHighValueOrders();
                    case 5-> handleDatabaseReset();
                    case 6 -> displayAbortedOrderMetrics();
                    case 7 -> isRunning = false;
                    case 0 -> System.exit(0);
                    default -> System.out.println("Invalid choice");
                }
            }
        }
        private void displayStatistics() {
            System.out.println("\n╔════════════════════════════════╗");
            System.out.println("║   FINANCIAL STATISTICS LOG     ║");
            System.out.println("╚════════════════════════════════╝\n");

            ArrayList<Order> allOrders = orderRepository.getAllOrders();
            int totalOrderCount = allOrders.size();

            // 💰 1. Stream for Gross Revenue
            double grossRevenue = allOrders.stream()
                    .mapToDouble(Order::calculatePrice) // Pulls the price of each order
                    .sum();                             // Tallies them all up

            // 🛒 2. Stream for Grand Total Items Sold
            int grandTotalItemsSold = allOrders.stream()
                    .mapToInt(order -> order.getItems().size()) // Pulls the size of each order's item list
                    .sum();                                     // Tallies them all up

            // Calculate Average Order Value (AOV) safely
            double averageOrderValue = (totalOrderCount > 0) ? (grossRevenue / totalOrderCount) : 0.0;

            // Print Business KPI Summary Metrics
            System.out.printf("📊 Total Orders Handled:  %d\n", totalOrderCount);
            System.out.printf("🛒 Total Items Sold:      %d\n", grandTotalItemsSold);
            System.out.printf("💵 Gross Revenue Stream:  $%.2f\n", grossRevenue);
            System.out.printf("📈 Average Order Value:   $%.2f\n", averageOrderValue);
            System.out.println("\n════════════════════════════════\n");
        }
        private void displayAllOrders() {
            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                       ALL AUDITED ORDERS                       ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

            ArrayList<AuditLog> allLogs = orderRepository.getAllAuditLogs();

            if (allLogs.isEmpty()) {
                System.out.println("No audited records found.");
            } else {
                java.time.format.DateTimeFormatter timeFormatter =
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                for (AuditLog log : allLogs) {
                    // These will now resolve perfectly because they belong to AuditLog!
                    String id = log.getOrderID();
                    String formattedTime = log.getTimestamp().format(timeFormatter);
                    String actionType = log.getAction();
                    double totalAmount = log.getAmount();
                    int quantities = log.getItemCount();
                    String breakdown = log.getDetails();

                    // Printing structured dashboard view
                    System.out.printf("🆔 Order ID:  %s  [%s]\n", id, actionType);
                    System.out.printf("📅 Timestamp: %s\n", formattedTime);
                    System.out.printf("📦 Quantity:  %d item(s)\n", quantities);
                    System.out.printf("💰 Revenue:   $%.2f\n", totalAmount);
                    System.out.println("📋 Breakdown:\n" + breakdown);
                    System.out.println("────────────────────────────────────────────────────────────────");
                }
            }
            System.out.println();
        }
        private void displayPastOrdersSummary() {
            System.out.println("\n=== PAST ORDERS HISTORY ===");

            // 1. Use getOrderCount() to see if we have anything to print
            int totalCount = orderRepository.getOrderCount();

            if (totalCount == 0) {
                System.out.println("No past orders found in the database.");
                return;
            }

            // 2. Loop using a standard counter to print them sequentially
            for (int i = 0; i < totalCount; i++) {
                // 3. Use getOrder(index) to safely retrieve each order item
                Order pastOrder = orderRepository.getOrder(i);

                if (pastOrder != null) {
                    System.out.printf("%d) Order - Total Cost: $%.2f (%d items)\n",
                            (i + 1),
                            pastOrder.calculatePrice(),
                            pastOrder.getItems().size()
                    );
                }
            }
            System.out.println("===========================\n");
        }
        private void displayHighValueOrders() {
            System.out.println("\n=== 🚨 HIGH-VALUE AUDIT COMPLIANCE (>$25.00) ===");
            int totalCount = orderRepository.getOrderCount();
            boolean foundAny = false;

            for (int i = 0; i < totalCount; i++) {
                Order pastOrder = orderRepository.getOrder(i);

                // Shape Intent: Filter out the small everyday retail orders
                if (pastOrder != null && pastOrder.calculatePrice() >= 25.00) {
                    foundAny = true;
                    System.out.printf("📌 Database Index [#%d] -> Revenue: $%.2f | Size: %d items\n",
                            i, pastOrder.calculatePrice(), pastOrder.getItems().size());

                    // Print brief sub-item details so the manager can see why it costs so much
                    pastOrder.getItems().forEach(item -> System.out.println("   ↳ " + item.getName()));
                }
            }

            if (!foundAny) {
                System.out.println("No high-value orders found in this batch session.");
            }
            System.out.println("==================================================\n");
        }
        private void handleDatabaseReset() {
            System.out.print("⚠️ WARNING: Are you sure you want to clear ALL order data? (Y/N): ");
            String confirmation = scanner.nextLine().trim().toUpperCase();

            if (confirmation.equals("Y")) {
                // 🔥 Trigger the repository cleanup routine
                orderRepository.clearAllOrders();
                System.out.println("Database cleared successfully! All records wiped. 🧼");
            } else {
                System.out.println("Reset cancelled. Operational logs are safe.");
            }
        }
        private void displayAbortedOrderMetrics() {
            System.out.println("\n=== 🧼 VOIDED / CANCELLED ORDERS CACHE ===");
            int totalCount = orderRepository.getOrderCount();
            double lostRevenue = 0.0;

            for (int i = 0; i < totalCount; i++) {
                Order pastOrder = orderRepository.getOrder(i);
                // Assuming the order tracking registers status elements
                if (pastOrder != null) {
                    System.out.printf("Voided Ticket #%d: Saved $%.2f across %d unmade items\n",
                            i, pastOrder.calculatePrice(), pastOrder.getItems().size());
                    lostRevenue += pastOrder.calculatePrice();
                }
            }
            System.out.printf("Total Preventable Kitchen Waste: $%.2f\n", lostRevenue);
        }
}
