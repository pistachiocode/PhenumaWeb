/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.dataqueries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import ontologizer.StudySet;
import ontologizer.types.ByteString;
import org.apache.log4j.Logger;
import phenomizer.hpo.PhenumaDB;
import phenuma.constants.Constants;
import phenuma.entities.AvaliableGeneSymbol;
import phenuma.entities.Disease;
import phenuma.entities.Gene;
import phenuma.entities.GeneSynonym;
import phenuma.entities.RareDisease;
import phenuma.network.Node;
import phenuma.network.NodeDisease;
import phenuma.network.NodeGene;
import phenuma.network.NodeRareDisease;
import phenuma.networkproyection.NetworkConstants;
import phenuma.networkproyection.NetworkUtils;
import phenuma.utils.ErrorMessages;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Rocío Rodríguez López
 */
public class DatabaseQueries {
    
    /**
     * Logger
     */
    static final Logger logger = Logger.getLogger(DatabaseQueries.class.getName());

    /**
     * Phenuma database object conection
     */
    PhenumaDB pdb;
      
    /**
     * Creates database queries object
     */
    public DatabaseQueries(){
        pdb = PhenumaDB.getInstance();
    };
    
 
    /**
     * Get all genes included in the database.
     * 
     * @return List of element symbols 
     */
    public List<AvaliableGeneSymbol> getAllGenes(){        
        
        List<AvaliableGeneSymbol> result = new ArrayList<AvaliableGeneSymbol>();
         
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        try{
            
            Query q = em.createNamedQuery("AvaliableGeneSymbol.findAll");
             
            if(q!=null && q.getResultList() != null){
               result =  q.getResultList();
            }

        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
           
        return result;
    }
    
    /**
     * Get all diseases included in the database.
     * 
     * @return List of Diseases.
     */
    public List<Disease> getAllDiseases(){
        
        List<Disease> result = new ArrayList<Disease>();
        
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        try{
             Query q = em.createNamedQuery("Disease.findAll");
             
             if(q!=null && q.getResultList() != null ) {
                result =  q.getResultList();
            }
            
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
           
        return result;
    }
    
    /**
     * Get all rare diseases included in the database,
     * 
     * @return 
     */
    public List<RareDisease> getAllRareDiseases(){
        
        List<RareDisease> result = new ArrayList<RareDisease>();
        
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        try{
             Query q = em.createNamedQuery("RareDisease.findAll");
             
             if(q!=null && q.getResultList() != null) {
                result =  q.getResultList();
            }

        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
           
        return result;
    }

            
    /**
     * Get element by entrez id from database
     * @param entrezid
     * @return 
     */
    public Gene getGeneByEntrezId(String entrezid)
    {
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        Gene gene = null;
        try{
            gene = em.find(Gene.class, new Integer(entrezid));
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
        
        return gene;
    }
    
    /**
     * Get element by symbol
     * 
     * @param symbol 
     * @return 
     */
    public Gene getGeneBySymbol(String symbol) throws PhenumaException
    {
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        Gene gene = null;
        
        try{
            Query q = em.createNamedQuery("Gene.findBySymbol").setParameter("symbol", symbol);
        
            if(q!=null && q.getResultList() != null && !q.getResultList().isEmpty()){
            
                List<Gene> genes = q.getResultList();
            
                if(genes.size()==1) {
                    gene = genes.get(0);
                }
                else {
                    throw new PhenumaException(ErrorMessages.MORETHANONE_GENE_SYMBOL);
                }
            }
            
            return gene;
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
        return gene;
    }
    
    /**
     * Get list of genes (entrez) by a symbol of another database.
     * 
     * @param symbol 
     * @param dbReference (Orphanum, Ensembl, MIM, HGNC, HPRD)
     * @return 
     */
    public List<Gene> getGeneBySynonym(String symbol, String dbReference)
    {
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        List<Gene> result = new ArrayList<Gene>();
        
        try{
            Query q = em.createNamedQuery("GeneSynonym.findBySymbolandDbReference").setParameter("symbol", symbol).setParameter("dbreference", dbReference);

            if(q!=null && q.getResultList() != null && !q.getResultList().isEmpty()){

                List<GeneSynonym> synonyms = q.getResultList();

                for(GeneSynonym synonym : synonyms)
                {
                    result.add(synonym.getEntrezid());
                }
            }
            
            return result;
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
        return result;
    }
    
    
    
    
    /**Get disease by omim from database
     * @param entrezid
     * @return 
     */
    public Disease getDiseaseByOmim(String omim)
    {
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        Disease d = null;
        List<Disease> list = null;
        
        try{
           Query q =  em.createNativeQuery("select * from disease where omim = "+omim, Disease.class);          
           list = q.getResultList();
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
  
        
        if (list != null && !list.isEmpty() && list.size() == 1)
            d = list.get(0);
        
        return d;
    }
    
    /**
     * Get list of entrez id related with a disease.
     * 
     * @param disease
     * @return 
     */
    public List<Gene> getGenesByDisease(String disease)
    {
        List<Gene> list = new ArrayList<Gene>();

        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();

        
        try{
            Disease d = em.find(Disease.class, new Integer(disease));
            
            if(d!=null)
                list = d.getGeneList();
            
            return list;
            
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }

        return list;
    }
    
       /**
     * Get list of entrez id related with a disease.
     * 
     * @param disease
     * @return 
     */
    public List<Gene> getGenesByRareDisease(String orphanum)  
    {
        List<Gene> list = new ArrayList<Gene>();
        
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();

        try{
            RareDisease rd = em.find(RareDisease.class, new Integer(orphanum));

            if(rd!=null)
                list = rd.getGeneList();
            
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }

        
        return list;
    }
    
    
    
    /**
     * Get list of omim related with a element.
     * 
     * @param disease
     * @return 
     */
    public List<Disease> getDiseasesByGene(String gene) 
    {
        List<Disease> list = new ArrayList<Disease>();

        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();

        try{
            Gene g = em.find(Gene.class, new Integer(gene));

            if(g!=null)
                list = g.getDiseaseList();
            
            return list;
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        } 
        
       return list;
    }
    
    
    
    /**
     * Get rare disease by orphanum
     * 
     * @param disease
     * @return 
     */
    public RareDisease getRareDiseasesByOrphanum(String orphanum)
    {
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
       
        RareDisease d  = null;
        try{
            d = em.find(RareDisease.class, new Integer(orphanum));        
        }catch(Exception ex){
            logger.info(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
        
        return d;
    }
    
    
    /**
     * Get list of rare diseases (orphanum)  related with a element.
     * 
     * @param disease
     * @return 
     */
    public List<RareDisease> getRareDiseasesByGene(String gene)
    {
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        List<RareDisease> list = new ArrayList<RareDisease>();
        
        try {
            Gene g = em.find(Gene.class, new Integer(gene));
            
            if(g!=null)
                list = g.getRareDiseaseList();
           
        } catch (PhenumaException ex) {
           logger.error(DatabaseQueries.class.getName()+" :"+ex.getMessage());
        } finally{
            em.close();
            emf.close();
        }
        
        return list;
    }
    
    /**
     * 
     * Get pvalue associated to an score and a disease
     * 
     * @param item
     * @param score
     * @param ontology
     * @param associations
     * @param size
     * @param field : adjustement_bon, adjustment_bh or pvalue
     * @param em
     * @return 
     */
    
    
    public  Double getPValue(String item, Double score, int ontology, int associations, int size, String field, EntityManager em){
        
        /**
         * If the size of the query is greater than 10 we use the pvalues calculated for a query of 10 items.
         */
        if(size>10)
            size = 10;
        /**
         * Get table name
         */
        String table_name = "distributions_size_";
        String field_name = "disease";
        
        if (associations == Constants.ASSOC_GENES_HPO){
            table_name = "distributions_gene_size_";
            field_name = "gene";
        }else if (associations == Constants.ASSOC_ER_HPO){
            table_name = "distributions_er_size_";
            field_name = "orphanum";
        }
        
        
        Double min = getMinScore(table_name, size, field_name, item, score, em);
        
        if(min!=null){
            String sqlQueryPValue = "select "+field+" from  "+table_name+size+" where "+field_name+"="+item+" and score = "+min;

            
            Query qpvalue = em.createNativeQuery(sqlQueryPValue, "GetPvalue");

            List<Object[]> pvaluelist = qpvalue.getResultList();

            if(pvaluelist != null && !pvaluelist.isEmpty())
                return (Double)pvaluelist.get(0)[0];
        }
        

        
        
        return 1.0;

        
    }
    
    
    public Double getMinScore(String table_name, int size, String field_name, String item, Double score, EntityManager em) 
    {
        /**
         * Get adjustment of bonferroni of the pvalue
         */
        Double min = 0.0;
        String sqlQuery = "select min(score) from  "+table_name+size+" where "+field_name+"="+item+" and score >= "+score;
        
        Query q = em.createNativeQuery(sqlQuery, "GetMinScore");

        List<Object[]> result = q.getResultList();


        if(result == null  || result.isEmpty() || result.get(0)[0] == null){

            String sqlQueryMax = "select max(score) from "+table_name+size+" where "+field_name+"="+item+" order by score limit 1";
            
            Query qmax = em.createNativeQuery(sqlQueryMax, "GetMaxScore");

            List<Object[]> resultmax = qmax.getResultList();

            if(resultmax != null || !resultmax.isEmpty()){
                Object[] resultArrayMax = (Object[])resultmax.get(0);
                min = (Double)resultArrayMax[0];
            }
            else
                min = null;

        }else{

            Object[] resultArray = (Object[])result.get(0);
            min = (Double)resultArray[0];
        }

        return min;
    }
   
    
    /**
     * For unipartite networks: get the study set with the objects involved in the network. 
     * For bipartite network: get the study set with the object related with the input set. In case of genes, the set of diseases.
     * @param query
     * @param relationship
     * @return 
     */
    public StudySet getNetworkNodes(StudySet query,  int inputtype, int querytype, double threshold)
    {
        /**
         * Empty result for an empty query.
         */
        if (query.getSize() == 0)
            return new StudySet();
       
       
        /**
         * Input studyset as String.
         */
        String input = stringSetfromStudySet(query);

        
        /**
         * SQL query for getting all relationships.
         */
        String sql = getQueryRelationshipsSQL(inputtype, querytype, threshold, input);      
        System.out.println(sql);
        
        /**
         * Executing sql query.
         */
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
     
        StudySet ss = new StudySet();
            
        try
        {
            Query sqlquery = em.createNativeQuery(sql);

            if(sqlquery!= null && sqlquery.getResultList() != null )
            {
                List<Object[]> result =  sqlquery.getResultList();
                
                for(Object[] element : result)
                {
                    ByteString elementId = new ByteString(element[0].toString());
                    String elementName   = (String)element[1];
                    
                    if(existsElement(elementId.toString(), querytype));
                        ss.addElement(elementId, elementName);
                }
                
            }

        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
        
        return ss;
    }

    
    /**
     * Get String with the Sql query to get objects relationships.
     * 
     * @param inputtype
     *                  integer with the input type (genes, omin or orphanums)
     * @param querytype
     *                  type of network requested
     * @param threshold
     *                  label applied to retrieve de data
     * @param input
     *                  string with all the input elements separated by commas
     * @return 
     */
    private String getQueryRelationshipsSQL(int inputtype, int querytype, double threshold, String input) {
        
        String colunmName = this.getColumnName(inputtype, querytype);
        
        /**
         * Columns names: identifiers and names.
         * The database stored relationships among elements
         * for example geneX, geneY for identifiers and geneXname,
         * geneYname for gene names. The structure is similar for
         * diseases.
         */
        String columnIdX = colunmName + "X";
        String columnIdY = colunmName + "Y";
        String columnNameX = colunmName + "Xname";
        String columnNameY = colunmName + "Yname";
        
        /**
         * Get relationship name.
         */
        String relationship = getRelationshipsTag(querytype, inputtype);
        
        /**
         * Get table name.
         */
        String tableName = this.getTableName(querytype);
        
        /**
         * Build SQL query sentence.
         */
        String sql =  "SELECT " + columnIdX + "," + columnNameX +" "
                    + "FROM "   + tableName + " "
                    + "WHERE "  + relationship + " >= " + threshold
                                + " AND (" + columnIdX + " IN " + input + " "
                                + " OR "   + columnIdY + " IN " + input
                                + ") "
                    + "UNION "
                    + "SELECT " + columnIdY + "," + columnNameY + " "
                    + "FROM "   + tableName + " "
                    + "WHERE "  + relationship + " >= " + threshold
                                + " AND (" + columnIdX + " IN " + input + " "
                                + "OR "    + columnIdY + " IN " + input 
                                + ") ";
        
        return sql;
    }

    
    /**
     * Get the related elements with the input set. Bipartite Network
     * @param query
     * @param inputtype
     * @param querytype
     * @return 
     */
    
    
    public StudySet getObjectRelatedBipartite (StudySet query,  int inputtype, int querytype, double threshold)
    {
        if (query.getSize() == 0)
        {
            return new StudySet();
        }
                
        String relationship = getRelationshipsTag(querytype, inputtype);
        String objectset = stringSetfromStudySet(query);
        
        
        String sql = "select "+this.getColumnsNameBipartite(inputtype, querytype)+" from "+this.getTableName(querytype)+" where "+relationship+" >="+threshold
                   + " and "+this.getColumnName(inputtype, querytype)+" in "+objectset;
        
        System.out.println(sql);
        
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        
        StudySet ss = new StudySet();
            
        try
        {
            Query sqlquery = em.createNativeQuery(sql);

            if(sqlquery!= null && sqlquery.getResultList() != null )
            {
                List<Object[]> result =  sqlquery.getResultList();
                
                for(Object[] gene : result)
                {
                    ss.addElement(new ByteString(gene[0].toString()), (String)gene[1]);
                }
                
            }

        }catch(Exception ex){
            logger.error(ex.getMessage());
        }finally{
            em.close();
            emf.close();
        }
        
        return ss;
    }
    

    /**
     * This method create a String with a SQL sentence to get the network by a set of genes
     * @param ss
     * @return 
     */
    public String sqlSetQuery(StudySet ss, int input_type, int querytype)
    {
        if(ss.getSize()>0)
        {
            String sql = "select * from "+this.getTableName(querytype) +" where ";

            String objlist = "(";

            for(ByteString gene : ss.getAllNodesNames())
            {
                objlist = objlist + gene + ",";
            }

            if(ss.getSize()>0)
                objlist = objlist.substring(0, objlist.length()-1) +")";
            else
                objlist = "()";
            
            sql = sql + "("+this.getColumnName(input_type, querytype)+"X in "+objlist+" and "+this.getColumnName(input_type, querytype)+"Y in "+objlist+")";

            return sql;
        }else{
            return "";
        }
            
    }
    
    
    
    /**
     * Create SQL sentence to get the bipartite relationships of a network
     * 
     * @param ss
     * @param inputtype
     * @param querytype
     * @return 
     */
    public String sqlSetQueryBipartite(StudySet ss, int inputtype, int querytype, double threshold)
    {
        String relationship = getRelationshipsTag(querytype, inputtype);
         
        if (ss.getSize() > 0)
        {
            String sql = "select * from " + getBipartiteView(querytype) + " where ";
 
            String objlist = "(";
 
            for (ByteString object : ss.getAllNodesNames())
            {
                objlist = objlist + object + ",";
            }
 
            if (ss.getSize() > 0)
                objlist = objlist.substring(0, objlist.length() - 1) + ")";
            else {
                objlist = "()";
            }
            
            sql = sql + "(" + getColumnName(inputtype, querytype) + " in " + objlist + ") and "+relationship+" >="+threshold;
 
            return sql;
        }
        
        return "";
   }

    
    /**
     * 
     * Create a SQL sentence to build a network from the database.
     * 
     * @param inputset
     * @param inputtype
     * @param querytype
     * @return
     * @throws IOException 
     */
    public List<Object[]> networkQuery(StudySet inputset, StudySet networknodesset, int inputtype, int querytype, double threshold) throws IOException
    {

        List<Object[]> result = new ArrayList<Object[]>();
        
        if (inputset.getSize() == 0) return result;

        String sql = "";
        
        if (NetworkUtils.isPhenotypeQuery(querytype))
        {
            sql = sqlSetQuery(inputset, inputtype,  querytype);
        }
        else 
        {
            if (!NetworkUtils.isBipartite(querytype))
            {
                sql = sqlSetQuery(networknodesset, inputtype,  querytype);
            } 
            else
            {  
                sql = sqlSetQueryBipartite(inputset, inputtype, querytype, threshold);
            }
        }
        
        System.out.println("DatabaseQueries.networkQuery: "+sql);
        
        result = executeSqlQuery(sql);
        
        return result;
        
    }

    
    /*
     * Execute a strint SQL query to create a network
     *
     */
    private List<Object[]> executeSqlQuery(String sql) {
        
        List<Object[]> result = new ArrayList<Object[]>();
        
        if (!sql.isEmpty())
        {
            EntityManagerFactory emf = pdb.getFactory();
            EntityManager em = emf.createEntityManager();

            long time = 0;
            Query query = null; 

            try
            {
                time = System.currentTimeMillis();

                query = em.createNativeQuery(sql);

                time = System.currentTimeMillis() - time;

                System.out.println("DB Query: "+time/1000);

                if(query!= null && query.getResultList() != null )
                    result =  query.getResultList();

            }catch(Exception ex){
                System.out.println("Network Query ERROR: "+ex.getMessage());
            }finally{
                em.close();
                emf.close();
            }
        }
       
        return result;
    }
    

    
    /**
     * 
     * @param object
     * @param type
     * @return 
     */
    public List<Object[]> getHPOTerms(Integer object, int type)
    {
        String field = "";
        String viewname = "";
        if(type == NetworkConstants.INPUT_TYPE_GENES)
        {
            field = "gene";
            viewname = "gene_hpo_view";
            
        }else if(type == NetworkConstants.INPUT_TYPE_OMIM)
        {
            field = "omim";
            viewname = "omim_hpo_view";
            
        }else if(type == NetworkConstants.INPUT_TYPE_ORPHA)
        {
            field = "orphanum";
            viewname = "orphan_hpo_view";
            
        }
            
        String sql = "select id, name, ic from phenumadb."+viewname+" where "+field+"="+object+" order by ic desc";
        List<Object[]> result = executeSqlQuery(sql);
        
        return result;
        
    }
    
    
    
    public List<Object[]> getRelationshipTerms(Integer objectX, Integer objectY, int relationship, int outputtype)
    {

        String table = this.getRelationshipTable(relationship, outputtype);
        String column = this.getRelationshipColumn(relationship, outputtype);
        
        
        
        String termcolumn = "";
        String termtable = "";
        
        if(relationship == NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC)
        {
            termcolumn = "hpo_max";
            termtable = "hpo_term";
        }
        else
        {
            termcolumn = "go_max";
            termtable = "go_term";
        }
        
        String sql = "select distinct g."+column+"X, g."+column+"Y, h.id, h.name, g.ic from "+table+" g, "+termtable+" h "+ 
                     " where h.id = g."+termcolumn +
                     " and (("+column+"X ="+objectX+" and "+column+"Y ="+objectY+") "+
                           " or ("+column+"Y ="+objectX+" and "+column+"X ="+objectY+")) "+
                     "order by g.ic desc";
        
        
        System.out.println(sql);
        
        return this.executeSqlQuery(sql);
        
    }
    
    
    
    /**
     * The bipartite relationships are phenotypics. The inputtype is
 necesary for know the table where the common hpo term are stored:
  
  - hpo_omim_gene (input: omim) or  hpo_gene_omim (input: element)
     * 
     * @param objectX
     * @param objectY
     * @param inputtype
     * @param outputtype
     * @return 
     */
    public List<Object[]> getRelationshipTermsBipartite(Integer gene, Integer disease, int inputtype, int outputtype)
    {

        String table = this.getRelationshipTableBipartite(inputtype, outputtype);

                
        String termcolumn = "";
        String termtable = "";
        String diseasecolumn = "";
        
        if (inputtype == NetworkConstants.INPUT_TYPE_OMIM)
        {
            diseasecolumn = "omim";
            termcolumn = "hpo_omim";
        }
        else if (inputtype == NetworkConstants.INPUT_TYPE_ORPHA)
        {
            diseasecolumn = "orpha";
            termcolumn = "hpo_orpha";
        }


        
        String sql = "select distinct g.hpo_gene, g."+termcolumn+", h.id, h.name, g.ic from "+table+" g, hpo_term h "+ 
                     " where h.id = g.hpo_max"+
                     " and gene ="+gene+" and "+diseasecolumn+" ="+disease+
                     " order by g.ic desc";
        
        
        System.out.println(sql);
        
        return this.executeSqlQuery(sql);
        
    }
    
    
    
    
     
    /**
     * This method returns true if g1 and g2 are inferred.
     * @param g1
     * @param g2
     * @return 
     */
    public Set<Node> isContentInferredGenes_byOMIM(Gene geneX, Gene geneY) {

        PhenumaDB pdb = PhenumaDB.getInstance();
        
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        Set<Node> diseases = new HashSet<Node>();
        try{
          
            String query_geneX = "SELECT distinct disease FROM  disease_gene  WHERE gene = "+geneX.getEntrezid()+" and "+
                                 "disease in (select disease from disease_gene where gene ="+geneY.getEntrezid()+")";
             
            Query q_geneX = em.createNativeQuery(query_geneX);
            List<Integer> omimlist_geneX = q_geneX.getResultList();


            for (Integer omim : omimlist_geneX)
            {

                Disease d = em.find(Disease.class, omim);

                Node node = new NodeDisease(d);
                diseases.add(node);
                
            }
            
            
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        finally
        {
            em.close();
            emf.close();
        }
        
        
        return diseases;
       
    }
    
    
    /**
     * This method returns true if g1 and g2 are inferred.
     * @param g1
     * @param g2
     * @return 
     */
    public Set<Node> isContentInferredGenes_byOrpha(Gene geneX, Gene geneY) {
        PhenumaDB pdb = PhenumaDB.getInstance();
        
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        
        Set<Node> diseases = new HashSet<Node>();
        try{          
            
            String query = "SELECT distinct orphanum FROM gene_rare_disease  WHERE entrez = "+geneX.getEntrezid()+" and "+
                                 "orphanum in (select orphanum from gene_rare_disease where entrez ="+geneY.getEntrezid()+")";
             
            Query q = em.createNativeQuery(query);
            List<Integer> prphalist = q.getResultList();


            for (Integer orpha : prphalist)
            {

                RareDisease d = em.find(RareDisease.class, orpha);

                Node node = new NodeRareDisease(d);
                diseases.add(node);
                
            }
            
            
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        finally
        {
            em.close();
            emf.close();
        }
        
        return diseases;
    }
    

    
    public Set<Node> isContentInferredOMIM_byGene(Disease diseaseX, Disease diseaseY)
    {
        PhenumaDB pdb = PhenumaDB.getInstance();
        
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();
        

        Set<Node> genes = new HashSet<Node>();
        try
        {
            String query = "SELECT distinct gene FROM  disease_gene WHERE disease = " + diseaseX.getOmim() + " and " + "gene in (select gene from disease_gene where disease =" + diseaseY.getOmim() + ")";
 
            Query q = em.createNativeQuery(query);
            List<Integer> genelist = q.getResultList();
 
            for (Integer gene : genelist)
            {
                Gene g = (Gene)em.find(Gene.class, gene);
 
                Node node = new NodeGene(g);
                genes.add(node);
            }
 
        }
        catch (Exception ex)
        {
            System.out.println("Network Query ERROR: " + ex.getMessage());
        }
        finally
        {
            em.close();
            emf.close();
        }
        
        return genes;
   }

    
    
    public Set<Node> isContentInferredOrpha_byGene(RareDisease diseaseX, RareDisease diseaseY)
    {
        PhenumaDB pdb = PhenumaDB.getInstance();
        
        EntityManagerFactory emf = pdb.getFactory();
        EntityManager em = emf.createEntityManager();

        Set<Node> genes = new HashSet<Node>();
         
        try
        {
           String query = "SELECT distinct entrez FROM  gene_rare_disease WHERE orphanum = " + diseaseX.getOrphanum() + " and " + "entrez in (select entrez from gene_rare_disease where orphanum =" + diseaseY.getOrphanum() + ")";

           Query q = em.createNativeQuery(query);
           List<Integer> genelist = q.getResultList();

           for (Integer gene : genelist)
           {
             Gene g = (Gene)em.find(Gene.class, gene);

             Node node = new NodeGene(g);
             genes.add(node);
           }

         }
         catch (Exception ex)
         {
           System.out.println("Network Query ERROR: " + ex.getMessage());
         } 
         finally
         {
            em.close();
            emf.close();
        }

        
        return genes;
   }
	 

    /**
     * This method creates a string with the elements of a study set.
     * This string is used to be incorporated to to a sql query.
     * 
     * @param query
     * @return 
     */
    public String stringSetfromStudySet(StudySet query) 
    {

        if(query == null || query.getSize() == 0)
            return "()";
           
        
        String geneset = "(";
        for(ByteString gene : query.getAllNodesNames())
        {
            geneset = geneset + gene + ", ";
        }
        geneset = geneset.substring(0, geneset.length()-2) +")";
        

        return geneset;
    }

    
    //Utils
    
    
    /**
     * This method return de view of the bipartite networks.
     * 
     * @param querytype
     * @return 
     */
    public String getBipartiteView(int querytype)
   {
     if (NetworkUtils.isBipartiteGeneDiseases(querytype)) {
       return Constants.DATABASE+".gene_omim_relationships";
     }
     if (NetworkUtils.isBipartiteGeneOrphan(querytype)) {
       return Constants.DATABASE+".gene_orpha_relationships";
     }
 
     return "";
   }
    
  

   
    /**
     * This method returns the name of view used for each type of input
     * @param type
     * @return 
     */
    
    public String getTableName(int querytype)
    {
        if(NetworkUtils.isUnipartiteGenes(querytype) || querytype == NetworkConstants.QUERY_GENE_NETWORK)
            return Constants.GENE_GENE_RELATIONSHIPS_VIEW;
        
        if(NetworkUtils.isUnipartiteDiseases(querytype) || querytype == NetworkConstants.QUERY_DISEASE_NETWORK)
            return Constants.OMIM_OMIM_RELATIONSHIPS_VIEW;
        
        if(NetworkUtils.isUnipartiteOrphan(querytype) || querytype == NetworkConstants.QUERY_ER_NETWORK)
            return Constants.ORPHA_ORPHA_RELATIONSHIPS_VIEW;
        
        if(NetworkUtils.isBipartiteGeneDiseases(querytype))
            return Constants.GENE_OMIM_RELATIONSHIPS_VIEW;
        
        if(NetworkUtils.isBipartiteGeneOrphan(querytype))
            return Constants.GENE_ORPHA_RELATIONSHIPS_VIEW;
        return "";
        
    }
    
    
    
     /**
     * This method returns the name of view used for each type of input
     * @param type
     * @return 
     */
    
    public String getViewObjects(int inputtype)
    {
        if(NetworkConstants.INPUT_TYPE_GENES == inputtype)
            return Constants.GENE_GENE_RELATIONSHIPS_VIEW;
        
        if(NetworkConstants.INPUT_TYPE_OMIM == inputtype)
            return Constants.OMIM_OMIM_RELATIONSHIPS_VIEW;
        
        if(NetworkConstants.INPUT_TYPE_ORPHA == inputtype)
            return Constants.ORPHA_ORPHA_RELATIONSHIPS_VIEW;
        
        return "";
        
    }
    
   /**
     * This method returns the field name of a relationships in the database table
     * @param querytype
     * @param inputtype
     * @return 
     */ 
 
   public String getRelationshipsTag(int querytype, int inputtype)
   {
     String relationshiptag = "";
 
     if (NetworkUtils.isUnipartitePhenotypic(querytype)) relationshiptag = NetworkConstants.REL_PHENOTYPIC_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_GENE_SS_GOBP) relationshiptag = NetworkConstants.REL_FUNCTIONAL_GO_BP_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_GENE_SS_GOCC) relationshiptag = NetworkConstants.REL_FUNCTIONAL_GO_CC_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_GENE_SS_GOMF) relationshiptag = NetworkConstants.REL_FUNCTIONAL_GO_MF_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_OMIM) relationshiptag = NetworkConstants.REL_INFERRED_OMIM_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_ORPHAN) relationshiptag = NetworkConstants.REL_INFERRED_ORPHA_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_GENE_PPI) relationshiptag = NetworkConstants.REL_PPI_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_GENE_METABOLIC) relationshiptag = NetworkConstants.REL_METABOLIC_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_DISEASE_INFERRED_BY_GENE) relationshiptag = NetworkConstants.REL_INFERRED_GENE_TAG;
     else if (querytype == NetworkConstants.UNIPARTITE_ORPHAN_INFERRED_BY_GENE) relationshiptag = NetworkConstants.REL_INFERRED_GENE_TAG;
     else if (querytype == NetworkConstants.BIPARTITE_GENE_DISEASE_SS)
     {
       if (inputtype == NetworkConstants.INPUT_TYPE_GENES)
         relationshiptag = NetworkConstants.REL_PHENOTYPIC_OMIM_TAG;
       else
         relationshiptag = NetworkConstants.REL_PHENOTYPIC_GENE_TAG;
     }
     else if (querytype == 205)
     {
       if (inputtype == NetworkConstants.INPUT_TYPE_GENES)
         relationshiptag = NetworkConstants.REL_PHENOTYPIC_ORPHA_TAG;
       else {
         relationshiptag = NetworkConstants.REL_PHENOTYPIC_GENE_TAG;
       }
     }
     else if (querytype == 203) relationshiptag = NetworkConstants.REL_KNOWN_TAG;
     else if (querytype == 204) relationshiptag = NetworkConstants.REL_KNOWN_TAG;
 
     return relationshiptag;
   }

    
    
    
    /**
     * Get the name of the database field for each type of input.
     * 
     * @param type
     * @return 
     */
    public String getColumnName(int inputtype, int querytype)
    {
        if (!NetworkUtils.isBipartite(querytype))
        {
            if(NetworkUtils.isUnipartiteGenes(querytype) || querytype == NetworkConstants.QUERY_GENE_NETWORK)
                return NetworkConstants.COL_NAME_GENE;

            if(NetworkUtils.isUnipartiteDiseases(querytype) || querytype == NetworkConstants.QUERY_DISEASE_NETWORK)
                return NetworkConstants.COL_NAME_OMIM;

            if(NetworkUtils.isUnipartiteOrphan(querytype) || querytype == NetworkConstants.QUERY_ER_NETWORK)
                return NetworkConstants.COL_NAME_ORPHA;
        }
        else
        {
            if(NetworkUtils.isBipartiteGeneDiseases(querytype))
            {
                if(inputtype == NetworkConstants.INPUT_TYPE_GENES)
                    return NetworkConstants.COL_NAME_GENE;
                else if(inputtype == NetworkConstants.INPUT_TYPE_OMIM)
                    return NetworkConstants.COL_NAME_OMIM;
                
            }
            else if (NetworkUtils.isBipartiteGeneOrphan(querytype))
            {
                if(inputtype == NetworkConstants.INPUT_TYPE_GENES)
                    return NetworkConstants.COL_NAME_GENE;
                else if(inputtype == NetworkConstants.INPUT_TYPE_ORPHA)
                    return NetworkConstants.COL_NAME_ORPHA;

            }
        }
        return "";
        
    }
    
    
    /**
     * Get the name of the database field for each type of input.
     * 
     * @param type
     * @return 
     */
    public String getColumnsNameBipartite(int inputtype, int querytype)
    {
        if (NetworkUtils.isBipartite(querytype))
        {
            if(NetworkUtils.isBipartiteGeneDiseases(querytype))
            {
                if(inputtype == NetworkConstants.INPUT_TYPE_GENES)
                    return NetworkConstants.COL_NAME_OMIM+", omimname";
                else if(inputtype == NetworkConstants.INPUT_TYPE_OMIM)
                    return NetworkConstants.COL_NAME_GENE+", genename";
                
            }
            else if (NetworkUtils.isBipartiteGeneOrphan(querytype))
            {
                if(inputtype == NetworkConstants.INPUT_TYPE_GENES)
                    return NetworkConstants.COL_NAME_ORPHA+", orphaname";
                else if(inputtype == NetworkConstants.INPUT_TYPE_ORPHA)
                    return NetworkConstants.COL_NAME_GENE+", genename";

            }
        }
        return "";
        
    }
    
    
    
    /**
     * Get relationship table
     * 
     * 
     */
    public String getRelationshipTable(int relationship, int outputtype)
    {
        String table = "";
        
        
        
        if (relationship == NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC)
        {
            if (NetworkUtils.isUnipartiteGenes(outputtype) || outputtype == NetworkConstants.QUERY_GENE_NETWORK)
            {
                return "hpo_max_gene";           
            }
            else if (NetworkUtils.isUnipartiteDiseases(outputtype) || outputtype == NetworkConstants.QUERY_DISEASE_NETWORK)
            {
                return "hpo_max_omim";
            }
            else if (NetworkUtils.isUnipartiteOrphan(outputtype) || outputtype == NetworkConstants.QUERY_ER_NETWORK)
            {
                return "hpo_max_orphan";
            } 
           
        }
        else if (relationship == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP)
        {
            return "go_max_gene_bp";
        }
        else if (relationship == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC)
        {
            return "go_max_gene_cc";
        }
        else if (relationship == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF)
        {
            return "go_max_gene_mf";
        }
   
        return table;
    }
    
    
        /**
     * Get relationship table
     * 
     * 
     */
    public String getRelationshipTableBipartite(int inputtype, int outputtype)
    {
        String table = "";
        
        if (NetworkUtils.isBipartiteGeneDiseases(outputtype))
        {
            if (inputtype == NetworkConstants.INPUT_TYPE_OMIM)
                return "hpo_max_omim_gene";
            else if (inputtype == NetworkConstants.INPUT_TYPE_GENES)
                return "hpo_max_gene_omim";
        }
        else if (NetworkUtils.isBipartiteGeneOrphan(outputtype))
        {
             if (inputtype == NetworkConstants.INPUT_TYPE_ORPHA)
                return "hpo_max_orpha_gene";
             else if (inputtype == NetworkConstants.INPUT_TYPE_GENES)
                return "hpo_max_gene_orpha";
        }
           

      
        return table;
    }

    
     /**
     * Get relationship table
     * 
     * 
     */
    public String getRelationshipColumn(int relationship, int outputtype)
    {
        String table = "";
        
        
        if (relationship == NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC)
        {
            if (NetworkUtils.isUnipartiteGenes(outputtype) || outputtype == NetworkConstants.QUERY_GENE_NETWORK)
            {
                return "gene";           
            }
            else if (NetworkUtils.isUnipartiteDiseases(outputtype) || outputtype == NetworkConstants.QUERY_DISEASE_NETWORK)
            {
                return "omim";
            }
            else if (NetworkUtils.isUnipartiteOrphan(outputtype) || outputtype == NetworkConstants.QUERY_ER_NETWORK)
            {
                return "orphan";
            }
            
        }
        else if (relationship == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP)
        {
            return "gene";
        }
        else if (relationship == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC)
        {
            return "gene";
        }
        else if (relationship == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF)
        {
            return "gene";
        }

        
        
        return table;
    }

    /**
     * 
     * @param elementId
     * @param querytype 
     */
    private boolean existsElement(String elementId, int querytype) {
        
        if(NetworkUtils.isUnipartiteGenes(querytype))
        {
           Gene gene = this.getGeneByEntrezId(elementId);
           return (gene!=null);
        }
        else if (NetworkUtils.isUnipartiteDiseases(querytype))
        {
            Disease disease = this.getDiseaseByOmim(elementId);
            return (disease!=null);
        }
        else if (NetworkUtils.isUnipartiteOrphan(querytype))
        {
            RareDisease orpha = this.getRareDiseasesByOrphanum(elementId);
            return (orpha!=null);
        }
        
        return false;
    }
    
 
}
