/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import webserver.plugins.NavigationSystem;
import webserver.plugins.RBTSystem;
import webserver.plugins.StaticFileSystem;
import webserver.plugins.TemperatureSystem;

public class JUnitTests {

    public JUnitTests() {
    }
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test

    public void TestUrlHandler_prepareUrl() throws UnsupportedEncodingException {
        String testUrl1 = "/this/is/an/url/";
        String expectedUrl1 = "this/is/an/url";
        UrlHandler urlHandleTester1 = new UrlHandler(testUrl1);

        String testUrl2 = "this/is/an/url/";
        String expectedUrl2 = "this/is/an/url";
        UrlHandler urlHandleTester2 = new UrlHandler(testUrl2);

        String testUrl3 = "";
        String expectedUrl3 = "";
        UrlHandler urlHandleTester3 = new UrlHandler(testUrl3);

        String testUrl4 = "/";
        String expectedUrl4 = "";
        UrlHandler urlHandleTester4 = new UrlHandler(testUrl4);

        String testUrl5 = "/url";
        String expectedUrl5 = "url";
        UrlHandler urlHandleTester5 = new UrlHandler(testUrl5);

        String testUrl6 = "url/";
        String expectedUrl6 = "url";
        UrlHandler urlHandleTester6 = new UrlHandler(testUrl6);

        String testUrl7 = "url/url";
        String expectedUrl7 = "url/url";
        UrlHandler urlHandleTester7 = new UrlHandler(testUrl7);

        assertEquals("url1 must be prepared", expectedUrl1, urlHandleTester1.getUrl());
        assertEquals("url2 must be prepared", expectedUrl2, urlHandleTester2.getUrl());
        assertEquals("url3 must be prepared", expectedUrl3, urlHandleTester3.getUrl());
        assertEquals("url4 must be prepared", expectedUrl4, urlHandleTester4.getUrl());
        assertEquals("url5 must be prepared", expectedUrl5, urlHandleTester5.getUrl());
        assertEquals("url6 must be prepared", expectedUrl6, urlHandleTester6.getUrl());
        assertEquals("url7 must be prepared", expectedUrl7, urlHandleTester7.getUrl());
    }

    public void TempSystem_XMLView() throws IOException, ParserConfigurationException {
        TemperatureSystem tempSys = new TemperatureSystem();
        ResponseHandler respHandle = new ResponseHandler();
        exception.expect(NullPointerException.class);
        tempSys.xmlView(respHandle, "2012", "10", "10");
    }

    @Test
    public void TestUrlHandler_obtainAttributes() throws UnsupportedEncodingException {
        String testUrl1 = "/url/index.html?attribute1=value1&attribute2=value2";
        Map<String, List<String>> map1 = new HashMap<>();
        List<String> arraylist1 = new ArrayList<>();
        arraylist1.add("value1");
        List<String> arraylist2 = new ArrayList<>();
        arraylist2.add("value2");
        map1.put("attribute1", arraylist1);
        map1.put("attribute2", arraylist2);
        UrlHandler urlHandleTester1 = new UrlHandler(testUrl1);
        assertEquals("1: maps must be equal", map1, urlHandleTester1.obtainAttributes(testUrl1));

        String testUrl2 = "/url/index.html?__mk_de_DE=AMAZON&url=search-alias%3Daps&field-keywords=this is a string";
        Map<String, List<String>> map2 = new HashMap<>();
        List<String> arraylist3 = new ArrayList<>();
        arraylist3.add("AMAZON");
        List<String> arraylist4 = new ArrayList<>();
        arraylist4.add("search-alias=aps");
        List<String> arraylist5 = new ArrayList<>();
        arraylist5.add("this is a string");
        map2.put("__mk_de_DE", arraylist3);
        map2.put("url", arraylist4);
        map2.put("field-keywords", arraylist5);
        UrlHandler urlHandleTester2 = new UrlHandler(testUrl2);
        assertEquals("2: maps must be equal", map2, urlHandleTester2.obtainAttributes(testUrl2));
    }

