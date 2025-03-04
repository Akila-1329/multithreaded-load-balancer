# Load Balancer Project (Multi-threaded)

## 📌 Overview
This project implements a **multi-threaded Load Balancer** that distributes incoming client requests to multiple backend servers in a **round-robin** manner.

### **📌 How It Works**
1. Clients send **requests** to the Load Balancer (`localhost:9000`).
2. The Load Balancer **queues requests** and forwards them to **backend servers**.
3. Backend servers **process requests** and send **responses back** to the clients.
4. The system **logs all activities** in `logs/load_balancer.log`.

---

## 🚀 **Setup & Run the Project**
### **1️⃣ Compile All Java Files**
Run the following command from the root of the project:

```sh
javac src/main/java/com/loadbalancer/*.java src/main/java/com/backend/*.java src/main/java/com/client/*.java

2️⃣ Start Backend Servers
The backend servers simulate real web servers handling requests.

You need to start at least two backend servers on different ports:

sh
Copy
Edit
java -cp src/main/java com.backend.BackendServer 8001
java -cp src/main/java com.backend.BackendServer 8002
Each backend server will now be waiting for requests.

3️⃣ Start Load Balancer
The Load Balancer listens for client requests on port 9000.

sh
Copy
Edit
java -cp src/main/java com.loadbalancer.LoadBalancer
You should see output indicating that the Load Balancer is running and waiting for client connections.

4️⃣ Run Multiple Clients
Now, simulate multiple clients sending requests to the Load Balancer.

sh
Copy
Edit
java -cp src/main/java com.client.ClientSimulator
java -cp src/main/java com.client.ClientSimulator
Each client will send a request to the Load Balancer, which will forward it to a backend server and return a response.

🎯 How to Add More Backend Servers
Modify the config/servers.properties file:

properties
Copy
Edit
server1=localhost:8001
server2=localhost:8002
server3=localhost:8003  # Add a new backend server
Then start another backend server:

sh
Copy
Edit
java -cp src/main/java com.backend.BackendServer 8003
Restart the Load Balancer to apply the changes.

📜 Expected Output
🔹 Load Balancer (Terminal Output)
yaml
Copy
Edit
[2024-03-04 12:45:30] Loaded backend servers from properties file: [localhost:8001, localhost:8002]
[2024-03-04 12:45:35] Load Balancer started on port 9000
[2024-03-04 12:46:00] New client connected: /127.0.0.1
[2024-03-04 12:46:02] Worker: Forwarded request to localhost:8001 | Response: Response from server 8001
🔹 Backend Server (Example Output)
vbnet
Copy
Edit
Backend Server started on port 8001
Handling request from Load Balancer...
Received request: GET /data
Sent response: Response from server 8001
🔹 Client Output
vbnet
Copy
Edit
Client: Sending request -> GET /data
Client: Received response <- Response from server 8001
🔍 Troubleshooting
Port Already in Use?
If a port is already in use, stop the process using it:

sh
Copy
Edit
lsof -i :9000   # Check if port 9000 is in use
kill -9 <PID>   # Replace <PID> with the process ID to stop it
Connection Refused?
Ensure:

The Load Balancer is running before starting clients.
Backend Servers are running before starting the Load Balancer.
Requests Not Forwarding?
Check logs/load_balancer.log for errors.
Restart the Load Balancer to refresh server configurations.

🛠 Project Structure
bash
Copy
Edit
load-balancer-project/
│── src/
│   ├── main/java/com/loadbalancer/
│   │   ├── LoadBalancer.java        # Main load balancer class (multi-threaded)
│   │   ├── ClientHandler.java       # Handles client connections in threads
│   │   ├── RequestQueue.java        # Queue for managing incoming requests
│   │   ├── WorkerThread.java        # Forwards requests to backend servers
│   │   ├── Logger.java              # Logging utility to track request distribution
│   ├── main/java/com/backend/
│   │   ├── BackendServer.java       # Simulated backend server
│   ├── main/java/com/client/
│   │   ├── ClientSimulator.java     # Simulates multiple clients sending requests
│── config/
│   ├── servers.properties           # Config file for backend server details (IP, Ports)
│── logs/                             # Stores logs for debugging and analysis
│── README.md                         # Project documentation
│── pom.xml (If using Maven)          # Dependencies and build settings (optional)