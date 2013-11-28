package webserver;


import java.util.*;
import java.io.*;
import java.net.*;

// Webserver wartet auf Clients, bei Verbindungsanfrage wird ein eigener Thread für diesen erzeugt
public class Server {

    private static ServerSocket serverSocket;
 

    public static void main(String[] args) throws IOException, Exception {
 
        /*
         if (args.length != 1) {
         System.out.println("Usage: java <port>");
         System.exit(-1);
         }
         int port = Integer.parseInt(args[0]);
         */
        int port = 8080;
        serverSocket = new ServerSocket(port);
        System.out.println("Server started at: " + new Date() + " on Port: " + port);
        
        while (true) {
            try {
                // Warte auf Client...
                Socket client = serverSocket.accept();
                // ...und erzeuge dann Thread für diesen
                new Client(client);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}