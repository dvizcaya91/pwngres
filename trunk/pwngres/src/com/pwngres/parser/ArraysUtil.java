package com.pwngres.parser;

import java.util.ArrayList;
import java.util.List;

public class ArraysUtil {

	/** 
	 * Removes all starting empty spaces from each of the elements of the list
	 * @param array The list of strings to be trimmed
	 */
	public static void flatten(List<String> array) {
		
		//get next offset
		int min = Integer.MAX_VALUE; 
		for (int i = 0; i < array.size(); i++) {
			String elm = array.get(i); 

			int j = 0; 
			while (elm.charAt(j) == ' ') 
				j++; 

			min = Math.min(min, j); 			
		}
			
		//remove offset 
		for (int i = 0; i < array.size(); i++) {
			String elm = array.get(i); 
			array.set(i, elm.substring(min)); 
			
		}
	}
	/**
	 * Returns a string array that is a subarray of <tt>array</tt>
     *
	 * An invocation subArray(x, a,b,c,d...) will return
	 * an array consisting of x[a...b],x[c...d],...
	 * 
	 * a,b,c,d... must be a strictly monotonically increasing sequence (except for 
	 *   the possibility of a single-point range a,a
	 * 
	 * @param array The array 
	 * @param range An even number of integers representing the disjoint ranges
	 * @return
	 */
	public static List<String> subArray(List<String> array, int... range ) {
		if (range.length % 2 != 0)
			throw new RuntimeException("Malformed range"); 
		
		int temp = -1 ;
		List<Range> ranges = new ArrayList<Range>(); 
		
		for (int age : range) {
			if (temp == -1) //first element in range
				temp = age; 
			else {
				ranges.add(new Range(temp, age)); 
				temp = -1;
			}		
		}
			
			return subArray(array, ranges); 
	}
	
	
	
	/*
	 * Same as above, but receiving list of pairs representing the ranges
	 */
	private static List<String> subArray(List<String> array, List<Range> ranges) {
		//for each element, check if it's somewhere in the array. sucky performance,
		//but I don't give a rat's ass. chances are we'll just have one or two ranges
		//and that's it. 
		
		List<String> subArrayList = new ArrayList<String>(); 
		
		for (int i = 0; i < array.size(); i++) {
			for (Range range : ranges)
				if (range.contains(i)) {
					subArrayList.add(array.get(i)); 
					break;
				}
		}		
		
		return subArrayList; 
	}
	
	/* Closed range
	 * 
	 */
	private static class Range {
	 
	  private int lower;	
	  private int higher; 
	  
	  private Range(int lower, int higher) {
		  this.lower = lower;
		  this.higher = higher;
	  }
	  
	  private boolean contains(int x) {
		  return (x >= lower && x <= higher); 
	  }
	  
	}
}
