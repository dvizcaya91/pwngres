package com.pwngres.parser;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents each of the different operations provided by Postgres
 * 
 * Each PostgresOp contains two attributes
 * 	- A <tt>Family</tt> to specify its general classification
 *  - A <tt>Description</tt> which is a unique String which can be used to 
 *  	identify this type of operation in a Postgres result. 
 * @author joseamuniz
 *
 */
public enum PostgresOp {
	
	
	//JOINS
	MERGE_JOIN(Family.JOIN, "MERGE JOIN"), 
	HASH_JOIN(Family.JOIN, "HASH JOIN"), 
	NESTED_LOOPS_JOIN(Family.JOIN, "NESTED LOOP"),
	//SCANS
	INDEX_SCAN(Family.SCAN, "INDEX SCAN"), 
	SEQ_SCAN(Family.SCAN, "SEQUENTIAL SCAN"),
	//MISC
	MATERIALIZE(Family.MISC, "MATERIALIZE"); 
	

	
	
	private Family fam; 
	private String desc; 
	
	private PostgresOp(Family fam, String desc) {
		this.fam = fam;
		this.desc = desc;
	}
	
	public Family getFamily() {
		return this.fam;
	}
	public String getDescription() {
		return this.desc; 
	}
	
	
	/**
	 * Returns the type of the operation that the query plan <tt>text</tt>
	 * is rooted on
	 * 
	 * @param text A textual representation of the query plan, in the format of 
	 * 				PostgreSQL's EXPLAIN output.
	 * @return
	 */
	public static PostgresOp typeOf(List<String> text) {
		if (text == null || text.isEmpty())
			return null; 
		
		for(PostgresOp op : EnumSet.allOf(PostgresOp.class))
			if (op.getDescription().contains(text.get(0)))
				return op;
		
		return null;
	}

}

/**
 * General classification of Postgres operations.
 * 
 * Currently supporting joins, scans, and misc
 * 
 * @author joseamuniz
 *
 */
enum Family {
	JOIN, SCAN, MISC;
}
