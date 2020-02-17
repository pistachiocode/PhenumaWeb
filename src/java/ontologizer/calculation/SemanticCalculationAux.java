/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontologizer.calculation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologizer.association.AssociationContainer;
import ontologizer.go.OBOParserException;
import ontologizer.go.Ontology;
import ontologizer.go.TermID;
import ontologizer.types.ByteString;
import phenomizer.hpo.Phenuma;
import phenomizer.utils.StatisticsUtils;

/**
 *
 * @author Armando
 */
public class SemanticCalculationAux extends SemanticCalculation{
    
    //Ontology g2;
   // AssociationContainer assoc2;

    public SemanticCalculationAux(Ontology ontology, AssociationContainer assocs) {
        super(ontology, assocs);
        //this.assoc2 = assoc2;
    }
    
    
    /**
     * Returns the shared information content of two given terms
     *  
     * @param t1
     * @param t2
     * @return
     */
    public TermID p_aux(TermID t1, TermID t2)
    {
        /*NOTE: sharedParents(t1,t2) = ComAnc(t1, t2) Common Ancestors */
        Collection<TermID> sharedParents = graph.getSharedParents(t1, t2);
        double p = 1.0;
        
        TermID mostInformativeTerm = null;

        /*NOTE: Calculate p for each common ancestor and return the minimum */
        /* The information content of two terms is defined as the minimum of
         * the information content of the shared parents.
         */
        for (TermID t : sharedParents)
        {
            double newP = probability(t);
            if (newP < p){ 
                p = newP;
                mostInformativeTerm = t;
            } 
        }
        
        return mostInformativeTerm;
    }
    
    

    
    /**
     * Median similarity.
     * 
     * Calculation of a semantic similarity based on the mean of the median.
     * 
     * NOTE: simMedian = avg( median[sim (g1, g2)])
     * @param p1
     * @param p2
     * @return 
     */
    public double medianSimilarity(ByteString p1, ByteString p2)
    {
        double sim = 0.0;

        if (!(goAssociations.containsPP(p1))) return 0;
        if (!(goAssociations.containsPP(p2))) return 0;

        List<TermID> tl1 = goAssociations.get(p1).getAssociations();
        List<TermID> tl2 = goAssociations.get(p2).getAssociations();
        
        /*List of ordered scores.*/
        List<Double> orderedArray = new ArrayList<Double>();

        

        for (TermID t1 : tl1)
        {
            for (TermID t2 : tl2)
            {
                    double newSim = resnik(t1,t2);
                    insertInOrder(orderedArray, new Double(newSim));
            }
            /*Calculate median*/
            Double d = StatisticsUtils.medianByOrderedList(orderedArray);
            sim = sim + d.doubleValue(); 
        }
        


        if (tl1.size()>0)
            sim = sim/tl1.size();
        else
            sim = 0.0;

        return sim;
    }
    
    

    
    
