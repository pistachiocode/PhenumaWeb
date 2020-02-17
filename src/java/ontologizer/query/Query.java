package ontologizer.query;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;


import ontologizer.association.AssociationContainer;
import ontologizer.association.PP2Associations;
import ontologizer.calculation.SemanticCalculation;
import ontologizer.go.OBOParserException;
import ontologizer.go.Ontology;
import ontologizer.go.TermID;
import ontologizer.types.ByteString;
import phenomizer.database.DBPValue;
import phenomizer.hpo.Phenomizer;
import phenomizer.hpo.Phenuma;

public class Query {
	
	//private PhenomizerResultList result;
	
	private int querySize;
        private int ontologyID;
        
	private ArrayList<TermID> termList;
	
        private Phenuma phenomizer = Phenuma.getInstance();
        
	/**
	 * 
	 * @param queryStr
	 * @throws IOException
	 * @throws OBOParserException
	 */
	
	public Query(String queryStr, int ontologyID) throws IOException, OBOParserException
	{		
		String[] hpoterms = queryStr.split("[\t \n \b]");		
		ArrayList<String> list = new ArrayList<String>();
		
		int i = hpoterms.length-1;
		while(i>=0){
			String hpoterm = hpoterms[i];
			list.add(hpoterm);
			i--;
		}
		

                Ontology ontology = phenomizer.getOntologyById(ontologyID);
                
		querySize = list.size();
		
		//build termID list
		termList = new ArrayList<TermID>();
		for(String t : list){
			termList.add(ontology.getTerm(t).getID());
		}
                
                

	}
	
	/**
	 * 
	 * @param query
	 * @throws IOException
	 * @throws OBOParserException
	 */
	
	public Query(File query, int ontologyID) throws IOException, OBOParserException
	{		
		//read query
		ArrayList<String> list = readTermList(query);
		
                Ontology ontology = phenomizer.getOntologyById(ontologyID);
                
		//query size
		querySize = list.size();
		
		termList = new ArrayList<TermID>();
		for(String t : list){
			termList.add(ontology.getTerm(t).getID());
		}
                

	}
        
        /**
         * 
         * @param ppname 
         */
        public Query(ArrayList<TermID> termList){
            
            this.termList = termList;
            
        }
        
	
	/**
	 * Execute a query of terms
	 * 
	 * @return PhenomizerResultList
	 * @throws OBOParserException 
	 * @throws IOException 
	 */
	public PhenomizerResultList execute() throws IOException, OBOParserException{
            /** Create new object PhenomizerResultList*/    
            PhenomizerResultList result = new PhenomizerResultList();
            
            /** Get Phenomizer instance with the current ontology and annotation information*/
            Phenomizer phenomizer = Phenomizer.getInstance();
            
            Ontology ontology = phenomizer.getOntology();
            AssociationContainer assocs = phenomizer.getAssocContainer();

            /**Create semantic calculation object */
            SemanticCalculation semanticCalculation = new SemanticCalculation(ontology, assocs);
            
            /**For each disease annotated */
            Set<ByteString> diseases = phenomizer.getAssocContainer().getAllAnnotatedPP();

            for(ByteString d : diseases)
            {
                /**Calculation of semantic similarity between the disease an the termlist*/
                PP2Associations g2a = phenomizer.getAssocContainer().get(d);
                double sim = semanticCalculation.simAvgSym(termList, d);

                PhenomizerResult pr = new PhenomizerResult(sim, g2a.id(), g2a.name());
                result.add(pr);
                
                /** Get the pvalue associated to the result (sim)*/
                if (sim>0) {	
                //    PhenomizerResult pr = new PhenomizerResult(sim, g2a.id(), g2a.name());

                    
                    DBPValue pvalue = new DBPValue(d.toString(), querySize, sim);
                   
                    pr.setPvalue(pvalue.getPvalue());

                    //adjust pvalue
                    double adjust;

                    if (phenomizer.getAdjust_type() == Phenomizer.BONFERRONI)
                        adjust = pvalue.getBonferroni_adjust();
                    else
                        adjust = pvalue.getBenjamini_adjust();


                    if (adjust <= 0)
                        adjust = 1;

                    pr.setAdjust_pvalue(adjust);

                    result.add(pr);
                }
            }

            return result;
	}
	
	/**
         * Read list of HP terms from a file. 
         * 
         * @param query
         * @return 
         */

	private ArrayList<String> readTermList(File query){
	    FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    DataInputStream dis = null;

	    ArrayList<String> tl = new ArrayList<String>();
	    
	    try {
	      fis = new FileInputStream(query);

	      // Here BufferedInputStream is added for fast reading.
	      bis = new BufferedInputStream(fis);
	      dis = new DataInputStream(bis);

	      // dis.available() returns 0 if the file does not have more lines.
	      while (dis.available() != 0) 
	        tl.add(dis.readLine());	        
	      

	      // dispose all the resources after using them.
	      fis.close();
	      bis.close();
	      dis.close();

	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    
	    return tl;
	 }

	

	public int getQuerySize() {
		return querySize;
	}



	public void setQuerySize(int querySize) {
		this.querySize = querySize;
	}
	
	
}
