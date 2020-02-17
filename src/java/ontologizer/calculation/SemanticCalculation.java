package ontologizer.calculation;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.logging.Logger;

//import Utils.Constants;
//import XMLObjects.Disease;

import ontologizer.GOTermEnumerator;
import ontologizer.StudySet;
import ontologizer.association.AssociationContainer;
import ontologizer.go.Ontology;
import ontologizer.go.Term;
import ontologizer.go.TermID;
import ontologizer.util.IntHashMapForDoubles;
import ontologizer.types.ByteString;
import phenomizer.hpo.Phenuma;
import phenomizer.utils.NumberUtils;
import phenomizer.utils.StatisticsUtils;
import phenuma.network.EdgeSemanticInfo;
import phenuma.network.Network;



/**
 * @author Armando
 *
 */
public class SemanticCalculation
{
	private static Logger logger = Logger.getLogger(SemanticCalculation.class.getCanonicalName());

	/*public static interface ISemanticCalculationProgress
	{
		void init(int max);
		vo*/
	
        /**
         * Number of available processors
         */
	private int numberOfProcessors = Runtime.getRuntime().availableProcessors();
	
        /**
         * Ontology 
         */
	protected Ontology graph;
      //  private int ontologyID;
        
        /**
         * Association container. The redundant relationships have not been removed yet.
         */
	protected AssociationContainer goAssociations;

        /**
         * Stude set with all objects annotated.
         */
	private StudySet allObjectStudy;
        
        /**
         * GoTermEnumerator
         */
	private GOTermEnumerator enumerator;
        
        /**
         * Number of objects annotated.
         */
	private int totalAnnotated;
	
	/** Similarity cache (indexed by term) ontologizer.util.IntHasMapForDoubles --> moved*/
	private IntHashMapForDoubles [] cache;

	private ReentrantReadWriteLock cacheLock;
	private ReadLock readLock;
	private WriteLock writeLock;

	/**
	 * Non-redundant associations (indexed by genes).
	 * Objects is an array of terms
	 */
	protected Object [] associations;
        
        
        private Phenuma phenomizer = Phenuma.getInstance();

        /**
         * Gene to index relationship. This map contains an index for each
         * object annotated.
         */
	private HashMap<ByteString,Integer> gene2index = new HashMap<ByteString,Integer>();



        public SemanticCalculation(Ontology ontology, AssociationContainer assoc)
	{
            
      
            this.graph = ontology;
            this.goAssociations = assoc;

            //new study set creation with all population
            allObjectStudy = new StudySet("population");
            for (ByteString gene : goAssociations.getAllAnnotatedPP())
                    allObjectStudy.addElement(gene,"");

            enumerator = allObjectStudy.enumerateGOTerms(graph, goAssociations);
            totalAnnotated = enumerator.getAnnotatedGenes(graph.getRootTerm().getID()).totalAnnotated.size();

            
            cache = new IntHashMapForDoubles[graph.maximumTermID()+1];

            /* Making associations non-redundant */
            associations = new Object[allObjectStudy.getSize()];


            int i = 0;
            for (ByteString gene : allObjectStudy) //diseases
            {
                /*Add index to each element (Gene or Disease)*/
                gene2index.put(gene,i);



                ArrayList<TermID> assocList = goAssociations.get(gene).getAssociations(); //list of terms 

                HashSet<TermID> inducedNodes = new HashSet<TermID>();

                for (TermID tid : assocList)
                        inducedNodes.addAll(graph.getTermsOfInducedGraph(null, tid));

                HashSet<TermID> nonRedundantTerms = new HashSet<TermID>();

                //:S
                termloop: 
                for (TermID tid : assocList)
                {
                        for (TermID desc : graph.getTermChildren(tid))
                        {
                                if (inducedNodes.contains(desc))
                                    continue termloop; 

                        }
                        nonRedundantTerms.add(tid);
                }

                TermID terms[] = new TermID[nonRedundantTerms.size()];
                int j=0;
                for (TermID t : nonRedundantTerms)
                        terms[j++]=t;

                /* TODO: Sort terms according to their information content */
                /* i is the index related with the element (gene or disease)*/
                associations[i] = terms;

                i++;

            }

            if (numberOfProcessors > 1)
            {
                    cacheLock = new ReentrantReadWriteLock();
                    readLock = cacheLock.readLock();
                    writeLock = cacheLock.writeLock();
            }
                
                
	}
        
        
        /**
	 * Returns the number of annotations of a given term
         * 
	 * @param id
	 * @return
	 */
	public double numberOfAnnotations(TermID id)
	{     
		return enumerator.getAnnotatedGenes(id).totalAnnotatedCount();
	}
        
        
        
	/**
	 * Returns the probability the given term.
	 * 
	 * @param id
	 * @return
	 */
	public double probability(TermID id)
	{     
		return (double)enumerator.getAnnotatedGenes(id).totalAnnotatedCount() / totalAnnotated;
	}
	
