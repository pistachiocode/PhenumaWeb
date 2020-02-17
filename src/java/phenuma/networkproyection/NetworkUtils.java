/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.networkproyection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import ontologizer.StudySet;
import ontologizer.types.ByteString;
import phenomizer.hpo.Phenuma;
import phenomizer.utils.FileUtils;
import phenomizer.utils.NumberUtils;
import phenuma.constants.Constants;
import phenuma.dataqueries.DatabaseQueries;
import phenuma.entities.Disease;
import phenuma.entities.Gene;
import phenuma.entities.RareDisease;
import phenuma.network.NetworkDB;
import phenuma.network.Node;
import phenuma.network.NodeDisease;
import phenuma.network.NodeGene;
import phenuma.network.NodeRareDisease;
import phenuma.network.PhenotypeNetworkDB;

/**
 *
 * @author Armando
 */
public class NetworkUtils {

    /**
     * This method restunrs the relationship type from the ontology used.
     * @param ontologyID
     * @return 
     */
    public static int getRelationshipTypeByOntology(int ontologyID){
         switch(ontologyID)
        {
            case Constants.ONTO_HPO :    return NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC;
            case Constants.ONTO_GO_BP :  return NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP;
            case Constants.ONTO_GO_MF :  return NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF;
            case Constants.ONTO_GO_CC :  return NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC;
            default:
                return -1;
        }
    }
    
    
    public static int getInferredRelationshipTypeByObject(Object object){
        
        if(object instanceof Disease)
            return NetworkConstants.NETWORK_REL_TYPE_INFERRED_OMIM;      
        else if(object instanceof Gene)
            return NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE;  
        else if(object instanceof RareDisease)
            return NetworkConstants.NETWORK_REL_TYPE_INFERRED_ORPHA;
        
        return -1;
        
    }
    
    
    
    public static String getRelationshipTypeNameById(int relationship_type){
        
        switch(relationship_type)
        {
            case NetworkConstants.NETWORK_REL_TYPE_PHENOTYPIC :  return "phenotypic";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_BP :  return "gobp";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_CC :  return "gocc";
            case NetworkConstants.NETWORK_REL_TYPE_FUNCTIONAL_MF :  return "gomf";
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_GENE :  return "infergene";
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_OMIM :  return "inferomim";
            case NetworkConstants.NETWORK_REL_TYPE_INFERRED_ORPHA :  return "inferorpha";
            case NetworkConstants.NETWORK_REL_TYPE_PPI :  return "ppi";
            case NetworkConstants.NETWORK_REL_TYPE_METABOLIC :  return "metabolic";
            case NetworkConstants.NETWORK_REL_TYPE_OMIM :  return "omim";  
            case NetworkConstants.NETWORK_REL_TYPE_ORPHADATA :  return "orphadata";     
            default:
                return "";
        }
            
        
    }

    public static String getRelationshipTypeNameByTablePos(int table_pos, int querytype){

        if (!NetworkUtils.isBipartite(querytype)) {
       
            if (NetworkUtils.isUnipartiteGenes(querytype) || querytype == NetworkConstants.QUERY_GENE_NETWORK)
            {
                if (table_pos == NetworkConstants.GENEGENE_REL_PHENOTYPIC_POS )
                {
                    return "phenotypic";
                }
                else if (table_pos == NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_BP_POS)
                {
                    return "gobp";
                }
                else if (table_pos == NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_CC_POS)
                {
                    return "gocc";
                }
                else if (table_pos == NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_MF_POS)
                {
                    return "gomf";
                }
                else if (table_pos == NetworkConstants.GENEGENE_REL_PPI_POS)
                {
                    return "ppi";
                }
                else if (table_pos == NetworkConstants.GENEGENE_REL_METABOLIC_POS)
                {
                    return "metabolic";
                }
                else if (table_pos == NetworkConstants.GENEGENE_REL_INFERRED_OMIM_POS)
                {
                    return "inferomim";
                }
                else if (table_pos == NetworkConstants.GENEGENE_REL_INFERRED_ORPHA_POS)
                {
                    return "inferorpha";
                }
                else if (table_pos == NetworkConstants.ORPHAORPHA_REL_INFERRED_GENE_POS || table_pos == NetworkConstants.OMIMOMIM_REL_INFERRED_GENE_POS)
                {
                    return "infergene";
                }
           }
           else if (NetworkUtils.isUnipartiteDiseases(querytype)|| querytype == NetworkConstants.QUERY_DISEASE_NETWORK)
           {
                if (table_pos == NetworkConstants.OMIMOMIM_REL_PHENOTYPIC_POS )
                {
                    return "phenotypic";
                }
                else if (table_pos == NetworkConstants.OMIMOMIM_REL_INFERRED_GENE_POS)
                {
                    return "infergene";
                }
           
           }
           else if (NetworkUtils.isUnipartiteOrphan(querytype)|| querytype == NetworkConstants.QUERY_ER_NETWORK)
           {
                if (table_pos == NetworkConstants.ORPHAORPHA_REL_PHENOTYPIC_POS )
                {
                    return "phenotypic";
                }
                else if (table_pos == NetworkConstants.ORPHAORPHA_REL_INFERRED_GENE_POS)
                {
                    return "infergene";
                }
           }
        
        }
        else
        {
            if (table_pos == NetworkConstants.GENEOMIM_REL_PHENOTYPIC_GENE_POS || table_pos == NetworkConstants.GENEOMIM_REL_PHENOTYPIC_OMIM_POS || 
                table_pos == NetworkConstants.GENEORPHA_REL_PHENOTYPIC_GENE_POS || table_pos == NetworkConstants.GENEORPHA_REL_PHENOTYPIC_ORPHA_POS)
            {
                return "phenotypic";
            }
            else if (table_pos == NetworkConstants.GENEORPHA_REL_KNOWN_POS && isBipartiteGeneOrphan(querytype))
            {
                return "orphadata";
            }
            else if (table_pos == NetworkConstants.GENEOMIM_REL_KNOWN_POS)
            {
                return "omim";
            }
        }
        
        return "";
        
    }
    
    
    
