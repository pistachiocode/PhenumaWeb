/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.managedbeans;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.context.FacesContext;
import ontologizer.PopulationSet;
import ontologizer.StudySet;
import ontologizer.StudySetFactory;
import ontologizer.association.AssociationContainer;
import ontologizer.calculation.AbstractHypergeometricCalculation;
import ontologizer.calculation.EnrichedGOTermsResult;
import ontologizer.calculation.TermForTermCalculation;
import ontologizer.go.OBOParserException;
import ontologizer.go.Ontology;
import ontologizer.go.Term;
import ontologizer.statistics.BenjaminiHochberg;
import ontologizer.statistics.Bonferroni;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import phenomizer.hpo.HPOUtils;
import phenomizer.hpo.Phenuma;
import phenomizer.utils.FileUtils;
import phenuma.constants.Constants;
import phenuma.cysnetwork.CysNetwork;
import phenuma.cysnetwork.CysNetworkFactory;
import phenuma.cysnetwork.CysNetworkUtils;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.entities.Disease;
import phenuma.entities.Gene;
import phenuma.entities.RareDisease;
import phenuma.network.NetworkDB;
import phenuma.networkproyection.NetworkUtils;
import phenuma.objects.ConfidenceType;
import phenuma.objects.EnrichmentType;
import phenuma.objects.InputType;
import phenuma.objects.NetworkType;
import phenuma.objects.PhenumaConstants;
import phenuma.objects.QueryExecution;
import phenuma.objects.QueryType;
import phenuma.objects.SubnetworkType;
import phenuma.utils.PhenumaException;
import phenuma.utils.JSONUtils;
import phenuma.utils.Messages;
import phenuma.utils.Utils;
/**
 *
 * @author Armando
 */
public final class NetworkFormBean implements Serializable{
    
    static final Logger logger = Logger.getLogger(NetworkFormBean.class);
    
    /**
     * OptionLists
     */
    private List<QueryType> list_queryType;
    
    private List<NetworkType> list_networkType;
        
    private List<InputType> list_inputType;
    
    private List<ConfidenceType> list_confidenceTypes;

    private List<EnrichmentType> list_enrichmentType;
    
    
    private CysNetwork currentCysNetwork;
    
    private NetworkDB currentNetworkDB;
    
    private EnrichedGOTermsResult currentEnrichment;
    
    /**
     * Subnetworks
     */
    
    private List<SubnetworkType> subnetworks;
    
    /**
     * Selected 
     */    
    private Integer selectedQueryType;
    
    private Integer selectedIdentifier;

    private Integer selectedInputType;
    
    private Integer selectedConfidence;
    
    private Integer selectedOutputType;
    
    private Integer selectedEnrichmentType;
    
    
    private UploadedFile file;
            
    private StreamedContent outputFile;  
      
    
    private NetworkBean network = new NetworkBean();
    
    private String enrichment_json;
    
    
    /**
     * Progres bar
     */
    private boolean validate;
    
    
    /**
     * Auxiliar fields
     */
    private List<String> notFoundList;
    /**
     * Messages object
     */
    private Messages messages;
    /**
     * Too large network flag
     */
    private boolean isTooLarge;
    /**
     * 
     */
    private String processtext;
    /**
     * 
     */
    private Integer progress;
    
    
    