	/**
	 * Returns the shared information content of two given terms
	 *  
	 * @param t1
	 * @param t2
	 * @return
	 */
	private double p(TermID t1, TermID t2)
	{
		/*NOTE: sharedParents(t1,t2) = ComAnc(t1, t2) Common Ancestors */
		Collection<TermID> sharedParents = graph.getSharedParents(t1, t2);
		double p = 1.0;
		/*NOTE: Calculate p for each common ancestor and return the minimum*/
		/* The information content of two terms is defined as the minimum of
		 * the information content of the shared parents.
		 */
		for (TermID t : sharedParents)
		{
			double newP = probability(t);
			if (newP < p) {
                            p = newP;
                        } 
		}
                
		return p;
	}

        
        /**
	 * Returns the termID of the most informative shared parent
	 *  
	 * @param t1
	 * @param t2
	 * @return
	 */
	public TermID p_term(TermID t1, TermID t2)
	{
		/*NOTE: sharedParents(t1,t2) = ComAnc(t1, t2) Common Ancestors */
		Collection<TermID> sharedParents = graph.getSharedParents(t1, t2);
		double p = 1.0;
		/*NOTE: Calculate p for each common ancestor and return the minimum*/
		/* The information content of two terms is defined as the minimum of
		 * the information content of the shared parents.
		 */
                TermID term = null;
		for (TermID t : sharedParents)
		{
			double newP = probability(t);
			if (newP <= p) {
                            p = newP;
                            term = t;
                        } 
		}
                
		return term;
	}
	/**
	 * Returns the similarity of the two given terms.
         * 
	 * resnik = - log (p(t1,t2)) where p is the 
         * max informative shared parent of t1 and t2.
         *  
	 * @param t1
	 * @param t2
	 * @return
	 */
	public double resnik(TermID t1, TermID t2)
	{
		/* The resnik similarity between two terms is simmetric */
		if (t1.id > t2.id)
		{
			TermID s = t2;
			t2 = t1;
			t1 = s;
		}

		if (cacheLock != null)
			readLock.lock();

		IntHashMapForDoubles map2 = cache[t1.id];
		if (map2 != null)
		{
			double val = map2.get(t2.id);
			if (!Double.isNaN(val))
			{       
				if (cacheLock != null) cacheLock.readLock().unlock();	
				return val;
			}
		}

		if (cacheLock != null)
		{
			/* Upgrade lock, must unlock the read lock manually before */
			readLock.unlock();
			writeLock.lock();
		}

		/* Create HashMap when needed, but the value is definitively not there */
		if (map2 == null)
		{
			map2 = new IntHashMapForDoubles();
			cache[t1.id] = map2;
		}

		/*NOTE: Information Content (IC) = -log(p(n)), where p(n) denotes the frequency 
                 * of annotations of n among all items in the domain*/
                                                
		double p = -Math.log(p(t1,t2)); 
		map2.put(t2.id,p);
		
		if (cacheLock != null)
			writeLock.unlock();

		return p;
	}
	
	/**
	 * Returns the similarity of the two given terms.
	 * NOTE: Returns the information content of the most informative ancestors of two given terms.
	 * @param t1
	 * @param t2
	 * @return
	 */
	public double informationContent(TermID t1)
	{
		double p = -Math.log(probability(t1)); 
		return p;
	}
	
        
        /**
         * Jiang and Conrath semantic similarity
         * simJC = 1 /(1+distJC)
         * 
         * distJC = ic(t1) + ic(t2) - 2*IC(MICA(t1,t2))
         * 
         * @param t1
         * @param t2
         * @return 
         */
        public double jiangConrath(TermID t1, TermID t2)
        {
            double ic_t1 = this.informationContent(t1);
            double ic_t2 = this.informationContent(t2);

            double resnik = this.resnik(t1, t2); //information content of the most informative common ancestor


            double distJC = ic_t1 + ic_t2 - 2*resnik;

            return 1/(1+distJC);
        }
        
        /**
         * Returns the value of the semantic similarity of two terms
         */
        public double termSemanticSimilarity(TermID t1, TermID t2)
        {
            return this.resnik(t1, t2);
        
        }
	
	/**
	 * Returns the phenomizer similarity of two given genes.
	 * NOTE: simPhenomizer = avg( sum( max[sim (g1, g2)])); 
	 * @param index of gene1
	 * @param index of gene2
	 * @return
	 */
	private double simAvg(int g1, int g2)
	{
		double simAvg, max;
		
		if (g1 < 0 || g2 < 0) return 0;
		
		simAvg = 0.0;
		max = 0.0;
		
		TermID [] tl1 = (TermID[])associations[g1]; 
		TermID [] tl2 = (TermID[])associations[g2];
		
		/* TODO: Research if we can employ sorting omit some or many of
		 * the pairs. 
		 */
		double sumSim = 0.0;
	
		for (TermID t1 : tl1)
		{
			max=0.0;
			for (TermID t2 : tl2)
			{
				double newSim = termSemanticSimilarity(t1,t2); //NOTE: -log(IC(t1,t2))
				if (newSim > max) max = newSim; //max(t1,tl2)
			}

			sumSim = sumSim+max; //max(t1,tl2) + max(t2,tl2) + .... + max(tn,tl2)

		}
		
		if (tl1.length>0)
			simAvg = sumSim/tl1.length;
		else
			simAvg = 0.0;
		
		return simAvg;
	}
	
	// Resnik semantic measure
	
