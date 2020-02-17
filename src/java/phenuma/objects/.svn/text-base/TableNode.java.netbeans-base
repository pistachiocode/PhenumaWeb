/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Armando
 */
public class TableNode {

    private String text;
    private String link;
    private String id;

    private double pvalue; 
    private double value;
    private String color;
    
    private List<TableNode> nodes;
    private List<TableEdge> edges;

    
    public TableNode(String text, String link, String id) {
        this.text = text;
        this.link = link;
        this.id = id;

        //nodes = new ArrayList<TableNode>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getPvalue() {
        return pvalue;
    }

    public void setPvalue(double information) {
        this.pvalue = information;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    

    public void addChild(TableNode node){
        if (nodes == null)
          nodes = new ArrayList<TableNode>();  
        
        nodes.add(node);
    }

    
    
    public void addEdge(TableEdge edge){
        if (edges == null)
          edges = new ArrayList<TableEdge>();  
        
        edges.add(edge);
    }
    
    public List<TableNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TableNode> nodes) {
        this.nodes = nodes;
    }

    public List<TableEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<TableEdge> edges) {
        this.edges = edges;
    }
    
    

    public boolean equals(TableNode node){
        return this.id == node.getId();
    }

}
