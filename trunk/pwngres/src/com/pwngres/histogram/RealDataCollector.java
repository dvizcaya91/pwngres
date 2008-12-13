package com.pwngres.histogram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.pwngres.histogram.MicrosoftSTHistogram.SplitMethod;


public class RealDataCollector {

	public static void main(String[] args) {

		try {
			// create input buffer
			System.out.println(args[0]);
			FileReader input = new FileReader(args[0] + "\\query3.csv");
			BufferedReader bufRead = new BufferedReader(input);
			
			MicrosoftSTHistogram endTimeHist1 = new MicrosoftSTHistogram(0, 30000, 10000, 20, null, null, null);
			endTimeHist1.setDamping(0.4);
			endTimeHist1.setMergeThresh(0.0001);
			endTimeHist1.setSplitThresh(0.1);
			endTimeHist1.setResQueryThresh(50);

			MicrosoftSTHistogram endTimeHist2 = new MicrosoftSTHistogram(0, 30000, 10000, 20, null, null, SplitMethod.HIGH_ERROR);
			endTimeHist2.setDamping(0.5);
			endTimeHist2.setMergeThresh(0.0001);
			endTimeHist2.setSplitThresh(0.1);
			endTimeHist2.setResQueryThresh(50);

			MicrosoftSTHistogram endTimeHist3 = new MicrosoftSTHistogram(0, 30000, 10000, 20, null, null, SplitMethod.HIGH_USAGE);
			endTimeHist3.setDamping(0.3);
			endTimeHist3.setMergeThresh(0.0001);
			endTimeHist3.setSplitThresh(0.1);
			endTimeHist3.setResQueryThresh(50);
			
			String line = bufRead.readLine();
			String[] parts;
			int est;
			int actual;
			int lower;
			int upper;
			String str = "";
			while (line != null) {
				parts = line.split(",");
				est = Integer.valueOf(parts[0]);
				actual = Integer.valueOf(parts[1]);
				lower = Integer.valueOf(parts[2]);
				upper = Integer.valueOf(parts[3]);
				
				str = "Estimate 0: " + est + "\n";
				str += "Estimate 1: " + endTimeHist1.get(lower, upper) + "\n";
				str += "Estimate 2: " + endTimeHist2.get(lower, upper) + "\n";
				str += "Estimate 3: " + endTimeHist3.get(lower, upper) + "\n";
				str += "Actual:     " + actual + "\n";
				
				endTimeHist1.receive(lower, upper, actual);
				endTimeHist2.receive(lower, upper, actual);
				endTimeHist3.receive(lower, upper, actual);
				
				System.out.println(str);
				
				line = bufRead.readLine();
			}

			bufRead.close();

		} catch (FileNotFoundException e) {
			// fail...
			System.err.println("You must specify the absolute pathname for your data");
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("You must specify the absolute pathname for your data");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
