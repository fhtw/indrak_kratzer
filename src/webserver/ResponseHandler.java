package webserver;

import java.io.*;
import java.net.*;

public class ResponseHandler {

    private Socket socket;
    private PrintStream servOut;

    public ResponseHandler() {
    }

    public ResponseHandler(Socket socket) {
        this.socket = socket;
    }

    public void startStream() throws IOException {
        this.servOut = new PrintStream(new BufferedOutputStream(
                this.socket.getOutputStream()));
    }

    public void closeStream() {
        this.servOut.close();
    }

    public void printHeader(String incMimeType) {
        servOut.print("HTTP/1.0 200 OK\r\n"
                + "Content-type: " + incMimeType + "\r\n\r\n");
    }

    public void printFileNotFound() {
        servOut.print("HTTP/1.0 404 Not Found\r\n"
                + "Content-type: text/html\r\n\r\n"
                + "<html><head></head><body>File not found</body></html>\n");
    }

    public void printText(String incText) {
        servOut.print(incText);
    }

    public void openFile(String incFile, String incMimeType) {
        try {
            // Datei wird geöffnet
            InputStream f = new FileInputStream(incFile);
            printHeader(incMimeType);

            // Dateinhalt wird dem Client übermittelt
            byte[] a = new byte[4096];
            int i;
            while ((i = f.read(a)) > 0) {
                servOut.write(a, 0, i);
            }
        } catch (IOException e) {
            printFileNotFound();
            servOut.close();
            System.out.println(e);
        }
    }

    public void runDefault(String url) throws IOException {
        if (url.startsWith("GetTemperature/"))
        {
            System.out.println("TO DO!!");
        } else {
            startStream();
            openFile("index.html", "text/html");
            closeStream();
        }
    }
}