    /** Creates a new instance of NetworkFormBean */
    public NetworkFormBean() throws OBOParserException, IOException {

        

        /**
         * Create Phenuma instance
         */
        Phenuma p = Phenuma.getInstance();

        /**
         * Initialize Network bean configuration
         */
        setInitialConfiguration(p);

        network = new NetworkBean();
        file = null;
        
        
        /**
         * Default values for input fields.
         */
        if(this.selectedQueryType ==  null)
            this.selectedQueryType = PhenumaConstants.ID_NETWORK_QUERY;
        
        
        if(this.selectedInputType == null)
            this.selectedInputType = PhenumaConstants.ID_GENES_INPUT;
        
        
        /*Feeding input select list*/
        this.list_queryType = PhenumaConstants.queryTypeList();
        this.list_inputType =  PhenumaConstants.inputTypeList_Network();   
        this.list_enrichmentType = PhenumaConstants.enrichmentTypeList();
        this.list_networkType = PhenumaConstants.networkTypeList_Genes();
        this.list_confidenceTypes = PhenumaConstants.confidenceTypeList();
 
        
        /*Messages*/
        notFoundList = new ArrayList<String>();

        validate = false;

        isTooLarge = false;

        messages = Messages.getInstance();
        
        this.processtext = "";
        
        this.progress = 0;
  
    }
    
    
    /**
     * Configuring relationships types included in the resulting network.
     * 
     * @param outputType 
     */
    public void configureSubnetworks(int outputType) 
    {
        logger.info("Configuring filters");
         
        /**
         * Getting relationships types included in the resulting network.
         */
        this.subnetworks = CysNetworkUtils.getSubnetworkTypes(outputType);
        
        
        System.out.println("Subnetworks: "+this.subnetworks.size());

        for(SubnetworkType s : this.subnetworks){         
            s.setMaxValue(CysNetworkUtils.getMaximumScore(s.getIdNetwork()));
            s.setMinValue(CysNetworkUtils.getMinimumScore(s.getIdNetwork())  );
            s.setColor(CysNetworkUtils.getRelationshipColor(s.getIdNetwork()));
            s.setShowHandle(CysNetworkUtils.getRelationshipShowHandle(s.getIdNetwork()));
            s.setStep(CysNetworkUtils.getRelationshipStep(s.getIdNetwork()));
        }
        
    }
    
    
    /**
     * Set initual phenuma configurtion.
     * @param p 
     */
    public void setInitialConfiguration(Phenuma p) {
        /**
         * Set semantic similariy pvakue threshold
         */
        p.setPvalue_hpo_query_threshold(1.0);

        /*Set resources location*/
        FacesContext ctx = FacesContext.getCurrentInstance();
        String resources_location = ctx.getExternalContext().getInitParameter("RESOURCES_LOCATION");
        p.setResources_location(resources_location);

        /*Set database*/
        p.setDatabase(phenuma.constants.Constants.PERSISTENCE_UNIT_PHENUMA_DB);
    }
    
    
    /**
     * Set default configuration to NetworkForm
     */
    public void initForm()
    {
        isTooLarge = false;
        
        this.network = new NetworkBean();
           
        this.selectedQueryType = PhenumaConstants.ID_NETWORK_QUERY;
        this.selectedInputType = PhenumaConstants.ID_GENES_INPUT;
        
        this.list_networkType = PhenumaConstants.networkTypeList_Genes();
        
        messages = Messages.getInstance();
    }
    

    // Getters and Setters
    
    public NetworkBean getNetwork() {
        return network;
    }

    public void setNetwork(NetworkBean network) {
        this.network = network;
    }

    public List<QueryType> getList_queryType() {
        return list_queryType;
    }

    public void setList_queryType(List<QueryType> list_queryType) {
        this.list_queryType = list_queryType;
    }
    
    public List<ConfidenceType> getList_confidenceTypes() {
        return list_confidenceTypes;
    }

    public void setList_confidenceTypes(List<ConfidenceType> list_confidenceTypes) {
        this.list_confidenceTypes = list_confidenceTypes;
    }

    public List<InputType> getList_inputType() {
        return list_inputType;
    }

    public void setList_inputType(List<InputType> list_inputType) {
        this.list_inputType = list_inputType;
    }

    public List<NetworkType> getList_networkType() {
        return list_networkType;
    }

    public void setList_networkType(List<NetworkType> list_networkType) {
        this.list_networkType = list_networkType;
    }

    public List<EnrichmentType> getList_enrichmentType() {
        return list_enrichmentType;
    }

    public void setList_enrichmentType(List<EnrichmentType> list_enrichmentType) {
        this.list_enrichmentType = list_enrichmentType;
    }
    
    public Integer getSelectedQueryType() {
        return selectedQueryType;
    }

    public void setSelectedQueryType(Integer selectedQueryType) {
        this.selectedQueryType = selectedQueryType;
    }   
    
    
    public Integer getSelectedInputType() {
        return selectedInputType;
    }

