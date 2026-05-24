package data;

import model.Order;
import java.io.IOException;
import java.util.ArrayList;

public class OrderRepository {
    private final ArrayList<Order> orders;
    private final ReceiptWriter receiptWriter;
    private final AuditLogger auditLogger;
    private final ArrayList<AuditLog> auditLogs;

    /**
     * Constructor - initializes the repository, receipt writer, and audit logger
     */
    public OrderRepository() {
        this.orders = new ArrayList<>();
        this.auditLogs = new ArrayList<>();
        this.receiptWriter = new ReceiptWriter();
        this.auditLogger = new AuditLogger();
    }

    /**
     * Complete an order: save receipt, log audit, store order
     */
    public String completeOrder(Order order) throws IOException {
        // Generate and save receipt
        String receiptFilename = receiptWriter.saveReceipt(order);

        // Log the order completion
        String orderID = generateOrderID();
        AuditLog auditLog = new AuditLog(
                orderID,
                "ORDER_COMPLETED",
                order.calculatePrice(),
                order.getItems().size(),
                "Receipt: " + receiptFilename
        );
        auditLogs.add(auditLog);
        auditLogger.logAudit(auditLog);

        // Store the order in history
        orders.add(order);

        return receiptFilename;
    }

    /**
     * Log order initiation
     */
    public void logOrderStart() throws IOException {
        AuditLog auditLog = new AuditLog(
                generateOrderID(),
                "ORDER_STARTED",
                0,
                0,
                "New order initiated"
        );
        auditLogs.add(auditLog);

        auditLogger.logAudit(auditLog);
    }

    /**
     * Get all orders
     */
    public ArrayList<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
    public ArrayList<AuditLog> getAllAuditLogs() {
        return new ArrayList<>(auditLogs); // Returns a safe copy of the logs list
    }

    /**
     * Get total order count
     */
    public int getOrderCount() {
        return orders.size();
    }

    /**
     * Get specific order by index
     */
    public Order getOrder(int index) {
        if (index >= 0 && index < orders.size()) {
            return orders.get(index);
        }
        return null;
    }

    /**
     * Clear all orders
     */
    public void clearAllOrders() {
        orders.clear();
    }

    /**
     * Generate unique order ID
     */
    private String generateOrderID() {
        return "ORD-" + System.currentTimeMillis();
    }
    public void logCancelledOrder(Order order) {
        AuditLog cancelLog = new AuditLog(
                "ORD-" + System.currentTimeMillis(),
                "ORDER_CANCELLED", // 🔥 Explicitly tags it as a cancellation!
                order.calculatePrice(),
                order.getItems().size(),
                "User explicitly aborted cart with items inside."
        );
        try {
            auditLogger.logAudit(cancelLog);
        } catch (IOException e) {
            throw new RuntimeException("The order wasn't properly tracked.");
        }
        auditLogs.add(cancelLog); // Saves it to cache for Option 6 in admin panel!
    }
}