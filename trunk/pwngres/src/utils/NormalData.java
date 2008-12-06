package utils;

import java.sql.*;
import java.util.Random;

import tool.PostgresConnection;

public class NormalData {
	
	public static String host = "//localhost:5432/";
	public static String username = "postgres";
	public static String password = "postgres";
	public static String database = "test";

	
	/**
	 * Prints a histogram of the given dataset.
	 * Only works for data in range 0 to upperBound
	 */
	public static void printHistogram(int[] data, int upperBound, int numBuckets) {
		
		int[] buckets = new int[numBuckets];
		
		for (int n : data) {
			double f = (double) n/(upperBound + 1);
			buckets[(int) (f*numBuckets)]++;
		}
		
		for (int count : buckets) {
			for (int i = 0 ; i < count ; i++) {
				System.out.print("*");	
			}
			System.out.println("");
		}
	}
	
	/**
	 * Generates a dataset drawn from a normal distribution. 
	 * 
	 * @param mean : desired mean
	 * @param standardDiv : desired standard deviation
	 * @param lowerBound : lower bound of the set of data values
	 * @param upperBound : upper bound of the set of data values
	 * @param datasetSize : size of the desired data set
	 * @return
	 */
	public static int[] generateNormalData(int mean, int standardDiv, 
			int lowerBound, int upperBound, int datasetSize) {
		
		int[] data = new int[datasetSize];
		
		Random r = new Random();
		for (int i = 0 ; i < data.length ; i++) {
			int n = (new Double(mean + r.nextGaussian()*standardDiv)).intValue();
			if (n > upperBound) n = upperBound;
			if (n < lowerBound) n = lowerBound;
			data[i] = n;
		}
		
		return data;
	}
	
	/**
	 * Connects to the database, generates a dataset from a normal 
	 * distribution, and inserts the generated values in a one column
	 * table called 'students'.
	 * 
	 * DO NOT RUN if you don't want to change the table 
	 */
	public static void main(String[] args) {
		
		PostgresConnection psconn = new PostgresConnection(host, username, password, database);
		System.out.println("Connection successful");
		Connection c = psconn.getConnection();
		
		int[] arr = generateNormalData(500, 150, 0, 999, 10000);
		try {
			Statement stmt = c.createStatement();
			for (int i : arr) {
				stmt.executeUpdate("INSERT INTO students VALUES(" + Integer.toString(i) + ")");
			}
			stmt.close();
			c.close();
		} catch (SQLException sqle)  {
			System.err.println("Some error");
			sqle.printStackTrace();
		}
	}

}