    public static String getNodeIdentifier(Node n){
        String str = "";
        
        if(n instanceof NodeDisease){
            Disease d = (Disease)n.getElement();
            str = d.getOmim().toString();
        }
        else if(n instanceof NodeGene)
        {
            Gene g = (Gene)n.getElement();
            str = g.getEntrezid().toString();
        }
        else if (n instanceof NodeRareDisease)
        {
            RareDisease er = (RareDisease)n.getElement();
            str = er.getOrphanum().toString();
        }
            
        return str;    
        
    }

    public static boolean isBipartite(int querytype)
    {
        return (querytype == NetworkConstants.BIPARTITE_GENE_DISEASE_GENEMAP || 
                querytype == NetworkConstants.BIPARTITE_GENE_DISEASE_SS ||
                querytype == NetworkConstants.BIPARTITE_GENE_ORPHAN_ORPHADATA || 
                querytype == NetworkConstants.BIPARTITE_GENE_ORPHAN_SS);
    }
    
    
    public static boolean isUnipartitePhenotypic(int querytype)
    {
        return (querytype == NetworkConstants.UNIPARTITE_GENE_SS_HPO || 
                querytype == NetworkConstants.UNIPARTITE_ORPHAN_SS || 
                querytype == NetworkConstants.UNIPARTITE_DISEASE_SS);
    }
 
    
    public static boolean isUnipartiteGenes(int querytype)
    {
        return (querytype == NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_OMIM || 
                querytype == NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_ORPHAN ||
                querytype == NetworkConstants.UNIPARTITE_GENE_METABOLIC || 
                querytype == NetworkConstants.UNIPARTITE_GENE_PPI ||
                querytype == NetworkConstants.UNIPARTITE_GENE_SS_GOBP ||
                querytype == NetworkConstants.UNIPARTITE_GENE_SS_GOCC ||
                querytype == NetworkConstants.UNIPARTITE_GENE_SS_GOMF || 
                querytype == NetworkConstants.UNIPARTITE_GENE_SS_HPO);
    }
    

    public static boolean isUnipartiteDiseases(int querytype)
    {
        return (querytype == NetworkConstants.UNIPARTITE_DISEASE_SS || 
                querytype == NetworkConstants.UNIPARTITE_DISEASE_INFERRED_BY_GENE);
    }
    
    
    public static boolean isUnipartiteOrphan(int querytype)
    {
        return (querytype == NetworkConstants.UNIPARTITE_ORPHAN_SS || 
                querytype == NetworkConstants.UNIPARTITE_ORPHAN_INFERRED_BY_GENE);
    }
    
    
    public static boolean isBipartiteGeneDiseases(int querytype)
    {
        return (querytype == NetworkConstants.BIPARTITE_GENE_DISEASE_GENEMAP || 
                querytype == NetworkConstants.BIPARTITE_GENE_DISEASE_SS);
    }
    
    
    public static boolean isBipartiteGeneOrphan(int querytype)
    {
        return (querytype == NetworkConstants.BIPARTITE_GENE_ORPHAN_ORPHADATA || 
                querytype == NetworkConstants.BIPARTITE_GENE_ORPHAN_SS);
    }    
    
    
    public static boolean isPhenotypeQuery(int querytype)
    {
        return (querytype == NetworkConstants.QUERY_DISEASE_NETWORK || 
                querytype == NetworkConstants.QUERY_GENE_NETWORK || 
                querytype == NetworkConstants.QUERY_ER_NETWORK);
    }
    
    
    public static Integer getPositionFirstRelationship (int outputNetwork)
    {
       
        
        if( outputNetwork == NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_ORPHAN || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_SS_HPO || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_SS_GOBP || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_SS_GOCC || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_SS_GOMF || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_OMIM || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_METABOLIC ||
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_PPI ||
            outputNetwork == NetworkConstants.QUERY_GENE_NETWORK)
        {
            return NetworkConstants.GENEGENE_REL_PHENOTYPIC_POS;
        }
        else if ( outputNetwork == NetworkConstants.UNIPARTITE_DISEASE_SS || 
                  outputNetwork == NetworkConstants.UNIPARTITE_DISEASE_INFERRED_BY_GENE||
                  outputNetwork == NetworkConstants.QUERY_DISEASE_NETWORK)
        {
            return NetworkConstants.OMIMOMIM_REL_PHENOTYPIC_POS;
        }
        else if ( outputNetwork == NetworkConstants.UNIPARTITE_ORPHAN_INFERRED_BY_GENE || 
                  outputNetwork == NetworkConstants.UNIPARTITE_ORPHAN_SS||
                  outputNetwork == NetworkConstants.QUERY_ER_NETWORK)
        {
            return NetworkConstants.ORPHAORPHA_REL_PHENOTYPIC_POS;
        }
        else if ( outputNetwork == NetworkConstants.BIPARTITE_GENE_DISEASE_GENEMAP|| 
                  outputNetwork == NetworkConstants.BIPARTITE_GENE_DISEASE_SS)
        {
            return NetworkConstants.GENEOMIM_REL_KNOWN_POS;
        }
        else if ( outputNetwork == NetworkConstants.BIPARTITE_GENE_ORPHAN_ORPHADATA || 
                  outputNetwork == NetworkConstants.BIPARTITE_GENE_ORPHAN_SS)
        {
            return NetworkConstants.GENEORPHA_REL_KNOWN_POS;
        }
        else{
            return 3; //la primera relacion en las matrices almacenadas siempre es 3
        }
            
        
        
      //  return null;
    }
    
    
    
