package phenomizer.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import ontologizer.association.AssociationContainer;
import ontologizer.go.OBOParserException;
import ontologizer.query.PhenomizerResult;
import ontologizer.query.PhenomizerResultList;
import ontologizer.query.Query;
import ontologizer.types.ByteString;
import phenomizer.hpo.HPOUtils;
import phenomizer.hpo.Phenomizer;
import phenuma.constants.Constants;
import phenuma.network.Network;

public class PhenomizerListener implements ActionListener {
	
	protected PhenomizerWindow window;
	protected PhenomizerResultList pr = null;
	protected Network sr = null;
	public PhenomizerListener(PhenomizerWindow window){
		this.window = window;
	}
	
	
	
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();

		
		if (window.phenomizer.dataLoaded()){
			window.hideErrorMessage();

		
			//Get diagnosis
			if (command.equalsIgnoreCase(PhenomizerWindow.DIAGNOSIS_COMMAND))
			{	
				getDiagnosisAction();
			}
			else if (command.equalsIgnoreCase(PhenomizerWindow.SAVE_COMMAND))
			{
				saveAction();
	
			}
			else if (command.equalsIgnoreCase(PhenomizerWindow.COLUMNS_COMMAND)){
				if (sr!=null){
					String[][] data = this.floatToStringTable(sr);
					
					window.addTable(PhenomizerWindow.HEADERS_PPTABLE, data);
				}
				
			}
			else if (command.equalsIgnoreCase(PhenomizerWindow.TABLE_COMMAND)){
				if (sr!=null){
					//get semantic result
					String query = window.getContent(PhenomizerWindow.QUERY_TEXTAREA);
					String[] study = this.getStudy(query);
					
					String[][] data = this.floatToString(sr);
					window.addMatrix(study, data);
				}
				
			}
			else if (command.equalsIgnoreCase(PhenomizerWindow.CLEAR_COMMAND))
			{
				window.setContent(PhenomizerWindow.QUERY_TEXTAREA, "");
			}
			else if (command.equalsIgnoreCase(PhenomizerWindow.ALL_COMMAND))
			{
				getAllAnnotatedAction();
			}
			else if (command.equalsIgnoreCase(PhenomizerWindow.ADJUST_COMMAND))
			{
				setAdjusment();
			}
				
		}else{
			window.errorMessage("You must to provide an ontology (.obo file) and at least an annotation file.");
		}
		
		
		if (command.equalsIgnoreCase(PhenomizerWindow.LOAD_DISEASES_COMMAND) || command.equalsIgnoreCase(PhenomizerWindow.LOAD_GENES_COMMAND) || command.equalsIgnoreCase(PhenomizerWindow.LOAD_ONTO_COMMAND))
		{
			loadAction(command); 

		}

	

	}

	/* Actions */

	/**
	 * @param command
	 */
	private void loadAction(String command) {
		String file = Utils.loadFile(window, "Save result as ...", new File("..").getAbsolutePath(), "*.txt");
		
		try {
			if (command.equalsIgnoreCase(PhenomizerWindow.LOAD_DISEASES_COMMAND))
				window.phenomizer.setAnnotationsPath(file,true);
//			else if (command.equalsIgnoreCase(PhenomizerWindow.LOAD_GENES_COMMAND))
//				window.phenomizer.setGenesAnnotationsPath(file);
			else if (command.equalsIgnoreCase(PhenomizerWindow.LOAD_ONTO_COMMAND))
				window.phenomizer.setOntologyPath(file, phenuma.constants.Constants.SUBONTO_PHENOTYPIC_ABNORMALITY);
			
			window.setContent(command, file);
			
		} catch (IOException e) {
			window.errorMessage(e.getMessage());
		} catch (OBOParserException e){
			window.errorMessage(e.getMessage());
		}
		
		if(window.phenomizer.dataLoaded())
			window.hideErrorMessage();
	}

	/**
	 * save result
	 */
	private void saveAction() {
		if (sr == null && pr == null)
			window.infoMessage("Nothing to save.");
		else
		{
			String file = Utils.saveFile(window, "Save result as ...", new File("..").getAbsolutePath(), "*.txt");
			
			if (file!=null){
				if (pr!=null)
					pr.writeToFile(new File(file));
				else if (sr!=null)
					//sr.writeColumns(new File(file));
 
				window.infoMessage("Result saved in "+file);
			}	
		}
	}

	/**
	 * get adjustment
	 */
	private void setAdjusment() {
		//Set adjusment type
		if (window.selectedItem(PhenomizerWindow.ADJUST_COMMAND).equalsIgnoreCase(PhenomizerWindow.BENJAMINI_ADJUSTMENT))
			Phenomizer.getInstance().setAdjust_type(Phenomizer.BENJAMINI_HOCHBERG);
		else
			Phenomizer.getInstance().setAdjust_type(Phenomizer.BONFERRONI);
	}

	/**
	 * set all annotated items
	 */
	private void getAllAnnotatedAction() {
		
		boolean genesQuery = window.selectedItem(PhenomizerWindow.QUERY_COMMAND).equalsIgnoreCase(PhenomizerWindow.GENES_COMMAND);
		boolean diseasesQuery = window.selectedItem(PhenomizerWindow.QUERY_COMMAND).equalsIgnoreCase(PhenomizerWindow.DISEASES_COMMAND);
		
		
		if (genesQuery || diseasesQuery)
		{
			//Get assocation container
			AssociationContainer assocs = null;

			if (diseasesQuery && window.phenomizer.getAssocContainer()!=null)
				assocs = Phenomizer.getInstance().getAssocContainer();
//			else if (genesQuery && window.phenomizer.getAssocContainerGenes()!=null)
//				assocs = Phenomizer.getInstance().getAssocContainerGenes();				
			
			if (assocs != null){
				//convert set of bytestring to string
				String str = "";
				int i = 0;
				
				Iterator<ByteString> iter = assocs.getAllAnnotatedPP().iterator();
				while(iter.hasNext()){
					str = str+iter.next().toString()+ "\n";
					i++;
				}
				
				window.setContent(PhenomizerWindow.QUERY_TEXTAREA, str);
			}else
			{
				if (genesQuery)
					window.errorMessage("You must provide an annotation file of genes.");
				else if (diseasesQuery)
					window.errorMessage("You must provide an annotation file of diseases.");
			}
				
		}
	}

	/**
	 * get diagnosis
	 */
	private void getDiagnosisAction() {
		//Read query
		String query = window.getContent(PhenomizerWindow.QUERY_TEXTAREA);
		if (query==null || query.isEmpty())
			window.errorMessage(phenuma.constants.Constants.EMPTY_QUERY);
		else {
		
			//HPO QUERY
			if (window.selectedItem(PhenomizerWindow.QUERY_COMMAND).equalsIgnoreCase(PhenomizerWindow.HPO_COMMAND))
			{
				//init semantic result
				sr = null;
				
				//input validation
				if (!this.validateHPOQuery(this.getStudy(query)))
				{
					window.errorMessage(phenuma.constants.Constants.FORMAT_INPUT_ERROR);
				}
				else
				{
					//get phenomizer result
					Query q;
					try {
						q = new Query(window.getContent(PhenomizerWindow.QUERY_TEXTAREA), Constants.ASSOC_DISEASES_HPO);
						pr = q.execute();
					} catch (IOException e) {
						window.errorMessage(e.getMessage());
					} catch (OBOParserException e) {
						window.errorMessage(e.getMessage());
					} catch (Exception e){
						window.errorMessage(e.getMessage());
					}

			    
					//display result
					if(pr!=null)
						window.addTable(PhenomizerWindow.HEADERS_HPOTABLE, this.phenomizarResultListToString(pr));
					else
						window.infoMessage("There are no results for the query.");
				}

			}
			else 
			{
				//DISEASE OR GENES QUERY
				
				//initializer phenomizer result;
			/*	pr =  null;
				
				//get semantic result
				String[] study = this.getStudy(query);
				SimilarityMatrix matrix = null;
				
				try{
					if (window.selectedItem(PhenomizerWindow.QUERY_COMMAND).equalsIgnoreCase(PhenomizerWindow.GENES_COMMAND))
						matrix = new SimilarityMatrix(study, Phenomizer.GENES);
					else
						matrix = new SimilarityMatrix(study, Phenomizer.DISEASES);
					
				} 
				catch (IOException e) {
					window.errorMessage(e.getMessage());
				} catch (OBOParserException e) {
					window.errorMessage(e.getMessage());
				}
				
				sr = matrix.getMatrix();
				
				//display result
				String[][] data = this.floatToString(sr);
				
				
				window.addMatrix(study, data);*/
				
			}
			
			System.out.println("fin");
		}
	}
	
	
	
	
	/* Auxiliary methods */
	
	/**
	 * float matrix to string matrix
	 */
	private String[][] floatToString(Network sr){
//		float[][] matrix = sr.mat;
//		String[][] data = new String[matrix.length][matrix.length];
//		
//		for(int i = 0; i<matrix.length; i++){
//			for(int j = 0; j<matrix.length; j++){
//				
//				data[i][j] = new Double(matrix[i][j]).toString();
//			}
//		}
//		
//		return data;
            return null;
	}

	/**
	 * 
	 * @param matrix
	 * @param study
	 * @return
	 */
	private String[][] floatToStringTable(Network sr){
//		float[][] matrix = sr.mat;
//		String[] study = phenomizer.utils.StringUtils.byteStringToString(sr.names);
//		
//		String[][] data = new String[matrix.length*matrix.length][3];
//		
//		//display result
//		int k = 0;
//
//		for(int i = 0; i<matrix.length; i++){
//			for(int j = 0; j<matrix.length; j++){
//				
//				data[k][0] = study[i].toString();
//				data[k][1] = study[j].toString();
//				data[k][2] = new Double(matrix[i][j]).toString(); 
//				
//				k++;
//			}
//		}
		
		return null;
	}
	
	
	/**
	 * PhenomizerResultList to String[][]
	 * @param prList
	 * @return
	 */
	private String[][] phenomizarResultListToString(PhenomizerResultList prList){

		String[][] data = new String[prList.size()][4];
		
		if (prList!=null){
			
			ArrayList<PhenomizerResult> resultList = prList.getResultList();
			Iterator<PhenomizerResult> iter = resultList.iterator();
			int i = 0;
			while(iter.hasNext()){
				PhenomizerResult pr = iter.next();
				data[i][0] = new Double(pr.getPvalue()).toString(); //pvalue
				data[i][1] = new Double(pr.getAdjust_pvalue()).toString(); //adjust pvalue
				data[i][2] = new Double(pr.getScore()).toString(); //score
				data[i][3] = pr.getOmim().toString(); //omim
				
				i++;
			}
			
		}
		
		return data;
	}
	
	/* Read input */
	private String[] getStudy(String query){
		return query.split("[\t \n \b]");
	}
	
	private boolean validateHPOQuery(String[] input){
		int i = input.length-1;
		
		while(i>=0 && HPOUtils.isHPOterm(input[i])){
			i--;
		}
		
		return !(i>=0);
		
	}
	


}
