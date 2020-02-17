/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.Console;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ontologizer.go.Prefix;
import ontologizer.go.TermID;
import phenomizer.database.DBUtils;
import phenomizer.hpo.Phenuma;
import phenomizer.utils.StringUtils;
import phenuma.networkproyection.NetworkConstants;

/**
 *
 * @author Armando
 */
public class PhenumaConsole {
   
    /**
     * Get a random list of diseases o genes from database
     *
     * @param size
     * @param table_name
     * @param field
     * @return
     * @throws SQLException 
     */
    public static ArrayList<String> randomQuery(int size, String table_name, String field) throws SQLException{

        String sql = "select "+field+" from "+ table_name +" order by rand() limit "+size;

        DBUtils.connect();
        ResultSet result = (ResultSet) DBUtils.execute(sql);


        ArrayList<String> study = new ArrayList<String>();
        int i = 0;

        while(result.next()){     
            
            if(table_name.equals("term")){
                TermID term = null;
                term = new TermID(new Prefix("HP"),result.getInt(1));
                study.add(term.toString());
            }else{
                study.add(new Integer(result.getInt(1)).toString());
            }
            
            
            i++;
        }

        DBUtils.close();

        return study;
    }
   
    
    
    
    public static void main (String[] args) throws IOException, SQLException {
        /**Load all ontologies*/
        Phenuma aux = Phenuma.getInstance();
                
        Variables var = Variables.getInstance();
                
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        boolean exit = false;
        
        while(!exit){
            System.out.print("PHENUMA>");
           
            String line = in.readLine();
            
            try {
                
                if (line.startsWith("exit")) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   {
                    System.out.println("Bye.");
                    exit = true;

                }} else if (ConsoleUtils.isAssignment(line))    processAssigment(line, var);

                   else if (line.startsWith(ConsoleContants.MONONETGENE_CMD))         execute(line,var, NetworkConstants.UNIPARTITE_GENE_SS_HPO);

                   else if (line.startsWith(ConsoleContants.MONONETDISEASE_CMD))      execute(line,var, NetworkConstants.UNIPARTITE_DISEASE_SS);

                   else if (line.startsWith(ConsoleContants.BIPNETDISEASEHPO_CMD))    execute(line,var, NetworkConstants.BIPARTITE_DISEASE_HPO);

                   else if (line.startsWith(ConsoleContants.BIPNETGENEHPO_CMD))       execute(line,var, NetworkConstants.BIPARTITE_GENE_HPO);
                   
                   else if (line.startsWith(ConsoleContants.BIPNETDISEASEGENE_CMD))   execute(line,var, NetworkConstants.BIPARTITE_GENE_DISEASE_SS);
                   
                   
                   else if (line.startsWith(ConsoleContants.HELP))     ConsoleUtils.printHelp();
                   
                   else if (var.exists(line))                          ConsoleUtils.showList(var.getValue(line));
                   
                   else if (ConsoleUtils.isOperation(line))     processOperation(line, var);
                   //Print list value
                   

                  else {

                    throw new PhenumaConsoleException(ConsoleContants.INCORRECTOPERATION);
                 }
                
            }catch(PhenumaConsoleException ex){
                System.err.println("PhenumaConsoleException: " +ex.getMessage());
            }
        }
                  
    }
 
    
    /**
     * Process assigment command
     * @param line
     * @param var 
     */
    public static void processAssigment(String line, Variables var) throws PhenumaConsoleException{
        /** Asignacion */
        String[] command = line.split("<-");
        if(command.length==2) {
            
            String varname = command[0].trim(); /*variable name*/ //TODO : Check if the variable name is correct. (It must start with a lower case letter).
            
            try{
                if (command[1].trim().equals(ConsoleContants.RNDGENELIST_CMD)) {
                    /**Random gene list from database*/    
                    ArrayList<String> genes =  randomQuery(100, "gene", "entrezid");
                    var.addList(varname, genes);

                } else if (command[1].trim().equals(ConsoleContants.RNDDISEASELIST_CMD)) {
                    /**Random diseases list from database */
                    ArrayList<String> diseases =  randomQuery(100, "disease", "omim");
                    var.addList(varname, diseases);
                    
                } else if (command[1].trim().equals(ConsoleContants.RNDHPOLIST_CMD)) {
                    /**Random diseases list from database */
                    ArrayList<String> hpo =  randomQuery(100, "term", "term_id");
                    var.addList(varname, hpo);
                    
                }else if (command[1].trim().startsWith(ConsoleContants.READ)) {
                     /**Read a list of elements from a file*/
                    readElementList(command, var, varname);
                
                } else if (ConsoleUtils.isList(command[1].trim())){
                    /*Create list from command line*/
                    ArrayList<String> list = ConsoleUtils.listByCommand(command[1].trim());
                    var.addList(varname, list);
                    
                }  else {
                    throw new PhenumaConsoleException(ConsoleContants.INCORRECTOPERATION);
                }
            }catch(SQLException ex){
                System.err.println(ex.getMessage());
            }
            
        }
    }
    
    /**
     * Process operation 
     * @param line
     * @param var 
     */
    public static void processOperation(String line, Variables var) throws PhenumaConsoleException{
        /** Asignacion */
        String[] command = line.split("\\.");
        if(command.length==2) {
            
            String varname = command[0]; /*variable name*/ 
            
            if(!var.exists(varname))
                throw new PhenumaConsoleException(ConsoleContants.variableNotFound(varname));
            
            if (command[1].startsWith(ConsoleContants.ADD)){
                
                String element =  command[1].split("add")[1]; //element = (a,b,c,...)
                var.addAll(varname, ConsoleUtils.listByCommand(element));
                
            } else if (command[1].equals(ConsoleContants.REMOVE)) {
                var.remove(varname);
            } else if (command[1].startsWith(ConsoleContants.LENGTH)) {
                System.out.println(var.length(varname));
            }  else {
                throw new PhenumaConsoleException(ConsoleContants.INCORRECTOPERATION);
            }
    
        }
    }

    /**
     * Read list of elements from a file.
     * 
     * @param command
     * @param var
     * @param varname 
     */
    private static void readElementList(String[] command, Variables var, String varname) throws PhenumaConsoleException{
       
        String[] args = command[1].split(" ");
        if(args.length == 2){
            try {
                ArrayList<String> items = ConsoleUtils.readFromFile(args[1]);
                var.addList(varname, items);
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }else{
            throw new PhenumaConsoleException(ConsoleContants.INCORRECTSINTAX+" a<-read <file>");
        }
    }

    
    /**
     * 
     * @param line
     * @param var 
     */
    private static void execute(String line, Variables var, int queryType) throws PhenumaConsoleException {
    //Monopartite Network of HPO 
        String[] arguments = line.split(" ");
        
        if(arguments.length>=2){
            
            try{
                /**Variable name*/
                String variable = arguments[1];
                if(!Variables.getInstance().exists(variable)) 
                    throw new PhenumaConsoleException(ConsoleContants.variableNotFound(variable));


                /**Input type*/
                int inputType = 0;
                if(arguments.length>=3)
                    inputType = new Integer(arguments[2].trim()).intValue();
                
                
                /**Output file*/
                String file = null;                        
                if(arguments.length>=4)
                    file = arguments[3];

                if(inputType == 0) 
                {
                    if(queryType != NetworkConstants.BIPARTITE_GENE_DISEASE_SS)
                    {
                        /*Input: phenotype*/
                        List<String> termlist = var.getValue(variable);
                        if(ConsoleUtils.validateTermList(termlist))
                            QueryExecution.executeHPOQuery(StringUtils.stringListToTermIDList(var.getValue(variable)), file, queryType);
                        else
                            throw new PhenumaConsoleException(ConsoleContants.INCORRECTSINTAXHPO);
                    }
                    else
                    {
                        throw new PhenumaConsoleException(ConsoleContants.INCORRECT_INPUT);
                    }
                } 
                else if (inputType == 1) 
                {
                    /**Input list of genes*/
                    QueryExecution.executeGeneQuery(var.getValue(variable), file, queryType);
                }
                else if (inputType == 2) 
                {
                    /**Input list of diseases*/
                    QueryExecution.executeDiseaseQuery(var.getValue(variable), file, queryType);
                }
                else { 
                    throw new PhenumaConsoleException(ConsoleContants.INCORRECTOPTION_BI);
                }
                
            }catch(IOException ex){
                 System.err.println(ex.getMessage());
            }
                    
            
        }else{
            throw new PhenumaConsoleException(ConsoleContants.INCORRECTSINTAX+"binetdiseasehpo <varname> <inputtype> [<filename>]");

        }
    }


 
}
