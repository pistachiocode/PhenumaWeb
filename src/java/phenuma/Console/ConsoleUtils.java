/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.Console;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import phenomizer.hpo.HPOUtils;

/**
 *
 * @author Armando
 */
public class ConsoleUtils {
    
    /**
     * Check if the input command is a assignment
     * @param line
     * @return boolean. True if the input represents an assignment.
     */
    
    public static boolean isAssignment(String line){
        return (line.split("<-").length == 2);
    }
    
    /**
     * Check if the input string represents a list
     * @param line
     * @return boolean. True if the input represents a list.
     */
    public static boolean isList(String line){
        Pattern p = Pattern.compile("\\(([A-Za-z0-9\\:]+)(\\,(\\s)*[A-Za-z0-9\\:]+)*\\)");
        Matcher m = p.matcher(line);
        
        return m.find();
    }
    
    
    /**
     * Check if the input string represents a list operation
     * @param line
     * @return boolean. True if the input represents an operation.
     */
    public static boolean isOperation(String line){
        return (line.split("\\.").length == 2);
    }
    
    
    /**
     * HPO list correct
     */
    public static boolean validateTermList(List<String> termlist)
    {
        Iterator<String> iter = termlist.iterator(); 
        
        boolean isTerm = true;
        
        while(iter.hasNext() && isTerm){
            String term = iter.next();
            isTerm = HPOUtils.isHPOterm(term);  
        }
        
        return isTerm;
    
    }
    
    /**
     * Create a list from a command line : (a,v,c,...)
     * @param line
     * @return boolean. True if the input represents a list.
     */
    public static ArrayList<String> listByCommand(String line)
    {
        
        String listContent = line.substring(1, line.length()-1); //substring. remove the parenthesis.
        
        if (!listContent.isEmpty())
        {
            ArrayList<String> res = new ArrayList<String>();

            String[] str = listContent.split(",");
            
            if(str.length>0){
                for(int i=0; i<str.length; i++)
                {
                    res.add(str[i].trim());
                }
            }else{
                //in this case the list has only one element.
                res.add(listContent.trim());
            }
            
            return res;
        } 
        
        return null;
        
    }
    
    /**
     * Print a list of string.
     * @param list
     * @return 
     */
    public static void showList(List<String> list){
        String str = "";
        
        for(int i = 0; i<list.size(); i++){
            str = str +"\n"+ list.get(i);
        }
        
        System.out.println(str);
       
    }

   public static ArrayList<String> readFromFile(String filename) throws FileNotFoundException, IOException {
        
        ArrayList<String> str = new ArrayList<String>();
        
        BufferedReader is = new BufferedReader(new FileReader(filename));
        String line = is.readLine();
        
        
        while(line!=null)
        {
            if(!line.startsWith("#")&&!line.isEmpty())
                str.add(line);

            line = is.readLine();
        }
        
        return str;
   }
   
   
   public static void printHelp(){
       
       String help = "Lists:\n"
                    + "\t- new list: a<-(element1,element2...)\n"
                    + "\t- add elements: a.add(element1,element2...)\n"
                    + "\t- length:       a.length \n"
                    + "\t- remove:       a.remove(varname)\n"
                    + "\t- print list    a\n"
                    + "\t- read list from file a<-read <filename>\n"
                    + "\t- random gene list a<-rndgenelist\n"
                    + "\t- random disease list a<-rnddiseaselist\n"
                    + "Networks:\n"
                    + "\t- disease monopartite network: mononetdisease <varname> <inputtype> [<filename>]\n"
                    + "\t- gene monopartite network:    mononetgene <varname> <inputtype> [<filename>]\n"
                    + "\t- hpo monpartite network:      mononethpo <varname> <inputtype> [<filenane>]\n"
                    + "\t- hpo-disease bipartite network:   bipnetdiseasehpo <varname> <inputtype> [<filenane>]\n"
                    + "\t- hpo-gene bipartite network:      bipnetgenehpo <varname> <inputtype> [<filenane>]\n"
                    + "\t- gene-disease bipartite network:  bipnetdiseasegene <varname> <inputtype> [<filenane>]\n"
                    + "\t- hpo-gene-disease tripartite network: tripnet <varname> <inputtype> [<filenane>]\n";
       
       System.out.println(help);
               
               
   
   }

       
}
