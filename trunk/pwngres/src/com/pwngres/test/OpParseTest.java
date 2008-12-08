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
	
	private static List<String> joinOne = new LinkedList<String>(); 
	private static List<String> joinTwo = new LinkedList<String>(); 
	private static List<String> joinThree = new LinkedList<String>(); 
	private static List<String> joinFour = new LinkedList<String>(); 
	
	//index scan nested loops join
	static {
		joinOne.add("Nested Loop  (cost=0.00..13447099.19 rows=2667913 width=29)"); 
		joinOne.add("  ->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)"); 
		joinOne.add("  ->  Index Scan using authors_pkey on authors a  (cost=0.00..5.01 rows=1 width=21)");
		joinOne.add("        Index Cond: (\"outer\".authid = a.id)"); 
	}

	//seq scan nested loops join
	static {
		joinTwo.add("Nested Loop  (cost=50179.12..41154898691.12 rows=1721496897120 width=29)");
		joinTwo.add("  ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)");
		joinTwo.add("  ->  Materialize  (cost=50179.12..87280.24 rows=2667912 width=8)");
		joinTwo.add("        ->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)"); 
	}

	
	//hash join 
	static {
		joinThree.add("Hash Join  (cost=16243.75..152915.85 rows=2560152 width=29)");
		joinThree.add("  ->  Seq Scan on paperauths pa  (cost=0.00..39757.12 rows=2667912 width=8)");
		joinThree.add("  ->  Hash  (cost=10849.60..10849.60 rows=645260 width=21)");
		joinThree.add("        ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)"); 
	}
	
	//merge join 
	static {
		joinFour.add("Merge Join  (cost=106925.64..180286.10 rows=2560152 width=29)");
		joinFour.add("  Merge Cond: (\"outer\".paperid = \"inner\".id)");
		joinFour.add("  ->  Index Scan using paperauths_paperid on paperauths pa  (cost=0.00..48270.00 rows=2667912 width=8)");
		joinFour.add("  ->  Sort  (cost=106925.64..108538.79 rows=645260 width=21)");
		joinFour.add("        Sort Key: a.id");
		joinFour.add("        ->  Seq Scan on authors a  (cost=0.00..10849.60 rows=645260 width=21)");
	}
	
	
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
    	System.out.println(new ScanParser().parse(seqScan).toString());
    	new ScanParser().parse(indexScan).toString(); 
      	new JoinParser().parse(join); 
            	
    	System.out.println(new JoinParser().parse(join).toString()); 
    	System.out.println(new JoinParser().parse(joinOne).toString()); 
    	System.out.println(new JoinParser().parse(joinTwo).toString()); 
    	System.out.println(new JoinParser().parse(joinThree).toString()); 
    	System.out.println(new JoinParser().parse(joinFour).toString()); 
    	
    }
    
    
}
