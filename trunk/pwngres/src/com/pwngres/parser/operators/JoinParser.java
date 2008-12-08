package com.pwngres.parser.operators;

import java.util.List;

import com.pwngres.adt.Join;
import com.pwngres.adt.Operator;
import com.pwngres.parser.ArraysUtil;
import com.pwngres.parser.OperatorParser;
import com.pwngres.parser.ParserFactory;
import com.pwngres.parser.PostgresOp;
import com.pwngres.parser.Family; 
import com.pwngres.parser.PostgresUtil;


public class JoinParser implements OperatorParser {

	public Operator parse(List<String> text) {
	 

	 assert Family.JOIN.equals(PostgresOp.typeOf(text).getFamily());
	 
	 if (text.size() <= 1)
		 throw new  RuntimeException("Malformed join-rooted query plan: " + text); 
	 
	  // Two types of joins: 
	 /* 
	  * (A)
	  * -> JOIN ... 
	  *    Condition
	  *    -> X
	  *    -> Y
	  *
	  *
	  */
	 
	 Join join = new Join("");
	 
	 List<List<String>> subOps = null; 
	 List<String> newText = null; 
	 
	 if (! text.get(1).startsWith(PostgresUtil.ARROW)) {	
		 newText = ArraysUtil.subArray(text, 0, 0, 2, text.size() - 1); 
	 }
	 
	 /*
	  * (B)
	  * -> JOIN ...
	  *    -> X
	  *    -> Index scan  ... 
	  *    	  Condition
	  */
	 else  {
		 newText = ArraysUtil.subArray(text, 0, text.size() - 1); 
	 }
	  
	 ArraysUtil.flatten(newText); 
	 subOps = PostgresUtil.getRoots(newText); 		 

	 assert subOps.size() == 2; 
	 
	 for (int i = 0; i < subOps.size(); i++) {
		List<String> subOp = subOps.get(i);
		OperatorParser parser = ParserFactory.parserFor(PostgresOp.typeOf(subOp)); 
		
		join.setChild(i,parser.parse(subOp)); 			 
	 }
	 
		 return join;
		 
	}


}
