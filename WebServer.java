//
// Multithreaded Java WebServer
// (C) 2001 Anders Gidenstam
// (based on a lab in Computer Networking: ..)
//

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    // Constants
    //   Recognized HTTP methods
    final static class HTTP_METHOD {
        final static String GET = "GET";
        final static String HEAD = "HEAD";
        final static String POST = "POST";
    }



    enum HTTP_STATUS_CODE {
        OK, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR, NOT_IMPLEMENTED;

        public static String toString(HTTP_STATUS_CODE code) {
            switch (code) {
                case OK:
                    return "200 OK";
                case BAD_REQUEST:
                    return "400 Bad Request";
                case NOT_FOUND:
                    return "404 Not Found";
                case INTERNAL_SERVER_ERROR:
                    return "500 Internal Server Error";
                default:
                    return "501 Not Implemented";
            }
        }
    }

    final static int MIN_VALID_PARTS = 3;
    final static String HTTPVERSION = "HTTP/1.0";
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
        String result = handleRequest(requestLine);
        System.out.println(result);

        // Close streams and sockets
        outs.close();
        br.close();
        socket.close();
    }

    private static String handleRequest(String request) {
        String[] spaces = request.split(" ");

        //BAD REQUEST
        if (spaces.length < MIN_VALID_PARTS) {
            return badRequest();
        }

        String method = spaces[0];
        String argument = spaces[1];
        String version = spaces[2];
        switch (method) {
            case HTTP_METHOD.GET:
                return handleGet(argument, version);
            case HTTP_METHOD.HEAD:
                return handleHead(argument, version);
            case HTTP_METHOD.POST:
                return handlePost();
            default:
                return badRequest();
        }
    }

    private static String handleGet(String argument, String version) {
        return "";
    }

    private static String handleHead(String argument, String version) {
        return "";
    }

    private static String handlePost() {
        return (HTTPVERSION + " ") + HTTP_STATUS_CODE.toString(HTTP_STATUS_CODE.NOT_IMPLEMENTED) + "\n" +
                displayDate();
    }

    private static String displayDate() {
        // s z y
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE MMM d H:m:s z y");
        ZonedDateTime d = ZonedDateTime.now();
        String s = d.format(formatter);
        return "Date: " + s;
    }

    private static String badRequest() {
        StringBuilder b = new StringBuilder();
        b.append(HTTPVERSION + " ").append(HTTP_STATUS_CODE.toString(HTTP_STATUS_CODE.BAD_REQUEST)).append("\n");
        b.append(displayDate());
        return b.toString();
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

    private static String contentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".htm") ||
                fileName.toLowerCase().endsWith(".html")) {
            return "text/html";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.toLowerCase().endsWith(".jpg")) {
            return "image/jpeg";
        } else {
            return "application/octet-stream";
        }
    }
}

