package webserver;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceLoader;

public class PluginHandler {
    private ServiceLoader<PluginControl> plugins;
    private Iterator<PluginControl> currentPlugin;
    

    public void boot() throws Exception {
        bootPlugins();
    }

    void bootPlugins() throws MalformedURLException {
        File loc = new File("plugins");

        File[] flist = loc.listFiles(new FileFilter() {
            public boolean accept(File file) {return file.getPath().toLowerCase().endsWith(".jar");}
        });
        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++)
            urls[i] = flist[i].toURI().toURL();
 
            URLClassLoader ucl = new URLClassLoader(urls);
            plugins = ServiceLoader.load(PluginControl.class, ucl);
            currentPlugin = plugins.iterator();
            while (currentPlugin.hasNext()){
                currentPlugin.next().start();
            }
        
    }
}