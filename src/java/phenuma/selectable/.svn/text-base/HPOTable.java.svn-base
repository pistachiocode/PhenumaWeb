/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.selectable;

import java.io.IOException;
import java.util.ArrayList;
import ontologizer.go.OBOParserException;
import ontologizer.go.Term;
import org.apache.log4j.Logger;
import org.primefaces.model.SelectableDataModel;
import phenomizer.hpo.Phenuma;

/**
 *
 * @author Armando
 */
public class HPOTable implements SelectableDataModel<SelectableObject>{

    static final Logger logger = Logger.getLogger(HPOTable.class.getName());
       
    private ArrayList<SelectableObject> terms;
    
    public HPOTable(){
        
        terms = new ArrayList<SelectableObject>();
        
        try {
            ArrayList<Term> termlist = Phenuma.getInstance().getHuman_phenotype_ontology().getTermsInTopologicalOrder();
            
            for (Term t : termlist)
                terms.add(new SelectableObject(t.getIDAsString(), t.getName()));
                   
            
            
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } catch (OBOParserException ex) {
            logger.error(ex.getMessage());
           
        }
    }

    public ArrayList<SelectableObject> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<SelectableObject> terms) {
        this.terms = terms;
    }
    
    
    @Override
    public Object getRowKey(SelectableObject t) {
        return t.getName();
    }

    @Override
    public SelectableObject getRowData(String string) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        ArrayList<SelectableObject> terms = this.terms;
          
        for(SelectableObject t : terms) {  
            if(t.getName().equals(string))  
                return t;  
        }  
          
        return null;  
    }
    
}
