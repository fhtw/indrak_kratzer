package webserver;

import java.io.IOException;
import java.net.*;

class Client extends Thread {

    private Socket socket;

    public Client(Socket incClient) {
        socket = incClient;
    }

    public void startClient() {
        start();
    }

    @Override
    public void run() {
        try {
            PluginHandler myPlugins = new PluginHandler();
            RequestHandler reqHandle = new RequestHandler(socket);
            ResponseHandler respHandle = new ResponseHandler(socket);
            
            myPlugins.init();
            if (reqHandle.processRequest(myPlugins.getPluginList())) {
                if (reqHandle.getPluginCheck()) {
                    if (myPlugins.runPlugin(reqHandle.getPlugin(), reqHandle.getAttributeList(), reqHandle.getUrl(), socket)) {
                        System.out.println("Requested Plugin started successfully");
                    } else {
                        System.out.println("Could not load plugin");
                    }
                } else {
                    respHandle.runDefault(reqHandle.getUrl());
                }
            } else { 
                System.out.println("Could not process request");
            }
            
        } catch (IOException ex) {
            System.out.println("IOException: " + ex);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }



    }
}