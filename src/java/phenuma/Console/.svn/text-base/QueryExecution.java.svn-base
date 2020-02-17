/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.Console;

import phenuma.network.Network;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ontologizer.StudySet;
import ontologizer.StudySetFactory;
import ontologizer.go.TermID;
import phenuma.networkproyection.*;
/**
 *
 * @author Armando
 */
public class QueryExecution {
    
     /**
     * Execute gene query
     * 
     * @param variable
     * @param file
     * @param querytype
     * @throws IOException 
     */
    public static void executeGeneQuery(List<String> variable, String file, int querytype) throws IOException 
    {
        /**Create a study set*/
        StudySet studyset = StudySetFactory.createFromList(variable, true);

        /**Generate network*/
        GeneQuery genequery = new GeneQuery(studyset, querytype);
        genequery.setOntology(getOntology(querytype));
        genequery.setSemanticSimilarity(phenuma.constants.Constants.ROBINSON_SYMMETRIC);
        genequery.executeQueryDB();
        
        /*Print network*/
        printNetwork(genequery, file);
    }
    
    
     /**
     * Execute gene query
     * 
     * @param variable
     * @param querytype
     * @return Network
     * @throws IOException 
     */
    public static Network executeGeneQueryNetwork(List<String> variable,  int querytype) throws IOException 
    {
        /**Create a study set*/
        StudySet studyset = StudySetFactory.createFromList(variable, true);

        /**Generate network*/
        GeneQuery genequery = new GeneQuery(studyset, querytype);
        genequery.setThreshold(1.8);
        genequery.setOntology(getOntology(querytype));
        genequery.setSemanticSimilarity(phenuma.constants.Constants.ROBINSON_SYMMETRIC);
        genequery.executeQueryDB();
        
        return genequery.getResult();
    }



    
    /**
     * Execute disease query and write the result in a file.
     * 
     * @param variable
     * @param querytype
     * @throws IOException 
     */
    public static void executeDiseaseQuery(List<String> variable, String file, int querytype) throws IOException {
        /**Create a study set*/
        StudySet studyset = StudySetFactory.createFromList(variable, true);

        /**Generate network*/
        OmimQuery diseasequery = new OmimQuery(studyset, querytype);
        diseasequery.setOntology(getOntology(querytype));
        diseasequery.setSemanticSimilarity(phenuma.constants.Constants.ROBINSON_SYMMETRIC);
        diseasequery.executeQueryDB();
        
        /*Print network*/
        printNetwork(diseasequery, file);

    }
    
    
       /**
     * Execute disease query 
     * 
     * @param variable
     * @param file
     * @param querytype
     * @throws IOException 
     */
    public static Network executeDiseaseQueryNetwork(List<String> variable,  int querytype) throws IOException {
        /**Create a study set*/
        StudySet studyset = StudySetFactory.createFromList(variable, true);

        /**Generate network*/
        OmimQuery diseasequery = new OmimQuery(studyset, querytype);
        diseasequery.setOntology(getOntology(querytype));
        diseasequery.setSemanticSimilarity(phenuma.constants.Constants.ROBINSON_SYMMETRIC);
        diseasequery.executeQueryDB();
        
        /*Print network*/
        return diseasequery.getResult();
    }
    
    /**
     * Execute phenotype query and write the result in a file.
     * 
     * @param variable
     * @param file
     * @param querytype
     * @throws IOException 
     */
    public static void executeHPOQuery(ArrayList<TermID> termlist, String file, int querytype) throws IOException {

        /**Generate network*/
        PhenotypeQuery phenotypequery = new PhenotypeQuery(termlist, querytype);
        phenotypequery.setOntology(getOntology(querytype));
        phenotypequery.setSemanticSimilarity(phenuma.constants.Constants.ROBINSON_SYMMETRIC);
        phenotypequery.executeQueryDB();
        
        /*Print network*/
        printNetwork(phenotypequery, file);
    }
    
    
    /**
     * Execute phenotype query and write the result in a file.
     * 
     * @param variable
     * @param querytype
     * @throws IOException 
     */
    public static Network executeHPOQueryNetwork(ArrayList<String> termlist, int querytype) throws IOException {
        
        ArrayList<TermID> termIDlist = phenomizer.utils.StringUtils.stringListToTermIDList(termlist);
        
        /**Generate network*/
        PhenotypeQuery phenotypequery = new PhenotypeQuery(termIDlist, querytype);
        phenotypequery.setOntology(getOntology(querytype));
        phenotypequery.setSemanticSimilarity(phenuma.constants.Constants.ROBINSON_SYMMETRIC);
        phenotypequery.executeQueryDB();
        
        /*Print network*/
        return phenotypequery.getResult();
    }
    
    
    
    
    /**
     * Get ontology used.
     * 
     * @param queryType
     * @return 
     */
    private static int getOntology(int queryType)
    {
        /*TODO: Esto sirve?? */
        if(queryType == NetworkConstants.BIPARTITE_GENE_HPO ||
           queryType == NetworkConstants.UNIPARTITE_GENE_SS_HPO)
            return phenuma.constants.Constants.ONTO_HPO;
        else
            return phenuma.constants.Constants.ONTO_HPO;
    }
    
    /**
     * Returns true if the network inlcudes score value for each pair of elements.
     * 
     * @param queryType
     * @return 
     */
    private static boolean getPrintScore(int queryType)
    {
        return(queryType != NetworkConstants.BIPARTITE_DISEASE_HPO 
               && queryType != NetworkConstants.BIPARTITE_GENE_HPO
               && queryType != NetworkConstants.BIPARTITE_GENE_DISEASE_SS);
    }
    
    
    /**
     * Print network in a file o in the console.
     * 
     * @param query
     * @param file
     * @throws IOException 
     */
    private static void printNetwork(NetworkQuery query, String file) throws IOException {
        
        /*The score is printed when the network is the result of a semantic similarity calculation.*/
        boolean printScore = getPrintScore(query.getQueryType());
 
        /**Write result in a file*/
        if(query.getResult()!=null || !query.getResult().getNetwork().isEmpty())
            if(file!=null)
                query.getResult().writeToFile(file);
            else
                System.out.println(query.getResult().toString());
        else
            System.err.println("The result network is empty.");
    }
    
    
}
