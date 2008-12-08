package com.pwngres.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.pwngres.adt.Join;
import com.pwngres.adt.Operator;
import com.pwngres.adt.QueryPlan;
import com.pwngres.adt.Select;

public class QueryPlanBuilder {

	String SEQ_SCAN = "Seq Scan";
	String INDEX_SCAN = "Index Scan";
	String NESTED_LOOP = "Nested Loop";
	String MERGE_JOIN = "Merge Join";
	String HASH_JOIN = "Hash Join";
	String MATERIALIZE = "Materialize";
	
	String SPACE = "  ";
	String ARROW = "->";
	
	public QueryPlanBuilder() {}
	
	
	/**
	 * Consider removing this... just use the two lines  inside instead . 
	 * Otherwise, each operationparser will need to know about this class too (sucks)
	 * @param textPlan
	 * @return
	 */
	
	public Operator buildPlan(List<String> textPlan) {
		
		if (textPlan == null || textPlan.isEmpty()) 
			return null;
		
		// plan not empty. 
		// figure out what the root operation is and 
		// dispatch to appropriate parser
		OperatorParser parser = ParserFactory.parserFor(PostgresOp.typeOf(textPlan)); 
		Operator plan = parser.parse(textPlan); 
		
		return plan;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public QueryPlan parseAnalyseQueryPlan(List<String> queryPlan) {
		
		for (int i = 0 ; i < queryPlan.size() ; i++) {
			System.out.println(queryPlan.get(i));
		}
		
		String firstLine = queryPlan.get(0);
		String secondLine = null;
		if (queryPlan.size() > 2) {		
			secondLine = queryPlan.get(1);
		}
		
		String rootId = getOperator(queryPlan.get(0));
		Operator root = createOperator(rootId, firstLine, secondLine);
		root.setOutputDestination(null);
		root.setInputs(
				buildTree(
						queryPlan.subList(1, queryPlan.size() - 1), 
						root, 
						SPACE.length()));

		return new QueryPlan(root);
	}
	
	private List<Operator> buildTree(List<String> queryPlan, Operator root, int stringIndex) {
		
		List<Integer> nodesIndices = new ArrayList<Integer>();
		List<Operator> parentNodes = new ArrayList<Operator>();
		
		for (int i = 0 ; i < queryPlan.size() ; i++) {
			String line = queryPlan.get(i).substring(stringIndex);		
			if (line.startsWith(ARROW)) {
				nodesIndices.add(i);
			}
		}
		
		if (nodesIndices.size() == 0)
			return null;
		else {		
			for (int i = 0 ; i < nodesIndices.size() ; i++) {
				int index = nodesIndices.get(i);
				
				int queryPlanBound;
				if (i+2 <= nodesIndices.size())
					queryPlanBound = nodesIndices.get(i+1);
				else
					queryPlanBound = queryPlan.size();
				
				
				String firstLine = queryPlan.get(index);
				String secondLine = null;
				if (index+1 < queryPlanBound) {
					secondLine = queryPlan.get(index + 1);
				}
				
				String operatorId = getOperator(firstLine);
				Operator operator = createOperator(operatorId, firstLine, secondLine);				
				operator.setOutputDestination(root);
				
				operator.setInputs(
						buildTree(queryPlan.subList(index+1, queryPlanBound), 
								operator, 
								stringIndex + ARROW.length() + 2*SPACE.length()));
				
				parentNodes.add(operator);
			}
			
			return parentNodes;
		}
	}
	
	private Operator createOperator(String id, String firstLine, String secondLine) {
		if (isSelect(id)) {
			Select select = new Select(id);
			
			// get table
			String on = "on ";
			int beginIndex = firstLine.indexOf(on) + on.length();
			int endIndex = firstLine.indexOf(" ", beginIndex);
			select.setTable(firstLine.substring(beginIndex, endIndex));
			
			// get condition, if any		
			if (secondLine != null && secondLine.startsWith(SPACE) && !secondLine.contains(ARROW)) {
				 //select.setCondition(secondLine.trim()); 
			}	
			return select;
			
		} else if (isJoin(id)) {
			
			Join join = new Join(id);
			
			// get condition, if any		
			if (secondLine != null && secondLine.startsWith(SPACE) && !secondLine.contains(ARROW)) {
				 //join.setConditions(Arrays.asList(secondLine.trim())); 
			}	
			return join;	
			
		} else {
			return new Operator(id);			
		}
	}
	
	/**
	 * Returns the type of operator from a line of EXPLAIN ANALYZE
	 * 
	 * @param line A line containing the description of the operator
	 * @return
	 */
	private String getOperator(String line) {
		if (line.contains(SEQ_SCAN))
			return SEQ_SCAN;
		else if (line.contains(INDEX_SCAN))
			return INDEX_SCAN;
		else if (line.contains(NESTED_LOOP))
			return NESTED_LOOP;
		else if (line.contains(MERGE_JOIN))
			return MERGE_JOIN;
		else if (line.contains(HASH_JOIN))
			return HASH_JOIN;
		else if (line.contains(MATERIALIZE))
			return MATERIALIZE;
		else 
			throw new RuntimeException("Failed to recognize operator in line " + line);
	}
	
	private boolean isSelect(String id) {
		if (id.equals(SEQ_SCAN))
			return true;
		else if (id.equals(INDEX_SCAN))
			return true;
		else
			return false;
	}
	
	private boolean isJoin(String id) {
		if (id.equals(NESTED_LOOP))
			return true;
		else if (id.equals(MERGE_JOIN))
			return true;
		else if (id.equals(HASH_JOIN))
			return true;
		else
			return false;
	}

	
	/**
	 * OLD CODE, might be useful
	 */
	private static String[] getNumberOfRows(String line)  {
		String rows = "rows=";
		int start = line.indexOf(rows) + rows.length();
		int end = line.indexOf(" ", start);
		String rowsE = line.substring(start, end);
		
		start = line.indexOf(rows, end) + rows.length();
		end = line.indexOf(" ", start);
		String rowsA = line.substring(start, end);
		
		String[] r = new String[]{rowsE, rowsA};
		return r;
	}
	
}
