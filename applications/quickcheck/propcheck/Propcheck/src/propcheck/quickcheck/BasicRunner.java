package propcheck.quickcheck;

import java.util.Random;

import propcheck.tools.SimpleLogger;

public abstract class BasicRunner implements Runner {
	
	static final int MARKER_SIZE = 80;
	static final String TESTED_MARKER = ".";
	static final String SKIPPED_MARKER = "x";
	
	/** default number of test **/
	int numOfTest = 100;
	
	
	public void setNumOftest(int number) {
		this.numOfTest = number;
	}
	
	public int getNumOfTest() {
		return numOfTest;
	}
	
	abstract <A> void shrinkCounterExample(A inputA);
	
	abstract <A, B> void shrinkCounterExample(A inputA, B inputB);
	
	abstract <A, B, C> void shrinkCounterExample(A inputA, B inputB, C inputC);
	
	void printResult(int generatedCase, int testedCase, boolean error) {
		if (error) {
			printError(generatedCase, testedCase);
		} else {
			printSucceed(generatedCase, testedCase);
		}
	}
	
	/**
	 * Adds new line character 
	 * 
	 * @param generatedCases
	 */
	void printTestMarker(int generatedCases, String marker) {
		if (generatedCases % MARKER_SIZE == 0) {
			System.out.println(marker);
		} else {
			System.out.print(marker);
		}
	}
	
	void printSucceed(int generatedCases, int testedCases) {
		SimpleLogger.log("Test passed after " + testedCases + " tests\nGenerated test case: " + generatedCases);
	}
	
	void printError(int generatedCases, int testedCases) {
		SimpleLogger.log("Test failed after " + testedCases + " tests\nGenerated test case: " + generatedCases);
	}
	
	int getNextRandom(Random rand, int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}
	
	<A> void printCounterModel(A a) {
		SimpleLogger.log(String.format("Counter model found:\n%s", a));
	}
	
	<A, B> void printCounterModel(A a, B b) {
		SimpleLogger.log(String.format("Counter model found:\n%s\n%s", a, b));
	}
	
	<A, B, C> void printCounterModel(A a, B b, C c) {
		SimpleLogger.log(String.format("\nCounter example found:\n[%s]\n[%s]\n[%s]", a, b, c));
	}
	
	// TODO print the elapsed time of the test at the end of the test
}
