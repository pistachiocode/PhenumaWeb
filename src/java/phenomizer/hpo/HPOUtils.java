package phenomizer.hpo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HPOUtils {
	
	public static final String HPOpattern = "HP:[0-9]{7}";
	
	public static boolean isHPOterm (String str){
            Pattern p = Pattern.compile(HPOpattern);
		
	    Matcher m = p.matcher(str);
            return m.find();
	}
        
        
        public static String getHPOterm (String str){
            
            String hpoid = null;
            
            Pattern p = Pattern.compile(HPOpattern);
		
	    Matcher m = p.matcher(str);
            if (m.find()) {
                hpoid =  m.group(0);
            }
            
            return hpoid;
	}
        
        public static void main (String[] args)
        {
            System.out.println(isHPOterm("Breast hypoplasia (HP:0003187)"));
        }

}
