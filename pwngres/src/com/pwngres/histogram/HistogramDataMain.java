//package com.pwngres.histogram;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//import convert.DataConverter;
//
//public class HistogramDataMain {
//	
//	public static void main(String[] args) {
//		
//		SelfTuningHistogram hist = new SelfTuningHistogram(0, 100000, 5000000, 10);
//		hist.setDamping(0.7);
//		hist.setMergeThresh(0.2);
//		hist.setRestructureThresh(100);
//		hist.setSplitThresh(0.2);
//		
//		try {
//			// create input buffer
//			System.out.println(args[0]);
//			FileReader input = new FileReader(args[0] + "\\hist-data.txt");
//			BufferedReader bufRead = new BufferedReader(input);
//			
//			DataConverter converter = new DataConverter(bufRead, args[1], args[2]);
//			converter.convertDataFromRegistrarToStudents(args[0] + "\\classes" + File.separator);
//			
//			
//			System.out.println(hist);
//			
//			for (int i = 0; i < 10000; i++) {
//				hist.receive(50000, 100000, i);
//				
//				System.out.println(hist.get(50000, 100000));
//			}
//			
//			System.out.println(hist);
//			
//			bufRead.close();
//			
//		} catch (FileNotFoundException e) {
//			// fail...
//			System.err.println("You must specify the absolute pathname for your data");
//			e.printStackTrace();
//		} catch (ArrayIndexOutOfBoundsException e) {
//			System.err.println("You must specify the absolute pathname for your data");
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
//
//}
