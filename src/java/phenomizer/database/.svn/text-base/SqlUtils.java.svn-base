package phenomizer.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SqlUtils {

	public static final String insert = "INSERT INTO";
	
	/**
         * Create a instert sentence.
         * 
         * insert into tableName (field1, field2, ...) values (value1, value2, ...);
         * 
         * @param tableName
         * @param fields
         * @param values
         * @return 
         */
	public static String insertSQL(String tableName, String[] fields, String[] values){
		String res = "";
		
		
		String valuesStr = "";
		String fieldsStr = "";
		
		if (fields.length == values.length){
			valuesStr = "(";
			fieldsStr = "(";
			
			for(int i=0; i<values.length; i++){
				valuesStr = valuesStr + values[i];
				fieldsStr = fieldsStr + fields[i];
				
				if(i!=values.length-1){ //last position
					valuesStr = valuesStr +", ";
					fieldsStr = fieldsStr +", ";
				}
			}
			
			valuesStr = valuesStr +")";
			fieldsStr = fieldsStr +")";
		
		}
		
		//Build insert sentence
		
		res = insert + " " + tableName + " " + fieldsStr + " VALUES " + valuesStr +";";
		
		return res;
	}
	
	
        /**
         * Create a sql script from a Map<Object, Object>
         * 
         * "tableName" must be a table with two field. The first field is the key
         * the second one the value.
         * 
         * insert into tableName (field1, field2) values (key1, value1);
         * insert into tableName (field1, field2) values (key2, value2);
         * 
         * @param tableName
         * @param fields
         * @param map
         * @return 
         */
        public static String map2SQL(String tableName, String[] fieldsName, Map<String, String> map)
        {
            String script = "";
            
            Set<String> keyset = map.keySet();
            Iterator<String> iter = keyset.iterator();

            
            /*For each orphanum get omim list*/
            while(iter.hasNext())
            {
                String key = iter.next();
                String value = map.get(key);
               
                String[] values = {key, value};

                String insertSQL = SqlUtils.insertSQL(tableName, fieldsName, values);

                script = script + "\n" +insertSQL;

            }
            
            return script;
        
        }

        /**
         * Create a sql script from a Map<Object, Collection<Object>>
         * 
         * "tableName" must be a table with two field. The first field is the key
         * the second one the value.
         *
         * insert into tableName (field1, field2) values (key1, value1);
         * insert into tableName (field1, field2) values (key2, value2);
         * 
         * @param tableName
         * @param fields
         * @param map
         * @return 
         */
        public static String mapCollection2SQL(String tableName, String[] fieldsName, Map<String, Collection<String>> map)
        {
            String script = "";
            
            Set<String> keyset = map.keySet();
            Iterator<String> iter = keyset.iterator();

            
            /*For each orphanum get omim list*/
            while(iter.hasNext())
            {
                String key = iter.next();
                Collection<String> value = map.get(key);
                
                
                /*
                 * Collection of values
                 */
                Iterator<String> iterValue = value.iterator();
                while(iterValue.hasNext())
                {
                    String listElement = iterValue.next(); 
                                    
                    String[] values = {key, listElement};

                    String insertSQL = SqlUtils.insertSQL(tableName, fieldsName, values);

                    script = script + "\n" +insertSQL;
                }

            }
            
            return script;
        
        }
        
        
	//test
	public static void main(String[] args){
		
		String tableName = "table";
		String[] fields = {"field1", "field2"};
                
                
                Map<Object, Object> testMap = new HashMap<Object, Object>();
                
                testMap.put("c", "b");
                testMap.put("r", "s");
                testMap.put("a", "x");
		
                Map<String, Collection<String>> testMap2 = new HashMap<String, Collection<String>>();
                
                List<String> list = new ArrayList<String>();
                list.add(new Integer(1).toString());
                list.add(new Integer(2).toString());
                
                testMap2.put("'a'", list);
                testMap2.put("'b'", list);
                
		System.out.println(mapCollection2SQL(tableName, fields, testMap2));
	}
	
}
