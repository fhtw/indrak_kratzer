package webserver;

import java.util.*;
import java.io.*;
import java.net.*;

public class UrlHandler {

    private String url;
    private String mimeType;

    UrlHandler(String unpreparedUrl) {
        url = unpreparedUrl;

        //Methode prepareUrl bereitet Url vor (s.u.)
        url = prepareUrl(url);

        // Methode obtainMimeType versucht den MimeType anhand der Dateiendung zu ermitteln
        mimeType = obtainMimeType(url);

    }

    public static String prepareUrl(String url) {
        // wurde keine Datei angegeben -> String Konkatenation mit "index.html"
        if (url.endsWith("/")) {
            url += "index.html";
        }

        // falls String mit / beginnt wird das Sonderzeichen entfernt
        while (url.indexOf("/") == 0) {
            url = url.substring(1);
        }

        // Slash "/" wird mit Backslash "\" ersetzt
        url = url.replace('/', File.separator.charAt(0));

        return url;
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

    public String getMimeType() {
        return this.mimeType;
    }

    public String getUrl() {
        return this.url;
    }

}
