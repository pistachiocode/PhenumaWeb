/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.cysnetwork;

import phenuma.cysnetwork.CysNetworkUtils;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import phenuma.networkproyection.NetworkConstants;
import static org.junit.Assert.*;

/**
 *
 * @author Armando
 */
public class CysNetworkUtilsTest {
    
    public CysNetworkUtilsTest() {
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
     * Test of getNetworkRelationshipTypeDB method, of class CysNetworkUtils.
     */
    @Test
    public void testGetNetworkRelationshipTypeDB() {
        System.out.println("getNetworkRelationshipTypeDB");

        Integer str = CysNetworkUtils.getRelationshipTypeLabelDB(4, NetworkConstants.INPUT_TYPE_GENES, NetworkConstants.UNIPARTITE_GENE_METABOLIC);
        System.out.println(str);
        
        str = CysNetworkUtils.getRelationshipTypeLabelDB(1, NetworkConstants.INPUT_TYPE_GENES, NetworkConstants.BIPARTITE_GENE_DISEASE_GENEMAP);
        System.out.println(str);
        
        str = CysNetworkUtils.getRelationshipTypeLabelDB(0, NetworkConstants.INPUT_TYPE_GENES, NetworkConstants.BIPARTITE_GENE_ORPHAN_SS);
        System.out.println(str);
        
        str = CysNetworkUtils.getRelationshipTypeLabelDB(3, NetworkConstants.INPUT_TYPE_GENES, NetworkConstants.BIPARTITE_GENE_ORPHAN_ORPHADATA);
        System.out.println(str);

    }

   
}
