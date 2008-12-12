package com.pwngres.histogram;

/**
 * 
 * 
 * @author Rodrigo Ipince
 */
public abstract class STHistogram implements Histogram {
	
	// Information about the data that this histogram encodes
	protected double min;
	protected double max;
	protected double initialTotal;
	protected int numBuckets;
	
	// Properties of the histogram
	protected double[] frequencies;
	protected Range[] ranges;

	@Override
	public int get(int lower, int upper) {
		double count = 0;
		for (int i = 0; i < numBuckets; i++) {
			count += ranges[i].overlaps(lower, upper) * frequencies[i];
		}
		return (int) count;
	}

	@Override
	public void receive(int lower, int upper, int num) {
		refineBucketFrequencies(lower, upper, num);
		
		updateInformation(lower, upper, num);
		
		if (needsRestructure())
			restructure();
	}
	
	protected abstract void initialize();
	
	protected abstract void updateInformation(int lower, int upper, int num);
	
	protected abstract void resetInformation();
	
	protected abstract boolean needsRestructure();
	
	protected abstract void refineBucketFrequencies(int lower, int upper, int actual);
	
	protected abstract void restructure();
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < numBuckets; i++) {
			sb.append("  " + (int) frequencies[i] + "  ");
		}
		sb.append("]\n");
		sb.append("[");
		for (int i = 0; i < numBuckets; i++) {
			sb.append( (int) ranges[i].lower + "-" + (int) ranges[i].upper + " ");
		}
		sb.append("]");
		return sb.toString();
	}

}
