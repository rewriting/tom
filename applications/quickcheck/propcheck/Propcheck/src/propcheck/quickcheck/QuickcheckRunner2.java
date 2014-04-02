package propcheck.quickcheck;

import propcheck.assertion.NotTestedSkip;
import propcheck.generator.quickcheck.RandomGenerator2;
import propcheck.property.Property2;
import propcheck.shrink.PropcheckShink;
import propcheck.shrink.Shrink;
import tom.library.enumerator.Enumeration;

class QuickcheckRunner2<A, B> extends BasicRunner {

	/*private Enumeration<A> enumA;
	private Enumeration<B> enumB;*/
	private Property2<A, B> property;
	private RandomGenerator2<A, B> generator;
	
	
	QuickcheckRunner2(Enumeration<A> enumA, Enumeration<B> enumB, Property2<A, B> property) {
		init(enumA, enumB, property);
		this.property = property;
	}
	
	QuickcheckRunner2(Enumeration<A> enumA, Enumeration<B> enumB, Property2<A, B> property, int numberOfTest) {
		init(enumA, enumB, property);
		this.numOfTest = numberOfTest;
	}
	
	private void init(Enumeration<A> enumA, Enumeration<B> enumB, Property2<A, B> property) {
		/*this.enumA = enumA;
		this.enumB = enumB;*/
		this.generator = new RandomGenerator2<A, B>(enumA, enumB);
		this.property = property;
	}
	
	@Override
	public void run() {
		//Random rand = new Random();
		int generatedTest = 0;
		int tested = 0;
		boolean errorFound = false;
		
		A inputA = null;
		B inputB = null;
		while (tested < numOfTest) {
			try {
				inputA = generator.generateNext().p1();
				inputB = generator.generateNext().p2();
				
				if (inputA != null && inputB != null) {
					generatedTest++;
					
					property.apply(inputA, inputB);
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
			printCounterModel(inputA, inputB);
		}
		shrink(inputA, inputB);
	}
	
	void shrink(A inputA, B inputB) {
		ShrinkRunner shrink = BasicRunnerFactory.make().get(inputA, inputB, property);
		shrink.run();
	}

}