    public void setSelectedInputType(Integer selectedInputType) {
        this.selectedInputType = selectedInputType;
        this.network.setInputType(this.selectedInputType);
    }

    
    public Integer getSelectedIdentifier() {
        return selectedIdentifier;
    }
    
    public void setSelectedIdentifier(Integer selectedIdentifier) {
        this.selectedIdentifier = selectedIdentifier;
         this.network.setInputType(this.selectedInputType);
    }

    
    public Integer getSelectedConfidence() {
        return selectedConfidence;
    }

    public void setSelectedConfidence(Integer selectedConfidence) {
        this.selectedConfidence = selectedConfidence;
         this.network.setConfidence(this.selectedConfidence);
    }

    public Integer getSelectedOutputType() {
        return selectedOutputType;
    }

    public void setSelectedOutputType(Integer selectedOutputType) {
        this.selectedOutputType = selectedOutputType;
         this.network.setOutputType(this.selectedOutputType);
    }

    public Integer getSelectedEnrichmentType() {
        return selectedEnrichmentType;
    }

    public void setSelectedEnrichmentType(Integer selectedEnrichmentType) {
        this.selectedEnrichmentType = selectedEnrichmentType;
    }
    
    
    public List<SubnetworkType> getSubnetworks() {
        return subnetworks;
    }

    public void setSubnetworks(List<SubnetworkType> subnetworks) {
        this.subnetworks = subnetworks;
    }

    public CysNetwork getCurrentCysNetwork() {
        return currentCysNetwork;
    }

    public void setCurrentCysNetwork(CysNetwork currentCysNetwork) {
        this.currentCysNetwork = currentCysNetwork;
    }

    public String getEnrichment_json() {
        return enrichment_json;
    }

    public void setEnrichment_json(String enrichment_json) {
        this.enrichment_json = enrichment_json;
    }
    
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getFileName() {
        if(file!=null) {
            return file.getFileName();
        }
        
        return "";
    }
 
    public void setOutputFile(StreamedContent file)
    {
        this.outputFile = file; 
    }
    
    public StreamedContent getOutputFile() {  
        return outputFile;
    }    
 
    
    public List<InputType> getInputType() {
        return list_inputType;
    }

    public void setInputType(List<InputType> inputType) {
        this.list_inputType = inputType;
    }

    
    public Integer getProgress() {  
        return progress;
    }  
  
    public void setProgress(Integer progress) {  

        this.progress = progress;
    }


    public boolean isValidate() {
        
        if(this.network.getItems()!=null && !this.network.getItems().isEmpty()) {
            validate = true;
        }
       
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getProcesstext() {
        return processtext;
    }

    public void setProcesstext(String processtext) {
        this.processtext = processtext;
    }
    

    public boolean isIsTooLarge() {
        System.out.println("Is too big "+this.isTooLarge);
        return isTooLarge;
    }

    public void setIsTooLarge(boolean isTooBig) {
        this.isTooLarge = isTooBig;
    }

    
    /**
     * Events
     */
    
    
     /**
     * Create a network from database.
     * 
     * @param inputItems
     *              List of String values containing input elements.
     * @return NetworkDB
     *              NetworkDB object containing resulting network.
     * 
     * @throws IOException
     * @throws OBOParserException 
     */ 
    public NetworkDB createNetworkDB(List<String> inputItems) throws IOException, OBOParserException {
        
        logger.info("Creating network DB");
        
        NetworkDB newnetwork = null;
        
        
        /**
         * Numeric value of threshold applied based on selected confidence level.
         */
        double threshold = PhenumaConstants.getNumericConfidence(this.network.getOutputType(), 
                                                                 this.network.getConfidence());
        
        /**
         * Output network type.
         */
        int outputType = this.network.getOutputType();
        
        
        /**
         * Executing database query to get the resulting network.
         */
        switch (this.network.getInputType()) {
            case PhenumaConstants.ID_GENES_INPUT:
                logger.info("Executing Gene query");
                newnetwork = QueryExecution.executeGeneQueryDB(inputItems, outputType, threshold);
                break;
            case PhenumaConstants.ID_OMIM_INPUT:
                logger.info("Executing OMIM query");
                newnetwork = QueryExecution.executeDiseaseQuery(inputItems, outputType, threshold);
                break;
            case PhenumaConstants.ID_ORPHANUM_INPUT:
                logger.info("Executing Orphanum query");
                newnetwork = QueryExecution.executeRareDiseaseQuery(inputItems, outputType, threshold);
                break;
            case PhenumaConstants.ID_PHENOTYPE_INPUT:
                newnetwork = QueryExecution.executePhenotypeQuery(inputItems, outputType, threshold);
                break;
            default:
                break;
        }
          
        return newnetwork;
    }
    
    
    
    /**
     * Create output file containing resulting network information.
     */
    public void createOutputFile()
    {
        try {
            if (this.selectedQueryType == PhenumaConstants.ID_NETWORK_QUERY) {
                NetworkUtils.createNetworkZip(currentNetworkDB);
                outputFile = new DefaultStreamedContent(new FileInputStream(Constants.TEMP+this.currentNetworkDB.getName()+".zip"), "application/zip", "network.zip");
                FileUtils.deleteDirectory(new File(Constants.TEMP+this.currentNetworkDB.getName()+".zip"));   
            }   
            else if (this.selectedQueryType == PhenumaConstants.ID_ENRICHMENT_QUERY) {
                
                InputStream is = new ByteArrayInputStream(Utils.enrichment2string(currentEnrichment).getBytes("UTF-8"));
                outputFile = new DefaultStreamedContent(is,"text","enrichment.txt");
            }
           
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage());
        } 
        
    }
    
      

