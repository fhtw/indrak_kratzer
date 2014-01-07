package webserver.plugins;

import java.io.IOException;
import java.sql.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.ParserConfigurationException;
import webserver.PluginControl;
import webserver.ResponseHandler;

public class TemperatureSystem implements PluginControl {

    private void showStarterScreen(ResponseHandler respHandle) {
        System.out.println("Plugin: no Attributes");
        respHandle.printHeader("text/html");
        respHandle.printStyle(1);
        respHandle.printText("<html><head></head><body><h1>Temperature System Plugin</h1>"
                + "<h1>Available Functions:</h1>"
                + "<table>"
                + "<thead><tr><th scope='col'>Action</th><th scope='col'>Description</th>"
                + "</tr></thead><tbody>");

        respHandle.printText("<tr><td><a href='TemperatureSystem?action=createDatabase' target='_blank'>Add entries</a></td><td>Creates 10.000 random database entries.</td></tr>");
        respHandle.printText("<tr><td><a href='TemperatureSystem?action=clearDatabase' target='_blank'>Clear database</a></td><td>Clears the current database.</td></tr>");
        respHandle.printText("<tr><td><a href='TemperatureSystem?action=showPage&pages=1+20'>View database</a></td><td>Browse the database.</td></tr>");
        respHandle.printText("<tr><td><a href='TemperatureSystem?action=getTemperature&date=2010+10+30' target='_blank'>View XML</a></td><td>XML-Viewer for a specific date.</td></tr>");
        respHandle.printText("</tbody></table></body></html>\n");
    }

    private void createDatabase(ResponseHandler respHandle) {
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
    }

