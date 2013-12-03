package webserver;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitTest4 {
    
    public UnitTest4() {
    }
    
    @Test
    public void TestUrlHandler_urlRemPlugin() {
        System.out.println("* JUnit4Test: TestUrlHandler_prepareUrl()");
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