/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.cysnetwork;

import java.util.List;
import phenuma.network.NetworkDB;
import phenuma.utils.PhenumaException;
import phenumaweb.database.PWDatabaseQueries;

/**
 *
 * @author Rocío
 */
public class CysNetworkFactory {

    
    public static CysNetwork createCysNetworkFromDatabase(NetworkDB network, List<String> object) throws PhenumaException
    {
        CysNetwork cysnetwork = new CysNetwork();
        
        cysnetwork.setNetworkDB(network);
        
        List<Object[]> resultlist = PWDatabaseQueries.getCysNetworkFromDatabase(object, network);
        
        if (resultlist != null && !resultlist.isEmpty())
        {
            for(Object[] row : resultlist)
            {
                String mainnetwork   = (String)row[0];
                String subnetworks   = (String)row[1];
                Integer toolarge     = (Integer)row[2];
                
                cysnetwork.setMainnetwork(mainnetwork);
                cysnetwork.setSubnetworks(subnetworks);
                cysnetwork.setToolarge(toolarge);
            }
            
            return cysnetwork;
        }

        return null;

    }
    
    
    
    
}
