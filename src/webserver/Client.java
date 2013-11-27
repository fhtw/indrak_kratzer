package webserver;

import java.net.*;

class Client extends Thread {

    //Socket des Clients
    private Socket socket;

    // Konstruktor der Klasse ClientLogic startet Thread für Client
    public Client(Socket incClient) {
        socket = incClient;
        start();
    }

    // Url und Request wird aufbereitet
    // sobald Datenübertragung durchgeführt wurde, wird die Verbindung geschlossen
    public void run() {
        RequestHandler reqHandle = new RequestHandler(socket);
        
        //to-do: request Klasse anpassen
        //ResponseHandler httpResp = new ResponseHandler(socket, urlHandle.getUrl(), urlHandle.getMimeType(), reqHandle.getMethod(), reqHandle.getStandard(), reqHandle.getBody());
        
        
        
    }
}