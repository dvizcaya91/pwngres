package com.pwngres.parser;

import java.util.LinkedList;
import java.util.List;


/**
 * Bunch of convenience static methods to parse different elements from a
 * Postgres EXPLAIN result. 
 * 
 * @author joseamuniz
 *
 */
public class PostgresUtil {

	public static final String ARROW = "->";
	
	
	/**
	 * Receives an array of the format
	 * 
	 * [
	 * -> A...,
	 *    B...,
	 * -> C...,
	 *    D...,
	 * -> E..., 
	 *    F...
	 * ...
	 * ]
	 * 
	 * Splits this into 
	 * [A...,
	 *  B...]
	 * [C...,
	 *  D...]
	 * ...
	 * 
	 * @return A list, where each element is a list of strings corresponding to a 
	 * 		full textual description of a subquery plan. 
	 */
	public static List<List<String>> getRoots(List<String> text) {
		
		int inf = 0;
		int sup = 0;
		
		List<List<String>> lists = new LinkedList<List<String>>();
	
		
		while (inf < text.size()) {
			List<String> current = new LinkedList<String>(); 

			do {
				current.add(text.get(sup)); 
				sup++;
				
			}
			while (sup < text.size() &&
					(text.get(sup).startsWith("\t") || text.get(sup).startsWith(" ")));

			lists.add(current); 
			inf = sup; 
		}
			
		for (List<String> list : lists) {
			list.set(0, list.get(0).substring(ARROW.length()));
			int j = 0; 
			while (list.get(0).charAt(j) == ' ') 
				j++; 
			list.set(0, list.get(0).substring(j)); 
		}

		return lists;
	}	
	
}
