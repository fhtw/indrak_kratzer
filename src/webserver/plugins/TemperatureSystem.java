package webserver.plugins;

import java.sql.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import webserver.PluginControl;

public class TemperatureSystem implements PluginControl {

    @Override
    public void init() {
        System.out.println("Found Plugin: TemperatureSystem");
    }

    @Override
    public void start() {
        System.out.println("Starte TemperatureSystem");
    }

    @Override
    public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
        if (incAttributes.isEmpty()) {
            System.out.println("Plugin: no Attributes");
        }  
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             String connectionUrl="jdbc:sqlserver://localhost:8080;DatabaseName=YourDBName;user=UserName;Password=YourPassword";
            Connection db = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException ex) {
            System.out.println("MS JDBC Driver not installed\n" + ex);
        } catch (SQLException ex) {
            System.out.println("Could not load database\n" + ex);
        }

    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}