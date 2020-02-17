/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import phenuma.constants.Constants;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.entities.Gene;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Armando
 */
public class NodeGene extends Node{
    
    public NodeGene(Gene element) {
        super(element);
    }
    
    public NodeGene(Gene element, boolean input){
        super(element, input);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof NodeGene){
            
            NodeGene nodegene = (NodeGene)o;
            
            return nodegene.element.equals(this.element);
        }
        
        return false;
        
    }
       
    @Override
    public String toString()
    {
        Gene gene = (Gene)element;

        return gene.getEntrezid().toString();
    }
    
    public String getLink() {
        Gene gene = (Gene)element;
        
        return Constants.GENE_LINK+gene.getEntrezid();
    }


    public String getText() throws PhenumaException {
        Gene gene = (Gene)element;
        
      //  DatabaseQueries q = new DatabaseQueries();
        
     //   gene = q.getGeneByEntrezId(gene.getEntrezid().toString());
        
        return gene.getSymbol();
    }

    @Override
    public String getId() {
        Gene gene = (Gene)element;
        
        return gene.getEntrezid().toString();
    }
    


}
