/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.Console;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rocio
 */
public class History {
    
    List<String> commands;
    
    private static History instance = null;
	
	
    /**
     * Private constructor of history. 
     */
    protected History()  
    {	
        commands = new ArrayList<String>();
    }
	 
	
    /**
     * Create a history instance. If the instance is not null, this method create it.
     * @return
     */
    public static History getInstance()
    {
        if (instance == null)
                instance = new History();

        return instance;
    }
    
    
    public String get(int i){
        return commands.get(i);
    }
    
    public void clear(){
       commands = new ArrayList<String>();
    }
    
    
}