    public static Integer getPositionLastRelationship (int outputNetwork)
    {
       
        
        if( outputNetwork == NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_ORPHAN || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_SS_HPO || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_SS_GOBP || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_SS_GOCC || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_SS_GOMF || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_INFERRED_BY_OMIM || 
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_METABOLIC ||
            outputNetwork == NetworkConstants.UNIPARTITE_GENE_PPI ||
            outputNetwork == NetworkConstants.QUERY_GENE_NETWORK)
        {
            return NetworkConstants.GENEGENE_REL_INFERRED_ORPHA_POS;
        }
        else if ( outputNetwork == NetworkConstants.UNIPARTITE_DISEASE_SS || 
                  outputNetwork == NetworkConstants.UNIPARTITE_DISEASE_INFERRED_BY_GENE||
                  outputNetwork == NetworkConstants.QUERY_DISEASE_NETWORK)
        {
            return NetworkConstants.OMIMOMIM_REL_INFERRED_GENE_POS;
        }
        else if ( outputNetwork == NetworkConstants.UNIPARTITE_ORPHAN_INFERRED_BY_GENE || 
                  outputNetwork == NetworkConstants.UNIPARTITE_ORPHAN_SS||
                  outputNetwork == NetworkConstants.QUERY_ER_NETWORK)
        {
            return NetworkConstants.ORPHAORPHA_REL_INFERRED_GENE_POS;
        }
        else if ( outputNetwork == NetworkConstants.BIPARTITE_GENE_DISEASE_GENEMAP|| 
                  outputNetwork == NetworkConstants.BIPARTITE_GENE_DISEASE_SS)
        {
            return NetworkConstants.GENEOMIM_REL_PHENOTYPIC_OMIM_POS;
        }
        else if ( outputNetwork == NetworkConstants.BIPARTITE_GENE_ORPHAN_ORPHADATA || 
                  outputNetwork == NetworkConstants.BIPARTITE_GENE_ORPHAN_SS)
        {
            return NetworkConstants.GENEORPHA_REL_PHENOTYPIC_ORPHA_POS;
        }
        else{
            return 3; //la primera relacion en las matrices almacenadas siempre es 3
        }
    }
    
    
    /**
     * Create a String containing the header of the output file. The type of 
     * the relationships included depends on the type of query executed and
     * the type of network generated.
     * 
     * @param querytype
     * @return 
     */
    
    public static String getCompleteNetworkHeader(int querytype)
    {
    
        String str = "";
        
        if (!NetworkUtils.isBipartite(querytype))
        {
            if (NetworkUtils.isUnipartiteGenes(querytype) || querytype == NetworkConstants.QUERY_GENE_NETWORK)
            {
                str = str + "# Relationships: \n";
                str = str + "# - gobp : Biological Process (Gene Ontology) \n";
                str = str + "# - gocc : Celullar Component (Gene Ontology) \n";
                str = str + "# - gomf : Molecular Function (Gene Ontology) \n";
                str = str + "# - phenotypic : Phenotypic Similarity (Human Phenotype Ontology) \n";
                str = str + "# - ppi : Protein-Protein Interaction (STRING) \n";
                str = str + "# - metabolic : Metabolic Flux Correlation \n";
                str = str + "# - inferomim : Inferred Relationship by OMIM. These genes are related with the same set of OMIM diseases. \n";
                str = str + "# - inferorpha : Inferred Relationship by Orphan Diseases (Orphadata). These genes are related with the same set of orphan diseases. \n";
                str = str + "# geneX   geneY  score   type isnovel\n";
            }
            else if (NetworkUtils.isUnipartiteDiseases(querytype) || NetworkUtils.isUnipartiteOrphan(querytype) || querytype == NetworkConstants.QUERY_DISEASE_NETWORK || querytype == NetworkConstants.QUERY_ER_NETWORK)
            {
                str = str + "# Relationships: \n";;
                str = str + "# - phenotypic : Phenotypic Similarity (Human Phenotype Ontology) \n";
                str = str + "# - infergene : Inferred Relationship by Gene. These diseases are related with the same set of genes. \n";
                str = str + "# disease  disease  score   type   isnovel\n";
            }
            
        }
        else
        {
            str = str + "# Relationships: \n";
            str = str + "# - phenotypic : Phenotypic Similarity (Human Phenotype Ontology) \n";
            
            if(NetworkUtils.isBipartiteGeneDiseases(querytype))
            {
                str = str + "# - omim: Gene-Disease relationship provided by OMIM\n";
                str = str + "# gene  disease  score   type \n";
            }
            else if (NetworkUtils.isBipartiteGeneOrphan(querytype))
            {
                str = str = "# - orphadata: Gene-Disease relationship provided by Orphanet (Orphadata)\n";
                str = str + "# gene  disease  score   type\n";
            }
        }
        
        return str;
    }
    
    
    /**
     * Create a String containing the header of the output file. The type of 
     * the relationships included depends on the type of query executed and
     * the type of network generated.
     * 
     * @param querytype
     * @return 
     */
    
