package com.pwngres.adt;

import java.util.*;

public class Operator {

	String id;
	List<Operator> inputs;
	Operator outputDestination;
	
	public Operator(String id) {
		this.id = id;
		inputs = null;
		outputDestination = null;
	}
	
	public void setOutputDestination(Operator outputDestination) {
		this.outputDestination = outputDestination;
	}
	
	public Operator getOutputDestination() {
		return outputDestination;
	}
	
	public void setChild(int i, Operator op) {
		inputs.set(i, op); 
	}
	
	public Operator getChild(int i) {
		return inputs.get(i);
	}
	
	public void setInputs(List<Operator> inputs) {
		if (inputs != null)
			this.inputs = new ArrayList<Operator>(inputs);
		else 
			inputs = null;
	}
	
	public List<Operator> getInputs() {
		return inputs;
	}
	
	public String toString() {
		//return "{" + id +  " || Inputs: " + inputs + "}";
		String string = "";
		string += description(); 
		for (Operator op : getInputs()) {
			string += "\t => " + description(); 
		}
		
		return string; 
	}
	
	public String description() {
		return "OPERATOR"; 
	}
	
}
