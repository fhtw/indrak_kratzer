package webserver.plugins;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import webserver.PluginControl;
 
import static webserver.PluginControl.params;
import webserver.ResponseHandler;

public class NavigationSystem implements PluginControl {
 

    private void showStarterScreen(ResponseHandler respHandle) {
        System.out.println("Plugin: no Attributes");
        respHandle.printHeader("text/html");
        respHandle.printText("<style>"
                + "body {  	margin:0; 	padding:20px; 	font:13px 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, Arial, sans-serif;	} "
                + "	#tfheader{\n"
                + "		background-color:#c3dfef;\n"
                + "             width:50%;"
                + "	}\n"
                + "	#tfnewsearch{\n"
                + "		float:left;\n"
                + "		padding:20px;\n"
                + "	}\n"
                + "	.tftextinput{\n"
                + "		margin: 0;\n"
                + "		padding: 5px 15px;\n"
                + "		font-family: Arial, Helvetica, sans-serif;\n"
                + "		font-size:14px;\n"
                + "		border:1px solid #0076a3; border-right:0px;\n"
                + "		border-top-left-radius: 5px 5px;\n"
                + "		border-bottom-left-radius: 5px 5px;\n"
                + "	}\n"
                + "	.tfbutton {\n"
                + "		margin: 0;\n"
                + "		padding: 5px 15px;\n"
                + "		font-family: Arial, Helvetica, sans-serif;\n"
                + "		font-size:14px;\n"
                + "		outline: none;\n"
                + "		cursor: pointer;\n"
                + "		text-align: center;\n"
                + "		text-decoration: none;\n"
                + "		color: #ffffff;\n"
                + "		border: solid 1px #0076a3; border-right:0px;\n"
                + "		background: #0095cd;\n"
                + "		background: -webkit-gradient(linear, left top, left bottom, from(#00adee), to(#0078a5));\n"
                + "		background: -moz-linear-gradient(top,  #00adee,  #0078a5);\n"
                + "		border-top-right-radius: 5px 5px;\n"
                + "		border-bottom-right-radius: 5px 5px;\n"
                + "	}\n"
                + "	.tfbutton2 {\n"
                + "		margin: 0;\n"
                + "		padding: 5px 15px;\n"
                + "		font-family: Arial, Helvetica, sans-serif;\n"
                + "		font-size:14px;\n"
                + "		outline: none;\n"
                + "		cursor: pointer;\n"
                + "		text-align: center;\n"
                + "		text-decoration: none;\n"
                + "		color: #ffffff;\n"
                + "		border: solid 1px #0076a3;\n"
                + "		background: #0095cd;\n"
                + "		background: -webkit-gradient(linear, left top, left bottom, from(#00adee), to(#0078a5));\n"
                + "		background: -moz-linear-gradient(top,  #00adee,  #0078a5);\n"
                + "		border-radius: 5px 5px;\n"
                + "	}\n"
                + "	.tfbutton:hover, .tfbutton2:hover {\n"
                + "		text-decoration: none;\n"
                + "		background: #007ead;\n"
                + "		background: -webkit-gradient(linear, left top, left bottom, from(#0095cc), to(#00678e));\n"
                + "		background: -moz-linear-gradient(top,  #0095cc,  #00678e);\n"
                + "	}\n"
                + "	/* Fixes submit button height problem in Firefox */\n"
                + "	.tfbutton::-moz-focus-inner {\n"
                + "	  border: 0;\n"
                + "	}\n"
                + "	.tfclear{\n"
                + "		clear:both;\n"
                + "	}"
                + "</style>");
        respHandle.printText("<html><head></head><body><h1>Navigation System</h1>");

        respHandle.printText("<div id='tfheader'>"
                + "		<form id='tfnewsearch' method='get' action='NavigationSystem' accept-charset='ISO-8859-1'>\n"
                + "		        <h2>Map Generator: </h2> <input type='hidden' name='action=reloadMap' /> <input type='submit' value='Generator' class='tfbutton2'>\n"
                + "		</form>\n"
                + "		<form id='tfnewsearch' method='get' action='NavigationSystem' accept-charset='ISO-8859-1'>\n"
                + "		        <h2>Location Finder: </h2><input type='text' class='tftextinput' name='action=findLocation&loc' size='21' maxlength='120'><input type='submit' value='Search' class='tfbutton'>\n"
                + "		</form><div class='tfclear'></div></div>");
        respHandle.printText("</body></html>\n");
    }

