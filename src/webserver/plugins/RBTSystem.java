package webserver.plugins;

import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import webserver.PluginControl;

import webserver.ResponseHandler;

public class RBTSystem implements PluginControl {

    static Map<String, String> params = new HashMap<>();
    private static String algorithm = "MD5";

    private void showStarterScreen(ResponseHandler respHandle) {
        System.out.println("Plugin: no Attributes");
        respHandle.printHeader("text/html");
        respHandle.printStyle(2);
        respHandle.printText("<html><head></head><body><h1>Rainbowtable System</h1>");
        respHandle.printText("<div id='tfheader'>"
                + "		<form id='tfnewsearch' method='get' action='RBTSystem' accept-charset='ISO-8859-1'>\n"
                + "		        <h2>View MD5 Database:</h2> <input type='hidden' name='action=showDB' /> <input type='submit' value='View DB' class='tfbutton2'></form>"
                + "		<form id='tfnewsearch' method='get' action='RBTSystem' accept-charset='ISO-8859-1'>\n"
                + "		        <h2>Add a password to create MD5 Hash: </h2><input type='text' class='tftextinput' name='action=addEntry&pw' size='21' maxlength='120'><input type='submit' value='Add Password' class='tfbutton'>\n"
                + "		</form>"
                + "             <form id='tfnewsearch' method='get' action='RBTSystem' accept-charset='ISO-8859-1'>\n"
                + "		        <h2>Search DB for MD5 password: </h2><input type='text' class='tftextinput' name='action=getHash&pw' size='21' maxlength='120'><input type='submit' value='Get Password' class='tfbutton'>\n"
                + "		</form><div class='tfclear'></div></div></body></html>\n");
    }
    
    private void showDB(ResponseHandler respHandle) {
        respHandle.printHeader("text/html");
        respHandle.printStyle(1);
        respHandle.printText("<html><head></head><body><h1>Rainbowtable System</h1>"
                + "<h1>MD5 Database:</h1>"
                + "<table summary='DB:'>"
                + "<thead><tr><th scope='col'>Password</th><th scope='col'>MD5</th>"
                + "</tr></thead><tbody>");
        for (Map.Entry<String, String> entry : params.entrySet()) {

            respHandle.printText("<tr><td>" + entry.getValue() + "</td><td>" + entry.getKey() + "</td></tr>");

        }
        respHandle.printText("</tbody></table></body></html>\n");
    }

    private void showResult(ResponseHandler respHandle, String incPW) {
        respHandle.printHeader("text/html");
        respHandle.printStyle(1);
        respHandle.printText("<html><head></head><body><h1>Rainbowtable System</h1>"
                + "<h1>Search Result:</h1>"
                + "<table>"
                + "<thead><tr><th scope='col'>MD5</th><th scope='col'>Password</th></tr></thead><tbody>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().equals(incPW)) {
                respHandle.printText("<tr><td>" + incPW + "</td><td>" + entry.getValue() + "</td></tr>");
            }
        }
        respHandle.printText("</tbody></table></body></html>\n");
    }

    private void proceedRequest(ResponseHandler respHandle, Map<String, List<String>> incAttributes, String request) {

        String PW = "";
        for (Map.Entry<String, List<String>> entry : incAttributes.entrySet()) {
            if (entry.getKey().equals("pw")) {
                for (String value : entry.getValue()) {
                    PW = value;
                }
            }
        }
        switch (request) {
            case ("show"):
                showResult(respHandle, PW);
                break;
            case ("add"):
                addEntry(respHandle, PW);
                break;
            default:
                break;
        }
    }

    private void addEntry(ResponseHandler respHandle, String incPW) {
        byte[] plainText = incPW.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        md.reset();
        md.update(plainText);
        byte[] encodedPassword = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        System.out.println("Added: " + sb.toString() + " and " + incPW);
        params.put(sb.toString(), incPW);
        showStarterScreen(respHandle);
    }

    @Override
    public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
        System.out.println("Starting RainbowSystem Plugin");
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
                System.out.println(key);
                if (key.equals("action")) {
                    try {
                        respHandle.startStream();
                    } catch (IOException ex) {
                        System.out.println("Could not start stream: " + ex);
                    }

                    System.out.println("Key: " + key);
                    for (String value : entry.getValue()) {
                        switch (value) {
                            case "addEntry":
                                proceedRequest(respHandle, incAttributes, "add");
                                respHandle.closeStream();
                                break;
                            case "getHash":
                                proceedRequest(respHandle, incAttributes, "show");
                                respHandle.closeStream();
                                break;
                            case "showDB":
                                showDB(respHandle);
                                respHandle.closeStream();
                                break;
                            default:
                                System.out.println("Unknown action");
                                showStarterScreen(respHandle);
                                respHandle.closeStream();
                                break;
                        }
                    }
                } else {
                    showStarterScreen(respHandle);
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