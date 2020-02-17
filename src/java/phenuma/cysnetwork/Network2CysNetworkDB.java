/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.cysnetwork;

import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import ontologizer.StudySet;
import ontologizer.types.ByteString;
import org.apache.log4j.Logger;
import phenomizer.utils.NumberUtils;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.entities.Disease;
import phenuma.entities.Gene;
import phenuma.entities.RareDisease;
import phenuma.managedbeans.PhenumaWeb;
import phenuma.network.NetworkDB;
import phenuma.network.PhenotypeNetworkDB;
import phenuma.networkproyection.NetworkConstants;
import phenuma.networkproyection.NetworkUtils;
import phenuma.objects.PhenumaConstants;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Armando
 */
public class Network2CysNetworkDB {
    
    static final Logger logger = Logger.getLogger(Network2CysNetworkDB.class.getName());
    
    PhenumaWeb pw = new PhenumaWeb();

    Set<Integer> nodes = new HashSet<Integer>(); 
       
    
    /**
     * This methods generates JSON structure for a input network.
     * @param network
     * @return network in json structure
     * @throws PhenumaException 
     */
    public static String network2CysNetworkDB(NetworkDB network)
    {
        String cysnetwork = "";
        
        logger.info("Rows: "+network.getRows().size());
        
        /*Create nodes list*/
        
        if (network.getRows() == null || network.getRows().isEmpty())
        {
            cysnetwork =  "{" + dataSchema() +", ";          
            cysnetwork = cysnetwork + "data: { nodes: []}}";
        }
        else if (!NetworkUtils.isBipartite(network.getQuerytype()))
        {
            
            logger.info("Unipartite network");
            
            cysnetwork =  "{" + dataSchema() +", ";
            
            /**
             * Begin nodes section
             */
            cysnetwork = cysnetwork + "data: { nodes: [";

            
            /**
             * Add phenotypic profile node
             */
            if(NetworkUtils.isPhenotypeQuery(network.getQuerytype()))             
                cysnetwork = cysnetwork + cysNode(0, "Query", false, CytoscapeConstants.CYS_NODE_TYPE_QUERY)+", ";                
            
            /**
             * Add nodes 
             */
            StudySet nodes = network.getNodes();
             
            for (ByteString object : nodes.getAllNodesNames())
            {          
               System.out.println("network2CysNetworkDB: "+nodes.getGeneDescription(object));
               
               Integer nodeId = new Integer(object.toString());
               String  nodeName = nodes.getGeneDescription(object);
               boolean isInputNode  = CysNetworkUtils.isInput(object, network);
               Integer nodeType = CysNetworkUtils.getCysNodeType(object, network.getQuerytype());
               
               
               cysnetwork = cysnetwork + cysNode(nodeId, nodeName, isInputNode, nodeType)
                                       + ", ";
            }

            /**
             * End nodes section
             */
            cysnetwork = cysnetwork + "]}} ";
            

        }else {
            
            cysnetwork =  "{" + dataSchema() +", ";
            /* Nodes */
            cysnetwork = cysnetwork + "data: { nodes: [";

            //Input nodes
            
            StudySet input = network.getInputElements();

            for (ByteString object : input.getAllNodesNames())
            {
               cysnetwork = cysnetwork + cysNode(new Integer(object.toString()), 
                                                 input.getGeneDescription(object), 
                                                 true, 
                                                 CysNetworkUtils.getCysBipartiteNodeType(network.getInputtype()))+", ";

            }
            
            
            StudySet nodes = network.getNodesDiseases();
                          
            for (ByteString object : nodes.getAllNodesNames())
            {
               cysnetwork = cysnetwork + cysNode(new Integer(object.toString()), 
                                                 nodes.getGeneDescription(object), 
                                                 false, 
                                                 CysNetworkUtils.getCysBipartiteNodeType_Related(network.getQuerytype(), network.getInputtype()))
                            +", ";

            }

            
            cysnetwork = cysnetwork + "]}} ";
            
        }
        
        System.out.println("Created cytoscape web nodes");
        logger.info("Created cytoscape web nodes");
        return cysnetwork;

    }

    /**
     * Current schema. TODO It must be stored in constants declaration, configuration file...
     * @return 
     */
    public static String dataSchema(){
        CysDataSchema dataSchema = new CysDataSchema();
        
        //node attributes
        dataSchema.addNodeAttr("type", "number");
        dataSchema.addNodeAttr("label", "string");
        dataSchema.addNodeAttr("input", "number");
        
        //edge attributes
        dataSchema.addEdgeAttr("score", "number");
        dataSchema.addEdgeAttr("pvalue", "number");
        dataSchema.addEdgeAttr("type", "number");
        dataSchema.addEdgeAttr("isnew", "boolean");
       
        return dataSchema.getDataSchema();
    }

    
    /**
     * This method create a string for a dataschema field.
     * @param name
     * @param type
     * @return 
     */
    public static String cysEdge(String source, String target, String score, Double pvalue, Integer relationshipType, boolean isnew)
    {
        String cysEdge = "";
                
        String id = source+"*"+target+"*"+relationshipType;

        /*If the score of the relationship is not null*/
        if(score!=null) {
            cysEdge = "{ id: \"" + id + "\", "
                       + "target: \"" + target + "\", "
                       + "source: \"" + source + "\", "
                       + "score: " + score + ", "
                       + "pvalue: " + pvalue + ", "
                       + "type:" + relationshipType + ", "
                       + "isnew:" + isnew
                       + "}";
        }
        
        
        return cysEdge;
    }
    
