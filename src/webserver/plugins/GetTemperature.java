package webserver.plugins;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import webserver.PluginControl;

public class GetTemperature implements PluginControl{
    @Override
    public void init() {
        System.out.println("Found Plugin: GetTemperature");
    }
    
    @Override
    public void start() {
        System.out.println("Starte GetTemperature");
    }
    
    @Override
    public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
        if (incAttributes.isEmpty())
        {
            System.out.println("Plugin: no Attributes");
        }
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
 
    
}