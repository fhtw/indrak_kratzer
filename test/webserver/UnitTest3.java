package webserver;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitTest3 {
    
    public UnitTest3() {
    }
    
    @Test
    public void TestUrlHandler_obtainPluginCandidate() {
        System.out.println("* JUnit4Test: TestUrlHandler_obtainPluginCandidate()");
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