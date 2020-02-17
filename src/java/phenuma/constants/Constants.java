package phenuma.constants;

import phenomizer.hpo.Phenuma;



public class Constants {
          
        /**
         * LINKS 
         */
        public static final String HPO_LINK = "http://www.human-phenotype-ontology.org/hpoweb/showterm?id=";
        public static final String OMIM_LINK = "http://omim.org/entry/";
        public static final String GENE_LINK = "http://www.ncbi.nlm.nih.gov/gene/";
        public static final String GO_LINK = "http://amigo.geneontology.org/cgi-bin/amigo/term_details?term=";
        public static final String ORPHA_LINK = "http://www.orpha.net/consor/cgi-bin/OC_Exp.php?lng=EN&Expert=";

        /**
         * PERSISTENCE UNIT  
         */
         public static final String PERSISTENCE_UNIT_PHENUMA_DB = "PhenumaDBPU";

         /* 
         * FOLDERS
         */        
        public static final String RESOURCES = Phenuma.getInstance().getResources_location()+"resources/";
        public static final String IN = "in/";
        public static final String ONTOLOGIES = "ontologies/";
        public static final String ANNOTATIONS = "annotations/";
        public static final String TESTS = "tests/";
        public static final String TEMP = RESOURCES+"temp/";

        
        /**
         * ONTOLOGIES PATH
         */
        public static final String HPO_PATH = RESOURCES+IN+ONTOLOGIES+"human-phenotype-ontology.obo";
        public static final String HPO_PATH_SEPT = RESOURCES+IN+ONTOLOGIES+"human-phenotype-ontology-20120910.obo";
        
        
	public static final String GO_PATH = RESOURCES+IN+ONTOLOGIES+"gene_ontology_ext.obo";
        public static final String GO_NOHASPART_PATH = RESOURCES+IN+ONTOLOGIES+"gene_ontology.1_2_nohaspart.obo";
        
        
        /**
         * ONTOLOGIES
         */
        
        /* HPO. Type of annotations genes or diseases. We will always work with the subontology Phenotypic Abnormality*/
        public static final int ONTO_HPO = 0;
        
        /* GO. Subontologies. The elements annotated will be always genes.*/
        public static final int ONTO_GO_BP = 4;
        public static final int ONTO_GO_MF = 5;
        public static final int ONTO_GO_CC = 6;
        
        
        /**
         * ANNOTATIONS
         */
        public static final int ASSOC_DISEASES_HPO = 0;
        public static final int ASSOC_ER_HPO = 1;
        public static final int ASSOC_GENES_HPO = 2;
        
        
        public static final int ASSOC_GENES_GO_BP = 3;
        public static final int ASSOC_GENES_GO_CC = 4;
        public static final int ASSOC_GENES_GO_MF = 5;
        
        public static final int ASSOC_SNP_HPO = 6;
        public static final int ASSOC_DISEASE_MEDGEN_HPO = 7;
        public static final int ASSOC_ALLELEID_HPO = 8;
        
        
        /**
         * MAXIMUM IC's
         */
        
        public static final double MAX_IC_GENES_HPO = 7.757478766584179;
        public static final double MAX_IC_DISEASES_HPO = 8.626944055375356;
        public static final double MAX_IC_ORPHAN_HPO = 8.052933036797567;
        public static final double MAX_IC_GENES_GOBP = 9.454540665816003;
        public static final double MAX_IC_GENES_GOMF = 9.510148624225804;
        public static final double MAX_IC_GENES_GOCC = 9.402034535016803;
        public static double CONSTANTE = 3;
        
        /*Prefixes*/
        
        public static final String HPO_PREFIX = "HP";
        public static final String GO_PREFIX = "GO";
        
        
        /**
         * SUBONTOLOGIES
         */
        
