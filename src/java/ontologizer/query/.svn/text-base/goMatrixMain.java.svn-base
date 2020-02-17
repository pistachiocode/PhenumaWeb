/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontologizer.query;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import ontologizer.association.AssociationContainer;
import ontologizer.calculation.SemanticCalculation;
import ontologizer.go.OBOParserException;
import ontologizer.go.Ontology;
import ontologizer.types.ByteString;
import phenomizer.hpo.Phenuma;

/**
 * NOTA: para las matrices almacenadas en GO no se ha usado este código.!! 
 * @author Armando
 */
public class goMatrixMain {
    
    
    
    
        public static void goMatrix(Ontology ontology, String annotationFile, String outputFile) throws IOException
        {
            Phenuma p = Phenuma.getInstance();
            
            
            AssociationContainer assoc = p.parseAssociations(annotationFile, ontology,true);
            
            ByteString[] strArray = new ByteString[assoc.getAllAnnotatedPP().size()];	
		
            //Create genes id array
            Iterator<ByteString> iter = assoc.getAllAnnotatedPP().iterator();
            int i = 0;
            while(iter.hasNext()){
                    strArray[i] = iter.next();	           
                    i++;
            }

            //Generate the matrix
            SemanticCalculation sc = new SemanticCalculation(ontology, assoc);
            
            
            PrintWriter out = new PrintWriter(new File(outputFile));
            
            for(int j = 0; j<strArray.length; j++)
            {
                for(int k = j+1; k<strArray.length; k++)
                {
                    double d = sc.resnikSimilarity(strArray[j], strArray[k]);
                    if(d>0)
                        out.println(strArray[j]+"\t"+strArray[k]+"\t"+d);
                }
                
                System.out.println(j+" de "+strArray.length);
            }
            
            out.close();  
        }
    
    	/**    
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, OBOParserException {    
            
            Phenuma p = Phenuma.getInstance();
            
            String annotationFile = "./resources/in/annotations/gene_associations_ensembl_go.txt";
            
            goMatrix(p.getGene_ontology_bp(),annotationFile, "./resources/out/matrix/gene_matrix_ensembl_go_bp.txt");
            goMatrix(p.getGene_ontology_mf(),annotationFile, "./resources/out/matrix/gene_matrix_ensembl_go_mf.txt");
            goMatrix(p.getGene_ontology_cc(),annotationFile, "./resources/out/matrix/gene_matrix_ensembl_go_cc.txt");
            
        }
}
