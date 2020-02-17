/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenumaweb.database;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import phenuma.network.NetworkDB;
import phenuma.objects.PhenumaConstants;
import phenuma.utils.Utils;

/**
 *
 * @author Armando
 */
public class PWDatabaseQueries {
    
    static final Logger logger = Logger.getLogger(PWDatabaseQueries.class.getName());
    
    public static List<Object[]> getCysNetworkFromDatabase(List<String> object, NetworkDB network)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PhenumaConstants.PERSISTENCE_UNIT_PHENUMA_HISTORY_DB);
        EntityManager em = emf.createEntityManager();
        
        long time = 0;
        Query query = null; 
        
        try
        {
            String sql = "select nodes, network, toolarge from phenumahistory.network where query = '"+Utils.inputToString(object)+"' "+
                         "and inputtype = '"+network.getInputtype()+"' "+
                         "and outputtype = '"+network.getQuerytype()+"' "+
                         "and confidence = '"+new Double(network.getConfidence()).intValue()+"' ";    
            
            time = System.currentTimeMillis();

            System.out.println(sql);
            
            query = em.createNativeQuery(sql);

            time = System.currentTimeMillis() - time;

            System.out.println("DB Query: "+time/1000);
            
            if(query!= null && query.getResultList() != null && query.getResultList().size()==1){
                return (List<Object[]>)query.getResultList();
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error al consultar en PhenumaHistory: "+ex.getMessage());
            em.close();
            emf.close();
        }
        finally
        {
            try{
                logger.error("Cerrando conexión");
                em.close();
                emf.close();
            }
            catch(Exception ex)
            {
                logger.error("Error cerrando conexión "+ex.getMessage());
                
            }
        }
        return null;
    }

    
}