    /**
     * 
     * @param network
     * @return 
     */
    public static String subNetworks(NetworkDB network){
        
        logger.info("Generating cytoscape subnetwork");
        
        String subnetworks = "";

        if (network.getRows() == null || network.getRows().isEmpty()) {
            return "{ simsemhpo: []}";
        }
            
        /*Map object which stores the subnetworks*/
        Map<Integer, String> subnetworkMap = new HashMap<Integer, String>();
        
        if (NetworkUtils.isPhenotypeQuery(network.getQuerytype()))
        {
            PhenotypeNetworkDB phenotypenetwork = (PhenotypeNetworkDB)network;
            
            for(Object[] row : phenotypenetwork.getResultRelationships())
            {
                 addPhenotypicQueryRelationship(subnetworkMap, row, network.getQuerytype());
            }
        }
       
        
        for(Object[] row : network.getRows()){
             
            
            if (!NetworkUtils.isBipartite(network.getQuerytype())) 
            {
                //Relationships     
                int i = NetworkUtils.getPositionFirstRelationship(network.getQuerytype());

                while(i <= NetworkUtils.getPositionLastRelationship(network.getQuerytype()))
                {
                    addRelationship(i, network, subnetworkMap, row);
                    i++;
                }
            }
            else
            {
                //Para orphan diseases las constantes tiene el mismo valor
                
                addRelationship(NetworkConstants.GENEOMIM_REL_KNOWN_POS, network, subnetworkMap, row);
                
                if (network.getInputtype() == NetworkConstants.INPUT_TYPE_GENES) {
                    addRelationship(NetworkConstants.GENEOMIM_REL_PHENOTYPIC_OMIM_POS, network, subnetworkMap, row);
                }
                else {
                    addRelationship(NetworkConstants.GENEOMIM_REL_PHENOTYPIC_GENE_POS, network, subnetworkMap, row);
                }
               
            }
        }
        
        
        //toString
        
        subnetworks = "{";
        Set<Integer> reltypeSet = subnetworkMap.keySet();
        for(Integer r : reltypeSet)
        {
            subnetworks = subnetworks + r +": ["+ subnetworkMap.get(r) +"], ";
        }
        if(!reltypeSet.isEmpty()) {
            subnetworks = subnetworks.substring(0, subnetworks.length()-2)+"}";
        }
        
     
        logger.info("Created cytoscape edgess");
        
        return subnetworks;
    }
    

    /**
     * This method create a string with the information of a node
     * in cytoscape networkmodel format:
     * 
     * {id=id, name="name", type="type"}
     * 
     * The fields depends on data schema defined
     * @param anObject
     * @return 
     */
    public static String cysNode(Integer objectID, String name, boolean isInput, Integer nodetype) 
    {

        DatabaseQueries q = new DatabaseQueries();
        
        String nodeStr = "";
        
        if(name == null || name.isEmpty())
        {
            if(nodetype.equals(CytoscapeConstants.CYS_NODE_TYPE_GENE)) {               
                try {
                    Gene g = q.getGeneByEntrezId(objectID.toString());
                    if(g!=null)
                        name = g.getSymbol();
                } catch (PhenumaException ex) {
                    name = objectID.toString();
                }
            }
            else if (nodetype.equals(CytoscapeConstants.CYS_NODE_TYPE_OMIM)) {
                Disease d = q.getDiseaseByOmim(objectID.toString());
                if(d!=null)
                {
                    try {
                        name = d.getName();
                    } catch (PhenumaException ex) {
                        name = objectID.toString();
                    }
                }

            }
            else if (nodetype.equals(CytoscapeConstants.CYS_NODE_TYPE_ORPHA)) {
                try {
                    RareDisease d = q.getRareDiseasesByOrphanum(objectID.toString());
                    if(d!=null)
                        name = d.getName();
                } catch (PhenumaException ex) {
                     name = objectID.toString();
                }
            }
            else if (nodetype.equals(CytoscapeConstants.CYS_NODE_TYPE_QUERY)) {
                name = "Query";
            }
        }
        
        
        Integer input = 0;
        if (isInput)
            input = 1;
        
        /* Build node */
        if(objectID!=null && nodetype!=null) {
            nodeStr = "{ id: \""+objectID+"\", input: "+input+", label:\""+name+"\", type: "+nodetype+"}";
        }
        
        System.out.println(nodeStr);
        return nodeStr;
        
    }    
    
