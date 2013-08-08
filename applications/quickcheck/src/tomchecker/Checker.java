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
								Set<F<Product1<T>, Boolean>> condBag, BigInteger defaultNumOfTest) {
		
		long startTime = System.currentTimeMillis();
		Map<Integer, Product1<T>> counterExamples = new HashMap<Integer, Product1<T>>();

		quickCheckProd1(depth, enumeration, prop, verbose, shrink, quotient, condBag, counterExamples,
										defaultNumOfTest, startTime);
	}

	// ---------------- QuickCheck Prod1 ------------------------------------------------------------
	// TODO: if shrink - remember tested inputs to avoid playing them again
	public static <T> void quickCheckProd1(int depth,  Enumeration< Product1<T> > enumeration,
								F<Product1<T>, Boolean> prop, int verbose, int shrink, int quotient,
								Set<F<Product1<T>, Boolean>> condBag, Map<Integer, Product1<T>> counterExamples,
								BigInteger defaultNumOfTest, long startTime) {
		
    LazyList<Finite<Product1<T>>> parts = enumeration.parts();
    BigInteger totalSize = ZERO;
    BigInteger[] nbProbes = new BigInteger[depth];

    logMessage(1, verbose,"RANDOM CHECK AT DEPTH " + depth);

    // if no smaller counter example
    if(depth == 0){
      assert false :  "FAILED  tests: " + counterExamples;
    }

		// Computes totalSize and number of probes for each depth
		totalSize = sizeAndProbes(depth, parts, nbProbes);
    logMessage(2, verbose,"TOTAL number of inputs " + totalSize);

		// Calculate the Default Number of Tests
		defaultNumOfTest = calcDNT(quotient, totalSize);
		logMessage(2, verbose, "DEFAULT number of tests " + defaultNumOfTest);

		// Recompute number of test inputs to distribute test cases among depths
		recomputeProbes(depth, nbProbes, defaultNumOfTest, totalSize);

		// Perform tests for each enumeration part
		performTests(parts, nbProbes, condBag, prop, counterExamples, shrink, enumeration, verbose,
								 quotient, defaultNumOfTest, startTime, depth);
    
  }


	// ---------------- Size And Probes -------------------------------------------------------------
	// Computes totalSize and number of probes for each depth
	public static <T> BigInteger sizeAndProbes(int depth, LazyList<Finite<Product1<T>>> parts,
																						 BigInteger[] nbProbes){

		BigInteger totalSize = ZERO;

    for(int i = 0; i < depth && !parts.isEmpty(); i++) {
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

	// ---------------- Perform Tests ---------------------------------------------------------------
  // Perform tests for each enumeration part
	public static <T> void performTests( LazyList<Finite<Product1<T>>> parts, BigInteger[] nbProbes,
													Set<F<Product1<T>, Boolean>> condBag, F<Product1<T>, Boolean> prop,
													Map<Integer,Product1<T>> counterExamples, int shrink,
													Enumeration< Product1<T> > enumeration, int verbose, int quotient,
													BigInteger defaultNumOfTest, long startTime, int depth ) {

		Product1<T> input = null;
    Set<BigInteger> testedInputs = new HashSet<BigInteger>();

		// TODO	: peut-être que je peux remplacer numOfTestCase, pour optimiser le code -> voir s'il n'y a pas de compteur qui varie pareillement

		BigInteger numOfTestCase = ZERO;
    Boolean failed = false;

		parts = enumeration.parts();

    for(int i = 0; i < depth; i++) {
      BigInteger card = parts.head().getCard();
      testedInputs.clear();

      logMessage(2, verbose,"AT DEPTH " + i + " SHOULD GENERATE :" + nbProbes[i] + " random inputs for the set with cardinality = " + card);

      BigInteger counter = ZERO;
      int limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      while(counter.compareTo(nbProbes[i]) == -1 && !failed && limit <= 0) {

				input =	chooseValidInput (card, verbose, testedInputs, parts, condBag, i);

				// New input found, so we can test it
				numOfTestCase = numOfTestCase.add(ONE);

		    if(!prop.apply(input)) {
		    	counterExamples.put(i, input);
		      failed = true;
		      if(shrink > 0) { // look for counter examples in smaller depths
		      	logMessage(3, verbose,"SHRINKING at depth " + i + "  " + input);
						// Note the first argument : it adjusts the depth to simulate a shrink
		      	quickCheckProd1(i, enumeration, prop, verbose, shrink, quotient/shrink, condBag, 
														counterExamples, defaultNumOfTest, startTime);
		      }
		    }

	    	counter = counter.add(ONE);
				limit = counter.compareTo(min(card,BigInteger.valueOf(Integer.MAX_VALUE)));

      }

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

      parts = parts.tail();

    }

    if(counterExamples.size() == 0){
      long endTime = System.currentTimeMillis();
      double time = (endTime - startTime) / 1000;
      System.out.println("OK, passed " + numOfTestCase + " tests");
      System.out.println("Finished in " + time + " seconds");
    }

	}

	// ---------------- Find a valid input to be tested ---------------------------------------------
	// A valid input does not mean that the input will not fail
	public static <T> Product1<T> chooseValidInput (BigInteger card, int verbose, Set<BigInteger>
																								testedInputs, LazyList<Finite<Product1<T>>> parts,
																								Set<F<Product1<T>, Boolean>> condBag, int currDepth){

		Product1<T> input = null;
    BigInteger index;
		Random rand = new Random();
		Boolean implication = false;

		while(!implication){
    	index = new BigInteger(card.bitLength(), rand);
	    logMessage(5, verbose, "TRY: " + index);

      // Get an input that hasn't been tested yet
			if( index.compareTo(card) >= 0 || testedInputs.contains(index) ){
				continue;
			}else{
				implication = true;
				input = parts.head().get(index);
				logMessage(3, verbose, "depth: " + currDepth + " input: " + input);

			  /*
				* If there is any implication, then check if they are satisfied.
				* Otherwise pick another input
				*/
			  for(F<Product1<T>, Boolean> entry : condBag) {
					if(!entry.apply(input)) {
						implication = false;
		        break;
	        }
				}
				if(!implication){
        	logMessage(4, verbose, "pre-condition not satisfied by the input: " + input);
					continue;
				}
	      testedInputs.add(index);
		  }
		}
		return input;
	}
 	
	// ---------------- QuickCheck Prod2 by default -------------------------------------------------
	public static <T1,T2> void quickCheckProd2(int depth,  Enumeration< Product2<T1,T2> > enumeration,
								F< Product2<T1,T2>, Boolean> prop, int verbose, int shrink, int quotient,
								Set<F<Product2<T1,T2>, Boolean>> condBag, BigInteger defaultNumOfTest) {

		long startTime = System.currentTimeMillis(); 
		HashMap<Integer, Product2<T1,T2>> counterExamples = new HashMap<Integer, Product2<T1,T2>>();

		quickCheckProd2(depth, enumeration, prop, verbose, shrink, quotient, condBag, counterExamples, defaultNumOfTest, startTime);
	}

	// ---------------- QuickCheck Prod2 ------------------------------------------------------------
  public static <T1,T2> void  quickCheckProd2(int depth,  Enumeration<Product2<T1,T2>> enumeration,
								F< Product2<T1,T2>, Boolean> prop, int verbose, int shrink, int quotient,
								Set<F<Product2<T1,T2>, Boolean>> condBag, Map<Integer, Product2<T1,T2>>
								counterExamples, BigInteger defaultNumOfTest, long startTime) {
	   
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
	    for(F<Product2<T1,T2>, Boolean> entry : condBag) {
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
	        	quickCheckProd2(i, enumeration, prop, verbose, shrink, quotient/shrink, condBag, counterExamples, defaultNumOfTest, startTime);
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
								quotient, Set<F< Product3<T1,T2,T3>, Boolean>> condBag, BigInteger defaultNumOfTest){
		
		long startTime = System.currentTimeMillis();  
		HashMap<Integer, Product3<T1,T2,T3>> counterExamples = new HashMap<Integer,Product3<T1,T2,T3>>();
		quickCheckProd3(depth, enumeration, prop, verbose, shrink, quotient, condBag, counterExamples,
								defaultNumOfTest, startTime);
	}

	// ---------------- QuickCheck Prod3 ------------------------------------------------------------
  public static <T1,T2,T3> void  quickCheckProd3(int depth,  Enumeration< Product3<T1,T2,T3> >
								enumeration, F< Product3<T1,T2,T3>, Boolean> prop, int verbose, int shrink, int
								quotient, Set<F<Product3<T1,T2,T3>, Boolean>> condBag, Map<Integer,
								Product3<T1,T2,T3>> counterExamples, BigInteger defaultNumOfTest, long startTime) {
	
    Product3<T1,T2,T3> input=null;
    LazyList<Finite<Product3<T1,T2,T3>>> parts = enumeration.parts();

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
	    for(F<Product3<T1,T2,T3>, Boolean> entry : condBag) {
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
	        	quickCheckProd3(i, enumeration, prop, verbose, shrink, quotient/shrink, condBag, counterExamples, defaultNumOfTest, startTime);
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

	// ---------------- SmallCheck Prod1 ------------------------------------------------------------
  public static <T> void smallCheckProd1(int depth,  Enumeration< Product1<T> > enumeration,
								F<Product1<T>, Boolean> prop, int verbose, Set<F< Product1<T>, Boolean>> condBag,
								Boolean existential) {
	
		long startTime = System.currentTimeMillis();
    Product1<T> args;
    LazyList<Finite<Product1<T>>> parts = enumeration.parts();
    Finite<Product1<T>> part = null;
    
    BigInteger total = BigInteger.ZERO;
    BigInteger counter = BigInteger.ONE;
    BigInteger counterTotal = BigInteger.ZERO;
    Boolean implication = true;
    		
    for(int i = 0; i < depth && !parts.isEmpty() ; i++) {
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
        
        if(condBag.size() > 0) {
        	for(F<Product1<T>, Boolean> entry : condBag) {
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
    System.out.println("OK, passed " + counter + " tests\n" + counterTotal + " of " + total + " generated inputs did not meet the pre-condition");
    long endTime = System.currentTimeMillis();
    double time = (endTime - startTime) / 1000;
    System.out.println("Finished in " + time + " seconds");
  }

	// ---------------- SmallCheck Prod2 ------------------------------------------------------------
  public static <T1,T2> void smallCheckProd2(int depth,  Enumeration< Product2<T1,T2> > enumeration,
								F< Product2<T1,T2>, Boolean> prop, int verbose, Set<F< Product2<T1,T2>, Boolean>>
								condBag, Boolean existential) {
	  
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
        
        if(condBag.size() > 0) {
        	for(F<Product2<T1,T2>, Boolean> entry : condBag) {
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
								Set<F<Product3<T1,T2,T3>, Boolean>> condBag, Boolean existential) {
	  
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
        
        if(condBag.size() > 0) {
        	for(F<Product3<T1,T2,T3>, Boolean> entry : condBag) {
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
