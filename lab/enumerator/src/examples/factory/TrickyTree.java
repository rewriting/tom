package examples.factory;

import examples.factory.generation.EnumerateGenerator;

public class TrickyTree<E,F,G> {
	private E elemE;
	private F elemF;
	private G elemG;
	private TrickyTree<E,F,G> left, right;

	//
	
	@EnumerateGenerator
	public TrickyTree(TrickyTree<E,F,G> left, TrickyTree<E,F,G> right, E elemE, G elemG, F elemF) {
		super();
		this.elemE = elemE;
		this.elemF = elemF;
		this.elemG = elemG;
		this.left = left;
		this.right = right;
	}

	// print
	@Override
	public String toString() {
		String ls,rs;
		if (this.left == null) {
			ls = "_";
		}else{
			ls = left.toString();
		}
		if (this.right == null) {
			rs = "_";
		}else{
			rs = right.toString();
		}
		return "Tree[" + elemE + ", " + elemF + ", " + elemG + ", " + ls + ", " + rs + "]";
	}

}
