package phenomizer.database;

import phenuma.constants.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * This class has some utils to work with a database
 * 	- Connection to the database
 *  - Execute a query
 *  - Close the connection
 *  
 * @author Rocio
 */
public final class DBUtils {
	
	//Constants
	public static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";
        
	private static Connection connection = null;
	
	//Create a connetion to the database
	public static void connect(){
		
		try {
			Class.forName(MYSQLDRIVER);
			connection = DriverManager.getConnection(Constants.CONNECTION_STR, Constants.USER, Constants.PASSWORD); 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Execute an sql query.
	 * 
	 * @param query
	 * @param connection
	 * @return
	 */
	public static ResultSet execute(String query){		
		Statement s = null;
		ResultSet res = null;
			
		try {
			s = connection.createStatement();
			res = s.executeQuery(query);
		} catch (SQLException e) {
			System.err.println(phenuma.constants.Constants.QUERY_EXECUTION_ERROR+" Error message: "+e.getMessage()+" Query: "+query);
		}
	
		return res;
	}
        
        
  	/**
	 * Execute update
	 * 
	 * @param query
	 * @param connection
	 * @return
	 */
	public static int executeUpdate(String sqlsentence){		
		Statement s = null;
		int res = 0;
			
		try {
			s = connection.createStatement();
			res = s.executeUpdate(sqlsentence);
		} catch (SQLException e) {
			System.err.println(phenuma.constants.Constants.QUERY_EXECUTION_ERROR+" Error message: "+e.getMessage()+" Query: "+sqlsentence);
		}
	
		return res;
	}
              
	
	/**
	 * Close a connection to the database
	 * 
	 * @param connection
	 */
	public static void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println(phenuma.constants.Constants.ERROR_CLOSE_CONNECTION);
		}
	}

}
