package webserver.plugins;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import webserver.PluginControl;
import webserver.ResponseHandler;

public class StaticFileSystem implements PluginControl {

    @Override
    public void init() {
        System.out.println("Found Plugin: StaticFileSystem");
    }

    @Override
    public void start() {
    }

    @Override
    public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
        System.out.println("Starting StaticFileSystem Plugin");
        ResponseHandler respHandle = new ResponseHandler(socket);
        if (incAttributes.isEmpty()) {
            try {
                respHandle.startStream();
                respHandle.printHeader("text/html");
                respHandle.printText("<style>"
                        + "body { 	margin:0; 	padding:20px; 	font:13px 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, Arial, sans-serif;	} p,table, caption, td, tr, th {	margin:0;	padding:0;	font-weight:normal;	}p {	margin-bottom:15px;	}table {	border-collapse:collapse;	margin-bottom:15px;	width:90%;	}		caption {	text-align:left;		font-size:15px;		padding-bottom:10px;		}		table td,	table th {		padding:5px;		border:1px solid #fff;	border-width:0 1px 1px 0;		}			thead th {		background:#91c5d4;		}					thead th[colspan],		thead th[rowspan] {			background:#66a9bd;			}			tbody th,	tfoot th {		text-align:left;		background:#91c5d4;		}			tbody td,	tfoot td {		text-align:center;		background:#d5eaf0;		}			tfoot th {		background:#b0cc7f;		}			tfoot td {		background:#d7e1c5;	font-weight:bold;		}				tbody tr.odd td { 		background:#bcd9e1;		}"
                        + "</style>");
                // HTTP error 404 File Not Found via try/catch FileNotFoundException


                String path = ".";
                if (incUrl != null && !incUrl.isEmpty()) {
                    path = incUrl;
                }

                File folder = new File(path);
                File[] listOfFiles = folder.listFiles();

                respHandle.printText("<html><head></head><body><h1>Current folder: " + path + "</h1>"
                        + "<h1>Files:</h1>"
                        + "<table summary='Folder:'>"
                        + "<thead><tr><th scope='col'>Filename</th><th scope='col'>Type</th><th scope='col'>Path</th>"
                        + "</tr></thead><tbody>");


                for (int i = 0; i < listOfFiles.length; i++) {

                    if (listOfFiles[i].isFile()) {
                        respHandle.printText("<tr><td><a href='StaticFileSystem/obtainFile?url=" + listOfFiles[i] + "'>" + listOfFiles[i].getName() + "</a></td>");
                        int index = listOfFiles[i].getName().lastIndexOf(".");
                        respHandle.printText("<td>" + listOfFiles[i].getName().substring(index + 1) + "</td>");
                        respHandle.printText("<td>" + listOfFiles[i].getPath() + "</td></tr>");
                    }
                }
                respHandle.printText("</tr></tbody></table></body></html>\n");
                respHandle.closeStream();

            } catch (IOException e) {
                System.out.println(e);
                respHandle.printFileNotFound();
                respHandle.closeStream();
            }
        } else {
                Set<String> keys = incAttributes.keySet();
                for (String key : keys) {
                    if (key.equals("url"))
                    {
                        String incStr = incAttributes.get(key).toString();
                        String filename = incStr.substring(1, incStr.length()-1);
                        try {
                            respHandle.startStream();
                            respHandle.openFile(getFileUrl(filename), getMimeType(filename));
                            respHandle.closeStream();
                        } catch (IOException ex) {
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
        if (url.startsWith("."))
            url = url.substring(2, url.length());
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