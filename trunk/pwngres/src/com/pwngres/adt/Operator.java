package com.pwngres.adt;

import java.util.*;

public class Operator {

	String id;
	List<Operator> inputs;
	Operator outputDestination;
	
	public Operator(String id) {
		this.id = id;
		inputs = new ArrayList<Operator>();
		outputDestination = null;
	}
	
	public void setOutputDestination(Operator outputDestination) {
		this.outputDestination = outputDestination;
	}
	
	public Operator getOutputDestination() {
		return outputDestination;
	}
	
	public void setChild(int i, Operator op) {
		if (inputs.size() < i + 1)
			inputs.add(i, op); 
		else
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
		string += description() + "\n";  
		for (Operator op : getInputs()) {
			String subString = op.toString(); 
			String[] tokenized = subString.split("\n"); 
			
			for (int i = 0; i < tokenized.length; i++) {
				String token = tokenized[i];
				if (i == 0)
					string += "\t => " + token + "\n"; 
				else
					string += "\t" + token + "\n"; 

			}
		}
		
		return string; 
	}
	
	public String description() {
		return "OPERATOR"; 
	}
	
}
