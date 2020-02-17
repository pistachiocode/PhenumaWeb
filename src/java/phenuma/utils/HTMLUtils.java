/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.utils;

import java.util.List;
import java.util.Set;
import ontologizer.go.Prefix;
import ontologizer.go.TermID;
import phenomizer.utils.NumberUtils;
import phenuma.constants.Constants;
import phenuma.network.EdgeSemanticInfo;
import phenuma.network.Node;
import phenuma.networkproyection.NetworkConstants;
import phenuma.objects.PhenumaConstants;

/**
 *
 * @author Rocío Rodríguez López
 */
public class HTMLUtils {
    
   
    public static String createPhenotypeTable(List<Object[]> terms){
        
               
        String table = "<div style=\"height:250px;width:100%;overflow:scroll;\">";

        if(!terms.isEmpty())
        {
            table =  table +"<table id=\'relationshiptable\' class=\'phenumatable\'> <thead><tr class=\"phenumarow\"> "
                    + "<th onclick=\"SortTable(0, \'relationshiptable\', 'hpoid', 'text');\">HPO ID</th> "
                    + "<th onclick=\"SortTable(1, \'relationshiptable\', 'hponame', 'text');\">HPO Name</th> "
                    + "<th onclick=\"SortTable(2, \'relationshiptable\', 'ic', 'float');\">IC</th> "
                    + "<th>Link</th> "
                    + "</tr></thead><tbody>";

            for(Object[] row : terms)
            {
                table = table + "<tr class=\"phenumarow\" style=\"background-color:"+getColor((Double)row[2]) +";\">"
                        + "<td class='phenumadata'>"+Utils.intToHpoId((Integer)row[0]) +"</td>"
                        + "<td class='phenumadata_text'>"+(String)row[1]+"</td>"
                        + "<td class='phenumadata'>"+NumberUtils.round((Double)row[2],3)+"</td>"
                        + "<td class='phenumadata'><a href=\""+Constants.HPO_LINK+Utils.intToHpoId((Integer)row[0])+"\" target=\"_blank\"><img src=\"./resources/img/world_link.png\"></img></a></td>"
                        + "</tr>";
            }

            table = table + "</tbody></table></div>";
        }
        else
        {
            table = table + "There is not terms related with the selected object.";
        }
        
        return table;
    } 
    
