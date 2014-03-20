package propcheck.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property2;
import tom.library.enumerator.Enumeration;

class QuickcheckRunner2<A, B> extends BasicRunner {

	private Enumeration<A> enumA;
	private Enumeration<B> enumB;
	private Property2<A, B> property;
	
	
	QuickcheckRunner2(Enumeration<A> enumA, Enumeration<B> enumB, Property2<A, B> property) {
		init(enumA, enumB, property);
		this.property = property;
	}
	
	QuickcheckRunner2(Enumeration<A> enumA, Enumeration<B> enumB, Property2<A, B> property, int numberOfTest) {
		init(enumA, enumB, property);
		this.numOfTest = numberOfTest;
	}
	
	private void init(Enumeration<A> enumA, Enumeration<B> enumB, Property2<A, B> property) {
		this.enumA = enumA;
		this.enumB = enumB;
		this.property = property;
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
				
				generatedTest++;
				
				property.apply(inputA, inputB);
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
