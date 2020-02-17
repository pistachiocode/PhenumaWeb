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
import phenuma.entities.Disease;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Armando
 */
public class OMIMTable implements SelectableDataModel<SelectableObject>{
    
    static final Logger logger = Logger.getLogger(OMIMTable.class.getName());
    
    private List<SelectableObject> diseases;
    
    public OMIMTable(){
        diseases = new ArrayList<SelectableObject>();
        
        List<Disease> diseaselist = Phenuma.getInstance().getDiseaselist();
        
        for (Disease d : diseaselist){
            try {
                diseases.add(new SelectableObject(d.getOmim().toString(), d.getName()));
            } catch (PhenumaException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    public List<SelectableObject> getTerms() {
        return diseases;
    }

    public void setTerms(ArrayList<SelectableObject> diseases) {
        this.diseases = diseases;
    }
    
    
    @Override
    public Object getRowKey(SelectableObject d) {
        return d.getName();
    }

    @Override
    public SelectableObject getRowData(String string) {
        
        for(SelectableObject d : this.diseases) {  
            if(d.getName().equals(string))  
                return d;

        }  
          
        return null;  
    }

    
}
