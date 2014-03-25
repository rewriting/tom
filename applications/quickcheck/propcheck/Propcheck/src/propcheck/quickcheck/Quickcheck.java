package propcheck.quickcheck;

import java.util.ArrayList;
import java.util.List;

import propcheck.property.Property;
import propcheck.property.Property2;
import propcheck.property.Property3;
import propcheck.tools.SimpleLogger;
import tom.library.enumerator.Enumeration;

public class Quickcheck {
	private int numTest;
	private List<Runner> runners;
	
	private Quickcheck() {
		numTest = 100;
		runners = new ArrayList<Runner>();
	}
	
	private Quickcheck(int numTest) {
		this.numTest = numTest;
		runners = new ArrayList<Runner>();
	}
	
	public static Quickcheck make() {
		return new Quickcheck();
	}
	
	public static Quickcheck make(int numTest) {
		return new Quickcheck(numTest);
	}
	
	public Quickcheck setTestNumber(int number) {
		this.numTest = number;
		return this;
	}
	
	public <A> Quickcheck forAll(Enumeration<A> enumeration, Property<A> property) {
		runners.add(new QuickcheckRunner1<A>(enumeration, property, numTest));
		return this;
	}
	
	public <A, B> Quickcheck forAll(Enumeration<A> enumerationA, Enumeration<B> enumerationB, Property2<A, B> property) {
		runners.add(new QuickcheckRunner2<A, B>(enumerationA, enumerationB , property, numTest));
		return this;
	}
	
	public <A, B, C> Quickcheck forAll(Enumeration<A> enumerationA, Enumeration<B> enumerationB, Enumeration<C> enumerationC, Property3<A, B, C> property) {
		runners.add(new QuickcheckRunner3<A, B, C>(enumerationA, enumerationB, enumerationC , property, numTest));
		return this;
	}
	
	/**
	 * Run the check
	 */
	public void run() {
		if (runners.size() == 0) {
			SimpleLogger.log("Quickcheck.run(): " + "no property to check");
		}
		for (Runner runner : runners) {
			runner.run();
		}
	}
}
