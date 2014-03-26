package propcheck.quickcheck;

import propcheck.assertion.NotTestedSkip;
import propcheck.generator.quickcheck.RandomGenerator3;
import propcheck.property.Property3;
import tom.library.enumerator.Enumeration;

class QuickcheckRunner3<A, B, C> extends BasicRunner {

	private Property3<A, B, C> property;
	private RandomGenerator3<A, B, C> generator;
	
	QuickcheckRunner3(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC, Property3<A, B, C> property) {
		init(enumA, enumB, enumC, property);
	}
	
	QuickcheckRunner3(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC, Property3<A, B, C> property, int numberOfTest) {
		init(enumA, enumB, enumC, property);
		this.numOfTest = numberOfTest;
	}
	
	private void init(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC, Property3<A, B, C> property) {
		generator = new RandomGenerator3<A, B, C>(enumA, enumB, enumC);
		this.property = property;
	}
	
	@Override
	public void run() {
		int generatedTest = 0;
		int tested = 0;
		boolean errorFound = false;
		A inputA = null;
		B inputB = null;
		C inputC = null;
		while (tested < numOfTest) {
			try {
				inputA = generator.generateNext().p1();
				inputB = generator.generateNext().p2();
				inputC = generator.generateNext().p3();
				
				if (inputA != null && inputB != null && inputC != null) {
					generatedTest++;
					
					property.apply(inputA, inputB, inputC);
					tested++;
					printTestMarker(generatedTest, TESTED_MARKER);
				}
				generator.moveToNextPart();
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
		if (errorFound) {
			printCounterModel(inputA, inputB, inputC);
		}
	}
}
