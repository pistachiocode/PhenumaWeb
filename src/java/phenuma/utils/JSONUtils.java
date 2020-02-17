
package phenuma.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ontologizer.StudySet;
import ontologizer.calculation.AbstractGOTermProperties;
import ontologizer.calculation.EnrichedGOTermsResult;
import ontologizer.calculation.TermForTermGOTermProperties;
import ontologizer.go.Prefix;
import ontologizer.go.TermID;
import ontologizer.types.ByteString;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.entities.Disease;
import phenuma.entities.Gene;
import phenuma.entities.RareDisease;
import phenuma.network.Node;
import phenuma.networkproyection.NetworkConstants;
import phenuma.objects.PhenumaConstants;

/**
 * @author Rocío Rodríguez López
 */
public class JSONUtils {

    
    public static String createEnrichmentJSON(EnrichedGOTermsResult enrichment, Integer studySetType) throws PhenumaException
    {
        
        DatabaseQueries q = new DatabaseQueries(); 
        int datatype = 0;
        
        //Get study set id2name relationship
        Map<Integer, String> id2name = new HashMap<Integer,String>();
        StudySet ss = enrichment.getStudySet();
        for(ByteString id : ss)
        {
            if(studySetType == PhenumaConstants.ID_GENES_INPUT)
            {
                Gene g = q.getGeneByEntrezId(id.toString());
                id2name.put(new Integer(id.toString()), g.getSymbol());
                datatype = 1;
            }
            else if(studySetType == PhenumaConstants.ID_OMIM_INPUT)
            {
                Disease d = q.getDiseaseByOmim(id.toString());
                if(d!=null)
                {
                    id2name.put(new Integer(id.toString()), d.getName());
                }
                else
                {
                    id2name.put(new Integer(id.toString()), "");
                }
                datatype = 2;
            }
            else if(studySetType == PhenumaConstants.ID_ORPHANUM_INPUT)
            {
                RareDisease rd = q.getRareDiseasesByOrphanum(id.toString());
                if(rd!=null)
                {
                    id2name.put(new Integer(id.toString()), rd.getName());
                }
                else
                {
                    id2name.put(new Integer(id.toString()), "");
                }
                datatype = 3;
            }
                
         }
        

        //Create enrichment json
        String enrichment_json = "{enrichment: [";
        
        Iterator<AbstractGOTermProperties> iter = enrichment.iterator();

        while(iter.hasNext())
        {
            TermForTermGOTermProperties properties = (TermForTermGOTermProperties) iter.next();


            if (properties.p_adjusted < 1)
            {
                String nodeslist = "[";
                for(ByteString node : properties.getOmimlist())
                {
                    
                    String id = "";
                    String name = "";
                                        
                    
                    nodeslist = nodeslist + "{ id: '"+node.toString() + "', name: '"+id2name.get(new Integer(node.toString())) +"', type:"+datatype+"},";
                }

                nodeslist.substring(0, nodeslist.length()-3);
                nodeslist = nodeslist + "]";


                enrichment_json = enrichment_json + "{ termid: '"+  properties.goTerm.getID()+"'"+
                                                   ", termname: '"+properties.goTerm.getName().replace("'", "\\'") +"'"+
                                                   ", apo: '"+properties.annotatedPopulationGenes+"' "+
                                                   ", aso: '"+properties.annotatedStudyGenes+"' "+
                                                   ", pvalue: '"+Utils.double2scientificnotation(properties.p_adjusted)+"' "+
                                                   ", objects: "+nodeslist+"}, ";

            }
        }
        
        enrichment_json = enrichment_json + "]}";
        
        return enrichment_json;
    }
    
    
    public static String createSemanticJSON_DB(List<Object[]> rows, double d, Integer rel_type) {
       
        String relationships_json = "terms: [";
        
        for (Object[] row : rows)
        {
            
            Integer hpoid  = (Integer)row[2];
            String  hpomax = (String)row[3];
            Double  ic     = (Double)row[4];
            
            TermID term = new TermID(new Prefix("GO"), hpoid);
            if (rel_type == NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC)
                term = new TermID(new Prefix("HP"), hpoid);
            
            
            relationships_json = relationships_json + "{ id: '"+term.toString()+"', name: '"+hpomax+"', ic:"+ic+"}, ";
        
        }
        
        relationships_json = relationships_json.substring(0, relationships_json.length()-2);
        relationships_json = relationships_json +"]";
        
        return relationships_json;

    }
    
    /**
     * 
     * @param nodes
     * @param relationshipType
     * @return
     * @throws PhenumaException 
     */
    //Create HTML Tables
    public static String createInferredJSON_DB(Set<Node> nodes, Integer relationshipType) throws PhenumaException
    {
        String relationships_json = "objects: [";
        

        for (Node node : nodes){
            relationships_json = relationships_json + "{id: '"+node.getId()+"', name: '"+node.getText()+"'}, ";
        }

        relationships_json = relationships_json.substring(0, relationships_json.length()-2);
        relationships_json = relationships_json +"]";
        
        return relationships_json;
    }
    
}