    public void addNetworkDB()    
    {
         long time = System.currentTimeMillis();
        
         messages.reset();
         
         /**
          * Init progress bar
          */
         this.progress = 0;

         try 
         {

             this.progress = 5;
             //Read query input
             List<String> inputItems = readItems();

             this.progress = 10;
             
             /**
              * If input Items is empty the objects have not been found in the database.
              */
             if(inputItems!=null && !inputItems.isEmpty()){

                 //Execute Query
                 logger.info("Type of Input:"+this.network.getInputType());  
                 logger.info("Input "+this.network.getItems());
                 logger.info("Output "+this.network.getOutputType());
                 logger.info("Confidence "+this.network.getConfidence());
                 logger.info("QueryType "+this.selectedQueryType);
                 
                 if (this.selectedQueryType == PhenumaConstants.ID_NETWORK_QUERY) {
                     networkQuery(inputItems);   
                 }   
                 else if (this.selectedQueryType == PhenumaConstants.ID_ENRICHMENT_QUERY) {
                     enrichmentQuery(inputItems);
                 }
             }

        }
        catch (IOException ex) 
        {
            logger.error(ex.getMessage());
        }
        catch (NullPointerException ex) 
        {
            logger.error(ex.getMessage());
        }
        catch (Exception ex) 
        {
            logger.error(ex.getMessage());
            
        }
        finally
        {           
             this.progress = 100;
             this.getShowMessages();

        }
    }

    
    
    /**
     * Retrieve a precomputed cytoscape network form the database.
     * 
     * @param inputItems
     * @return
     * @throws IOException
     * @throws OBOParserException
     * @throws PhenumaException 
     */
     private CysNetwork findNetworkDB(List<String> inputItems) throws IOException, OBOParserException, PhenumaException {
         
         //Find network stored in the database
         NetworkDB networkDB = new NetworkDB();
         networkDB.setConfidence(this.network.getConfidence());
         networkDB.setQuerytype(this.network.getOutputType());
         networkDB.setInputtype(this.network.getInputType());


         CysNetwork newcysnetwork = CysNetworkFactory.createCysNetworkFromDatabase(networkDB, inputItems);
         
         return  newcysnetwork;
     }
    
     
     
