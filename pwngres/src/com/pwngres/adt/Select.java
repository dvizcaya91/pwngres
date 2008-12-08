package com.pwngres.adt;

import java.util.List;


public class Select extends Operator{
	
	String table;
	List<Condition> conditions;
	
	public Select(String id) {
		super(id);
		table = null;
		conditions = null;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	
	public String getTable() {
		return table;
	}
	
	public List<Condition> getConditions() {
		return conditions;
	}
	
	public String description() {
		return "SCAN on " + conditions; 
	}
}
