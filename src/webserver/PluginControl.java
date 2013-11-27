package webserver;

public interface PluginControl {
    void init();
    void start();
    String getName();
}