//TODO Why is shrink a Integer and not a Boolean ?

//TODO Write a comment about differences between Quick and Small Check

//TODO Je viens de modifier smallCheckProd1, il faut le tester

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
								F<Product1<T>, Boolean> prop, int verbose, int shrink, int quotient,
								Set<F<Product1<T>, Boolean>> preCond, BigInteger defaultNumOfTest,
								Boolean existential) {
								
		long startTime = System.currentTimeMillis();
		Map<Integer, Product1<T>> counterExamples = new HashMap<Integer, Product1<T>>();

		quickCheckProd1(depth, enumeration, prop, verbose, shrink, quotient, preCond, counterExamples,
										defaultNumOfTest, startTime, existential);
	}

	// ---------------- QuickCheck Prod1 ------------------------------------------------------------
	// TODO: if shrink - remember tested inputs to avoid playing them again
	public static <T> void quickCheckProd1(int depth,  Enumeration< Product1<T> > enumeration,
								F<Product1<T>, Boolean> prop, int verbose, int shrink, int quotient,
								Set<F<Product1<T>, Boolean>> preCond, Map<Integer, Product1<T>> counterExamples,
								BigInteger defaultNumOfTest, long startTime, Boolean existential) {
		
    BigInteger totalSize = ZERO;
    BigInteger[] nbProbes = new BigInteger[depth];

    logMessage(1, verbose,"RANDOM CHECK AT DEPTH " + depth);

    // If no smaller counter example
    if(depth == 0){
      assert false :  "FAILED  tests: " + counterExamples;
    }

		// Verify and recompute (if necessary) the input value for depth
		depth = recomputeDepth1(depth, enumeration, verbose);

		// Computes totalSize and number of probes for each depth
		totalSize = sizeAndProbes(depth, enumeration, nbProbes);
    logMessage(2, verbose,"TOTAL number of inputs " + totalSize);

		// Calculate the Default Number of Tests
		defaultNumOfTest = calcDNT(quotient, totalSize);
		logMessage(2, verbose, "DEFAULT number of tests " + defaultNumOfTest);

		// Recompute number of test inputs to distribute test cases among depths
		recomputeProbes(depth, nbProbes, defaultNumOfTest, totalSize);

		if(existential){
			// Look for one valid example, that is, a example which respects the properties
			findExistQuant(nbProbes, preCond, prop, enumeration, startTime, depth, verbose);
		}else{
			// Perform tests for each enumeration part
			performTests(nbProbes, preCond, prop, counterExamples, shrink, enumeration, verbose, quotient,
									 defaultNumOfTest, startTime, depth, existential);
		}
    
  }

	// ---------------- Recompute Depth -------------------------------------------------------------
	/*
	 * If the enumeration has a very small valid depth (less than that passed as argument), it have to 
	 * be recomputed, but we need to be attentive to infinite depths
	*/ 
	public static <T> int recomputeDepth1(int depth, Enumeration< Product1<T> > enumeration,
																				int verbose){

		LazyList<Finite<Product1<T>>> parts = enumeration.parts();
		
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

	// ---------------- Size And Probes -------------------------------------------------------------
	// Computes totalSize and number of probes for each depth
	public static <T> BigInteger sizeAndProbes(int depth, Enumeration< Product1<T> > enumeration,
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

	// ---------------- Find Existential Quantifier -------------------------------------------------
  // Perform tests for each enumeration part in order to find a valid example
	public static <T> void findExistQuant(BigInteger[] nbProbes, Set<F<Product1<T>, Boolean>> preCond,
																				F<Product1<T>, Boolean> prop, Enumeration< Product1<T> > enumeration, long
																				startTime, int depth, int verbose) {

		LazyList<Finite<Product1<T>>> parts = enumeration.parts();
		Product1<T> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
    Boolean existQuantFound = false;

searchInDepths:
    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && limit <= 0) {
      
				input =	findValidInput(card, verbose, testedInputs, parts, preCond, i);
				
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

	// ---------------- Perform Tests ---------------------------------------------------------------
  // Perform tests for each enumeration part
	public static <T> void performTests(BigInteger[] nbProbes, Set<F<Product1<T>, Boolean>> preCond,
																			F<Product1<T>, Boolean> prop, Map<Integer,Product1<T>> counterExamples,
																			int shrink, Enumeration< Product1<T> > enumeration, int verbose,
																			int quotient, BigInteger defaultNumOfTest, long startTime, int depth,
																			Boolean existential) {

		LazyList<Finite<Product1<T>>> parts = enumeration.parts();
		Product1<T> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
		BigInteger numOfTestCase = ZERO;
    Boolean failed = false;

    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && !failed && limit <= 0) {

				input =	findValidInput(card, verbose, testedInputs, parts, preCond, i);

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
		      if(shrink > 0) {
		      	logMessage(3, verbose,"SHRINKING at depth " + i + "  " + input);
						// Note the first argument : it adjusts the depth to simulate a shrink
		      	quickCheckProd1(i, enumeration, prop, verbose, shrink, quotient/shrink, preCond, 
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
	public static <T> Product1<T> findValidInput(BigInteger card, int verbose, Set<BigInteger>
																								testedInputs, LazyList<Finite<Product1<T>>> parts,
																								Set<F<Product1<T>, Boolean>> preCond, int currDepth){

		Product1<T> input = null;
    BigInteger index;
		Random rand = new Random();
		Boolean implication = false;
		Boolean foundValidInput = false;

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
			  for(F<Product1<T>, Boolean> entry : preCond) {
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

	// ---------------- QuickCheck Prod2 by default -------------------------------------------------
	public static <T1,T2> void quickCheckProd2(int depth,  Enumeration< Product2<T1,T2> > enumeration,
								F< Product2<T1,T2>, Boolean> prop, int verbose, int shrink, int quotient,
								Set<F<Product2<T1,T2>, Boolean>> preCond, BigInteger defaultNumOfTest,
								Boolean existential) {

		long startTime = System.currentTimeMillis(); 
		HashMap<Integer, Product2<T1,T2>> counterExamples = new HashMap<Integer, Product2<T1,T2>>();

		quickCheckProd2(depth, enumeration, prop, verbose, shrink, quotient, preCond, counterExamples, defaultNumOfTest, startTime, existential);
	}

	// ---------------- QuickCheck Prod2 ------------------------------------------------------------
  public static <T1,T2> void  quickCheckProd2(int depth,  Enumeration<Product2<T1,T2>> enumeration,
								F< Product2<T1,T2>, Boolean> prop, int verbose, int shrink, int quotient,
								Set<F<Product2<T1,T2>, Boolean>> preCond, Map<Integer, Product2<T1,T2>>
								counterExamples, BigInteger defaultNumOfTest, long startTime, Boolean existential) {
	   
    Product2<T1,T2> input=null;
    LazyList<Finite<Product2<T1,T2>>> parts = enumeration.parts();
    
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
    Random rand = new Random();

    BigInteger totalSize = BigInteger.ZERO;
    BigInteger[] nbProbes = new BigInteger[depth];
    BigInteger numOfTestCase = BigInteger.ONE;
    BigInteger maxNumOfTestCase = BigInteger.ONE;
    
    Boolean failed = false;
    int numOfPart = 0;
    logMessage(1,verbose,"RANDOM CHECK AT DEPTH " + depth);
    // if no smaller counter example    
    if(depth == 0){ 
      assert false :  "FAILED  tests: " + counterExamples;
    }
    // computes total size and number of probes for each depth
    for(int i = 0; i < depth && !parts.isEmpty(); i++) {
      nbProbes[i] = parts.head().getCard();
      totalSize = totalSize.add(nbProbes[i]);
      parts = parts.tail();
      numOfPart++;
    }
    logMessage(2, verbose,"TOTAL number of inputs " + totalSize);
    quotient++; // at least 1
    if(totalSize.divide(BigInteger.valueOf(quotient)).compareTo(BigInteger.ZERO) == 0){
      defaultNumOfTest =  BigInteger.ONE;
    }else{
      do {
        defaultNumOfTest = new BigInteger(totalSize.bitLength(), rand);
      } while(defaultNumOfTest.compareTo(totalSize.divide(BigInteger.valueOf(quotient))) >= 0);
      defaultNumOfTest = defaultNumOfTest.add(BigInteger.ONE);
    }
    logMessage(2, verbose, "DEFAULT number of tests " + defaultNumOfTest);
    for(int i = 0; i < numOfPart; i++) {
      if(nbProbes[i].compareTo(BigInteger.ZERO) == 1) { // if not an empty part
	    	if(((nbProbes[i].multiply(defaultNumOfTest)).divide(totalSize)).compareTo(BigInteger.ZERO) == 0) {
	        nbProbes[i] = BigInteger.ONE;
	    	} else {
	        nbProbes[i] = (nbProbes[i].multiply(defaultNumOfTest)).divide(totalSize);
	    	}
      }
    }
    // perform tests for each part
    parts = enumeration.parts();
    for(int i = 0; i < numOfPart; i++) {
      BigInteger card = parts.head().getCard();
      BigInteger index = BigInteger.ZERO;
      testedInputs.clear();
      logMessage(3, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);
      BigInteger j = BigInteger.ZERO;
      BigInteger maxLimit = BigInteger.ZERO;
      while(j.compareTo(nbProbes[i]) == -1 && !failed && maxLimit.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE))) <= 0) {
        Boolean implication = true;
  	    maxLimit = maxLimit.add(BigInteger.ONE);
        // get an input that hasn't been tested yet 
      	do {
          index = new BigInteger(card.bitLength(), rand);
          logMessage(5, verbose, "TRY: " + index);
      	} while(index.compareTo(card) >= 0 || testedInputs.contains(index));
        // input = enumeration.parts().index(BigInteger.valueOf(i)).get(index);
        input = parts.head().get(index);
        logMessage(3, verbose, "depth: " + i + " input: " + input);
        // if there is any implication, then check if they are satisfied; otherwise pick another input
	    for(F<Product2<T1,T2>, Boolean> entry : preCond) {
	    	if(!entry.apply(input)) {
	    		//logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
	            implication = false;
	            break;
	          }
	    }
        if(!implication) {
        	logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
        	continue;
        }

        // new input here so we can test it
        numOfTestCase = numOfTestCase.add(BigInteger.ONE);
        testedInputs.add(index);

        if(!prop.apply(input)) {
        	counterExamples.put(i, input);
	        failed = true;
	        if(shrink > 0) { // look for counter examples in smaller depths
	        	logMessage(3, verbose,"SHRINKING at depth " + i + "  " + input);
	        	quickCheckProd2(i, enumeration, prop, verbose, shrink, quotient/shrink, preCond, counterExamples, defaultNumOfTest, startTime, existential);
	        }
        }
	    	j = j.add(BigInteger.ONE);
      }

      logMessage(2, verbose,"AT DEPTH " + i + " GENERATED " + testedInputs.size() + " random inputs for the set with cardinality = " + card);

      if(failed){
        String errorMessage = "FAILED test(s): ";
        for(Integer size: counterExamples.keySet()){
        	errorMessage += "\n\t " + counterExamples.get(size) + " in depth " + size;
        }
        long end = System.currentTimeMillis();
        double time = (end - startTime) / 1000;
        assert false :  errorMessage + "\n" + "Finished in " + time + " seconds";
      }
      parts = parts.tail();
    }
    if(counterExamples.size() == 0){
    	long endTime = System.currentTimeMillis();
        double time = (endTime - startTime) / 1000;
        System.out.println("OK, passed " + numOfTestCase + " tests");
        System.out.println("Finished in " + time + " seconds");
    }
  }
	
	// ---------------- QuickCheck Prod3 by default -------------------------------------------------
	public static <T1,T2,T3> void quickCheckProd3(int depth,  Enumeration< Product3<T1,T2,T3> >
								enumeration, F< Product3<T1,T2,T3>, Boolean> prop, int verbose, int shrink, int
								quotient, Set<F< Product3<T1,T2,T3>, Boolean>> preCond, BigInteger defaultNumOfTest,
								Boolean existential){

		long startTime = System.currentTimeMillis();
		Map<Integer, Product3<T1,T2,T3>> counterExamples = new HashMap<Integer,Product3<T1,T2,T3>>();

		quickCheckProd3(depth, enumeration, prop, verbose, shrink, quotient, preCond, counterExamples,
										defaultNumOfTest, startTime, existential);
										
	}

	// ---------------- QuickCheck Prod3 ------------------------------------------------------------
  public static <T1,T2,T3> void quickCheckProd3(int depth,  Enumeration<Product3<T1,T2,T3>>
								enumeration, F< Product3<T1,T2,T3>, Boolean> prop, int verbose, int shrink, int
								quotient, Set<F<Product3<T1,T2,T3>, Boolean>> preCond, Map<Integer,
								Product3<T1,T2,T3>> counterExamples, BigInteger defaultNumOfTest, long startTime,
								Boolean existential) {

    Product3<T1,T2,T3> input = null;

		//TODO: BUG
		System.out.println("The bug is right here!!!!!!!!!!!!!");
    LazyList<Finite<Product3<T1,T2,T3>>> parts = enumeration.parts();

		depth = recomputeDepth3(depth, parts);

    Set<BigInteger> testedInputs = new HashSet<BigInteger>();
    Random rand = new Random();

    BigInteger totalSize = BigInteger.ZERO;
    BigInteger[] nbProbes = new BigInteger[depth];
    BigInteger numOfTestCase = BigInteger.ONE;
    BigInteger maxNumOfTestCase = BigInteger.ONE;
    
    int numOfPart = 0;
    Boolean failed = false;

    logMessage(1,verbose,"RANDOM CHECK AT DEPTH " + depth);

    // if no smaller counter example    
    if(depth == 0){ 
      assert false :  "FAILED  tests: " + counterExamples;
    }

    // computes total size and number of probes for each depth
    for(int i = 0; i < depth && !parts.isEmpty(); i++) {
      nbProbes[i] = parts.head().getCard();
      totalSize = totalSize.add(nbProbes[i]);
      parts = parts.tail();
      numOfPart++;
    }
    logMessage(2, verbose,"TOTAL number of inputs " + totalSize);

    quotient++; // at least 1
    if(totalSize.divide(BigInteger.valueOf(quotient)).compareTo(BigInteger.ZERO) == 0){
      defaultNumOfTest =  BigInteger.ONE;
    }else{
      do {
        defaultNumOfTest = new BigInteger(totalSize.bitLength(), rand);
      } while(defaultNumOfTest.compareTo(totalSize.divide(BigInteger.valueOf(quotient))) >= 0);
      defaultNumOfTest = defaultNumOfTest.add(BigInteger.ONE);
    }
    logMessage(2, verbose, "DEFAULT number of tests " + defaultNumOfTest);
    for(int i = 0; i < numOfPart; i++) {	 
      if(nbProbes[i].compareTo(BigInteger.ZERO) == 1) { // if not an empty part
	    	if(((nbProbes[i].multiply(defaultNumOfTest)).divide(totalSize)).compareTo(BigInteger.ZERO) == 0) {
	        nbProbes[i] = BigInteger.ONE;
	    	} else {
	        nbProbes[i] = (nbProbes[i].multiply(defaultNumOfTest)).divide(totalSize);
	    	}
      }
    }

    // perform tests for each part
    parts = enumeration.parts();
    for(int i = 0; i < numOfPart; i++) {
      BigInteger card = parts.head().getCard();
      BigInteger index = BigInteger.ZERO;
      testedInputs.clear();
      logMessage(3, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);
      BigInteger j = BigInteger.ZERO;
      BigInteger maxLimit = BigInteger.ZERO;
      while(j.compareTo(nbProbes[i]) == -1 && !failed && maxLimit.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE))) <= 0) {  
        Boolean implication = true;
  	    maxLimit = maxLimit.add(BigInteger.ONE);
        // get an input that hasn't been tested yet 
      	do {
          index = new BigInteger(card.bitLength(), rand);
          logMessage(5, verbose, "TRY: " + index);
      	} while(index.compareTo(card) >= 0 || testedInputs.contains(index));
        // input = enumeration.parts().index(BigInteger.valueOf(i)).get(index);
        input = parts.head().get(index);
        logMessage(3, verbose, "depth: " + i + " input: " + input);
        // if there is any implication, then check if they are satisfied; otherwise pick another input
	    for(F<Product3<T1,T2,T3>, Boolean> entry : preCond) {
	    	if(!entry.apply(input)) {
	            //logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
	            implication = false;
	            break;
	        }
	    }
        if(!implication) {
        	logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
        	continue;
        }

        // new input here so we can test it
        numOfTestCase = numOfTestCase.add(BigInteger.ONE);
        testedInputs.add(index);
        if(!prop.apply(input)) {
        	counterExamples.put(i, input);
	        failed = true;
	        if(shrink > 0) { // look for counter examples in smaller depths
	        	logMessage(3, verbose,"SHRINKING at depth " + i + "  " + input);
	        	quickCheckProd3(i, enumeration, prop, verbose, shrink, quotient/shrink, preCond, counterExamples, defaultNumOfTest, startTime, existential);
	        }
        }
	    	j = j.add(BigInteger.ONE);
      }

      logMessage(2, verbose,"AT DEPTH " + i + " GENERATED " + testedInputs.size() + " random inputs for the set with cardinality = " + card);

      if(failed){
        String errorMessage = "FAILED test(s): ";
        for(Integer size: counterExamples.keySet()){
        	errorMessage += "\n\t " + counterExamples.get(size) + " in depth " + size;
        }
        long end = System.currentTimeMillis();
        double time = (end - startTime) / 1000;
        assert false :  errorMessage + "\n" + "Finished in " + time + " seconds";
      }
      parts = parts.tail();
    }
    if(counterExamples.size() == 0){
    	long endTime = System.currentTimeMillis();
    	double time = (endTime - startTime) / 1000;
        System.out.println("OK, passed " + numOfTestCase + " tests");
        System.out.println("Finished in " + time + " seconds");
    }
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
  public static void  logMessage(Integer level, int verbose, String message) {
    if(level <= verbose){
      System.out.println(message);
    }
  }  
  
	// ---------------- Big Integer -----------------------------------------------------------------
  private static BigInteger min(BigInteger n1, BigInteger n2){
    if(n1.compareTo(n2) <= 0) {
      return n1;
    } else {
      return n2;
    }
  }

	//TODO: il faut implémenter existential quantifier pour smallCheck
	// ---------------- SmallCheck Prod1 ------------------------------------------------------------
  public static <T> void smallCheckProd1(int depth,  Enumeration< Product1<T> > enumeration,
																				 F<Product1<T>, Boolean> prop, int verbose,
																				 Set<F< Product1<T>, Boolean>> preCond, Boolean existential) {
	
		long startTime = System.currentTimeMillis();
    Product1<T> input = null;
    LazyList<Finite<Product1<T>>> parts = enumeration.parts();

    BigInteger totalSize = ZERO;
    BigInteger counterTestedInputs = ZERO;
    BigInteger preCondNotSatisfied = ZERO;
    Boolean implication = false;
    Boolean failed = false;
    Boolean existQuantFound = false;

		depth = recomputeDepth1(depth, enumeration, verbose);

    for(int i = 0; i < depth; i++) {
      totalSize = totalSize.add( parts.head().getCard() );
      parts = parts.tail();
    }

    parts = enumeration.parts();

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
				System.out.println("FAILED after: " + counterTestedInputs + "/" + totalSize + " tests. Last input: " + input);
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
								F< Product2<T1,T2>, Boolean> prop, int verbose, Set<F< Product2<T1,T2>, Boolean>>
								preCond, Boolean existential) {
	  
		long startTime = System.currentTimeMillis();  
    Product2<T1,T2> args;
    LazyList<Finite<Product2<T1,T2>>> parts = enumeration.parts(); 
    Finite<Product2<T1,T2>> part = null;

    BigInteger total = BigInteger.ZERO;
    BigInteger counter = BigInteger.ONE;
    BigInteger counterTotal = BigInteger.ZERO;
    Boolean implication = true;
    	
    //&& parts.head().getCard().compareTo(BigInteger.ZERO)==1
    for(int i = 0; i < depth; i++) {
      total = total.add(parts.head().getCard());
      parts = parts.tail(); 
    }

    parts = enumeration.parts(); 
    for(int i = 0; i < depth && !parts.isEmpty() ; i++) {
      part = parts.head();
      BigInteger partSize = part.getCard();
      for(BigInteger j = BigInteger.ZERO; j.compareTo(partSize) == -1 ; j = j.add(BigInteger.ONE)) {
        args = part.get(j);
        
        logMessage(3, verbose, "depth "  + i + ": " + (j.add(BigInteger.ONE)) + "/" + partSize + " ***   " + "input: " + args);
        
        if(preCond.size() > 0) {
        	for(F<Product2<T1,T2>, Boolean> entry : preCond) {
        		if(!entry.apply(args)) {
        			implication = false;
        		}
        	}
        	if(!implication) {
        		logMessage(4, verbose, "pre-condition not satisfied by the input: " + args);
        	}
        }
        if(implication) {
        	long end = System.currentTimeMillis();
        	double time = (end - startTime) / 1000;
            assert prop.apply(args) :  "FAILED after " + counter + " tests: " + args + "\n" + counterTotal + " of " + counter.add(counterTotal) + " generated inputs did not meet the pre-condition" +
        		"\nFinished in " + time + " seconds";
            counter = counter.add(BigInteger.ONE);
        } else {
        	counterTotal = counterTotal.add(BigInteger.ONE);
        }
        implication = true;
      }
      parts = parts.tail();
    }
    System.out.println("OK, passed " + counter + " tests\n" + counterTotal + " of " + total + " generated inputs did not meet pre-condition");
    long endTime = System.currentTimeMillis();
    double time = (endTime - startTime) / 1000;
    System.out.println("Finished in " + time + " seconds");
  }

	// ---------------- SmallCheck Prod3 ------------------------------------------------------------
  public static <T1,T2,T3> void smallCheckProd3(int depth,  Enumeration< Product3<T1,T2,T3> >
								enumeration, F< Product3<T1,T2,T3>, Boolean> prop, int verbose, 
								Set<F<Product3<T1,T2,T3>, Boolean>> preCond, Boolean existential) {
	  
		long startTime = System.currentTimeMillis();  
    Product3<T1,T2,T3> args;
    LazyList<Finite<Product3<T1,T2,T3>>> parts = enumeration.parts(); 
    Finite<Product3<T1,T2,T3>> part = null;

    BigInteger total = BigInteger.ZERO;
    BigInteger counter = BigInteger.ONE;
    BigInteger counterTotal = BigInteger.ZERO;
    Boolean implication = true;
    
    // && parts.head().getCard().compareTo(BigInteger.ZERO)==1		
    for(int i = 0; i < depth ; i++) {
      total = total.add(parts.head().getCard());
      parts = parts.tail(); 
    }
  
    parts = enumeration.parts(); 
    for(int i = 0; i < depth && !parts.isEmpty() ; i++) {
      part = parts.head();
      BigInteger partSize = part.getCard();
      for(BigInteger j = BigInteger.ZERO; j.compareTo(partSize) == -1 ; j = j.add(BigInteger.ONE)) {
        args = part.get(j);
        
        logMessage(3, verbose, "depth "  + i + ": " + (j.add(BigInteger.ONE)) + "/" + partSize + " ***   " + "input: " + args);
        
        if(preCond.size() > 0) {
        	for(F<Product3<T1,T2,T3>, Boolean> entry : preCond) {
        		if(!entry.apply(args)) {
        			implication = false;
        		}
        	}
        	if(!implication) {
        		logMessage(4, verbose, "pre-condition not satisfied by the input: " + args);
        	}
        }
        if(implication) {
        	long end = System.currentTimeMillis();
        	double time = (end - startTime) / 1000;
            assert prop.apply(args) :  "FAILED after " + counter + " tests: " + args + "\n" + counterTotal + " of " + counter.add(counterTotal) + " generated inputs did not meet the pre-condition" +
        		"\nFinished in " + time + " seconds";
            counter = counter.add(BigInteger.ONE);
        } else {
        	counterTotal = counterTotal.add(BigInteger.ONE);
        }
        implication = true;
      }
      parts = parts.tail();
    }
    System.out.println("OK, passed " + counter + " tests\n" + counterTotal + " of " + total + " generated inputs did not meet pre-condition");
    long endTime = System.currentTimeMillis();
    float time = (endTime - startTime) / 1000;
    System.out.println("Finished in " + time + " seconds");
  }

}
