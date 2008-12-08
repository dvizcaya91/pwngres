package com.pwngres.parser.operators;

import java.util.List;

import com.pwngres.adt.Operator;
import com.pwngres.parser.ArraysUtil;
import com.pwngres.parser.OperatorParser;
import com.pwngres.parser.ParserFactory;
import com.pwngres.parser.PostgresOp;

public class MaterializeParser implements OperatorParser {

	public Operator parse(List<String> text) {
		List<String> subOp = ArraysUtil.subArray(text, 0, text.size() - 1); 
		OperatorParser parser = ParserFactory.parserFor(PostgresOp.typeOf(subOp)); 
					 
		return parser.parse(subOp);
	}

}
