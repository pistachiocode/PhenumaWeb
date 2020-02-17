/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontologizer.query;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologizer.go.OBOParserException;
import ontologizer.go.TermID;
import ontologizer.types.ByteString;
import phenomizer.utils.OntologyUtils;

/**
 * This class execute a query for each phenotypic profile annotated in the input annotation file 
 * (this annotation file do not have to be the same file tothe annotation file setted with the current ontology).
 * 
 * The query are executed in the setted enviroment (ontology and annotationFile). 
 * 
 * The result is written in the outputFile. 
 *
 * 
 * @author Rocio
 */
public class MultipleQuery {
    
    private String annotationFile;
    private String outputFile;

    
    public MultipleQuery(String annotationFile) {
        this.annotationFile = annotationFile;
    }
   
    public String getAnnotationFile() {
        return annotationFile;
    }

    public void setAnnotationFile(String annotationFile) {
        this.annotationFile = annotationFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
    
    /**
     * This method execute all queries and write the result in a file.
     * 
     * TODO: It would be interesting return the result in a list of PhenomizerResultList.
     */
    public void executeQuery() {
        PrintWriter out = null;
        try {
            //Get map with the relationship between a phenotypic profile id and hpo term list
            Map<Integer, ArrayList<TermID>> id2hpolist = OntologyUtils.annotation2hpomap(this.annotationFile);
            Set<Integer> patients = id2hpolist.keySet();
            
            //Execute queries
            Iterator<Integer> iter = patients.iterator();
            out = new PrintWriter(new FileWriter(this.outputFile));
            
            int n = patients.size();
            int i = 1;
            while(iter.hasNext()) {
                //Create a new query    
                Integer id = iter.next();
                Query query = new Query(id2hpolist.get(id));

                out.write(">"+id.toString()+" \n");
                //Execute query
                PhenomizerResultList result = query.execute();
                out.write(result.toString());
                System.out.println(i+" de "+n);
                i++;
           }
            
            out.close();
            
        } catch (OBOParserException ex) {
            Logger.getLogger(MultipleQuery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MultipleQuery.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }
    
    
}
