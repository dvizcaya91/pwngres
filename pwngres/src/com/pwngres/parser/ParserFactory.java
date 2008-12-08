package com.pwngres.parser;

import com.pwngres.parser.operators.JoinParser;
import com.pwngres.parser.operators.AccumulateParser;
import com.pwngres.parser.operators.ScanParser;
import com.pwngres.parser.operators.SortParser;


public class ParserFactory {
	
	/**
	 * Provides an appropriate <tt>OperatorParser</tt> for a particular type 
	 * of Postgres operation (<tt> PostgresOp </tt>). 
	 * 
	 * @param type The type of Postgres operation to be parsed
	 * @return A parser of type <tt>type</tt>
	 */
	public static OperatorParser parserFor(PostgresOp type) {
				
		if (Family.JOIN.equals(type.getFamily()))
			return new JoinParser(); 
		else if (Family.SCAN.equals(type.getFamily())) 
			return new ScanParser(); 
		else if (PostgresOp.SORT.equals(type))
			return new SortParser();
		else if (Family.MISC.equals(type.getFamily()))
			return new AccumulateParser();
		else
			throw new RuntimeException("Cannot find parser for " + type); 
	}
	

}
