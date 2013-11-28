package webserver;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    public void run() {
 
        PluginHandler myPlugins = new PluginHandler();
        List<String> availablePlugins = new ArrayList<String>();

        try {        
            myPlugins.init();
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        availablePlugins = myPlugins.getPluginList();
   
        RequestHandler reqHandle = new RequestHandler(socket, availablePlugins);
        if (reqHandle.getPluginCheck())
            myPlugins.runPlugin(reqHandle.getPlugin());
            
        
        //to-do: request Klasse anpassen
        //ResponseHandler httpResp = new ResponseHandler(socket, urlHandle.getUrl(), urlHandle.getMimeType(), reqHandle.getMethod(), reqHandle.getStandard(), reqHandle.getBody());

        
    }
}