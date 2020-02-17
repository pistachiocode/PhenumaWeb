//package ontologizer.calculation;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.logging.Logger;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import ontologizer.association.AssociationContainer;
//import ontologizer.go.Ontology;
//import ontologizer.types.ByteString;
//import phenomizer.utils.NumberUtils;
//import phenuma.entities.Disease;
//import phenuma.entities.Gene;
//import phenuma.network.Network;
//import phenuma.network.Edge;
//import phenuma.network.Node;
//
//public class SemanticResult
//{
//        //Entity manager factory
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory(phenuma.constants.Constants.PERSISTENCE_UNIT_PHENOMIZER_DB);
//
//        
//	private static Logger logger = Logger.getLogger(SemanticResult.class.getCanonicalName());
//
//	public Ontology g;
//        public AssociationContainer assoc;
//
//	public ByteString [] names;
//	public float [][] mat;
//	public String name;
//
//	public SemanticCalculation calculation;
//
//	/**
//	 * Write similarity matrix using columns
//	 * @param file
//	 */
//	public void writeColumns(File file, String header)
//	{
//		double y = 0.00;
//		try
//		{
//			logger.info("Writing to \"" + file.getCanonicalPath() + "\".");
//
//			PrintWriter out = new PrintWriter(file);
//                        
//                        out.println(header);
//
//			if (names != null && names.length>0)
//			{
//				
//				for (int i=0;i<names.length;i++)
//				{		
//					for (int j=i+1;j<names.length;j++)
//					{
//                                            y = mat[i][j];
//                                            if (y>0){
//                                                    out.print(names[i]);
//                                                    out.print("\t");
//                                                    out.print(names[j]);
//                                                    out.print("\t");
//
//                                                    out.print(NumberUtils.round(y, 4));
//                                                    out.println();
//                                            }
//                                            
//					}	
//				}
//			}
//			
//			out.close();
//		} 
//		catch (IOException e)
//		{
//			System.out.println(e.getMessage());
//		}
//
//	}
//	
//	/**
//	 * Write the semanatic similarity matrix in file
//	 * @param file
//	 */
//	
//	public void writeTable(File file)
//	{
//		double y = 0.00;
//		try
//		{
//			logger.info("Writing to \"" + file.getCanonicalPath() + "\".");
//
//			PrintWriter out = new PrintWriter(file);
//
//			if (names != null && names.length>0)
//			{
//				out.print("\t");
//				out.print(names[0]);
//				for (int i=0;i<names.length;i++)
//				{
//					out.print("\t");
//					out.print(names[i]); 
//				}
//				
//				out.println();
//				
//				for (int i=0;i<names.length;i++)
//				{		
//					out.print(names[i]);
//					for (int j=0;j<names.length;j++)
//					{
//						y = mat[i][j];
//						out.print("\t");
//						out.print(Math.rint(y*1000)/1000);
//					}	
//					out.println();
//				}
//			}
//			
//			out.close();
//		} catch (IOException e)
//		{
//			System.out.println(e.getMessage());
//		}
//
//	}
//
//	public Ontology getG() {
//		return g;
//	}
//
//	public AssociationContainer getAssoc() {
//		return assoc;
//	}
//
//	public void setG(Ontology g) {
//		this.g = g;
//	}
//
//	public void setAssoc(AssociationContainer assoc) {
//		this.assoc = assoc;
//	}
//
//        
//        //Create a network with the semantic result
//        public Network toNetwork(String type) {
//            // obtener una instancia de EntityManager del factory
//            EntityManager em = factory.createEntityManager();
//            /**Create Network object*/
//            Network network = new Network(phenuma.networkproyection.NetworkConstants.SYMMETRIC);
//            
//            double y = 0.00;
//
//
//            if (names != null && names.length>0)
//            {
//
//                for (int i=0;i<names.length;i++)
//                {		
//                    for (int j=i+1;j<names.length;j++)
//                    {
//                        y = mat[i][j];
//                        
//                        if (y>0)
//                        {
//                            //Get object from database
//                            Object o1, o2 = null;
//
//                            if (type.equals("Genes")) {
//                                /** Genes */
//                                o1 = em.find(Gene.class, new Integer(names[i].toString()).intValue());
//                                o2 = em.find(Gene.class, new Integer(names[j].toString()).intValue());
//                            } else {
//                                /** Disease */
//                                o1 = em.find(Disease.class, new Integer(names[i].toString()).intValue());
//                                o2 = em.find(Disease.class, new Integer(names[j].toString()).intValue()); 
//                            }
//
//                            /** Create network item object*/
//                            Edge item = new Edge(new Node(o1), new Node(o2));
//                            
//                            item.setScore(y);
//
//                            /**Add item to the network*/
//                            network.add(item);
//                        }
//                    }	
//                }
//            }
//            /**Close Entity Manager instance*/
//            em.close();
//            return network;
//        }
//
//	
//}
