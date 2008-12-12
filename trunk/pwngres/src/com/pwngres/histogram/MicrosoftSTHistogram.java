package com.pwngres.histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class MicrosoftSTHistogram extends STHistogram {
	
	private enum RefinementMethod {
		FREQUENCY,
		RANGE,
		HYBRID,
		NORMAL
	}
	
	private enum RestructureTrigger {
		QUERY_NUMBER,
		CUM_ERROR,
	}
	
	private enum SplitMethod {
		FREQUENCY,
		ERROR,
		USAGE
	}

	// == Default parameters ==
	
	// Triggers and methods
	private static final RefinementMethod DEFAULT_REF_METHOD = RefinementMethod.HYBRID;
	private static final RestructureTrigger DEFAULT_RES_TRIGGER = RestructureTrigger.QUERY_NUMBER;
	private static final SplitMethod DEFAULT_SPLIT_METHOD = SplitMethod.FREQUENCY;
	
	// Parameters used in all cases
	private static final double DAMPING = 1; // For bucket refinement
	private static final double MERGE_THRESH = 0.015; // For choosing which buckets to merge
	private static final double SPLIT_THRESH = 0.2; // For choosing *number* of buckets to split
	
	// Params for restructure trigger
	private static final int RES_QUERY_THRESH = 10; // For QUERY_NUMBER
	private static final int RES_ERROR_THRESH = 100; // For CUM_ERROR
	
	
	// == Actual instance fields ==
	
	// Type of algorithm we'll use
	private final RefinementMethod refMethod;
	private final RestructureTrigger resTrigger;
	private final SplitMethod splitMethod;
	
	// Parameters used in all cases
	private double damping = DAMPING; // For bucket refinement
	private double mergeThresh = MERGE_THRESH; // For choosing which buckets to merge
	private double splitThresh = SPLIT_THRESH; // For choosing *number* of buckets to split
	
	// Used for restructure trigger
	private int resQueryThresh = RES_QUERY_THRESH; // Threshold for QUERY_NUMBER 
	private double resErrorThresh = RES_ERROR_THRESH; // Threshold For CUM_ERROR
	private int numberQueries; // Actual value for QUERY_NUMBER
	private double cumError; // Actual value for CUM_ERROR
	
	// Used for split method
	private double[] bucketError;
	private double[] bucketUsage;
	
	
	/**
	 * Creates a new SelfTuningHistogram (lol, no shit).
	 */
	public MicrosoftSTHistogram(int min, int max, int total, int numBuckets,
			RefinementMethod refType, RestructureTrigger resType, SplitMethod splitMethod) {
		this.refMethod = (refType != null) ? refType : DEFAULT_REF_METHOD;
		this.resTrigger = (resType != null) ? resType : DEFAULT_RES_TRIGGER;
		this.splitMethod = (splitMethod != null) ? splitMethod : DEFAULT_SPLIT_METHOD;
		
		this.min = min;
		this.max = max;
		this.initialTotal = total;
		this.numBuckets = numBuckets;
		
		this.frequencies = new double[numBuckets];
		this.ranges = new Range[numBuckets];
		
		initialize();
	}
	
	// Setters for parameters
	
	public void setDamping(double damp) {
		damping = damp;
	}
	
	public void setMergeThresh(double mt) {
		mergeThresh = mt;
	}
	
	public void setSplitThresh(double st) {
		splitThresh = st;
	}
	
	public void setResQueryThresh(int rq) {
		resQueryThresh = rq;
	}
	
	public void setResErrorThresh(int re) {
		resErrorThresh = re;
	}
	
	@Override
	protected void initialize() {
		double freq = initialTotal / numBuckets;
		double range = (max - min) / numBuckets;
		for (int i = 0; i < numBuckets; i++) {
			frequencies[i] = freq;
			ranges[i] = new Range(i * range, (i + 1) * range);
		}
		
		resetInformation();
	}
	
	@Override
	protected void refineBucketFrequencies(int lower, int upper, int actual) {
		// Calculate absolute error
		int est = get(lower, upper);
		int err = actual - est;
		
		// Distribute it along the buckets
		for (int i = 0; i < numBuckets; i++)
			frequencies[i] = Math.max(frequencies[i] + damping * err * multiplier(lower, upper, actual, i), 0);
	}
	
	private double multiplier(int lower, int upper, int actual, int iter) {
		double mult = 1;
		double freq = frequencies[iter] / get(lower, upper);
		double range = ranges[iter].overlaps(lower, upper);
		switch (refMethod) {
		case FREQUENCY:
			mult = freq;
			break;
		case RANGE:
			mult = range;
			break;
		case HYBRID:
			mult = freq * range;
			break;
		case NORMAL:
			return 1;
		}
		return mult;
	}
	
	@Override
	protected void updateInformation(int l, int u, int actual) {
		// Increase counter of how many times we've received information
		numberQueries++;
		cumError += Math.abs(actual - get(l, u));
	}
	
	@Override
	protected void resetInformation() {
		numberQueries = 0;
		cumError = 0;
	}
	
	@Override
	protected boolean needsRestructure() {
		boolean restruct = false;
		switch (resTrigger) {
		case QUERY_NUMBER:
			restruct = numberQueries >= resQueryThresh; break;
		case CUM_ERROR:
			restruct = cumError >= resErrorThresh; break;
		}
		return restruct;
	}
	
	@Override
	protected void restructure() {
		
		System.out.println("Starting restructuring...");
		
		// Merge buckets
		List<ArrayList<Double>> runs = initializeRuns();
		ArrayList<Double> toMerge;
		List<Double> maxDiffs;
		boolean doneMerging = false;
		while (!doneMerging) {
			maxDiffs = new ArrayList<Double>();
			// Get min diff between consecutive runs of buckets
			for (int i = 0; i < runs.size() - 1; i++) {
				maxDiffs.add(maxDiff(runs.get(i), runs.get(i + 1)));
			}
			
			try {
				double minDiff = Collections.min(maxDiffs);

				if (minDiff <= mergeThresh * initialTotal) {
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
			} catch (NoSuchElementException e) {
				// No restructuring
				resetInformation();
				return;
			}
		}
		
		// Pick buckets to split
		List<Integer> toSplit = getBucketsToSplit(runs);
		
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
	
	private List<Integer> getBucketsToSplit(List<ArrayList<Double>> runs) {
		int numSplits = (int) Math.round(splitThresh * numBuckets);
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
		return toSplit;
	}

}
