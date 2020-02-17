//package ontologizer.query;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import ontologizer.go.OBOParserException;
//import phenomizer.hpo.PhenomizerAux;
//import phenomizer.matrix.SimilarityMatrixGeneration;
//import phenomizer.matrix.HybridMatrixGeneration;
//import phenomizer.matrix.HybridMatrixResult;
//import phenuma.constants.Constants;
//
//public class phenomizer2 {
//    
//    
//        public static Map<Integer, Integer> patient2querysize()
//        {
//            Map<Integer, Integer> patient2querysize = new HashMap<Integer, Integer>();
//
//
//            int numline = 0;
//
//            try
//            {
//
//                BufferedReader is = new BufferedReader(new FileReader("./resources/patient_querysize.txt"));
//                String line = is.readLine();
//
//                while(line!=null)
//                {
//                    if(!line.startsWith("#"))
//                    {
//                        /**
//                         * Get values from file
//                         */
//                        String[] str = line.split("[\t]");
//
//                        Integer id = new Integer(str[0]);
//                        Integer querysize = new Integer(str[1]);
//
//                        patient2querysize.put(id, querysize);
//
//                        
//                    }
//
//                    line = is.readLine();
//                    numline++;
//                }
//
//    //            
//    //            Set<Integer> keyset = id2hpolist.keySet();
//    //            Iterator<Integer> iter = keyset.iterator();
//    //            while(iter.hasNext()) {
//    //                Integer i = iter.next();
//    //                ArrayList<TermID> list = id2hpolist.get(i);
//    //                
//    //                System.out.println(i.toString()+" query size "+list.size());
//    //            }
//
//            }
//            catch(Exception e)
//            {
//                System.err.println(e.getMessage()+" error en la linea "+numline);
//            }
//
//
//
//
//            return patient2querysize;
//        
//        }   
//    
//
//        /*Check phenotypic profile*/
//        
//        public static boolean genes(String str){
//            
//            String curatedStr = str.toLowerCase().trim();
//            
//            return curatedStr.equals("gene") || curatedStr.equals("genes");
//        }
//        
//        public static boolean diseases(String str){
//            
//            String curatedStr = str.toLowerCase().trim();
//            
//            return curatedStr.equals("disease") || curatedStr.equals("diseases") || 
//                   curatedStr.equals("omim") || curatedStr.equals("enfermedades") ||
//                   curatedStr.equals("enfermedad");
//        }
//              
//        public static boolean patient(String str){
//            
//            String curatedStr = str.toLowerCase().trim();
//            
//            return curatedStr.equals("patient") || curatedStr.equals("patients") || 
//                   curatedStr.equals("paciente") || curatedStr.equals("pacientes");
//        }
//                
//        public static boolean patientdenovo(String str){
//            
//            String curatedStr = str.toLowerCase().trim();
//            
//            return curatedStr.equals("patientdenovo") || curatedStr.equals("patientsdenovo")   || 
//                   curatedStr.equals("pacientedenovo") || curatedStr.equals("pacientesdenovo") ||
//                   curatedStr.equals("novo") || curatedStr.equals("denovo");
//        }
//                           
//        public static boolean syndrome(String str){
//            
//            String curatedStr = str.toLowerCase().trim();
//            
//            return curatedStr.equals("syndrome") || curatedStr.equals("syndromes") || 
//                   curatedStr.equals("síndrome") || curatedStr.equals("síndromes") ||
//                   curatedStr.equals("sindrome") || curatedStr.equals("sindromes");
//        }
//        
//        
//        /**
//         * Help
//         */
//        public static void help(){
//            System.out.println("PHENOTYPOC SIMILARITY MATRIX GENERATION");
//            System.out.println("Genes:             java -jar Phenuma.jar genes");
//            System.out.println("Diseases:          java -jar Phenuma.jar disease");
//            System.out.println("Patients:          java -jar Phenuma.jar patients");
//            System.out.println("Patients de novo : java -jar Phenuma.jar patientsdenovo");
//            System.out.println("Sydromes:          java -jar Phenuma.jar syndromes");
//            
//        }
//        
//	/**    
//	 * @param args
//	 * @throws ParseException 
//	 */
//	public static void main(String[] args) throws IOException, OBOParserException {    
//            
//                PhenomizerAux p = PhenomizerAux.getInstance();
//                
//                
//                int numargs = args.length; 
//                
//                
//                if (numargs == 0)
//                    help();
//                
//                
//                
//                if (numargs == 1)
//                {
//
//                    SimilarityMatrixGeneration simmatrix = new SimilarityMatrixGeneration();
//                    
//                    //Ontology/subontology
//                    
//                    simmatrix.setSemanticSimilarity(Constants.ROBINSON_SYMMETRIC);
//                    p.setTermSemanticSimilarityMeasure(Constants.RESNIK_TSS);
//                    
//                    
//                    //Annotated Phenotypic Profiles
//                    if(genes(args[0]))
//                        simmatrix.setOntologyId(Constants.HPO_GENES);
//                    else if(diseases(args[0]))
//                        simmatrix.setOntologyId(Constants.HPO_DISEASES);
//                 /*   else if(patient(args[0]))
//                        simmatrix.setAnnotatedPPAnnotationFile(Constants.ASSOCIATION_PATIENT_PATH);
//                    else if (patientdenovo(args[0]))
//                        simmatrix.setAnnotatedPPAnnotationFile(Constants.ASSOCIATION_PATIENT_DENOVO_PATH);
//                    else if (syndrome(args[0]))
//                        simmatrix.setAnnotatedPPAnnotationFile(Constants.ASSOCIATION_SYNDROME_PATH);*/
//                    else {
//                        System.err.println("Incorrect phenotypic profile");
//                        help();
//                    }
//
//
//                    //Output file
//                    simmatrix.setOutputFile("./resources/out/matrix/hpo/"+args[0]+"_matrix_robinson_symmetric.txt");
//                    
//                    String header = "###############################################\n"
//                                  + "#  SIMILARITY MATRIX \n#\n"
//                                  + "#  Annotation file :" +simmatrix.getAnnotatedPPAnnotationFile()+"\n"
//                                  + "#  Ontology File: "+simmatrix.getOntologyFile()+" updated: 2011-06-21\n"
//                                  + "#  Subontology: "+simmatrix.getSubontology()+"\n"
//                                  + "#  Semantic Similarity Measure "+p.getSemanticSimilarityName(simmatrix.getSemanticSimilarity())+"\n"
//                                  + "#  Threshold: 0.0 \n"
//                                  + "#  Date: "+new Date().toString()+"\n"
//                                  + "###############################################\n";
//                    
//                    simmatrix.createMatrix(header);
//
//                                 
//                    
//                }
//                else if (numargs == 2)
//                {
//                    HybridMatrixGeneration hybridmatrix = new HybridMatrixGeneration();
//                    
//                    //Ontology/subontology
//                    hybridmatrix.setOntologyFile(phenuma.constants.Constants.HPO_PATH_JUN);
//                    hybridmatrix.setSubontology(phenuma.constants.Constants.PHENOTYPIC_ABNORMALITY);
//                    
//                    //Annotated Phenotypic Profiles
//                    if(genes(args[0]))
//                        hybridmatrix.setAnnotatedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_GENE_HPO_PATH);
//                    else if(diseases(args[0]))
//                        hybridmatrix.setAnnotatedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_DISEASE_PATH_JUN);
//                    else if(patient(args[0]))
//                        hybridmatrix.setAnnotatedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_PATIENT_PATH);
//                    else if (patientdenovo(args[0]))
//                        hybridmatrix.setAnnotatedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_PATIENT_DENOVO_PATH);
//                    else if (syndrome(args[0]))
//                        hybridmatrix.setAnnotatedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_SYNDROME_PATH);
//                    else {
//                        System.err.println("Incorrect phenotypic profile: "+args[0]);
//                        help();
//                    }
//          
//
//                    //Queried Phenotypic Profiles
//                    if(genes(args[1]))
//                        hybridmatrix.setQueriedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_GENE_HPO_PATH);
//                    else if(diseases(args[1]))
//                        hybridmatrix.setQueriedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_DISEASE_PATH_JUN);
//                    else if(patient(args[1]))
//                        hybridmatrix.setQueriedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_PATIENT_PATH);
//                    else if (patientdenovo(args[1]))
//                        hybridmatrix.setQueriedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_PATIENT_DENOVO_PATH);
//                    else if (syndrome(args[1]))
//                        hybridmatrix.setQueriedPPAnnotationFile(phenuma.constants.Constants.ASSOCIATION_SYNDROME_PATH);
//                    else {
//                        System.err.println("Incorrect phenotypic profile: "+args[1]);
//                        help();
//                    }
//                    
//                    //Output file
//                    hybridmatrix.setOutputFile("./resources/out/matrix/hpo/"+args[0]+"_"+args[1]+"_matrix_robinson_symmetric.txt");                    
//
//                    HybridMatrixResult result = hybridmatrix.createHybridMatrix();
//
//                    String header =   "###############################################\n"
//                                    + "#  HYBRID MATRIX \n#\n"
//                                    + "#  Annotation file :" +hybridmatrix.getAnnotatedPPAnnotationFile()+"\n"
//                                    + "#  Queried elements annotation file: "+hybridmatrix.getQueriedPPAnnotationFile()+"\n"
//                                    + "#  Ontology File: "+hybridmatrix.getOntologyFile()+" updated: 2011-06-21\n"
//                                    + "#  Subontology: "+hybridmatrix.getSubontology()+"\n"
//                                    + "#  Threshold: 0.0 \n"
//                                    + "#  Date: "+new Date().toString()+"\n"
//                                    + "###############################################\n";
//                    
//                    result.setHeader(header);
//                    result.writeColumns(new File(hybridmatrix.getOutputFile()));
//                    
//                
//                }
//        /**        else if (option == 3)
//                {
//                    try {
//                        //Query
//                        Phenomizer phenomizer = Phenomizer.getInstance();
//                        
//                        //Set ontology and annotation file
//                        phenomizer.setOntologyPath(constants.Constants.HPO_PATH_JUN, constants.Constants.PHENOTYPIC_ABNORMALITY);
//                        phenomizer.setAnnotationsPath(constants.Constants.ASSOCIATION_PATIENT_PATH);
//                        
//                        MultipleQuery query = new MultipleQuery(constants.Constants.ASSOCIATION_PATIENT_PATH);
//                        query.setOutputFile("./resources/out/query_patient_disease.txt");
//                        
//                        query.executeQuery();
//                        
//                    } catch (OBOParserException ex) {
//                        Logger.getLogger(phenomizer2.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (IOException ex) {
//                        Logger.getLogger(phenomizer2.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                else if (option == 4)
//                {
//                    //Chapuzilla para saber el numero de terminos asociado a cada paciente.
//                    Phenomizer phenomizer = Phenomizer.getInstance();
//                    try {
//                        phenomizer.setOntologyPath(constants.Constants.HPO_PATH_JUN, constants.Constants.PHENOTYPIC_ABNORMALITY);
//                    } catch (IOException ex) {
//                        Logger.getLogger(phenomizer2.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (OBOParserException ex) {
//                        Logger.getLogger(phenomizer2.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    
//                    
//                    Map<Integer, Integer> patient2querysize = patient2querysize();
//
//                   
//                    //Codigo para generar matrix paciente - diseases con p-valores a partir de un archivo
//                    //con los scores ya calculados. Auxiliar.
//                    
//                    FileReader fis = null;
//                    BufferedReader bis = null;
//                    PrintWriter out = null;
//
//                    try
//                    {
//                        
//			fis = new FileReader("./resources/in/query_patient_disease.txt");
//			bis = new BufferedReader(fis);
//                        out = new PrintWriter(new FileWriter("./resources/out/query_patient_disease_columns.txt"));
//                        
//			String patient = "";
//                        Integer querysize = new Integer(0);
//                        
//			String linea=bis.readLine().trim();
//			
//			int numlinea = 0;
//			boolean error = false;
//
//			//ArrayList<ScorePValue> scores = new ArrayList<ScorePValue>();
//			
//			while(!error && linea != null){
//				//Get disease omim
//				if (linea.contains(">")){
//
//                                    String[] str = linea.split(">");
//
//                                    //if the structure of the file is wrong
//                                    error = !(str.length == 2);
//                                    
//                                    //read next disease omim
//                                    if (!error)
//                                        patient = str[1].trim();
//
//                                   
//                                    System.out.println(patient);
//                                    querysize = patient2querysize.get(new Integer(patient));
//
//
//				}
//				else
//				{			
//                                    String[] str = linea.split("\t");
//		
//                                    if (str.length == 5){
//                                        //read ScorePValue object values
//                                        double score = new Double(str[2]).doubleValue();//score
//                                        String disease = str[3]; //disease omim
//                                        if(score > 0){
//                                            //Obtener pvalor de la base de datos.
//                                            DBPValue pvalor = new DBPValue(disease, querysize.intValue(), score);
//                                            out.write(patient+"\t"+disease+"\t"+score+"\t"+pvalor.getPvalue()+"\t"+pvalor.getBenjamini_adjust()+"\n");
//
//                                        }
//						
//                                    }//if
//				
//
//                                }//if  
//                                
//                                //Read next line
//                                if(!error){
//                                        linea = bis.readLine();
//                                        numlinea++;
//
//                                        //ignore empty lines
//                                        if(linea!=null){
//                                                while(linea!=null && linea.isEmpty()){
//                                                        linea = bis.readLine();
//                                                        numlinea++;
//                                                }
//                                        }
//
//                                }
//                              
//
//			}//while
//		
//			fis.close();
//		   	bis.close();
//		   	out.close();
//                        
//		   	if(error)
//		   		System.err.println("Error en la linea "+numlinea+" del fichero : "+linea);
//
//                    }  catch (IOException e) {
//                        e.printStackTrace();
//                    }
//            } else if (option == 5) {
//                
//                DBUtils.connect();
//		//execute query
//                long start = System.currentTimeMillis();
//                //for(int i = 0; i<= 5000; i++){
//                    DBPValue pvalor = new DBPValue("129500", 5, 2);
//                //}
//                long end = System.currentTimeMillis();
//                System.out.println("took: "+ new Double(end-start)/1000+" seconds");
//                DBUtils.close();
//            }*/
//        }//main*
//
//}