    public static String getNetworkHeader(int relationshiptype, int querytype)
    {
    
        String str = "";
        
        
        if (NetworkUtils.isUnipartiteGenes(querytype) || querytype == NetworkConstants.QUERY_GENE_NETWORK)
        {
            switch (relationshiptype)
            {
                case NetworkConstants.GENEGENE_REL_PHENOTYPIC_POS:       str = "#Gene Symbol\tGene Symbol\tphenotypic\tisnew";
                                                                         break;
                case NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_BP_POS: str = "#Gene Symbol\tGene Symbol\tgobp";
                                                                         break;
                case NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_CC_POS: str = "#Gene Symbol\tGene Symbol\tgocc";
                                                                         break;
                case NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_MF_POS: str = "#Gene Symbol\tGene Symbol\tgomf";
                                                                         break;
                case NetworkConstants.GENEGENE_REL_INFERRED_OMIM_POS:    str = "#Gene Symbol\tGene Symbol\tinfer_omim";
                                                                         break;
                case NetworkConstants.GENEGENE_REL_INFERRED_ORPHA_POS:   str = "#Gene Symbol\tGene Symbol\tinfer_orpha";
                                                                         break;
                case NetworkConstants.GENEGENE_REL_METABOLIC_POS:        str = "#Gene Symbol\tGene Symbol";
                                                                         break;
                case NetworkConstants.GENEGENE_REL_PPI_POS:              str = "#Gene Symbol\tGene Symbol\tppi";
                                                                         break;
                case NetworkConstants.QUERYOBJECT_REL_PHENOTYPIC_POS:    str = "#Query\tGene Symbol\tphenotypic\tpvalue";
                                                                         break;                      
            }
        }
        else if (NetworkUtils.isUnipartiteDiseases(querytype) || querytype == NetworkConstants.QUERY_DISEASE_NETWORK)
        {
            switch (relationshiptype)
            {
                case NetworkConstants.OMIMOMIM_REL_PHENOTYPIC_POS:       str = "#OMIM\tOMIM\tphenotypic\tisnew";
                                                                         break;
                case NetworkConstants.OMIMOMIM_REL_INFERRED_GENE_POS:    str = "#OMIM\tOMIM\tinfer_gene";
                                                                         break;
                case NetworkConstants.QUERYOBJECT_REL_PHENOTYPIC_POS:    str = "#Query\tOMIM\tphenotypic\tpvalue";
                                                                         break;                    
            }
        }
        else if (NetworkUtils.isUnipartiteOrphan(querytype) || querytype == NetworkConstants.QUERY_ER_NETWORK)
        {
            switch (relationshiptype)
            {
                case NetworkConstants.ORPHAORPHA_REL_PHENOTYPIC_POS:     str = "#Orphan Disease\tOrphan Disease\tphenotypic\tisnew";
                                                                         break;
                case NetworkConstants.ORPHAORPHA_REL_INFERRED_GENE_POS:  str = "#Orphan Disease\tOrphan Disease\tinfer_gene";
                                                                         break;
                case NetworkConstants.QUERYOBJECT_REL_PHENOTYPIC_POS:    str = "#Query\tOrphan Disease\tphenotypic\tpvalue";
                                                                         break;
            }
        }
        else if (NetworkUtils.isBipartiteGeneDiseases(querytype))
        {   
            switch (relationshiptype)
            {
                case NetworkConstants.GENEOMIM_REL_PHENOTYPIC_GENE_POS:  str = "#Gene Symbol\tOMIM\tphenotypic\tisnew";
                                                                         break;
                case NetworkConstants.GENEOMIM_REL_PHENOTYPIC_OMIM_POS:  str = "#Gene Symbol\tOMIM\tphenotypic\tisnew";
                                                                         break;
                case NetworkConstants.GENEOMIM_REL_KNOWN_POS:            str = "#Gene Symbol\tOMIM\tknown(OMIM)";
                                                                         break;
            }
        }
        else if (NetworkUtils.isBipartiteGeneOrphan(querytype))
        {   
            switch (relationshiptype)
            {
                case NetworkConstants.GENEORPHA_REL_PHENOTYPIC_GENE_POS:  str = "#Gene Symbol\tOrphan Disease\tphenotypic\tisnew";
                                                                         break;
                case NetworkConstants.GENEORPHA_REL_PHENOTYPIC_ORPHA_POS:  str = "#Gene Symbol\tOrphan Disease\tphenotypic\tisnew";
                                                                         break;
                case NetworkConstants.GENEORPHA_REL_KNOWN_POS:            str = "#Gene Symbol\tOrphan Disease\tknown(Orphanet)";
                                                                         break;
            }
        }
       

        return str;
    }
    
