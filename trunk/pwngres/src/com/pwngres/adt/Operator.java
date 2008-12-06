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
		
		String string = "==D " + id; 
		for (Operator op : inputs) {
			string += "\n\t " + op; 
		}
		return string;
	}
	
}
