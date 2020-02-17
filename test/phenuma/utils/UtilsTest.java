/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.entities.Disease;

/**
 *
 * @author Armando
 */
public class UtilsTest {
    
    public UtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of inputToString method, of class Utils.
     */
    //@Test
    public void testInputToString() {
        System.out.println("inputToString");
        
        List<String> object = new ArrayList<String>();
        
        object.add("144");
        object.add("8342");
        object.add("7342");

        String expResult = "144 8342 7342";
        
        
        String result = Utils.inputToString(object);
        
        
        assertEquals(expResult, result);

    }
    
    //@Test
    public void testEmptyInputToString() {
        System.out.println("emptyInputToString");
        
        List<String> object = new ArrayList<String>();
        
        String expResult = "";
        
        
        String result = Utils.inputToString(object);
        
        
        assertEquals(expResult, result);

    }
    
    
    //@Test
    public void testNullInputToString() {
        System.out.println("nullInputToString");
        
        List<String> object = null;
        
        String expResult = "";
        
        
        String result = Utils.inputToString(object);
        
        
        assertEquals(expResult, result);

    }
    
    //@Test
    public void testDiseaseByOMIM()
    {
        try {
            DatabaseQueries q = new DatabaseQueries();
            
            Disease name = q.getDiseaseByOmim("271980");
            
            System.out.println(name.getName());
            
        } 
        catch (PhenumaException ex) 
        {
            System.out.println("ERROR");
        }
        
    }
    
    
    @Test
    public void testCurrentDateTime(){
        
        String date = Utils.currentDateTime();
        System.out.println(date);
        
    }
    
    //@Test
    public void testIsSymbol()
    {
        assertTrue(Utils.isSymbol("MAP2K1"));
        assertTrue(Utils.isSymbol("MAP2K2"));
        assertTrue(Utils.isSymbol("SLC17A5"));
        
        assertFalse(Utils.isSymbol("1234"));
        assertFalse(Utils.isSymbol("1234A"));
        
    }
}