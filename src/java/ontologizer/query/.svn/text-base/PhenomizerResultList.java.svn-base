package ontologizer.query;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


//import ontologizer.association.Gene2Associations;
//import ontologizer.types.ByteString;

public class PhenomizerResultList {
	
	ArrayList<PhenomizerResult> resultList;

	public PhenomizerResultList(){
		resultList = new ArrayList<PhenomizerResult>();
	}

	public ArrayList<PhenomizerResult> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<PhenomizerResult> resultList) {
		this.resultList = resultList;
	}
	
	
	public void add(PhenomizerResult r){
			resultList.add(r);
	}
	

	public void writeToFile(File file){
	
		//this.resultList;
		try {
			PrintWriter out = new PrintWriter(file);
                        
			
			out.println("P-Value\tAdjust P-Value\tScore\tOmim\tName");
			for (PhenomizerResult r : resultList){	
				out.print(r.pvalue);
				out.print("\t");
				out.print(r.adjust_pvalue);
				out.print("\t");
				out.print(r.score);
				out.print("\t");
				out.print(r.omim);
				out.print("\t");
				out.print(r.name);
				out.print("\n");
			}
			
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
        
        
        public String toString(){
            String res = "";
            
            for(PhenomizerResult r : resultList)  {
            
                res = res + r.toString() +"\n";
            
            }
            
            return res;
        
        }
	
	public int size(){
		return resultList.size();
	}

	
}
