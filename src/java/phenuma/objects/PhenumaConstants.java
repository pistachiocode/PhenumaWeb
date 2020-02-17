/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.objects;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import phenuma.constants.Constants;
import phenuma.networkproyection.NetworkConstants;


/**
 *
 * @author Armando
 */
public class PhenumaConstants {
    
    static final Logger logger = Logger.getLogger(PhenumaConstants.class);
    
    /*Persistence Unit*/
    public static final String PERSISTENCE_UNIT_PHENUMA_HISTORY_DB = "PhenumaHistoryPU";
    
    
    /*Query Type*/
    public static final String NETWORK_QUERY = "Network";
    public static final String ENRICHMENT_QUERY = "Enrichment Analysis";
    
    public static final int ID_NETWORK_QUERY = 0;
    public static final int ID_ENRICHMENT_QUERY = 1;    
    
    
    /*Input Type*/
    public static final String GENES_INPUT = "Genes";
    public static final String OMIM_INPUT = "OMIM (Genes/Diseases)";
    public static final String ORPHANUM_INPUT = "Orphan Diseases";
    public static final String PHENOTYPE_INPUT = "Phenotype";
    
    public static final int ID_GENES_INPUT = 0;
    public static final int ID_OMIM_INPUT = 1;
    public static final int ID_ORPHANUM_INPUT = 2;
    public static final int ID_PHENOTYPE_INPUT = 3;
    
    
    /*Identifier Type*/
    public static final int ENTREZ_ID = 1;
    public static final int ENSEMBL_ID = 2;
    public static final int SYMBOL_ID = 3;
    public static final int MIM_ID = 4;
    public static final int HGNC_ID = 5;
    public static final int HPRD_ID = 6;
    public static final int ORPHANUM_ID = 7;
    public static final int OMIM_ID = 8;
    public static final int NAME_ID = 9;
    public static final int HPO_ID = 10;
    
    
    /*Output Network Type --> NetworkConstants (Phenunma.jar)*/
   
    
    /* Enrichment Type */
    
    public static final String PHENOTYPIC_ENRICHMENT = "Phenotypic (HPO)";
    public static final String FUNCTIONAL_ENRICHMENT_BP = "Functional (GO Biological Process)";
    public static final String FUNCTIONAL_ENRICHMENT_CC = "Functional (GO Cellular Component)";
    public static final String FUNCTIONAL_ENRICHMENT_MF = "Functional (GO Molecular Function)";
    
    
    public static final int ID_PHENOTYPIC_ENRICHMENT = 0;
    public static final int ID_FUNCTIONAL_ENRICHMENT_BP = 1;
    public static final int ID_FUNCTIONAL_ENRICHMENT_CC = 2;
    public static final int ID_FUNCTIONAL_ENRICHMENT_MF = 3;    
    
    /**
     * Confidence type
     */
    public static final int CONFIDENCE_HIGH_ID = 1;
    public static final int CONFIDENCE_MEDIUM_ID = 2;
    public static final int CONFIDENCE_LOW_ID = 3;
    
    
    
    /*Network Item Type*/
    public static final int NET_ITEM_TYPE_GENES = 0;
    public static final int NET_ITEM_TYPE_OMIM = 1;
    public static final int NET_ITEM_TYPE_ORPHA = 2;
    public static final int NET_ITEM_TYPE_PHENOTYPES = 0;
    
    
    /*Relationships label*/
    public static final String NET_REL_LABEL_SEMSIMGOBP = "simsemgobp";
    public static final String NET_REL_LABEL_SEMSIMGOCC = "simsemgocc";
    public static final String NET_REL_LABEL_SEMSIMGOMF = "simsemgomf";
    public static final String NET_REL_LABEL_SEMSIMHPO  = "simsemhpo";
    public static final String NET_REL_LABEL_PPI = "ppi";
    public static final String NET_REL_LABEL_METABOLIC = "metabolic";
    public static final String NET_REL_LABEL_INFERRED_GENE = "infgene";
    public static final String NET_REL_LABEL_INFERRED_OMIM = "infomim";
    public static final String NET_REL_LABEL_INFERRED_ORPHA = "inforpha";
    public static final String NET_REL_LABEL_OMIM = "omim";
    public static final String NET_REL_LABEL_ORPHADATA = "orphadata";
    
    public static final String NET_REL_LABEL_PHENO_GENE = "phenogene";
    public static final String NET_REL_LABEL_PHENO_OMIM = "phenoomim";
    public static final String NET_REL_LABEL_PHENO_ORPHA = "phenoorpha";
    
