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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof P2) {
			return _1().equals(((P2) obj)._1()) &&  _2().equals(((P2) obj)._2());
		} 
		return false;
	}

}
