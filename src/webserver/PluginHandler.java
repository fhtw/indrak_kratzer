package webserver;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Vector;

public class PluginHandler {
    private ServiceLoader<PluginControl> plugins;
    private Iterator<PluginControl> currentPlugin;
    List<String> pluginList = new ArrayList<String>();

    public void init() throws Exception {
        initPlugins();
    }
    
    public List<String> getPluginList()
    {
        currentPlugin = plugins.iterator();
        while (currentPlugin.hasNext()){
            pluginList.add(currentPlugin.next().getClass().getSimpleName());
        }
        return pluginList;
    }
    
    public int runPlugin(String pluginRequest)
    {
        currentPlugin = plugins.iterator();
        while (currentPlugin.hasNext()){
            PluginControl plugin = currentPlugin.next();
            if (pluginRequest.equalsIgnoreCase(plugin.getClass().getSimpleName()))
            {
                plugin.start();
                return 1;
            }
        }
        return 0;
    }

    void initPlugins() throws MalformedURLException {
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
                currentPlugin.next().init();
            }
    }
}