package webserver;


import java.io.*;
import java.net.*;
import java.util.Date;

public class ResponseHandler {

    ResponseHandler(Socket socket, String filename, String mimetype, String method, String standard, String body) {
        System.out.println(new Date() + " | Method: " + method + " | Standard: " + standard 
                         + " | File: " + filename + " | mimeType: " + mimetype + "\n" + body + "\n\n");
        try {
            PrintStream servOut = new PrintStream(new BufferedOutputStream(
                    socket.getOutputStream()));
            // HTTP error 404 File Not Found via try/catch FileNotFoundException
            try {

                if (method.equalsIgnoreCase("GET")) {
                    //GET
                } else if (method.equalsIgnoreCase("POST")) {
                    //POST
                } else {
                    System.out.println("Warning: could not identify http request method!");
                }

                // Datei wird geöffnet
                InputStream f = new FileInputStream(filename);

                servOut.print("HTTP/1.0 200 OK\r\n"
                        + "Content-type: " + mimetype + "\r\n\r\n");

                // Dateinhalt wird dem Client übermittelt
                byte[] a = new byte[4096];
                int i;
                while ((i = f.read(a)) > 0) {
                    servOut.write(a, 0, i);
                }
                servOut.close();
            } catch (FileNotFoundException e) {
                servOut.println("HTTP/1.0 404 Not Found\r\n"
                        + "Content-type: text/html\r\n\r\n"
                        + "<html><head></head><body>" + filename + " not found</body></html>\n");
                servOut.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}