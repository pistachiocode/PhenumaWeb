/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import phenuma.utils.PhenumaException;

/**
 * Node class
 * 
 * @author Armando
 */

public abstract class Node {
    
    protected Object element;
    
    protected String link;
    protected String text;
    protected String id;
    protected boolean input;
    

    
    public Node() {
        this.element = null;
        this.input = false;
    }
        
    public Node(Object element) {
        this.element = element;
        this.input = false;
    }

    public Node(Object element, boolean isInput) {
        this.element = element;
        this.input = isInput;
    }
    
        
    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }


    
    
    
    @Override
    public boolean equals(Object o)
    {
        Node n = (Node)o;
        
        if(this.element!=null && n.element!=null)
            return this.element.equals(n.element);
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.element != null ? this.element.hashCode() : 0);
        return hash;
    }
      
  
    @Override
    public String toString(){
        if(element!=null)
            return element.toString();
        else
            return text;
    }
    
    public abstract String getLink();
    public abstract String getId();
    public abstract String getText() throws PhenumaException;
    
    
    
    
}
