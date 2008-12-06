package tool;

import java.sql.*;
import java.util.*;

import com.pwngres.adt.QueryPlan;
import com.pwngres.parser.QueryPlanBuilder;

/**
 * Run this class with a SQL query as an argument
 * 
 * USAGE: java Query {query};
 * Ex: java Query SELECT * FROM test-table;
 * 
 * @author julian
 */
public class Pwngres {
	
	public static String host = "//localhost:5432/";
	public static String username = "postgres";
	public static String password = "jamn1986";
	public static String database = "pwngres";
	
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("USAGE: java Query <query>");
			System.out.println("Ex: java Query SELECT * FROM test-table;");
			System.exit(0);
		}
		
		
		PostgresConnection pgconnect = new PostgresConnection(
				host, username, password, database);
		Connection c = pgconnect.getConnection();
		QueryPlanBuilder qpb = new QueryPlanBuilder();
		String query = getQueryString(args);	
		
		//executeQuery(query, c);
		QueryPlan queryPlan = qpb.parseAnalyseQueryPlan(analizeQuery(query, c));
		System.out.println(queryPlan);
		
		//executeQuery("SELECT * FROM pg_statistic WHERE starelid = 16399;", c);
	
		try {
			c.close();
		} catch (SQLException sqle) {
			System.err.println("error when closing connection");
			sqle.printStackTrace();
		}
		
	}
	
	private static String getQueryString(String[] args) {
		String q = "";
		for (String s : args) {
			q = q.concat(s);
			q = q.concat(" ");
		}
		return q;
	}
	
	private static void executeQuery(String query, Connection c) {
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			// print the columns' names
			for (int i=1 ; i <= rsmd.getColumnCount() ; i++) {
				System.out.print(rsmd.getColumnName(i));
				System.out.print(" | ");
			}
			System.out.println("");
			
			// print the rows from the result set
			while (rs.next()) {
				for (int i=1 ; i <= rsmd.getColumnCount() ; i++) {
					System.out.print(getData(i, rs));
					System.out.print(" | ");
				}
				System.out.println("");
			}
			
			stmt.close();			
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	private static List<String> analizeQuery(String query, Connection c) {
		
		String analize = "EXPLAIN ANALYZE " + query;

		try {
			
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(analize);
			List<String> queryPlan = new ArrayList<String>();
			
			while (rs.next()) {
				String line = getData(1, rs);
				queryPlan.add(line);
			}
			
			stmt.close();
			
			return queryPlan;
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return null;
	}
	
	private static String getData(int i, ResultSet rs) {
		String data = "";
		try {
			int type = rs.getMetaData().getColumnType(i);
			
			// in case you want to see the integer 
			// corresponding this type
			//System.out.println(type);
			
			// add more code here for the different data types
			switch (type) {
				case Types.INTEGER: data = Integer.toString(rs.getInt(i)); break;
				case Types.BIGINT: data = Long.toString(rs.getLong(i)); break;
				case Types.VARCHAR: data = rs.getString(i); break;
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return data;
	}
	

	
}
