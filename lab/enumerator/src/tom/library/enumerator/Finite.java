package tom.library.enumerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

/*
 * This class represent finite sets
 * Basics constructors are empty and singleton
 * They can be combined with plus, times and apply operators
 */
public class Finite<A> {
	private BigInteger card;
	private Operation operation;

	/**
	 * constructors
	 */
	public Finite(BigInteger c, Operation o) {
		this.card = c;
		this.operation = o;
	}

	public static <A> Finite<A> empty() {
		return new Finite<A>(ZERO, new OEmpty());
	}

	public static <A> Finite<A> singleton(final A x) {
		return new Finite<A>(ONE, new OSingleton<A>(x));
	}

	/**
	 * cardinality of the set
	 */
	public BigInteger getCard() {
		return card;
	}

	/**
	 * retrieve an element
	 */
	public A get(BigInteger i) {
		return eval(i);
	}

	/**
	 * union of two sets
	 */
	public Finite<A> plus(final Finite<A> other) {
		final BigInteger newCard = card.add(other.card);
		return new Finite<A>(newCard, new OAdd(this, other));
	}

	/**
	 * cartesian product of two sets
	 */
	public <B> Finite<P2<A, B>> times(final Finite<B> other) {
		final BigInteger newCard = card.multiply(other.card);
		return new Finite<P2<A,B>>(newCard, new OMult(this, other));
	}

	/**
	 * compose a function with the indexer
	 */
	public <B> Finite<B> map(final F<A,B> f) {
		return new Finite<B>(card, new OMap(this,f));
	}

	/**
	 * apply a set of functions to a set of elements (cartesian product)
	 */
	public <B> Finite<B> applyFiniteFunctions(final Finite<F<A,B>> subject) {
		F<P2<F<A,B>,A>,B> pair = new F<P2<F<A,B>,A>,B>() {
			// pair : f x a -> f(a) 
			public B apply(P2<F<A,B>,A> p) { return p._1().apply(p._2()); }
		};
		return subject.times(Finite.this).map(pair);
	}

	List<A> toList() {
		List<A> res = new ArrayList<A>();
		for(BigInteger i=ZERO ; i.compareTo(getCard())<0; i=i.add(ONE)) {
			A elt = get(i);
			res.add(elt);
		}
		return res;
	}

	public static <A> Finite<A> fromList(List<A> arg) {
		Finite<A> res = Finite.empty();
		for(A e:arg) {
			res = res.plus(Finite.singleton(e));
		}
		return res;
	}

	public String toString() {
		return toList().toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Finite) {
			return toList().equals(((Finite)obj).toList());
		}
		return super.equals(obj);
	}

	private A eval(BigInteger index) {
		Stack<Instruction> stack = new Stack<Instruction>();
		Operation op = this.operation;
		Object val = null;
		boolean evalOp = true;
		while(true) {
			if(evalOp) {
				switch(op.getCode()) {
				case Operation.ADD:
					OAdd add = (OAdd) op;
					if(index.compareTo(add.finite1.card) < 0) {
						op = add.finite1.operation;
					} else {
						op = add.finite2.operation;
						index = index.subtract(add.finite1.card);
					}
					break;
				case Operation.EMPTY:
					throw new RuntimeException("index out of range");
					break;
				case Operation.MULT:
					OMult mul = (OMult) op;
					BigInteger[] qr = index.divideAndRemainder(mul.finite2.card);
					final BigInteger q = qr[0];
					final BigInteger r = qr[1];
					index = q;
					stack.push(new IPair1(mul.finite2,r));
					op = mul.finite1.operation;
					break;
				case Operation.SINGLETON:
					OSingleton<A> sing = (OSingleton) op;
					if (index.compareTo(ZERO) == 0) {
						val = sing.value;
						evalOp = false;
					} else {
						throw new RuntimeException("index out of range");
					}
					break;
				case Operation.MAP:
					OMap map = (OMap) op;
					stack.push(new IMap(map.fun));
					op = map.finite.operation;
					break;
				}
			} else {
				Instruction inst = stack.pop();
				switch(inst.getCode()) {
				case Instruction.DONE:
					return val;
				case Instruction.MAP:
					IMap<A> map = (IMap) inst;
					val = map.fun.apply(val);
					break;
				case Instruction.PAIR2:
					IPair2<A> pair2 = (IPair2) inst;
					val = pa(pair2.snd, val);
					break;
				case Instruction.PAIR1:
					IPair1 pair1 = (IPair1) inst;
					op = pair1.finite.operation;
					index = pair1.r;
					stack.push(new IPair2<A>(val));
					evalOp = true;
					break;
				}
			}


		}
	}

	private P2<A,A> pa(final A i, final A j) {
		return new P2<A, A>() {
			public A _1() { return i; }
			public A _2() { return j; }
		};
	}
	
	/*
	 * classes for VM
	 */
	private static abstract class Instruction {
		protected int code;	
		protected static int DONE  = 1;
		protected static int MAP   = 2;
		protected static int PAIR1 = 3;
		protected static int PAIR2 = 4;

		public int getCode() { return this.code; }
	}

	private static class IDone extends Instruction {
		public IDone() { this.code = DONE; }
	}

	private static class IMap<A> extends Instruction {
		public F<A,A> fun;
		public IMap(F<A,A> f) { this.code = MAP; this.fun = f; }
	}

	private static class IPair1 extends Instruction {
		public Finite finite;
		public BigInteger r;
		public IPair1(Finite fin, BigInteger r) { this.code = PAIR1; this.finite = fin; this.r = r; }
	}

	private static class IPair2<A> extends Instruction {
		public A snd;
		public IPair2(A x) { this.code = PAIR2; this.snd = x; }
	}

	private static abstract class Operation {
		protected int code;	
		protected static int ADD       = 1;
		protected static int MULT      = 2;
		protected static int EMPTY     = 3;
		protected static int SINGLETON = 4;
		protected static int MAP       = 5;

		public int getCode() { return this.code; }
	}

	private static class OEmpty extends Operation {
		public OEmpty() { this.code = EMPTY; }
	}

	private static class OSingleton<A> extends Operation {
		public A value;
		public OSingleton(A v) { this.code = SINGLETON; this.value = v; }
	}

	private static class OAdd extends Operation {
		public Finite finite1;
		public Finite finite2;
		public OAdd(Finite fin1, Finite fin2) { this.code = ADD; this.finite1 = fin1; this.finite2 = fin2; }
	}

	private static class OMult extends Operation {
		public Finite finite1;
		public Finite finite2;
		public OMult(Finite fin1, Finite fin2) { this.code = MULT; this.finite1 = fin1; this.finite2 = fin2; }
	}

	private static class OMap extends Operation {
		public Finite finite;
		public F fun;
		public OMap(Finite fin, F f) { this.code = MAP; this.finite = fin; this.fun = f; }
	}


}
