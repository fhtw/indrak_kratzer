package webserver;

import java.util.*;
import java.io.*;
import java.net.*;

public class UrlHandler {

    private String url;
    private String pluginCandidate;
    Map<String, List<String>> attributeList;
    
  
    
    UrlHandler(String incUrl) throws UnsupportedEncodingException {
        this.url = prepareUrl(incUrl);
        pluginCandidate = obtainPluginCandidate(this.url);
        this.url = URLDecoder.decode(this.url, "iso-8859-1");
    }

    public Map<String, List<String>> obtainAttributes(String url) {
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
        
 
        if (url.endsWith("/")) {
            url = url.substring(0, url.length()-1);
        }
 
        
        return url;
    }
    
    public static String obtainPluginCandidate(String url)
    {
        String [] arr = url.split("[\\W]", 2);
        if (arr[0] != null)
            return arr[0];
        return "none";

    }
    
    public String getPluginCandidate() {
        return this.pluginCandidate;
    }

    public String getUrl() {
        return this.url;
    }
    
    public void urlRemPlugin()
    {
        String subStr = this.url.substring(this.pluginCandidate.length());
        if (subStr.startsWith("/"))
            this.url = prepareUrl(this.url.substring(this.pluginCandidate.length()+1));
        else this.url = prepareUrl(this.url.substring(this.pluginCandidate.length()));
    }
}
