package examples.data.treenat;


public class Leaf<A> extends Tree<A> {
	public A value;

	public Leaf(A t) {
		this.value=t;
	}

	public String toString() {
		return "Leaf(" + value + ")";
	}

	public int size() {
		return 0;
	}



}