    @Test
    public void TestUrlHandler_obtainPluginCandidate() throws UnsupportedEncodingException {
        String testUrl1 = "/superplugin/is/an/url/";
        String expectedPlugin1 = "superplugin";

        String testUrl2 = "staticFileSys/";
        String expectedPlugin2 = "staticFileSys";

        String testUrl3 = "/pluginTool";
        String expectedPlugin3 = "pluginTool";

        String testUrl4 = "simpleplug";
        String expectedPlugin4 = "simpleplug";

        String testUrl5 = "";
        String expectedPlugin5 = "";

        String testUrl6 = "/";
        String expectedPlugin6 = "";

        UrlHandler urlHandleTester1 = new UrlHandler(testUrl1);
        UrlHandler urlHandleTester2 = new UrlHandler(testUrl2);
        UrlHandler urlHandleTester3 = new UrlHandler(testUrl3);
        UrlHandler urlHandleTester4 = new UrlHandler(testUrl4);
        UrlHandler urlHandleTester5 = new UrlHandler(testUrl5);
        UrlHandler urlHandleTester6 = new UrlHandler(testUrl6);

        assertEquals("returned plugin must be equal to given string", expectedPlugin1, urlHandleTester1.getPluginCandidate());
        assertEquals("returned plugin must be equal to given string", expectedPlugin2, urlHandleTester2.getPluginCandidate());
        assertEquals("returned plugin must be equal to given string", expectedPlugin3, urlHandleTester3.getPluginCandidate());
        assertEquals("returned plugin must be equal to given string", expectedPlugin4, urlHandleTester4.getPluginCandidate());
        assertEquals("returned plugin must be equal to given string", expectedPlugin5, urlHandleTester5.getPluginCandidate());
        assertEquals("returned plugin must be equal to given string", expectedPlugin6, urlHandleTester6.getPluginCandidate());

    }

    @Test
    public void ResponseHandler_printHeader() {
        StaticFileSystem staticFileTest = new StaticFileSystem();

        assertEquals("returned plugin must be equal to given string", "\\testurl", staticFileTest.getFileUrl("/testurl"));
        assertEquals("returned plugin must be equal to given string", "\\test\\url\\", staticFileTest.getFileUrl("/test/url/"));
        assertEquals("returned plugin must be equal to given string", "test\\url", staticFileTest.getFileUrl("./test/url"));
        assertEquals("returned plugin must be equal to given string", "testurl", staticFileTest.getFileUrl("./testurl"));
    }

    @Test
    public void TestUrlHandler_urlRemPlugin() throws UnsupportedEncodingException {
        String testUrl1 = "/plugin/this/is/an/url/";
        String expectedUrl1 = "this/is/an/url";
        UrlHandler urlHandleTester1 = new UrlHandler(testUrl1);
        urlHandleTester1.urlRemPlugin();

        assertEquals("plugin must be removed from string", expectedUrl1, urlHandleTester1.getUrl());

    }

    @Test
    public void ResponseHandler_getMimeType() {
        StaticFileSystem staticFileTest = new StaticFileSystem();

        assertEquals("returned plugin must be equal to given string", "text/html", staticFileTest.getMimeType("/testurl.html"));
        assertEquals("returned plugin must be equal to given string", "text/plain", staticFileTest.getMimeType("/test/url/test.java"));
        assertEquals("returned plugin must be equal to given string", "image/gif", staticFileTest.getMimeType("./test/url.gif"));
        assertEquals("returned plugin must be equal to given string", "application/octet-stream", staticFileTest.getMimeType("./testurl.class"));
        assertEquals("returned plugin must be equal to given string", "image/jpeg", staticFileTest.getMimeType("./testurl.jpg"));
        assertEquals("returned plugin must be equal to given string", "text/plain", staticFileTest.getMimeType("./testurl.else"));
    }

    @Test
    public void ResponseHandle_NullPointerException() {
        ResponseHandler responseHandle = new ResponseHandler();
        exception.expect(NullPointerException.class);
        responseHandle.openFile("nosocketnullpointer", "none");
    }
    
    @Test
    public void PluginHandler_initPlugins() throws MalformedURLException {
        PluginHandler pluginHandle = new PluginHandler();
        pluginHandle.initPlugins();
        int amount = pluginHandle.getPluginList().size();
        assertEquals("value must be equal to given string", 4, amount);
    }

