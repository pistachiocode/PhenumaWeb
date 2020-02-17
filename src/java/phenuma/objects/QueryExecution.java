/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import ontologizer.StudySetFactory;
import ontologizer.go.OBOParserException;
import ontologizer.go.TermID;
import org.apache.log4j.Logger;
import phenomizer.database.SqlUtils;
import phenomizer.utils.NumberUtils;
import phenuma.constants.Constants;
import phenuma.network.Network;
import phenuma.network.NetworkDB;
import phenuma.network.PhenotypeNetworkDB;
import phenuma.networkproyection.GeneQuery;
import phenuma.networkproyection.OmimQuery;
import phenuma.networkproyection.OrphanumQuery;
import phenuma.networkproyection.PhenotypeQuery;
import phenuma.utils.Utils;


/**
 *
 * @author Armando
 */
public class QueryExecution {
    
    static final Logger logger = Logger.getLogger(QueryExecution.class.getName());
    
    static class QueryThread implements Callable<Network>{
       static final Logger logger = Logger.getLogger(QueryThread.class.getName());
           
        private List<String> items;
        private int networkType;
        private double threshold;
        
        private Network network = null;
        
        public QueryThread(List<String> items, int networkType, double threshold)
        {
            this.items = items;
            this.networkType = networkType;
            this.threshold = threshold;
        }
        


        @Override
        public Network call() throws Exception {
            
            Network network = null;

            GeneQuery q = new GeneQuery(StudySetFactory.createFromList(items, true), networkType);
            q.setThreshold(threshold);

            q.executeQueryDB();

            if(q.getResultDB()==null) {
                System.out.println("ES NULL");
            }
                
            return this.network = q.getResult();

        }
        
       
    }
    
    
    
    public static void saveQuery(String input, Integer querytype, int inputtype, Integer confidence){
       
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
        String ip = httpServletRequest.getRemoteAddr(); 

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PhenumaConstants.PERSISTENCE_UNIT_PHENUMA_HISTORY_DB);
        EntityManager em = emf.createEntityManager();
        
        try
        {
            System.out.println("Adding query information to phenumahistory database.");


            String[] fields = {"inputtype", "input", "querytype", "time", "date", "ip", "confidence"};
            String[] values = {"\'"+inputtype+"\'", "\'"+input+"\'", "\'"+querytype.toString()+"\'", "\""+Utils.currentDateTime()+"\"", "now()", "\""+ip+"\"", confidence.toString()};

            //Building sql query
            String sql = SqlUtils.insertSQL("phenumahistory.query", fields, values);

            em.getTransaction().begin();
            Query q = em.createNativeQuery(sql);
            q.executeUpdate();
            em.getTransaction().commit();

            System.out.println("Query information inserted.");
            
        }
        catch(Exception ex)
        {
            System.out.println("Error al insertar en PhenumaHistory: "+ex.getMessage());
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
        
    }
    
    public static NetworkDB executeGeneQueryDB(List<String> items,int networkType, double threshold) throws IOException
    {    
        logger.info(PhenumaConstants.LOGMESSAGES_CREATING_GENE_NETWORK_DB);
        GeneQuery q = new GeneQuery(StudySetFactory.createFromList(items, true), networkType);
        q.setThreshold(NumberUtils.round(threshold,3));
        q.executeQueryDB();
     
        return q.getResultDB();
    }
    
    
    
    public static NetworkDB executeDiseaseQuery(List<String> items, int networkType, double threshold) throws IOException
    {
        logger.info(PhenumaConstants.LOGMESSAGES_CREATING_OMIM_NETWORK_DB);
        OmimQuery q = new OmimQuery(StudySetFactory.createFromList(items, true), networkType);
        q.setThreshold(NumberUtils.round(threshold,3));
        q.executeQueryDB();

        return q.getResultDB();

    }
     
     
    public static NetworkDB executeRareDiseaseQuery(List<String> items, int networkType, double threshold) throws IOException
    {
        logger.info(PhenumaConstants.LOGMESSAGES_CREATING_ORPHANUM_NETWORK_DB);
        OrphanumQuery q = new OrphanumQuery(StudySetFactory.createFromList(items, true), networkType);     
        q.setThreshold(NumberUtils.round(threshold,3));
        q.executeQueryDB();
        
        return q.getResultDB();
        
    }
    
    
    public static PhenotypeNetworkDB executePhenotypeQuery(List<String> items, int networkType,  double threshold)
    {
        logger.info(PhenumaConstants.LOGMESSAGES_CREATING_PHENOTYPE_NETWORK_DB);
        
        ArrayList<TermID> input = new ArrayList<TermID>();
        
        for(String item : items){
            TermID termid = new TermID(item);
            input.add(termid);
        }
        
        PhenotypeQuery q = new PhenotypeQuery(input, networkType);
        q.setThreshold(NumberUtils.round(threshold,3));
        q.setOntology(Constants.ONTO_HPO);
        q.executeQueryDB();

        
        return (PhenotypeNetworkDB) q.getResultDB();
        
    }
    
}
