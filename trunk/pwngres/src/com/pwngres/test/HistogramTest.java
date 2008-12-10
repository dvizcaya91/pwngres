package com.pwngres.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pwngres.histogram.SelfTuningHistogram;

public class HistogramTest {
	
	SelfTuningHistogram hist;
	
	@Test public void testInitialization() {
		hist = new SelfTuningHistogram(0, 100, 300, 10);
		
		for (int i = 0; i < 10; i++) {
			assertEquals(30, hist.get(i * 10, (i + 1) * 10));
		}
	}
	
	@Test public void testUpdateSimple() {
		hist = new SelfTuningHistogram(0, 100, 300, 10);
		
//		System.out.println(hist);
//		
//		hist.receive(30, 50, 80);
//		
//		System.out.println(hist);
		
	}
	
	@Test public void testRestructure() {
		hist = new SelfTuningHistogram(0, 100, 200, 10);
		hist.setDamping(1);
		hist.setMergeThresh(0.015);
		hist.setRestructureThresh(10);
		hist.setSplitThresh(0.2);
		
		System.out.println(hist);
		
		hist.receive(0, 10, 10);
		hist.receive(10, 20, 13);
		hist.receive(20, 30, 17);
		hist.receive(30, 40, 14);
		hist.receive(40, 50, 13);
		hist.receive(50, 60, 11);
		hist.receive(60, 70, 25);
		hist.receive(70, 80, 70);
		hist.receive(80, 90, 10);
		System.out.println(hist);
		hist.receive(90, 100, 30);
		
		System.out.println(hist);
		
	}

}