        //hpo
        public static final String SUBONTO_PHENOTYPIC_ABNORMALITY = "Phenotypic abnormality";
        public static final String SUBONTO_ONSET_AND_CLINICAL_COURSE = "Onset and clinical course";
        public static final String SUBONTO_MODE_OF_INHERITANCE = "Mode of inheritance";
         
        //go
	public static final String SUBONTO_MOLECULAR_FUNCTION = "molecular_function";
        public static final String SUBONTO_CELLULAR_COMPONENT = "cellular_component";
        public static final String SUBONTO_BIOLOGICAL_PROCESS = "biological_process";
        
        
        /**
         * ANNOTATIONS
         */
        
        //go annotations
	public static final String ASSOCIATION_GO_HUMAN_PATH = RESOURCES+IN+ANNOTATIONS+"gene_associations_unitprotkb_go.txt";
        public static final String ASSOCIATION_ENTREZGENE_GO_PATH = RESOURCES+IN+ANNOTATIONS+"gene_associations_entrez_go.txt";    //protein2geneid 
        
        //hpo annotations

        public static final String ASSOCIATION_DISEASE_PATH = RESOURCES+IN+ANNOTATIONS+"disease_annotation_omim_hpo.txt";
        public static final String ASSOCIATION_DISEASE_MEDGEN_PATH = RESOURCES+IN+ANNOTATIONS+"disease_annotation_medgen_hpo.txt";
	public static final String ASSOCIATION_GENE_HPO_PATH = RESOURCES+IN+ANNOTATIONS+"gene_associations_entrez_hpo.txt";
        public static final String ASSOCIATION_SNP_HPO_PATH = RESOURCES+IN+ANNOTATIONS+"snp_associations_hpo_beta.txt";
        public static final String ASSOCIATION_ALLELEID_HPO_PATH = RESOURCES+IN+ANNOTATIONS+"variation_annotation_alleleid_hpo.txt";
        //er annotations
        public static final String ASSOCIATION_ER_PATH = RESOURCES+IN+ANNOTATIONS+"er_associations_orphanum_hpo.txt";
        public static final String ASSOCIATION_GENEER_PATH = RESOURCES+IN+ANNOTATIONS+"geneer_associations_entrezid_hpo.txt";
            
            
            
        /**
         *  TESTS 
         */    
        public static final String TEST_MONOPARTITE_DISEASE_NETWORK = RESOURCES+IN+TESTS+"monopartiteDiseaseNetwork.txt";   
        public static final String TEST_MONOPARTITE_GENE_NETWORK = RESOURCES+IN+TESTS+"monopartiteGeneNetwork.txt"; 
        
        
        /**
         * SEMANTIC SIMILARITY MEASURE (GROUPS OF TERMS)
         */
        
        public static final int RESNIK  = 0;
        public static final int ROBINSON = 1;
        public static final int ROBINSON_SYMMETRIC = 2;
        
        public static final int PHENUMA = 3;
        public static final int PHENUMA_SYMMETRIC = 4;
        public static final int MEDIAN_SS = 5;
        public static final int MEDIAN_SS_SYMMETRIC = 6;
        public static final int THIRD_QUARTILE_SS = 7;
        public static final int THIRD_QUARTILE_SS_SYMMETRIC = 8;
        public static final int MODE_SS = 9;
        public static final int MODE_SS_SYMMETRIC = 10;
       
        public static final int ROBINSON_AND = 11;
        public static final int ROBINSON_AND_SYMMETRIC = 12;
        
        
        /**
         * TERM SEMANTIC SIMILARITY MEASURE
         */
        public static final int RESNIK_TSS = 0;
        public static final int JIANGCONRATH_TSS = 1;
        
        /**
         * ERROR MESSAGES
         */
	public static final String ONTOLOGY_NOT_FOUND = "Ontology file not found.";
	public static final String ANNOTATION_NOT_FOUND = "Annotation file not found.";
	public static final String QUERY_NOT_FOUND = "Please provide an query file.";
	
