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
	 * Returns the fraction of overlap with this range. That is, if u-l is
	 * bigger than upper-lower, then it will return 1. If they don't overlap
	 * at all, returns 0.
	 */
	public double overlaps(double l, double u) {
		return Math.max(Math.min(upper, u) - Math.max(lower, l), 0) / range();
	}

}
