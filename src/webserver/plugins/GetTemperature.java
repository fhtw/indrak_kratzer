package webserver.plugins;

import webserver.PluginControl;

public class GetTemperature implements PluginControl{
    @Override
    public void init() {
        System.out.println("Init GetTemperature");
    }
    
    @Override
    public void start() {
        System.out.println("Starte GetTemperature");
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
 
    
}