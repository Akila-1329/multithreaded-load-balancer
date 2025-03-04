package com.backend;

import java.io.*;
import java.net.*;

/**
 * BackendServer class simulates a simple backend server.
 * It listens for requests from the Load Balancer and responds with a message.
 */
public class BackendServer {
    private int port;

    public BackendServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Backend Server started on port " + port);

            while (true) {
                // Accept incoming connection from Load Balancer
                Socket clientSocket = serverSocket.accept();
                System.out.println("Handling request from Load Balancer...");

                // Handle request in a separate thread
                new Thread(() -> handleRequest(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error in Backend Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles incoming requests from the Load Balancer.
     */
    private void handleRequest(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Read request from Load Balancer
            String request = in.readLine();
            System.out.println("Received request: " + request);

            // Send response back to Load Balancer
            String response = "Response from server " + port;
            out.println(response);

            System.out.println("Sent response: " + response);
        } catch (IOException e) {
            System.err.println("Error processing request: " + e.getMessage());
        }
    }

    /**
     * Main method to start the backend server with a given port.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java com.backend.BackendServer <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        BackendServer server = new BackendServer(port);
        server.start();
    }
}
