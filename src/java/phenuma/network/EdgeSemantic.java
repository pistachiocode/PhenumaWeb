/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Armando
 */
public class EdgeSemantic extends Edge {
    
    private List<EdgeSemanticInfo> edgeinfo;

    public EdgeSemantic() {
        super();
        this.edgeinfo = new ArrayList<EdgeSemanticInfo>();
    }
    
    public EdgeSemantic(Node node1, Node node2, int relationship_type) {
        super(node1, node2, relationship_type);
        this.edgeinfo = new ArrayList<EdgeSemanticInfo>();
    }
    
    public List<EdgeSemanticInfo> getMaximumTerm2IC() {
        return edgeinfo;
    }

    public void setMaximumTerm2IC(List<EdgeSemanticInfo> edgeinfo) {
        if(edgeinfo!=null)
            this.edgeinfo = edgeinfo;
    }

    @Override
    public String toString() {
        
        
//        String str = "";
//        
//        for(EdgeSemanticInfo info : edgeinfo){
//            
//            str = str + super.toString()+"\t"+info.getTermX()+"\t"+info.getTermY()+"\t"+info.getTermMax()+"\t"+info.getScore() + "\n";
//            
//        }
//        
//        
        return super.toString();
    }
    
    
    
     
    
    
    

}
