/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import ontologizer.go.Term;
import phenuma.constants.Constants;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Armando
 */
public class NodeTerm extends Node {
    
    public NodeTerm(Term element) {
        super(element);
    }
    
    public NodeTerm(Term element, boolean input) {
        super(element, input);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof NodeTerm){
            
            NodeTerm nodeterm = (NodeTerm)o;
            
            return nodeterm.element.equals(this.element);
        }
        
        return false;
        
    }
        
    @Override
    public String toString()
    {
        Term term = (Term)element;
        
        return term.getID().toString();
    }   
    
    
    public String getLink() {
        Term term = (Term)element;
        
        return Constants.HPO_LINK+term.getIDAsString();
    }


    public String getText() throws PhenumaException {
        Term term = (Term)element;
        
        return term.getName();
    }

    @Override
    public String getId() {
        Term term = (Term)element;
        
        return term.getIDAsString();
    }
    
    
}
