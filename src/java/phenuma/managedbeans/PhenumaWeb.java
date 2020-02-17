
package phenuma.managedbeans;

import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import phenuma.cysnetwork.CysNetwork;

/**
 *
 * @author Rocío Rodríguez López
 */
@SessionScoped
public class PhenumaWeb implements Serializable{
    

    private CysNetwork currentNetwork; 

    public PhenumaWeb()  
    {	
        currentNetwork = null;
    }
    
    public CysNetwork getCurrentNetwork() {
        return currentNetwork;
    }

    public void setCurrentNetwork(CysNetwork currentNetwork) {
        this.currentNetwork = currentNetwork;
    }

    public CysNetwork getCurrentNetworkDB() {
            return currentNetwork;
    }

    public void setCurrentNetworkDB(CysNetwork currentNetworkkDB) {
        this.currentNetwork = currentNetwork;
    }


 
    
    
}
