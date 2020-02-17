/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.Console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Armando
 */
public class Variables {
    
    
    private Map<String, ArrayList<String>> lists;
    
    private static Variables instance = null;
	
	
    /**
     * Private constructor of variables. 
     */
    protected Variables()  
    {	
        lists = new HashMap<String,ArrayList<String>>();
    }
	 
	
    /**
     * Create a variables instance. If the instance is not null, this method create it.
     * @return
     */
    public static Variables getInstance()
    {
        if (instance == null)
                instance = new Variables();

        return instance;
    }
    
    
    
    public void addList(String name, ArrayList<String> list){
        if(list!=null && name!=null)
            lists.put(name, list);
    }
    
    /**
     * Add an element to the var with name 'name'. If the variable
     * is not exist a new var will be created.
     * 
     * @param name
     * @param element 
     */
    public void add(String name, String element){
        ArrayList<String> var = lists.get(name);
        
        if(var!=null)
            var.add(element);
        else
        {
            /*Create new var with one element.*/
            var = new ArrayList<String>();
            var.add(element);
            lists.put(name, var);
        }    
            
            
        lists.put(name, var);
    }
    
   /**
     * Add several elements to the var with name 'name'. If the variable
     * is not exist a new var will be created.
     * 
     * @param name
     * @param element 
     */
    public void addAll(String name, ArrayList<String> list){
        ArrayList<String> var = lists.get(name);
        
        if(var!=null)
            var.addAll(list);
        else
        {
            /*Create new var.*/
            list = new ArrayList<String>();
            list.addAll(list);
            lists.put(name, list);
        }    
            
            
        lists.put(name, var);
    }
    
    /**
     * Returns true if there is a variable created with the input name.
     * @param name
     * @return 
     */
    public boolean exists(String name){
        return lists.containsKey(name);
    }
    
    /**
     * Get variable length
     * @param name
     * @return 
     */
    public int length(String name){
        List<String> var = lists.get(name);
        
        if(var!=null)
            return var.size();
        
        return -1; //if the variable is not exist.
    }
    /**
     * Remove a variable and it content.
     *
     */
    public void remove(String name){
        lists.remove(name);
    }
    
     /**
     * Remove all variables and it content.
     *
     */
    public void removeAll(){
        lists = new HashMap<String,ArrayList<String>>();
    }

    /**
     * Get a variable value.
     * 
     * @param name
     * @return 
     */
    public ArrayList<String> getValue(String name){
        if (this.exists(name))
            return (ArrayList<String>) lists.get(name);
        else
            return null;
    }
    
}
