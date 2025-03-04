package com.loadbalancer;

import java.net.*;
import java.util.concurrent.*;

/**
 * RequestQueue is a thread-safe queue that stores client requests before they are forwarded to backend servers.
 */
public class RequestQueue {
    private BlockingQueue<ClientRequest> queue = new LinkedBlockingQueue<>();

    /**
     * Adds a client request to the queue.
     */
    public void addRequest(String request, Socket clientSocket) {
        queue.add(new ClientRequest(request, clientSocket));
    }

    /**
     * Retrieves and removes the next request from the queue.
     */
    public ClientRequest getRequest() throws InterruptedException {
        return queue.take(); // Waits if queue is empty
    }
}

/**
 * Helper class to store a client request and its associated socket.
 */
class ClientRequest {
    public String request;
    public Socket clientSocket;

    public ClientRequest(String request, Socket clientSocket) {
        this.request = request;
        this.clientSocket = clientSocket;
    }
}