    /**
     * Execute an enrichment calculation query.
     * TODO: This method is too long.
     * @param inputitems
     *          String containing a set of input elements.
     * @throws IOException
     * @throws OBOParserException
     * @throws PhenumaException 
     */ 
    private void enrichmentQuery(List<String> inputitems) throws IOException, OBOParserException, PhenumaException
    {
        /**
         * Ontology used to calculate the enrichment
         */
        Ontology ontology = null;
        
        /**
         * Annotation set 
         */
        AssociationContainer assocs = null;
        
        /**
         * Input type
         */
        Integer studySetType = 0;
        
        /**
         * Input studySet
         */
        StudySet studySet = StudySetFactory.createFromList(inputitems, true);
        
        if(this.selectedEnrichmentType != PhenumaConstants.ID_PHENOTYPIC_ENRICHMENT)
        {
           
            if (null != this.selectedEnrichmentType)
            
            /**
             * Setting Gene Ontology branch and annotations for functional 
             * enrichment. 
             */
            switch (this.selectedEnrichmentType) {
                case PhenumaConstants.ID_FUNCTIONAL_ENRICHMENT_BP:
                    ontology = Phenuma.getInstance().getGene_ontology_bp();
                    assocs   = Phenuma.getInstance().getGenes_go_associations_bp();
                    studySetType = PhenumaConstants.ID_GENES_INPUT;
                    break;
                case PhenumaConstants.ID_FUNCTIONAL_ENRICHMENT_CC:
                    ontology = Phenuma.getInstance().getGene_ontology_cc();
                    assocs   = Phenuma.getInstance().getGenes_go_associations_cc();
                    studySetType = PhenumaConstants.ID_GENES_INPUT;
                    break;
                case PhenumaConstants.ID_FUNCTIONAL_ENRICHMENT_MF:
                    ontology = Phenuma.getInstance().getGene_ontology_mf();
                    assocs   = Phenuma.getInstance().getGenes_go_associations_mf();
                    studySetType = PhenumaConstants.ID_GENES_INPUT;
                    break;
                default:
                    break;
            }

            
            /**
             * Getting diseases-causing genes for OMIM or ORPHANUM queries.
             * Functional enrichment is applied only to genes.
             */
            if(PhenumaConstants.ID_OMIM_INPUT == this.network.getInputType())
            {
                Map<Integer, List<Integer>> disease2genes = NetworkUtils.geneStudyFromOmim(studySet);
                studySet = StudySetFactory.createFromIntegerSet(disease2genes.keySet(), true);   
            }

            if(PhenumaConstants.ID_ORPHANUM_INPUT == this.network.getInputType())
            {
                Map<Integer, List<Integer>> disease2genes = NetworkUtils.geneStudyFromOrphanum(studySet);
                studySet = StudySetFactory.createFromIntegerSet(disease2genes.keySet(), true);     
            }
        
            
        } else {
            
            /**
             * Phenotypic Enrichment.
             */
            
            studySetType =  this.network.getInputType();
            
            ontology = Phenuma.getInstance().getHuman_phenotype_ontology();
            
            /**
             * Select the annotations set. 
             * The annotations should be of the same type that
             * the input type query.
             */
            switch (this.network.getInputType()) {
                case PhenumaConstants.ID_OMIM_INPUT:
                    assocs = Phenuma.getInstance().getDiseases_hpo_associations();
                    break;
                case PhenumaConstants.ID_ORPHANUM_INPUT:
                    assocs = Phenuma.getInstance().getEr_hpo_associations();
                    break;
                case PhenumaConstants.ID_GENES_INPUT:
                    assocs = Phenuma.getInstance().getGenes_hpo_associations();
                    break;
                default:
                    break;
            }
           
        }

        this.progress = 60;
        
        /**
         * Execute enrichment based on the input data.
         */
        
        PopulationSet populationSet = (PopulationSet) StudySetFactory.createFromByteStringSet(assocs.getAllAnnotatedPP(), true);

        AbstractHypergeometricCalculation t4t_cal = new TermForTermCalculation();

        currentEnrichment = t4t_cal.calculateStudySet(ontology, assocs, populationSet, studySet, new Bonferroni());

        this.progress = 80;
        
        /**
         * Saving result in a JSON structure and sending output to the view.
         */
        if(currentEnrichment.getSize() > 0)
        {
            this.currentCysNetwork = new CysNetwork();
            
            enrichment_json = JSONUtils.createEnrichmentJSON(currentEnrichment, studySetType);
            redirectVisualizer();
            
            messages.doneMessage("Done!", "The query has been executed succesfully.");
        }
        else {
            messages.warningMessage("Enrichment analysis", "No results. Not found any enriched terms for the query: "+this.network.getItems());
        }
        
        
        this.progress = 100;
        
    }
    
    
    /**
     * This methods create a CysNetwork object from a query.
     * A CysNetwork object is used by the view to display a network in the 
     * CytoscapeWeb visualizer. This methods gets the content of the 
     * network from the database and send the resulting network to 
     * the visualizer.
     * @param inputitems
     *          String containing a set of input elements.
     */
    private void networkQuery(List<String> inputItems)
    {
        
        boolean networkbuilded = false;
        
        try {

            CysNetwork newcysnetwork = null;
            
            this.currentNetworkDB = null; 
            
            if(newcysnetwork == null)
            {
                /**
                 * Create NetworkDB from database.
                 */
                NetworkDB newnetwork = createNetworkDB(inputItems);
                          
                this.currentNetworkDB = newnetwork; 
                
                if(newnetwork == null || newnetwork.getRows()==null || newnetwork.getRows().isEmpty())
                {   
                    messages.warningMessage("Network Builder", "The network is empty");
                    
                    newcysnetwork = new CysNetwork(new NetworkDB(), this.network.getOutputType());
  
                }
                else if (!NetworkUtils.isBipartite(this.network.getOutputType()) && (newnetwork.getRows().size() > 7000 || newnetwork.getNodes().getSize() > 500))
                {    
                   
                    this.setIsTooLarge(true); 
                    
                    newcysnetwork = new CysNetwork();

                    this.messages.tooLargeMessage(this.network.getConfidence());
                }
                else
                {
                    newcysnetwork = new CysNetwork(newnetwork, this.network.getOutputType());  
                    
                    messages.doneMessage("Done!", "The query has been executed succesfully. Please wait while the network is loaded into the display.");
                    
                    networkbuilded = true;
                   
                }
                
            }else if (newcysnetwork.getToolarge().equals(1)) 
            {
                    this.setIsTooLarge(true); 
                    
                    newcysnetwork = new CysNetwork(new NetworkDB(), this.network.getOutputType());

                    this.messages.tooLargeMessage(this.network.getConfidence());
            }else{
                
                    messages.doneMessage("Done!", "The query has been executed succesfully. Please wait while the network is loaded into the display.");
                    
                    networkbuilded = true;
            }
            
            logger.info("Configuring subnetworks..");
            configureSubnetworks(this.network.getOutputType());
            
            this.currentCysNetwork = newcysnetwork; 
            
            if(networkbuilded) {
                redirectVisualizer();
                QueryExecution.saveQuery(this.network.getItems(), this.getSelectedInputType(), this.getSelectedOutputType(), this.getSelectedConfidence());
            }

            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(NetworkFormBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OBOParserException ex) {
            java.util.logging.Logger.getLogger(NetworkFormBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PhenumaException ex) {
            java.util.logging.Logger.getLogger(NetworkFormBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    /**
     * Refreshing the view.
     * 
     * @throws IOException 
     */
    private void redirectVisualizer() throws IOException 
    {
        logger.info("Redirecting to index.jsf");

        FacesContext contex = FacesContext.getCurrentInstance();
        if(contex.getViewRoot().getViewId().equals("/main.xhtml")){
           contex.getExternalContext().redirect( contex.getExternalContext().getInitParameter("VIEW_URL") );
        }
    }
    
   
    public String getShowMessages(){
        messages.show();
        messages.reset();
        return "";
    }

    
 
    public void listener() {  
        this.processtext = this.processtext + ".";
    }  
    
    /**
     * Upload combo boxes
     */
    public void updateComboBoxes(){
        updateInputType();
        updateNetworkType();
    }
    
    /**
     * Update network type list
     */
    public void updateInputType()
    {
       
        System.out.println("Updating input type "+this.selectedQueryType);
        
        if(this.selectedQueryType !=null)
        {
            if(PhenumaConstants.ID_ENRICHMENT_QUERY == this.selectedQueryType)
            {
                list_inputType = PhenumaConstants.inputTypeList_Enrichment();
            }
            else if(PhenumaConstants.ID_NETWORK_QUERY == this.selectedQueryType)
            {
                list_inputType = PhenumaConstants.inputTypeList_Network();
            }
        }
    }
    
    /**
     * Update network type list
     */
    public void updateNetworkType()
    {
        System.out.println("Updating network type "+this.selectedInputType);
       
        if(this.selectedInputType !=null)
        {
            if(PhenumaConstants.ID_GENES_INPUT == this.selectedInputType)
            {
                list_networkType = PhenumaConstants.networkTypeList_Genes();
            }
            else if(PhenumaConstants.ID_OMIM_INPUT == this.selectedInputType)
            {
                list_networkType = PhenumaConstants.networkTypeList_Diseases();
            }
            else if(PhenumaConstants.ID_ORPHANUM_INPUT == this.selectedInputType)
            {
                list_networkType = PhenumaConstants.networkTypeList_RareDiseases();
            }
            else if(PhenumaConstants.ID_PHENOTYPE_INPUT == this.selectedInputType)
            {
                list_networkType = PhenumaConstants.networkTypeList_Phenotypes();
            }
        }
    }
    
    
    /**
     * Update text area content. 
     */
    public void updateItemTextArea()
    {
        Messages current_messages = Messages.getInstance();
       
        if(this.file !=null)
        {
            try {
                this.readItemsFormFile();
            } catch (UnsupportedEncodingException ex) {
                 current_messages.warningMessage("Unsoported Encodign Exception" ,"ERROR: "+ex.getMessage()); 
            } catch (IOException ex) {
                 current_messages.warningMessage("I/O Exception" ,"ERROR: "+ex.getMessage()); 
            }
        }
    }
    
    
    
    /**
     * HandleFileUpload. Read the input file and store the information to the text area.
     * @param event 
     */
    
    public void handleFileUpload(FileUploadEvent event) {
        
        Messages current_messages = Messages.getInstance();
        
        try{
            //Hay que hacer esta asignacion, no se realiza automaticamente al hacer submit en el formulario :s
            
            this.file = event.getFile();
            this.readItemsFormFile();
                        
        }catch(Exception ex){
             current_messages.warningMessage("File Upload" ,"ERROR: "+ex.getMessage()); 
        }
    }
    
    
    /**
     * Remove the file loaded
     * @param event 
     */
    public void removeFile() {
        this.file = null;
        this.network.setItems("");
    }
    
    
    //Enable/disable
    
    
    public boolean getVisibleClear(){
        return this.file != null;
    }
    
    
    //Auxiliar methods
    
    
    /**
     * Read the content of the uploaded file and is loaded in the
     * 'items' field.
     * 
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public List<String> readItems() throws UnsupportedEncodingException, IOException 
    {
        
        List<String> inputItems = new ArrayList<String>();

        /**
         * Read items string.
         */
        if(this.network.getItems()!=null && !this.network.getItems().isEmpty())
        {
           // System.out.println("Processing input..."+this.network.getItems());
            String[] str = this.network.getItems().split("[\t\n;,]+");
            
            for(String s : str)
            {
                //Process input checks if the item is stored in the database
                List<String> itemID = this.processInput(s.replace("\"", "").trim());
               
                for(String id : itemID){
                    inputItems.add(id);
                }
            }
            
            this.network.setItems(this.network.getItems().replace("\n", " "));
            
        }else{
            messages.warningMessage("Missing input", "You must write a list of genes, disease or phenotypes accoding to your selection.");
            return null;
        }
        
        /**
         * Show warning. Not found items.
         */
        if(!this.notFoundList.isEmpty()){
            messages.warningMessage(Messages.NOT_FOUND, Messages.itemNotFound(this.selectedInputType, notFoundList));
            this.notFoundList = new ArrayList<String>(); //reset list
        }
        
        
        return inputItems;

    }

    /**
     * Read items form file.
     * @throws IOException 
     */
    public void readItemsFormFile() throws IOException {
        
        if(file != null)
        {
            InputStream is = file.getInputstream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String line = br.readLine();
            
            String items = "";
            while(line!=null)
            {
                String[] str = line.split("[\t\n;,]+");
            
                for(String s : str){
                    items=items+s+", ";
                }
                
                line = br.readLine();
            }

            this.network.setItems(items);
        }
    }
    
    /**
     * Process input: this method check if the input item is stored in the database
     * @param input 
     */
    
    public List<String> processInput(String input)
    {
        Messages current_messages = Messages.getInstance();
                
        List<String> res = new ArrayList<String>();
        try{

            if (PhenumaConstants.ID_GENES_INPUT == this.selectedInputType)
            {
                List<Gene> geneslist = this.getGeneByInput(input);
                
                if(geneslist!=null && !geneslist.isEmpty()){
                    for(Gene g : geneslist) {
                        res.add(g.getEntrezid().toString());
                    }
                    
                }else{
                    notFoundList.add(input);
                }
            
            }
            else if (PhenumaConstants.ID_OMIM_INPUT == this.selectedInputType)
            {
                DatabaseQueries q = new DatabaseQueries();
                Disease d = q.getDiseaseByOmim(input);

                if(d!=null) {
                    res.add(d.getOmim().toString());
                }
                else {
                    notFoundList.add(input);
                }
            }
            else if (PhenumaConstants.ID_ORPHANUM_INPUT == this.selectedInputType)
            {
                //Remove prefix "OPRHA" in Orphanet diseases.
                if(input.toUpperCase().contains("ORPHA")) {
                    input = input.toUpperCase().replace("ORPHA", "");
                }
                        
                DatabaseQueries q = new DatabaseQueries(){};
                RareDisease rd = q.getRareDiseasesByOrphanum(input);

                if(rd!=null) {
                    res.add(rd.getOrphanum().toString());
                }
                else {
                    notFoundList.add(input);
                }  
            }
            else if (PhenumaConstants.ID_PHENOTYPE_INPUT == this.selectedInputType)
            {

               if(HPOUtils.isHPOterm(input)) 
               { 
                   //Extraer hpo id y eliminar paréntesis y texto.
                   input = HPOUtils.getHPOterm(input);
                   Phenuma p = Phenuma.getInstance();

                   /*Getting allTerms and term object of the each input in order to check if the input is correct.*/
                   ArrayList<Term> allterms = p.getHuman_phenotype_ontology().getTermsInTopologicalOrder();
                   Term term = p.getHuman_phenotype_ontology().getTerm(input);

                   if(allterms.contains(term)){
                       res.add(term.getID().toString());
                   }
                   else
                   {
                       notFoundList.add(input);
                   }  
               }
               else
               {
                   notFoundList.add(input);
               }  
            }
            
            
        }catch(Exception ex){
            current_messages.warningMessage("Input Process Exception. ", ex.getMessage());
        }
        
        return res;
        
    }
    
    
    
    public List<Gene> getGeneByInput(String input) throws PhenumaException{

        List<Gene> genes_list = new ArrayList<Gene>();
                
        DatabaseQueries q = new DatabaseQueries();
        
        input = input.toUpperCase();
        
        if(input.startsWith("MIM"))
        {
            input = input.replace(":", "").replace("MIM", "");
            genes_list = q.getGeneBySynonym(input, "Mim");
        }
        else if(input.startsWith("ENSG"))
        {
            genes_list = q.getGeneBySynonym(input, "Ensembl");
            
        }
        else if(input.startsWith("ORPHAN"))
        {  
            input = input.replace(":", "").replace("ORPHAN", "");
            genes_list = q.getGeneBySynonym(input, "Orphanum");  
        }
        else if(input.startsWith("ORPHA"))
        {  
            input = input.replace(":", "").replace("ORPHA", "");
            genes_list = q.getGeneBySynonym(input, "Orphanum");
        }
        else if(input.startsWith("HGNC"))
        {            
            input = input.replace(":", "").replace("HGNC", "");
            genes_list = q.getGeneBySynonym(input, "Hgnc");
        }
        else
        {
            Gene g = null; 
        
            if(Utils.isSymbol(input)) {
                g = q.getGeneBySymbol(input);
            }
            else{                        
             
                input = input.replace(":", "").replace("ENTREZ", "").replace("GENEID", "");
                g = q.getGeneByEntrezId(input);
            }
            
            if(g!=null) {
                genes_list.add(g);
            }
            
        }
        System.out.println(genes_list);
        return genes_list;

    }

}
