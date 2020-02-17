/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenomizer.hpo;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Armando
 */
public class PhenumaDB {
    
    private String currentPU;
   
    private static PhenumaDB instance = null;
    
    private PhenumaDB(){     
        currentPU =  phenuma.constants.Constants.PERSISTENCE_UNIT_PHENUMA_DB;
    }


    public static PhenumaDB getInstance(){
        if (instance == null)
                instance = new PhenumaDB();
        
        return instance;      

    }

    public String getCurrentPU() {
        return currentPU;
    }

    public void setCurrentPU(String currentPU) {
        this.currentPU = currentPU;
    }

    public EntityManagerFactory getFactory() {
        return Persistence.createEntityManagerFactory(currentPU);
    }    
    
}
