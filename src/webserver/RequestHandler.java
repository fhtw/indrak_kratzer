package webserver;

import java.util.*;
import java.io.*;
import java.net.*;

public class RequestHandler {

    private String url;
    private String method;
    private String body;
    private String standard;
    private int contentLength;
    private String pluginName;
    private boolean plugin;
    UrlHandler urlHandle;

    RequestHandler(Socket socket) {
        pluginName = "";
        plugin = false;
        url = "";
        method = "";
        standard = "";
        body = "";

        try {
            BufferedReader clientOut = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            // Erste Zeile des Client requests wird eingelesen
            // -> Klasse StringTokenizer um String in Elemente aufzubrechen
            String firstLine = clientOut.readLine();

            /*
             * Zeile wird eingelesen, HTTP Request der Form:
             * <METHODE> <DIRECTORY> <STANDARD>
             * Bsp.: GET /infotext.html HTTP/1.1
             * erster Token = request Methode
             * Folgetoken = filename
             * falls Token leer -> bad request
             */
            if (firstLine != null) {
                if (!firstLine.isEmpty()) {
                    StringTokenizer st = new StringTokenizer(firstLine);
                    if (st.countTokens() > 1 && st.countTokens() <= 3) {
                        method = st.nextToken();
                        url = st.nextToken();
                        standard = st.nextToken();
                        urlHandle = new UrlHandler(url, method);
                        
                        System.out.println("Plugin-Request: " + urlHandle.getPluginName());
                        
                    } else {
                        throw new FileNotFoundException();
                    }
                    
                    if (method.equalsIgnoreCase("GET"))
                    {
                        
                    } else if (method.equalsIgnoreCase("POST"))
                    {
                        String getHeader = "";
                        String cLength = "";
                        int i = 0;
                        do {
                            getHeader = clientOut.readLine();
                            if (getHeader.contains("Content-Length:")) {
                                cLength = getHeader;
                            }
                            System.out.println(getHeader);
                        } while (!getHeader.isEmpty());

                        if (!cLength.isEmpty()) {
                            int tempStart = cLength.indexOf(": ")+2;
                            int tempEnd = cLength.length();
                            String temp = cLength.substring(tempStart, tempEnd);
                            this.contentLength = Integer.parseInt(temp);

                            while (i < this.contentLength) {
                                body += (char) clientOut.read();
                                i++;
                            }
                            //body = URLDecoder.decode(body, "UTF-8");
                        }
                    }

                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String getUnpreparedUrl() {
        return this.url;
    }

    public String getMethod() {
        return this.method;
    }

    public String getBody() {
        return this.body;
    }

    public String getStandard() {
        return this.standard;
    }
}
