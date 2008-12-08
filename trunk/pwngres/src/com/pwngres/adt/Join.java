package com.pwngres.adt;

import java.util.List;


public class Join extends Operator {

	List<Condition> conditions;
	
	public Join(String id) {
		super(id);
		conditions = null;
	}
	
	public void setConditions(List<Condition> condition) {
		this.conditions = condition;
	}
	
	public List<Condition> getConditions() {
		return conditions;
	}
	
	public String description() {
		return "JOIN on " + conditions; 
	}
	
//	public String toString() {
//		return "{JOIN-" + id  + "," + condition + " || Inputs: " + inputs + "}";
//	}
	
}
