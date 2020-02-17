/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.Console;

/**
 *
 * @author Armando
 */
public class ConsoleContants {    
    
    
    public static final String EXIT_CMD = "exit";
    public static final String RNDGENELIST_CMD = "rndgenelist";
    public static final String RNDDISEASELIST_CMD = "rnddiseaselist";
    public static final String RNDHPOLIST_CMD = "rndhpolist";
    public static final String HELP = "help";
    
    /*Input*/
    public static final String READ = "read";
    
    /*Create networks*/
    public static final String MONONETGENE_CMD = "mononetgene";
    public static final String MONONETDISEASE_CMD = "mononetdisease";
    public static final String MONONETHPO_CMD = "mononethpo";
    
    public static final String BIPNETGENEHPO_CMD = "bipnetgenehpo";
    public static final String BIPNETDISEASEHPO_CMD = "bipnetdiseasehpo";
    
    public static final String BIPNETDISEASEGENE_CMD = "bipnetdiseasegene";
    public static final String TRIPARTITE_CMD = "tripnet";
    
    /**List Operations*/
    public static final String ADD = "add";
    public static final String REMOVE = "remove";
    public static final String LENGTH = "length";
   
    /**Error Messages*/
    public static final String variableNotFound(String variable){
       return "The variable '"+variable+"' has not been declared.";
    }
    
    public static final String INCORRECTOPERATION = "Incorrect Operation";
    public static final String INCORRECTSINTAX = "The sintax of the command is not correct. The correct sintax is:  ";
    public static final String EMPTYNETWORK = "The result network is empty.";
    
    public static final String INCORRECTOPTION_MONO = "Incorrect option:\n\t0 - HPO input\n\t1 - Gene input\n\t2 - Disease input.";
    public static final String INCORRECTOPTION_BI = "Incorrect option:\n\t0 - Gene input\n\t1 - Disease input.";
    public static final String INCORRECTSINTAXHPO = "Incorrect sintax of the list of HPO terms.";
    public static final String INCORRECT_INPUT = "Incorrect input. It is not possible generate a gene-disease bipartite network from a hpo list.";
}
