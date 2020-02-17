/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.cysnetwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologizer.StudySet;
import ontologizer.StudySetFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import phenuma.network.NetworkDB;
import phenuma.networkproyection.GeneQuery;
import phenuma.networkproyection.NetworkConstants;
import phenuma.objects.QueryExecution;
import phenuma.utils.PhenumaException;

/**
 *
 * @author rocio
 */
public class CysNetworkTest {
    
    public StudySet genes;
    public StudySet genes2;
        
    
    @BeforeClass
    public static void setUpClass() {
        

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {                      
        try 
        {
            String[] genes_array = {"5009"};
            genes = StudySetFactory.createFromArray(genes_array, true);
            
            
            String[] genes_array2 = {"384","4953","79814","113451","6723","6611","262","54498","216"};
            genes2 = StudySetFactory.createFromArray(genes_array2, true);
        } 
        catch (IOException ex) 
        {
            System.err.println("Error al crear el study set.");
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMainnetwork method, of class CysNetwork.
     */
    @Test
    public void testOrphaCysNetwork() {
        System.out.println("orphaCysNetwork");
        
        CysNetwork instance = null;
         
        try {
            //Input query
            List<String> orphalist = new ArrayList<String>();
            orphalist.add("95");
                        
            //Database query
            NetworkDB n = QueryExecution.executeRareDiseaseQuery(orphalist, NetworkConstants.UNIPARTITE_ORPHAN_SS, 0.0);
            
            //Convert to cytoscape network format
            instance = new CysNetwork(n, NetworkConstants.UNIPARTITE_ORPHAN_SS);         
            
            
        } catch (IOException ex) {
            Logger.getLogger(Network2CysNetworkTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.out.println(instance.toString());
                

    }

   // @Test
    public void testOrphaNetwork() 
    {
        try {
            List<String> orphalist = new ArrayList<String>();
            orphalist.add("95");
            
            
            NetworkDB n = QueryExecution.executeRareDiseaseQuery(orphalist, NetworkConstants.UNIPARTITE_ORPHAN_SS, 0.0);
            
            CysNetwork network1 = new CysNetwork(n, NetworkConstants.UNIPARTITE_ORPHAN_SS);         
            
            
        } catch (IOException ex) {
            Logger.getLogger(Network2CysNetworkTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //@Test
    public void testGeneNetwork() 
    {
        GeneQuery genequery = new GeneQuery(genes2, NetworkConstants.UNIPARTITE_GENE_METABOLIC);
        genequery.executeQueryDB();

        CysNetwork cysnet = new CysNetwork(genequery.getResultDB(), NetworkConstants.UNIPARTITE_GENE_METABOLIC);

        System.out.println(cysnet.getMainnetwork());
        System.out.println(cysnet.getSubnetworks());
                    
    }

}
