package com.loadbalancer;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * WorkerThread fetches client requests from the queue and forwards them to backend servers.
 */
public class WorkerThread implements Runnable {
    private RequestQueue requestQueue;
    private List<String> backendServers;

    public WorkerThread(RequestQueue queue, List<String> servers) {
        this.requestQueue = queue;
        this.backendServers = servers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Fetch the next request from the queue
                ClientRequest clientRequest = requestQueue.getRequest();

                // Get the next backend server (Round-Robin handled by LoadBalancer)
                String backendServer = LoadBalancer.getNextServer();
                String[] serverDetails = backendServer.split(":");
                String serverHost = serverDetails[0];
                int serverPort = Integer.parseInt(serverDetails[1]);

                // Forward request to backend server
                try (
                        Socket backendSocket = new Socket(serverHost, serverPort);
                        PrintWriter backendOut = new PrintWriter(backendSocket.getOutputStream(), true);
                        BufferedReader backendIn = new BufferedReader(new InputStreamReader(backendSocket.getInputStream()));
                        PrintWriter clientOut = new PrintWriter(clientRequest.clientSocket.getOutputStream(), true)
                ) {
                    backendOut.println(clientRequest.request); // Send request to backend
                    String backendResponse = backendIn.readLine(); // Receive response

                    // Send the response back to the client
                    clientOut.println(backendResponse);
                    System.out.println("Worker: Forwarded request to " + backendServer + " | Response: " + backendResponse);
                    Logger.log("Worker: Forwarded request to " + backendServer + " | Response: " + backendResponse);

                } catch (IOException e) {
                    System.err.println("Worker: Error forwarding request: " + e.getMessage());
                    Logger.log("Worker: Error forwarding request: " + e.getMessage());
                }
            } catch (InterruptedException e) {
                System.err.println("Worker: Error retrieving request from queue.");
                Logger.log("Worker: Error retrieving request from queue: " + e.getMessage());
            }
        }
    }
}
