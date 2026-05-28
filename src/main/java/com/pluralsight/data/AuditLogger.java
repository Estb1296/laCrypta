package com.pluralsight.data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class AuditLogger {
    private static final String LOGS_FOLDER = "logs/";
    /**
     * Logs audit information to daily audit file
     */

    public void logAudit(AuditLog auditLog) throws IOException {
        String folderPath = createAndGetFolderPath();
        LocalDate today = LocalDate.now();
        String filename = generateFilename(today);
        String filepath = folderPath + filename;

        // Append to audit file
        try (FileWriter writer = new FileWriter(filepath, true)) {
            writer.write(auditLog.toString());
            writer.write("\n");
        }
    }

    /**
     * Creates the folder structure for today's audit logs
     * Creates: logs/2026-01-15/ (if it doesn't exist)
     * @return The folder path
     */
    private String createAndGetFolderPath() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String folderPath = LOGS_FOLDER + today.format(formatter) + "/";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                System.out.println("Warning: Could not create logs folder: " + folderPath);
            }
        }
        return folderPath;
    }

    /**
     * Generates filename for today's audit log
     * Format: audit-yyyy-MM-dd.txt
     */
    private String generateFilename(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "audit-" + date.format(formatter) + ".txt";
    }
}
