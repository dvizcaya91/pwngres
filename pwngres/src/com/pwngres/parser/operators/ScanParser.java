package com.pwngres.parser.operators;

import java.util.List;

import com.pwngres.adt.Operator;
import com.pwngres.adt.Select;
import com.pwngres.parser.OperatorParser;
import com.pwngres.parser.PostgresUtil;

public class ScanParser extends OperatorParser {

	public Operator parse(List<String> text) {
		super.parse(text);
		/*
		 * Expects format of the type 
		 * 
		 * "Seq Scan on x  (cost=0.00..32.60 rows=2260 width=6) 
		 *  [   Index Cond: (id = 0) ]"
		 *     
		 *    
		 *     
		 */
		Select sel = new Select(""); 
		
		if (text.size() > 1 && text.get(1).contains(PostgresUtil.CONDITION))
			sel.setConditions(PostgresUtil.getConditions(text.get(1)));
		else
			sel.setConditions(PostgresUtil.getConditions(null)); 
		
		return sel;
	}

}
