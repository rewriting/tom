package tom.library.enumerator;

public abstract class P1<A> {
	/*
	 * product of 1 element
	 */
	public abstract A _1();
	 
	 public String toString() {
			return "(" + _1() + ")";
	 }
}
