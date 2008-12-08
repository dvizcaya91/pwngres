package com.pwngres.parser.operators;

import java.util.List;

import com.pwngres.adt.Operator;
import com.pwngres.parser.ArraysUtil;
import com.pwngres.parser.OperatorParser;
import com.pwngres.parser.ParserFactory;
import com.pwngres.parser.PostgresOp;

public class SortParser extends OperatorParser {

	public Operator parse(List<String> text) {
		super.parse(text);
		List<String> subOp = ArraysUtil.subArray(text, 2, text.size() - 1); 
		ArraysUtil.flatten(subOp); 
		
		OperatorParser parser = ParserFactory.parserFor(PostgresOp.typeOf(subOp)); 
					 
		return parser.parse(subOp);
	}

}