    public static final String NET_REL_LABEL_PVALUE = "pvalue";
    
    
    /*Relationship image*/
    public static final String NET_REL_IMAGE_SEMSIMGOBP = "./resources/img/legend/gobp_rel.png";
    public static final String NET_REL_IMAGE_SEMSIMGOCC = "./resources/img/legend/gocc_rel.png";
    public static final String NET_REL_IMAGE_SEMSIMGOMF = "./resources/img/legend/gomf_rel.png";
    public static final String NET_REL_IMAGE_SEMSIMHPO  = "./resources/img/legend/phenotypic_rel.png";
    public static final String NET_REL_IMAGE_PPI = "./resources/img/legend/ppi_rel.png";
    public static final String NET_REL_IMAGE_METABOLIC = "./resources/img/legend/metabolic_rel.png";
    public static final String NET_REL_IMAGE_INFERRED_GENE = "./resources/img/legend/infergene_rel.png";
    public static final String NET_REL_IMAGE_INFERRED_OMIM = "./resources/img/legend/inferomim_rel.png";
    public static final String NET_REL_IMAGE_INFERRED_ORPHA = "./resources/img/legend/inferorpha_rel.png";
    public static final String NET_REL_IMAGE_OMIM = "./resources/img/legend/omim_rel.png";
    public static final String NET_REL_IMAGE_ORPHADATA = "./resources/img/legend/orphadata_rel.png";;
    
    /*Row Colors*/
    public static final String COLOR_1 = "#F5F5F5"; //WhiteSmoke
    public static final String COLOR_2 = "#F7F2E0"; 
    public static final String COLOR_3 = "#F7F2E0"; 
    public static final String COLOR_4 = "#F5ECCE"; 
    public static final String COLOR_5 = "#F5ECCE"; 
    public static final String COLOR_6 = "#F3E2A9"; 
    public static final String COLOR_7 = "#F5DA81"; 
    public static final String COLOR_8 = "#F7D358"; 
    
    
    /*Row Colors Gray*/
    public static final String COLOR_1_GRAY = "#FFFFFF"; //WhiteSmoke
    public static final String COLOR_2_GRAY = "#F0F0F0"; 
    public static final String COLOR_3_GRAY = "#F0F0F0"; 
    public static final String COLOR_4_GRAY = "#E0E0E0"; 
    public static final String COLOR_5_GRAY = "#E0E0E0"; 
    public static final String COLOR_6_GRAY = "#D0D0D0"; 
    public static final String COLOR_7_GRAY = "#C0C0C0 "; 
    public static final String COLOR_8_GRAY = "#B8B8B8"; 
    
    /*Relationships title*/
    public static final String NET_REL_TITLE_SEMSIMGOBP = "Biological Process";
    public static final String NET_REL_TITLE_SEMSIMGOCC = "Cellular Component";
    public static final String NET_REL_TITLE_SEMSIMGOMF = "Molecular Function";
    public static final String NET_REL_TITLE_SEMSIMHPO  = "Phenotypic";
    public static final String NET_REL_TITLE_PPI = "Protein-Protein Interaction";
    public static final String NET_REL_TITLE_METABOLIC = "Metabolic";
    public static final String NET_REL_TITLE_INFERRED_GENE = "Inferred from Genes";
    public static final String NET_REL_TITLE_INFERRED_OMIM = "Inferred from OMIM";
    public static final String NET_REL_TITLE_INFERRED_ORPHA = "Inferred from Orphanet";
    public static final String NET_REL_TITLE_OMIM = "OMIM";
    public static final String NET_REL_TITLE_ORPHADATA = "Orphadata";
    
    public static final String NET_REL_TITLE_PHENO_GENE = "Genes";
    public static final String NET_REL_TITLE_PHENO_OMIM = "OMIM (Genes/Diseases)";
    public static final String NET_REL_TITLE_PHENO_ORPHA = "Orphan Diseases";

    
    /*Semantic Similarity Measure*/
    public static final int SS_MEASURE_RESNIK = 1;
    public static final int SS_MEASURE_ROBINSON = 2; 
    
    /* Confidence values (Provisionales)*/
    public static final double CONFIDENCE_HPO_GENES_HIGH = 0.08897;
    public static final double CONFIDENCE_HPO_GENES_MEDIUM = 0.03768;
    public static final double CONFIDENCE_HPO_GENES_LOW = 0;
    
