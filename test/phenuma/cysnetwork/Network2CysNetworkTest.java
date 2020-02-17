/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.cysnetwork;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologizer.StudySet;
import ontologizer.StudySetFactory;
import ontologizer.go.TermID;
import ontologizer.types.ByteString;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import phenomizer.hpo.Phenuma;
import phenuma.utils.PhenumaException;
import phenuma.objects.QueryExecution;
import phenuma.network.NetworkDB;
import phenuma.network.PhenotypeNetworkDB;
import phenuma.networkproyection.GeneQuery;
import phenuma.networkproyection.NetworkConstants;
import phenuma.objects.PhenumaConstants;

/**
 *
 * @author Armando
 */
public class Network2CysNetworkTest {
    
    public StudySet genes;
    public StudySet genes2;
        
    public Network2CysNetworkTest() {
        
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
     * Test of network2CysNetwork method, of class Network2CysNetwork.
     */
    //@Test
    public void testNetwork2CysNetwork() throws Exception {
        System.out.println("network2CysNetwork");

        ByteString omim = new ByteString("230800");
        
        ArrayList<TermID> hpo = Phenuma.getInstance().getDiseases_hpo_associations().get(omim).getAssociations();
       
        ArrayList<String> query = new ArrayList<String>();
        for (TermID term : hpo)
        {
            query.add(term.toString());
        }
        
        PhenotypeNetworkDB n = (PhenotypeNetworkDB) QueryExecution.executePhenotypeQuery(query, NetworkConstants.QUERY_DISEASE_NETWORK, 2);

        
        saveToFile("resources/out/tests/network.txt", n.toString());
        
    }
    
    
    /**
     * Test cysNode method.
     */
    @Test
    public void testCysNode() 
    {

        Integer objectID = 255225;
        String name =  null;
        boolean isInput = false;

        Integer nodetype = CytoscapeConstants.CYS_NODE_TYPE_ORPHA;


        String cysNodeStr = Network2CysNetworkDB.cysNode(objectID, name, isInput, nodetype);
        System.out.println(cysNodeStr);

        
    }
    
    
    public static void saveToFile(String file, String content) throws IOException{
        
        PrintWriter pw = new PrintWriter(new FileWriter(file));
        
        pw.println(content);
        
        pw.close();
        
    }


}
