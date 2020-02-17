package phenuma.managedbeans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import ontologizer.PopulationSet;
import ontologizer.StudySet;
import ontologizer.StudySetFactory;
import ontologizer.association.AssociationContainer;
import ontologizer.calculation.AbstractGOTermProperties;
import ontologizer.calculation.AbstractHypergeometricCalculation;
import ontologizer.calculation.EnrichedGOTermsResult;
import ontologizer.calculation.SemanticCalculation;
import ontologizer.calculation.TermForTermCalculation;
import ontologizer.calculation.TermForTermGOTermProperties;
import ontologizer.go.OBOParserException;
import ontologizer.go.Ontology;
import ontologizer.go.Term;
import ontologizer.go.TermID;
import ontologizer.statistics.Bonferroni;
import ontologizer.types.ByteString;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import phenomizer.hpo.Phenuma;
import phenomizer.utils.NumberUtils;
import phenuma.constants.Constants;
import phenuma.cysnetwork.CysNetwork;
import phenuma.cysnetwork.CytoscapeConstants;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.entities.Disease;
import phenuma.entities.Gene;
import phenuma.entities.RareDisease;
import phenuma.network.EdgeSemanticInfo;
import phenuma.network.NetworkDB;
import phenuma.network.Node;
import phenuma.network.PhenotypeNetworkDB;
import phenuma.networkproyection.NetworkConstants;
import phenuma.networkproyection.NetworkUtils;
import phenuma.utils.PhenumaException;
import phenuma.utils.HTMLUtils;
import phenuma.utils.JSONUtils;
import phenuma.utils.Utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rocío Rodríguez López
 */
public class DataTableManagedBean implements Serializable{

    static final Logger logger = Logger.getLogger(DataTableManagedBean.class);
   
    
    /*Selected node and edge in the tables to include additional information below*/
    private String selectedNode;
    private String phenotypeTable;
    
    private String selectedEdge;
    private String relationshipInfoTable;
   
    /*Selected nodes in the network for enrichment calculation*/
    private String selectedNodes;
    private String enrichmentTable;
    
    /*Output file*/
    private StreamedContent outputFile;  
    private EnrichedGOTermsResult currentEnrichment;
    
    
    /*phenotypes qtip*/
    private String qtip;
    
    /** Creates a new instance of infoManagedBean */
    public DataTableManagedBean() {
    }


    public String getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(String selectedNode) {
        this.selectedNode = selectedNode;
    }


    public String getPhenotypeTable() {
        return phenotypeTable;
    }

    public void setPhenotypeTable(String phenotypeTable) {
        this.phenotypeTable = phenotypeTable;
    }

    public String getRelationshipInfoTable() {
        return relationshipInfoTable;
    }

    public void setRelationshipInfoTable(String relationshipInfoTable) {
        this.relationshipInfoTable = relationshipInfoTable;
    }

    public String getSelectedEdge() {
        return selectedEdge;
    }

    public void setSelectedEdge(String selectedEdge) {
        this.selectedEdge = selectedEdge;
    }

