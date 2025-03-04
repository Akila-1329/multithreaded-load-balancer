package com.loadbalancer;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * LoadBalancer class distributes client requests to backend servers using Round-Robin.
 */
public class LoadBalancer {
    private static final int PORT = 9000;
    private static List<String> backendServers = new ArrayList<>();
    private static int roundRobinIndex = 0;

    public static void main(String[] args) {
        loadServerConfig();

        System.out.println("Starting Load Balancer on port " + PORT + "...");
        Logger.log("Load Balancer started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Load Balancer is running and waiting for client connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                Logger.log("New client connected: " + clientSocket.getInetAddress());

                new Thread(() -> handleClientRequest(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error in Load Balancer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadServerConfig() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("config/servers.properties")) {
            properties.load(input);

            // Load all servers dynamically
            for (String key : properties.stringPropertyNames()) {
                String server = properties.getProperty(key);
                backendServers.add(server);
            }

            Logger.log("Loaded backend servers from properties file: " + backendServers);
        } catch (IOException e) {
            Logger.log("Error loading backend server configuration: " + e.getMessage());
        }
    }

    public static synchronized String getNextServer() {
        String server = backendServers.get(roundRobinIndex);
        roundRobinIndex = (roundRobinIndex + 1) % backendServers.size();
        return server;
    }

    private static void handleClientRequest(Socket clientSocket) {
        try (
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter clientOutput = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String clientRequest = clientInput.readLine();
            System.out.println("Received request from client: " + clientRequest);

            String backendServer = getNextServer();
            String[] serverDetails = backendServer.split(":");
            String serverHost = serverDetails[0];
            int serverPort = Integer.parseInt(serverDetails[1]);

            try (
                    Socket backendSocket = new Socket(serverHost, serverPort);
                    PrintWriter backendOutput = new PrintWriter(backendSocket.getOutputStream(), true);
                    BufferedReader backendInput = new BufferedReader(new InputStreamReader(backendSocket.getInputStream()))
            ) {
                backendOutput.println(clientRequest);
                String backendResponse = backendInput.readLine();
                clientOutput.println(backendResponse);
                System.out.println("Forwarded request to " + backendServer + " | Response: " + backendResponse);
            }
        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
        }
    }
}
