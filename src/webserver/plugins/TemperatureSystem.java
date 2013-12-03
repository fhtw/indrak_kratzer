package webserver.plugins;

import java.io.IOException;
import java.sql.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import webserver.PluginControl;
import webserver.ResponseHandler;

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
        ResponseHandler respHandle = new ResponseHandler(socket);
        if (incAttributes.isEmpty()) {
            System.out.println("Plugin: no Attributes");
        } else {
            Set<String> keys = incAttributes.keySet();
            for (String key : keys) {
                if (key.equals("action")) {
                    String incStr = incAttributes.get(key).toString();
                    String action = incStr.substring(1, incStr.length() - 1);
                    try {
                        respHandle.startStream();
                    } catch (IOException ex) {
                        System.out.println("Could not start stream: " + ex);
                    }

                    switch (action) {
                        case "createDatabase":
                            try {
                                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                                String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=TemperatureSystemDB;user=JulianL;password=Indrak";
                                Connection db = DriverManager.getConnection(connectionUrl);
                                Statement stmt = db.createStatement();
                                for (int i = 0; i <= 10000; i++) {
                                    Random rand = new Random();
                                    int randomNum = rand.nextInt((i + 1) + i);
                                    String sql = "INSERT INTO TemperatureTable (Temperature, Date) "
                                            + "VALUES (" + randomNum + ", DateAdd(d, ROUND(DateDiff(d, '2010-01-01', '2013-12-31')"
                                            + "* RAND(CHECKSUM(NEWID())), 0),"
                                            + "DATEADD(second,CHECKSUM(NEWID())%48000, '2010-01-01')))";
                                    stmt.executeUpdate(sql);
                                }
                                respHandle.printHeader("text/html");
                                respHandle.printText("<html><head></head><body><h1>Database created.</h1></body></html>\n");
                            } catch (ClassNotFoundException ex) {
                                System.out.println("MS JDBC Driver not installed\n" + ex);
                            } catch (SQLException ex) {
                                System.out.println("Could not load database\n" + ex);
                            }
                            break;
                        case "clearDatabase":
                            try {
                                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                                String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=TemperatureSystemDB;user=JulianL;password=Indrak";
                                Connection db = DriverManager.getConnection(connectionUrl);
                                Statement stmt = db.createStatement();
                                String sql = "DELETE FROM TemperatureTable WHERE ID > 0;";
                                stmt.executeUpdate(sql);
                                respHandle.printHeader("text/html");
                                respHandle.printText("<html><head></head><body><h1>Database cleared.</h1></body></html>\n");
                            } catch (ClassNotFoundException ex) {
                                System.out.println("MS JDBC Driver not installed\n" + ex);
                            } catch (SQLException ex) {
                                System.out.println("Could not load database\n" + ex);
                            }
                            break;
                    }
                    respHandle.closeStream();
                }
            }
        }


    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}