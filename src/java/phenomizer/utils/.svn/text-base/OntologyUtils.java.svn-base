/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenomizer.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologizer.go.Ontology;
import ontologizer.go.Term;
import ontologizer.go.TermID;
import ontologizer.types.ByteString;
import phenomizer.hpo.Phenomizer;

/**
 *
 * @author Armando
 */
public class OntologyUtils {
    //Field position
    public static final int ID = 1;
    public static final int HPOTERM = 4;
   
     /**
     * Get mapping between each queried element name and its list of hpo terms. :S
     */

    public static Map<Integer, ArrayList<TermID>> annotation2hpomap(String annotationFile){

        Map<Integer, ArrayList<TermID>> id2hpolist = new HashMap<Integer, ArrayList<TermID>>();


        int numline = 0;

        try
        {

            BufferedReader is = new BufferedReader(new FileReader(annotationFile));
            String line = is.readLine();

            while(line!=null)
            {
                if(!line.startsWith("#"))
                {
                    /**
                     * Get values from file
                     */
                    String[] str = line.split("[\t]");

                    Integer id = new Integer(str[ID]);
                    String hpoterm = str[HPOTERM];

                    //get TermID object of hpo term
                    TermID termid = new TermID(hpoterm);

                    //Check if the term exists in the ontology
                    Ontology ontology = Phenomizer.getInstance().getOntology();
                    Term term = ontology.getTerm(termid);

                    if (Phenomizer.getInstance().getOntology().getTermsInTopologicalOrder().contains(term))
                    {
                        //Add the term to the map.

                        ArrayList<TermID> termlist = id2hpolist.get(id);

                        if(termlist != null)
                        {
                            if (!termlist.contains(termid)){
                                termlist.add(termid);
                                id2hpolist.put(new Integer(id.toString()), termlist);
                            }
                        }
                        else
                        {               
                            termlist = new ArrayList<TermID>();
                            termlist.add(termid);

                            id2hpolist.put(new Integer(id.toString()), termlist);

                        }
                    }
                }

                line = is.readLine();
                numline++;
            }
            
//            
//            Set<Integer> keyset = id2hpolist.keySet();
//            Iterator<Integer> iter = keyset.iterator();
//            while(iter.hasNext()) {
//                Integer i = iter.next();
//                ArrayList<TermID> list = id2hpolist.get(i);
//                
//                System.out.println(i.toString()+" query size "+list.size());
//            }
            
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage()+" error en la linea "+numline);
        }
        
        
       
        
        return id2hpolist;

    }




    
}
