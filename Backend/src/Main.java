import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import Com.Hlophe.Connection.CreateDatabase;
import Com.Hlophe.OrphanageHome.BookVisit;
import Com.Hlophe.OrphanageHome.ChildrenAvailable;
import Com.Hlophe.OrphanageHome.Donations;
import Com.Hlophe.Authentication.*;

public class Main {
    private static final int PORT = 8080;
    private static final String BASE_PATH = "/OrphanageHome/api";
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    
    public static void main(String[] args) {
        try {
        	CreateDatabase _CreateDatabase = new CreateDatabase();
        	_CreateDatabase.writeQueries();
        	
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Running");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
    
    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                
                String requestLine = in.readLine();
                if (requestLine == null || requestLine.isEmpty()) {
                    clientSocket.close();
                    return;
                }
                
                String[] requestParts = requestLine.split(" ");
                if (requestParts.length < 3) {
                    clientSocket.close();
                    return;
                }
                
                String method = requestParts[0];
                String path = requestParts[1];
                
                Map<String, String> headers = new HashMap<>();
                String headerLine;
                while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                    int colonIndex = headerLine.indexOf(":");
                    if (colonIndex > 0) {
                        String key = headerLine.substring(0, colonIndex).trim();
                        String value = headerLine.substring(colonIndex + 1).trim();
                        headers.put(key, value);
                    }
                }
                
                StringBuilder bodyBuilder = new StringBuilder();
                if (headers.containsKey("Content-Length")) {
                    int contentLength = Integer.parseInt(headers.get("Content-Length"));
                    for (int i = 0; i < contentLength; i++) {
                        bodyBuilder.append((char) in.read());
                    }
                }
                
                String response = handleRequest(method, path, headers, bodyBuilder.toString());
                
                out.print(response);
                out.flush();
                
                in.close();
                out.close();
                clientSocket.close();
                
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }
        
        private String handleRequest(String method, String path, Map<String, String> headers, String body) {
            if (method.equals("OPTIONS")) {
                return createCorsResponse(200, "OK", "");
            }
            
            if (!path.startsWith(BASE_PATH)) {
                return createResponse(404, "Not Found", "{\"error\":\"API endpoint not found\"}", true);
            }
            
            String endpoint = path.substring(BASE_PATH.length());
            
            try {
                switch (method) {
                    case "GET":
                        if (endpoint.equals("/children")) {
                            String childrenJson = ChildrenAvailable.handleGetRequest();
                            return createResponse(200, "OK", childrenJson, true);
                        }
                        break;
                        
                    case "POST":
						if (endpoint.equals("/auth/login")) {
							String loginResponse = AuthController.login(body);
							return createResponse(200, "OK", loginResponse, true);
							}
                        else if (endpoint.equals("/donations")) {
                            String donationResponse = Donations.handlePostRequest(body);
                            int statusCode = donationResponse.contains("\"success\":true") ? 201 : 400;
                            return createResponse(statusCode, 
                                statusCode == 201 ? "Created" : "Bad Request", 
                                donationResponse, true);
                        } 
                        else if (endpoint.equals("/bookvisit")) {
                            String visitResponse = BookVisit.handlePostRequest(body);
                            int statusCode = visitResponse.contains("\"success\":true") ? 201 : 400;
                            return createResponse(statusCode, statusCode == 201 ? "Created" : "Bad Request", visitResponse, true);
                        }
                        break;
                }
                
                return createResponse(404, "Not Found", "{\"error\":\"Endpoint not found\"}", true);
                
            } 
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        
        private String createResponse(int statusCode, String statusMessage, String body, boolean isJson) {
            StringBuilder response = new StringBuilder();
            response.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");
            response.append("Content-Type: ").append(isJson ? "application/json" : "text/plain").append("\r\n");
            response.append("Content-Length: ").append(body.length()).append("\r\n");
            response.append("Access-Control-Allow-Origin: *\r\n");
            response.append("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE\r\n");
            response.append("Access-Control-Allow-Headers: Content-Type, Accept, Origin, Authorization\r\n");
            response.append("Access-Control-Allow-Credentials: true\r\n");
            response.append("Access-Control-Max-Age: 3600\r\n");
            response.append("Connection: close\r\n");
            response.append("\r\n");
            response.append(body);
            return response.toString();
        }
        
        private String createCorsResponse(int statusCode, String statusMessage, String body) {
            StringBuilder response = new StringBuilder();
            response.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");
            response.append("Access-Control-Allow-Origin: *\r\n");
            response.append("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE\r\n");
            response.append("Access-Control-Allow-Headers: Content-Type, Accept, Origin, Authorization\r\n");
            response.append("Access-Control-Allow-Credentials: true\r\n");
            response.append("Access-Control-Max-Age: 3600\r\n");
            response.append("Content-Length: ").append(body.length()).append("\r\n");
            response.append("Connection: close\r\n");
            response.append("\r\n");
            response.append(body);
            return response.toString();
        }
    }
}