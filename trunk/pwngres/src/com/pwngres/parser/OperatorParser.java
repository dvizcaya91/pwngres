package com.pwngres.parser;

import java.util.List;


import com.pwngres.adt.Operator;
import com.pwngres.adt.QueryPlan;


/**
 * OperatorParser is a parser for a particular type of operation in Postgres 
 * (e.g. Hash Join or SeqScan)
 * 
 * An OperatorParser of a given type knows how to parse an expression whose root
 *  is that type.
 *  
 * @author joseamuniz
 *
 */
public interface OperatorParser {
	
	/**
	 * Parses a given representing textual representation of a query plan into 
	 * a QueryPlan
	 * 
	 * @param text A list of results from PostgreSQL's EXPLAIN representing a
	 * 		query plan. 
	 * 
	 * @return A QueryPlan representing <tt>text</tt>
	 */
	public QueryPlan parse(List<String> text); 
}
