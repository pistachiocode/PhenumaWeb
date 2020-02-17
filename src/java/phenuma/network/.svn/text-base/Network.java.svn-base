/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import phenuma.network.Edge;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A network is a list of network items.
 * 
 * @author Rocio
 */
public class Network{
    
    private List<Edge> edges;
    private List<Edge> hidenEdges;
    
    /**
     * Set of nodes of the network. This set is used to make easier the
     * generation of networks in cytoscapeweb format. The nodes of the rigth
     * and the left are separated to allow the inference of relatioship
     * easier.
     * 
     */
    private Set<Node> nodesRight;
    private Set<Node> nodesLeft;
    
    
    private int type;

    /**
     * Type is ASYMETRIC OR SYMETRIC. The type attribute is used to compare
     * network items. If the network is symmetric A - B == B - A in otherwise 
     * are not equals.
     * 
     * @param type 
     */
    public Network(int type){
        this.edges = new ArrayList<Edge>();
        
        this.nodesLeft = new HashSet<Node>();
        this.nodesRight = new HashSet<Node>();
        
        this.type = type;
    }
    
    public Network(List<Edge> network, int type) {
        this.edges = network;
        this.type = type;
    }


    public List<Edge> getNetwork() {
        return edges;
    }

    public void setNetwork(List<Edge> network) {
        this.edges = network;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Set<Node> getNodesLeft() {
        return nodesLeft;
    }
    
    public Set<Node> getNodesRight() {
        return nodesRight;
    }
    
    public Set<Node> getNodes()
    {
        Set<Node> allNodes = new HashSet<Node>();
        allNodes.addAll(nodesLeft);
        allNodes.addAll(nodesRight);
        
        return allNodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public List<Edge> getHidenEdges() {
        return hidenEdges;
    }

    public void setHidenEdges(List<Edge> hidenEdges) {
        this.hidenEdges = hidenEdges;
    }
    
    
    
    /**
     * This method returns the set of nodes related with a input node.
     * @param n
     * @return 
     */
    public Set<Node> getNodesOf(Node n)
    {
        Set<Node> nodes = new HashSet<Node>();
        
        for(Edge edge : edges)
        {
            if(edge.getNode1().equals(n))
                nodes.add(edge.getNode2());
            else if (edge.getNode2().equals(n))
                nodes.add(edge.getNode1());

        }
        
        return nodes;
    }
   
    
    public void add(Edge item){
        
        item.setType(this.type);
        
        /*Get the nodes*/
        
        nodesLeft.add(item.getNode1());
        nodesRight.add(item.getNode2());
        
        if(!this.contains(item))
            edges.add(item);

    }
    
    /**
     * Add the networkitems if the input to the main network.
     * 
     * @param network 
     */
    public void concat(Network network){
        
        if (network!=null)
        {
            Iterator<Edge> iter = network.getNetwork().iterator();
            while(iter.hasNext())
            {
                Edge item = iter.next();
            
                this.add(item);
        
            }
        }
        
    }   
    
    public boolean contains(Edge edge){
        return edges.contains(edge);
    }
    
    
    /**
     * Two networks are equals if contain the same elements. 
     * The order of the items is not important.
     */
    @Override
    public boolean equals(Object o){
        
        Network n = (Network)o;
        
        if(this.edges.size() == n.edges.size()){
            
            int i = 0;
            boolean eq = true;
            
            while(i<this.edges.size() && eq){
                
                Edge item = n.edges.get(i);
                
                eq = this.edges.contains(item);
                
                i++;
            }
            
            return (i>=this.edges.size() && eq);
            
        
        }else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.edges != null ? this.edges.hashCode() : 0);
        return hash;
    }
    

    /**
     * Network to String. Format:
     * 
     * node1 node2 score12
     * node1 node3 score13
     * ...
     * 
     * If the links have not score the format is:
     * 
     * node1 node2
     * node1 node3
     * ...
     * 
     * 
     * @return 
     */
    @Override
    public String toString(){
        String str = "";
        
        Iterator<Edge> iter = edges.iterator();
        while(iter.hasNext()){
            Edge item = iter.next();

            /*If we want to print the score, its must to be greater than 0. In other case we print all items.*/
           // if (item.getScore()!=null && item.getScore()>0)
                str = str + item.toString()+"\n";

        }
        
        return str;
    }
   
   
    public String toTextFile(){
        String str = "";
        
        Iterator<Edge> iter = edges.iterator();
        while(iter.hasNext()){
            Edge item = iter.next();

            /*If we want to print the score, its must to be greater than 0. In other case we print all items.*/
           // if (item.getScore()!=null && item.getScore()>0)
                str = str + item.toString() +"\n";

        }
        
        return str;
    }
   
    
    
    /**
     * Get the greater score of a relationship_type
     * @param relationship_type
     * @return 
     */
    public double getMaxScore(int relationship_type)
    {
        double max = 0.0;
        
        for(Edge edge : this.edges)
        {
            if(edge.score!=null && edge.getRelationship_type() == relationship_type && edge.score > max)
                max = edge.score;
            
        }
        
        return max;
    }
    
     /**
     * Get the greater score of a relationship_type
     * @param relationship_type
     * @return 
     */
    public double getMinScore(int relationship_type)
    {
        double min = this.getMaxScore(relationship_type);
        
        for(Edge edge : this.edges)
        {
            if(edge.score!=null && edge.getRelationship_type() == relationship_type && edge.score < min)
                min = edge.score;
            
        }
        
        return min;
    }
    

    /**
     * Store the network in a file. Format:
     * 
     * node1 node2 score12
     * node1 node3 score13
     * ...
     * 
     * If the links have not scoro the format is:
     * 
     * node1 node2
     * node1 node3
     * ...
     * 
     * @param file
     * @param printScore
     * @throws IOException 
     */
    public void writeToFile(String file) throws IOException {
        
        PrintWriter out = new PrintWriter(new FileWriter(file));

        out.write(this.toString()+"\n");

        out.close();
    }


    
}
