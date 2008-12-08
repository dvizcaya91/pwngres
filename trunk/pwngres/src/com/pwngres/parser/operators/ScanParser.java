package com.pwngres.parser.operators;

import java.util.List;

import com.pwngres.adt.Operator;
import com.pwngres.adt.Select;
import com.pwngres.parser.OperatorParser;

public class ScanParser implements OperatorParser {

	public Operator parse(List<String> text) {
		return new Select("");
	}

}
