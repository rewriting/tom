package examples.data.treenat;


public class Fork<A> extends Tree<A> {
	public Tree<A> left;
	public Tree<A> right;

	public Fork(Tree<A> l, Tree<A> r) {
		this.left = l;
		this.right = r;
	}

	public String toString() {
		return "Fork(" + left + "," + right + ")";
	}

	public int size() {
		return 1 + left.size() + right.size();
	}
}