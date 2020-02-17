/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import phenuma.constants.Constants;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.entities.Disease;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Armando
 */
public class NodeDisease extends Node {

    public NodeDisease(Disease element) {
        super(element);
    }
    
    public NodeDisease(Disease element, boolean input){
        super(element, input);
    }
    
        
    @Override
    public int hashCode() {
        return super.hashCode();
    }
        
    
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof NodeDisease){
            
            NodeDisease nodedisease = (NodeDisease)o;
            
            return nodedisease.element.equals(this.element);
        }
        
        return false;
        
    }
    
    
    @Override
    public String toString()
    {
        Disease disease = (Disease)element;
        
        return disease.getOmim().toString();
    }

    public String getLink() {
        Disease disease = (Disease)element;
        
        return Constants.OMIM_LINK+disease.getOmim();
    }


    public String getText() throws PhenumaException {
        Disease disease = (Disease)element;
        
        DatabaseQueries q = new DatabaseQueries();
        
        //disease = q.getDiseaseByOmim(this.getId());
        
        return disease.getName();
    }

    @Override
    public String getId() {
        Disease disease = (Disease)element;
        
        return disease.getOmim().toString();
    }
    
    
}