	/**
	 * Returns the similarity of two given genes.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	public double resnikSimilarity(ByteString g1, ByteString g2)
	{
            Integer intg1 = this.gene2index.get(g1);
            Integer intg2 = this.gene2index.get(g2);
            
            if(intg1!=null && intg2!=null)
                return resnikSimilarity(intg1, intg2);
            else
                return 0;
	}
	
	/**
	 * Returns the similarity of two given diseases or genes. Calculated as the maximum(ic(ti,tj))
	 * 
	 * Source:
	 * http://www.cs.cmu.edu/afs/cs/project/jair/pub/volume11/resnik99a.pdf
	 * 
	 * @param index of gene1
	 * @param index of gene2
	 * @return
	 */
	private double resnikSimilarity(int g1, int g2)
	{
            double sim;

            if (g1 < 0 || g2 < 0) return 0;

            sim = 0.0;

            TermID [] tl1 = (TermID[])associations[g1]; 
            TermID [] tl2 = (TermID[])associations[g2];

            /* TODO: Research if we can employ sorting omit some or many of
             * the pairs. 
             */
            for (TermID t1 : tl1)
            {
                    for (TermID t2 : tl2)
                    {
                            double newSim = termSemanticSimilarity(t1,t2);
                            if (newSim > sim) sim = newSim;
                    }
            }
            return sim;
	}
        
	
        
        /**
	 * Returns the list of terms which IC is maximum
	 * 
	 * 
	 * @param index of gene1
	 * @param index of gene2
	 * @return
	 */
	public List<EdgeSemanticInfo> resnikSimilarityMaximumTerms(ByteString g1, ByteString g2)
	{
            List<EdgeSemanticInfo> maximumTerms = new ArrayList<EdgeSemanticInfo>();
             
            Integer intg1 = this.gene2index.get(g1);
            Integer intg2 = this.gene2index.get(g2);
            
            if(intg1 == null)
            {
                System.out.println("Null: "+g1);
                return maximumTerms;
            }
            else if(intg2 == null){
                System.out.println("Null: "+g2);
                return maximumTerms;
            }
           

            if (intg1 < 0 || intg2 < 0) return maximumTerms; //empty list


            TermID [] tl1 = (TermID[])associations[intg1]; 
            TermID [] tl2 = (TermID[])associations[intg2];
            
            double sim = 0.0;
            
            TermID maxTerm = null;
            TermID t_max = null;
            
            //Include the information t1->t2
            
            for (TermID t1 : tl1)
            {
                    for (TermID t2 : tl2)
                    {
                            double newSim = termSemanticSimilarity(t1,t2);
                            if (newSim >= sim)
                            { 
                                sim = newSim;
                                t_max = t2;
                                maxTerm = p_term(t1,t2);
                            }
                    }
                    
                    EdgeSemanticInfo info = new EdgeSemanticInfo(t1,t_max,maxTerm, NumberUtils.round(sim, 3));
                    
                    info.setTermMaxName(this.graph.getTerm(maxTerm).getName());
                    
                    if(!maximumTerms.contains(info))
                        maximumTerms.add(info);
                   
                    sim = 0.0;
                    maxTerm = null;
            }
            
            
            //Include the information t2->t1
            sim = 0.0;
            maxTerm = null;
            
            for (TermID t2 : tl2)
            {
                    for (TermID t1 : tl1)
                    {
                            double newSim = termSemanticSimilarity(t2,t1);
                            if (newSim >= sim)
                            { 
                                sim = newSim;
                                t_max = t1;
                                maxTerm = p_term(t2,t1);
                            }
                    }
                    
                    EdgeSemanticInfo info = new EdgeSemanticInfo(t2, t_max, maxTerm, NumberUtils.round(sim, 3));
                    info.setTermMaxName(this.graph.getTerm(maxTerm).getName());
                    
                    if(!maximumTerms.contains(info))
                        maximumTerms.add(info);
                    
                    sim = 0.0;
                    maxTerm = null;
            }

            return maximumTerms;
	}
        
        
         /**
	 * Returns the list of terms which IC is maximum
	 * 
	 * Each EdgeSemanticInfo in the output list has:
         *  
         *  - termX --> term of annotated object
         *  - termY --> term of queried object
         * 
	 * @param index of gene1
	 * @param index of gene2
	 * @return
	 */
	public List<EdgeSemanticInfo> resnikSimilarityMaximumTerms(List<TermID> termlist, ByteString object)
	{
            Integer intg1 = this.gene2index.get(object);
            
            List<EdgeSemanticInfo> maximumTerms = new ArrayList<EdgeSemanticInfo>();

            if (intg1 < 0) return maximumTerms; //empty list


            TermID [] tl1 = (TermID[])associations[intg1]; 
            
            double sim = 0.0;
            
            TermID maxTerm = null;
            TermID t_max = null;
            
            for (TermID t1 : tl1)
            {
                    for (TermID t2 : termlist)
                    {
                        /*Get max IC of sharedparents between t1 and t2*/
                        double newSim = termSemanticSimilarity(t1,t2);
                        if (newSim >= sim)
                        { 
                            sim = newSim;
                            t_max = t2;
                            maxTerm = p_term(t1,t2); //get max shared parent (TermID)
                        }
                    }
                    
                    EdgeSemanticInfo info = new EdgeSemanticInfo(t1, t_max, maxTerm, NumberUtils.round(sim, 3));
                    info.setTermMaxName(this.graph.getTerm(maxTerm).getName());
                    
                    if(!maximumTerms.contains(info))
                        maximumTerms.add(info);
                    

                    sim = 0.0;
                    maxTerm = null;
            }
            
            sim = 0.0;
            maxTerm = null;
            
            for (TermID t2 : termlist)
            {
                    for (TermID t1 : tl1)
                    {
                        /*Get max IC of sharedparents between t1 and t2*/
                        double newSim = termSemanticSimilarity(t2,t1);
                        if (newSim >= sim)
                        { 
                            sim = newSim;
                            t_max = t1;
                            maxTerm = p_term(t2,t1); //get max shared parent (TermID)
                        }
                    }
                    
                    EdgeSemanticInfo info = new EdgeSemanticInfo(t2, t_max, maxTerm, NumberUtils.round(sim, 3));
                    info.setTermMaxName(this.graph.getTerm(maxTerm).getName());
                    
                    if(!maximumTerms.contains(info))
                        maximumTerms.add(info);

                    sim = 0.0;
                    maxTerm = null;
            }

            return maximumTerms;
	}
        
