package webserver.plugins;

import webserver.PluginControl;

public class StaticFileSystem implements PluginControl{
    @Override
    public void init() {
        System.out.println("Init StaticFileSystem");
    }
    
    @Override
    public void start() {
        System.out.println("Starte StaticFileSystem");
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
 
    
}