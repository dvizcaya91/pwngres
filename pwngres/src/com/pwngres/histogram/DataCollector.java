package com.pwngres.histogram;

import java.util.Random;

import com.pwngres.histogram.MicrosoftSTHistogram.SplitMethod;


public class DataCollector {

	public static void main(String[] args) {

		// Seconds in day
		int day = 6 * 60 * 24;

		// Create some fake data
		Database data = new Database("id", "product_id", "end_time");
		Random rand = new Random();
		for (int i = 0; i < 10000; i++) {
			data.add(i, rand.nextInt(1000), i + day + (rand.nextInt(10) >= 5 ? 1 : -1) * rand.nextInt(day / 3));
		}

		MicrosoftSTHistogram endTimeHist1 = new MicrosoftSTHistogram(0, 30000, 10000, 20, null, null, null);
		endTimeHist1.setDamping(0.5);
		endTimeHist1.setMergeThresh(0.0001);
		endTimeHist1.setSplitThresh(0.1);
		endTimeHist1.setResQueryThresh(50);

		MicrosoftSTHistogram endTimeHist2 = new MicrosoftSTHistogram(0, 30000, 10000, 20, null, null, SplitMethod.HIGH_ERROR);
		endTimeHist2.setDamping(0.5);
		endTimeHist2.setMergeThresh(0.0001);
		endTimeHist2.setSplitThresh(0.1);
		endTimeHist2.setResQueryThresh(50);

		MicrosoftSTHistogram endTimeHist3 = new MicrosoftSTHistogram(0, 30000, 10000, 20, null, null, SplitMethod.HIGH_USAGE);
		endTimeHist3.setDamping(0.5);
		endTimeHist3.setMergeThresh(0.0001);
		endTimeHist3.setSplitThresh(0.1);
		endTimeHist3.setResQueryThresh(50);
		
		//
		
		MicrosoftSTHistogram productHist1 = new MicrosoftSTHistogram(0, 1000, 10000, 10, null, null, null);
		productHist1.setDamping(0.5);
		productHist1.setMergeThresh(0.0001);
		productHist1.setSplitThresh(0.1);
		productHist1.setResQueryThresh(50);

		MicrosoftSTHistogram productHist2 = new MicrosoftSTHistogram(0, 1000, 10000, 10, null, null, SplitMethod.HIGH_ERROR);
		productHist2.setDamping(0.5);
		productHist2.setMergeThresh(0.0001);
		productHist2.setSplitThresh(0.1);
		productHist2.setResQueryThresh(50);

		MicrosoftSTHistogram productHist3 = new MicrosoftSTHistogram(0, 1000, 10000, 10, null, null, SplitMethod.HIGH_USAGE);
		productHist3.setDamping(0.5);
		productHist3.setMergeThresh(0.0001);
		productHist3.setSplitThresh(0.1);
		productHist3.setResQueryThresh(50);

		int actual;
		boolean print = false;
		String str = "";
		for (int i = 10000; i < 20000; i++) {
			print = i % 600 == 0;
			if (i % 60 == 0) {
				int product = rand.nextInt(1000);
				if (print) {
					str = "Query 1 (i = " + i + ")" + "\t\tQuery 2\t\t\t\tQuery 3\n";
					str += "Estimate 1: " + endTimeHist1.get(0, i) + "\t\tEstimate 1: " + endTimeHist1.get(i, i + 2 * day) + "\t\tEstimate 1: " + productHist1.get(product, product) + "\n";
					str += "Estimate 2: " + endTimeHist2.get(0, i) + "\t\tEstimate 2: " + endTimeHist2.get(i, i + 2 * day) + "\t\tEstimate 2: " + productHist2.get(product, product) + "\n";
					str += "Estimate 3: " + endTimeHist3.get(0, i) + "\t\tEstimate 3: " + endTimeHist3.get(i, i + 2 * day) + "\t\tEstimate 3: " + productHist3.get(product, product) + "\n";
				}
				actual = data.get("end_time", 0, i);
				if (print)
					str += "Actual:     " + actual;
				endTimeHist1.receive(0, i, actual);
				endTimeHist2.receive(0, i, actual);
				endTimeHist3.receive(0, i, actual);

				actual = data.get("end_time", i, i + 2 * day);
				if (print)
					str += "\t\tActual:     " + actual;
				endTimeHist1.receive(i, i + 2 * day, actual);
				endTimeHist2.receive(i, i + 2 * day, actual);
				endTimeHist3.receive(i, i + 2 * day, actual);
				
				actual = data.get("product_id", product, product);
				if (print) {
					str += "\t\tActual:     " + actual + "\n";
					System.out.println(str);
				}
				productHist1.receive(product, product, actual);
				productHist2.receive(product, product, actual);
				productHist3.receive(product, product, actual);
			}
			data.add(i, rand.nextInt(10000), i + day + (rand.nextInt(10) >= 5 ? 1 : -1) * rand.nextInt(day / 3));
		}
	}

}