	/**
	 * Returns the similarity of two given diseases or genes. Calculated as the maximum(ic(ti,tj))
	 * 
	 * Source:
	 * http://www.cs.cmu.edu/afs/cs/project/jair/pub/volume11/resnik99a.pdf
	 * 
	 * @param index of gene1
	 * @param index of gene2
	 * @return
	 */
	private double robinsonSimilarity(int g1, int g2)
	{
		double simAvg, max = 0.0;
		
		if (g1 < 0 || g2 < 0) return 0;
	
		TermID [] tl1 = (TermID[])associations[g1]; 
		TermID [] tl2 = (TermID[])associations[g2];
		
		/* TODO: Research if we can employ sorting omit some or many of
		 * the pairs. 
		 */
                
                double sumSim = 0.0;
                
		for (TermID t1 : tl1)
		{
                    max=0.0;
                    for (TermID t2 : tl2)
                    {
                        double newSim = termSemanticSimilarity(t1,t2);
                      //  System.out.println(t1.toString()+" "+t2.toString()+" "+NumberUtils.round(newSim,4));
			if (newSim > max) max = newSim;
                    }
			
                    sumSim = sumSim+max; //max(t1,tl2) + max(t2,tl2) + .... + max(tn,tl2)
		}
		
                if (tl1.length>0)	
			simAvg = sumSim/tl1.length;
		else
			simAvg = 0.0;
			
		return simAvg;
	}
        
        
	/**
	 * Returns the similarity of two given genes or diseases.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	private double robinsonSimilarity(ByteString g1, ByteString g2)
	{		
            Integer intg1 = this.gene2index.get(g1);
            Integer intg2 = this.gene2index.get(g2);
            
            if(intg1!=null && intg2!=null)
                return robinsonSimilarity(intg1, intg2);
            else
                return 0;
            
                               
	}
        
        
        	/**
	 * Returns the similarity of two given diseases or genes. Calculated as the maximum(ic(ti,tj))
	 * 
	 * Source:
	 * http://www.cs.cmu.edu/afs/cs/project/jair/pub/volume11/resnik99a.pdf
	 * 
	 * @param index of gene1
	 * @param index of gene2
	 * @return
	 */
	private double robinsonSimilarityAND(int g1, int g2)
	{
                double simAvg, max = 0.0;
		
		if (g1 < 0 || g2 < 0) return 0;
	
		TermID [] tl1 = (TermID[])associations[g1]; 
		TermID [] tl2 = (TermID[])associations[g2];
		
		/* TODO: Research if we can employ sorting omit some or many of
		 * the pairs. 
		 */
                
                double sumSim = 0.0;
                
		for (TermID t1 : tl1)
		{
                    max=0.0;
                    for (TermID t2 : tl2)
                    {
                        double newSim = termSemanticSimilarity(t1,t2);
                      //  System.out.println(t1.toString()+" "+t2.toString()+" "+NumberUtils.round(newSim,4));
			if (newSim > max) max = newSim;
                    }
			
                    sumSim = sumSim+max; //max(t1,tl2) + max(t2,tl2) + .... + max(tn,tl2)
		}
		
                
                int maxlength = Math.max(tl1.length,tl2.length);
                
                if (maxlength >0)	
			simAvg = sumSim/maxlength;
		else
			simAvg = 0.0;
			
		return simAvg;
	}
        
        
	/**
	 * Returns the similarity of two given genes or diseases.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	private double robinsonSimilarityAND(ByteString g1, ByteString g2)
	{		
            Integer intg1 = this.gene2index.get(g1);
            Integer intg2 = this.gene2index.get(g2);
            
            if(intg1!=null && intg2!=null)
                return robinsonSimilarityAND(intg1, intg2);
            else
                return 0;
            
                               
	}
        
        /**
	 * Returns the similarity of two given genes or diseases.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	public double phenumaSimilarity(ByteString g1, ByteString g2)
	{		
            Integer intg1 = this.gene2index.get(g1);
            Integer intg2 = this.gene2index.get(g2);
            
            if(intg1!=null && intg2!=null)
                return phenumaSimilarity(intg1, intg2);
            else
                return 0;
	}
	
        
        private double phenumaSimilarity(int g1, int g2)
	{
		double simAvg, max = 0.0;
		
		if (g1 < 0 || g2 < 0) return 0;

		TermID [] tl1 = (TermID[])associations[g1]; 
		TermID [] tl2 = (TermID[])associations[g2];
		
		/* TODO: Research if we can employ sorting omit some or many of
		 * the pairs. 
		 */
                double subSumSim = 0.0;
                double sumSim = 0.0;
                
		for (TermID t1 : tl1)
		{
                    max=0.0;
                    for (TermID t2 : tl2)
                    {
                        double newSim = termSemanticSimilarity(t1,t2);
			subSumSim = subSumSim + newSim;
                    }
			
                    sumSim = sumSim+(subSumSim/tl2.length); //max(t1,tl2) + max(t2,tl2) + .... + max(tn,tl2)
		}
		