    /**
     * Returns the similarity of two given phenotypic profiles.
     * 
     * NOTE: simMedian = avg( median[sim (g1, g2)])
     * @param index of gene2
     * @return
     */
    public double medianSimilarity(int g1, int g2)
    {
        double sim = 0;

        if (g1 < 0 || g2 < 0) return 0;

        TermID [] tl1 = (TermID[])associations[g1]; 
        TermID [] tl2 = (TermID[])associations[g2];

        /* TODO Robinson: Research if we can employ sorting omit some or many of
         * the pairs. 
         */

        /*List of ordered scores.*/
        List<Double> orderedArray = new ArrayList<Double>();

        double sumSim = 0.0;

        for (TermID t1 : tl1)
        {
            for (TermID t2 : tl2)
            {
                    double newSim = resnik(t1,t2); //NOTE: -log(IC(t1,t2))
                    insertInOrder(orderedArray, new Double(newSim));
            }

            /*Calculate median*/
            Double d = StatisticsUtils.medianByOrderedList(orderedArray);
            sim = sim + d.doubleValue(); 

        }

        if (tl1.length>0)
            sim = sim/tl1.length;
        else
            sim = 0.0;

        return sim;
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
    public double thirdQuartileSimilarity(ByteString p1, ByteString p2)
    {
        double sim = 0.0;

        if (!(goAssociations.containsPP(p1))) return 0;
        if (!(goAssociations.containsPP(p2))) return 0;

        List<TermID> tl1 = goAssociations.get(p1).getAssociations();
        List<TermID> tl2 = goAssociations.get(p2).getAssociations();
        
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
            
            insertInOrder(orderedList, new Double(max));
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
            
            insertInOrder(orderedList, new Double(max));
        }

        
        /*Calculate q3 median*/
        q3_position = StatisticsUtils.thirdQuartilePosByOrderedList(orderedList);
        double q3mean_LR = StatisticsUtils.mean(orderedList.subList(q3_position, orderedList.size()));
        
        sim = q3mean_RL*0.5 + q3mean_LR*0.5; 

        return sim;
    }
    
    /*
     * Third Quartile
     * 
     * Calculation of a semantic similarity based on the mean of the third quartile.
     * 
     * NOTE: simMedian = avg( q3[sim (g1, g2)])
     * @param p1
     * @param p2
     * @return 
     */
    public double thirdQuartileSimilarity(int g1, int g2){
        
        double sim = 0;

        if (g1 < 0 || g2 < 0) return 0;

        TermID [] tl1 = (TermID[])associations[g1]; 
        TermID [] tl2 = (TermID[])associations[g2];

        /* TODO Robinson: Research if we can employ sorting omit some or many of
         * the pairs. 
         */

        /*List of ordered scores.*/
        List<Double> orderedList = new ArrayList<Double>();

        double sumSim = 0.0;

        for (TermID t1 : tl1)
        {
            for (TermID t2 : tl2)
            {
                    double newSim = resnik(t1,t2); //NOTE: -log(IC(t1,t2))
                    insertInOrder(orderedList, new Double(newSim));
            }

           /*Calculate median*/
            int q3_position = StatisticsUtils.thirdQuartilePosByOrderedList(orderedList);
            
            double q3mean = StatisticsUtils.mean(orderedList.subList(q3_position, orderedList.size()));
            sim = sim + q3mean; 

        }

        if (tl1.length>0)
            sim = sim/tl1.length;
        else
            sim = 0.0;

        return sim;
    }
    
    
    
    
  
    
    
    /**
     * Insert in order in a list of double
     * @param list
     * @param d 
     */
    private static void insertInOrder(List<Double> list, Double d)
    {
        int i = 0;
        
        if(!list.isEmpty())
        {
            Double e = list.get(i);
        
            while(i<list.size() && e.compareTo(d)<=0)
            {
                i++;
                if(i<list.size())
                    e = list.get(i);
            }
        }
        
        list.add(i, d);
        
    }
    
    
    /**
     * Insert in frecuency distribution
     * @param list
     * @param d 
     */
    private static void insertInFrecuenciesDistribution(Map<Double, Integer> distribution, Double d)
    {
        
        Integer frecuency = distribution.get(d);
        
        if(frecuency==null)
        {
            distribution.put(d, 1);
        }
        
        distribution.put(d, frecuency+1);
        
    }
    
    
    
    /**
     * Test insert in order
     * @param args 
     */
    public static void main(String[] args)
    {
        try {
            Phenuma p = Phenuma.getInstance();
            p.setResources_location("/Users/Armando/");
            
            AssociationContainer assoc = p.getDiseases_hpo_associations();
            
            Set<ByteString> omims = assoc.getAllAnnotatedPP();
            
            for(ByteString omim : omims)
            {
                
            }
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(SemanticCalculationAux.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OBOParserException ex) {
            Logger.getLogger(SemanticCalculationAux.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
