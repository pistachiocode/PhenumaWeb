/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.networkproyection;


/**
 *
 * @author Armando
 */
public class NetworkConstants {
    
    /**
     * Monopartite networks
     */
    
    public static final int UNIPARTITE_GENE_SS_GOBP = 113;
    public static final int UNIPARTITE_GENE_SS_GOCC = 114;
    public static final int UNIPARTITE_GENE_SS_GOMF = 115;
    public static final int UNIPARTITE_GENE_SS_HPO = 151;
    public static final int UNIPARTITE_DISEASE_SS = 152;
    public static final int UNIPARTITE_ORPHAN_SS = 153;

    public static final int UNIPARTITE_DISEASE_INFERRED_BY_GENE = 107;
    public static final int UNIPARTITE_GENE_INFERRED_BY_OMIM = 108;
    public static final int UNIPARTITE_GENE_INFERRED_BY_ORPHAN = 109;
    public static final int UNIPARTITE_ORPHAN_INFERRED_BY_GENE = 110;
    public static final int UNIPARTITE_GENE_PPI = 111;
    public static final int UNIPARTITE_GENE_METABOLIC = 112;
    
    
    public static final int BIPARTITE_GENE_HPO = 201;
    public static final int BIPARTITE_GENE_DISEASE_SS = 202;
    public static final int BIPARTITE_GENE_DISEASE_GENEMAP = 203;
    public static final int BIPARTITE_GENE_ORPHAN_ORPHADATA = 204;
    public static final int BIPARTITE_GENE_ORPHAN_SS = 205;
    public static final int BIPARTITE_DISEASE_HPO = 206;
    public static final int BIPARTITE_ORPHAN_HPO = 207;
    public static final int BIPARTITE_GENE_ORPHAN = 208;
    

    /**
     * Query networks
     */
    public static final int QUERY_GENE_NETWORK = 401;
    public static final int QUERY_ER_NETWORK = 402;
    public static final int QUERY_DISEASE_NETWORK = 403;
    
    
    
    /**
     * RelationshipType
     */
    public static final int NETWORK_REL_TYPE_PHENOTYPIC = 0;
    public static final int NETWORK_REL_TYPE_FUNCTIONAL_BP = 1;
    public static final int NETWORK_REL_TYPE_FUNCTIONAL_CC = 2;
    public static final int NETWORK_REL_TYPE_FUNCTIONAL_MF = 3;
    public static final int NETWORK_REL_TYPE_INFERRED_OMIM = 4;
    public static final int NETWORK_REL_TYPE_INFERRED_ORPHA = 5;
    public static final int NETWORK_REL_TYPE_INFERRED_GENE = 6;
    public static final int NETWORK_REL_TYPE_PPI = 7;
    public static final int NETWORK_REL_TYPE_METABOLIC = 8;
    public static final int NETWORK_REL_TYPE_OMIM = 9;
    public static final int NETWORK_REL_TYPE_ORPHADATA = 10;
    public static final int NETWORK_REL_PHENOTYPIC_QUERY_ER = 11;
    public static final int NETWORK_REL_PHENOTYPIC_QUERY_DISEASE = 12;
    public static final int NETWORK_REL_PHENOTYPIC_QUERY_GENE = 13;
    
    
    /*
     * Gene Gene Relationships position
     */    
    public static final int DATABASE_MATRIX_ID_POS = 0;
    public static final int DATABASE_MATRIX_OBJECTX_POS = 1;
    public static final int DATABASE_MATRIX_OBJECTY_POS = 2;
    
    public static final int GENEGENE_REL_PHENOTYPIC_POS = 3;
    public static final int GENEGENE_REL_FUNCTIONAL_GO_BP_POS = 4;
    public static final int GENEGENE_REL_FUNCTIONAL_GO_CC_POS = 5;
    public static final int GENEGENE_REL_FUNCTIONAL_GO_MF_POS = 6;
    public static final int GENEGENE_REL_PPI_POS = 7;
    public static final int GENEGENE_REL_METABOLIC_POS = 8;
    public static final int GENEGENE_REL_INFERRED_OMIM_POS = 9;
    public static final int GENEGENE_REL_INFERRED_ORPHA_POS = 10;
    public static final int GENEGENE_REL_GENEX_SYMBOL_POS = 11;
    public static final int GENEGENE_REL_GENEY_SYMBOL_POS = 12;
    
    /**
     * Omim Omim Relationships position
     */
    public static final int OMIMOMIM_REL_PHENOTYPIC_POS = 3;   
    public static final int OMIMOMIM_REL_INFERRED_GENE_POS = 4;
    
    /**
     * Orpha Orpha Relationships position
     */
    public static final int ORPHAORPHA_REL_PHENOTYPIC_POS = 3;
    public static final int ORPHAORPHA_REL_INFERRED_GENE_POS = 4;
    
    public static final int GENEOMIM_REL_KNOWN_POS = 3;
    public static final int GENEOMIM_REL_PHENOTYPIC_GENE_POS = 4;
    public static final int GENEOMIM_REL_PHENOTYPIC_OMIM_POS = 5;
    public static final int GENEOMIM_REL_GENE_SYMBOL_POS = 7;
 
    public static final int GENEORPHA_REL_KNOWN_POS = 3;
    public static final int GENEORPHA_REL_PHENOTYPIC_GENE_POS = 4;
    public static final int GENEORPHA_REL_PHENOTYPIC_ORPHA_POS = 5;
    public static final int GENEORPHA_REL_GENE_SYMBOL_POS = 6;

    public static final int QUERYOBJECT_REL_PHENOTYPIC_POS = 2;
    public static final int QUERYOBJECT_REL_PVALUE = 3;
    

            
    /**
     * Object type
     */

    public static final int INPUT_TYPE_GENES = 0;
    public static final int INPUT_TYPE_OMIM = 1;
    public static final int INPUT_TYPE_ORPHA = 2;
    

    public static final String COL_NAME_GENE = "gene";
    public static final String COL_NAME_OMIM = "omim";
    public static final String COL_NAME_ORPHA = "orpha";

    public static String REL_PHENOTYPIC_TAG = "phenotypic";
    public static String REL_PHENOTYPIC_OMIM_TAG = "phenotypic_omim";
    public static String REL_PHENOTYPIC_ORPHA_TAG = "phenotypic_orpha";
    public static String REL_PHENOTYPIC_GENE_TAG = "phenotypic_gene";
    public static String REL_KNOWN_TAG = "known";
    public static String REL_FUNCTIONAL_GO_BP_TAG = "functional_bp";
    public static String REL_FUNCTIONAL_GO_CC_TAG = "functional_cc";
    public static String REL_FUNCTIONAL_GO_MF_TAG = "functional_mf";
    public static String REL_PPI_TAG = "ppi";
    public static String REL_METABOLIC_TAG = "metabolic";
    public static String REL_INFERRED_OMIM_TAG = "inferred_omim";
    public static String REL_INFERRED_ORPHA_TAG= "inferred_orpha";
    public static String REL_INFERRED_GENE_TAG= "inferred_gene";

 
    
    
}