                if (tl1.length>0)	
			simAvg = sumSim/tl1.length;
		else
			simAvg = 0.0;
			
		return simAvg;
	}
        
         /**
         * Third Cuartile similarity.
         * 
         * Calculation of a semantic similarity based on the mean of the median.
         * 
         * NOTE: simMedian = avg( q3[sim (g1, g2)])
         * @param p1
         * @param p2
         * @return 
         */
        public double thirdQuartileSimilarity(int g1, int g2)
        {
            if (g1 < 0 || g2 < 0) return 0;

            double sim = 0.0;

            TermID [] tl1 = (TermID[])associations[g1]; 
            TermID [] tl2 = (TermID[])associations[g2];

            /**
             *  Calculate q3 mean from rigth to left
             */
            List<Double> orderedList = new ArrayList<Double>();

            for (TermID t1 : tl1)
            {
                double max = 0.0;
                for (TermID t2 : tl2)
                {
                        double newSim = resnik(t1,t2);
                        if (newSim>max) max = newSim;

                }

                 StatisticsUtils.insertInOrder(orderedList, new Double(max));
            }

            /*Calculate q3 median*/
            int q3_position = StatisticsUtils.thirdQuartilePosByOrderedList(orderedList);    
            double q3mean_RL = StatisticsUtils.mean(orderedList.subList(q3_position, orderedList.size()));


            /**
             *  Calculate q3 mean from left to right
             */
            orderedList = new ArrayList<Double>();

            for (TermID t1 : tl2)
            {
                double max = 0.0;
                for (TermID t2 : tl1)
                {
                        double newSim = resnik(t1,t2);
                        if (newSim>max) max = newSim;

                }

                StatisticsUtils.insertInOrder(orderedList, new Double(max));
            }


            /*Calculate q3 median*/
            q3_position = StatisticsUtils.thirdQuartilePosByOrderedList(orderedList);
            double q3mean_LR = StatisticsUtils.mean(orderedList.subList(q3_position, orderedList.size()));


            
            sim = q3mean_RL*0.5 + q3mean_LR*0.5; 

            return sim;
        }
	
        
        public double thirdQuartileSimilarity(ByteString g1, ByteString g2)
	{		
            Integer intg1 = this.gene2index.get(g1);
            Integer intg2 = this.gene2index.get(g2);
            
            if(intg1!=null && intg2!=null)
                return thirdQuartileSimilarity(intg1, intg2);
            else
                return 0;
	}
        
        
	/**
	 * UDT_JUN: Returns the symmetric similarity of a list of terms and a disease.
	 * sim(Q,D) = 1/2*sim(Q-->D)+1/2*sim(D-->Q)
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	public double simAvgSym(ArrayList<TermID> query, ByteString object)
	{
            
            double simAvgQD, simAvgDQ, sim = 0.0;

            Integer intd = this.gene2index.get(object);

            if (!(goAssociations.containsPP(object))) return 0;

            TermID[] object_terms = (TermID[])associations[intd];

            double sumSim = 0.0;
            //int i = 0;
            for (TermID tx : query)
            {
                    sim=0.0;

                    for (TermID ty : object_terms)
                    {
                            double newSim = termSemanticSimilarity(tx,ty);
                            if (newSim > sim) sim = newSim;
                    }
                    sumSim = sumSim+sim; //max(t1,tl2) + max(t2,tl2) + .... + max(tn,tl2)
            }

            simAvgQD = sumSim/query.size(); //simAvg(Q-->D)


            sumSim=0.0;
            for (TermID tx : object_terms)
            {
                    sim=0.0;
                    for (TermID ty : query)
                    {
                            //Max information content between t1 y t2.1, ..., t2.n
                            double newSim = termSemanticSimilarity(tx,ty);
                            if (newSim > sim) sim = newSim;	
                    }
                    sumSim = sumSim+sim; //max(t1,tl2) + max(t2,tl2) + .... + max(tn,tl2)
            }

            simAvgDQ = sumSim/object_terms.length;//simAvg(D-->Q)

            sim = Math.rint((simAvgQD/2 + simAvgDQ/2)*10000)/10000; //simAvg(Q-->D)/2 + simAvg(D-->Q)/2

            return sim;
	}
	
	
	
        
        /**
         * Jaccard index
         */
        public double jaccardSimilarity(int g1, int g2)
        {
            if (g1 < 0 || g2 < 0) return 0;


            //Directed associations to g1 and g2
            TermID [] tl1 = (TermID[])associations[g1]; 
            TermID [] tl2 = (TermID[])associations[g2];
            
            
            ArrayList<TermID> terms1 = new ArrayList<TermID>();
            for(TermID t : tl1)
            {
                terms1.add(t);
            }
            
            Ontology graph_terms1 = graph.getInducedGraph(terms1);
            ArrayList<Term> allterms_1 = graph_terms1.getTermsInTopologicalOrder();
            
            
            
            ArrayList<TermID> terms2 = new ArrayList<TermID>();
            for(TermID t : tl2)
            {
                terms2.add(t);
            }
            
            Ontology graph_terms2 = graph.getInducedGraph(terms2);
            ArrayList<Term> allterms_2 = graph_terms2.getTermsInTopologicalOrder();
            
            
            //Union
            ArrayList<Term> union = new ArrayList<Term>();
            for (Term t : allterms_2)
                union.add(t);

            
            for(Term t : allterms_1)
            {
                if(!union.contains(t))
                    union.add(t);
            }
            
            //Intersection
            ArrayList<Term> intersection = new ArrayList<Term>();
            
            for(Term t : allterms_1)
            {
                if(allterms_2.contains(t))
                    intersection.add(t);
            }
            
            
            
            double unionsize = union.size();
            double intersectionsize = intersection.size();
            double jaccard = intersectionsize/unionsize;

            return jaccard;
            
        }
        
        
        public double jaccardSimilarity(ByteString g1, ByteString g2)
	{		
            Integer intg1 = this.gene2index.get(g1);
            Integer intg2 = this.gene2index.get(g2);
            
            if(intg1!=null && intg2!=null)
                return jaccardSimilarity(intg1, intg2);
            else
                return 0;
	}
        
        
	/**
	 * Calculates the similarity of genes of the study set.
	 * 
	 * @param study
	 * @return
	 */
	/*public Network calculate(StudySet study)
	{
		return calculate(study,null);
	}*/
	
        
        protected float calculateSemanticSimilarity(int i1, int i2) {
            
            Phenuma phenomizer = Phenuma.getInstance();
            
            switch(phenomizer.getSemanticSimilarityMeasure())
            {
                case phenuma.constants.Constants.ROBINSON:              return  (float)Math.rint(robinsonSimilarity(i1,i2)*1000)/1000;
                    
                case phenuma.constants.Constants.ROBINSON_SYMMETRIC:    return  (float)Math.rint(robinsonSymmetricSimilarity(i1,i2)*1000)/1000;
                
                case phenuma.constants.Constants.RESNIK:                return  (float)Math.rint(resnikSimilarity(i1,i2)*1000)/1000;
                
                case phenuma.constants.Constants.PHENUMA:               return  (float)Math.rint(phenumaSimilarity(i1,i2)*1000)/1000;
                
                case phenuma.constants.Constants.PHENUMA_SYMMETRIC:     return  (float)Math.rint(phenumaSymmetricSimilarity(i1,i2)*1000)/1000;
                
                case phenuma.constants.Constants.ROBINSON_AND:               return  (float)Math.rint(robinsonSimilarityAND(i1,i2)*1000)/1000;

                case phenuma.constants.Constants.ROBINSON_AND_SYMMETRIC:     return  (float)Math.rint(robinsonSymmetricSimilarityAND(i1,i2)*1000)/1000;
                    
                case phenuma.constants.Constants.MEDIAN_SS:             return   (float)0.0;  
                  
                case phenuma.constants.Constants.MEDIAN_SS_SYMMETRIC:           return   (float)0.0;
                    
                case phenuma.constants.Constants.THIRD_QUARTILE_SS:             return   (float)0.0;
                    
                case phenuma.constants.Constants.THIRD_QUARTILE_SS_SYMMETRIC:   return   (float)Math.rint(thirdQuartileSimilarity(i1,i2)*1000)/1000;
                    
                case phenuma.constants.Constants.MODE_SS:                       return   (float)0.0;
                    
                case phenuma.constants.Constants.MODE_SS_SYMMETRIC:             return   (float)0.0;
                    
                default:       
                    return (float)0.0;
            }
            
        }
	
        
        /**
         * Calculate semantic similarity. This method check which is the 
         * active measure. The measure setted is stored in PhenomizerAux object.
         * 
         * @param g1
         * @param g2
         * @return 
         */
        public float calculateSemanticSimilarity(ByteString g1, ByteString g2)
	{		
            Integer i1 = this.gene2index.get(g1);
            Integer i2 = this.gene2index.get(g2);
            
            if(i1!=null && i2!=null)
            {
                Phenuma phenomizer = Phenuma.getInstance();

                switch(phenomizer.getSemanticSimilarityMeasure())
                {
                    case phenuma.constants.Constants.ROBINSON:              return  (float)Math.rint(robinsonSimilarity(i1,i2)*1000)/1000;

                    case phenuma.constants.Constants.ROBINSON_SYMMETRIC:    return  (float)Math.rint(robinsonSymmetricSimilarity(i1,i2)*1000)/1000;

                    case phenuma.constants.Constants.RESNIK:                return  (float)Math.rint(resnikSimilarity(i1,i2)*1000)/1000;

                    case phenuma.constants.Constants.PHENUMA:               return  (float)Math.rint(phenumaSimilarity(i1,i2)*1000)/1000;

                    case phenuma.constants.Constants.PHENUMA_SYMMETRIC:     return  (float)Math.rint(phenumaSymmetricSimilarity(i1,i2)*1000)/1000;
                    
                    case phenuma.constants.Constants.ROBINSON_AND:               return  (float)Math.rint(robinsonSimilarityAND(i1,i2)*1000)/1000;

                    case phenuma.constants.Constants.ROBINSON_AND_SYMMETRIC:     return  (float)Math.rint(robinsonSymmetricSimilarityAND(i1,i2)*1000)/1000;

                    case phenuma.constants.Constants.MEDIAN_SS:             return   (float)0.0;  

                    case phenuma.constants.Constants.MEDIAN_SS_SYMMETRIC:           return   (float)0.0;

                    case phenuma.constants.Constants.THIRD_QUARTILE_SS:             return   (float)0.0;

                    case phenuma.constants.Constants.THIRD_QUARTILE_SS_SYMMETRIC:   return   (float)Math.rint(this.thirdQuartileSimilarity(i1,i2)*1000)/1000;
                    
                 //   case phenuma.constants.Constants.FIRST_QUARTILE_SS_SYMMETRIC:   return   (float)Math.rint(this.firstQuartileSimilarity(i1,i2)*1000)/1000;

                    case phenuma.constants.Constants.MODE_SS:                       return   (float)0.0;

                    case phenuma.constants.Constants.MODE_SS_SYMMETRIC:             return   (float)0.0;

                    default:       
                        return (float)0.0;
                }
            }
            
            return (float)0.0;
            
        }
	

	class WorkerThread extends Thread
	{


            class Message {};
            class BeginWorkMessage extends Message{};
            class FinishMessage extends Message{};

            /** Where the thread is put in when it is unemployed */
            private BlockingQueue<WorkerThread> unemployedQueue;

            private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();

            private int addPairCount;
            private int pairsDone;

            /** Array of entries that need to be processed by this thread. "work" contains index of object
                annotated. The indexes are stored in the gene2index map. Each index is the position of 
                each element in the set allObjectStudy.
             */
            
            private int [] work;

            private int WORK_LENGTH = 4000;
            
            private Network network;


            /**
             * 
             * @param unemployedQueue defines the queue in which the threads adds itself when a job has been finished.
             * @param mat the result matrix
             * @param indices matrix coordinates to coordinates used by the sim() method. 
             */
            //NOTA: he quitado indice[] de la entrada.
            public WorkerThread(BlockingQueue<WorkerThread> unemployedQueue, Network network)
            {
                WORK_LENGTH = totalAnnotated*2;
                work = new int[totalAnnotated*2];
                this.unemployedQueue = unemployedQueue;
                this.network = network;

            }

            @Override
            public void run()
            {


                try {
                     //System.out.println("Running trhead "+this.threadnumber);
                    unemployedQueue.put(this);

                    while (true)
                    {

                        Message msg =  messageQueue.take();

                        if (msg instanceof BeginWorkMessage)
                        {

                            for (int i=0;i<addPairCount;i+=2)
                            {
                                    /**
                                     * Each integer stored in "work" represent a object of the ontology.   
                                     */
                                    int i1 = work[i];
                                    int i2 = work[i+1];

                                    /**
                                     * Calculate semantic similarity between each pair of object stored in "work" and add the result to the network
                                     */
                                    if (i1 >= 0 || i2 >=0){
                                        
                                         double score = calculateSemanticSimilarity(i1, i2);
                                        // addItem2Network(network,i1,i2,score);
                                         
                                    }


                            }

                            pairsDone = addPairCount / 2;
                            addPairCount = 0;
                            unemployedQueue.put(this);
                        } else
                        {
                                if (msg instanceof FinishMessage)
                                        break;
                        }
                    }
                }
                catch (InterruptedException e)
                {
                }
            }

            /**
             * 
             * @param g1
             * @param g2
             * @return whether there is more place in the queue.
             * 
             */
            public boolean addPairForWork(int g1, int g2)
            {
                    work[addPairCount] = g1;
                    work[addPairCount+1] = g2;
                    addPairCount += 2;
                    return addPairCount < WORK_LENGTH;
            }

            /**
             * Start working on the feeded pairs.
             * @param unemployedQueue 
             * @throws InterruptedException 
             */
            public void fire() throws InterruptedException
            {
                    messageQueue.put(new BeginWorkMessage());
            }

            public int getPairsDone()
            {
                    return pairsDone;
            }

            public void finish() throws InterruptedException
            {
                    messageQueue.put(new FinishMessage());
            }
            
	}

	