    /**
     * 
     * @param nodes
     * @param relationshipType
     * @return
     * @throws PhenumaException 
     */
    //Create HTML Tables
    public static String createInferredDataTable(Set<Node> nodes, Integer relationshipType) throws PhenumaException
    {
       String table = "";
        if(nodes != null)
        {

            table = "<div style=\"height:250px;width:100%;overflow:scroll;\">"
                  + "<table id=\"reltable\" class=\"phenumatable\"> <thead><tr class=\"phenumarow\"> "
                  + "<th onclick=\"SortTable(0, \'reltable\', 'id', 'text');\">ID</th> "
                  + "<th onclick=\"SortTable(1, \'reltable\', 'name', 'text');\">Name</th> "
                  + "<th>Link</th>"
                  + "</tr></thead><tbody>";


            for (Node node : nodes){
                table = table + "<tr class=\"phenumarow\">"
                              + "<td class='phenumadata'>"+node.getId()+"</td>"
                              + "<td class='phenumadata_text' style='width:80%;'>"+node.getText()+"</td>"
                              + "<td class='phenumadata' style='text-align: center;' ><a href=\""+PhenumaConstants.getSourceWebLink(relationshipType)+node.getId()+"\" target=\"_blank\"><img src=\"./resources/img/world_link.png\"></img></a></td>"
                              + "</tr>";
            }

        }
        table = table +"</table></div>";
        
        return table;
    }
    
    
    public static String createSemanticDataTable(List<EdgeSemanticInfo> semanticinfo, Double max_ic, Integer relationshipType) throws PhenumaException
    {
       String table = "";
       
       table = table+ " <div style=\"height:250px;width:100%;overflow:scroll;\">"
                + "<table id=\"reltable\" class=\"phenumatable\"> <thead><tr class=\"phenumarow\"> "
                + "<th onclick=\"SortTable(0, \'reltable\', 'id', 'text');\">"+PhenumaConstants.getTableTitleID(relationshipType)+"</th> "
                + "<th onclick=\"SortTable(1, \'reltable\', 'name', text');\">"+PhenumaConstants.getTableTitleName(relationshipType)+"</th> "
                + "<th onclick=\"SortTable(2, \'reltable\', 'ic', 'float');\">IC</th> "
                + "<th>From</th> "
                + "<th>Link</th> "
                + "</tr></thead><tbody>";
        
        for (EdgeSemanticInfo info : semanticinfo)
        {
           
          table = table + "<tr class=\"phenumarow\"  style=\"background-color:"+getColor(info.getScore()) +";\">"
                        + "<td class='phenumadatadlg_id'>"+info.getTermMax()+"</td>"
                        + "<td class='phenumadatadlg_name'>"+info.getTermMaxName()+"</td>"
                        + "<td class='phenumadatadlg_id'>"+NumberUtils.round(info.getScore()/max_ic, 3)+"</td>"
                        + "<td class='phenumadatadlg_name'>"+info.getTermX().toString()+","+info.getTermY().toString()+"</td>"
                        + "<td class='phenumadatadlg_id'><a href=\""+PhenumaConstants.getSourceWebLink(relationshipType)+info.getTermMax()+"\" target=\"_blank\"><img src=\"./resources/img/world_link.png\"></img></a></td>"
                        + "</tr>";


        }
        table = table +"</table></div>";
        
        return table;
    }
    
    
    
        
    public static String createSemanticDataTable_DB(List<Object[]> semanticinfo, Integer relationshipType) throws PhenumaException
    {

        String table = " <div style=\"height:300px;width:100%;overflow:scroll;\">"
                        + "<table id=\"reltable\" class=\"phenumatable\"> <thead><tr class=\"phenumarow\"> "
                        + "<th onclick=\"SortTable(0, \'reltable\', 'id', 'text');\">"+PhenumaConstants.getTableTitleID(relationshipType)+"</th> "
                        + "<th onclick=\"SortTable(1, \'reltable\', 'name','text');\">"+PhenumaConstants.getTableTitleName(relationshipType)+"</th> "
                        + "<th onclick=\"SortTable(2, \'reltable\', 'ic', 'float');\">IC</th> "
                        + "<th>Link</th> "
                        + "</tr></thead><tbody>";
        
        for (Object[] row : semanticinfo)
        {
            
            Integer hpoid  = (Integer)row[2];
            String  hpomax = (String)row[3];
            Double  ic     = (Double)row[4];
            
            TermID term = new TermID(new Prefix("GO"), hpoid);
            if (relationshipType == NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC)
                term = new TermID(new Prefix("HP"), hpoid);
            
            table = table + "<tr class=\"phenumarow\"  style=\"background-color:"+getColor(ic) +";\">"
                          + "<td class='phenumadata'>"+term.toString()+"</td>"
                          + "<td class='phenumadata_text'>"+hpomax+"</td>"
                          + "<td class='phenumadata'>"+NumberUtils.round(ic, 3)+"</td>"
                          + "<td class='phenumadata' style='text-align: center;'><a href=\""+PhenumaConstants.getSourceWebLink(relationshipType)+term.toString()+"\" target=\"_blank\"><img src=\"./resources/img/world_link.png\"></img></a></td>"
                          + "</tr>";


        }
        
        table = table +"</table></div>";
        
        return table;
    }

    /**
     * Get background color 
     * 
     * @param ic
     * @return 
     */
    public static String getColor(double ic)
    {
        if (ic >= 1.0)
           return  PhenumaConstants.COLOR_8;    
        else if (ic >= 0.9)
           return  PhenumaConstants.COLOR_7;         
        else if  (ic >= 0.8)
           return  PhenumaConstants.COLOR_6;
        else if  (ic >= 0.5)
           return  PhenumaConstants.COLOR_5;     
        else if  (ic >= 0.4)
           return  PhenumaConstants.COLOR_4;
        else if  (ic >= 0.3)
           return  PhenumaConstants.COLOR_3;      
        else if  (ic >= 0.2)
           return  PhenumaConstants.COLOR_2;
        
       return  PhenumaConstants.COLOR_1;         
                
    }
    
    /**
     * Get background color (gray scale)
     * 
     * @param ic
     * @return 
     */
    public static String getColorGray(double ic)
    {
    
        if (ic >= 8)
           return  PhenumaConstants.COLOR_8_GRAY;     
        else if (ic >= 7)
           return  PhenumaConstants.COLOR_7_GRAY;           
        else if (ic >= 6)
           return  PhenumaConstants.COLOR_6_GRAY;      
        else if (ic >= 5)
           return  PhenumaConstants.COLOR_5_GRAY;          
       else if (ic >= 4)
           return  PhenumaConstants.COLOR_4_GRAY;     
        else if (ic >= 3)
           return  PhenumaConstants.COLOR_3_GRAY;             
        else if (ic >= 2)
           return  PhenumaConstants.COLOR_2_GRAY;

       return  PhenumaConstants.COLOR_1_GRAY;         
                
    }
    
    
}
