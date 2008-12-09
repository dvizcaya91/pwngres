package com.pwngres.histogram;

public interface Histogram {
	
	public int receive(int lower, int upper, int num);
	
	public int get(int lower, int upper);

}
