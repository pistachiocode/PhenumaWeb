package phenomizer.database;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Get pvalue and adjustments for a score, disease omim and querysize
 * 
 * @author Rocio
 *
 */

public class DBPValue {
	
	private double pvalue;
	private double bonferroni_adjust;
	private double benjamini_adjust;
	
	//aux
	private String disease;
	private int querysize;
	private double score;
	
	
	/**
	 * Create a database query to get the pvalue and its adjustments.
	 * 
	 * @param disease
	 * @param querysize
	 * @param score
	 */
	public DBPValue(String disease, int querysize, double score){
		
		this.disease = disease;
                this.querysize = querysize;
                
                /**
                 * If the the query size is greater than Max_querysize, we set the querysize with the maximum value allowed.
                 */
                if(querysize >= phenuma.constants.Constants.MAX_QUERYSIZE)
                    this.querysize=phenuma.constants.Constants.MAX_QUERYSIZE;
                
		this.score = score;
                
		if(querysize > 0)
                    this.queryPvalue();
		
	}
	

	/**
	 * Get the associated p-value to a score, disease and querysize.
	 */
	public void queryPvalue(){
		DBUtils.connect();
		//execute query
		
		ResultSet respvalue = null;
                
                //Get the score value greater or equal to "score" realated with the disease.
                ResultSet resscore = DBUtils.execute(buildSqlMinScore());
		 
                try {
                     
			//if there is not a score greater than the input score get pvalue of the the maximum score stored.
			if(!resscore.next() || resscore.getDouble(phenuma.constants.Constants.SCORE_COL)==0.0) {
                            
                                //Get max score stored for the disease
                                ResultSet resmaxscore = DBUtils.execute(buildSqlMaxScore());
                                
                                //If there is max score get pvalue and the adjustments.
                                if (resmaxscore.next())
                                    respvalue = DBUtils.execute(buildSqlMax(resmaxscore.getDouble(phenuma.constants.Constants.SCORE_COL)));
                                
                        } else {
                            //move the cursor to the first row
                            resscore.first();
                            double score = resscore.getDouble(phenuma.constants.Constants.SCORE_COL);
                            respvalue = DBUtils.execute(buildSqlMin(score));
			}
			
                        
                        
                        if (respvalue.first()){
                            pvalue = respvalue.getDouble(phenuma.constants.Constants.PVALUE_COL);
                            bonferroni_adjust = respvalue.getDouble(phenuma.constants.Constants.ADJUSTMENT_BON_COL);
                            benjamini_adjust = respvalue.getDouble(phenuma.constants.Constants.ADJUSTMENT_BH_COL);
                        }
                        
                        resscore.close();
                        respvalue.close();
                        
                        
			DBUtils.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Create a string with a sql query to get the pvalues, adjust_bon, adjust_bh
	 * 
	 * @return sql query
	 */
	private String buildSqlMin(double minscore){
		//build query
		String sql = "select "+phenuma.constants.Constants.ADJUSTMENT_BON_COL+", "+phenuma.constants.Constants.ADJUSTMENT_BH_COL+", "+phenuma.constants.Constants.PVALUE_COL +
		 			 " from "+phenuma.constants.Constants.DATABASE+"."+phenuma.constants.Constants.DISTRIBUTION_TB +querysize+
		 			 " where disease = "+disease+
		 			 " and score = "+minscore;
                return sql;
		
	}
        
        
	
	/**
	 * Create a string with a sql query to get the pvalues, adjust_bon, adjust_bh
	 * 
	 * @return sql query
	 */
	private String buildSqlMax(double maxscore){
		//build query
		String sql = "select "+phenuma.constants.Constants.ADJUSTMENT_BON_COL+", "+phenuma.constants.Constants.ADJUSTMENT_BH_COL+", "+phenuma.constants.Constants.PVALUE_COL +
		 			 " from "+phenuma.constants.Constants.DATABASE+"."+phenuma.constants.Constants.DISTRIBUTION_TB +querysize+
		 			 " where disease = "+disease+
		 			 " and score ="+ maxscore;
		return sql;
		
	}
        
        
        /**
         * Create string with a sql query to get the max score
         * @return 
         */
        private String buildSqlMaxScore(){
		//build query
		String sql = "select max(score) 'score' from "+phenuma.constants.Constants.DATABASE+"."+phenuma.constants.Constants.DISTRIBUTION_TB+querysize
                            +" where disease = "+disease;
		
		return sql;	
	}
        
                /**
         * Create string with a sql query to get the max score
         * @return 
         */
        private String buildSqlMinScore(){
		//build query
		String sql = "select min(score) 'score' from "+phenuma.constants.Constants.DATABASE+"."+phenuma.constants.Constants.DISTRIBUTION_TB+querysize
                            +" where disease = "+disease+" and score >="+score;
		
		return sql;	
	}
        
	

	public double getPvalue() {
		return pvalue;
	}

	public void setP(double pvalue) {
		this.pvalue = pvalue;
	}

	public double getBonferroni_adjust() {
		return bonferroni_adjust;
	}

	public void setBonferroni_adjust(double bonferroni_adjust) {
		this.bonferroni_adjust = bonferroni_adjust;
	}

	public double getBenjamini_adjust() {
		return benjamini_adjust;
	}

	public void setBenjamini_adjust(double benjamini_adjust) {
		this.benjamini_adjust = benjamini_adjust;
	}
	
	
	
	/**
	 * Test
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{

		DBPValue dbp = new DBPValue("129500", 10, 1.7);
		System.out.println("pvalue: "+dbp.getPvalue());
		System.out.println("adjust_bon: "+dbp.getBonferroni_adjust());
		System.out.println("adjust_bh: "+dbp.getBenjamini_adjust());
	}
	
	

}
