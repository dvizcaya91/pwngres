package com.pwngres.histogram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
	
	private Map<String, List<Integer>> tuples = new HashMap<String, List<Integer>>();
	
	private String col1;
	private String col2;
	private String col3;
	
	public Database(String column1) {
		tuples.put(column1, new ArrayList<Integer>());
	}
	
	public Database(String column1, String column2) {
		tuples.put(column1, new ArrayList<Integer>());
		tuples.put(column2, new ArrayList<Integer>());
	}
	
	public Database(String column1, String column2, String column3) {
		tuples.put(column1, new ArrayList<Integer>());
		tuples.put(column2, new ArrayList<Integer>());
		tuples.put(column3, new ArrayList<Integer>());
		
		col1 = column1;
		col2 = column2;
		col3 = column3;
	}
	
	public void add(int val1, int val2, int val3) {
//		System.out.println("Adding: " + val1 + ", " + val2 + ", " + val3);
		tuples.get(col1).add(val1);
		tuples.get(col2).add(val2);
		tuples.get(col3).add(val3);
	}
	
	public int get(String col, int lower, int upper) {
		List<Integer> shits = tuples.get(col);
//		System.out.println("shits has " + shits.size() + " elements");
		int count = 0;
		for (Integer i : shits) {
//			System.out.println("Int: " + i + ", lower: " + lower + ", upper: " + upper);
			if (i >= lower && i <= upper)
				count++;
		}
		
		return count;
	}

}
