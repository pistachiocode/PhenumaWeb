/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma;

import phenuma.objects.QueryExecution;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import phenuma.network.NetworkDB;
import phenuma.networkproyection.NetworkConstants;

/**
 *
 * @author Armando
 */
public class QueryExecutionTest {
    
    public QueryExecutionTest() {
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
     * Test of executeGeneQuery method, of class QueryExecution.
     */
    @Test
    public void testExecuteGeneQuery() throws Exception {
        System.out.println("executeGeneQuery");
        
        List<String> items = new ArrayList<String>();
        NetworkDB result = QueryExecution.executeGeneQueryDB(items, NetworkConstants.UNIPARTITE_GENE_SS_HPO, 0.0);
        
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of executeDiseaseQuery method, of class QueryExecution.
     */
   // @Test
    public void testExecuteDiseaseQuery() throws Exception {
        System.out.println("executeDiseaseQuery");
        
        List<String> items = new ArrayList<String>();
        items.add("166200");
        
       // Network result = QueryExecution.executeDiseaseQuery(items, NetworkConstants.UNIPARTITE_GENE_SS_HPO, 0.0);
        
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of executeRareDiseaseQuery method, of class QueryExecution.
     */
    //@Test
    public void testExecuteRareDiseaseQuery() throws Exception {
        System.out.println("executeRareDiseaseQuery");
        
        List<String> items = new ArrayList<String>();
        items.add("154");
        
        System.out.println(phenuma.constants.Constants.MAX_IC_GENES_HPO);
        
       // Network result = QueryExecution.executeRareDiseaseQuery(items, NetworkConstants.UNIPARTITE_ORPHAN_SS, 0.0);
    }
    
    
   //@Test
    public void textSaveQuery(){
        
        //QueryExecution.saveQuery("1411, 1410", NetworkConstants.UNIPARTITE_GENE_SS_HPO, NetworkConstants.INPUT_TYPE_GENES, new Double(12.2));
    }


}
