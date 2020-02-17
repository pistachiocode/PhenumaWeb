/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenomizer.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Armando
 */
public class StatisticsUtils {
    
    
    /*
     * Mean from a list 
     */
    public static Double mean(List<Double> list)
    {
        double sum = 0.0;
        for(Double d : list)
        {
            sum = sum + d;
        }
        
        return sum/list.size();
    }
    
  
    /**
     * Median calculation with a ordered list
     * @param list
     * @return 
     */
    public static Double medianByArray(double[] scores){

        if(scores.length == 0)
        {
            return new Double(0);
        }
        else if(scores.length %2==0)
        {
            int mpos1 = scores.length /2-1;
            int mpos2 = (scores.length +2)/2-1;

            return (scores[mpos1] + scores[mpos2])/2;
        }
        else
        {
            int mpos = (scores.length + 1)/2-1;
            return scores[mpos];
        }
    }
    
    /**
     * Median calculation with a ordered list
     * @param list
     * @return 
     */
    public static Double medianByOrderedList(List<Double> list){

        if(list.isEmpty())
        {
            return new Double(0);
        }
        else if(list.size()%2==0)
        {
            int mpos1 = list.size()/2-1;
            int mpos2 = (list.size()+2)/2-1;

            return (list.get(mpos1) + list.get(mpos2))/2;
        }
        else
        {
            int mpos = (list.size() + 1)/2-1;
            return list.get(mpos);
        }
    }
    
    /* TODO: Es poco probable que al calcular las similitudes semanticas
     * de un termino con los terminos de un perfil fenotipico haya una moda. Los
     * valores va a ser casi siempre distintos.
     * 
     * Si hay más de una moda... ¿?
     */  
    public static Double mode(List<Double> list){
        
        /*Build map value to frequency*/
        Map<Double, Integer> value2freq = new HashMap<Double, Integer>();
        
        for(int i=0; i<list.size(); i++)
        {
            Double value = list.get(i);
            
            if(value2freq.containsKey(value))
            {
                Integer freq = value2freq.get(value);
                
                value2freq.put(value, new Integer(freq.intValue()+1));
            }
            else
            {
                value2freq.put(value, new Integer(1));
            }
        }
        
        /*Get value most frequent.*/
        boolean existsMode = false;
        int maxfreq = 1;
        Double mode = null;
        
        Set<Double> values = value2freq.keySet();
        Iterator<Double> iter = values.iterator();
        while(iter.hasNext())
        {
            Double value = iter.next();
            if(value2freq.get(value)>maxfreq)
            {
                maxfreq = value2freq.get(value);
                mode = value;
                existsMode = true;
            }
        }
        
        return mode;
    }
    
    /**
     * Third quartile position by ordered list
     * @param list
     * @return 
     */
    public static int thirdQuartilePosByOrderedList(List<Double> list)
    {
      
        Double quartile = new Double(0);
        
        /*Empty list. Q3 is null*/ 
        if(list.isEmpty())
            return 0;
        
        /* Base cases */       
        if(list.size()==1)
            return 0;
        

            /*Calculate third quartile position*/
        double q3 = list.size()*0.75;
        int q3pos = (int)Math.floor(q3);

        
        return q3pos;
               
    }
    
    
        
    public static Double thirdQuartileByArray(double[] array)
    {
       Double quartile = new Double(0);
       
        if(array.length==0)
            return new Double(0);
        
        if(array.length==1)
            return array[0];
        
        Double median = medianByArray(array);
        
        int i = 0;
        while(i<array.length && array[i]<=median)
            i++;
        
        if(i<array.length){
            double[] subarray = Arrays.copyOfRange(array, i, array.length);
            System.out.println("Length "+subarray.length+" "+i+" a "+(array.length));
            quartile = medianByArray(subarray);
        }
        
        return quartile;
        
    }
    
    /**
     * Insert in order in a list of double
     * @param list
     * @param d 
     */
    public static void insertInOrder(List<Double> list, Double d)
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
    
    
    public static void main(String[] args)
    {
        for(int i = 0; i<=7 ; i++)
        {

            double n = i*0.75;
            System.out.println(i+" "+n+" "+(Math.floor(n)));

        }
    }
    
}
