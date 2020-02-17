/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import phenuma.network.EdgeSemanticInfo;

/**
 *
 * @author Rocío Rodríguez López
 */
public class TableEdge  implements Serializable,Comparable {
    
    private String id;
    private TableNode source;
    private TableNode target;
    private double score;
    private double pvalue;
    
    private String relationship_type;
    private String color;
    
    
    /**
     * List of objects involve in the relationship. 
     * 
     *  Phenotypic Relationship --> HPO terms
     *  Functional Relationship --> GO terms
     *  Inferred --> Disease, ER or Genes
     */
    private List<TableNode> objects;
    
    private List<TableNode> info; 

    public TableEdge() {
        objects = new ArrayList<TableNode>();
        info = new ArrayList<TableNode>();    
    }
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TableNode> getObjects() {
        return objects;
    }

    public void setObjects(List<TableNode> objects) {
        this.objects = objects;
    }

    public double getPvalue() {
        return pvalue;
    }

    public void setPvalue(double pvalue) {
        this.pvalue = pvalue;
    }

    public String getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(String relationship_type) {
        this.relationship_type = relationship_type;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public TableNode getSource() {
        return source;
    }

    public void setSource(TableNode source) {
        this.source = source;
    }

    public TableNode getTarget() {
        return target;
    }

    public void setTarget(TableNode target) {
        this.target = target;
    }

    public List<TableNode> getInfo() {
        return info;
    }

    public void setInfo(List<TableNode> info) {
        this.info = info;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    
    

    @Override
    public int compareTo(Object t) {
        
        TableEdge e = (TableEdge)t;
        
        if (this.pvalue < e.pvalue)
            return -1;
        else if (this.pvalue > e.pvalue)          
            return 1;
        else 
            return 0;
        
    }
    
    
}
