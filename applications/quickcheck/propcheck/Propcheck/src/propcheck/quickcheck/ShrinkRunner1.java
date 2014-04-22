package propcheck.quickcheck;

import java.util.ArrayList;
import java.util.List;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property;
import propcheck.shrink.ShrinkException;
import propcheck.shrink.TermReducer;
import propcheck.shrink.TreeTools;
import propcheck.tools.SimpleLogger;
import zipper.ZipperException;

public class ShrinkRunner1<A> implements ShrinkRunner {

	private A rootTerm, cex;
	private Property<A> property;
	private int shrunkCount = 0;
	
	public ShrinkRunner1(A term, Property<A> property) {
		this.rootTerm = term;
		cex = rootTerm;
		this.property = property;
	}
	
	public void run() {
		// shrink part 1: get the least subterm fails the property
		// apply the combination of inputs to the property
		apply1(rootTerm);

		// part 2: explode terms and apply to the property
		apply2(cex);

		print(shrunkCount, cex);
	}
	
	private void apply1(A a) {
		Combiner<A> combiner = new Combiner<A>(a).buildInputs();
		while (combiner.hasMoreInputs()) {
			System.out.println("ShrinkRunner1.apply1()");
			System.out.println(cex);
			A input = combiner.getNextInput();
			try {
				property.apply(input);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				combiner.setTerms(input).buildInputs();
				cex = input;
				shrunkCount ++;
			}
		}
	}

	private void apply2(A a) {
		Combiner<A> combiner = new Combiner<A>(a);
		List<A> inputs = null;
		
		A input = null;
		
		A prevCexA = null, currCexA = a;
		System.out.println("ShrinkRunner1.apply2()");
		boolean err = false;
		while (prevCexA != currCexA) {
			System.out.println(prevCexA + " - " + currCexA);
			prevCexA = currCexA == null? prevCexA : currCexA;
			
			inputs = combiner.setTerms(currCexA).getExplodedInputs();
			for (int index = 0; index < inputs.size(); index++) {
				input = inputs.get(index);
				try {
					property.apply(input);
				} catch (NotTestedSkip skip) {
					// do nothing
				} catch (AssertionError error) {
					// check the size
					currCexA = TreeTools.size(input) < TreeTools.size(currCexA)? input : currCexA;
					err = true;
				}
			}
			if (err) {
				shrunkCount ++;
			}
		}
		cex = currCexA;
	}
	
	void print(int shrunk, A input) {
		String message = String.format("Shrunk %s times, counter example:\n%s", shrunk, input);
		SimpleLogger.log(message);
	}

	class Combiner<A> {
		private A _a;
		
		private List<A> inputs;
		private TermReducer<A> trA;
		
		private int index;
		
		public Combiner(A a) {
			_a = a;
			init();
		}
		
		public boolean hasMoreInputs() {
			return index < inputs.size();
		}
		
		public void init() {
			inputs = new ArrayList<A>();
			trA = new TermReducer<A>();
		}
		
		public Combiner setTerms(A a) {
			_a = a;
			return this;
		}
		
		public A getNextInput() {
			return inputs.get(index++);
		}
		
		public List<A> getExplodedInputs() {
			inputs.clear();
			List<A> as = new ArrayList<A>();

			// shrink part 1: get the least subterm fails the property
			try {
				as = (List<A>) trA.explode(_a);
			} catch (ShrinkException e) {
				e.printStackTrace();
			} catch (ZipperException e) {
				e.printStackTrace();
			}
			
			combineInputs(as);
			return inputs;
		}
		
		public Combiner buildInputs() {
			inputs.clear();
			index = 0;
			
			List<A> as = new ArrayList<A>();

			// shrink part 1: get the least subterm fails the property
			try {
				as = (List<A>) trA.reduce(_a);
			} catch (ShrinkException e) {
				e.printStackTrace();
			}
			
			combineInputs(as);
			return this;
		}

		private void combineInputs(List<A> as) {
			for (A a : as) {
				inputs.add(a);
			}
		}
		
		
	}
}
