package propcheck.quickcheck;

import java.util.ArrayList;
import java.util.List;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property3;
import propcheck.shrink.ShrinkException;
import propcheck.shrink.TermReducer;
import propcheck.shrink.TreeTools;
//import propcheck.shrink.TermReducer;
import propcheck.tools.SimpleLogger;
import zipper.ZipperException;

public class ShrinkRunner3<A, B, C> implements ShrinkRunner {

	private A rootTermA, cexA;
	private B rootTermB, cexB;
	private C rootTermC, cexC;

	private Property3<A, B, C> property;
	private int shrunkCount = 0;

	public ShrinkRunner3(A termA, B termB, C termC, Property3<A, B, C> property) {
		this.rootTermA = termA;
		this.rootTermB = termB;
		this.rootTermC = termC;
		cexA = termA;
		cexB = termB;
		cexC = termC;
		this.property = property;
	}

	@Override
	public void run() {
		// shrink part 1: get the least subterm fails the property
		// apply the combination of inputs to the property
		apply1(rootTermA, rootTermB, rootTermC);
		
		// part 2: explode terms and apply to the property
		apply2(cexA, cexB, cexC);
		
		print(shrunkCount, cexA, cexB, cexC);
	}

	// TODO continue this
	/**
	 * Algorithm:
	 * while not reaching a fix point
	 * 	explode the current counter example
	 * 	for each exploded term
	 * 		test the prop
	 * 		if fails then
	 * 			compare with the previous cex, set current cex to be the min out of the two
	 * 		end
	 * 	end
	 * end
	 * 
	 * @param a
	 * @param b
	 * @param c
	 */
	private void apply2(A a, B b, C c) {
		Combiner<A, B, C> combiner = new Combiner<A, B, C>(a, b, c);
		List<Tuple<A, B, C>> inputs = null;//combiner.getExplodedInputs();
		//int index = 0;
//		if (inputs.size() <= 0) {
//			return;
//		}
		
		Tuple<A, B, C> input = null;
		
		A prevCexA = null, currCexA = a;
		B prevCexB = null, currCexB = b;
		C prevCexC = null, currCexC = c;
		boolean err = false;
//		System.out.println("ShrinkRunner3.apply2()");
		while (prevCexA != currCexA || prevCexB != currCexB || prevCexC != currCexC) {
			prevCexA = currCexA == null? prevCexA : currCexA;
			prevCexB = currCexB == null? prevCexB : currCexB;
			prevCexC = currCexC == null? prevCexC : currCexC;
//			System.out.println(String.format("pcA: %s | ccA: %s", prevCexA, currCexA));
//			System.out.println(String.format("pcB: %s | ccB: %s", prevCexB, currCexB));
//			System.out.println(String.format("pcC: %s | ccC: %s", prevCexC, currCexC));
			inputs = combiner.setTerms(currCexA, currCexB, currCexC).getExplodedInputs();
			for (int index = 0; index < inputs.size(); index++) {
				input = inputs.get(index);
				try {
					property.apply(input._a(), input._b(), input._c());
				} catch (NotTestedSkip skip) {
					// do nothing
				} catch (AssertionError error) {
					// check the size
					currCexA = TreeTools.size(input._a()) < TreeTools.size(currCexA)? input._a() : currCexA;
					currCexB = TreeTools.size(input._b()) < TreeTools.size(currCexB)? input._b() : currCexB;
					currCexC = TreeTools.size(input._c()) < TreeTools.size(currCexC)? input._c() : currCexC;
					err = true;
				}
			}
			if (err) {
				shrunkCount ++;
			}
		}
		cexA = currCexA;
		cexB = currCexB;
		cexC = currCexC;
	}

	private void apply1(A a, B b, C c) {
		Combiner<A, B, C> combiner = new Combiner<A, B, C>(a, b, c).buildInputs();
		while (combiner.hasMoreInputs()) {
			Tuple<A, B, C> inputs = combiner.getNextInput();
			try {
				property.apply(inputs._a(), inputs._b(), inputs._c());
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				combiner.setTerms(inputs._a(), inputs._b(), inputs._c()).buildInputs();
				cexA = inputs._a();
				cexB = inputs._b();
				cexC = inputs._c();
				shrunkCount ++;
			}
		}
	}

	
	void print(int shrunk, A inputA, B inputB, C inputC) {
		String message = String.format("Shrunk %s times, counter example:\n%s\n%s\n%s", shrunk, inputA, inputB, inputC);
		SimpleLogger.log(message);
	}
	
	class Combiner<A, B, C> {
		private A _a;
		private B _b;
		private C _c;
		
		private List<Tuple<A, B, C>> inputs;
		private TermReducer<A> trA;
		private TermReducer<B> trB;
		private TermReducer<C> trC;
		
		private int index;
		
		public Combiner(A a, B b, C c) {
			_a = a;
			_b = b;
			_c = c;
			init();
		}
		
		public boolean hasMoreInputs() {
			return index < inputs.size();
		}
		
		public void init() {
			inputs = new ArrayList<Tuple<A,B,C>>();
			trA = new TermReducer<A>();
			trB = new TermReducer<B>();
			trC = new TermReducer<C>();
		}
		
		public Combiner setTerms(A a, B b, C c) {
			_a = a;
			_b = b;
			_c = c;
			return this;
		}
		
		public Tuple<A, B, C> getNextInput() {
			return inputs.get(index++);
		}
		
		public List<Tuple<A, B, C>> getExplodedInputs() {
			inputs.clear();
			List<A> as = new ArrayList<A>();
			List<B> bs = new ArrayList<B>();
			List<C> cs = new ArrayList<C>();

			// shrink part 1: get the least subterm fails the property
			try {
				as = (List<A>) trA.explode(_a);
				bs = (List<B>) trB.explode(_b);
				cs = (List<C>) trC.explode(_c);
			} catch (ShrinkException e) {
				e.printStackTrace();
			} catch (ZipperException e) {
				e.printStackTrace();
			}
			
			combineInputs(as, bs, cs);
			return inputs;
		}
		
		public Combiner buildInputs() {
			inputs.clear();
			index = 0;
			
			List<A> as = new ArrayList<A>();
			List<B> bs = new ArrayList<B>();
			List<C> cs = new ArrayList<C>();

			// shrink part 1: get the least subterm fails the property
			try {
				as = (List<A>) trA.reduce(_a);
				bs = (List<B>) trB.reduce(_b);
				cs = (List<C>) trC.reduce(_c);
			} catch (ShrinkException e) {
				e.printStackTrace();
			}
			
			combineInputs(as, bs, cs);
			return this;
		}

		private void combineInputs(List<A> as, List<B> bs, List<C> cs) {
			for (A a : as) {
				for (B b : bs) {
					for (C c : cs) {
						inputs.add(new Tuple<A, B, C>(a, b, c));
					}
				}
			}
		}
		
		
	}
	
	class Tuple<A, B, C> {
		private A _a;
		private B _b;
		private C _c;
		
		public Tuple(A a, B b, C c) {
			_a = a;
			_b = b;
			_c = c;
		}
		
		public A _a() {
			return _a;
		}
		
		public B _b() {
			return _b;
		}
		
		public C _c() {
			return _c;
		}
		
		public String toString() {
			return String.format("<%s, %s, %s>", _a, _b, _c);
		}
	}
}