    /**
     * 
     * @param i              relationship position
     * @param network        NetworkDB object: list of rows from the database, querytype, input type
     * @param subnetworkMap  Input subnetwork map Name --> subnetwork edges 
     * @param row            Current row
     * @param source         Source Gene
     * @param target       
     */
     private static void addRelationship(int i, NetworkDB network, Map<Integer, String> subnetworkMap, Object[] row) 
    {
        try
        {
            /**
             * Get source node Id.
             */
            Integer sourceNode = (Integer) row[NetworkConstants.DATABASE_MATRIX_OBJECTX_POS];
            
            /**
             * Get target node Id.
             */
            Integer targetNode = (Integer) row[NetworkConstants.DATABASE_MATRIX_OBJECTY_POS];
            
            /**
             * Get relationship type.
             */
            Integer relationshipType = CysNetworkUtils.getRelationshipTypeLabelDB(i, network.getInputtype(), network.getQuerytype());
                      
            /**
             * Get score value as string.
             */
            String score = getScore(row[i], relationshipType);
            
            /**
             * Get subnetwork.
             */                       
            String subnetwork = subnetworkMap.get(relationshipType);

            if(subnetwork == null) {
                subnetwork = "";
            }
            
            /**
             * Get novel relationships. 
             */           
            boolean isnew = false;
            
            if (i==NetworkConstants.GENEGENE_REL_PHENOTYPIC_POS) {
                isnew = NetworkUtils.isNewRelationship(row, network.getQuerytype());
            }
            
            
            if(new Double(score) > -1.0){
                subnetwork = subnetwork + " \n{ group:\"edges\", data: "
                                        + cysEdge(sourceNode.toString(), 
                                                  targetNode.toString(), 
                                                  score, 
                                                  0.0, 
                                                  CysNetworkUtils.getNetworkRelationshipTypeDB(i, network.getInputtype(), network.getQuerytype()), isnew)+"}, ";
            }


          
            if(!subnetwork.isEmpty()){
                subnetworkMap.put(relationshipType, subnetwork);
            }
            
        }
        catch(Exception ex)
        {
            System.out.println("Error add relationship "+i+" of "+row[1]+"\t"+row[2]+"\t"+row[i]+": "+ex.getMessage());
        }
    }

    /**
     * 
     * @param row
     * @param i
     * @return
     * @throws NumberFormatException 
     */ 
    private static String getScore(Object object, Integer relationshipType) throws NumberFormatException {
        /**
         * Get score value.
         */      
        String score = NumberUtils.object2string(object);
        
        if(score != null && !score.isEmpty())
        {
            Double score_d = NumberUtils.round(new Double(score),3);
            
            /**
             * This is bad.
             * This code is necessary for the version deployed in Sevilla, because 
             * the database is already deployed. To indicate that two diseases 
             * are not related we use the score = -1. But, there is a bug in
             * the database, and for relationships among rare diseases this 
             * score takes the value 0. So, in the network visualizer some
             * diseases appear related, however, there is not any relationship
             * among them.
             * This code is to correct this.
             */
            if(relationshipType == NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE)
            {
                if(score_d == 0)
                    score_d = -1.0;   
            }
            
            score = score_d.toString();
        }
        
        return score;
    }
     
     
     /**
     * 
     * @param i              relationship position
     * @param network        NetworkDB object: list of rows from the database, querytype, input type
     * @param subnetworkMap  inout subnetwork map Name --> subnetwork edges 
     * @param row            Current row
     * @param source         Source Gene
     * @param target       
     */
    private static void addPhenotypicQueryRelationship(Map<Integer, String> subnetworkMap, Object[] row, int querytype) 
    {
        try
        {
            String source = (String)row[0]; //"Query"
            String target = (String)row[1]; //Object: Disease/gene

         
            Integer reltype = NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC;
            
            String subnetwork = subnetworkMap.get(reltype);

            if(subnetwork == null) {
                subnetwork = "";
            }

           
            System.out.println(row[NetworkConstants.QUERYOBJECT_REL_PHENOTYPIC_POS]);
            
            String score = NumberUtils.object2string(row[NetworkConstants.QUERYOBJECT_REL_PHENOTYPIC_POS]);
            Double pvalue = new Double(NumberUtils.object2string(row[NetworkConstants.QUERYOBJECT_REL_PVALUE]));
            
            if(score!=null && !score.isEmpty()){
                subnetwork = subnetwork + " \n{ group:\"edges\", data: "
                                        + cysEdge(source, target, score, pvalue, NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC, false)+"}, ";
            }

          
            if(!subnetwork.isEmpty()){
                subnetworkMap.put(reltype, subnetwork);
            }
            

        }
        catch(Exception ex)
        {
            logger.info("Error add phenotype query relationship: "+row[0]+"\t"+row[1]+"\t"+row[2]+": "+ex.getMessage());
        }
    } 
    
    
}
