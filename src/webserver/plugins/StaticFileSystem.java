package webserver.plugins;

import webserver.PluginControl;

public class StaticFileSystem implements PluginControl{
    @Override
    public void start() {
        System.out.println("Starte StaticFileSystem");
    }
}