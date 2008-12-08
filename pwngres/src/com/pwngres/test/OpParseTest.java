package com.pwngres.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.pwngres.parser.operators.JoinParser;
import com.pwngres.parser.operators.ScanParser;

import junit.framework.TestCase;

public class OpParseTest extends TestCase {
	
	private static List<String> seqScan = new LinkedList<String>(); 
	private static List<String> indexScan = new LinkedList<String>(); 
	private static List<String> join = new LinkedList<String>(); 
	
	
    static {
    	seqScan.add("Seq Scan on ages a  (cost=0.00..32.60 rows=2260 width=6) (actual time=0.000..0.000 rows=0 loops=1)");
 
    	indexScan.add("Index Scan using ages_pkey on ages a2  (cost=0.00..13.45 rows=753 width=6) (never executed)");  
    	indexScan.add("  Index Cond: (a.id <= a2.id)"); 
 
    	join.add("Nested Loop  (cost=0.00..38970763.57 rows=1282574860 width=18) (actual time=0.002..0.002 rows=0 loops=1)"); 
    	join.add("  ->  Nested Loop  (cost=0.00..51695.09 rows=1702533 width=12) (actual time=0.001..0.001 rows=0 loops=1)"); 
    	join.add("        ->  Seq Scan on ages a  (cost=0.00..32.60 rows=2260 width=6) (actual time=0.000..0.000 rows=0 loops=1)");
    	join.add("        ->  Index Scan using ages_pkey on ages a2  (cost=0.00..13.45 rows=753 width=6) (never executed)"); 
    	join.add("              Index Cond: (a.id <= a2.id)");
    	join.add("  ->  Index Scan using ages_pkey on ages a3  (cost=0.00..13.45 rows=753 width=6) (never executed)");
    	join.add("        Index Cond: (a2.id <= a3.id)");    
    
    }
    
    @Test
    public void testScanParser() {
    	new ScanParser().parse(seqScan);
    	new ScanParser().parse(indexScan); 
    	
    	System.out.println(new JoinParser().parse(join).toString()); 
    	
    }
    
    
}
