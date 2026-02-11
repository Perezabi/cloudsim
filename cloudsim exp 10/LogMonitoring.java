// Experiment 10: Log Monitoring with Incident Management

import java.io.*;
import java.util.*;

public class LogMonitoring {

    private static final String LOG_FILE = "system_logs.txt";

    public static void main(String[] args) {

        try {
            System.out.println("Log Monitoring System Started\n");

            // Step 1: Generate Logs
            generateLogs();

            // Step 2: Monitor Logs
            monitorLogs();

            System.out.println("\nLog Monitoring System Completed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Generate sample system logs
    private static void generateLogs() throws IOException {

        FileWriter writer = new FileWriter(LOG_FILE);

        writer.write("INFO: User login successful\n");
        writer.write("INFO: File uploaded\n");
        writer.write("WARNING: Login failed\n");
        writer.write("WARNING: Login failed\n");
        writer.write("WARNING: Login failed\n");
        writer.write("ERROR: Unauthorized access attempt\n");

        writer.close();
        System.out.println("Logs generated");
    }

    // Monitor logs and detect incidents
    private static void monitorLogs() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE));
        String line;
        int failedLogins = 0;

        System.out.println("Monitoring logs...");

        while ((line = reader.readLine()) != null) {

            if (line.contains("Login failed")) {
                failedLogins++;
            }

            if (line.contains("Unauthorized access")) {
                System.out.println("INCIDENT ALERT: Unauthorized access detected!");
            }
        }

        reader.close();

        if (failedLogins >= 3) {
            System.out.println("INCIDENT ALERT: Possible brute-force attack detected!");
        }
    }
}
