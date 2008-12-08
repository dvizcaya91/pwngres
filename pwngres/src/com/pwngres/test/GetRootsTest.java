package com.pwngres.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.pwngres.parser.PostgresUtil;

import junit.framework.TestCase;

public class GetRootsTest extends TestCase {

	private static List<String> planA = new ArrayList<String>(3); 
	private static List<String> planB = new ArrayList<String>(3); 
	private static List<String> planBprime = new ArrayList<String>(3); 
	private static List<String> planC = new ArrayList<String>(5); 
	
	static {
		planA.add("->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)"); 
		planA.add("->  Hash  (cost=10849.60..10849.60 rows=645260 width=21)");  
		planA.add("        ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)"); 		
		

		planB.add("->  Hash  (cost=10849.60..10849.60 rows=645260 width=21)");  
		planB.add("        ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)"); 		
		planB.add("->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)"); 
	

		planBprime.add("->  Hash  (cost=10849.60..10849.60 rows=645260 width=21)");  
		planBprime.add("\t->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)"); 		
		planBprime.add("->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)"); 

		planC.add("->  Hash  (cost=10849.60..10849.60 rows=645260 width=21)");  
		planC.add("        ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)"); 		
		planC.add("            ->  Hash  (cost=10849.60..10849.60 rows=645260 width=21)");  
		planC.add("            ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)"); 				
		planC.add("->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)"); 
		planC.add("         ->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)"); 
		
	
	
	}

	
	@Test
	public void testOneTwoWithEmptySpaces() {
		List<List<String>> result = PostgresUtil.getRoots(planA); 
		assertNotNull("expected non null query plan", result); 
		assertEquals(result.size(), 2);
		assertEquals(result.toString(),
				"[[Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)], " +
  				 "[Hash  (cost=10849.60..10849.60 rows=645260 width=21),         ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)]]"
				);
	}
	
	@Test
	public void testTwoOneWithEmptySpaces() {
		List<List<String>> result = PostgresUtil.getRoots(planB); 
		assertNotNull("expected non null query plan", result); 
		assertEquals(result.size(), 2);
		assertEquals(result.toString(),
				"[[Hash  (cost=10849.60..10849.60 rows=645260 width=21),         ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)], " + 
				 "[Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)]]"
				);
		
		
	}

	@Test
	public void testTwoOneWithTabs() {
		List<List<String>> result = PostgresUtil.getRoots(planBprime); 
		assertNotNull("expected non null query plan", result); 
		assertEquals(result.size(), 2);
		assertEquals(result.toString(),
				"[[Hash  (cost=10849.60..10849.60 rows=645260 width=21), "+
				"\t->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)],"+
				" [Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)]]"
		);
		
		
	}

	
	@Test
	public void testFourTwoWithEmptySpaces() {
		List<List<String>> result = PostgresUtil.getRoots(planC); 
		assertNotNull("expected non null query plan", result); 
		assertEquals(result.size(), 2);
		assertEquals(result.toString(),
				"[[Hash  (cost=10849.60..10849.60 rows=645260 width=21)," +
				"         ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21),"+
			    "             ->  Hash  (cost=10849.60..10849.60 rows=645260 width=21),"+"" +
			    "             ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)],"+
				" [Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8),"+
				"          ->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)]]");		
		
		
	}

}
