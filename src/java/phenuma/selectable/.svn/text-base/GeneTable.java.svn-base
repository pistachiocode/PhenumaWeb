/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.selectable;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.primefaces.model.SelectableDataModel;
import phenomizer.hpo.Phenuma;
import phenuma.entities.AvaliableGeneSymbol;

/**
 *
 * @author Armando
 */
public class GeneTable  implements SelectableDataModel<SelectableObject> {
    
    static final Logger logger = Logger.getLogger(GeneTable.class.getName());
    
    private ArrayList<SelectableObject> genes;
    
    public GeneTable(){
        
        genes = new ArrayList<SelectableObject>();
        
        List<AvaliableGeneSymbol> genelist = Phenuma.getInstance().getGeneslist();
        
        for (AvaliableGeneSymbol g : genelist){
            try {
              // genes.add(new SelectableObject(new Integer(g.getGene()).toString(), g.getSymbol()));
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        
        
    }

    public ArrayList<SelectableObject> getTerms() {
        return genes;
    }

    public void setTerms(ArrayList<SelectableObject> genes) {
        this.genes = genes;
    }
    
    
    @Override
    public Object getRowKey(SelectableObject g) {
        return g.getName();
    }

    @Override
    public SelectableObject getRowData(String string) {
        
        for(SelectableObject g : this.genes) {  
            if(g.getName().equals(string))  
                return g;

        }  
          
        return null;  
    }


    
    
}
