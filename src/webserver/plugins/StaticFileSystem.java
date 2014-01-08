package webserver.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;
import webserver.PluginControl;
import webserver.ResponseHandler;
import javax.swing.text.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class StaticFileSystem implements PluginControl {

    private String path;

    @Override
    public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
        System.out.println("Starting StaticFileSystem Plugin");
        ResponseHandler respHandle = new ResponseHandler(socket);
JEditorPane jep = new JEditorPane( );
jep.setEditable(false);
        try {
            jep.setPage("http://www.google.com");
        } catch (IOException ex) {
            Logger.getLogger(StaticFileSystem.class.getName()).log(Level.SEVERE, null, ex);
        }

        JScrollPane scrollPane = new JScrollPane(jep);
JFrame f = new JFrame("O'Reilly & Associates");
f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
f.setContentPane(scrollPane);
f.setSize(512, 342);
  

        if (incAttributes.isEmpty()) {
            try {
                this.path = ".";
                if ((!incUrl.isEmpty()) && (incUrl != null)) {
                    this.path = incUrl;
                }

                File folder = new File(path);
                respHandle.startStream();
                if (!folder.exists()) {
                    throw new FileNotFoundException("File does not exist");
                }
                File[] listOfFiles = folder.listFiles();
                
                respHandle.printHeader("text/html");
                respHandle.printStyle(1);
                respHandle.printText("<html><head></head><body><h1>Current folder: " + path + "</h1>"
                        + "<h1>Files:</h1>"
                        + "<table summary='Folder:'>"
                        + "<thead><tr><th scope='col'>Filename</th><th scope='col'>Type</th><th scope='col'>Path</th>"
                        + "</tr></thead><tbody>");


                for (int i = 0; i < listOfFiles.length; i++) {

                    if (listOfFiles[i].isFile()) {
                        respHandle.printText("<tr><td><a href='StaticFileSystem?url=" + listOfFiles[i] + "'>" + listOfFiles[i].getName() + "</a></td>");

                        int index = listOfFiles[i].getName().lastIndexOf(".");
                        respHandle.printText("<td>" + listOfFiles[i].getName().substring(index + 1) + "</td>");
                        respHandle.printText("<td>" + listOfFiles[i].getPath() + "</td></tr>");
                    }
                }
                respHandle.printText("</tr></tbody></table></body></html>\n");
                respHandle.closeStream();
            } catch (FileNotFoundException e) {
                System.out.println(e);
                respHandle.printFileNotFound();
                respHandle.closeStream();
            } catch (IOException e) {
                System.out.println(e);
                respHandle.printFileNotFound();
                respHandle.closeStream();
            }
        } else {
            Set<String> keys = incAttributes.keySet();
            for (String key : keys) {
                if (key.equals("url")) {
                    String incStr = incAttributes.get(key).toString();
                    String filename = incStr.substring(1, incStr.length() - 1);
                    try {
                        respHandle.startStream();
                        respHandle.openFile(getFileUrl(filename), getMimeType(filename));
                        respHandle.closeStream();
                    } catch (IOException e) {
                        System.out.println(e);
                        respHandle.printFileNotFound();
                        respHandle.closeStream();
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    private String getFileUrl(String url) {
        // Slash "/" wird mit Backslash "\" ersetzt
        url = url.replace('/', File.separator.charAt(0));
        if (url.startsWith(".")) {
            url = url.substring(2, url.length());
        }
        return url;
    }

    private String getMimeType(String incUrl) {
        if (incUrl.endsWith(".html") || incUrl.endsWith(".htm")) {
            return "text/html";
        } else if (incUrl.endsWith(".txt") || incUrl.endsWith(".java")) {
            return "text/plain";
        } else if (incUrl.endsWith(".gif")) {
            return "image/gif";
        } else if (incUrl.endsWith(".class")) {
            return "application/octet-stream";
        } else if (incUrl.endsWith(".jpg") || incUrl.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "text/plain";
        }
    }
}