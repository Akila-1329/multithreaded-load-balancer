package com.loadbalancer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger class for recording request distribution and errors.
 */
public class Logger {
    private static final String LOG_FILE_PATH = "logs/load_balancer.log";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        // Create logs directory if it doesn't exist
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }

    /**
     * Logs a message to the console and a log file.
     */
    public static synchronized void log(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = "[" + timestamp + "] " + message;

        // Print to console
        System.out.println(logMessage);

        // Append to log file
        try (FileWriter fw = new FileWriter(LOG_FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(logMessage);
        } catch (IOException e) {
            System.err.println("Logger: Error writing to log file.");
        }
    }
}
