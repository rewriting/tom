package propcheck.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property3;
import tom.library.enumerator.Enumeration;

class QuickcheckRunner3<A, B, C> extends BasicRunner {

	/*private Enumeration<A> enumA;
	private Enumeration<B> enumB;
	private Enumeration<C> enumC;*/
	private Property3<A, B, C> property;
	private EnumProduct2<A, B, C> enumProduct;
	
	QuickcheckRunner3(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC, Property3<A, B, C> property) {
		/*this.enumA = enumA;
		this.enumB = enumB;
		this.enumC = enumC;
		this.property = property;*/
		init(enumA, enumB, enumC, property);
	}
	
	QuickcheckRunner3(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC, Property3<A, B, C> property, int numberOfTest) {
		/*this.enumA = enumA;
		this.enumB = enumB;
		this.enumC = enumC;
		this.property = property;*/
		init(enumA, enumB, enumC, property);
		this.numOfTest = numberOfTest;
	}
	
	private void init(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC, Property3<A, B, C> property) {
		enumProduct = new EnumProduct2<A, B, C>(enumA, enumB, enumC);
		this.property = property;
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		int generatedTest = 0;
		int tested = 0;
		boolean errorFound = false;
		A inputA = null;
		B inputB = null;
		C inputC = null;
		while (tested < numOfTest) {
			try {
				// get random number, uniform distributed
				// int val = getNextRandom(rand, 0, numOfTest);
				/*A inputA = enumA.get(BigInteger.valueOf(val));
				B inputB = enumB.get(BigInteger.valueOf(val));
				C inputC = enumC.get(BigInteger.valueOf(val));*/
				inputA = enumProduct.generateNext().p1();
				inputB = enumProduct.generateNext().p2();
				inputC = enumProduct.generateNext().p3();
				
				if (inputA != null && inputB != null && inputC != null) {
					generatedTest++;
					
					property.apply(inputA, inputB, inputC);
					tested++;
					printTestMarker(generatedTest, TESTED_MARKER);
				}
				enumProduct.moveToNextPart();
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
