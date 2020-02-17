/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import java.util.ArrayList;
import ontologizer.go.TermID;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Armando
 */
public class NodeQuery extends Node {
    
    private ArrayList<TermID> terms;

    public NodeQuery(ArrayList<TermID> terms) {
        this.terms = terms;
        this.text = "Query";
    }
    
    public NodeQuery(ArrayList<TermID> terms, boolean input) {
        super(terms, input);
    }

    public ArrayList<TermID> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<TermID> terms) {
        this.terms = terms;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.terms != null ? this.terms.hashCode() : 0);
        return hash;
    }
    
    public boolean equals(Object node){
        NodeQuery nodequery = (NodeQuery)node;
        
        return nodequery.hashCode() == this.hashCode();
    }
    
    
    @Override
    public String getLink() {
        return "";
    }

    @Override
    public String getId() {
        return "Query";
    }

    @Override
    public String getText() throws PhenumaException {
        return "Query";
    }
    
    public String toString(){
        return "Query";
    }
    
    
    
    
}