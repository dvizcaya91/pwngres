package com.pwngres.test;

import java.util.Arrays;
import java.util.List;

import com.pwngres.parser.ArraysUtil;

import junit.framework.TestCase;


public class SubArrayTest extends TestCase {

	List<String> arrayOne= Arrays.asList("cat", "dog", "Julian", "Rodrigo", "Jose"); 
	
	public void testOneRange() {
		
		assertEquals("range 0,1 failed", ArraysUtil.subArray(arrayOne, 0,1), Arrays.asList("cat", "dog"));
		assertEquals("range 0,2 failed", ArraysUtil.subArray(arrayOne, 0,2), Arrays.asList("cat", "dog", "Julian"));
		assertEquals("range 0,3 failed", ArraysUtil.subArray(arrayOne, 0,3), Arrays.asList("cat", "dog", "Julian", "Rodrigo"));
		assertEquals("range 1,2 failed", ArraysUtil.subArray(arrayOne, 1,2), Arrays.asList("dog", "Julian"));
		assertEquals("range 3,3 failed", ArraysUtil.subArray(arrayOne, 3,3), Arrays.asList("Rodrigo"));
		
	}
	
	public void testTwoRanges() {
		
		assertEquals("range 0,1,3,3 failed", ArraysUtil.subArray(arrayOne, 0,1, 3, 3), Arrays.asList("cat", "dog", "Rodrigo"));
		assertEquals("range 0,1,3,4 failed", ArraysUtil.subArray(arrayOne, 0,1, 3, 4), Arrays.asList("cat", "dog", "Rodrigo", "Jose"));
		
	}
	
}
