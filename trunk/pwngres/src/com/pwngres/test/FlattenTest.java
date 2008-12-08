package com.pwngres.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.pwngres.parser.ArraysUtil;

import junit.framework.TestCase;

public class FlattenTest extends TestCase {

	private static List<String> flattenOne = new LinkedList<String>(); 
	private static List<String> flattenTwo = new LinkedList<String>(); 
	private static List<String> expected = new LinkedList<String>(); 
	
	static {
		flattenOne.add("this is a test "); 
		flattenOne.add("   this is a test"); 
		flattenOne.add("   this is a test"); 
		flattenOne.add("this is a test"); 
		
		flattenTwo.add("   this is a test"); 
		flattenTwo.add("         this is a test"); 
		flattenTwo.add("         this is a test"); 
		flattenTwo.add("   this is a test"); 

		expected.add("this is a test"); 
		expected.add("      this is a test"); 
		expected.add("      this is a test"); 
		expected.add("this is a test"); 
	}
	
	
	@Test 
	public void testOne() {
		List<String> flattenCopy = new LinkedList<String>(flattenOne); 
		ArraysUtil.flatten(flattenCopy); 
		
		assertEquals("Unequal strings", flattenCopy, flattenOne); 

		List<String> flattenCopyTwo = new LinkedList<String>(flattenTwo); 
		ArraysUtil.flatten(flattenCopyTwo); 
		assertEquals("Flatten error", flattenCopyTwo, expected); 
	
	
	}

}

