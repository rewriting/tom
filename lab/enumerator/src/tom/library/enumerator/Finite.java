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
	
	private Finite(BigInteger c, Operation o) {
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
		return (A) eval(i);
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
		//final BigInteger newCard = card.multiply(other.card);
		final BigInteger newCard = karatsuba(card, other.card);
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
		for(BigInteger i=ZERO ; i.compareTo(card)<0; i=i.add(ONE)) {
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
			return toList().equals(((Finite<?>)obj).toList());
		}
		return super.equals(obj);
	}

	private Object eval(BigInteger index) {
		Stack<Instruction> stack = new Stack<Instruction>();
		stack.push(new IDone());
		
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
				case Operation.MULT:
					OMult mul = (OMult) op;
					BigInteger[] qr = index.divideAndRemainder(mul.finite2.card);
					final BigInteger q = qr[0];
					final BigInteger r = qr[1];
					index = q;
					stack.push(new IP2(mul.finite2,r));
					op = mul.finite1.operation;
					break;
				case Operation.SINGLETON:
					OSingleton<A> singleton = (OSingleton) op;
					if (index.compareTo(ZERO) == 0) {
						val = singleton.value;
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
					IMap map = (IMap) inst;
					val = map.fun.apply(val);
					break;
				case Instruction.P1:
					IP1 p1 = (IP1) inst;
					val = pair_o(p1.val, val);
					break;
				case Instruction.P2:
					IP2 p2 = (IP2) inst;
					op = p2.finite.operation;
					index = p2.r;
					stack.push(new IP1(val));
					evalOp = true;
					break;
				}
			}
		}
	}

	private P2<Object,Object> pair_o(final Object i, final Object j) {
		return new P2<Object, Object>() {
			public Object _1() { return i; }
			public Object _2() { return j; }
		};
	}
	
	private static BigInteger karatsuba(BigInteger x, BigInteger y) {

        // cutoff to brute force
        int N = Math.max(x.bitLength(), y.bitLength());
        if (N <= 2000) return x.multiply(y);                // optimize this parameter

        // number of bits divided by 2, rounded up
        N = (N / 2) + (N % 2);

        // x = a + 2^N b,   y = c + 2^N d
        BigInteger b = x.shiftRight(N);
        BigInteger a = x.subtract(b.shiftLeft(N));
        BigInteger d = y.shiftRight(N);
        BigInteger c = y.subtract(d.shiftLeft(N));

        // compute sub-expressions
        BigInteger ac    = karatsuba(a, c);
        BigInteger bd    = karatsuba(b, d);
        BigInteger abcd  = karatsuba(a.add(b), c.add(d));

        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2*N));
    }

	/*
	 * classes for VM
	 */
	private static abstract class Instruction {
		protected int code;	
		protected final static int DONE  = 1;
		protected final static int MAP   = 2;
		protected final static int P2 = 3;
		protected final static int P1 = 4;

		public int getCode() { return this.code; }
	}

	private static class IDone extends Instruction {
		public IDone() { this.code = DONE; }
	}

	private static class IMap<A> extends Instruction {
		public F<A,A> fun;
		public IMap(F<A,A> f) { this.code = MAP; this.fun = f; }
	}

	private static class IP2 extends Instruction {
		public Finite finite;
		public BigInteger r;
		public IP2(Finite fin, BigInteger r) { this.code = P2; this.finite = fin; this.r = r; }
	}

	private static class IP1 extends Instruction {
		public Object val;
		public IP1(Object val) { this.code = P1; this.val = val; }
	}

	private static abstract class Operation {
		protected int code;	
		protected final static int ADD       = 1;
		protected final static int MULT      = 2;
		protected final static int EMPTY     = 3;
		protected final static int SINGLETON = 4;
		protected final static int MAP       = 5;

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
