package com.pwngres.parser;

import java.util.EnumSet;
import java.util.List;

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
	MERGE_JOIN(Family.JOIN, "Merge Join"), 
	HASH_JOIN(Family.JOIN, "Hash Join"), 
	NESTED_LOOPS_JOIN(Family.JOIN, "Nested Loop"),
	//SCANS
	INDEX_SCAN(Family.SCAN, "Index Scan"), 
	SEQ_SCAN(Family.SCAN, "Seq Scan"),
	//MISC
	MATERIALIZE(Family.MISC, "Materialize"); 
	

	
	
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
		
		for(PostgresOp op : EnumSet.allOf(PostgresOp.class)) {
			if (text.get(0).contains(op.getDescription())) {
				System.out.println("Type for " + text.get(0) + " is " + op);
				return op;
			}
		}
		
		throw new RuntimeException("No such type found:  " + text.get(0)); 
	}

}


