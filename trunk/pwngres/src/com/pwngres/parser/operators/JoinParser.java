package com.pwngres.parser.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pwngres.adt.Condition;
import com.pwngres.adt.Join;
import com.pwngres.adt.Operator;
import com.pwngres.debug.DebugConstants;
import com.pwngres.parser.ArraysUtil;
import com.pwngres.parser.OperatorParser;
import com.pwngres.parser.ParserFactory;
import com.pwngres.parser.PostgresOp;
import com.pwngres.parser.Family; 
import com.pwngres.parser.PostgresUtil;


public class JoinParser extends OperatorParser {

	/*
	 * TODO For now, try multiple conditions and watch this class FAIL 
	 * 
	 *
	 * @see com.pwngres.parser.OperatorParser#parse(java.util.List)
	 */
	public Operator parse(List<String> text) {
		super.parse(text);

		assert Family.JOIN.equals(PostgresOp.typeOf(text).getFamily());

		if (text.size() <= 1)
			throw new  RuntimeException("Malformed join-rooted query plan: " + text); 


		Join join = new Join("");

		List<List<String>> subOps = null; 
		List<String> newText = null; 
		String condition = null; 
		
		// Two types of joins: 
		/* 
		 * (A)
		 * JOIN ... 
		 *    [Condition]
		 *    -> X
		 *    -> Y
		 *
		 *
		 */
		if (! ArraysUtil.flatten(text.get(1)).startsWith(PostgresUtil.ARROW)) {	
			condition = text.get(1); 			
			newText = ArraysUtil.subArray(text, 2, text.size() - 1); 
			
		}

		/*
		 * (B)
		 * -> JOIN ...
		 *    -> X
		 *    -> Index scan  ... 
		 *    	  Condition
		 */
		else  {
			condition = text.get(text.size() - 1); 
			newText = ArraysUtil.subArray(text, 1, text.size() - 1); 

		}

		ArraysUtil.flatten(newText); 

		subOps = PostgresUtil.getRoots(newText); 		 

		assert subOps.size() == 2; 

		for (int i = 0; i < subOps.size(); i++) {
			List<String> subOp = subOps.get(i);
			OperatorParser parser = ParserFactory.parserFor(PostgresOp.typeOf(subOp)); 

			join.setChild(i,parser.parse(subOp)); 			 
		}
		
		if (! ArraysUtil.flatten(condition).contains(PostgresUtil.CONDITION))
			condition = null; 
		List<Condition> conditions = getConditions(condition); 

		return join;

	}
	
	
	/*
	 * Returns a list of conditions
	 */
	protected List<Condition> getConditions(String conditionString) {
		
		
		if (conditionString == null)
			return new LinkedList<Condition>(); 
		
		assert conditionString.contains("Cond"); 
		
		String paren = conditionString.substring(conditionString.indexOf('(') + 1,
												 conditionString.indexOf(')')); 
	
		String[] result = paren.split(" "); 
		
		assert result.length == 3; 
		
		Condition cond = new Condition(result[0], result[1], result[2]); 
		List<Condition> list = new LinkedList<Condition>(); 
		list.add(cond);
		
		if (DebugConstants.DEBUG)
			System.out.println("Condition is" + list ); 
		System.out.println("List is " + list); 
		return list;
	}


}
