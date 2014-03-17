package examples;

import java.math.BigInteger;
import tom.library.enumerator.*;

public class Bug {
    /*

    empty() * singleton("foo") == empty()
    singleton("foo") * empty() == empty()

    cela vient de ma représentation douteuse des listes lazy.
    Je les ai remplacées par des liste lazy "classiques" et
    j'ai rendu le _conv un peu plus lazy (le zipWith en fait mais je l'ai inliné).

    */

	public static void main(String args[]) {
		final Enumeration<T> aEnum = Enumeration.singleton((T)new A());
        final Enumeration<T> empty = Enumeration.empty();

        Enumeration<P2<T, T>> res1 = aEnum.times(empty);
        Enumeration<P2<T, T>> res2 = empty.times(aEnum);

        System.out.println(res1);
        System.out.println(res2);

    }

	private static class T {
	}

	private static class A extends T {
		public String toString() {
			return "a";
		}
	}

	private static class Foo extends T {
		private T arg;

		public Foo(T t) {
			this.arg = t;
		}

		public String toString() {
			return "foo(" + arg + ")";
		}
	}

}