    public String getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(String selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public String getEnrichmentTable() {
        return enrichmentTable;
    }

    public void setEnrichmentTable(String enrichmentTable) {
        this.enrichmentTable = enrichmentTable;
    }

    public EnrichedGOTermsResult getCurrentEnrichment() {
        return currentEnrichment;
    }

    public void setCurrentEnrichment(EnrichedGOTermsResult currentEnrichment) {
        this.currentEnrichment = currentEnrichment;
    }

    public String getQtip() {
        return qtip;
    }

    public void setQtip(String qtip) {
        this.qtip = qtip;
    }
   
    public void click(){
        this.phenotypeTable = this.phenotypesTable();
    }
    
    public void edgeClick(){
        try {
       
            this.phenotypeTable = relationshipsTableDB();
        } catch (IOException ex) {
             logger.error(ex.getMessage());
        } catch (OBOParserException ex) {
             logger.error(ex.getMessage());
        } catch (PhenumaException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    public void enrichmentPhenotypesClick(){
        try {
            
            Application app = FacesContext.getCurrentInstance().getApplication();
            ExpressionFactory exprFactory = app.getExpressionFactory();
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            ValueExpression valExpr = exprFactory.createValueExpression(elContext, "#{networkFormBean.currentCysNetwork}", CysNetwork.class);

            CysNetwork cysnetwork = (CysNetwork)valExpr.getValue(elContext);

            Phenuma p = Phenuma.getInstance();
            
            NetworkDB network = cysnetwork.getNetworkDB();
            AssociationContainer assocs = null;

            if (NetworkUtils.isUnipartiteGenes(network.getQuerytype()) || NetworkUtils.isPhenotypeQuery(network.getQuerytype())) {
                assocs = p.getGenes_hpo_associations();
            }
            else if (NetworkUtils.isUnipartiteDiseases(network.getQuerytype()) || NetworkUtils.isPhenotypeQuery(network.getQuerytype())) {
                assocs = p.getDiseases_hpo_associations();
            }
            else if (NetworkUtils.isUnipartiteOrphan(network.getQuerytype()) || NetworkUtils.isPhenotypeQuery(network.getQuerytype())) {
                assocs = p.getEr_hpo_associations();
            }

            this.enrichmentTable = enrichmentTable(Phenuma.getInstance().getHuman_phenotype_ontology(), assocs);
            
        } catch (IOException ex) {
             logger.error(ex.getMessage());
        } catch (OBOParserException ex) {
             logger.error(ex.getMessage());
        }
  
    }
        
    public void enrichmentGOBPClick(){
        try {
            Phenuma p = Phenuma.getInstance();
            this.enrichmentTable = enrichmentTable(p.getGene_ontology_bp(), p.getGenes_go_associations_bp());
        } catch (IOException ex) {
             logger.error(ex.getMessage());
        } catch (OBOParserException ex) {
             logger.error(ex.getMessage());
        }
  
    }
            
    public void enrichmentGOMFClick(){
        try {
            Phenuma p = Phenuma.getInstance();
            this.enrichmentTable = enrichmentTable(p.getGene_ontology_mf(), p.getGenes_go_associations_mf());
        } catch (IOException ex) {
             logger.error(ex.getMessage());
        } catch (OBOParserException ex) {
             logger.error(ex.getMessage());
        }
  
    }
    
    public void enrichmentGOCCClick()
    {

        try {
            Phenuma p = Phenuma.getInstance();
            this.enrichmentTable = enrichmentTable(p.getGene_ontology_cc(), p.getGenes_go_associations_cc());
        } catch (IOException ex) {
             logger.error(ex.getMessage());
        } catch (OBOParserException ex) {
             logger.error(ex.getMessage());
        }
  
    }
    
    public void clearEnrichmentClick(){

            this.currentEnrichment = null;
            this.enrichmentTable = clearEnrichmentTable();
  
    }  
    
    
    /**
     * Qtips info (provisional)
     * @param file 
     */
    
    public void getNodeInformationQtip()
    {
        try {
            this.qtip = nodeInformationQtip();
        } catch (PhenumaException ex) {
             logger.error(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
             logger.error(ex.getMessage());
            
        }
    }
    

    public void getEdgeInformationQtip()
    {
        try {
            this.qtip = edgeInformationQtip();
        } catch (PhenumaException ex) {
            java.util.logging.Logger.getLogger(DataTableManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DataTableManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OBOParserException ex) {
            java.util.logging.Logger.getLogger(DataTableManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    
    public void setOutputFile(StreamedContent file)
    {
        this.outputFile = file; 
    }
    
    
    
    public StreamedContent getOutputFile() {  
       return this.outputFile;
    }    
    
    
    public void createOutputFile()
    {
         try {
            String data = Utils.enrichment2string(this.currentEnrichment);
            
            InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
            outputFile = new DefaultStreamedContent(is,"text","enrichment.txt");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage());
        }  
    }
    
    
    /**
     * HTML table with the information of the relaionships between two objects.
     * @return
     * @throws PhenumaException
     * @throws IOException
     * @throws OBOParserException 
     */
    public String relationshipsTableDB() throws PhenumaException, IOException, OBOParserException{
    
        Application app = FacesContext.getCurrentInstance().getApplication();
        ExpressionFactory exprFactory = app.getExpressionFactory();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ValueExpression valExpr = exprFactory.createValueExpression(elContext, "#{networkFormBean.currentCysNetwork}", CysNetwork.class);
        
        CysNetwork cysnetwork = (CysNetwork)valExpr.getValue(elContext);
        
        //Current network
        NetworkDB n = cysnetwork.getNetworkDB();
        
        //Get row of the selected edge
        
        String table = "";
        
        if(this.selectedEdge == null)
        {
            table = "";
        }
        else if (!this.selectedEdge.startsWith("0*"))
        {

            String[] strs = this.selectedEdge.split("\\*");
            
            
            Integer rel_type = new Integer(strs[2]);
            System.out.println("Relationship type: "+rel_type);
            
            Integer nodeX =  new Integer(strs[0]);
            Integer nodeY =  new Integer(strs[1]);


            //Semantic Similarity relationships            
            if (rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP || rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC ||
                rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF || rel_type == NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC)
            {
                if(!NetworkUtils.isBipartite(n.getQuerytype()))
                {
                    table =  semanticEdgeInfoHTML_DB(nodeX, nodeY, rel_type, n);
                }
                else
                {
                    table =  semanticEdgeInfoHTML(nodeX, nodeY, rel_type,n);
                }
            }

            //Inferred relationships
            if (rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE || rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_OMIM || rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_ORPHA)
            {
                table =  inferredObjectsHTML(nodeX, nodeY, rel_type, n);
            }

        }
        else
        {
            //Phenotypic Query
            PhenotypeNetworkDB phenotype_network = (PhenotypeNetworkDB)cysnetwork.getNetworkDB();
   
            Object[] edge = findEdge(phenotype_network.getResultRelationships());
            
            if(edge!=null)
            {
                String node = NumberUtils.object2string(edge[1]);
                if(node.isEmpty())
                {
                   node = (String)edge[1];
                }
  
                logger.info("Displaying "+this.selectedEdge);
                table =  semanticEdgeInfoHTML(phenotype_network.getQueryTerms(), new Integer(node), phenotype_network);
            }
            
            
        }
        
        return table;
    }

    private Object[] findEdge(List<Object[]> edges) {
        
        if(edges != null)
        {
            int i = 0;
            while(i < edges.size() && !isEqualtoEdgeDB(this.selectedEdge, edges.get(i)))
            {
                i++;
            }

            if(i < edges.size()){
                logger.info("Found "+edges.get(i)[0]);
                return edges.get(i);
            }
        }
        
        return null;
    }


    /**
     * Get from de database the list of terms related with an object
     * @param object
     * @param type
     * @return
     * @throws NumberFormatException
     * @throws IOException
     * @throws OBOParserException 
     */
    private List<Object[]> getTermList(Integer object, Integer type) throws NumberFormatException, IOException, OBOParserException {
        
        List<Object[]> terms = new ArrayList<Object[]>();
        
        Double max_ic = 0.0;
        
        Application app = FacesContext.getCurrentInstance().getApplication();
        ExpressionFactory exprFactory = app.getExpressionFactory();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ValueExpression valExpr = exprFactory.createValueExpression(elContext, "#{networkFormBean.currentCysNetwork}", CysNetwork.class);

        CysNetwork cysnetwork = (CysNetwork)valExpr.getValue(elContext);
        
        DatabaseQueries dbq = new DatabaseQueries();
        
        
        if (type.equals(CytoscapeConstants.CYS_NODE_TYPE_GENE))
        {
            terms = dbq.getHPOTerms(new Integer(object), NetworkConstants.INPUT_TYPE_GENES);                 
        }
        else if (type.equals(CytoscapeConstants.CYS_NODE_TYPE_OMIM))
        {
            terms = dbq.getHPOTerms(new Integer(object), NetworkConstants.INPUT_TYPE_OMIM);   
          
        }
        else if (type.intValue() >= CytoscapeConstants.CYS_NODE_TYPE_ORPHA)
        {
            terms = dbq.getHPOTerms(new Integer(object), NetworkConstants.INPUT_TYPE_ORPHA); 

        }
        else if (type.equals(CytoscapeConstants.CYS_NODE_TYPE_QUERY))
        {
            
            Ontology hpo = Phenuma.getInstance().getHuman_phenotype_ontology();
            SemanticCalculation sc = null;
            
            PhenotypeNetworkDB n = (PhenotypeNetworkDB)cysnetwork.getNetworkDB();
            
            if (n.getQuerytype() == NetworkConstants.QUERY_GENE_NETWORK) {
                sc = Phenuma.getInstance().getSc_hpo_genes();
                max_ic = new Double(Constants.MAX_IC_GENES_HPO);
            }
            else if(n.getQuerytype() == NetworkConstants.QUERY_DISEASE_NETWORK) {
                sc = Phenuma.getInstance().getSc_hpo_diseases();
                max_ic = new Double(Constants.MAX_IC_DISEASES_HPO);
            }        
            else if(n.getQuerytype() == NetworkConstants.QUERY_ER_NETWORK) {
                sc = Phenuma.getInstance().getSc_hpo_er();
                max_ic = new Double(Constants.MAX_IC_ORPHAN_HPO);
            } 
            
            List<TermID> termlist = n.getQueryTerms();
            
            for(TermID t : termlist)
            {
                Term term = hpo.getTerm(t);
                
                String name = "Term removed/moved from the ontology.";
                if (term != null) {
                    name = term.getName();
                }
                
                Object[] obj = {t.id, name, sc.informationContent(t)/max_ic};
                terms.add(obj);
            }
        }
        return terms;
    }
    
    
    
    
    /**
     * 
     * @param rel_type
     * @param network
     * @param nodeX
     * @param nodeY
     * @return 
     */
    
    private Set<Node> getInferredObjects(Integer rel_type, NetworkDB network, Integer nodeX, Integer nodeY) {
        
        DatabaseQueries q = new DatabaseQueries();
        
        Set<Node> nodes = null;
        
        if (rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE)
        {        
            if (NetworkUtils.isUnipartiteDiseases(network.getQuerytype()) || network.getQuerytype() == NetworkConstants.QUERY_DISEASE_NETWORK)
            {
                nodes = q.isContentInferredOMIM_byGene(new Disease(nodeX), new Disease(nodeY));
            }
            else if (NetworkUtils.isUnipartiteOrphan(network.getQuerytype()) || network.getQuerytype() == NetworkConstants.QUERY_ER_NETWORK)
            {
                nodes = q.isContentInferredOrpha_byGene(new RareDisease(nodeX), new RareDisease(nodeY));
            }
        }    
        else if (rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_OMIM)
        {
             nodes = q.isContentInferredGenes_byOMIM(new Gene(nodeX), new Gene(nodeY));
        } 
        else if (rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_ORPHA)
        {
             nodes = q.isContentInferredGenes_byOrpha(new Gene(nodeX), new Gene(nodeY));
        }
        return nodes;
    }

    
    
    /**
     * This method creates the HTML code of a table of inferred objects of a relationship.
     * 
     * @param nodeX
     * @param nodeY
     * @param rel_type
     * @param n
     * @return
     * @throws PhenumaException 
     */
    private String inferredObjectsHTML( Integer nodeX, Integer nodeY, Integer rel_type, NetworkDB n) throws PhenumaException {
        
        Set<Node> nodes = getInferredObjects(rel_type, n, nodeX, nodeY);
        
        String table;
        
        table = HTMLUtils.createInferredDataTable(nodes, rel_type);
        
        return table;
    }
    
    /**
     * This method creates the HTML code of a table of inferred objects of a relationship.
     * 
     * @param nodeX
     * @param nodeY
     * @param rel_type
     * @param n
     * @return
     * @throws PhenumaException 
     */
    private String inferredObjectsJSON( Integer nodeX, Integer nodeY, Integer rel_type, NetworkDB n) throws PhenumaException {
        
        Set<Node> nodes = getInferredObjects(rel_type, n, nodeX, nodeY);
        
        String table;
        
        table = JSONUtils.createInferredJSON_DB(nodes, rel_type);
        
        return table;
    }
    
    
    /**
     * Creates HTML table that includes the common ontological terms between two objects. These terms
     * are used to calculate the semantic similarity.
     * @param nodeX
     * @param nodeY
     * @param rel_type
     * @param network
     * @return
     * @throws IOException
     * @throws OBOParserException
     * @throws PhenumaException 
     */

     private String semanticEdgeInfoHTML_DB(Integer nodeX, Integer nodeY, Integer rel_type,  NetworkDB network) throws IOException, OBOParserException, PhenumaException {
        
        logger.info("Displaying "+nodeX+" "+nodeY+" "+rel_type+" edge");
        
        DatabaseQueries dbq = new DatabaseQueries();
            
        List<Object[]> rows = dbq.getRelationshipTerms(nodeX, nodeY, rel_type, network.getQuerytype());
        
        String table = HTMLUtils.createSemanticDataTable_DB(rows, rel_type);

        return table;
     }
     
     
     /**
      * Creates JSON structure that includes the common ontological terms between two objects. These terms
      * are used to calculate the semantic similarity.
      * @param nodeX
      * @param nodeY
      * @param rel_type
      * @param network
      * @return
      * @throws IOException
      * @throws OBOParserException
      * @throws PhenumaException 
      */
     private String semanticEdgeInfoJSON_DB(Integer nodeX, Integer nodeY, Integer rel_type,  NetworkDB network) throws IOException, OBOParserException, PhenumaException {
        
        logger.info("Displaying "+nodeX+" "+nodeY+" "+rel_type+" edge");
        
        DatabaseQueries dbq = new DatabaseQueries();
            
        List<Object[]> rows = dbq.getRelationshipTerms(nodeX, nodeY, rel_type, network.getQuerytype());
        
        String table = JSONUtils.createSemanticJSON_DB(rows, 1.0, rel_type);

        return table;
     }
    
    
    /**
     *  Create HTML table with the semantic similarity info using the ontology. This method is used 
     *  in bipartite relationships because the terms of these relatinships are not included in the database.
     * 
     * @param rel_type
     * @param nodeX
     * @param nodeY
     * @param n
     * @return
     * @throws IOException
     * @throws OBOParserException 
     */

    private String semanticEdgeInfoHTML(Integer nodeX, Integer nodeY, Integer rel_type,  NetworkDB n) throws IOException, OBOParserException, PhenumaException {
       
        logger.info("Displaying "+nodeX+" "+nodeY+" "+rel_type+" edge");
        
        Double max_ic = 0.0;
//        //Get semantic similarity info
//        
        List<EdgeSemanticInfo> semanticinfo = null;
        SemanticCalculation sc = null;
//        
//        if (rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP){
//
//            sc = Phenuma.getInstance().getSc_go_bp_genes();
//            semanticinfo = sc.resnikSimilarityMaximumTerms(new ByteString(nodeX.toString()), new ByteString(nodeY.toString()));
//            max_ic = new Double(Constants.MAX_IC_GENES_GOBP);
//        }
//        else if(rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC){
//            sc = Phenuma.getInstance().getSc_go_cc_genes();
//            semanticinfo = sc.resnikSimilarityMaximumTerms(new ByteString(nodeX.toString()), new ByteString(nodeY.toString()));
//            max_ic = new Double(Constants.MAX_IC_GENES_GOCC);
//        }
//        else if(rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF){
//
//            sc = Phenuma.getInstance().getSc_go_mf_genes();
//            semanticinfo = sc.resnikSimilarityMaximumTerms(new ByteString(nodeX.toString()), new ByteString(nodeY.toString()));
//            max_ic = new Double(Constants.MAX_IC_GENES_GOMF);
//        }
//        else{
//            
//            if(!NetworkUtils.isBipartite(n.getQuerytype()))
//            {
//                if(NetworkUtils.isUnipartiteGenes(n.getQuerytype()) || n.getQuerytype()==NetworkConstants.QUERY_GENE_NETWORK){
//                    sc = Phenuma.getInstance().getSc_hpo_genes();
//                    max_ic =new Double(Constants.MAX_IC_GENES_HPO);
//                }else if (NetworkUtils.isUnipartiteDiseases(n.getQuerytype())|| n.getQuerytype()==NetworkConstants.QUERY_DISEASE_NETWORK){
//                    sc = Phenuma.getInstance().getSc_hpo_diseases();
//                    max_ic = new Double(Constants.MAX_IC_DISEASES_HPO);
//                }else if (NetworkUtils.isUnipartiteOrphan(n.getQuerytype())|| n.getQuerytype()==NetworkConstants.QUERY_ER_NETWORK){
//                    sc = Phenuma.getInstance().getSc_hpo_er();
//                    max_ic = new Double(Constants.MAX_IC_ORPHAN_HPO);
//                }
//
//
//                semanticinfo = sc.resnikSimilarityMaximumTerms(new ByteString(nodeX.toString()), new ByteString(nodeY.toString()));
//                
//            }
//            else
//            {
                //Bipartite networks: nodeX == Gene, nodeY == Disease or Rare Disease
                Integer gene = nodeX;
                Integer disease = nodeY;
                
                AssociationContainer assoc = null;
                ArrayList<TermID> terms = new ArrayList<TermID>();
                
     
                
                if(NetworkUtils.isBipartiteGeneDiseases(n.getQuerytype()) && n.getInputtype() == NetworkConstants.INPUT_TYPE_GENES)
                {
                    sc = Phenuma.getInstance().getSc_hpo_diseases();
                    
                    //Get gene terms
                    assoc = Phenuma.getInstance().getGenes_hpo_associations();
                    terms = assoc.get(new ByteString(gene.toString())).getAssociations();
                    max_ic = new Double(phenuma.constants.Constants.MAX_IC_DISEASES_HPO);
                    
                    semanticinfo = sc.resnikSimilarityMaximumTerms(terms, new ByteString(disease.toString()));
                }
                else if(NetworkUtils.isBipartiteGeneDiseases(n.getQuerytype()) && n.getInputtype() == NetworkConstants.INPUT_TYPE_OMIM)
                {
                    sc = Phenuma.getInstance().getSc_hpo_genes();
                    
                    //Get disease terms
                    assoc = Phenuma.getInstance().getDiseases_hpo_associations();
                    terms = assoc.get(new ByteString(disease.toString())).getAssociations();
                    max_ic =  new Double(phenuma.constants.Constants.MAX_IC_GENES_HPO);
                    
                    semanticinfo = sc.resnikSimilarityMaximumTerms(terms, new ByteString(gene.toString()));
                }
                else if (NetworkUtils.isBipartiteGeneOrphan(n.getQuerytype()) && n.getInputtype() == NetworkConstants.INPUT_TYPE_GENES)
                { 
                    sc = Phenuma.getInstance().getSc_hpo_er();
                    
                    assoc = Phenuma.getInstance().getGenes_hpo_associations();
                    terms = assoc.get(new ByteString(gene.toString())).getAssociations();
                    max_ic =  new Double(phenuma.constants.Constants.MAX_IC_ORPHAN_HPO);
                   
                    semanticinfo = sc.resnikSimilarityMaximumTerms(terms, new ByteString(disease.toString()));
                }
                else if (NetworkUtils.isBipartiteGeneOrphan(n.getQuerytype()) && n.getInputtype() == NetworkConstants.INPUT_TYPE_ORPHA)
                { 
                    sc = Phenuma.getInstance().getSc_hpo_genes();
                    
                    assoc = Phenuma.getInstance().getEr_hpo_associations();
                    terms = assoc.get(new ByteString(disease.toString())).getAssociations();
                    max_ic =  new Double(phenuma.constants.Constants.MAX_IC_GENES_HPO);
                    
                    semanticinfo = sc.resnikSimilarityMaximumTerms(terms, new ByteString(gene.toString()));
                }
                 
            
        
        
        
        //Create HTML table
        return HTMLUtils.createSemanticDataTable(semanticinfo, max_ic, rel_type);
    }
    
    
    /**
     * HTML table for the information of a relationship selected between a Query of phenotypes and an object.
     * 
     * @param inputquery
     * @param node
     * @param n
     * @return
     * @throws IOException
     * @throws OBOParserException 
     */
    private String semanticEdgeInfoHTML(ArrayList<TermID> inputquery, Integer node,  NetworkDB n) throws IOException, OBOParserException, PhenumaException {
       
        String table = "";
        Double max_ic = 0.0;
        //Get semantic similarity info
        
        List<EdgeSemanticInfo> semanticinfo = null;
        SemanticCalculation sc = null;
        
        if (n.getQuerytype() == NetworkConstants.QUERY_GENE_NETWORK) {
            sc = Phenuma.getInstance().getSc_hpo_genes();
            max_ic = new Double(Constants.MAX_IC_GENES_HPO);
        }
        else if(n.getQuerytype() == NetworkConstants.QUERY_DISEASE_NETWORK) {
            sc = Phenuma.getInstance().getSc_hpo_diseases();
            max_ic = new Double(Constants.MAX_IC_DISEASES_HPO);
        }        
        else if(n.getQuerytype() == NetworkConstants.QUERY_ER_NETWORK) {
            sc = Phenuma.getInstance().getSc_hpo_er();
            max_ic =new Double( Constants.MAX_IC_ORPHAN_HPO);
        } 
        
        semanticinfo = sc.resnikSimilarityMaximumTerms(inputquery, new ByteString(node.toString()));
        
        return HTMLUtils.createSemanticDataTable(semanticinfo, max_ic, NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC);
        
    }
    
    /**
     * Compare two edges. 
     * 
     * @param edge
     * @param e
     * @return 
     */
    
    public boolean isEqualtoEdgeDB(String edge, Object[] e){
        
        String[] strs = edge.split("\\*");
       
        if(!strs[0].equals("0") && !strs[1].equals("0"))
        {
            Integer node1_id = (Integer) e[NetworkConstants.DATABASE_MATRIX_OBJECTX_POS];
            Integer node2_id = (Integer) e[NetworkConstants.DATABASE_MATRIX_OBJECTY_POS];
           // String rel_type = new Integer(e.getRelationship_type()).toString();

            boolean isequal_onedirection   = node1_id.toString().equals(strs[0]) && node2_id.toString().equals(strs[1]);//&&  rel_type.equals(strs[2]);

            boolean isequal_otherdirection = node2_id.toString().equals(strs[0]) && node1_id.toString().equals(strs[1]);// &&  rel_type.equals(strs[2]);
            
            return isequal_onedirection || isequal_otherdirection;
        }
        else 
        {
            // Si alguno de los nodos es la consulta de fenotipos comparar con el objeto que se relaciona
            // para determinar si son iguales.
            if (strs[0].trim().equals("0"))
            {
               return e[1].toString().equals(strs[1].toString());
            }
           
            
            if (strs[1].trim().equals("0"))
            {
                return e[0].toString().equals(strs[0].toString());
            }
        }
        
        return false;
        
        
    }
    
    
    /**
     * This method creates a HTML table with the HPO terms related with the selected node.
     * 
     * @return string object with the HTML table.
     */
    public String phenotypesTable(){

        String[] strs = this.selectedNode.split(":");
        
        String table = "";
                
        if (strs.length == 2){
            Integer object = new Integer(strs[1]);
            Integer type = new Integer(strs[0]);

            try {

                List<Object[]> terms = getTermList(object, type); 
                
                table = HTMLUtils.createPhenotypeTable(terms);                

            }catch (IOException ex) {
                java.util.logging.Logger.getLogger(DataTableManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OBOParserException ex) {
                java.util.logging.Logger.getLogger(DataTableManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        return table;
    }

    
    /**
     * @param ontology
     * @param assocs
     * @return 
     */
    public String enrichmentTable(Ontology ontology, AssociationContainer assocs){
            
        String table = "";
         
        try
        {
            Phenuma p = Phenuma.getInstance();
             
            
            table = "<div style=\"height:90%;width:100%;overflow:auto;\" onmouseout=\"removeFilterEnriched()\">"
                  + "<table id=\"enrichmentTable\" class=\"phenumatable\"> <thead><tr class=\"phenumarow\"> "
                + "<th onclick=\"SortTable(0, \'enrichmentTable\', 'Term', 'text');\"> Term </th> "
                + "<th onclick=\"SortTable(1, \'enrichmentTable\', 'aso', 'pairs');\"> ASO/APO </th> "
                + "<th onclick=\"SortTable(2, \'enrichmentTable\', 'pvalue','scientific');\"> P-Value </th> "
                + "</tr></thead><tbody>";
            
            String[] strs = this.selectedNodes.split(",");
           

            if (assocs != null)
            {
                PopulationSet populationSet = (PopulationSet) StudySetFactory.createFromByteStringSet(assocs.getAllAnnotatedPP(), true);
                StudySet studySet = StudySetFactory.createFromArray(strs, true);

                AbstractHypergeometricCalculation t4t_cal = new TermForTermCalculation();
                currentEnrichment = t4t_cal.calculateStudySet(ontology, assocs, populationSet, studySet, new Bonferroni());

                Iterator<AbstractGOTermProperties> iter = currentEnrichment.iterator();

                while(iter.hasNext())
                {
                    TermForTermGOTermProperties properties = (TermForTermGOTermProperties) iter.next();
                    
                    
                   /* if (properties.p_adjusted < 0.005)
                    {*/
                        String nodeslist = "[";
                        for(ByteString node : properties.getOmimlist())
                        {
                            nodeslist = nodeslist + "\'" + node.toString() + "\',";
                        }

                        nodeslist.substring(0, nodeslist.length()-3);
                        nodeslist = nodeslist + "]";


                        table = table + "<tr class=\"phenumarow\">"
                                + "<td class='phenumadata_text' onclick=\"filterEnriched("+nodeslist+")\">"+properties.goTerm.getName()+" ("+properties.goTerm.getID()+")"+"</td>"
                                + "<td class='phenumadata'>"+properties.annotatedStudyGenes+"/"+properties.annotatedPopulationGenes+"</td>"
                                + "<td class='phenumadata'>"+Utils.double2scientificnotation(properties.p_adjusted)+"</td>"
                                + "</tr>";

                   /* }*/
                }
            }else{
                table = "<span> This information is not available for this type of network. </span>";
            }
            
            
            table = table + "</table></div>";
           

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DataTableManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return table;
    }
  
    
    public String nodeInformationQtip() throws PhenumaException, UnsupportedEncodingException
    {
        String[] strs = this.selectedNode.split(":");
        
        Integer type = new Integer(strs[0]);
        String  name = strs[1];

        String qtips_phenotypes = "";
        
        if (strs.length == 2){
            
           qtips_phenotypes = nodeInformationJSON(name, type);
         
        }
        return qtips_phenotypes;
        
           
    }
    
    public String nodeInformationJSON(String name, Integer type) throws PhenumaException, UnsupportedEncodingException
    {
        DatabaseQueries dbq = new DatabaseQueries();
        
        List<Object[]> terms_hpo = new ArrayList<Object[]>();
        
        String object_data = "";
        String object_data_lists = "";
        
        if (type.equals(CytoscapeConstants.CYS_NODE_TYPE_GENE))
        {
            Gene gene = dbq.getGeneByEntrezId(name);
            
            object_data = "fullname: '"+gene.getName()+"', location: '"+gene.getLocation()+"', chromosome: '"+gene.getChromosome()+"', genetype:'"+gene.getGenetype()+"'";
            
            terms_hpo        = dbq.getHPOTerms(new Integer(name), NetworkConstants.INPUT_TYPE_GENES);  
            object_data_lists =  geneInformation(name); 
        }
        else if (type.equals(CytoscapeConstants.CYS_NODE_TYPE_OMIM))
        {
            Disease disease = dbq.getDiseaseByOmim(name);
            
            object_data = "fullname: '"+disease.getName()+"' "; 
            
            terms_hpo = dbq.getHPOTerms(new Integer(name), NetworkConstants.INPUT_TYPE_OMIM);   
            object_data_lists =  diseaseInformation(name); 
        }
        else if (type.intValue() >= CytoscapeConstants.CYS_NODE_TYPE_ORPHA)
        {
            RareDisease disease = dbq.getRareDiseasesByOrphanum(name);
            
            object_data = "fullname: '"+disease.getName()+"' "; 
            
            terms_hpo = dbq.getHPOTerms(new Integer(name), NetworkConstants.INPUT_TYPE_ORPHA); 
            object_data_lists =  rarediseaseInformation(name); 

        }
        
        String phenotypes = " phenotypes: [";
        int i = 0;
        while(i < terms_hpo.size() && i < 4)
        {
            Object[] row = terms_hpo.get(i);
            phenotypes = phenotypes + "{id: '"+Utils.intToHpoId((Integer)row[0])+"', name: '"+((String)row[1]).replace("'", "\\'")+"', ic:"+NumberUtils.round((Double)row[2],2)+" }, ";
            i++;
        }
        
        phenotypes.substring(0, phenotypes.length()-2);
        phenotypes = phenotypes + "]";
        
        
        String qtip = "{objname: '"+name+"', "+object_data+", "+object_data_lists+", "+phenotypes+"}";
         
        return qtip;
        
        
    }
        
    public String geneInformation(String gene) throws PhenumaException
    {
        DatabaseQueries dbq = new DatabaseQueries();
                
        List<Disease> diseases         = dbq.getDiseasesByGene(gene);
        List<RareDisease> rarediseases = dbq.getRareDiseasesByGene(gene);
        

        //Disease
        String diseases_str = "diseases: [";
        
        for (Disease d : diseases)
        {
            diseases_str = diseases_str + " {id: '"+d.getOmim().toString()+"', name:'"+d.getName().toString()+"'}, ";
        }
        
        diseases_str = diseases_str + "]";

        String orphan_diseases_str = "orphandiseases: [";
        
        for (RareDisease d : rarediseases)
        {
            orphan_diseases_str = orphan_diseases_str + " {id: '"+d.getOrphanum().toString()+"', name:'"+d.getName().toString()+"'}, ";
        }
        
        orphan_diseases_str = orphan_diseases_str + "]";
        
        return  diseases_str + ", " + orphan_diseases_str;
        
    }
    
    public String diseaseInformation(String disease) throws PhenumaException
    {
        DatabaseQueries dbq = new DatabaseQueries();
                
        List<Gene> genes     = dbq.getGenesByDisease(disease);

        //Disease
        String genes_str = "genes: [";
        
        for (Gene g : genes)
        {
            genes_str = genes_str + " {id: '"+g.getEntrezid().toString()+"', name:'"+g.getSymbol().toString()+"'}, ";
        }
        
        genes_str = genes_str + "]";

        
        return  genes_str;
        
    }
    
    public String rarediseaseInformation(String raredisease) throws PhenumaException
    {
        DatabaseQueries dbq = new DatabaseQueries();
                
        List<Gene> genes     = dbq.getGenesByRareDisease(raredisease);

        //Disease
        String genes_str = "genes: [";
        
        for (Gene g : genes)
        {
            genes_str = genes_str + " {id: '"+g.getEntrezid().toString()+"', name:'"+g.getSymbol().toString()+"'}, ";
        }
        
        genes_str = genes_str + "]";

        
        return  genes_str;
        
    }
    
    public String edgeInformationQtip() throws PhenumaException, IOException, OBOParserException
    {
        Application app = FacesContext.getCurrentInstance().getApplication();
        ExpressionFactory exprFactory = app.getExpressionFactory();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ValueExpression valExpr = exprFactory.createValueExpression(elContext, "#{networkFormBean.currentCysNetwork}", CysNetwork.class);
        
        CysNetwork cysnetwork = (CysNetwork)valExpr.getValue(elContext);
        
        DatabaseQueries q = new DatabaseQueries();
        
        //Current network
        NetworkDB n = cysnetwork.getNetworkDB();
        
        //Get row of the selected edge
        
        String edgeinformation = "{ ";
        
        if(this.selectedEdge == null)
        {
            edgeinformation = edgeinformation + "";
        }
        else if (!this.selectedEdge.startsWith("0*"))
        {

            String[] strs = this.selectedEdge.split("\\*");
            Integer rel_type = new Integer(strs[2]);

            Integer nodeX =  new Integer(strs[0]);
            Integer nodeY =  new Integer(strs[1]);


            //Semantic Similarity relationships            
            if (rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP || rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC ||
                rel_type == NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF || rel_type == NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC)
            {
                if(!NetworkUtils.isBipartite(n.getQuerytype()))
                {
                    edgeinformation = edgeinformation + semanticEdgeInfoJSON_DB(nodeX, nodeY, rel_type, n);
                }
                else
                {
                    edgeinformation = edgeinformation + "terms: [ {id: 'id', name: 'name', ic: 1}]";
                }
            }

            //Inferred relationships
            if (rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE || rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_OMIM || rel_type == NetworkConstants.NETWORK_REL_TYPE_INFERRED_ORPHA)
            {
                edgeinformation = edgeinformation + inferredObjectsJSON(nodeX, nodeY, rel_type, n);
            }

        }
        else
        {
            //Phenotypic Query
            PhenotypeNetworkDB phenotype_network = (PhenotypeNetworkDB)cysnetwork.getNetworkDB();
   
            Object[] edge = findEdge(phenotype_network.getResultRelationships());
            
            if(edge!=null)
            {
                String node = NumberUtils.object2string(edge[1]);
                if(node.isEmpty())
                {
                   node = (String)edge[1];
                }
  
                logger.info("Displaying "+this.selectedEdge);
            }
            
            
        }
        
        edgeinformation = edgeinformation + "}";
        
        return edgeinformation;
        
           
    }

   
    public String clearEnrichmentTable(){
            
        String table =  "<div><table></table></div>";         
        return table;
    }

    

}
