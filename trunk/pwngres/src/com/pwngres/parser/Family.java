package com.pwngres.parser;

/**
 * General classification of Postgres operations.
 * 
 * Currently supporting joins, scans, and misc
 * 
 * @author joseamuniz
 *
 */
public enum Family {
	JOIN, SCAN, MISC;
}