    public static final double CONFIDENCE_HPO_OMIM_HIGH = 0.09821;
    public static final double CONFIDENCE_HPO_OMIM_MEDIUM = 0.046;//2.406;
    public static final double CONFIDENCE_HPO_OMIM_LOW = 0;//2.145;
    
    public static final double CONFIDENCE_HPO_ORPHAN_HIGH = 0.081248858864341758; //2.328
    public static final double CONFIDENCE_HPO_ORPHAN_MEDIUM = 0.038159576410443687; //2.092
    public static final double CONFIDENCE_HPO_ORPHAN_LOW = 0; //1.883
    
    
    public static final double CONFIDENCE_GO_BP_HIGH = 0.3197908048694007;//6.616;
    public static final double CONFIDENCE_GO_BP_MEDIUM = 0.19016664696844343;//6.177;
    public static final double CONFIDENCE_GO_BP_LOW = 0;//5.517;
    
    public static final double CONFIDENCE_GO_CC_HIGH = 0.23621811333675755;//5.426;
    public static final double CONFIDENCE_GO_CC_MEDIUM = 0.14248416367060454;//4.988;
    public static final double CONFIDENCE_GO_CC_LOW = 0;//4.318;
    
    public static final double CONFIDENCE_GO_MF_HIGH = 0.27213406495625092;//5.97;
    public static final double CONFIDENCE_GO_MF_MEDIUM = 0.15102081170596685;//5.514;
    public static final double CONFIDENCE_GO_MF_LOW = 0;//4.863;
    
    
    /*Maximos*/
    public static final double MAX_GENE_GENE_HPO = 6.8057;
    public static final double MAX_OMIM_OMIM_HPO = 7.934;//7.817;
    public static final double MAX_ORPHA_ORPHA_HPO = 7.36;
    
    public static final double MAX_OMIM_GENE_HPO = 7.4989;
    public static final double MAX_GENE_OMIM_HPO = 8.5102;
    
    public static final double MAX_ORPHA_GENE_HPO = 7.7575;
    public static final double MAX_GENE_ORPHA_HPO = 8.0529;
    
    public static final double MAX_GENE_GENE_GOBP = 8.9181;
    public static final double MAX_GENE_GENE_GOCC = 8.995;
    public static final double MAX_GENE_GENE_GOMF = 8.9148;
    
    
    public static final double MIN_GENE_GENE_HPO = 1.8795;
    public static final double MIN_OMIM_OMIM_HPO = 2.146;
    public static final double MIN_ORPHA_ORPHA_HPO = 1.882;
    
    public static final double MIN_OMIM_GENE_HPO = 1.8;
    public static final double MIN_GENE_OMIM_HPO = 1.8;
    
    public static final double MIN_ORPHA_GENE_HPO = 1.8;
    public static final double MIN_GENE_ORPHA_HPO = 1.8;
    
    public static final double MIN_GENE_GENE_GOBP = 5.5337;
    public static final double MIN_GENE_GENE_GOCC = 4.3222;
    public static final double MIN_GENE_GENE_GOMF = 4.869;
    
            
    /**
     * Orphan disease prevalence
     */
    public static int OD_PREV_MORE_THAN_1_1000_ID    = 0;
    public static int OD_PREV_BETWEEN_1_5_10000_ID   = 1;
    public static int OD_PREV_BETWEEN_1_9_100000_ID  = 2;
    public static int OD_PREV_BETWEEN_1_9_1000000_ID = 3;
    public static int OD_PREV_LESS_1_1000000_ID      = 4;
    public static int OD_PREV_UNKNOWN_ID = 5;
    public static int OD_PREV_NODATA_ID = 6;
    public static int OD_PREV_NULL_ID = 7;
    
    
    
