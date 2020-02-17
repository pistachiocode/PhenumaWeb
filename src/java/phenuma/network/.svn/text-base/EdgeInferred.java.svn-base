/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import java.util.HashSet;
import java.util.Set;
import phenuma.networkproyection.NetworkConstants;

/**
 *
 * @author Armando
 */
public class EdgeInferred extends Edge{
    
    private Set<Node> commonNodes;
    
    public EdgeInferred(Node node1, Node node2, int relationship_type){
        super(node1, node2, relationship_type);
        this.score = 0.0;
        commonNodes = new HashSet<Node>();
    }

    public Set<Node> getCommonNodes() {
        return commonNodes;
    }

    /**
     * This method set the common nodes of a inferred relationship.
     * The score of a relationship is the number of nodes
     * involved on it.
     * 
     * @param commonNodes 
     */
    public void setCommonNodes(Set<Node> commonNodes) {
        this.commonNodes = commonNodes;
        /**
         * Add score of relationships
         */
        this.setScore(new Double(commonNodes.size()));
    }
    
    
    public void addCommonNode(Node node){
        commonNodes.add(node);
        this.score++;    
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.commonNodes != null ? this.commonNodes.hashCode() : 0);
        return hash;
    }
    
    
    
    @Override
    public boolean equals(Object o){
    
        if(o instanceof EdgeInferred) {
            EdgeInferred item = (EdgeInferred)o;
            
            /*Compare elements*/
            boolean equalsElements = false;
            
//            if(super.getType() == NetworkConstants.ASYMMETRIC) {
                /*In an asymmetric network the elements has to be at the same order.*/
                equalsElements = super.getNode1().equals(item.getNode1()) && super.getNode2().equals(item.getNode2());
//            } else {
//                /*If the network is symmetric the order is not important*/
//                equalsElements = super.getNode1().equals(item.getNode1()) && super.getNode2().equals(item.getNode2()) ||
//                                 super.getNode1().equals(item.getNode2()) && super.getNode2().equals(item.getNode1());
//            }
            
            
            /*Compare relationship type*/
            boolean equalRelationship = false;
            
            if(equalsElements){
                
                equalRelationship = (super.getRelationship_type() == item.getRelationship_type());
            }
           
            /*Compare score*/
            if (equalsElements && equalRelationship){
                
                if(score!=null){
                    double score1 = Math.rint(score.doubleValue()*1000)/1000;
                    double score2 = Math.rint(item.score.doubleValue()*1000)/1000;
                
                    return score1==score2;
                }
                
                /*The nodes of the edge are equals and the edge has not score.*/
                return true;
            }
                        
        }
        
        /*The object is not a network item*/
        return false;
    }
    
    /**
     * @deprecated 
     * @return 
     */
    @Override
    public String toString(){
        String str = super.toString();
    
        for(Node n : commonNodes)
        {
            str = str + "\t" + n.getId()+"\t";
        }
        
        return str;
    }
    
    @Override
    public String edgeToSif(){
        
        String result = super.edgeToSif();
        
        String commonNodesCol = "";
        
        for(Node n : commonNodes)
        {
            commonNodesCol = commonNodesCol + n.getId()+",";
        }
        
        commonNodesCol = commonNodesCol.substring(0, commonNodesCol.length()-1); 
        
        return result+"\t"+commonNodesCol;
        
        
    }
    
}
