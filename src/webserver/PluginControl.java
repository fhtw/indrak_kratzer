package webserver;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface PluginControl {
    final Map<String, List<String>> params = new HashMap<String, List<String>>();
 
    void init();
    void start();
    void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket);
    String getName();
 
}