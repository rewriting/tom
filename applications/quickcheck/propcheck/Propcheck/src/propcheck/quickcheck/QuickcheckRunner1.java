package propcheck.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property;
import tom.library.enumerator.Enumeration;

class QuickcheckRunner1<A> extends BasicRunner {

	private Enumeration<A> enumeration;
	private Property<A> property;


	QuickcheckRunner1(Enumeration<A> enumeration, Property<A> property) {
		this.enumeration = enumeration;
		this.property = property;
	}

	QuickcheckRunner1(Enumeration<A> enumeration, Property<A> property, int numberOfTest) {
		this.enumeration = enumeration;
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
				A input = enumeration.get(BigInteger.valueOf(val));
				
				generatedTest++;
				
				property.apply(input);
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
