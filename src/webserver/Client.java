package webserver;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    @Override
    public void run() {
 
        PluginHandler myPlugins = new PluginHandler();

        try {        
            myPlugins.init();
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        RequestHandler reqHandle = new RequestHandler(socket, myPlugins.getPluginList());
        if (reqHandle.getPluginCheck()) {
            if (myPlugins.runPlugin(reqHandle.getPlugin(), reqHandle.getAttributeList(), reqHandle.getUrl(), socket))
                System.out.println("Requested Plugin started successfully");
            else System.out.println("Could not load plugin");
        } else {
            ResponseHandler respHandle = new ResponseHandler(socket);
            try {
                respHandle.runDefault(reqHandle.getUrl());
            } catch (IOException ex) {
                System.out.println("IOException: " + ex);
            }
        }
        
    }
}