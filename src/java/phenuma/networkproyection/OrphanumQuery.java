/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.networkproyection;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ontologizer.StudySet;
import ontologizer.StudySetFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import phenomizer.hpo.PhenumaDB;
import phenuma.constants.Constants;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.network.NetworkDB;

/**
 *
 * @author Rocío Rodríguez López
 */
public class OrphanumQuery extends NetworkQuery implements QueryOperations{
    
    /**
     * Logger object
     */
    static final Logger logger = Logger.getLogger(OrphanumQuery.class.getName());
    
    /**
     * PhenumaDB: phenuma database conection object.
     */
    PhenumaDB pdb = PhenumaDB.getInstance();                 
        
    private StudySet studySet;    
    
    /**
     * Constructor of OrphanumQuery.
     * This constructor create a OrphanumQuery object and set the
     * parameters for Orphanum queries: ontology, annotation, and measure.
     * 
     * @param studySet
     *              StudySet object that contains a set of Orphanums.
     * @param queryType 
     *              Type of query to be executed.
     */
    public OrphanumQuery(StudySet studySet, int queryType) {
        super(queryType);
        
        /* Query stydySet*/
        this.studySet = studySet;
        
        /* Ontology */
        this.ontology = Constants.ONTO_HPO;
        
        /* Annotations (rare diseases)*/
        this.associations = Constants.ASSOC_ER_HPO;
        
        /* Semanti similarity measure*/
        this.semanticSimilarity = Constants.ROBINSON_SYMMETRIC; 
    }
 
    public StudySet getStudySet() {
        return studySet;
    }

    public void setStudySet(StudySet studySet) {
        this.studySet = studySet;
    }
    
   
    /**
     * Execute database query.
     * 
     */
    @Override
    public void executeQueryDB() {
        try {
            logger.info("Execute orphanum query: "+queryType+" input orphan "+studySet.toString());

            DatabaseQueries q  = new DatabaseQueries(); 
            
            NetworkDB networkdb = new NetworkDB();
            networkdb.setInputElements(studySet);
            networkdb.setInputtype(NetworkConstants.INPUT_TYPE_ORPHA);
            networkdb.setQuerytype(queryType);
            
            StudySet relatedObjectsStudySet = studySet; 
            Map<Integer, List<Integer>> disease2genes = new HashMap<Integer, List<Integer>>();     
            
            /**
             * Gene network from an orphanum query.
             * Getting genes related with the orphanum query
             * for gene network request.
             */           
            if(NetworkUtils.isUnipartiteGenes(queryType))
            { 
                disease2genes = NetworkUtils.geneStudyFromOrphanum(this.studySet);
                relatedObjectsStudySet = StudySetFactory.createFromIntegerSet(disease2genes.keySet(), true);             
            }
            
            /**
             * Get the set of Nodes of the outcome network.
             */
            StudySet networknodesset = new StudySet();
            if (!NetworkUtils.isBipartite(queryType))
            {
                networknodesset = q.getNetworkNodes(relatedObjectsStudySet, 
                                                    NetworkConstants.INPUT_TYPE_ORPHA, 
                                                    queryType, 
                                                    this.threshold);
                networkdb.setNodes(networknodesset);
            }
            else
            {
                networkdb.setNodesDiseases(q.getObjectRelatedBipartite(relatedObjectsStudySet, 
                                                                       NetworkConstants.INPUT_TYPE_ORPHA, 
                                                                       queryType, 
                                                                       this.threshold));
            }
            
            /**
             * Get the rows of the outcome network
             */
            networkdb.setRows(q.networkQuery(relatedObjectsStudySet, networknodesset, NetworkConstants.INPUT_TYPE_ORPHA, this.queryType, this.threshold));
            networkdb.setRelatedElements(disease2genes);
            
            this.resultDB = networkdb;

        } catch (IOException ex) {
            Logger.getLogger(OrphanumQuery.class.getName()).log(Level.ERROR, null, ex);
        } 
    }


 

}
