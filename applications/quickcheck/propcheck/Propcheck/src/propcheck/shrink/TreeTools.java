package propcheck.shrink;

import tom.library.sl.Visitable;

public class TreeTools {
	
	public static<A> int size(A term) {
		if (term == null) {
			return Integer.MAX_VALUE;
		}
		if (term instanceof Visitable) {
			Visitable t = (Visitable) term;
			int size = sizeRec(0, t);
			return size;
		} else {
			return 0;
		}
		
	}
	
	private static int sizeRec(int s, Visitable term) {
		if (term.getChildCount() == 0) {
			return 1;
		}
		for (Visitable v : term.getChildren()) {
			s += sizeRec(s, v);
		}
		return s;
	}
	
	/**
	 * Returns a {@code TNode} tree that represents the term. Each node contains 
	 * sub-term of the given term.
	 * 
	 * @param term
	 * @return {@code TNode} tree
	 */
	public static TNode buildTree(Visitable term) {
		TNode tree = new TNode(term, 0);
		buildTreeRec(tree, term);
		return tree;
	}
	
	private static void buildTreeRec(TNode parent, Visitable parentTerm) {
		for (int i = 0; i < parentTerm.getChildCount(); i++) {
			Visitable t = parentTerm.getChildAt(i);
			TNode n = new TNode(t, i);
			parent.addChild(n);
			buildTreeRec(n, t);
		}
	}
}
