package propcheck.quickcheck;

import propcheck.assertion.NotTestedSkip;
import propcheck.generator.quickcheck.RandomGenerator1;
import propcheck.property.Property;
import tom.library.enumerator.Enumeration;

class QuickcheckRunner1<A> extends BasicRunner {

	private RandomGenerator1<A> generator;
	private Property<A> property;


	QuickcheckRunner1(Enumeration<A> enumeration, Property<A> property) {
		init(enumeration, property);
	}

	QuickcheckRunner1(Enumeration<A> enumeration, Property<A> property, int numberOfTest) {
		init(enumeration, property);
		this.numOfTest = numberOfTest;
	}

	private void init(Enumeration<A> enumeration, Property<A> property) {
		generator = new RandomGenerator1<A>(enumeration);
		this.property = property;
		
	}
	
	@Override
	public void run() {
		int generatedTest = 0;
		int tested = 0;
		boolean errorFound = false;
		
		A input = null;
		while (tested < numOfTest) {
			try {
				input = generator.generateNext();
				if (input != null) {
					generatedTest++;
					
					property.apply(input);
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
			printCounterModel(input);
			shrink(input);
		}
		
	}
	
	void shrink(A input) {
		ShrinkRunner shrink = BasicRunnerFactory.make().get(input, property);
		shrink.run();
	}
}
