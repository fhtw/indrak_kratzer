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
    private boolean pluginUse;
    UrlHandler urlHandle;
    Map<String, List<String>> attributeList;

    RequestHandler(Socket socket, List<String> availablePlugins) {
        pluginName = "";
        pluginUse = false;
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

                        Iterator<String> it = availablePlugins.iterator();
                        while (it.hasNext()) {
                            if (urlHandle.getPluginCandidate().equalsIgnoreCase(it.next().toString())) {
                                pluginUse = true;
                                this.pluginName = urlHandle.getPluginCandidate();
                                urlHandle.urlRemPlugin();
                            }
                        }
                        this.url = urlHandle.getUrl();
                    } else {
                        throw new FileNotFoundException();
                    }

                    if (method.equalsIgnoreCase("GET")) {
                        this.attributeList = urlHandle.obtainAttributes(url);
                        if (!attributeList.isEmpty())
                        {
                            Set<String> keys = attributeList.keySet();
                        }
                    } else if (method.equalsIgnoreCase("POST")) {
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
                            int tempStart = cLength.indexOf(": ") + 2;
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

    public String getUrl() {
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

    public boolean getPluginCheck() {
        return this.pluginUse;
    }

    public String getPlugin() {
        return this.pluginName;
    }
    
    public UrlHandler getUrlHandle()
    {
        return this.urlHandle;
    }
       
    public Map<String, List<String>> getAttributeList() {
        return this.attributeList;
    }
}