    /*Tutorial*/
    public static final String HELP_TEXT_ENTREZ = "The ID <b>Entrez</b> consists in a number of several digits that represents a gene, you can search the code of the gene that you need in <a href='http://www.ncbi.nlm.nih.gov/gene website'>NCBI</a>";
    public static final String HELP_TEXT_ENSEMBL = "The <b>Ensembl</b> names has the sintax <b>ENSG###</b>. The Ensembl project produces genome databases for vertebrates and other eukaryotic species. More information in <a href='http://www.ensembl.org/'>Ensmbl</a>";
    public static final String HELP_TEXT_MIM = "Each <b>MIM</b> code consists in a number of six digits to each gene and disease. You can use the browser in the <a href='http://www.omim.org/'>OMIM</a> web page";
    public static final String HELP_TEXT_ORPHANUM = "The <b>Orphanum</b> code is assigned for orphanet. Orphanet  assing codes to rare disease and genes. More information in <a href='http://www.orpha.net/'>Orphanet</a>";
    public static final String HELP_TEXT_HGNC = "The HUGO Gene Nomenclature Committee (HGNC) has assigned unique gene symbols and names to over 33,000 human loci, of which around 19,000 are protein coding. <a href='http://www.genenames.org/'>See more.</a>"; 
    public static final String HELP_TEXT_GENESYMBOL = "The HUGO Gene Nomenclature Committee (HGNC) has assigned unique gene symbols and names to over 33,000 human loci, of which around 19,000 are protein coding. <a href='http://www.genenames.org/'>See more.</a>"; 
    public static final String HELP_TEXT_HPRD = "The HPRD (Human Protein Reference Database) represents a centralized platform to visually depict and integrate information pertaining to domain architecture, post-translational modifications, interaction networks "
            + "and disease association for each protein in the human proteome. All the information in HPRD has been manually extracted from the literature by expert biologists who read, interpret and analyze the published data.   "
            + "<a href='http://www.hprd.org/'>See more.</a>"; 

    
    /*Relationships displayed for each type of subnetwork*/
    
