package webserver;

import java.util.*;
import java.io.*;
import java.net.*;

public class UrlHandler {

    private String url;
    private String mimeType;
    private String pluginCandidate;
    Map<String, List<String>> attributeList;
 

    UrlHandler(String incUrl, String incMethod) {

        //Methode prepareUrl bereitet Url vor (s.u.)
        if (incMethod.equalsIgnoreCase("GET"))
        {
            attributeList = obtainAttributes(incUrl);
            if (!attributeList.isEmpty())
            {
                Set<String> keys = attributeList.keySet();
                System.out.println("Parsing GET-attributes:");
                for (String key : keys)  
                {  
                   System.out.println("Name=" + key + ", Value=" + attributeList.get(key));   
                }
            }
        }
        pluginCandidate = obtainPluginCandidate(incUrl);
        url = prepareUrl(incUrl);

        // Methode obtainMimeType versucht den MimeType anhand der Dateiendung zu ermitteln
        mimeType = obtainMimeType(url);

    }

  
    public static Map<String, List<String>> obtainAttributes(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }
    
    
    public static String prepareUrl(String url) {
        
        // falls String mit / beginnt wird das Sonderzeichen entfernt
        while (url.indexOf("/") == 0) {
            url = url.substring(1);
        }
        
        // wurde keine Datei angegeben -> String Konkatenation mit "index.html"
        if (url.endsWith("/")) {
            url += "index.html";
        }
        // Slash "/" wird mit Backslash "\" ersetzt
        url = url.replace('/', File.separator.charAt(0));

        return url;
    }
    
    public static String obtainPluginCandidate(String url)
    {
        StringTokenizer st = new StringTokenizer(url, "/");
        if (st.countTokens() >= 1)
            return st.nextToken();
        return "none";

    }

    public static String obtainMimeType(String name) {
        if (name.endsWith(".html") || name.endsWith(".htm")) {
            return "text/html";
        } else if (name.endsWith(".txt") || name.endsWith(".java")) {
            return "text/plain";
        } else if (name.endsWith(".gif")) {
            return "image/gif";
        } else if (name.endsWith(".class")) {
            return "application/octet-stream";
        } else if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "text/plain";
        }
    }
    
    public String getPluginCandidate() {
        return this.pluginCandidate;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getUrl() {
        return this.url;
    }
 
}
