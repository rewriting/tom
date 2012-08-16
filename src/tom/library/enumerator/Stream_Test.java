package enumerator;

import fj.P1;
import fj.data.Stream;

/**
 * Produces natural numbers forever.
 */
public class Stream_Test {
	public static void main(final String[] args) {
		Stream<Integer> s = naturals(5);
		int i = 0;
		while (i < 10) {
			System.out.println("i=" + i + " --> " + s.index(i));
			i++;
		}
	}

	private static Stream<Integer> naturals(final int n) {
		P1<Stream<Integer>> cont = new P1<Stream<Integer>>() {
			public Stream<Integer> _1() {
				return naturals(n + 1);
			}
		};

		return Stream.cons(n, cont);
	}
}
