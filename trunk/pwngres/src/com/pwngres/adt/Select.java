package com.pwngres.adt;


public class Select extends Operator{
	
	String table;
	String condition;
	
	public Select(String id) {
		super(id);
		table = null;
		condition = null;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public String getTable() {
		return table;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public String description() {
		return "SCAN"; 
	}
}
