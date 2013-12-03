package webserver;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitTest1 {
    
    public UnitTest1() {
    }
    
    @Test
    public void TestUrlHandler_prepareUrl() {
        System.out.println("* JUnit4Test: TestUrlHandler_prepareUrl()");
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