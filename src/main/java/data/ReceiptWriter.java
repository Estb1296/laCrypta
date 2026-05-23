package data;

import model.Order;
import model.MenuItem;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.lang.String.format;

public class ReceiptWriter {
    private static final String RECEIPTS_FOLDER = "receipts/";

    /**
     * Generates and saves a receipt file for a completed order
     * @param order The completed order
     * @return The filename of the saved receipt
     * @throws IOException If file writing fails
     */
    public String saveReceipt(Order order) throws IOException {
        // Generate timestamp filename
        String filename = generateFilename();
        String filepath = RECEIPTS_FOLDER + filename;

        // Generate receipt content
        String receiptContent = generateReceiptContent(order);

        // Write to file
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(receiptContent);
        }

        return filename;
    }

    /**
     * Generates a unique filename with timestamp
     * Format: receipt_YYYY_MM_DD_HH_MM_SS_mmm.txt
     */
    private String generateFilename() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS");
        return "receipt_" + now.format(formatter) + ".txt";
    }

    /**
     * Generates the full receipt content from an order
     */
    private String generateReceiptContent(Order order) {
        StringBuilder receipt = new StringBuilder();

        // Header
        receipt.append("╔════════════════════════════════════╗\n");
        receipt.append("║         SANDWICH SHOP RECEIPT       ║\n");
        receipt.append("╚════════════════════════════════════╝\n\n");

        // Timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        receipt.append("Date & Time: ").append(now.format(formatter)).append("\n");
        receipt.append("═".repeat(36)).append("\n\n");

        // Order items
        ArrayList<MenuItem> items = order.getItems();
        if (items.isEmpty()) {
            receipt.append("No items ordered.\n");
        } else {
            receipt.append("ORDER DETAILS:\n");
            receipt.append("─".repeat(36)).append("\n\n");

            // Group by category for cleaner receipt
            displayItemsByCategory(receipt, items, "Sandwich");
            displayItemsByCategory(receipt, items, "Drink");
            displayItemsByCategory(receipt, items, "Chips");
        }

        // Footer with totals
        receipt.append("\n").append("═".repeat(36)).append("\n");
        receipt.append(format("SUBTOTAL: $%.2f\n", order.calculatePrice()));
        receipt.append("TAX (0%%): $0.00\n");
        receipt.append(format("TOTAL: $%.2f\n", order.calculatePrice()));
        receipt.append("═".repeat(36)).append("\n\n");
        receipt.append("Thank you for your order!\n");
        receipt.append("Please come again soon!\n");
        return receipt.toString();
    }

    /**
     * Helper method to display items by category in the receipt
     */
    private void displayItemsByCategory(StringBuilder receipt, ArrayList<MenuItem> items, String category) {
        ArrayList<MenuItem> categoryItems = new ArrayList<>();
        for (MenuItem item : items) {
            if (item.getCategory().equals(category)) {
                categoryItems.add(item);
            }
        }

        if (!categoryItems.isEmpty()) {
            receipt.append(category.toUpperCase()).append("S:\n");
            int itemNum = 1;
            for (MenuItem item : categoryItems) {
                receipt.append(itemNum).append(") ").append(item.toReceiptLine()).append("\n");
                receipt.append("   ").append(item.getDescription()).append("\n");
                itemNum++;
            }
            receipt.append("\n");
        }
    }
}
