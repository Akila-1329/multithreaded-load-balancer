package com.client;

import java.io.*;
import java.net.*;

/**
 * ClientSimulator class simulates a client making requests to the Load Balancer.
 */
public class ClientSimulator {
    private static final String LOAD_BALANCER_HOST = "localhost"; // Load Balancer address
    private static final int LOAD_BALANCER_PORT = 9000; // Load Balancer port

    public static void main(String[] args) {
        try (Socket socket = new Socket(LOAD_BALANCER_HOST, LOAD_BALANCER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send request to Load Balancer
            String request = "GET /data";
            System.out.println("Client: Sending request -> " + request);
            out.println(request);

            // Receive response from Load Balancer
            String response = in.readLine();
            System.out.println("Client: Received response <- " + response);

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
