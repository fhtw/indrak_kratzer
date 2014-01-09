package webserver;

import java.io.*;
import java.net.*;

public class ResponseHandler {

    private Socket socket;
    private PrintStream servOut;

    public ResponseHandler() {
    }

    public ResponseHandler(Socket socket) {
        this.socket = socket;
    }

    public void startStream() throws IOException {
        this.servOut = new PrintStream(new BufferedOutputStream(
                this.socket.getOutputStream()));
    }

    public void closeStream() {
        this.servOut.close();
    }

    public void printHeader(String incMimeType) {
        servOut.print("HTTP/1.0 200 OK\r\n"
                + "Content-type: " + incMimeType + "\r\n\r\n");
    }

    public void printStyle(int incStyle) {
        switch (incStyle) {
            case 1:
                servOut.print("<style>"
                        + "body {  	margin:0; 	padding:20px; 	font:13px 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, Arial, sans-serif;	} p,table, caption, td, tr, th {	margin:0;	padding:0;	font-weight:normal; text-align:left;	}p {	margin-bottom:15px;	}table {	border-collapse:collapse;	margin-bottom:15px;	width:60%;	}		caption {	text-align:left;		font-size:15px;		padding-bottom:10px;		}		table td,	table th {		padding:5px;		border:1px solid #fff;	border-width:0 1px 1px 0;		}			thead th {		background:#91c5d4;		}					thead th[colspan],		thead th[rowspan] {			background:#66a9bd;			}			tbody th,	tfoot th {		text-align:left;		background:#91c5d4;		}			tbody td,	tfoot td {		text-align:left;		background:#d5eaf0;		}			tfoot th {		background:#b0cc7f;		}			tfoot td {		background:#d7e1c5;	font-weight:bold;		}				tbody tr.odd td { 		background:#bcd9e1;		}"
                        + "</style>");
                break;
            case 2:
                servOut.print("<style> body { margin:0; padding:20px; font:13px 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, Arial, sans-serif; } #tfheader{ background-color:#c3dfef; width:50%; } #tfnewsearch{ float:left; padding:20px; } .tftextinput{ margin: 0; padding: 5px 15px; font-family: Arial, Helvetica, sans-serif; font-size:14px; border:1px solid #0076a3; border-right:0px; border-top-left-radius: 5px 5px; border-bottom-left-radius: 5px 5px; } .tfbutton { margin: 0; padding: 5px 15px; font-family: Arial, Helvetica, sans-serif; font-size:14px; outline: none; cursor: pointer; text-align: center; text-decoration: none; color: #ffffff; border: solid 1px #0076a3; border-right:0px; background: #0095cd; background: -webkit-gradient(linear, left top, left bottom, from(#00adee), to(#0078a5)); background: -moz-linear-gradient(top, #00adee, #0078a5); border-top-right-radius: 5px 5px; border-bottom-right-radius: 5px 5px; } .tfbutton2 { margin: 0; padding: 5px 15px; font-family: Arial, Helvetica, sans-serif; font-size:14px; outline: none; cursor: pointer; text-align: center; text-decoration: none; color: #ffffff; border: solid 1px #0076a3; background: #0095cd; background: -webkit-gradient(linear, left top, left bottom, from(#00adee), to(#0078a5)); background: -moz-linear-gradient(top, #00adee, #0078a5); border-radius: 5px 5px; } .tfbutton:hover, .tfbutton2:hover { text-decoration: none; background: #007ead; background: -webkit-gradient(linear, left top, left bottom, from(#0095cc), to(#00678e)); background: -moz-linear-gradient(top, #0095cc, #00678e); }   .tfbutton::-moz-focus-inner { border: 0; } .tfclear{ clear:both; }</style>");
                break;
            default:
                servOut.print("<style>"
                        + "body {  	margin:0; 	padding:20px; 	font:13px 'Lucida Grande', 'Lucida Sans Unicode', Helvetica, Arial, sans-serif;	} p,table, caption, td, tr, th {	margin:0;	padding:0;	font-weight:normal; text-align:left;	}p {	margin-bottom:15px;	}table {	border-collapse:collapse;	margin-bottom:15px;	width:60%;	}		caption {	text-align:left;		font-size:15px;		padding-bottom:10px;		}		table td,	table th {		padding:5px;		border:1px solid #fff;	border-width:0 1px 1px 0;		}			thead th {		background:#91c5d4;		}					thead th[colspan],		thead th[rowspan] {			background:#66a9bd;			}			tbody th,	tfoot th {		text-align:left;		background:#91c5d4;		}			tbody td,	tfoot td {		text-align:left;		background:#d5eaf0;		}			tfoot th {		background:#b0cc7f;		}			tfoot td {		background:#d7e1c5;	font-weight:bold;		}				tbody tr.odd td { 		background:#bcd9e1;		}"
                        + "</style>");
                break;
        }
    }

    public void printFileNotFound() {
        servOut.print("HTTP/1.0 404 Not Found\r\n"
                + "Content-type: text/html\r\n\r\n"
                + "<html><head></head><body>File not found</body></html>\n");
    }

    public void printText(String incText) {
        servOut.print(incText);
    }

    public void openFile(String incFile, String incMimeType) {
        try {
            // Datei wird geöffnet
            InputStream f = new FileInputStream(incFile);
            printHeader(incMimeType);

            // Dateinhalt wird dem Client übermittelt
            byte[] a = new byte[4096];
            int i;
            while ((i = f.read(a)) > 0) {
                servOut.write(a, 0, i);
            }
        } catch (IOException e) {
            printFileNotFound();
            servOut.close();
            System.out.println(e);
        }
    }

    public void runDefault(String url) throws IOException {
        //    if (url.startsWith("GetTemperature/")) {
        startStream();
        openFile("index.html", "text/html");
        closeStream();
    }
}