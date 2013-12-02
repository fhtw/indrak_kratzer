package webserver;

import java.net.Socket;
import java.util.List;
import java.util.Map;

public interface PluginControl {
    void init();
    void start();
    void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket);
    String getName();
}