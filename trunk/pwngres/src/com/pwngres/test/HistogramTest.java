package com.pwngres.test;

import static com.pwngres.histogram.MicrosoftSTHistogram.RefinementMethod.FREQUENCY;
import static com.pwngres.histogram.MicrosoftSTHistogram.RefinementMethod.HYBRID;
import static com.pwngres.histogram.MicrosoftSTHistogram.RefinementMethod.NORMAL;
import static com.pwngres.histogram.MicrosoftSTHistogram.RefinementMethod.RANGE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pwngres.histogram.MicrosoftSTHistogram;

public class HistogramTest {
	
	private MicrosoftSTHistogram hist;
	
	@Test public void testInitialization() {
		hist = new MicrosoftSTHistogram(0, 100, 300, 10, null, null, null);
		
		for (int i = 0; i < 10; i++) {
			assertEquals(30, hist.get(i * 10, (i + 1) * 10));
		}
	}
	
	@Test public void testBucketRefinementFrequency() {
		hist = new MicrosoftSTHistogram(0, 100, 300, 10, FREQUENCY, null, null);
		hist.setDamping(1);
		
		hist.receive(30, 40, 80); // single complete bucket
		assertEquals(80, hist.get(30, 40));
		
		hist.receive(40, 45, 30); // single fractional bucket
		assertEquals(60, hist.get(40, 50));
		
		hist.receive(10, 30, 80); // multiple complete uniform buckets
		assertEquals(40, hist.get(10, 20));
		assertEquals(40, hist.get(20, 30));
		
		hist.receive(20, 40, 90); // multiple complete non-uniform buckets
		assertEquals(30, hist.get(20, 30));
		assertEquals(60, hist.get(30, 40));
		
		hist.receive(60, 75, 65); // multiple fractional uniform buckets
		assertEquals(43, hist.get(60, 70));
		assertEquals(43, hist.get(70, 80));
		assertEquals(21, hist.get(70, 75));
		
	}
	
	@Test public void testBucketRefinementRange() {
		hist = new MicrosoftSTHistogram(0, 100, 300, 10, RANGE, null, null);
	}
	
	@Test public void testBucketRefinementHybrid() {
		hist = new MicrosoftSTHistogram(0, 100, 300, 10, HYBRID, null, null);
		hist.setDamping(1);
		
		hist.receive(30, 40, 80); // single complete bucket
		assertEquals(80, hist.get(30, 40));
		
		hist.receive(40, 45, 30); // single fractional bucket
		assertEquals(45, hist.get(40, 50));
		
		hist.receive(10, 30, 80); // multiple complete uniform buckets
		assertEquals(40, hist.get(10, 20));
		assertEquals(40, hist.get(20, 30));
		
		hist.receive(20, 40, 90); // multiple complete non-uniform buckets
		assertEquals(30, hist.get(20, 30));
		assertEquals(60, hist.get(30, 40));
		
		hist.receive(60, 75, 65); // multiple fractional uniform buckets
		assertEquals(43, hist.get(60, 70));
		assertEquals(36, hist.get(70, 80));
		assertEquals(18, hist.get(70, 75));
		
		
	}
	
	@Test public void testBucketRefinementNormal() {
		hist = new MicrosoftSTHistogram(0, 100, 300, 10, NORMAL, null, null);
	}
	
	@Test public void testRestructureTriggerQueryNum() {
		hist = new MicrosoftSTHistogram(0, 100, 200, 10, null, null, null);
		hist.setDamping(1);
		hist.setMergeThresh(0.015); // merge if they're 0.015 * 200 = 3 units apart
		hist.setResQueryThresh(10); // restructure after 10 queries
		hist.setSplitThresh(0.2); // 2 buckets will be split
		
//		System.out.println(hist);
		
		hist.receive(0, 10, 10);
		hist.receive(10, 20, 13);
		hist.receive(20, 30, 17);
		hist.receive(30, 40, 14);
		hist.receive(40, 50, 13);
		hist.receive(50, 60, 11);
		hist.receive(60, 70, 25);
		hist.receive(70, 80, 70);
		hist.receive(80, 90, 10);
//		System.out.println(hist);
		hist.receive(90, 100, 30);
		
//		System.out.println(hist);
		
	}

	@Test public void testRestructureTriggerCumError() {
		
	}
	
	@Test public void testSplitMethodFrequency() {
		
	}
	
	@Test public void testSplitMethodError() {
		
	}
	
	@Test public void testSplitMethodUsage() {
		
	}
	
	
}