//	public Network calculate(StudySet study)
//	{
//            //Create empty result matrix
//           // SemanticResult sr = new SemanticResult();
//           
//
//            /** Get phenomizer object. We use PhenmizerAux because there is a problem
//                with Phenomizer. The semantic similarity measure is not setted properly and
//                it always use Resnik.*/
//            Phenuma phenomizer = Phenuma.getInstance();
//
//            System.out.println("Calculating matrix using "+phenomizer.getSemanticSimilarityName(phenomizer.getSemanticSimilarityMeasure())+" measure."); 
//
//            Network network = new Network(NetworkConstants.SYMMETRIC);
//
//
//            if (numberOfProcessors > 1)
//            {
//                try
//                {
//                    // Create and start the worker threads and put them in the queue 
//                    WorkerThread [] wt = new WorkerThread[numberOfProcessors];
//                    BlockingQueue<WorkerThread> unemployedQueue = new LinkedBlockingQueue<WorkerThread>();		
//
//                        
//                    for (int j=0;j<numberOfProcessors;j++)
//                    {
//                            wt[j] = new WorkerThread(unemployedQueue,network);
//                            wt[j].start();
//                    }
//
//                    // Take first unemployed thread 
//                    WorkerThread currentWorker = unemployedQueue.take();
//
//                    for (int i=0;i<study.getSize();i++)
//                    {
//                        for (int j=0;j<this.totalAnnotated;j++)
//                        {
//                            //addPairForWork == false if there is not space to add the new work. addPairCount < WORK_LENGTH
//                            //INFO: se ha sustituido la entrada el addPair de i,j por indices(i) e indices(j).
//                            //para trabajar directamente con los identificadores de los objetos en lugar
//                            //de con su posición en el array.
//                            
//                            int object1index = this.gene2index.get(study.getAllElements()[i]);
//                            int object2index = this.gene2index.get(this.allObjectStudy.getAllElements()[j]);
//                            
//                            if(object1index!=object2index){
//                                if (!currentWorker.addPairForWork(object1index, object2index))
//                                {
//                                        //Put a BeginWork Message in the message queue.
//                                        currentWorker.fire();
//
//                                        // Take next unemployed thread (may wait if there is no unemployed thread left) 
//                                        currentWorker = unemployedQueue.take();
//                                        currentWorker.pairsDone = 0;
//
//                                }
//                            }
//
//                        }
//                        
//                    }
//
//
//                    for (int j=0;j<numberOfProcessors;j++)
//                    {
//                            wt[j].finish();
//                            wt[j].join();
//                    }
//
//                } catch(InterruptedException e)
//                {
//                        e.printStackTrace();
//                }
//
//            } 
//            else
//            {
//                // Single threaded 
//                   for (int i=0;i<study.getSize();i++)
//                    {
//                        for (int j=0;j<this.totalAnnotated;j++)
//                        {
//                            int object1index = this.gene2index.get(study.getAllElements()[i]);
//                            int object2index = this.gene2index.get(this.allObjectStudy.getAllElements()[j]);
//                            
//                            double score = this.calculateSemanticSimilarity(object1index,object2index);
//                            addItem2Network(network,object2index,object2index,score);
//                        }
//
//                }
//
//            }
//
//            return network;
//
//    }
        