    @Test
    public void PluginControl_Notnull() throws UnsupportedEncodingException {
        PluginControl obj = new PluginControl() {

            @Override
            public void start(Map<String, List<String>> incAttributes, String incUrl, Socket socket) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getName() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        assertNotNull(obj);
    }

    @Test
    public void PluginHandler_Notnull() throws UnsupportedEncodingException {
        PluginHandler obj = new PluginHandler();
        assertNotNull(obj);
    }

    @Test
    public void URlHandle_Notnull() throws UnsupportedEncodingException {

        UrlHandler obj = new UrlHandler("test");
        assertNotNull(obj);
    }

    @Test
    public void TempSys_Notnull() throws UnsupportedEncodingException {
        TemperatureSystem tempSys = new TemperatureSystem();
        assertNotNull(tempSys);

    }

    @Test
    public void RBTSys_Notnull() throws UnsupportedEncodingException {
        RBTSystem rbt = new RBTSystem();
        assertNotNull(rbt);
    }

    @Test
    public void StaticFile_Notnull() throws UnsupportedEncodingException {
        StaticFileSystem obj = new StaticFileSystem();
        assertNotNull(obj);
    }

    @Test
    public void Nav_Sys_Notnull() throws UnsupportedEncodingException {
        NavigationSystem obj = new NavigationSystem();
        assertNotNull(obj);
    }

    @Test
    public void TestUrlHandler_staticFileSys() throws UnsupportedEncodingException {

        TemperatureSystem tempSys = new TemperatureSystem();
        ResponseHandler respHandle = new ResponseHandler();
        exception.expect(NullPointerException.class);
        tempSys.clearDatabase(respHandle);
    }

    @Test
    public void PluginHandler_initPlugins_Exception() {
        PluginHandler pluginHandle = new PluginHandler();
        exception.expect(NullPointerException.class);
        pluginHandle.getPluginList().size();
    }

    @Test
    public void ResponseHandle_openFile_Exception() {
        ResponseHandler responseHandle = new ResponseHandler();
        exception.expect(NullPointerException.class);
        responseHandle.openFile("nosocketnullpointer", "none");
    }

    @Test
    public void TestUrlHandler_urlRemPlugin2() throws UnsupportedEncodingException {
        String testUrl1 = "/plugin/this/is/an/url/";
        String expectedUrl1 = "this/is/an/url";
        UrlHandler urlHandleTester1 = new UrlHandler(testUrl1);
        urlHandleTester1.urlRemPlugin();

        String testUrl2 = "plugin/this/is/an/url";
        String expectedUrl2 = "this/is/an/url";
        UrlHandler urlHandleTester2 = new UrlHandler(testUrl2);
        urlHandleTester2.urlRemPlugin();

        String testUrl3 = "plugin/this/is/an/url/";
        String expectedUrl3 = "this/is/an/url";
        UrlHandler urlHandleTester3 = new UrlHandler(testUrl3);
        urlHandleTester3.urlRemPlugin();

        String testUrl4 = "/";
        String expectedUrl4 = "";
        UrlHandler urlHandleTester4 = new UrlHandler(testUrl4);
        urlHandleTester4.urlRemPlugin();

        String testUrl5 = "/plugin";
        String expectedUrl5 = "";
        UrlHandler urlHandleTester5 = new UrlHandler(testUrl5);
        urlHandleTester5.urlRemPlugin();

        String testUrl6 = "plugin/";
        String expectedUrl6 = "";
        UrlHandler urlHandleTester6 = new UrlHandler(testUrl6);
        urlHandleTester6.urlRemPlugin();

        String testUrl7 = "";
        String expectedUrl7 = "";
        UrlHandler urlHandleTester7 = new UrlHandler(testUrl7);
        urlHandleTester7.urlRemPlugin();

        assertEquals("plugin must be removed from string", expectedUrl1, urlHandleTester1.getUrl());
        assertEquals("plugin must be removed from string", expectedUrl2, urlHandleTester2.getUrl());
        assertEquals("plugin must be removed from string", expectedUrl3, urlHandleTester3.getUrl());
        assertEquals("plugin must be removed from string", expectedUrl4, urlHandleTester4.getUrl());
        assertEquals("plugin must be removed from string", expectedUrl5, urlHandleTester5.getUrl());
        assertEquals("plugin must be removed from string", expectedUrl6, urlHandleTester6.getUrl());
        assertEquals("plugin must be removed from string", expectedUrl7, urlHandleTester7.getUrl());

    }
    @Test
    public void ResponseHandler_TestType() {
        StaticFileSystem staticFileTest = new StaticFileSystem();

        assertEquals("returned plugin must be equal to given string", "text/html", staticFileTest.getMimeType("/testurl.html"));

    }
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
