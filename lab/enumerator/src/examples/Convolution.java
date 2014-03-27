package examples;

import java.util.Arrays;

import tom.library.enumerator.F;
import tom.library.enumerator.LazyList;
import tom.library.enumerator.P1;

public class Convolution {

	public static void main(String args[]) {
		
		LazyList<Integer> l1 = LazyList.fromList(Arrays.asList(1,2,3,4,5));
		//LazyList<Character> l2 = LazyList.fromList(Arrays.asList('a','b','c','d'));
		//LazyList<Integer> l1 = naturals(1);
		LazyList<Integer> l2 = range(10);
		LazyList<String> l3 = prod(l1, l2.reversals());
		
		long start = System.currentTimeMillis();
		int i = 0;
		while(!l3.isEmpty() && i<20) {
			System.out.println(l3.head());
			l3 = l3.tail();
			i++;
		}
		long stop = System.currentTimeMillis();
		System.out.println((stop-start));
		
		l1 = range(4);
		l2 = range(1000000);
		start = System.currentTimeMillis();
		l3 = prod(l1, l2.reversals());

		i = 0;
		while(!l3.isEmpty()) {
			//System.out.println(l3.head());
			l3 = l3.tail();
			i++;
		}
		stop = System.currentTimeMillis();
		System.out.println((stop-start));
		
	}
	
	/**
     * tools for LazyList
     */
    private static <A, B> LazyList<String> prod(LazyList<A> xs, final LazyList<LazyList<B>> rys) {
        if (xs.isEmpty() || rys.isEmpty()) {
            return LazyList.nil();
        }
        return goY(xs, rys);
    }

    private static <A, B> LazyList<String> goY(final LazyList<A> xs, final LazyList<LazyList<B>> rys) {
    	P1<LazyList<String>> p = new P1<LazyList<String>>() {
    		public LazyList<String> _1() {
    			return (rys.tail().isEmpty()) ? goX(xs, rys.head()) : goY(xs, rys.tail());
    		}
    	};
    	return LazyList.cons(conv(xs, rys.head()), p);
    }

    private static <A, B> LazyList<String> goX(final LazyList<A> xs, final LazyList<B> ry) {
        F<LazyList<A>, String> fs = new F<LazyList<A>, String>() {
            public String apply(LazyList<A> x) {
                return conv(x, ry);
            }
        };
        return xs.tail().tails().map(fs);
    }

    private static <A, B> String conv(LazyList<A> xs, LazyList<B> ys) {
    	String result = "";
		if(ys.isEmpty()) { return result; }
    	while(true) {
    		if(xs.isEmpty()) { return result; }
    		result = result + " + " + xs.head() + "*" + ys.head();
    		ys = ys.tail();
    		if(ys.isEmpty()) { return result; }
    		xs = xs.tail();
    	}
    }
    
    private static LazyList<Integer> naturals(final int n) {
		return LazyList.<Integer>cons(n, new P1<LazyList<Integer>>() {
			public LazyList<Integer> _1() { return naturals(n+1); }
		});
	}
    
    private static <A> LazyList<A> take(final LazyList<A> l, final long maxSize) {
		if (maxSize > 0) {
			return LazyList.<A>cons(l.head(), new P1<LazyList<A>>() {
				public LazyList<A> _1() { return take(l.tail(), maxSize-1); }
			});			
		} else {
			return LazyList.nil();
		}
	}
    
    private static LazyList<Integer> range(final int n) {
    	return take(naturals(0),n);
	}
    
    
}
