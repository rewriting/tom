package tomchecker; 

import tom.library.enumerator.*;
import java.util.Random;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Checker {

	// Definition of constants
	public static final BigInteger ZERO = BigInteger.ZERO;
	public static final BigInteger ONE = BigInteger.ONE;

	// ---------------- QuickCheck Prod1 by default -------------------------------------------------
	public static <T> void quickCheckProd1(int depth,  Enumeration< Product1<T> > enumeration,
																				 F<Product1<T>, Boolean> prop, int verbose, boolean shrink,
																				 int quotient, Set<F<Product1<T>, Boolean>> preCond, BigInteger
																				 defaultNumOfTest, boolean existential) {
								
		long startTime = System.currentTimeMillis();
		Map<Integer, Product1<T>> counterExamples = new HashMap<Integer, Product1<T>>();

		quickCheckProd1(depth, enumeration, prop, verbose, shrink, quotient, preCond, counterExamples,
										defaultNumOfTest, startTime, existential);
	}
	
	// ---------------- QuickCheck Prod1 ------------------------------------------------------------
	// TODO: if shrink - remember tested inputs to avoid playing them again
	public static <T> void quickCheckProd1(int depth,  Enumeration< Product1<T> > enumeration,
																				 F<Product1<T>, Boolean> prop, int verbose, boolean shrink,
																				 int quotient, Set<F<Product1<T>, Boolean>> preCond,
																				 Map<Integer, Product1<T>> counterExamples, BigInteger
																				 defaultNumOfTest, long startTime, boolean existential) {
		
    BigInteger totalSize = ZERO;
    BigInteger[] nbProbes = new BigInteger[depth];

    logMessage(1, verbose,"RANDOM CHECK AT DEPTH " + depth);

    // If no smaller counter example
    if(depth == 0) {
      assert false :  "FAILED  tests: " + counterExamples;
    }

		// Verify and recompute (if necessary) the input value for depth
		depth = recomputeDepthProd1(depth, enumeration, verbose);

		// Computes totalSize and number of probes for each depth
		totalSize = sizeAndProbesProd1(depth, enumeration, nbProbes);
    logMessage(2, verbose,"TOTAL number of inputs " + totalSize);

		// Calculate the Default Number of Tests
		defaultNumOfTest = calcDNT(quotient, totalSize);
		logMessage(2, verbose, "DEFAULT number of tests " + defaultNumOfTest);

		// Recompute number of test inputs to distribute test cases among depths
		recomputeProbes(depth, nbProbes, defaultNumOfTest, totalSize);

		if(existential){
			// Look for one valid example, that is, a example which respects the properties
			findExistQuantProd1(nbProbes, preCond, prop, enumeration, startTime, depth, verbose);
		}else{
			// Perform tests for each enumeration part
			performTestsProd1(nbProbes, preCond, prop, counterExamples, shrink, enumeration, verbose, quotient,
									 defaultNumOfTest, startTime, depth, existential);
		}
    
  }

	// ---------------- Recompute Depth Prod1 -------------------------------------------------------
	/*
	 * If the enumeration has a very small valid depth (less than that passed as argument), it have to 
	 * be recomputed, but we need to be attentive to infinite depths
	*/ 
	public static <T> int recomputeDepthProd1(int depth, Enumeration< Product1<T> > enumeration,
																						int verbose){

		LazyList<Finite<Product1<T>>> parts = enumeration.parts();
		
		int suitableDepth = 0;
    while( suitableDepth < depth && !parts.isEmpty() ) {
      parts = parts.tail();
	    suitableDepth++;
    }
    if(depth > suitableDepth){
	    logMessage(2, verbose, "DEPTH " + depth + " is invalid. Maximum valid depth is " + suitableDepth);
    }
    return suitableDepth;
	}

	// ---------------- Size And Probes Prod1 -------------------------------------------------------
	// Computes totalSize and number of probes for each depth
	public static <T> BigInteger sizeAndProbesProd1(int depth, Enumeration< Product1<T> > enumeration,
																									BigInteger[] nbProbes){

		LazyList<Finite<Product1<T>>> parts = enumeration.parts();
		BigInteger totalSize = ZERO;

    for(int i = 0; i < depth; i++) {
      nbProbes[i] = parts.head().getCard();
      totalSize = totalSize.add(nbProbes[i]);
      parts = parts.tail();
    }
    
		return totalSize;
	}
	
	// ---------------- Calculate the Default Number of Tests ---------------------------------------
	public static BigInteger calcDNT (int quotient, BigInteger totalSize){

		BigInteger defaultNumOfTest;
		Random rand = new Random();
		BigInteger ratio;

		// quotient should be at least 1
		if(quotient == 0){
			quotient = 1;
		}

		ratio = totalSize.divide(BigInteger.valueOf(quotient));

	  if(ratio.compareTo(ZERO) == 0){
	    defaultNumOfTest =  ONE;
	  }else {

			do {
	      defaultNumOfTest = new BigInteger(totalSize.bitLength(), rand);
	    }while(defaultNumOfTest.compareTo(ratio) >= 0);
			// defaultNumOfTest should be at least 1
			defaultNumOfTest = defaultNumOfTest.add(ONE);
	  }

		return defaultNumOfTest;
	}

	// ---------------- Recompute Probes ------------------------------------------------------------
	// Recompute number of test inputs to distribute test cases among depths
	public static void recomputeProbes (int depth, BigInteger[] nbProbes, BigInteger defaultNumOfTest,
																			BigInteger totalSize) {
		BigInteger proportion;
		
    for(int i = 0; i < depth; i++) {
			// if not an empty part
      if(nbProbes[i].compareTo(ZERO) == 1) {
		    proportion = (nbProbes[i].multiply(defaultNumOfTest)).divide(totalSize);
	    	if( proportion.compareTo(ZERO) == 0 ) {
	        nbProbes[i] = ONE;
	    	}else {
	        nbProbes[i] = proportion;
	    	}
      }
    }

	}

	// ---------------- Find Existential Quantifier Prod1 -------------------------------------------
  // Perform tests for each enumeration part in order to find a valid example
	public static <T> void findExistQuantProd1(BigInteger[] nbProbes, Set<F<Product1<T>, Boolean>> preCond,
																						 F<Product1<T>, Boolean> prop,
																						 Enumeration< Product1<T> > enumeration,
																						 long startTime, int depth, int verbose) {

		LazyList<Finite<Product1<T>>> parts = enumeration.parts();
		Product1<T> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
    boolean existQuantFound = false;

		// Label for the loop
		searchInDepths:
    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && limit <= 0) {
      
				input =	findValidInputProd1(card, verbose, testedInputs, parts, preCond, i);
				
				/*
				* If there is no valid input from a depth (all inputs from a depth have not 
				* satisfied the pre-conditions ), we move to the next depth
				*/
				if(input == null){
					break;
				}

		    if(prop.apply(input)) {
		    	existQuantFound = true;
	    		break searchInDepths;
	    	}

	    	counter = counter.add(ONE);
				limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));
	    }
	    
	    if(input == null){
	      logMessage(2, verbose,"AT DEPTH " + i + " no input from the set with cardinality " + card + " have satisfied the pre-conditions");
	    }else{
	      logMessage(2, verbose,"AT DEPTH " + i + " GENERATED " + testedInputs.size() + " random inputs for the set with cardinality = " + card);
	    }

	    parts = parts.tail();
	    
    }
    
		if(existQuantFound == false){
			System.out.println("Existential quantifier not found. It's recommended to test this property with smallCheck");
		}else{
			System.out.println("Existential quantifier found:\n\tinput " + input + " passed");
		}

		long endTime = System.currentTimeMillis();
		double time = (endTime - startTime) / 1000;
		System.out.println("Finished in " + time + " seconds");

  }

	// ---------------- Perform Tests Prod1 ---------------------------------------------------------
  // Perform tests for each enumeration part
	public static <T> void performTestsProd1(BigInteger[] nbProbes, Set<F<Product1<T>, Boolean>> preCond,
																					 F<Product1<T>, Boolean> prop,
																					 Map<Integer,Product1<T>> counterExamples, boolean shrink,
																					 Enumeration< Product1<T> > enumeration, int verbose,
																					 int quotient, BigInteger defaultNumOfTest, long startTime,
																					 int depth, boolean existential) {

		LazyList<Finite<Product1<T>>> parts = enumeration.parts();
		Product1<T> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
		BigInteger numOfTestCase = ZERO;
    boolean failed = false;

    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && !failed && limit <= 0) {

				input =	findValidInputProd1(card, verbose, testedInputs, parts, preCond, i);

				/*
				* If there is no valid input from a depth (all inputs from a depth have not 
				* satisfied the pre-conditions ), we move to the next depth
				*/
				if(input == null){
					break;
				}

				// New input found, so we can test it
				numOfTestCase = numOfTestCase.add(ONE);

		    if(!prop.apply(input)) {
		    	counterExamples.put(i, input);
		      failed = true;
		      // Look for counter examples in smaller depths
		      if(shrink) {
		      	logMessage(3, verbose,"SHRINKING at depth " + i + "  " + input);
						// Note the first argument : it adjusts the depth to simulate a shrink
		      	quickCheckProd1(i, enumeration, prop, verbose, shrink, quotient, preCond, 
														counterExamples, defaultNumOfTest, startTime, existential);
		      }
		    }
		    	
	    	counter = counter.add(ONE);
				limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      }

			if(input == null){

	      logMessage(2, verbose,"AT DEPTH " + i + " no input from the set with cardinality " + card + " have satisfied the pre-conditions");
	      
			}else{
			
				logMessage(2, verbose,"AT DEPTH " + i + " GENERATED " + testedInputs.size() + " random inputs for the set with cardinality = " + card);
				
		    if(failed){
		      String errorMessage = "FAILED test(s): ";
		      for(Integer size: counterExamples.keySet()){
		        errorMessage += "\n\t " + counterExamples.get(size) + " in depth " + size;
		      }
		      long endTime = System.currentTimeMillis();
		      double time = (endTime - startTime) / 1000;
		      assert false :  errorMessage + "\n" + "Finished in " + time + " seconds";
		    }

			}

      parts = parts.tail();

    }

    if(counterExamples.size() == 0){

      if(numOfTestCase.compareTo(ZERO)==0){
      	System.out.println("WARNING : No inputs have been tested. It's recommended to test this property with smallCheck");
      }else{
		    System.out.println("OK, passed " + numOfTestCase + " tests");
      }

      long endTime = System.currentTimeMillis();
      double time = (endTime - startTime) / 1000;
	    System.out.println("Finished in " + time + " seconds");

    }

	}

	// ---------------- Find a valid input (from a specified depth) to be tested --------------------
	// A valid input does not necessarily mean that the input will not fail
	public static <T> Product1<T> findValidInputProd1(BigInteger card, int verbose, Set<BigInteger>
																										testedInputs, LazyList<Finite<Product1<T>>> parts,
																										Set<F<Product1<T>, Boolean>> preCond, int currDepth){

		Product1<T> input = null;
    BigInteger index;
		Random rand = new Random();
		boolean implication = false;
		boolean foundValidInput = false;

		// Contains the inputs that have not satified the pre-conditions
		List<BigInteger> preCondNotSatisfied = new ArrayList();

		while(!foundValidInput) {
    	index = new BigInteger(card.bitLength(), rand);
	    logMessage(5, verbose, "TRY: " + index);
	    
      // Get an input that hasn't been tested yet
			if( index.compareTo(card) >= 0 || testedInputs.contains(index) || preCondNotSatisfied.contains(index) ) {
				continue;
			}else{
				input = parts.head().get(index);
				logMessage(3, verbose, "depth: " + currDepth + " input: " + input);

			  /*
				* If there is any implication, then check if they are satisfied.
				* Otherwise pick another input
				*/
			  for(F<Product1<T>, Boolean> entry : preCond) {
					if(!entry.apply(input)) {
						implication = true;
						preCondNotSatisfied.add(index);
						break;
	        }
				}
				
				if(implication) {
        	logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
					// If all inputs from a depth have not satisfied the pre-conditions
					BigInteger size = BigInteger.valueOf( preCondNotSatisfied.size() );
					if( size.compareTo(card) >= 0 ) {
						return null;
					}else{
						implication = false;
						continue;
					}
				}
				foundValidInput = true;
	      testedInputs.add(index);
		  }
		}
		return input;
	}

	// ---------------- QuickCheck Prod2 by default -------------------------------------------------
	public static <T1,T2> void quickCheckProd2(int depth,  Enumeration< Product2<T1,T2> > enumeration,
																						 F< Product2<T1,T2>, Boolean> prop, int verbose, boolean shrink,
																						 int quotient, Set<F<Product2<T1,T2>, Boolean>> preCond,
																						 BigInteger defaultNumOfTest, boolean existential) {

		long startTime = System.currentTimeMillis(); 
		HashMap<Integer, Product2<T1,T2>> counterExamples = new HashMap<Integer, Product2<T1,T2>>();

		quickCheckProd2(depth, enumeration, prop, verbose, shrink, quotient, preCond, counterExamples,
										defaultNumOfTest, startTime, existential);
	}

	// ---------------- QuickCheck Prod2 ------------------------------------------------------------
  public static <T1,T2> void  quickCheckProd2(int depth,  Enumeration<Product2<T1,T2>> enumeration,
  																						F< Product2<T1,T2>, Boolean> prop, int verbose, boolean shrink,
  																						int quotient, Set<F<Product2<T1,T2>, Boolean>> preCond,
  																						Map<Integer, Product2<T1,T2>> counterExamples, BigInteger
  																						defaultNumOfTest, long startTime, boolean existential) {
	   
    BigInteger totalSize = ZERO;
    BigInteger[] nbProbes = new BigInteger[depth];
    
    logMessage(1,verbose,"RANDOM CHECK AT DEPTH " + depth);

    // if no smaller counter example    
    if(depth == 0) { 
      assert false :  "FAILED  tests: " + counterExamples;
    }
    
		// Verify and recompute (if necessary) the input value for depth
    depth = recomputeDepthProd2(depth, enumeration, verbose);
    
		// Computes totalSize and number of probes for each depth
		totalSize = sizeAndProbesProd2(depth, enumeration, nbProbes);
    logMessage(2, verbose,"TOTAL number of inputs " + totalSize);
    
		// Calculate the Default Number of Tests
		defaultNumOfTest = calcDNT(quotient, totalSize);
		logMessage(2, verbose, "DEFAULT number of tests " + defaultNumOfTest);
		
    // Recompute number of test inputs to distribute test cases among depths
		recomputeProbes(depth, nbProbes, defaultNumOfTest, totalSize);

		if(existential){
			// Look for one valid example, that is, a example which respects the properties
			findExistQuantProd2(nbProbes, preCond, prop, enumeration, startTime, depth, verbose);
		}else{
			// Perform tests for each enumeration part
			performTestsProd2(nbProbes, preCond, prop, counterExamples, shrink, enumeration, verbose, quotient,
									 defaultNumOfTest, startTime, depth, existential);
		}
    
  }
  
	// ---------------- Recompute Depth Prod2 -----------------------------------------------------------
	/*
	 * If the enumeration has a very small valid depth (less than that passed as argument), it have to 
	 * be recomputed, but we need to be attentive to infinite depths
	*/ 
	public static <T1,T2> int recomputeDepthProd2(int depth, Enumeration< Product2<T1,T2> > enumeration,
																								int verbose){

		LazyList<Finite<Product2<T1,T2>>> parts = enumeration.parts();
		
		int suitableDepth = 0;
    while( suitableDepth < depth && !parts.isEmpty() ){
      parts = parts.tail();
	    suitableDepth++;
    }
    if(depth > suitableDepth){
	    logMessage(2, verbose, "DEPTH " + depth + " is invalid. Maximum valid depth is " + suitableDepth);
    }
    return suitableDepth;
	}
	
	// ---------------- Size And Probes Prod2 -------------------------------------------------------------
	// Computes totalSize and number of probes for each depth
	public static <T1,T2> BigInteger sizeAndProbesProd2(int depth, Enumeration< Product2<T1,T2> > enumeration,
																											BigInteger[] nbProbes){

		LazyList<Finite<Product2<T1,T2>>> parts = enumeration.parts();
		BigInteger totalSize = ZERO;

    for(int i = 0; i < depth; i++) {
      nbProbes[i] = parts.head().getCard();
      totalSize = totalSize.add(nbProbes[i]);
      parts = parts.tail();
    }
    
		return totalSize;
	}
	
	// ---------------- Find Existential Quantifier Prod2 -------------------------------------------
  // Perform tests for each enumeration part in order to find a valid example
	public static <T1,T2> void findExistQuantProd2(BigInteger[] nbProbes, Set<F<Product2<T1,T2>, Boolean>>
																								 preCond, F<Product2<T1,T2>, Boolean> prop,
																								 Enumeration< Product2<T1,T2> > enumeration,
																								 long startTime, int depth, int verbose) {

		LazyList<Finite<Product2<T1,T2>>> parts = enumeration.parts();
		Product2<T1,T2> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
    Boolean existQuantFound = false;

		// Label for the loop
		searchInDepths:
    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && limit <= 0) {
      
				input =	findValidInputProd2(card, verbose, testedInputs, parts, preCond, i);
				
				/*
				* If there is no valid input from a depth (all inputs from a depth have not 
				* satisfied the pre-conditions ), we move to the next depth
				*/
				if(input == null){
					break;
				}

		    if(prop.apply(input)) {
		    	existQuantFound = true;
	    		break searchInDepths;
	    	}

	    	counter = counter.add(ONE);
				limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));
	    }
	    
	    if(input == null){
	      logMessage(2, verbose,"AT DEPTH " + i + " no input from the set with cardinality " + card + " have satisfied the pre-conditions");
	    }else{
	      logMessage(2, verbose,"AT DEPTH " + i + " GENERATED " + testedInputs.size() + " random inputs for the set with cardinality = " + card);
	    }

	    parts = parts.tail();
	    
    }
    
		if(existQuantFound == false){
			System.out.println("Existential quantifier not found. It's recommended to test this property with smallCheck");
		}else{
			System.out.println("Existential quantifier found:\n\tinput " + input + " passed");
		}

		long endTime = System.currentTimeMillis();
		double time = (endTime - startTime) / 1000;
		System.out.println("Finished in " + time + " seconds");

  }
  
	// ---------------- Perform Tests Prod2 ---------------------------------------------------------
  // Perform tests for each enumeration part
	public static <T1,T2> void performTestsProd2(BigInteger[] nbProbes, Set<F<Product2<T1,T2>, Boolean>>
																							 preCond, F<Product2<T1,T2>, Boolean> prop,
																							 Map<Integer,Product2<T1,T2>> counterExamples,
																							 boolean shrink, Enumeration< Product2<T1,T2> > enumeration,
																							 int verbose, int quotient, BigInteger defaultNumOfTest,
																							 long startTime, int depth, boolean existential) {

		LazyList<Finite<Product2<T1,T2>>> parts = enumeration.parts();
		Product2<T1,T2> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
		BigInteger numOfTestCase = ZERO;
    boolean failed = false;

    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && !failed && limit <= 0) {

				input =	findValidInputProd2(card, verbose, testedInputs, parts, preCond, i);

				/*
				* If there is no valid input from a depth (all inputs from a depth have not 
				* satisfied the pre-conditions ), we move to the next depth
				*/
				if(input == null){
					break;
				}

				// New input found, so we can test it
				numOfTestCase = numOfTestCase.add(ONE);

		    if(!prop.apply(input)) {
		    	counterExamples.put(i, input);
		      failed = true;
		      // Look for counter examples in smaller depths
		      if(shrink) {
		      	logMessage(3, verbose,"SHRINKING at depth " + i + "  " + input);
						// Note the first argument : it adjusts the depth to simulate a shrink
		      	quickCheckProd2(i, enumeration, prop, verbose, shrink, quotient, preCond, 
														counterExamples, defaultNumOfTest, startTime, existential);
		      }
		    }
		    	
	    	counter = counter.add(ONE);
				limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      }

			if(input == null){

	      logMessage(2, verbose,"AT DEPTH " + i + " no input from the set with cardinality " + card + " have satisfied the pre-conditions");
	      
			}else{
			
				logMessage(2, verbose,"AT DEPTH " + i + " GENERATED " + testedInputs.size() + " random inputs for the set with cardinality = " + card);
				
		    if(failed){
		      String errorMessage = "FAILED test(s): ";
		      for(Integer size: counterExamples.keySet()){
		        errorMessage += "\n\t " + counterExamples.get(size) + " in depth " + size;
		      }
		      long endTime = System.currentTimeMillis();
		      double time = (endTime - startTime) / 1000;
		      assert false :  errorMessage + "\n" + "Finished in " + time + " seconds";
		    }

			}

      parts = parts.tail();

    }

    if(counterExamples.size() == 0){

      if(numOfTestCase.compareTo(ZERO)==0){
      	System.out.println("WARNING : No inputs have been tested. It's recommended to test this property with smallCheck");
      }else{
		    System.out.println("OK, passed " + numOfTestCase + " tests");
      }

      long endTime = System.currentTimeMillis();
      double time = (endTime - startTime) / 1000;
	    System.out.println("Finished in " + time + " seconds");

    }

	}
	
	// ---------------- Find a valid input (from a specified depth) to be tested --------------------
	// A valid input does not necessarily mean that the input will not fail
	public static <T1,T2> Product2<T1,T2> findValidInputProd2(BigInteger card, int verbose, Set<BigInteger>
																														testedInputs, LazyList<Finite<Product2<T1,T2>>>
																														parts, Set<F<Product2<T1,T2>, Boolean>> preCond,
																														int currDepth){

		Product2<T1,T2> input = null;
    BigInteger index;
		Random rand = new Random();
		boolean implication = false;
		boolean foundValidInput = false;

		// Contains the inputs that have not satified the pre-conditions
		List<BigInteger> preCondNotSatisfied = new ArrayList();

		while(!foundValidInput){
    	index = new BigInteger(card.bitLength(), rand);
	    logMessage(5, verbose, "TRY: " + index);
	    
      // Get an input that hasn't been tested yet
			if( index.compareTo(card) >= 0 || testedInputs.contains(index) || preCondNotSatisfied.contains(index) ) {
				continue;
			}else{
				input = parts.head().get(index);
				logMessage(3, verbose, "depth: " + currDepth + " input: " + input);

			  /*
				* If there is any implication, then check if they are satisfied.
				* Otherwise pick another input
				*/
			  for(F<Product2<T1,T2>, Boolean> entry : preCond) {
					if(!entry.apply(input)) {
						implication = true;
						preCondNotSatisfied.add(index);
						break;
	        }
				}
				
				if(implication){
        	logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
					// If all inputs from a depth have not satisfied the pre-conditions
					BigInteger size = BigInteger.valueOf( preCondNotSatisfied.size() );
					if( size.compareTo(card) >= 0 ){
						return null;
					}else{
						implication = false;
						continue;
					}
				}
				foundValidInput = true;
	      testedInputs.add(index);
		  }
		}
		return input;
	}

	// ---------------- QuickCheck Prod3 by default -------------------------------------------------
	public static <T1,T2,T3> void quickCheckProd3(int depth,  Enumeration< Product3<T1,T2,T3> >
																								enumeration, F< Product3<T1,T2,T3>, Boolean> prop,
																								int verbose, boolean shrink, int quotient,
																								Set<F< Product3<T1,T2,T3>, Boolean>> preCond,
																								BigInteger defaultNumOfTest, boolean existential){

		long startTime = System.currentTimeMillis();
		Map<Integer, Product3<T1,T2,T3>> counterExamples = new HashMap<Integer,Product3<T1,T2,T3>>();

		quickCheckProd3(depth, enumeration, prop, verbose, shrink, quotient, preCond, counterExamples,
										defaultNumOfTest, startTime, existential);
										
	}

	// ---------------- QuickCheck Prod3 ------------------------------------------------------------
  public static <T1,T2,T3> void quickCheckProd3(int depth,  Enumeration<Product3<T1,T2,T3>>
																								enumeration, F< Product3<T1,T2,T3>, Boolean> prop,
																								int verbose, boolean shrink, int quotient,
																								Set<F<Product3<T1,T2,T3>, Boolean>> preCond,
																								Map<Integer,Product3<T1,T2,T3>> counterExamples, BigInteger
																								defaultNumOfTest, long startTime, boolean existential) {

		BigInteger totalSize = ZERO;
    BigInteger[] nbProbes = new BigInteger[depth];

    logMessage(1, verbose,"RANDOM CHECK AT DEPTH " + depth);

    // If no smaller counter example
    if(depth == 0){
      assert false :  "FAILED  tests: " + counterExamples;
    }

		// Verify and recompute (if necessary) the input value for depth
		depth = recomputeDepthProd3(depth, enumeration, verbose);

		// Computes totalSize and number of probes for each depth
		totalSize = sizeAndProbesProd3(depth, enumeration, nbProbes);
    logMessage(2, verbose,"TOTAL number of inputs " + totalSize);

		// Calculate the Default Number of Tests
		defaultNumOfTest = calcDNT(quotient, totalSize);
		logMessage(2, verbose, "DEFAULT number of tests " + defaultNumOfTest);

		// Recompute number of test inputs to distribute test cases among depths
		recomputeProbes(depth, nbProbes, defaultNumOfTest, totalSize);

		if(existential){
			// Look for one valid example, that is, a example which respects the properties
			findExistQuantProd3(nbProbes, preCond, prop, enumeration, startTime, depth, verbose);
		}else{
			// Perform tests for each enumeration part
			performTestsProd3(nbProbes, preCond, prop, counterExamples, shrink, enumeration, verbose, quotient,
									 defaultNumOfTest, startTime, depth, existential);
		}
    
  }
  
	// ---------------- Recompute Depth Prod3 -------------------------------------------------------
	/*
	 * If the enumeration has a very small valid depth (less than that passed as argument), it have to 
	 * be recomputed, but we need to be attentive to infinite depths
	*/ 
	public static <T1,T2,T3> int recomputeDepthProd3(int depth, Enumeration< Product3<T1,T2,T3> > enumeration,
																									 int verbose){

		LazyList<Finite<Product3<T1,T2,T3>>> parts = enumeration.parts();
		
		int suitableDepth = 0;
    while( suitableDepth < depth && !parts.isEmpty() ){
      parts = parts.tail();
	    suitableDepth++;
    }
    if(depth > suitableDepth){
	    logMessage(2, verbose, "DEPTH " + depth + " is invalid. Maximum valid depth is " + suitableDepth);
    }
    return suitableDepth;
	}

	// ---------------- Size And Probes Prod3 -------------------------------------------------------
	// Computes totalSize and number of probes for each depth
	public static <T1,T2,T3> BigInteger sizeAndProbesProd3(int depth, Enumeration< Product3<T1,T2,T3> >
																												 enumeration, BigInteger[] nbProbes){

		LazyList<Finite<Product3<T1,T2,T3>>> parts = enumeration.parts();
		BigInteger totalSize = ZERO;

    for(int i = 0; i < depth; i++) {
      nbProbes[i] = parts.head().getCard();
      totalSize = totalSize.add(nbProbes[i]);
      parts = parts.tail();
    }
    
		return totalSize;
	}

	// ---------------- Find Existential Quantifier Prod3 -------------------------------------------
  // Perform tests for each enumeration part in order to find a valid example
	public static <T1,T2,T3> void findExistQuantProd3(BigInteger[] nbProbes, Set<F<Product3<T1,T2,T3>, Boolean>>
																										preCond, F<Product3<T1,T2,T3>, Boolean> prop,
																										Enumeration< Product3<T1,T2,T3> > enumeration,
																										long startTime, int depth, int verbose) {

		LazyList<Finite<Product3<T1,T2,T3>>> parts = enumeration.parts();
		Product3<T1,T2,T3> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
    boolean existQuantFound = false;

		// Label for the loop
		searchInDepths:
    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && limit <= 0) {
      
				input =	findValidInputProd3(card, verbose, testedInputs, parts, preCond, i);
				
				/*
				* If there is no valid input from a depth (all inputs from a depth have not 
				* satisfied the pre-conditions ), we move to the next depth
				*/
				if(input == null){
					break;
				}

		    if(prop.apply(input)) {
		    	existQuantFound = true;
	    		break searchInDepths;
	    	}

	    	counter = counter.add(ONE);
				limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));
	    }
	    
	    if(input == null){
	      logMessage(2, verbose,"AT DEPTH " + i + " no input from the set with cardinality " + card + " have satisfied the pre-conditions");
	    }else{
	      logMessage(2, verbose,"AT DEPTH " + i + " GENERATED " + testedInputs.size() + " random inputs for the set with cardinality = " + card);
	    }

	    parts = parts.tail();
	    
    }
    
		if(existQuantFound == false){
			System.out.println("Existential quantifier not found. It's recommended to test this property with smallCheck");
		}else{
			System.out.println("Existential quantifier found:\n\tinput " + input + " passed");
		}

		long endTime = System.currentTimeMillis();
		double time = (endTime - startTime) / 1000;
		System.out.println("Finished in " + time + " seconds");

  }


	// ---------------- Perform Tests Prod3 ---------------------------------------------------------
  // Perform tests for each enumeration part
	public static <T1,T2,T3> void performTestsProd3(BigInteger[] nbProbes, Set<F<Product3<T1,T2,T3>, Boolean>>
																									preCond, F<Product3<T1,T2,T3>, Boolean> prop,
																									Map<Integer,Product3<T1,T2,T3>>	counterExamples,
																									boolean shrink, Enumeration< Product3<T1,T2,T3> > enumeration,
																									int verbose, int quotient, BigInteger defaultNumOfTest,
																									long startTime, int depth, boolean existential) {

		LazyList<Finite<Product3<T1,T2,T3>>> parts = enumeration.parts();
		Product3<T1,T2,T3> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
		BigInteger numOfTestCase = ZERO;
    boolean failed = false;

    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && !failed && limit <= 0) {

				input =	findValidInputProd3(card, verbose, testedInputs, parts, preCond, i);

				/*
				* If there is no valid input from a depth (all inputs from a depth have not 
				* satisfied the pre-conditions ), we move to the next depth
				*/
				if(input == null){
					break;
				}

				// New input found, so we can test it
				numOfTestCase = numOfTestCase.add(ONE);

		    if(!prop.apply(input)) {
		    	counterExamples.put(i, input);
		      failed = true;
		      // Look for counter examples in smaller depths
		      if(shrink) {
		      	logMessage(3, verbose,"SHRINKING at depth " + i + "  " + input);
						// Note the first argument : it adjusts the depth to simulate a shrink
		      	quickCheckProd3(i, enumeration, prop, verbose, shrink, quotient, preCond, 
														counterExamples, defaultNumOfTest, startTime, existential);
		      }
		    }
		    	
	    	counter = counter.add(ONE);
				limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      }

			if(input == null){

	      logMessage(2, verbose,"AT DEPTH " + i + " no input from the set with cardinality " + card + " have satisfied the pre-conditions");
	      
			}else{
			
				logMessage(2, verbose,"AT DEPTH " + i + " GENERATED " + testedInputs.size() + " random inputs for the set with cardinality = " + card);
				
		    if(failed){
		      String errorMessage = "FAILED test(s): ";
		      for(Integer size: counterExamples.keySet()){
		        errorMessage += "\n\t " + counterExamples.get(size) + " in depth " + size;
		      }
		      long endTime = System.currentTimeMillis();
		      double time = (endTime - startTime) / 1000;
		      assert false :  errorMessage + "\n" + "Finished in " + time + " seconds";
		    }

			}

      parts = parts.tail();

    }

    if(counterExamples.size() == 0){

      if(numOfTestCase.compareTo(ZERO)==0){
      	System.out.println("WARNING : No inputs have been tested. It's recommended to test this property with smallCheck");
      }else{
		    System.out.println("OK, passed " + numOfTestCase + " tests");
      }

      long endTime = System.currentTimeMillis();
      double time = (endTime - startTime) / 1000;
	    System.out.println("Finished in " + time + " seconds");

    }

	}
	
	// ---------------- Find a valid input (from a specified depth) to be tested --------------------
	// A valid input does not necessarily mean that the input will not fail
	public static <T1,T2,T3> Product3<T1,T2,T3> findValidInputProd3(BigInteger card, int verbose,
																																	Set<BigInteger> testedInputs,
																																	LazyList<Finite<Product3<T1,T2,T3>>> parts,
																																	Set<F<Product3<T1,T2,T3>, Boolean>> preCond,
																																	int currDepth){

		Product3<T1,T2,T3> input = null;
    BigInteger index;
		Random rand = new Random();
		boolean implication = false;
		boolean foundValidInput = false;

		// Contains the inputs that have not satified the pre-conditions
		List<BigInteger> preCondNotSatisfied = new ArrayList();

		while(!foundValidInput){
    	index = new BigInteger(card.bitLength(), rand);
	    logMessage(5, verbose, "TRY: " + index);
	    
      // Get an input that hasn't been tested yet
			if( index.compareTo(card) >= 0 || testedInputs.contains(index) || preCondNotSatisfied.contains(index) ) {
				continue;
			}else{
				input = parts.head().get(index);
				logMessage(3, verbose, "depth: " + currDepth + " input: " + input);

			  /*
				* If there is any implication, then check if they are satisfied.
				* Otherwise pick another input
				*/
			  for(F<Product3<T1,T2,T3>, Boolean> entry : preCond) {
					if(!entry.apply(input)) {
						implication = true;
						preCondNotSatisfied.add(index);
						break;
	        }
				}
				
				if(implication){
        	logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
					// If all inputs from a depth have not satisfied the pre-conditions
					BigInteger size = BigInteger.valueOf( preCondNotSatisfied.size() );
					if( size.compareTo(card) >= 0 ){
						return null;
					}else{
						implication = false;
						continue;
					}
				}
				foundValidInput = true;
	      testedInputs.add(index);
		  }
		}
		return input;
	}

	// ---------------- Recompute Depth -------------------------------------------------------------
	public static <T1,T2,T3> int recomputeDepth3(int depth, LazyList<Finite<Product3<T1,T2,T3>>> parts){

		int suitableDepth = 0;
    while( suitableDepth < depth && !parts.isEmpty() ){
      parts = parts.tail();
	    suitableDepth++;
    }
    return suitableDepth;
	}

	// ---------------- Log Message -----------------------------------------------------------------
  public static void  logMessage(Integer depth, int verbose, String message) {
    if(depth <= verbose){
      System.out.println(message);
    }
  }  
  
	// ---------------- Big Integer -----------------------------------------------------------------
  private static BigInteger min(BigInteger n1, BigInteger n2) {
    if(n1.compareTo(n2) <= 0) {
      return n1;
    } else {
      return n2;
    }
  }

	// ---------------- SmallCheck Prod1 ------------------------------------------------------------
  public static <T> void smallCheckProd1(int depth,  Enumeration< Product1<T> > enumeration,
																				 F<Product1<T>, Boolean> prop, int verbose,
																				 Set<F< Product1<T>, Boolean>> preCond, boolean existential) {
	
		long startTime = System.currentTimeMillis();
    Product1<T> input = null;
    LazyList<Finite<Product1<T>>> parts = enumeration.parts();

    BigInteger totalSize = ZERO;
    BigInteger counterTestedInputs = ZERO;
    BigInteger preCondNotSatisfied = ZERO;
    boolean implication = false;
    boolean failed = false;
    boolean existQuantFound = false;

		depth = recomputeDepthProd1(depth, enumeration, verbose);

    for(int i = 0; i < depth; i++) {
      totalSize = totalSize.add( parts.head().getCard() );
      parts = parts.tail();
    }

    parts = enumeration.parts();

		// Label for the loop
		searchInDepths:
		// For each depth...
    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      
  		// ... test all inputs
      for( BigInteger index = ZERO; index.compareTo(card) == -1; index = index.add(ONE) ) {
      
        input = parts.head().get(index);
        
        logMessage(3, verbose, "depth "  + i + ": " + index.add(ONE) + "/" + card + " input: " + input);
        
        // Test pre-conditions
        if(preCond.size() > 0) {
        	for(F<Product1<T>, Boolean> entry : preCond) {
        		if(!entry.apply(input)) {
        			implication = true;
        			preCondNotSatisfied = preCondNotSatisfied.add(ONE);
        			break;
        		}
        	}
        	if(implication) {
        		logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
        		implication = false;
        		continue;
        	}
        }

				// Either if input respected pre-conditions or there are no pre-conditions
        if(!implication) {
					counterTestedInputs = counterTestedInputs.add(ONE);
					// Test properties
					if(prop.apply(input)){
 						// If we are looking for an Existential Quantifier
 						if(existential){
							existQuantFound = true;
							break searchInDepths;
 						}
 					// Failed tests are not interesting when looking for an Existential Quantifier
					}else if(!existential){
						failed = true;
						break searchInDepths;
					}
				}
			}

      parts = parts.tail();

    }

  	long endTime = System.currentTimeMillis();
  	double time = (endTime - startTime) / 1000;
		
		if(existential){
			if(existQuantFound == false){
				System.out.println("Existential quantifier not found.");
			}else{
				System.out.println("Existential quantifier found:\n\tinput " + input + " passed after " + counterTestedInputs + " tests");
			}

		}else{
			if(failed){
				System.out.println("FAILED after: " + counterTestedInputs + "/" + totalSize + " tests.\n\tLast input: " + input);
			}else{
			  System.out.println("OK, passed " + counterTestedInputs + "/" + totalSize + " tests");
			}
		}

		System.out.println( preCondNotSatisfied + " generated inputs did not meet the pre-condition" );
		System.out.println("Total of generated inputs: " + counterTestedInputs.add(preCondNotSatisfied) );
		System.out.println("Finished in " + time + " seconds");

  }

	// ---------------- SmallCheck Prod2 ------------------------------------------------------------
  public static <T1,T2> void smallCheckProd2(int depth,  Enumeration< Product2<T1,T2> > enumeration,
  																					 F< Product2<T1,T2>, Boolean> prop, int verbose,
  																					 Set<F< Product2<T1,T2>, Boolean>> preCond,
  																					 boolean existential) {

		long startTime = System.currentTimeMillis();
    Product2<T1,T2> input = null;
    LazyList<Finite<Product2<T1,T2>>> parts = enumeration.parts();

    BigInteger totalSize = ZERO;
    BigInteger counterTestedInputs = ZERO;
    BigInteger preCondNotSatisfied = ZERO;
    boolean implication = false;
    boolean failed = false;
    boolean existQuantFound = false;

		depth = recomputeDepthProd2(depth, enumeration, verbose);

    for(int i = 0; i < depth; i++) {
      totalSize = totalSize.add( parts.head().getCard() );
      parts = parts.tail();
    }

    parts = enumeration.parts();

		// Label for the loop
		searchInDepths:
		// For each depth...
    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      
  		// ... test all inputs
      for( BigInteger index = ZERO; index.compareTo(card) == -1; index = index.add(ONE) ) {
      
        input = parts.head().get(index);
        
        logMessage(3, verbose, "depth "  + i + ": " + index.add(ONE) + "/" + card + " input: " + input);
        
        // Test pre-conditions
        if(preCond.size() > 0) {
        	for(F<Product2<T1,T2>, Boolean> entry : preCond) {
        		if(!entry.apply(input)) {
        			implication = true;
        			preCondNotSatisfied = preCondNotSatisfied.add(ONE);
        			break;
        		}
        	}
        	if(implication) {
        		logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
        		implication = false;
        		continue;
        	}
        }

				// Either if input respected pre-conditions or there are no pre-conditions
        if(!implication) {
					counterTestedInputs = counterTestedInputs.add(ONE);
					// Test properties
					if(prop.apply(input)){
 						// If we are looking for an Existential Quantifier
 						if(existential){
							existQuantFound = true;
							break searchInDepths;
 						}
 					// Failed tests are not interesting when looking for an Existential Quantifier
					}else if(!existential){
						failed = true;
						break searchInDepths;
					}
				}
			}

      parts = parts.tail();

    }

  	long endTime = System.currentTimeMillis();
  	double time = (endTime - startTime) / 1000;
		
		if(existential){
			if(existQuantFound == false){
				System.out.println("Existential quantifier not found.");
			}else{
				System.out.println("Existential quantifier found:\n\tinput " + input + " passed after " + counterTestedInputs + " tests");
			}

		}else{
			if(failed){
				System.out.println("FAILED after: " + counterTestedInputs + "/" + totalSize + " tests.\n\tLast input: " + input);
			}else{
			  System.out.println("OK, passed " + counterTestedInputs + "/" + totalSize + " tests");
			}
		}

		System.out.println( preCondNotSatisfied + " generated inputs did not meet the pre-condition" );
		System.out.println("Total of generated inputs: " + counterTestedInputs.add(preCondNotSatisfied) );
		System.out.println("Finished in " + time + " seconds");

  }

	// ---------------- SmallCheck Prod3 ------------------------------------------------------------
  public static <T1,T2,T3> void smallCheckProd3(int depth,  Enumeration< Product3<T1,T2,T3> >
																								enumeration, F< Product3<T1,T2,T3>, Boolean> prop,
																								int verbose, Set<F<Product3<T1,T2,T3>, Boolean>> preCond,
																								boolean existential) {

		long startTime = System.currentTimeMillis();
    Product3<T1,T2,T3> input = null;
    LazyList<Finite<Product3<T1,T2,T3>>> parts = enumeration.parts();

    BigInteger totalSize = ZERO;
    BigInteger counterTestedInputs = ZERO;
    BigInteger preCondNotSatisfied = ZERO;
    boolean implication = false;
    boolean failed = false;
    boolean existQuantFound = false;

		depth = recomputeDepthProd3(depth, enumeration, verbose);

    for(int i = 0; i < depth; i++) {
      totalSize = totalSize.add( parts.head().getCard() );
      parts = parts.tail();
    }

    parts = enumeration.parts();

		// Label for the loop
		searchInDepths:
		// For each depth...
    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      
  		// ... test all inputs
      for( BigInteger index = ZERO; index.compareTo(card) == -1; index = index.add(ONE) ) {
      
        input = parts.head().get(index);
        
        logMessage(3, verbose, "depth "  + i + ": " + index.add(ONE) + "/" + card + " input: " + input);
        
        // Test pre-conditions
        if(preCond.size() > 0) {
        	for(F<Product3<T1,T2,T3>, Boolean> entry : preCond) {
        		if(!entry.apply(input)) {
        			implication = true;
        			preCondNotSatisfied = preCondNotSatisfied.add(ONE);
        			break;
        		}
        	}
        	if(implication) {
        		logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
        		implication = false;
        		continue;
        	}
        }

				// Either if input respected pre-conditions or there are no pre-conditions
        if(!implication) {
					counterTestedInputs = counterTestedInputs.add(ONE);
					// Test properties
					if(prop.apply(input)){
 						// If we are looking for an Existential Quantifier
 						if(existential){
							existQuantFound = true;
							break searchInDepths;
 						}
 					// Failed tests are not interesting when looking for an Existential Quantifier
					}else if(!existential){
						failed = true;
						break searchInDepths;
					}
				}
			}

      parts = parts.tail();

    }

  	long endTime = System.currentTimeMillis();
  	double time = (endTime - startTime) / 1000;
		
		if(existential){
			if(existQuantFound == false){
				System.out.println("Existential quantifier not found.");
			}else{
				System.out.println("Existential quantifier found:\n\tinput " + input + " passed after " + counterTestedInputs + " tests");
			}

		}else{
			if(failed){
				System.out.println("FAILED after: " + counterTestedInputs + "/" + totalSize + " tests.\n\tLast input: " + input);
			}else{
			  System.out.println("OK, passed " + counterTestedInputs + "/" + totalSize + " tests");
			}
		}

		System.out.println( preCondNotSatisfied + " generated inputs did not meet the pre-condition" );
		System.out.println("Total of generated inputs: " + counterTestedInputs.add(preCondNotSatisfied) );
		System.out.println("Finished in " + time + " seconds");

  }

}