    private void showLocation(ResponseHandler respHandle, String location) {
        respHandle.printHeader("text/html");
        respHandle.printText("<style>"
                + "body {  	margin:0; 	padding:20px; 	font:13px 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, Arial, sans-serif;	} p,table, caption, td, tr, th {	margin:0;	padding:0;	font-weight:normal; text-align:left;	}p {	margin-bottom:15px;	}table {	border-collapse:collapse;	margin-bottom:15px;	width:60%;	}		caption {	text-align:left;		font-size:15px;		padding-bottom:10px;		}		table td,	table th {		padding:5px;		border:1px solid #fff;	border-width:0 1px 1px 0;		}			thead th {		background:#91c5d4;		}					thead th[colspan],		thead th[rowspan] {			background:#66a9bd;			}			tbody th,	tfoot th {		text-align:left;		background:#91c5d4;		}			tbody td,	tfoot td {		text-align:left;		background:#d5eaf0;		}			tfoot th {		background:#b0cc7f;		}			tfoot td {		background:#d7e1c5;	font-weight:bold;		}				tbody tr.odd td { 		background:#bcd9e1;		}"
                + "</style>");
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
        if (true) {
            params.clear();
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();

                DefaultHandler handler = new DefaultHandler() {
                    boolean node = false;
                    boolean tag = false;
                    boolean bnname = false;
                    boolean bsalary = false;
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
                                if (!values.contains(key))
                                {
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

            respHandle.printHeader("text/html");
            respHandle.printText("<style>"
                    + "body {  	margin:0; 	padding:20px; 	font:13px 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, Arial, sans-serif;	} p,table, caption, td, tr, th {	margin:0;	padding:0;	font-weight:normal; text-align:left;	}p {	margin-bottom:15px;	}table {	border-collapse:collapse;	margin-bottom:15px;	width:60%;	}		caption {	text-align:left;		font-size:15px;		padding-bottom:10px;		}		table td,	table th {		padding:5px;		border:1px solid #fff;	border-width:0 1px 1px 0;		}			thead th {		background:#91c5d4;		}					thead th[colspan],		thead th[rowspan] {			background:#66a9bd;			}			tbody th,	tfoot th {		text-align:left;		background:#91c5d4;		}			tbody td,	tfoot td {		text-align:left;		background:#d5eaf0;		}			tfoot th {		background:#b0cc7f;		}			tfoot td {		background:#d7e1c5;	font-weight:bold;		}				tbody tr.odd td { 		background:#bcd9e1;		}"
                    + "</style>");
            respHandle.printText("<html><head></head><body><h1>Navigation System</h1>"
                    + "<h2>Search result:</h2>"
                    + "<h1>DONE</h1></body></html>\n");
            for (Map.Entry<String, List<String>> param : params.entrySet()) {
                String key = param.getKey();
                List<String> valueList = param.getValue();
                System.out.println("Key: " + key);
                System.out.print("Values: ");
                for (String s : valueList) {
                    System.out.print(s + " ");
                }
            }

        } else {
            respHandle.printHeader("text/html");
            respHandle.printText("<style>"
                    + "body {  	margin:0; 	padding:20px; 	font:13px 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, Arial, sans-serif;	} p,table, caption, td, tr, th {	margin:0;	padding:0;	font-weight:normal; text-align:left;	}p {	margin-bottom:15px;	}table {	border-collapse:collapse;	margin-bottom:15px;	width:60%;	}		caption {	text-align:left;		font-size:15px;		padding-bottom:10px;		}		table td,	table th {		padding:5px;		border:1px solid #fff;	border-width:0 1px 1px 0;		}			thead th {		background:#91c5d4;		}					thead th[colspan],		thead th[rowspan] {			background:#66a9bd;			}			tbody th,	tfoot th {		text-align:left;		background:#91c5d4;		}			tbody td,	tfoot td {		text-align:left;		background:#d5eaf0;		}			tfoot th {		background:#b0cc7f;		}			tfoot td {		background:#d7e1c5;	font-weight:bold;		}				tbody tr.odd td { 		background:#bcd9e1;		}"
                    + "</style>");
            respHandle.printText("<html><head></head><body><h1>Navigation System</h1>"
                    + "<h2>Rebuilding map... please wait</h2>"
                    + "</body></html>\n");
        } 
    }

    @Override
    public void init() {
        System.out.println("Found Plugin: NavigationSystem");
    }

    @Override
    public void start() {
 
    }

    @Override
    public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
 
 
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
                                break;
                            case "findLocation":
                                try {
                                    findLocation(respHandle, incAttributes);
                                } catch (ParserConfigurationException | SAXException ex) {
                                    System.out.println("SAX Error: " + ex);
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