    public static List<SubnetworkType> subnetworkType_monoGenes(){
  
        List<SubnetworkType> subnetworks = new ArrayList<SubnetworkType>();
        
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP, NET_REL_LABEL_SEMSIMGOBP, NET_REL_TITLE_SEMSIMGOBP,NET_REL_IMAGE_SEMSIMGOBP,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC, NET_REL_LABEL_SEMSIMGOCC, NET_REL_TITLE_SEMSIMGOCC,NET_REL_IMAGE_SEMSIMGOCC,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF, NET_REL_LABEL_SEMSIMGOMF, NET_REL_TITLE_SEMSIMGOMF,NET_REL_IMAGE_SEMSIMGOMF,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC, NET_REL_LABEL_SEMSIMHPO, NET_REL_TITLE_SEMSIMHPO,NET_REL_IMAGE_SEMSIMHPO,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_PPI, NET_REL_LABEL_PPI, NET_REL_TITLE_PPI,NET_REL_IMAGE_PPI,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_METABOLIC, NET_REL_LABEL_METABOLIC, NET_REL_TITLE_METABOLIC,NET_REL_IMAGE_METABOLIC,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_INFERRED_OMIM, NET_REL_LABEL_INFERRED_OMIM, NET_REL_TITLE_INFERRED_OMIM,NET_REL_IMAGE_INFERRED_OMIM,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_INFERRED_ORPHA,  NET_REL_LABEL_INFERRED_ORPHA,  NET_REL_TITLE_INFERRED_ORPHA, NET_REL_IMAGE_INFERRED_ORPHA,""));
        
        return subnetworks;
                
    }
    
    public static List<SubnetworkType> subnetworkType_monoDiseases(){
  
        List<SubnetworkType> subnetworks = new ArrayList<SubnetworkType>();
        
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC, NET_REL_LABEL_SEMSIMHPO, NET_REL_TITLE_SEMSIMHPO, NET_REL_IMAGE_SEMSIMHPO,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE, NET_REL_LABEL_INFERRED_GENE, NET_REL_TITLE_INFERRED_GENE, NET_REL_IMAGE_INFERRED_GENE,""));
       
        return subnetworks;

    }
    
    public static List<SubnetworkType> subnetworkType_monoRareDiseases(){
  
        List<SubnetworkType> subnetworks = new ArrayList<SubnetworkType>();
        
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC, NET_REL_LABEL_SEMSIMHPO, NET_REL_TITLE_SEMSIMHPO, NET_REL_IMAGE_SEMSIMHPO,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE, NET_REL_LABEL_INFERRED_GENE, NET_REL_TITLE_INFERRED_GENE, NET_REL_IMAGE_INFERRED_GENE,""));
       
        return subnetworks;
    }
    
    public static List<SubnetworkType> subnetworkType_bipGeneDiseases(){
  
        List<SubnetworkType> subnetworks = new ArrayList<SubnetworkType>();
        
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC, NET_REL_LABEL_SEMSIMHPO, NET_REL_TITLE_SEMSIMHPO, NET_REL_IMAGE_SEMSIMHPO,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_OMIM, NET_REL_LABEL_OMIM, NET_REL_TITLE_OMIM, NET_REL_IMAGE_OMIM,""));
       
        return subnetworks;
    }
    
    public static List<SubnetworkType> subnetworkType_bipGeneRareDiseases(){
        
        
        List<SubnetworkType> subnetworks = new ArrayList<SubnetworkType>();
        
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC, NET_REL_LABEL_SEMSIMHPO, NET_REL_TITLE_SEMSIMHPO,NET_REL_IMAGE_SEMSIMHPO,""));
        subnetworks.add(new SubnetworkType(NetworkConstants.NETWORK_REL_TYPE_ORPHADATA, NET_REL_LABEL_ORPHADATA, NET_REL_TITLE_ORPHADATA, NET_REL_IMAGE_ORPHADATA,""));
       
        return subnetworks;
    }
    
    
    /*Query Type*/
    public static List<QueryType> queryTypeList(){

        List<QueryType> queryType = new ArrayList<QueryType>();
        
        queryType.add(new QueryType(PhenumaConstants.ID_NETWORK_QUERY, PhenumaConstants.NETWORK_QUERY));
        queryType.add(new QueryType(PhenumaConstants.ID_ENRICHMENT_QUERY, PhenumaConstants.ENRICHMENT_QUERY));

        
        return queryType;
    }
   
    
    
    /*Input Type*/
    public static List<InputType> inputTypeList_Network(){

        List<InputType> inputType = new ArrayList<InputType>();
        
        inputType.add(new InputType(PhenumaConstants.ID_GENES_INPUT, PhenumaConstants.GENES_INPUT));
        inputType.add(new InputType(PhenumaConstants.ID_OMIM_INPUT, PhenumaConstants.OMIM_INPUT));
        inputType.add(new InputType(PhenumaConstants.ID_ORPHANUM_INPUT, PhenumaConstants.ORPHANUM_INPUT));
        inputType.add(new InputType(PhenumaConstants.ID_PHENOTYPE_INPUT, PhenumaConstants.PHENOTYPE_INPUT));

        
        return inputType;
    }
    
    public static List<InputType> inputTypeList_Enrichment(){

        List<InputType> inputType = new ArrayList<InputType>();
        
        inputType.add(new InputType(PhenumaConstants.ID_GENES_INPUT, PhenumaConstants.GENES_INPUT));
        inputType.add(new InputType(PhenumaConstants.ID_OMIM_INPUT, PhenumaConstants.OMIM_INPUT));
        inputType.add(new InputType(PhenumaConstants.ID_ORPHANUM_INPUT, PhenumaConstants.ORPHANUM_INPUT));

        
        return inputType;
    }
    
    
    
    /*Confidence type*/
    public static List<ConfidenceType> confidenceTypeList(){

        List<ConfidenceType> confidenceType = new ArrayList<ConfidenceType>();
        
        confidenceType.add(new ConfidenceType(PhenumaConstants.CONFIDENCE_LOW_ID, "Low"));
        confidenceType.add(new ConfidenceType(PhenumaConstants.CONFIDENCE_MEDIUM_ID, "Medium"));
        confidenceType.add(new ConfidenceType(PhenumaConstants.CONFIDENCE_HIGH_ID, "High"));

        
        return confidenceType;
    }
   
    
    /*Output Type*/
    public static List<NetworkType> networkTypeList_Genes(){
        
        List<NetworkType> networkType = new ArrayList<NetworkType>();
        
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_HPO, "Gene-Gene Semantic Similarity from HPO"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOBP, "Gene-Gene Semantic Similarity from GO Biological Process"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOCC, "Gene-Gene Semantic Similarity from GO Celular Component"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOMF, "Gene-Gene Semantic Similarity from GO Molecular Function"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_ORPHAN, "Gene-Gene Inferred from Orphanet"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_OMIM, "Gene-Gene Inferred from OMIM"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_PPI, "Gene-Gene Known Protein-Protein Interactions from STRING "));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_METABOLIC, "Gene-Gene Known Metabolic Interactions (Veeramani et al.)"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_DISEASE_SS, "OMIM-OMIM Semantic Similarity from HPO "));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_ORPHAN_SS, "ORPHA-ORPHA Semantic Similarity from HPO"));
        networkType.add(new NetworkType(NetworkConstants.BIPARTITE_GENE_DISEASE_GENEMAP, "Gene-OMIM Known OMIM Relationships"));
        networkType.add(new NetworkType(NetworkConstants.BIPARTITE_GENE_DISEASE_SS, "Gene-OMIM Semantic Similarity from HPO"));
        networkType.add(new NetworkType(NetworkConstants.BIPARTITE_GENE_ORPHAN_ORPHADATA, "Gene-ORPHA Known from Orphanet"));
        networkType.add(new NetworkType(NetworkConstants.BIPARTITE_GENE_ORPHAN_SS, "Gene-ORPHA Semantic Similarity from HPO"));

        
        return networkType;
    }
    
    
    public static List<NetworkType> networkTypeList_Diseases(){
        
        List<NetworkType> networkType = new ArrayList<NetworkType>();
        
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_DISEASE_SS, "OMIM-OMIM Semantic Similarity from HPO "));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_DISEASE_INFERRED_BY_GENE, "OMIM-OMIM Inferred from Genes"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_HPO, "Gene-Genes Semantic Similarity from HPO"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOBP, "Gene-Gene Semantic Similarity from GO Biological Process"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOCC, "Gene-Gene Semantic Similarity from GO Celular Component"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOMF, "Gene-Gene Semantic Similarity from GO Molecular Function"));
        networkType.add(new NetworkType(NetworkConstants.BIPARTITE_GENE_DISEASE_GENEMAP, "Gene-OMIM Known from OMIM"));
        networkType.add(new NetworkType(NetworkConstants.BIPARTITE_GENE_DISEASE_SS, "Gene-OMIM Semantic Similarity from HPO"));
        
        return networkType;
    }
    
    
    public static List<NetworkType> networkTypeList_RareDiseases(){
        
        List<NetworkType> networkType = new ArrayList<NetworkType>();
        
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_ORPHAN_SS, "ORPHA-ORPHA Semantic Similarity from HPO "));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_ORPHAN_INFERRED_BY_GENE, "ORPHA-ORPHA Inferred from Genes"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_HPO, "Gene-Genes Semantic Similarity from HPO"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOBP, "Gene-Gene Semantic Similarity from GO Biological Process"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOCC, "Gene-Gene Semantic Similarity from GO Celular Component"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_SS_GOMF, "Gene-Gene Semantic Similarity from GO Molecular Function"));
        networkType.add(new NetworkType(NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_ORPHAN, "Gene-Genes Inferred from Orphanet"));
        networkType.add(new NetworkType(NetworkConstants.BIPARTITE_GENE_ORPHAN_ORPHADATA, "Gene-ORPHA Known from Orphanet"));
        networkType.add(new NetworkType(NetworkConstants.BIPARTITE_GENE_ORPHAN_SS, "Gene-ORPHA Semantic Similarity from HPO"));
        
        return networkType;
    }
    
    
    public static List<NetworkType> networkTypeList_Phenotypes(){
        
        List<NetworkType> networkType = new ArrayList<NetworkType>();
        
        networkType.add(new NetworkType(NetworkConstants.QUERY_DISEASE_NETWORK, "OMIM (Gene/Diseases)"));
        networkType.add(new NetworkType(NetworkConstants.QUERY_ER_NETWORK, "Orphan Diseases"));
        networkType.add(new NetworkType(NetworkConstants.QUERY_GENE_NETWORK, "Genes"));
        
        return networkType;
    }
    
    public static List<EnrichmentType> enrichmentTypeList(){
        
        List<EnrichmentType> enrichmentType = new ArrayList<EnrichmentType>();
        
        enrichmentType.add(new EnrichmentType(PhenumaConstants.ID_PHENOTYPIC_ENRICHMENT, PhenumaConstants.PHENOTYPIC_ENRICHMENT));
        enrichmentType.add(new EnrichmentType(PhenumaConstants.ID_FUNCTIONAL_ENRICHMENT_BP, PhenumaConstants.FUNCTIONAL_ENRICHMENT_BP));
        enrichmentType.add(new EnrichmentType(PhenumaConstants.ID_FUNCTIONAL_ENRICHMENT_CC, PhenumaConstants.FUNCTIONAL_ENRICHMENT_CC));
        enrichmentType.add(new EnrichmentType(PhenumaConstants.ID_FUNCTIONAL_ENRICHMENT_MF, PhenumaConstants.FUNCTIONAL_ENRICHMENT_MF));
        
        return enrichmentType;
    }    
    
    
    /*Type of measure*/
    public static List<MeasureType> measureTypeList()
    {
        List<MeasureType> measuretype = new ArrayList<MeasureType>();
        
        measuretype.add(new MeasureType(Constants.ROBINSON_SYMMETRIC, "Robinson Symmetric"));
        measuretype.add(new MeasureType(Constants.RESNIK, "Resnik (max)"));
        
        return measuretype;
    }
    
    
    
    
    
    /*Type of identifier*/
    public static List<IdentifierType> geneIdentifiers(){
       List<IdentifierType> identifiers = new ArrayList<IdentifierType>();
        
        identifiers.add(new IdentifierType(ENTREZ_ID,"Entrez (Recommended)"));
        identifiers.add(new IdentifierType(ENSEMBL_ID,"Ensembl"));
        identifiers.add(new IdentifierType(SYMBOL_ID,"GeneSymbol (Official Symbol)"));//1-1
        identifiers.add(new IdentifierType(MIM_ID,"MIM"));
        identifiers.add(new IdentifierType(HGNC_ID,"HGNC"));//1-1
        identifiers.add(new IdentifierType(ORPHANUM_ID,"Orphanum"));

        return identifiers;
    }
    
    public static List<IdentifierType> diseasesIdentifiers(){
       List<IdentifierType> identifiers = new ArrayList<IdentifierType>();
        
       identifiers.add(new IdentifierType(OMIM_ID,"MIM"));   
       
       return identifiers;
    }
    
    public static List<IdentifierType> rareDiseasesIdentifiers(){
       List<IdentifierType> identifiers = new ArrayList<IdentifierType>();
        
       identifiers.add(new IdentifierType(ORPHANUM_ID,"Orphanum"));
       
       return identifiers;
    }
    
    public static List<IdentifierType> phenotypeIdentifiers(){
       List<IdentifierType> identifiers = new ArrayList<IdentifierType>();
        
       identifiers.add(new IdentifierType(HPO_ID, "HPO Id"));

       return identifiers;
    }
    
    
    
    /**
     * this method returns the numeric value of semantic similarity of the selected threshold.
     * This value depends on the type if query and the
     * type of confidence selected (low, medium, or high)
     * @param querytype
     *              type of requested query
     * @param confidencetype
     *              level of confident selected
     * @return 
     *              numeric value of the threshold
     */
    public static double getNumericConfidence(int querytype, int confidencetype)
    {

        switch (querytype) {
            case NetworkConstants.UNIPARTITE_GENE_SS_GOBP:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_BP_HIGH;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_BP_MEDIUM;
                                                case CONFIDENCE_LOW_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_BP_LOW;
                                                default:
                                                    break;
                                            }
                break;
                
            case NetworkConstants.UNIPARTITE_GENE_SS_GOCC:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_CC_HIGH;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_CC_MEDIUM;
                                                case CONFIDENCE_LOW_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_CC_LOW;
                                                default:
                                                    break;
                                            }
                break;
                
            case NetworkConstants.UNIPARTITE_GENE_SS_GOMF:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_MF_HIGH;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_MF_MEDIUM;
                                                case CONFIDENCE_LOW_ID:
                                                    return PhenumaConstants.CONFIDENCE_GO_MF_LOW;
                                                default:
                                                    break;
                                            }
                                            
                break;
                
            case NetworkConstants.UNIPARTITE_GENE_SS_HPO:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_GENES_HIGH;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_GENES_MEDIUM;
                                                case CONFIDENCE_LOW_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_GENES_LOW;
                                                default:
                                                    break;
                                            }
                break;
                
            case NetworkConstants.BIPARTITE_GENE_ORPHAN_SS:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return 0.08;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return 0.03;
                                                case CONFIDENCE_LOW_ID:
                                                    return 0;
                                                default:
                                                    break;
                                            }
                break;
                
            case NetworkConstants.UNIPARTITE_DISEASE_SS:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_OMIM_HIGH;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_OMIM_MEDIUM;
                                                case CONFIDENCE_LOW_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_OMIM_LOW;
                                                default:
                                                    break;
                                            }
                break;
                
            case NetworkConstants.UNIPARTITE_ORPHAN_SS:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_ORPHAN_HIGH;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_ORPHAN_MEDIUM;
                                                case CONFIDENCE_LOW_ID:
                                                    return PhenumaConstants.CONFIDENCE_HPO_ORPHAN_LOW;
                                                default:
                                                    break;
                                            }
                break;
                
            case NetworkConstants.QUERY_DISEASE_NETWORK:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return 2.702/ phenuma.constants.Constants.MAX_IC_DISEASES_HPO;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return 2.406/ phenuma.constants.Constants.MAX_IC_DISEASES_HPO;
                                                case CONFIDENCE_LOW_ID:
                                                    return 2.145/ phenuma.constants.Constants.MAX_IC_DISEASES_HPO;
                                                default:
                                                    break;
                                            }
                                            
                break;
                
            case NetworkConstants.QUERY_GENE_NETWORK:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return 2.37/ phenuma.constants.Constants.MAX_IC_GENES_HPO;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return 2.1/ phenuma.constants.Constants.MAX_IC_GENES_HPO;
                                                case CONFIDENCE_LOW_ID:
                                                    return 1.8794/ phenuma.constants.Constants.MAX_IC_GENES_HPO;
                                                default:
                                                    break;
                                            }
                                            
                break;
                
            case NetworkConstants.QUERY_ER_NETWORK:
                
                                            switch (confidencetype) {
                                                case CONFIDENCE_HIGH_ID:
                                                    return 2.327/ phenuma.constants.Constants.MAX_IC_ORPHAN_HPO;
                                                case CONFIDENCE_MEDIUM_ID:
                                                    return 2.091 / phenuma.constants.Constants.MAX_IC_ORPHAN_HPO;
                                                case CONFIDENCE_LOW_ID:
                                                    return 1.883 / phenuma.constants.Constants.MAX_IC_ORPHAN_HPO;
                                                default:
                                                    break;
                                            }
                                            
                break;
                
            default:
                break;
        }
 
        
        return 0;
        
    }
    
    
    /**
     * Name of relationship which is shown in the table.
     * 
     * @param relationship_type
     * @return 
     */
    public static String getRelationshipName(int relationship_type)
    {
        switch(relationship_type)
        {
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP: return "BP";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC: return "CC";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF: return "MF";
            case NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC:    return "HPO";   
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE: return "Inferred form Gene";
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_OMIM: return "Inferred form OMIM";
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_ORPHA: return "Inferred form Orphanet";
            case NetworkConstants.NETWORK_REL_TYPE_METABOLIC:    return "Metabolic";
            case NetworkConstants.NETWORK_REL_TYPE_PPI:    return "PPI STRING";
            case NetworkConstants.NETWORK_REL_TYPE_OMIM:    return "OMIM";
            case NetworkConstants.NETWORK_REL_TYPE_ORPHADATA:    return "Orphadata";
            
            default:
                return "";
        }
        
    }
    
    
    
    public static String getSourceWebLink(int relationship_type)
    {
        switch(relationship_type)
        {
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP: return Constants.GO_LINK;
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC: return Constants.GO_LINK;
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF: return Constants.GO_LINK;
            case NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC:    return Constants.HPO_LINK;
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE: return Constants.GENE_LINK;
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_OMIM: return Constants.OMIM_LINK;
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_ORPHA: return Constants.ORPHA_LINK;
            case NetworkConstants.NETWORK_REL_TYPE_METABOLIC:    return "Metabolic";
            case NetworkConstants.NETWORK_REL_TYPE_PPI:    return "PPI STRING";
            case NetworkConstants.NETWORK_REL_TYPE_OMIM:    return "OMIM";
            case NetworkConstants.NETWORK_REL_TYPE_ORPHADATA:    return "Orphadata";
            
            default:
                return "";
        }
    }
    
    
    
    public static String getTableTitleID(int relationship_type)
    {
        switch(relationship_type)
        {
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP: return "GO Id";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC: return "GO Id";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF: return "GO Id";
            case NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC:    return "HPO Id";
            
            default:
                return "";
        }
    }
    
    
    public static String getTableTitleName(int relationship_type)
    {
        switch(relationship_type)
        {
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP: return "GO Name";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC: return "GO Name";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF: return "GO Name";
            case NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC:    return "HPO Name";
            
            default:
                return "";
        }
    }
    
    
    /**
     * Log messages
     */
    
    final static String LOGMESSAGES_CREATING_GENE_NETWORK_DB = "Creating gene network from database.";
    final static String LOGMESSAGES_CREATING_OMIM_NETWORK_DB = "Creating omim network from database.";
    final static String LOGMESSAGES_CREATING_ORPHANUM_NETWORK_DB = "Creating orphanum network from database.";
    final static String LOGMESSAGES_CREATING_PHENOTYPE_NETWORK_DB = "Creating phenotype network from database.";
    
}
