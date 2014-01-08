package webserver.plugins;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import webserver.PluginControl;

import webserver.ResponseHandler;

public class NavigationSystem implements PluginControl {
    static Map<String, List<String>> params = new HashMap<>();
    static boolean isBusy = false;

    private void showStarterScreen(ResponseHandler respHandle) {
        System.out.println("Plugin: no Attributes");
        respHandle.printHeader("text/html");
        respHandle.printStyle(2);
        respHandle.printText("<html><head></head><body><h1>Navigation System</h1>");
        respHandle.printText("<div id='tfheader'>"
                + "		<form id='tfnewsearch' method='get' action='NavigationSystem' accept-charset='ISO-8859-1'>\n"
                + "		        <h2>Map Generator: </h2> <input type='hidden' name='action=reloadMap' /> <input type='submit' value='Generator' class='tfbutton2'></form>"
                + "		<form id='tfnewsearch' method='get' action='NavigationSystem' accept-charset='ISO-8859-1'>\n"
                + "		        <h2>Location Finder: </h2><input type='text' class='tftextinput' name='action=findLocation&loc' size='21' maxlength='120'><input type='submit' value='Search' class='tfbutton'>\n"
                + "		</form><div class='tfclear'></div></div></body></html>\n");
    }

    private void showLocation(ResponseHandler respHandle, String location) {
        respHandle.printHeader("text/html");
        respHandle.printStyle(1);
        respHandle.printText("<html><head></head><body><h1>Navigation System</h1>"
                + "<h1>Search Result:</h1>"
                + "<table>"
                + "<thead><tr><th scope='col'>" + location + "</th></tr></thead><tbody>");
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            if (entry.getKey().equals(location)) {
                for (String value : entry.getValue()) {
                    respHandle.printText("<tr><td>" + value + "</td></tr>");
                }
            }
        }
        respHandle.printText("</tbody></table></body></html>\n");
    }

    private void findLocation(ResponseHandler respHandle, Map<String, List<String>> incAttributes) throws ParserConfigurationException, SAXException {
        String location = "";
        for (Map.Entry<String, List<String>> entry : incAttributes.entrySet()) {
            if (entry.getKey().equals("loc")) {
                for (String value : entry.getValue()) {
                    location = value;
                }
            }
        }
        System.out.println("Full Location: " + location);
        showLocation(respHandle, location);
    }

    private void reloadMap(ResponseHandler respHandle) {
        ExecutorService myExecutor = Executors.newCachedThreadPool();
        params.clear();

        if (!isBusy) {
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        isBusy = true;
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser saxParser = factory.newSAXParser();

                        DefaultHandler handler = new DefaultHandler() {
                            String key = "";
                            String value = "";

                            @Override
                            public void startElement(String uri, String localName, String qName, Attributes attributes)
                                    throws SAXException {
                                if (qName.equalsIgnoreCase("node")) {
                                }
                                if (qName.equalsIgnoreCase("tag")) {
                                    if (attributes.getValue("k") != null && attributes.getValue("v") != null) {
                                        String k = attributes.getValue("k");
                                        String v = attributes.getValue("v");
                                        switch (k) {
                                            case "addr:city":
                                                key = v;
                                                break;
                                            case "addr:street":
                                                value = v;
                                                break;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void endElement(String uri, String localName,
                                    String qName) throws SAXException {
                                if (qName.equalsIgnoreCase("tag")) {
                                }
                                if (qName.equalsIgnoreCase("node")) {


                                    if (!value.isEmpty() && !key.isEmpty()) {
                                        List<String> values = params.get(value);
                                        if (values == null) {
                                            values = new ArrayList<String>();
                                            params.put(value, values);
                                        }
                                        if (!values.contains(key)) {
                                            values.add(key);
                                        }
                                    }
                                    value = "";
                                    key = "";
                                }
                            }
                            @Override
                            public void characters(char ch[], int start, int length) throws SAXException {
                            }
                        };
                        saxParser.parse("austria-latest.osm", handler);
                    } catch (ParserConfigurationException | SAXException | IOException e) {
                        System.out.println("SAX Error: " + e);
                    }
                    isBusy = false;
                }
            });

            respHandle.printHeader("text/html");
            respHandle.printStyle(1);
            respHandle.printText("<html><head></head><body><h1>Navigation System</h1>"
                    + "<h2>Rebuilding map... please wait</h2></body></html>\n");
        } else {
            respHandle.printHeader("text/html");
            respHandle.printStyle(1);
            respHandle.printText("<html><head></head><body><h1>Navigation System</h1>"
                    + "<h2>Abort: the map is currently reloading</h2>"
                    + "</body></html>\n");
        }

    }

    @Override
    public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
        System.out.println("Starting NavigationSystem Plugin");
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
                            case "reloadMap":
                                reloadMap(respHandle);
                                respHandle.closeStream();
                                break;
                            case "findLocation":
                                try {
                                    findLocation(respHandle, incAttributes);
                                    respHandle.closeStream();
                                } catch (ParserConfigurationException | SAXException ex) {
                                    System.out.println("SAX Error: " + ex);
                                }
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