/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitTest2 {
    
    public UnitTest2() {
    }

    @Test
    public void TestUrlHandler_obtainAttributes() {
        System.out.println("* JUnit4Test: TestUrlHandler_obtainAttributes()");
        String testUrl1 = "/url/index.html?attribute1=value1&attribute2=value2";
        Map<String, List<String>> map1 = new HashMap<String, List<String>>();
        List<String> arraylist1 = new ArrayList<String>();
        arraylist1.add("value1");
        List<String> arraylist2 = new ArrayList<String>();
        arraylist2.add("value2");
        map1.put("attribute1",arraylist1);
        map1.put("attribute2",arraylist2);
        UrlHandler urlHandleTester1 = new UrlHandler(testUrl1);
        assertEquals("1: maps must be equal", map1, urlHandleTester1.obtainAttributes(testUrl1)); 
        
        String testUrl2 = "/url/index.html?__mk_de_DE=AMAZON&url=search-alias%3Daps&field-keywords=this is a string";
        Map<String, List<String>> map2 = new HashMap<String, List<String>>();
        List<String> arraylist3 = new ArrayList<String>();
        arraylist3.add("AMAZON");
        List<String> arraylist4 = new ArrayList<String>();
        arraylist4.add("search-alias=aps");
        List<String> arraylist5 = new ArrayList<String>();
        arraylist5.add("this is a string");
        map2.put("__mk_de_DE",arraylist3);
        map2.put("url",arraylist4);
        map2.put("field-keywords",arraylist5);
        UrlHandler urlHandleTester2 = new UrlHandler(testUrl2);
        assertEquals("2: maps must be equal", map2, urlHandleTester2.obtainAttributes(testUrl2)); 
        
        /* FRAGEN!!
        System.out.println("* JUnit4Test: TestUrlHandlerObtainAttributes()");
        String testUrl3 = "/url/index.html?attribute1=value1,value2&attribute2=value3";
        Map<String, List<String>> map3 = new HashMap<String, List<String>>();
        List<String> arraylist6 = new ArrayList<String>();
        arraylist6.add("value1");
        arraylist6.add("value2");
        List<String> arraylist7 = new ArrayList<String>();
        arraylist7.add("value3");
        map3.put("attribute1",arraylist6);
        map3.put("attribute2",arraylist7);
        UrlHandler urlHandleTester3 = new UrlHandler(testUrl3);
        assertEquals("1: maps must be equal", map3, urlHandleTester3.obtainAttributes(testUrl3));
        */
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