//    /**
//         * Add edge to the network. Check the type of node.
//         * @param network
//         * @param studySetIndex
//         * @param allAnnotatedIndex
//         * @param score 
//         */
//    public void addItem2Network(Network network, int studySetIndex, int allAnnotatedIndex, double score, int relationship_type){
//        
//        ByteString studySetName = this.allObjectStudy.getAllElements()[studySetIndex];
//        ByteString allAnnotatedName = this.allObjectStudy.getAllElements()[allAnnotatedIndex];
//        
//        Node n1 = new Node(studySetName);
//        Node n2 = new Node(allAnnotatedName.toString());
//        
//        /**
//         * Add edge to the network
//         */
//        if(n1!=null && n2!=null){
//            Edge edge = new Edge(n1,n2,relationship_type);
//            edge.setScore(score);
//            network.add(edge);
//        }
//        
//    }    
  
    
//    
//    public void calculateMatrix() throws FileNotFoundException
//    {
//        //Create empty result matrix
//        PrintWriter out = new PrintWriter(new File("./resources/out/matrixgeneinfo.txt"));
//        
//        //SemanticResult sr = new SemanticResult();
//            
//        int entries = this.allObjectStudy.getSize(); 
//
//        float [][] mat =  new float[entries][entries];
//            
//        HashSet<ByteString> genes = this.allObjectStudy.getAllGeneNames();
//       
//        int i = 0;
//        int j = 0;
//        
//        Iterator<ByteString> iterX = genes.iterator();
//        while(iterX.hasNext()){
//            
//            ByteString geneX = iterX.next();
//            
//            int geneXID = this.gene2index.get(geneX);
//            TermID[] termsX = (TermID[]) associations[geneXID]; 
//
//     //       TermID[] listgeneX = associations[this.gene2index.(geneX)];
//                    
//            Iterator<ByteString> iterY = genes.iterator();
//            j=0;
//            while(iterY.hasNext()){
//                
//                ByteString geneY = iterY.next();
//                
//                mat[i][j] = (float)this.robinsonSymmetricSimilarity(geneX, geneY);
//                
//                int geneYID = this.gene2index.get(geneY);
//                TermID [] termsY = (TermID[])associations[geneYID];
//                
//                if(mat[i][j]>0)
//                    out.println(geneX+"\t"+geneY+"\t"+NumberUtils.round(mat[i][j],4));
//                
//                j++;
//            }
//            
//            System.out.println(i+" de "+entries);
//            
//            i++;
//        }
//        
//        out.close();
//        
////        sr.mat = mat;
//        sr.names = this.allObjectStudy.getAllGeneNames();
//        sr.name = this.allObjectStudy.getName();
//        sr.assoc = goAssociations;
//
//        sr.g = graph;
//        sr.calculation = this;
        
       // return sr;
        
