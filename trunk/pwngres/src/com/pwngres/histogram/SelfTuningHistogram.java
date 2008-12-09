package com.pwngres.histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelfTuningHistogram implements Histogram {
	
	private static final double DAMPING = 1;
	private static final int RESTRUCTURE_THRESH = 10;
	private static final double MERGE_THRESH = 0.015;
	private static final double SPLIT_THRESH = 0.2;
	
	private double min;
	private double max;
	private double initialTotal;
	private final int numBuckets;
	
	private double[] frequencies;
	private Range[] ranges;
	
	private int numberQueries;
	
	public SelfTuningHistogram(int min, int max, int total, int numBuckets) {
		this.min = min;
		this.max = max;
		this.initialTotal = total;
		this.numBuckets = numBuckets;
		
		this.frequencies = new double[numBuckets];
		this.ranges = new Range[numBuckets];
		
		initialize();
	}

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
		
		updateInformation();
		
		if (needsRestructure())
			restructure();
	}
	
	private void initialize() {
		double freq = initialTotal / numBuckets;
		double range = (max - min) / numBuckets;
		for (int i = 0; i < numBuckets; i++) {
			frequencies[i] = freq;
			ranges[i] = new Range(i * range, (i + 1) * range);
		}
	}
	
	private void updateInformation() {
		// Increase counter of how many times we've received information
		numberQueries++;
		
	}
	
	private void resetInformation() {
		numberQueries = 0;
	}
	
	private boolean needsRestructure() {
		return numberQueries >= RESTRUCTURE_THRESH;
	}
	
	private void refineBucketFrequencies(int lower, int upper, int actual) {
		// Calculate absolute error
		int est = get(lower, upper);
		int err = actual - est;
		
		// Distribute it along the buckets
		double frac;
		for (int i = 0; i < numBuckets; i++) {
			frac = ranges[i].overlaps(lower, upper);
			frequencies[i] = Math.max(frequencies[i] + DAMPING * err * frac * frequencies[i] / est, 0);
		}
	}
	
	private void restructure() {
		
		System.out.println("Starting restructuring");
		
		// Merge buckets
		boolean doneMerging = false;
		List<ArrayList<Double>> runs = initializeRuns();
		ArrayList<Double> toMerge;
		List<Double> maxDiffs;
		while (!doneMerging) {
			maxDiffs = new ArrayList<Double>();
			// Get min diff between consecutive runs of buckets
			for (int i = 0; i < runs.size() - 1; i++) {
				maxDiffs.add(maxDiff(runs.get(i), runs.get(i + 1)));
			}
			double minDiff = Collections.min(maxDiffs);
			
			if (minDiff <= MERGE_THRESH * initialTotal) {
				// Merge the two runs
				int index = maxDiffs.indexOf(minDiff);
				toMerge = runs.get(index); // get the first run
				toMerge.addAll(runs.get(index + 1)); // add the second one to it
				runs.remove(index); // remove the old first run
				runs.add(index, toMerge); // add the new one (merged)
				runs.remove(index + 1); // remove the second run
			} else {
				// Done merging
				doneMerging = true;
			}
		}
		
		// Pick buckets to split
		int numSplits = (int) Math.round(SPLIT_THRESH * numBuckets);
		List<Integer> toSplit = new ArrayList<Integer>();
		List<Double> freqs = new ArrayList<Double>(); // TODO: nasty
		for (int i = 0; i < numBuckets; i++)
			freqs.add(frequencies[i]);
		
		List<Double> copyFreqs = new ArrayList<Double>(freqs);
		while (toSplit.size() < numSplits) {
			// Get current max and its index
			double max = Collections.max(copyFreqs);
			int index = freqs.indexOf(max);
			
			// Check if it will be merged or not
			int cursor = 0;
			boolean toBeMerged = false;
			for (int j = 0; j < runs.size(); j++) {
				// Find the run the bucket is at
				if (index >= cursor && index < cursor + runs.get(j).size())
					toBeMerged = runs.get(j).size() != 1;
				cursor += runs.get(j).size();
			}
			
			// If not, then we want to split it; store the bucket index in a list
			if (!toBeMerged)
				toSplit.add(index);
			
			// Don't consider this max again
			copyFreqs.remove(max);
		}
		
		// We know which buckets to split and how many to merge; distribute
		// extra buckets to splitting buckets based on frequencies
		
		int extraBuckets = 0; // num of extra buckets resulting from merge
		for (List<Double> run : runs) {
			extraBuckets += run.size() - 1;
		}
		
		// How many of the extra buckets should go to each splitting bucket?
		List<Integer> newBuckets = new ArrayList<Integer>(toSplit.size());
		
		// Get the total frequency first
		double totalFreq = 0;
		for (int i = 0; i < toSplit.size(); i++) {
			totalFreq += frequencies[toSplit.get(i)];
		}
		// Next, decide how many go to each bucket
		int assigned = 0;
		for (int i = 0; i < toSplit.size(); i++) {
			if (i == toSplit.size() - 1) // last bucket, just give it the rest
				newBuckets.add(extraBuckets - assigned);
			newBuckets.add((int) Math.round((frequencies[toSplit.get(i)] / totalFreq) * extraBuckets));
			assigned += (int) Math.round((frequencies[toSplit.get(i)] / totalFreq) * extraBuckets);
		}
		
		// Now we can do the merging and splitting
		double[] newFrequencies = new double[numBuckets];
		Range[] newRanges = new Range[numBuckets];
		List<Double> run;
		int newIndex = 0;
		int oldIndex = 0;
		for (int i = 0; i < runs.size(); i++) {
			run = runs.get(i);
			
			if (toSplit.contains(oldIndex)) {
				// Got a bucket to split
				int numNewBuckets = newBuckets.get(toSplit.indexOf(oldIndex)) + 1;
				double newLength = ranges[oldIndex].range() / numNewBuckets;
				for (int j = 0; j < numNewBuckets; j++) {
					// number of buckets in new one
					newFrequencies[newIndex] = frequencies[oldIndex] / numNewBuckets;
					newRanges[newIndex] = new Range(ranges[oldIndex].lower + j * newLength, ranges[oldIndex].lower + (j + 1) * newLength);
					newIndex++;
				}
				oldIndex++;
			} else if (run.size() > 1) {
				// Got some buckets to merge
				double freq = 0;
				double low = 0;
				double high = 0;
				for (int j = 0; j < run.size(); j++) {
					freq += run.get(j);
					if (j == 0)
						low = ranges[oldIndex].lower;
					if (j == run.size() - 1)
						high = ranges[oldIndex + run.size() - 1].upper;
				}
				
				newFrequencies[newIndex] = freq;
				newRanges[newIndex] = new Range(low, high);
				newIndex++;
				oldIndex += run.size();
			} else {
				// Do nothing with this bucket; just copy frequency and range
				newFrequencies[newIndex] = frequencies[oldIndex];
				newRanges[newIndex] = ranges[oldIndex];
				newIndex++;
				oldIndex++;
			}
		}
		
		frequencies = newFrequencies;
		ranges = newRanges;
		
		resetInformation();
	}
	
	private List<ArrayList<Double>> initializeRuns() {
		List<ArrayList<Double>> runs = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> run;
		for (int i = 0; i < numBuckets; i++) { 
			run = new ArrayList<Double>();
			run.add(frequencies[i]);
			runs.add(run);
		}
		return runs;
	}
	
	private double maxDiff(List<Double> run1, List<Double> run2) {
		// Find max and min of each run
		double max1 = Collections.max(run1);
		double min1 = Collections.min(run1);
		double max2 = Collections.max(run2);
		double min2 = Collections.min(run2);
		
		// Return max of differences
		return Math.max(Math.abs(max1 - min2), Math.abs(max2 - min1));
	}
	
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
