package propcheck.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property3;
import tom.library.enumerator.Enumeration;

class QuickcheckRunner3<A, B, C> extends BasicRunner {

	private Enumeration<A> enumA;
	private Enumeration<B> enumB;
	private Enumeration<C> enumC;
	private Property3<A, B, C> property;
	
	
	QuickcheckRunner3(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC, Property3<A, B, C> property) {
		this.enumA = enumA;
		this.enumB = enumB;
		this.enumC = enumC;
		this.property = property;
	}
	
	QuickcheckRunner3(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC, Property3<A, B, C> property, int numberOfTest) {
		this.enumA = enumA;
		this.enumB = enumB;
		this.enumC = enumC;
		this.property = property;
		this.numOfTest = numberOfTest;
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		int generatedTest = 0;
		int tested = 0;
		boolean errorFound = false;
		while (tested < numOfTest) {
			try {
				// get random number, uniform distributed
				int val = getNextRandom(rand, 0, numOfTest);
				A inputA = enumA.get(BigInteger.valueOf(val));
				B inputB = enumB.get(BigInteger.valueOf(val));
				C inputC = enumC.get(BigInteger.valueOf(val));
				generatedTest++;
				
				property.apply(inputA, inputB, inputC);
				tested++;
				printTestMarker(generatedTest, TESTED_MARKER);
			} catch (NotTestedSkip skip) {
				printTestMarker(generatedTest, SKIPPED_MARKER);
			} catch (AssertionError error) {
				printTestMarker(generatedTest, TESTED_MARKER);
				tested++;
				errorFound = true;
				break;
			}
		}
		printResult(generatedTest, tested, errorFound);
	}
}
