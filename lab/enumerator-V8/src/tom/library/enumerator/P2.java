package tom.library.enumerator;

public abstract class P2<A,B> {
	/*
	 * product of 2 elements
	 */
	 public abstract A _1();
	 public abstract B _2();
	 
	 public String toString() {
			return "(" + _1() + "," + _2() + ")";
	 }

}
