package com.pluralsight.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLog {
    private final String orderID;
    private final LocalDateTime timestamp;
    private final String action;  // "ORDER_CREATED", "CHECKOUT", "RECEIPT_SAVED"
    private final double amount;
    private final int itemCount;
    private final String details;

    public AuditLog(String orderID, String action, double amount,
                    int itemCount, String details) {
        this.orderID = orderID;
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.amount = amount;
        this.itemCount = itemCount;
        this.details = details;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] ORDER: %s | ACTION: %s | AMOUNT: $%.2f | ITEMS: %d | DETAILS: %s",
                timestamp.format(formatter), orderID, action, amount, itemCount, details);
    }

    // Getters
    public String getOrderID() {
        return orderID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }

    public double getAmount() {
        return amount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getDetails() {
        return details;
    }
}
