package ontologizer.query;

import ontologizer.types.ByteString;

public class PhenomizerResult implements Comparable<PhenomizerResult> {
	double score;
	double pvalue;
	double adjust_pvalue;
	//disease
	ByteString omim;
	ByteString name;
	
	
	public PhenomizerResult(double score, ByteString omim, ByteString name){
		this.score = score;
		this.omim = omim;
		this.name = name;
	}


	public double getScore() {
		return score;
	}


	public void setScore(double score) {
		this.score = score;
	}


	public ByteString getOmim() {
		return omim;
	}


	public void setOmim(ByteString omim) {
		this.omim = omim;
	}


	public ByteString getName() {
		return name;
	}


	public void setName(ByteString name) {
		this.name = name;
	}


	public double getPvalue() {
		return pvalue;
	}


	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}


	public double getAdjust_pvalue() {
		return adjust_pvalue;
	}


	public void setAdjust_pvalue(double adjust_pvalue) {
		this.adjust_pvalue = adjust_pvalue;
	}


	public int compareTo(PhenomizerResult r){
		if (this.score < r.score)
			return -1;
		else if (this.score == r.score)
			return 0;
		else 
			return 1;
	}
        
        
        public String toString() {
            
           return this.pvalue + "\t" + this.adjust_pvalue + "\t" + this.score + "\t" + this.omim + "\t" + this.name;
            
        }
	
	
	
	
}
