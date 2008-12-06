package com.pwngres.adt;


public class QueryPlan {

	Operator root;
	
	public QueryPlan() {
		this.root = null;
	}
	
	public QueryPlan(Operator root) {
		this.root = root;
	}
	
	public void setRoot(Operator root) {
		this.root = root;
	}
	
	public Operator getRoot() {
		return root;
	}	
	
	public String toString() {
		return root.toString();
	}
	
}
