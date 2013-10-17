package webserver;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.net.JarURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginHandler {

    private ArrayList<File> PluginFiles;
    private String defaultPluginsLocation = "C:/Users/iNDi/Documents/NetBeansProjects2/Server/src/ServerIndrakKratzer/plugins/";
    private String pluginsLocation = System.getProperty("ServerIndrakKratzer.pluginsLocation", defaultPluginsLocation);

    public void boot() throws Exception {
        registerPlugins();
        run();
    }

    private void run() {

        ServiceLoader<PluginControl> plugins = ServiceLoader.load(PluginControl.class);
        System.out.println("Starte Plugins");
        for (PluginControl plugin : plugins) {
            plugin.start();
        }
    }

    void registerPlugins() throws MalformedURLException {
        File pluginsFolder = new File(pluginsLocation);

        Path pluginPath = FileSystems.getDefault().getPath("plugins");
        File f = pluginPath.toFile();
        this.PluginFiles = new ArrayList(Arrays.asList(f.listFiles()));


        /*File[] plugins;
         plugins = pluginsFolder.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(File dir, String name) {
         return name.endsWith(".jar");
         }
         });*/
        URL[] pluginUrls = new URL[this.PluginFiles.size()];
        int i = 0;

        for (File pFile : this.PluginFiles) {
            if (pFile.toString().endsWith(".jar")) {
                System.out.println("Found plugin: " + pFile);
                //pluginUrls[i] = pFile.toURI().toURL();
                String jarURL = "jar:" + pFile.toURI().toURL() + "!/";
                pluginUrls[i] = new URL(jarURL);
                i++;
            }
        }
        URLClassLoader ucl = new URLClassLoader(pluginUrls, this.getClass().getClassLoader());

    }

    void addToClassPathOf(URLClassLoader urlClassLoader, URL... urls) {
        try {
            Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrlMethod.setAccessible(true);
            for (URL url : urls) {
                try {
                    /*JarURLConnection uc = (JarURLConnection)url.openConnection();
                     Attributes attr = uc.getMainAttributes();
                     String bla = attr.getValue(Attributes.Name.MAIN_CLASS);*/

                    addUrlMethod.invoke(urlClassLoader, url);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    System.err.println(String.format("could not add: %s to classpath", url));
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
