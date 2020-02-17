/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import ontologizer.calculation.AbstractGOTermProperties;
import ontologizer.calculation.EnrichedGOTermsResult;
import ontologizer.calculation.TermForTermGOTermProperties;
import ontologizer.go.Prefix;
import ontologizer.go.TermID;
import ontologizer.types.ByteString;

/**
 *
 * @author Rocío Rodríguez López
 */

public class Utils {
    
    /*
     * List of identifiers to string separated for spaces.
     */
    public static String inputToString(List<String> object) 
    {
        String str = "";
        
        if(object == null)
            return str;
                    
        for(String s : object)
        {
            str = str + s  +" ";
        }
        
        str = str.trim();
        
        return str;
    }
    
    public static String intToHpoId(Integer id)
    {
        TermID termid = new TermID(new Prefix("HP"), id);
        return termid.toString();
    }
    
    public static boolean isSymbol(String gene)
    {
        return Pattern.matches("[a-zA-Z]+[a-zA-Z0-9]*", gene);
    }
    
    public static String double2scientificnotation(double d)
    {
        NumberFormat formatter = new DecimalFormat();
        formatter = new DecimalFormat("0.##E0");
        return formatter.format(d); 
    }
    
    public static String currentDateTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
   
    
    public static String enrichment2string(EnrichedGOTermsResult enrichment) {
        String data = "";
        
        if(enrichment != null)
        {
            data = "# Phenotypical Enrichment \n"+
                          "# Study Set: "+enrichment.getStudySet().toString()+"\n"+
                          "# Study Set Size "+enrichment.getStudyGeneCount()+"\n"+
                          "# Population Size "+enrichment.getPopulationGeneCount()+"\n"+
                          "# HPO id\tHPO Name\tAnnotated Study\tAnnotated Population\tP-Value Ajusted (Bonferroni)\tObjects\n";

            Iterator<AbstractGOTermProperties> iter = enrichment.iterator();
            while(iter.hasNext())
            {
                TermForTermGOTermProperties properties = (TermForTermGOTermProperties) iter.next();


                if (properties.p_adjusted < 0.05)
                {
                    String nodeslist = "[";
                    for(ByteString node : properties.getOmimlist())
                    {
                        nodeslist = nodeslist + "\'" + node.toString() + "\',";
                    }

                    nodeslist.substring(0, nodeslist.length()-3);
                    nodeslist = nodeslist + "]";


                    data = data + properties.goTerm.getID()   + "\t" 
                                + properties.goTerm.getName() + "\t" 
                                + properties.annotatedStudyGenes + "\t" 
                                + properties.annotatedPopulationGenes + "\t" 
                                + Utils.double2scientificnotation(properties.p_adjusted) + "\t"
                                + properties.omimlist.toString().replace("[","").replace("]","") + "\n";

                }
            }
        }
        return data;
    }
    
    
}
