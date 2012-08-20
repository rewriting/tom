package enumerator;

import java.math.BigInteger;

public class LazyList_Test {

	public static void main(final String[] args) {
		LazyList<Integer> s = naturals(5);
		
		for(int i=0; i<5 ; i++) {
			System.out.println("i=" + i + " --> " + s.index(BigInteger.valueOf(i)));
		}
		
		LazyList<LazyList<Integer>> tails = s.tails();
		for(int i=0; i<5 ; i++) {
			System.out.print("tail " + i + " --> ");
			for(int j=0; j<5 ; j++) { System.out.print(tails.index(BigInteger.valueOf(i)).index(BigInteger.valueOf(j)) + " "); }
			System.out.println("...");
		}
		
		LazyList<LazyList<Integer>> revs = s.reversals();
		for(int i=0; i<5 ; i++) {
			System.out.println("rev " + i + " --> " + revs.index(BigInteger.valueOf(i)).toList());
		}
	}

	private static LazyList<Integer> naturals(final int n) {
		return LazyList.<Integer>fromPair(new P2<Integer,LazyList<Integer>>() {
			public Integer _1() { return n; }
			public LazyList<Integer> _2() { return naturals(n+1); }
		});
	}

}
