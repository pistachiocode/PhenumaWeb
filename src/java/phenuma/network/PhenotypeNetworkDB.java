/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologizer.go.TermID;
import phenomizer.utils.NumberUtils;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Armando
 */
public class PhenotypeNetworkDB extends NetworkDB{

    static final Logger logger = Logger.getLogger(PhenotypeNetworkDB.class.getName());
    
    ArrayList<TermID> queryTerms;
    
    List<Object[]> resultRelationships;

    
    
    public PhenotypeNetworkDB() {
        
        super();
    }

    public List<Object[]> getResultRelationships() {
        return resultRelationships;
    }

    public void setResultRelationships(List<Object[]> resultRelationships) {
        this.resultRelationships = resultRelationships;
    }

    public ArrayList<TermID> getQueryTerms() {
        return queryTerms;
    }

    public void setQueryTerms(ArrayList<TermID> queryTerms) {
        this.queryTerms = queryTerms;
    }


    public String toStringQuery()
    {
        DatabaseQueries q = new DatabaseQueries();
        
        String str = null;
        
        for (Object[] row : this.resultRelationships)
        {
            try {
            
                str = str + "Query\t" + q.getGeneByEntrezId((String)row[1]).getSymbol() + "\t" + NumberUtils.object2string(row[2]) + "\t" +  NumberUtils.object2string(row[3]) + "\n";
            
            } catch (PhenumaException ex) {
                Logger.getLogger(PhenotypeNetworkDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return str;
    }
    
    
    @Override
    public String toString () 
    {
        String str =  "# Query relationships: \n"
                   + "# - Columns: Query Gene/Disease Score P-Value \n"
                   + "# - Input: ";
        
        
        DatabaseQueries q = new DatabaseQueries();
        
        for (TermID t : this.getQueryTerms())
        {
            str = str + t.toString()+", ";
        }

        str =  str.substring(0, str.length()-2) + "\n" + super.toString();

        
        for (Object[] row : this.resultRelationships)
        {
            try {
            
                str = str + "Query\t" + q.getGeneByEntrezId((String)row[1]).getSymbol() + "\t" + NumberUtils.object2string(row[2]) + "\t" +  NumberUtils.object2string(row[3]) + "\n";
            
            } catch (PhenumaException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(PhenotypeNetworkDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return str;
    }
    
}