//    }


    public double robinsonSymmetricSimilarity(ByteString g1, ByteString g2){
            double simQD = robinsonSimilarity(g1,g2);            
            double simDQ = robinsonSimilarity(g2,g1);


            return  (simQD+simDQ)/2;
    }


    public double robinsonSymmetricSimilarity(int g1, int g2){
            double simQD = robinsonSimilarity(g1,g2);
            double simDQ = robinsonSimilarity(g2,g1);
            

            return  (simQD+simDQ)/2;
    }

    public double robinsonSymmetricSimilarityAND(ByteString g1, ByteString g2){
            double simQD = robinsonSimilarityAND(g1,g2);            
            double simDQ = robinsonSimilarityAND(g2,g1);


            return  (simQD+simDQ)/2;
    }


    public double robinsonSymmetricSimilarityAND(int g1, int g2){
            double simQD = robinsonSimilarityAND(g1,g2);
            double simDQ = robinsonSimilarityAND(g2,g1);
            

            return  (simQD+simDQ)/2;
    }
    public double phenumaSymmetricSimilarity(ByteString g1, ByteString g2){
            double simQD = phenumaSimilarity(g1,g2);
            double simDQ = phenumaSimilarity(g2,g1);

            return (simQD+simDQ)/2;
    }


    public double phenumaSymmetricSimilarity(int g1, int g2){
            double simQD = phenumaSimilarity(g1,g2);
            double simDQ = phenumaSimilarity(g2,g1);

            return (simQD+simDQ)/2;
    }


}
