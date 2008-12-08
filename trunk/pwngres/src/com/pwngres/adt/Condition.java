package com.pwngres.adt;

/**
 * Condition of type a op b
 * 
 * For example, 2 > 3 
 * @author joseamuniz
 *
 */
public class Condition {

	private String a; 
	private String op;
	private String b; 
	
	public Condition(String a, String op, String b) {
		//convenience method 
		
		this.a = a; 
		this.op = op;
		this.b = b; 
	}
	
	public String toString() {
		return a + " " + op + " " + b; 
	}
}
