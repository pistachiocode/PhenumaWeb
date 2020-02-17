package phenomizer.utils;

public class NumberUtils {

	/**
	 * Apply a roundFactor to a decimal.
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static double round(double number, int decimals){
		int roundFactor = (int) Math.pow(10, decimals);
		
		return Math.rint(number*roundFactor)/roundFactor;	
	}
	
	
	/**
	 * Apply a roundFactor to a decimal.
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static float round(float number, int decimals){
		int roundFactor = (int) Math.pow(10, decimals);

		return (float)(Math.rint(number*roundFactor)/roundFactor);
	}
        
        
        /**
         * Convert a object (Double or Integer) to String
         * 
         * @param object 
         */
        public static String object2string(Object object) {
            String valuestr = "";

            try{
                Double dvalue = (Double)object;
                valuestr = dvalue.toString();
            }
            catch (Exception ex)
            {
                try{
                    Integer ivalue = (Integer)object;
                    valuestr = ivalue.toString();
                }
                catch(Exception ex2)
                {
                    valuestr = "";
                }
            }
            
            return valuestr;
        }
}