	public static final String FORMAT_INPUT_ERROR = "The input format is incorrect. HPO terms sintax is HP:#######";
	public static final String EMPTY_QUERY = "The query is empty. You must provide a list of HPO terms or phenotype profiles.";
	
	
	//Database
	public static final String ERROR_CLOSE_CONNECTION = "The database connection could not be closed.";
	public static final String QUERY_EXECUTION_ERROR = "The query could not be executed.";
	
        
        
        /**
         * DATABASE 
         */
        
        public static final String CONNECTION_STR = "jdbc:mysql://150.214.214.39/phenomizerdb";
        public static final String PASSWORD = "37$A72a9";
        public static final String USER = "phenuma_admin";
        
        //local en cath
        public static final String PASSWORD_ROOT = "13bmbg35";
        public static final String ROOT = "root";
                
                
        public static final String DATABASE = "phenumadb";
        
        /**
         * The pvalues table has been divided in 10 tables (one table for each query size).
         */
        public static final String DISTRIBUTION_TB = "distributions_size_";

        
        public static final String DISEASE_MATRIX_HPO_TB = "disease_matrix_hpo";
        public static final String RARE_DISEASE_TB = "rare_disease";
        public static final String ORPHANUM_MATRIX_HPO_TB = "orphanum_matrix_hpo";
        
        public static final String GENE_GENE_RELATIONSHIPS_TAB = DATABASE+".gene_gene_relationships";
        public static final String OMIM_OMIM_RELATIONSHIPS_TAB = DATABASE+".omim_omim_relationships";
        public static final String ORPHA_ORPHA_RELATIONSHIPS_TAB = DATABASE+".orpha_orpha_relationships";
        
        public static final String GENE_GENE_RELATIONSHIPS_VIEW = DATABASE+".gene_gene_relationships";
        public static final String OMIM_OMIM_RELATIONSHIPS_VIEW = DATABASE+".omim_omim_relationships";
        public static final String ORPHA_ORPHA_RELATIONSHIPS_VIEW= DATABASE+".orpha_orpha_relationships";
        public static final String GENE_OMIM_RELATIONSHIPS_VIEW = DATABASE+".gene_omim_relationships";
        public static final String GENE_ORPHA_RELATIONSHIPS_VIEW= DATABASE+".gene_orpha_relationships";
        //columns
        public static final String ADJUSTMENT_BON_COL = "adjustment_bon";
        public static final String ADJUSTMENT_BH_COL = "adjustment_bh";
        public static final String PVALUE_COL = "pvalue";
        public static final String SCORE_COL = "score";
        
        public static final String GENEX_COL ="geneX";
        public static final String GENEY_COL = "geneY";
        public static final String ORPHANUMX_COL ="orphanumX";
        public static final String ORPHANUMY_COL = "orphanumY";
        public static final String ORPHAX_COL = "orphaX";
        public static final String ORPHAY_COL = "orphaY";
        public static final String OMIMX_COL = "omimX";
        public static final String OMIMY_COL = "omimY";
        
        public static final String ID_COL = "id";
        public static final String TERM_COL = "term";
        public static final String DISEASE_COL = "disease";
        public static final String NAME_COL = "name";
        public static final String IS_OBSOLETE_COL = "is_obsolete";
        public static final String IS_ROOT_COL = "is_root";
        public static final String ONTOLOGY_COL = "ontology";
        public static final String ACC_COL = "acc";
        public static final String TERM1_ID = "term1_id";
        public static final String TERM2_ID = "term2_id";
        public static final String RELATIONSHIP_TYPE_COL = "relationship_type";
        
        public static final String DISEASEX_COL = "diseaseX";
        public static final String DISEASEY_COL = "diseaseY";
        
        
        /**
         * We only store pvalue for query size less or equal then 10. 
         */
        public static final int MAX_QUERYSIZE=10;
        
        

        

	
}
