/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.objects;

/**
 *
 * @author Rocío Rodríguez López
 */
public class QueryType {
    
    private int id;
    private String name;

    public QueryType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
