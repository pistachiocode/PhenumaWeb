/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.cysnetwork;

import phenuma.network.NetworkDB;
import phenuma.utils.PhenumaException;


/**
 *
 * @author Rocío Rodríguez
 */
public class CysNetwork {
        

    private NetworkDB networkDB;
        
    private String mainnetwork = "{data: { nodes: []}}";
    private String subnetworks = "{ simsemhpo: []}";
    private double threshold;
    private Integer toolarge;
    
    /**
     * Networks from database constructor
     * 
     * @param network
     * @param type
     * @throws PhenumaException 
     */
    public CysNetwork(NetworkDB network, int type) 
    {

        this.networkDB = network;
        
        mainnetwork   =  Network2CysNetworkDB.network2CysNetworkDB(network);
        subnetworks   =  Network2CysNetworkDB.subNetworks(network);
    }

    /**
     * Create cysnetwork from database
     * @param network
     * @param type
     * @throws PhenumaException 
     */
    public CysNetwork() throws PhenumaException 
    {
        //Empty network
//        mainnetwork   =  Network2CysNetworkDB.network2CysNetworkDB(new NetworkDB());
//        subnetworks   =  Network2CysNetworkDB.subNetworks(new NetworkDB());
        this.threshold = 0.0;
        this.toolarge = 0;
    }


    public String getMainnetwork() {
        return mainnetwork;
    }

    public void setMainnetwork(String xml) {
        this.mainnetwork = xml;
    }

    public String getSubnetworks() {
        return subnetworks;
    }

    public void setSubnetworks(String subnetworks) {
        this.subnetworks = subnetworks;
    }

    public NetworkDB getNetworkDB() {
        return networkDB;
    }

    public void setNetworkDB(NetworkDB networkDB) {
        this.networkDB = networkDB;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public Integer getToolarge() {
        return toolarge;
    }

    public void setToolarge(Integer toolarge) {
        this.toolarge = toolarge;
    }
    
    
    @Override
    public String toString()
    {
        return "var network = "+this.mainnetwork+"\n var subnetworks = "+this.subnetworks;
    }
    
    
}
