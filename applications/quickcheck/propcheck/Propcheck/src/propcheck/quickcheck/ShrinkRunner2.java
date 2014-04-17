package propcheck.quickcheck;

import java.util.ArrayList;
import java.util.List;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property2;
import propcheck.shrink.ShrinkException;
import propcheck.shrink.TermReducer;
import propcheck.shrink.TreeTools;
import propcheck.tools.SimpleLogger;
import zipper.ZipperException;

public class ShrinkRunner2<A, B> implements ShrinkRunner {

	private A rootTermA, cexA;
	private B rootTermB, cexB;
	private Property2<A, B> property;
	private int shrunkCount = 0;
	
	public ShrinkRunner2(A termA, B termB, Property2<A, B> property) {
		this.rootTermA = termA;
		this.rootTermB = termB;
		cexA = rootTermA;
		cexB = rootTermB;
		this.property = property;
	}
	
	@Override
	public void run() {
		// shrink part 1: get the least subterm fails the property
		// apply the combination of inputs to the property
		apply1(rootTermA, rootTermB);

		// part 2: explode terms and apply to the property
		apply2(cexA, cexB);

		print(shrunkCount, cexA, cexB);
	}
	
	private void apply2(A a, B b) {
		Combiner<A, B> combiner = new Combiner<A, B>(a, b);
		List<Tuple<A, B>> inputs = null;
		
		Tuple<A, B> input = null;
		
		A prevCexA = null, currCexA = a;
		B prevCexB = null, currCexB = b;
		boolean err = false;
		while (prevCexA != currCexA || prevCexB != currCexB) {
			prevCexA = currCexA == null? prevCexA : currCexA;
			prevCexB = currCexB == null? prevCexB : currCexB;

			inputs = combiner.setTerms(currCexA, currCexB).getExplodedInputs();
			for (int index = 0; index < inputs.size(); index++) {
				input = inputs.get(index);
				try {
					property.apply(input._a(), input._b());
				} catch (NotTestedSkip skip) {
					// do nothing
				} catch (AssertionError error) {
					// check the size
					currCexA = TreeTools.size(input._a()) < TreeTools.size(currCexA)? input._a() : currCexA;
					currCexB = TreeTools.size(input._b()) < TreeTools.size(currCexB)? input._b() : currCexB;
					err = true;
				}
			}
			if (err) {
				shrunkCount ++;
			}
		}
		cexA = currCexA;
		cexB = currCexB;
	}

	private void apply1(A a, B b) {
		Combiner<A, B> combiner = new Combiner<A, B>(a, b).buildInputs();
		while (combiner.hasMoreInputs()) {
			Tuple<A, B> inputs = combiner.getNextInput();
			try {
				property.apply(inputs._a(), inputs._b());
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				combiner.setTerms(inputs._a(), inputs._b()).buildInputs();
				cexA = inputs._a();
				cexB = inputs._b();
				shrunkCount ++;
			}
		}
	}
	
	void print(int shrunk, A inputA, B inputB) {
		String message = String.format("Shrunk %s times, counter example:\n%s\n%s", shrunk, inputA, inputB);
		SimpleLogger.log(message);
	}

	class Combiner<A, B> {
		private A _a;
		private B _b;
		
		private List<Tuple<A, B>> inputs;
		private TermReducer<A> trA;
		private TermReducer<B> trB;
		
		private int index;
		
		public Combiner(A a, B b) {
			_a = a;
			_b = b;
			init();
		}
		
		public boolean hasMoreInputs() {
			return index < inputs.size();
		}
		
		public void init() {
			inputs = new ArrayList<Tuple<A,B>>();
			trA = new TermReducer<A>();
			trB = new TermReducer<B>();
		}
		
		public Combiner setTerms(A a, B b) {
			_a = a;
			_b = b;
			return this;
		}
		
		public Tuple<A, B> getNextInput() {
			return inputs.get(index++);
		}
		
		public List<Tuple<A, B>> getExplodedInputs() {
			inputs.clear();
			List<A> as = new ArrayList<A>();
			List<B> bs = new ArrayList<B>();

			// shrink part 1: get the least subterm fails the property
			try {
				as = (List<A>) trA.explode(_a);
				bs = (List<B>) trB.explode(_b);
			} catch (ShrinkException e) {
				e.printStackTrace();
			} catch (ZipperException e) {
				e.printStackTrace();
			}
			
			combineInputs(as, bs);
			return inputs;
		}
		
		public Combiner buildInputs() {
			inputs.clear();
			index = 0;
			
			List<A> as = new ArrayList<A>();
			List<B> bs = new ArrayList<B>();

			// shrink part 1: get the least subterm fails the property
			try {
				as = (List<A>) trA.reduce(_a);
				bs = (List<B>) trB.reduce(_b);
			} catch (ShrinkException e) {
				e.printStackTrace();
			}
			
			combineInputs(as, bs);
			return this;
		}

		private void combineInputs(List<A> as, List<B> bs) {
			for (A a : as) {
				for (B b : bs) {
					inputs.add(new Tuple<A, B>(a, b));
				}
			}
		}
		
		
	}
	
	class Tuple<A, B> {
		private A _a;
		private B _b;
		
		public Tuple(A a, B b) {
			_a = a;
			_b = b;
		}
		
		public A _a() {
			return _a;
		}
		
		public B _b() {
			return _b;
		}
		
		public String toString() {
			return String.format("<%s, %s>", _a, _b);
		}
	}
}
