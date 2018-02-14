//
// Multithreaded Java WebServer
// (C) 2001 Anders Gidenstam
// (based on a lab in Computer Networking: ..)
//

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public final class WebServer {
    public static void main(String argv[]) throws Exception {
        // Set port number
        int port = 0;

        // Establish the listening socket
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Port number is: " + serverSocket.getLocalPort());


        // Wait for and process HTTP service requests
        while (true) {
            // Wait for TCP connection
            Socket requestSocket = serverSocket.accept();
            requestSocket.setSoLinger(true, 5);

            // Create an object to handle the request
            HttpRequest request = new HttpRequest(requestSocket);

            //request.run()

            // Create a new thread for the request
            Thread thread = new Thread(request);

            // Start the thread
            thread.start();
        }
    }
}



final class HttpRequest implements Runnable {

    final static class HTTP_METHOD {
        final static String GET = "GET";
        final static String HEAD = "HEAD";
        final static String POST = "POST";
        final static String PUT = "PUT";
        final static String DELETE = "DELETE";
        final static String LINK = "LINK";
        final static String UNLINK = "UNLINK";
    }

    final static String CRLF = "\r\n";
    Socket socket;

    // Constructor
    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    // Implements the run() method of the Runnable interface
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Process a HTTP request
    private void processRequest() throws Exception {
        // Get the input and output streams of the socket.
        InputStream ins = socket.getInputStream();
        DataOutputStream outs = new DataOutputStream(socket.getOutputStream());

        // Set up input stream filters
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        
        // Get the request line of the HTTP request
        String requestLine = br.readLine();

        // Display the request line
        System.out.println();
        System.out.println("Request:");
        System.out.println("  " + requestLine);
        //Handles the response
        String result = RequestManager.handleRequest(requestLine);
        System.out.println(result);

        // Close streams and sockets
        outs.close();
        br.close();
        socket.close();
    }

    private static void sendBytes(FileInputStream fins,
                                  OutputStream outs) throws Exception {
        // Coopy buffer
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while ((bytes = fins.read(buffer)) != -1) {
            outs.write(buffer, 0, bytes);
        }
    }


}