    private void clearDatabase(ResponseHandler respHandle) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=TemperatureSystemDB;user=JulianL;password=Indrak";
            Connection db = DriverManager.getConnection(connectionUrl);
            Statement stmt = db.createStatement();
            String sql = "DELETE FROM TemperatureTable WHERE ID > 0;";
            stmt.executeUpdate(sql);
            sql = "DBCC CHECKIDENT ('TemperatureTable', RESEED, 1)";
            stmt.executeUpdate(sql);
            respHandle.printHeader("text/html");
            respHandle.printText("<html><head></head><body><h1>Database cleared.</h1></body></html>\n");
        } catch (ClassNotFoundException ex) {
            System.out.println("MS JDBC Driver not installed\n" + ex);
        } catch (SQLException ex) {
            System.out.println("Could not load database\n" + ex);
        }
    }

    private void showPageRange(ResponseHandler respHandle, Map<String, List<String>> incAttributes) {
        String index = "";
        for (Map.Entry<String, List<String>> entry : incAttributes.entrySet()) {
            if (entry.getKey().equals("pages")) {
                for (String value : entry.getValue()) {
                    index = value;
                }
            }
        }
        String[] urlParts = index.split(" ");
        if (urlParts.length != 2) {
            System.out.println("Error: invalid index range");
        } else {
            pageView(respHandle, urlParts[0], urlParts[1]);
        }
    }

    private void pageView(ResponseHandler respHandle, String incStart, String incEnd) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=TemperatureSystemDB;user=JulianL;password=Indrak";
            Connection db = DriverManager.getConnection(connectionUrl);
            Statement stmt = db.createStatement();

            int entries = 0;
            ResultSet rowCount = stmt.executeQuery("SELECT COUNT(1) FROM TemperatureTable");
            while (rowCount.next()) {
                entries = rowCount.getInt(1);
            }

            String sql = "SELECT ID, Temperature, Date\n"
                    + "FROM (SELECT ROW_NUMBER() OVER (ORDER BY Date) AS RowNum, *\n"
                    + "From TemperatureTable )\n"
                    + "AS RowConstrainedResult\n"
                    + "WHERE   RowNum >= " + Integer.parseInt(incStart) + " AND RowNum < " + Integer.parseInt(incEnd) + "\n"
                    + "ORDER BY RowNum";
            ResultSet rs = stmt.executeQuery(sql);

            respHandle.printHeader("text/html");
            respHandle.printStyle(1);
            if (!rs.isBeforeFirst()) {
                respHandle.printText("<h1>Invalid database range. Please check the entered range and ensure that entries exist.</h1></body></html>\n");
            } else {
                respHandle.printText("<html><head></head><body><h1>Browsing database...</h1>"
                        + "<table>"
                        + "<thead><tr><th scope='col'>ID</th><th scope='col'>Temperature</th><th scope='col'>Date</th>"
                        + "</tr></thead><tbody>");
                while (rs.next()) {
                    respHandle.printText("<td>" + rs.getInt("ID") + "</td>");
                    respHandle.printText("<td>" + rs.getInt("Temperature") + "</td>");
                    respHandle.printText("<td>" + rs.getDate("Date") + "</td></tr>");
                }
                rs.close();
                respHandle.printText("</tbody></table>\n");

                int currentStart = Integer.parseInt(incStart);
                int currentEnd = Integer.parseInt(incEnd);
                int prevStart, prevEnd, nextStart, nextEnd;


                respHandle.printText("<table><tr>");
                if (currentStart > 1) {
                    if (currentStart - 20 <= 1) {
                        prevStart = 1;
                        prevEnd = 20;
                    } else {
                        prevStart = currentStart - 20;
                        prevEnd = currentStart - 1;
                    }
                    respHandle.printText("<td><a href='TemperatureSystem?action=showPage&pages=" + prevStart + "+" + prevEnd + "'>Previous</a></td>");
                }

                if (currentEnd <= entries) {
                    if (currentEnd + 20 >= (entries + 1)) {
                        nextEnd = entries + 1;
                        nextStart = entries - 18;
                    } else {
                        nextEnd = currentEnd + 20;
                        nextStart = currentEnd + 1;
                    }
                    respHandle.printText("<td><a href='TemperatureSystem?action=showPage&pages=" + nextStart + "+" + nextEnd + "'>Next</a></td>");
                }
                respHandle.printText("</tr></table>");
                respHandle.printText("</body></html>\n");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("MS JDBC Driver not installed\n" + ex);
        } catch (SQLException ex) {
            System.out.println("Could not load database\n" + ex);
        }
    }

    private void showXmlInfo(ResponseHandler respHandle, Map<String, List<String>> incAttributes) throws ParserConfigurationException {
        String index = "";
        for (Map.Entry<String, List<String>> entry : incAttributes.entrySet()) {
            if (entry.getKey().equals("date")) {
                for (String value : entry.getValue()) {
                    index = value;
                }
            }
        }
        String[] urlParts = index.split(" ");
        if (urlParts.length != 3) {
            System.out.println("Error: invalid date");
        } else {
            xmlView(respHandle, urlParts[0], urlParts[1], urlParts[2]);
        }
    }

    private void xmlView(ResponseHandler respHandle, String incYear, String incMonth, String incDay) throws ParserConfigurationException {
        System.out.println("Info: " + incYear + " " + incMonth + " " + incDay);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=TemperatureSystemDB;user=JulianL;password=Indrak";
            Connection db = DriverManager.getConnection(connectionUrl);
            Statement stmt = db.createStatement();

            // Create statement and result set for customer information
            String sql = "SELECT ID, Temperature, Date\n"
                    + "FROM TemperatureTable\n"
                    + "WHERE Date = ' " + incYear + incMonth + incDay + " ';";
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                respHandle.printText("<h1>Invalid database range. Please check the entered range and ensure that entries exist.</h1></body></html>\n");
            } else {

                respHandle.printText("<?xml version='1.0' encoding='utf-8'?>"
                        + "<temperatures>\n");
                // For all selected customers
                while (rs.next()) {

                    respHandle.printText("<id>" + rs.getInt("ID") + "</id>\n");
                    respHandle.printText("<temperature>" + rs.getInt("Temperature") + "</temperature>\n");
                    respHandle.printText("<date>" + rs.getDate("Date") + "</date>\n");
                }
                respHandle.printText("</temperatures>");
                rs.close();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("MS JDBC Driver not installed\n" + ex);
        } catch (SQLException ex) {
            System.out.println("Could not load database\n" + ex);
        }
    }

    @Override
    public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
        System.out.println("Starting TemperatureSystem Plugin");
        ResponseHandler respHandle = new ResponseHandler(socket);

        if (incAttributes.isEmpty()) {
            try {
                respHandle.startStream();
            } catch (IOException ex) {
                System.out.println("Could not start stream: " + ex);
            }
            showStarterScreen(respHandle);
            respHandle.closeStream();
        } else {


            for (Map.Entry<String, List<String>> entry : incAttributes.entrySet()) {
                String key = entry.getKey();
                if (key.equals("action")) {
                    try {
                        respHandle.startStream();
                    } catch (IOException ex) {
                        System.out.println("Could not start stream: " + ex);
                    }

                    System.out.println("Key: " + key);
                    for (String value : entry.getValue()) {
                        switch (value) {
                            case "createDatabase":
                         
                                     createDatabase(respHandle);
                          
                                
                                break;
                            case "clearDatabase":
                                clearDatabase(respHandle);
                                break;
                            case "showPage":
                                showPageRange(respHandle, incAttributes);
                                break;
                            case "getTemperature":
                                try {
                                    showXmlInfo(respHandle, incAttributes);
                                } catch (ParserConfigurationException ex) {
                                    System.out.println("XML Parse Error: " + ex);
                                }
                                break;
                            default:
                                System.out.println("Unknown action");
                                showStarterScreen(respHandle);
                                break;
                        }
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