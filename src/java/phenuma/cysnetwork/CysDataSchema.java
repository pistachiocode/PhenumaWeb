/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.cysnetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Armando
 */
public class CysDataSchema {
    Map<String,String> nodeAttrs = new HashMap<String,String>();
    Map<String,String> edgeAttrs = new HashMap<String,String>();
    
    public CysDataSchema(){
        
    }
    
    
    public void addNodeAttr(String name, String type){
        this.nodeAttrs.put(name, type);
    }
    
    public void addEdgeAttr(String name, String type){
        this.edgeAttrs.put(name, type);
    }
    
    
    
    public String getDataSchema(){
        String dataSchema = "dataSchema: { nodes: [";
        
        
        Set<String> nodes = nodeAttrs.keySet();
        
        int i = 0;
        for(String n : nodes)
        {
            dataSchema = dataSchema + cysField(n,this.nodeAttrs.get(n));
            
            if(i<nodes.size()-1)
                dataSchema = dataSchema +", ";
            i++;
        }
        dataSchema = dataSchema+"],";
        
        //Edges attributes
        Set<String> edges = edgeAttrs.keySet();
        
        dataSchema = dataSchema+" edges: [";
        
        int j = 0;
        for(String n : edges){
            dataSchema = dataSchema + cysField(n,this.edgeAttrs.get(n));
            
            if(j<edges.size()-1)
                dataSchema = dataSchema +", ";
            j++;
        }
                
        dataSchema = dataSchema+"]}";
        
        return dataSchema;
    }
    
    /**
     * This method create a string for a dataschema field.
     * @param name
     * @param type
     * @return 
     */
    private static String cysField(String name, String type)
    {
        String field = null;
        
        if(name!=null && type!=null)
            field = "{ name: \""+name+"\",  type: \""+type+"\"}";
        
        return field;
    }
            
    
}
