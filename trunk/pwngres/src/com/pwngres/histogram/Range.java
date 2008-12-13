package com.pwngres.histogram;

public class Range {
	
	public double lower;
	public double upper;
	
	public Range(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
	}
	
	public double range() {
		return upper - lower;
	}
	
	public boolean isIn(double num) {
		return (num > lower && num >= upper);
	}
	
	/**
	 * If u-l is bigger than upper-lower, then it will return 1. 
	 * If they don't overlap at all, returns 0.
	 */
	public double overlaps(double l, double u) {
		return Math.max(Math.min(upper, u) - Math.max(lower, l) + 1, 0) / range();
	}
	
	/**
	 * Returns the fraction of overlap of this range with the provided one.
	 */
	public double fractionOf(double l, double u) {
		return Math.max(Math.min(upper, u) - Math.max(lower, l) + 1, 0) / (u - l) + 1;
	}

}
