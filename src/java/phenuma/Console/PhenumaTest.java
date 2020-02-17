/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.Console;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologizer.StudySet;
import ontologizer.StudySetFactory;
import ontologizer.calculation.SemanticCalculation;
import phenomizer.hpo.Phenuma;
import phenuma.networkproyection.GeneQuery;
import phenuma.networkproyection.NetworkConstants;

/**
 *
 * @author rocio
 */
public class PhenumaTest {
    
    
    public static void testGeneQuery() throws IOException{
        /*Phenomizer Object*/
        Phenuma phenomizer = Phenuma.getInstance();

        /*Semantic calculation hpo*/
        SemanticCalculation sc_hpogenes;

        /*Query inputs*/
        String[] genes_array2 = {"1411"};
        StudySet genes2 = StudySetFactory.createFromArray(genes_array2, true);
        
        GeneQuery q = new GeneQuery(genes2, NetworkConstants.UNIPARTITE_GENE_SS_HPO);
        q.setThreshold(2.0);
        
        q.executeQueryDB();   

    }
    
    
    
    
    public static void main(String[] args){
        
        try {
            testGeneQuery();
        } catch (IOException ex) {
            Logger.getLogger(PhenumaTest.class.getName()).log(Level.SEVERE, "StudySet creation error.", ex);
        }

                
        
    }
    
}