    /**
     * Output files:
     * 
     * - Uniparite Gene Networks or Phenotype Queries (Genes)
     * - Unipartite Disease Networks or Phenotype Queries: OMIM and Rare Diseases
     * - Bipartite Networks
     * 
     * 
     * @param network 
     */
    public static void createNetworkZip(NetworkDB network)
    {
        Phenuma p = Phenuma.getInstance();
        
        
        String dirname = Constants.TEMP+"/"+network.getName()+"/";
        File networkFile = new File(dirname);
        
        FileUtils.createDirectory(networkFile);
        
        if(NetworkUtils.isUnipartiteGenes(network.getQuerytype()) || network.getQuerytype() == NetworkConstants.QUERY_GENE_NETWORK)
        {
            createUnipartiteGeneNetworkFiles(dirname, network);
        }
        else if(NetworkUtils.isUnipartiteDiseases(network.getQuerytype()) || NetworkUtils.isUnipartiteOrphan(network.getQuerytype()) || 
                network.getQuerytype() == NetworkConstants.QUERY_DISEASE_NETWORK || network.getQuerytype() == NetworkConstants.QUERY_ER_NETWORK)
        {
            createUnipartiteDiseaseNetworkFiles(dirname, network);
        }
        else if (NetworkUtils.isBipartite(network.getQuerytype()) || network.getQuerytype() == NetworkConstants.QUERY_ER_NETWORK)
        {
            createBipartiteGeneDiseaseNetworkFiles(dirname, network);
        }
        
        FileUtils.createZipFromDir(networkFile);
       // FileUtils.deleteDirectory(networkFile);
        
    }
    
    
    /**
     * Create uniparite gene network files: this method creates a file for each type of relationships.
     * 
     * The input network must be a NetworkDB object.This object has implemented the method getRows() that returns a
     * List<Object[]>, created with the data extrated from the database after execute the query. See the database
     * structure to check the information of this List.
     * 
     * 
     * @param dirname : target directory
     * @param network : input network (genes network) retrieved from the database
     */
    
   
    public static void createUnipartiteGeneNetworkFiles(String dirname, NetworkDB network)
    {
         try 
         {
            PrintWriter out_network     = new PrintWriter(new FileWriter(dirname+"complete_network.txt")); 
            PrintWriter out_phenotypic  = new PrintWriter(new FileWriter(dirname+"phenotypic.txt"));
            PrintWriter out_gobp        = new PrintWriter(new FileWriter(dirname+"gobp.txt"));
            PrintWriter out_gocc        = new PrintWriter(new FileWriter(dirname+"gocc.txt"));
            PrintWriter out_gomf        = new PrintWriter(new FileWriter(dirname+"gomf.txt"));
            PrintWriter out_inferomim   = new PrintWriter(new FileWriter(dirname+"inferomim.txt"));
            PrintWriter out_inerorpha   = new PrintWriter(new FileWriter(dirname+"inferorpha.txt"));
            PrintWriter out_ppi         = new PrintWriter(new FileWriter(dirname+"ppi.txt"));
            PrintWriter out_metabolic   = new PrintWriter(new FileWriter(dirname+"metabolic.txt"));

            //Phenotype Query
            PrintWriter out_query =  null;
            
            
            if(NetworkUtils.isPhenotypeQuery(network.getQuerytype()))
            {
                out_query = new PrintWriter(new FileWriter(dirname+"query.txt"));
                out_query.println(getNetworkHeader(NetworkConstants.QUERYOBJECT_REL_PHENOTYPIC_POS, network.getQuerytype()));
            }
            
            
            out_phenotypic.println(getNetworkHeader(NetworkConstants.GENEGENE_REL_PHENOTYPIC_POS, network.getQuerytype()));
            out_gobp.println(getNetworkHeader(NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_BP_POS, network.getQuerytype()));
            out_gocc.println(getNetworkHeader(NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_CC_POS, network.getQuerytype()));
            out_gomf.println(getNetworkHeader(NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_MF_POS, network.getQuerytype()));
            out_inferomim.println(getNetworkHeader(NetworkConstants.GENEGENE_REL_INFERRED_OMIM_POS, network.getQuerytype()));
            out_inerorpha.println(getNetworkHeader(NetworkConstants.GENEGENE_REL_INFERRED_ORPHA_POS, network.getQuerytype()));
            out_ppi.println(getNetworkHeader(NetworkConstants.GENEGENE_REL_PPI_POS, network.getQuerytype()));
            out_metabolic.println(getNetworkHeader(NetworkConstants.GENEGENE_REL_METABOLIC_POS, network.getQuerytype()));
            
            

            for (Object[] row : network.getRows())
            {
                
                String objectX = (String)row[NetworkConstants.GENEGENE_REL_GENEX_SYMBOL_POS];
                String objectY = (String)row[NetworkConstants.GENEGENE_REL_GENEY_SYMBOL_POS];
                
                String rel = objectX+"\t"+objectY;

                for(int i = NetworkUtils.getPositionFirstRelationship(network.getQuerytype()); i <= NetworkUtils.getPositionLastRelationship(network.getQuerytype()); i++)
                {
                    String value = NumberUtils.object2string(row[i]);                  

                    if (i == NetworkConstants.GENEGENE_REL_METABOLIC_POS || (!value.isEmpty() && !value.equals("0") && !value.equals("0.0")))
                    {
                        Double double_value = 0.0;
                        if(i != NetworkConstants.GENEGENE_REL_METABOLIC_POS)
                        {
                            double_value = NumberUtils.round(new Double(value), 3);
                        }

                        switch (i)
                        {
                            case NetworkConstants.GENEGENE_REL_PHENOTYPIC_POS:          out_phenotypic.print(rel+"\t"+double_value+"\t"); 
                                                                                        if(isNewRelationship(row, network.getQuerytype()))
                                                                                            out_phenotypic.println("yes");
                                                                                        else
                                                                                            out_phenotypic.println("no");
                                                                                                  
                                                                                            
                                                                                        break;
                            case NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_BP_POS:    out_gobp.println(rel+"\t"+double_value); 
                                                                                        break;
                            case NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_CC_POS:    out_gocc.println(rel+"\t"+double_value); 
                                                                                        break;
                            case NetworkConstants.GENEGENE_REL_FUNCTIONAL_GO_MF_POS:    out_gomf.println(rel+"\t"+double_value); 
                                                                                        break;
                            case NetworkConstants.GENEGENE_REL_INFERRED_OMIM_POS:       out_inferomim.println(rel+"\t"+double_value*10); 
                                                                                        break;
                            case NetworkConstants.GENEGENE_REL_INFERRED_ORPHA_POS:      out_inerorpha.println(rel+"\t"+double_value*10); 
                                                                                        break;
                            case NetworkConstants.GENEGENE_REL_METABOLIC_POS:           out_metabolic.println(rel); 
                                                                                        break;
                            case NetworkConstants.GENEGENE_REL_PPI_POS:                 out_ppi.println(rel+"\t"+double_value); 
                                                                                        break;                                    
                        }

                    }

                }
             }
            
            

            //Phenotype Query
            if(NetworkUtils.isPhenotypeQuery(network.getQuerytype()))
            {
                PhenotypeNetworkDB phenotypenetwork = (PhenotypeNetworkDB)network;
                
                out_network.println(phenotypenetwork.toString());
                
                out_query.println(phenotypenetwork.toStringQuery());
                out_query.close();
            }
            else
            {
                out_network.println(network.toString());
            }
            
            
            out_network.close();
            out_phenotypic.close();
            out_gobp.close();
            out_gocc.close();
            out_gomf.close();
            out_inferomim.close();
            out_inerorpha.close();
            out_ppi.close();
            out_metabolic.close();
     
            
            

            
           
        } catch (IOException ex) {
            Logger.getLogger(NetworkUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    
    
    /**
     * Create uniparite gene network files: this method creates a file for each type of relationships.
     * 
     * This method takes into account the resulting networks from phenotype queries.
     * 
     * @param network 
     */
    public static void createUnipartiteDiseaseNetworkFiles(String dirname, NetworkDB network)
    {
        try 
         {
            PrintWriter out_network = new PrintWriter(new FileWriter(dirname+"complete_network.txt")); 
            PrintWriter out_phenotypic = new PrintWriter(new FileWriter(dirname+"phenotypic.txt"));
            PrintWriter out_infergene = new PrintWriter(new FileWriter(dirname+"infergene.txt"));

            //Phenotype Query
            PrintWriter out_query =  null;
            
            
            if(NetworkUtils.isPhenotypeQuery(network.getQuerytype()))
            {
                out_query = new PrintWriter(new FileWriter(dirname+"query.txt"));
                out_query.println(getNetworkHeader(NetworkConstants.QUERYOBJECT_REL_PHENOTYPIC_POS, network.getQuerytype()));
            }
            
            //Add header
            
            out_phenotypic.println(getNetworkHeader(NetworkConstants.OMIMOMIM_REL_PHENOTYPIC_POS, network.getQuerytype()));
            out_infergene.println(getNetworkHeader(NetworkConstants.OMIMOMIM_REL_INFERRED_GENE_POS, network.getQuerytype()));
            

            for (Object[] row : network.getRows())
            {
                Integer objectX = (Integer)row[NetworkConstants.DATABASE_MATRIX_OBJECTX_POS];
                Integer objectY = (Integer)row[NetworkConstants.DATABASE_MATRIX_OBJECTY_POS];
                
                String rel = objectX+"\t"+objectY;

                
                
                for(int i = NetworkUtils.getPositionFirstRelationship(network.getQuerytype()); i <= NetworkUtils.getPositionLastRelationship(network.getQuerytype()); i++)
                {
                    String value = NumberUtils.object2string(row[i]);                  

                    if (!value.isEmpty() && !value.equals("0") && !value.equals("0.0") )
                    {
                        Double double_value = NumberUtils.round(new Double(value), 3);


                        switch (i)
                        {
                            case NetworkConstants.OMIMOMIM_REL_PHENOTYPIC_POS:      out_phenotypic.print(rel+"\t"+double_value+"\t"); 
                                                                                    if(isNewRelationship(row, network.getQuerytype()))
                                                                                        out_phenotypic.println("yes");
                                                                                    else
                                                                                        out_phenotypic.println("no");
                                                                                    break;
                                
                            case NetworkConstants.OMIMOMIM_REL_INFERRED_GENE_POS:   out_infergene.println(rel+"\t"+double_value*10); 
                                                                                    break;
                        }

                    }

                }
             }

            //Phenotype Query
            if(NetworkUtils.isPhenotypeQuery(network.getQuerytype()))
            {
                PhenotypeNetworkDB phenotypenetwork = (PhenotypeNetworkDB)network;
                out_network.println(phenotypenetwork.toString());
                
                for (Object[] row : phenotypenetwork.getResultRelationships())
                {
                    out_query.println(row[0] + "\t" + row[1] + "\t" + NumberUtils.object2string(row[2]) + "\t" +  NumberUtils.object2string(row[3]));
                }
                
    
                out_query.close();
            }
            else
            {
                out_network.println(network.toString());
            }
            
            out_network.close();
            out_phenotypic.close();
            out_infergene.close();
            
                
        } catch (IOException ex) {
            Logger.getLogger(NetworkUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create uniparite gene network files: this method creates a file for each type of relationships.
     * 
     * @param network 
     */
    public static void createBipartiteGeneDiseaseNetworkFiles(String dirname, NetworkDB network)
    {
         try 
         {
            PrintWriter out_network = new PrintWriter(new FileWriter(dirname+"complete_network.txt")); 
            PrintWriter out_phenotypic = new PrintWriter(new FileWriter(dirname+"phenotypic.txt"));
            PrintWriter out_known = new PrintWriter(new FileWriter(dirname+"known.txt"));

            
            if(network.getInputtype() == NetworkConstants.INPUT_TYPE_OMIM)
            {
                out_phenotypic.println(getNetworkHeader(NetworkConstants.GENEOMIM_REL_PHENOTYPIC_OMIM_POS, network.getQuerytype()));
                out_known.println(getNetworkHeader(NetworkConstants.GENEOMIM_REL_KNOWN_POS, network.getQuerytype()));            
            }
            else if(network.getInputtype() == NetworkConstants.INPUT_TYPE_GENES)
            {
                out_phenotypic.println(getNetworkHeader(NetworkConstants.GENEOMIM_REL_PHENOTYPIC_GENE_POS, network.getQuerytype()));
                out_known.println(getNetworkHeader(NetworkConstants.GENEOMIM_REL_KNOWN_POS, network.getQuerytype()));
            }     
            else
            {
                out_phenotypic.println(getNetworkHeader(NetworkConstants.GENEORPHA_REL_PHENOTYPIC_ORPHA_POS, network.getQuerytype()));
                out_known.println(getNetworkHeader(NetworkConstants.GENEORPHA_REL_KNOWN_POS, network.getQuerytype()));
            }
            
            
            
            for (Object[] row : network.getRows())
            {
                String rel = null;
                if(NetworkUtils.isBipartiteGeneDiseases(network.getQuerytype()))
                    rel = row[NetworkConstants.GENEOMIM_REL_GENE_SYMBOL_POS]+"\t"+row[NetworkConstants.DATABASE_MATRIX_OBJECTY_POS];
                else
                    rel = row[NetworkConstants.GENEORPHA_REL_GENE_SYMBOL_POS]+"\t"+row[NetworkConstants.DATABASE_MATRIX_OBJECTY_POS];
                
                for(int i = NetworkUtils.getPositionFirstRelationship(network.getQuerytype()); i <= NetworkUtils.getPositionLastRelationship(network.getQuerytype()); i++)
                {
                    String value = NumberUtils.object2string(row[i]);                  

                    if (!value.isEmpty() && !value.equals("0") && !value.equals("0.0") )
                    {
                        Double double_value = NumberUtils.round(new Double(value), 3);

                        
                        if(network.getInputtype() == NetworkConstants.INPUT_TYPE_OMIM)
                        {
                            
                            switch (i)
                            {
                                case NetworkConstants.GENEOMIM_REL_PHENOTYPIC_OMIM_POS: out_phenotypic.print(rel+"\t"+double_value+"\t"); 
                                                                                        if(isNewRelationship(row, network.getQuerytype()))
                                                                                            out_phenotypic.println("yes");
                                                                                        else
                                                                                            out_phenotypic.println("no");
                                                                                                  
                                                                                            
                                                                                        break;
                                    
                                case NetworkConstants.GENEOMIM_REL_KNOWN_POS :          out_known.println(rel+"\t"+double_value); 
                                                                                        break;                            
                            }
                        }
                        else
                        {
                            switch (i)
                            {
                                case NetworkConstants.GENEOMIM_REL_PHENOTYPIC_GENE_POS: out_phenotypic.print(rel+"\t"+double_value+"\t"); 
                                                                                        if(isNewRelationship(row, network.getQuerytype()))
                                                                                            out_phenotypic.println("yes");
                                                                                        else
                                                                                            out_phenotypic.println("no");
                                                                                                  
                                                                                            
                                                                                        break;
                                case NetworkConstants.GENEOMIM_REL_KNOWN_POS :          out_known.println(rel+"\t"+double_value); 
                                                                                        break;                            
                            }

                        }

                    }

                }
             }
            
            out_network.println(network.toString());
            
            out_network.close();
            out_phenotypic.close();
            out_known.close();
                
        } catch (IOException ex) {
            Logger.getLogger(NetworkUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    
    /**
     * This method creates a set of node from a list of objects (Genes, Diseases or RareDiseases)
     * @param list
     * @return 
     */
    public static Set<Node> listObject2nodeSet(List list){
        
        Set<Node> res = new HashSet<Node>();
        
        for(Object o : list)
        {
            Node node = null;
            if(o instanceof Gene){
                node = new NodeGene((Gene)o);
            }else if(o instanceof Disease){
                node = new NodeDisease((Disease)o);
            }else if(o instanceof RareDisease){
                node = new NodeRareDisease((RareDisease)o);
            }
            
            if(node!=null)
                res.add(node);
            
        } 
        return res;
        
    }
    
    /**
     * This method creates a set of node from a list of objects (Genes, Diseases or RareDiseases)
     * @param list
     * @return 
     */
    public static List set2list(Set set){
        
        List res = new ArrayList();
        
        for(Object o : set)
        {
            res.add(o);

        } 
        return res;

    }
    
    
    /**
     * Get disease study set from a genes study set.
     * 
     * @param genes
     * @return 
     */
    
    public static Map<Integer,List<Integer>> diseaseStudyFromGenes(StudySet genes) 
    {
        Map<Integer, List<Integer>> disease2gene = new HashMap<Integer, List<Integer>>();
        
        Iterator<ByteString> iter = genes.iterator();
        
        while(iter.hasNext())
        {
            ByteString gene = iter.next();

            List<Disease> omimlist = new DatabaseQueries().getDiseasesByGene(gene.toString());

            for(Disease omim : omimlist)
            {
                
                List<Integer> genelist_omim = disease2gene.get(new Integer(gene.toString()));
                
                if(genelist_omim == null)
                    genelist_omim = new ArrayList<Integer>();
                
                genelist_omim.add(new Integer(gene.toString()));
                
                disease2gene.put(omim.getOmim(), genelist_omim);
            }
        }
        
       
        
        return disease2gene;
    }
    
    
    /**
     * Get rare disease study set from a gene study set.
     * 
     * @param genes
     * @return 
     */
    
    public static Map<Integer,List<Integer>> rareDiseaseStudyFromGenes(StudySet genes)
    {
        Map<Integer, List<Integer>> orphan2gene = new HashMap<Integer, List<Integer>>();
        
        Iterator<ByteString> iter = genes.iterator();
        
        while(iter.hasNext())
        {
            ByteString gene = iter.next();

            List<RareDisease> orphanlist = new DatabaseQueries().getRareDiseasesByGene(gene.toString());

            for(RareDisease orphan : orphanlist)
            {
                
                List<Integer> genelist_orphan = orphan2gene.get(new Integer(gene.toString()));
                
                if(genelist_orphan == null)
                    genelist_orphan = new ArrayList<Integer>();
                
                genelist_orphan.add(new Integer(gene.toString()));
                
                orphan2gene.put(orphan.getOrphanum(), genelist_orphan);
            }
        }
        
       
        
        return orphan2gene;
    }
    
    
     
    /**
     * Get gene study set from a diseases study set. The information
     * stored in the database has been extrated of morpbidmap file of omim database.
     * 
     * @param diseases
     * @return 
     */
    
    public static Map<Integer,List<Integer>> geneStudyFromOmim(StudySet omimset) 
    {
        Map<Integer, List<Integer>> gene2omim = new HashMap<Integer, List<Integer>>();
        
        Iterator<ByteString> iter = omimset.iterator();
        
        while(iter.hasNext())
        {
            ByteString currentomim = iter.next();

            List<Gene> genelist_currentomim = new DatabaseQueries().getGenesByDisease(currentomim.toString());

            for(Gene gene : genelist_currentomim)
            {
                
                List<Integer> genelist_omim = gene2omim.get(new Integer(currentomim.toString()));
                
                if(genelist_omim == null)
                    genelist_omim = new ArrayList<Integer>();
                
                genelist_omim.add(new Integer(currentomim.toString()));
                
                gene2omim.put(gene.getEntrezid(), genelist_omim);
            }
        }
        
       
        
        return gene2omim;
    }
   
    /**
     * Get gene study set from a diseases study set. The information
     * stored in the database has been extrated of morpbidmap file of omim database.
     * 
     * @param diseases
     * @return 
     */
    
    public static Map<Integer,List<Integer>> geneStudyFromOrphanum(StudySet orphanumSet){

        Map<Integer, List<Integer>> gene2orphan = new HashMap<Integer, List<Integer>>();
        
        Iterator<ByteString> iter = orphanumSet.iterator();
        
        while(iter.hasNext())
        {
            ByteString currentorphanum = iter.next();

            List<Gene> genelist_currentorphanum = new DatabaseQueries().getGenesByRareDisease(currentorphanum.toString());
            
            for(Gene gene : genelist_currentorphanum){

                List<Integer> genelist_orphan = gene2orphan.get(new Integer(currentorphanum.toString()));
                
                if(genelist_orphan == null)
                    genelist_orphan = new ArrayList<Integer>();

                genelist_orphan.add(new Integer(currentorphanum.toString()));

                gene2orphan.put(gene.getEntrezid(), genelist_orphan);
            }

        }

        return gene2orphan;
    }  
    
     /**
     * This method check if the input row is a new relationships, that is, there is phenotypic
     * relationship between the genes or disease but there is not other kind of relationship. (Only for unipartite networks)
     * 
     * @param row
     * @return 
     */
    public static boolean isNewRelationship(Object[] row, int querytype)
    {
        boolean isnew = false;
        
        if(!NetworkUtils.isBipartite(querytype))
        {        
            
            
            if (NetworkUtils.isUnipartiteGenes(querytype))
            {
                Object phenotypic = row[NetworkConstants.GENEGENE_REL_PHENOTYPIC_POS];
                Object inferred_omim = row[NetworkConstants.GENEGENE_REL_INFERRED_OMIM_POS];
                Object inferred_orpha = row[NetworkConstants.GENEGENE_REL_INFERRED_ORPHA_POS];
                
                isnew = phenotypic!=null && (inferred_omim==null && inferred_orpha==null);

            }
            else if (NetworkUtils.isUnipartiteDiseases(querytype))
            {
                Object phenotypic = row[NetworkConstants.OMIMOMIM_REL_PHENOTYPIC_POS];
                Object inferred_gene = row[NetworkConstants.OMIMOMIM_REL_INFERRED_GENE_POS];
                
                isnew = phenotypic!=null && inferred_gene==null;
            }
            else if (NetworkUtils.isUnipartiteOrphan(querytype))
            {
                Object phenotypic = row[NetworkConstants.ORPHAORPHA_REL_PHENOTYPIC_POS];
                Object inferred_gene = row[NetworkConstants.ORPHAORPHA_REL_INFERRED_GENE_POS];
                
                isnew = phenotypic!=null && inferred_gene==null;
                
            }
            
        }

        
        
        return isnew;
    }
}
