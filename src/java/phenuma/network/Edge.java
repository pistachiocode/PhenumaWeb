/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import phenuma.networkproyection.NetworkConstants;
import phenuma.networkproyection.NetworkUtils;

/**
 * This class represents a network item. A NetworkItem is a relationship between two elements of a network.
 * 
 * @author rocio
 */
public class Edge implements Comparable{
   
    private Node node1;
    private Node node2;
    
    protected Double score;
    protected Double pvalue;
    
    //Symmetric of Asymmetric
    private int type;
    
    private int relationship_type;
    
    protected boolean hiden;
    

    /**
     * Empty Edge
     */
    public Edge() {
        pvalue = 0.0;
        score  = 0.0;
    }

    /**
     * Edge from two nodes.
     * @param node1
     * @param node2 
     */    
    public Edge(Node node1, Node node2, int relationship_type) {
        this.node1 = node1;
        this.node2 = node2;
        this.relationship_type = relationship_type;
    }

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getPvalue() {
        return pvalue;
    }

    public void setPvalue(Double pvalue) {
        this.pvalue = pvalue;
    }
    

    /*SYMMETRIC OR ASYMMETRIC*/
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * Type or relationship displayed in the graph
     * 
     * @return 
     */
    public int getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(int relationship_type) {
        this.relationship_type = relationship_type;
    }
    
    
    public boolean isHiden() {
        return hiden;
    }

    public void setHiden(boolean hiden) {
        this.hiden = hiden;
    }
    
    
    
    /**
     * This methos check if two edges are equals. If the network
     * is symmetric a edge like A-B is equals to B-A. If the
     * network is asymmetric that elements are not the same.
     * 
     * @param o
     * @return 
     */
    
    @Override
    public boolean equals(Object o)
    {
        
        if(o instanceof Edge) {
            Edge item = (Edge)o;
            
            /*Compare elements*/
            boolean equalsElements = false;
            
//            if(type == NetworkConstants.ASYMMETRIC) {
//                /*In an asymmetric network the elements has to be at the same order.*/
//                equalsElements = node1.equals(item.node1) && node2.equals(item.node2);
//            } else {
                /*If the network is symmetric the order is not important*/
                equalsElements = node1.equals(item.node1) && node2.equals(item.node2) ||
                                 node1.equals(item.node2) && node2.equals(item.node1);
//            }
            
            
            /*Compare relationship type*/
            boolean equalRelationship = false;
            
            if(equalsElements){
                
                equalRelationship = (this.relationship_type == item.getRelationship_type());
            }
           
            /*Compare score*/
            if (equalsElements && equalRelationship){
                
                if(score!=null && item.score!=null){
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.node1 != null ? this.node1.hashCode() : 0);
        hash = 97 * hash + (this.node2 != null ? this.node2.hashCode() : 0);
        hash = 97 * hash + (this.score != null ? this.score.hashCode() : 0);
        return hash;
    }
    
    /**
     * @deprecated replace for cytoscape format
     * Network Item  to String
     * 
     * @return 
     */
    public String toString(){
        String networkItemStr;
        
        String element1name = node1.toString();
        if(node1.isInput())
            element1name =  element1name+"*";
        
        String element2name = node2.toString();
        if(node2.isInput())
            element2name =  element2name+"*";
        
        /*Build network item result*/
        if(score!=null){
            
            networkItemStr = element1name+"\t"+element2name+"\t"+Math.rint(score*1000)/1000;
            
            if(pvalue!=null)
                networkItemStr = networkItemStr+"\t"+pvalue;
            
            networkItemStr = networkItemStr+"\t"+relationship_type;
            
        }else
            networkItemStr = element1name+"\t"+element2name+"\t"+relationship_type;
       
        return networkItemStr;
        
    }
    
    
    public String edgeToSif(){
        
        String result = "";
        
        String element1name = node1.toString();       
        String element2name = node2.toString();
        String element1type = "";
        String element2type = "";
        
        
        if(node1 instanceof NodeGene)
            element1type = "gene";
        else if (node1 instanceof NodeDisease)
            element1type = "omim";
        else if (node1 instanceof NodeRareDisease)
            element1type = "oprhadata";
        else if (node1 instanceof NodeQuery)
            element1type = "query";
        
        
        if(node2 instanceof NodeGene)
            element2type = "gene";
        else if (node2 instanceof NodeDisease)
            element2type = "omim";
        else if (node2 instanceof NodeRareDisease)
            element2type = "oprhadata";
        else if (node2 instanceof NodeQuery)
            element2type = "query";
        
        
        /*Build network item result*/
        result = element1name+"\t"+element2name+"\t"+element1type+"\t"+element2type+"\t"+node1.isInput()+"\t"+node2.isInput()+"\t";
        if(score!=null){
            
            result = result+NetworkUtils.getRelationshipTypeNameById(relationship_type)+"\t"+Math.rint(score*1000)/1000;
            
            if(pvalue!=null)
                result = result+"\t"+pvalue;
            
        }else
            result = result+NetworkUtils.getRelationshipTypeNameById(relationship_type);
             
        
        return result;
        
        
    }

    @Override
    public int compareTo(Object t) {
        
        Edge edge = (Edge)t;
        
        if (this.getScore() == null || edge.getScore() == null)
            return 0;
                    
        return this.getScore().compareTo(edge.getScore());
    }
            
            
}
