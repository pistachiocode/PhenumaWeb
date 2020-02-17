package phenomizer.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import ontologizer.go.Prefix;
import ontologizer.go.TermID;
import ontologizer.types.ByteString;
import phenuma.entities.Disease;
import phenuma.entities.Gene;
import phenuma.entities.RareDisease;

public class StringUtils {

	/**
	 * Calculate the length of the most long string.
	 * @param String[] strings
	 * @return
	 */
	public static int maxLenght(String[] strings){
		int maxlength = 0;
		int i = strings.length-1;
		
		while(i>=0){
			if (strings[i].length()>maxlength)
				maxlength = strings[i].length();
			
			i--;
		}
		
		return maxlength;
		
	}
	
	
	/**
	 * Byte String utils
	 */
	
	public static String[] byteStringToString(ByteString[] str){
		String[] res = new String[str.length];
		int i = str.length-1;
		
		while(i >= 0){
			res[i] = str[i].toString();
			i--;
		}
		
		return res;
		
	}
        
        
        
	/**
	 * Byte String to String array utils
	 */
	
        public static String[] byteStringSetToString(Set<ByteString> set){
		String[] res = new String[set.size()];
                int i = 0;

                Iterator<ByteString> iter = set.iterator();
                while(iter.hasNext()){
                    res[i] = iter.next().toString();
                    i++;
                }

                return res;

	}
        
        /**
         * String list to array
         */
        public static String[] stringListToArray(List<String> list){
            String[] res = new String[list.size()];
            int i = 0;
            
            Iterator<String> iter = list.iterator();
            while(iter.hasNext()){
                res[i] = iter.next();
                i++;
            }
            
            return res;
        }
        
        
        /**
         * String set to array
         */
        public static String[] stringSetToArray(Set<String> set){
            String[] res = new String[set.size()];
            int i = 0;
            
            Iterator<String> iter = set.iterator();
            while(iter.hasNext()){
                res[i] = iter.next();
                i++;
            }
            
            return res;
        }
        
        
         /**
         * String set to array
         */
        public static String[] integerSetToArray(Set<Integer> set){
            String[] res = new String[set.size()];
            int i = 0;
            
            for(Integer integer : set){
                res[i] = integer.toString();
                i++;
            }
            
            return res;
        }
        
        /**
         * list to array
         */
        public static Object[] listToArray(List<Object> list){
            Object[] res = new Object[list.size()];
            int i = 0;
            
            Iterator<Object> iter = list.iterator();
            while(iter.hasNext()){
                res[i] = iter.next();
                i++;
            }
            
            return res;
        }
        
        /**
         * Array of String to TermID array
         * @param value
         * @return 
         */

        public static TermID[] stringArrayToTermArray(String[] value) {
            TermID[] term = new TermID[value.length];

            for(int i=0; i<value.length; i++){
                term[i] = new TermID(value[i]);
                i++;
            }

            return term;
        }
        
        
        /**
         * List<Integer> to String[]
         */
        public static String[] integerListToStringArray(List<Integer> list){
            String[] res = new String[list.size()];
            int i = 0;
            
            Iterator<Integer> iter = list.iterator();
            while(iter.hasNext()){
                res[i] = iter.next().toString();
                i++;
            }
            
            return res;
        }
        
        /**
         * List<Disease> to String[] with the omim id of each disease.
         * 
         * @param diseases
         * @return 
         */
        public static String[] diseaseListToStringArray(List<Disease> diseases){
            
            String[] res = new String[diseases.size()];
            int i = 0;
            
            Iterator<Disease> iter = diseases.iterator();
            while(iter.hasNext())
            {
                String omimstr = iter.next().getOmim().toString();
                res[i] = omimstr;
                
                i++;
            }
            
            return res;
            
        }
        
        
        /**
         * List<RareDisease> to String[] with the orphanum id of each rare disease.
         * 
         * @param rare disease
         * @return 
         */
        public static String[] rareDiseaseListToStringArray(List<RareDisease> rarediseases){
            
            String[] res = new String[rarediseases.size()];
            int i = 0;
            
            Iterator<RareDisease> iter = rarediseases.iterator();
            while(iter.hasNext())
            {
                String omimstr = iter.next().getOrphanum().toString();
                res[i] = omimstr;
                
                i++;
            }
            
            return res;
            
        }
        

        /**
         * List<Gene> to String[] with the entreiz id of each disease.
         * 
         * @param genes
         * @return 
         */
        public static String[] geneListToStringArray(List<Gene> genes){
            
            String[] res = new String[genes.size()];
            int i = 0;
            
            Iterator<Gene> iter = genes.iterator();
            
            while(iter.hasNext())
            {
                Gene g = iter.next();
                
                if(g!=null)
                {
                    String entrezidstr = g.getEntrezid().toString();
                    
                    res[i] = entrezidstr;
                
                    i++;
                }
            }
            
            return res;
            
        }
        
        /**
         * List<String> to List<TermID> 
         * 
         * @param List<String> termList
         * @return 
         */
        public static ArrayList<TermID> stringListToTermIDList(ArrayList<String> termList){
            
            ArrayList<TermID> res = new ArrayList<TermID>();
            
            Iterator<String> iter = termList.iterator();
            while(iter.hasNext())
            {
                TermID term = new TermID(iter.next());
                res.add(term);
                
            }
            
            return res;
            
        }
        
        
        /**
         * 
         */
        public static String termArrayToString(TermID[] terms){
            
            String res = "";
            int i = 0;
            
            for(TermID t : terms){
                res = res + t.toString();
                
                if(i!=(terms.length-1))
                    res = res+",";
                i++;
                
            }
            
            
            return res;
            
        }
        
        /**
         * This method creates a random String of an input length 
         * 
         * @param length
         * @return 
         */
        public static String randomString(int length)
        {
            String randomString = "";
            
            long millis = System.currentTimeMillis();
            
            Random r = new Random(millis);
            int i = 0;
            while ( i < length)
            {
                char c = (char)r.nextInt(255);
                
                if ( (c >= '0' && c <= '9') || (c >='A' && c <='Z') ){
                    randomString += c;
                    i ++;
                }
            }
            return randomString;
            
        }
        
        
        /*
         * Save to file
         */
        public static void saveToFile(String file, String content) throws IOException{

            PrintWriter pw = new PrintWriter(new FileWriter(file));

            pw.println(content);

            pw.close();

        }
    
}
