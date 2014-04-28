package tom.library.theory.shrink.ds;

import tom.library.sl.Visitable;

public class TermTreeBuilder {

	public static TermNode buildTree(Visitable term) {
		TermNode tree = new TermNode(term, 0);
		buildTree(tree, term);
		return tree;
	}
	
	private static void buildTree(TermNode parent, Visitable parentTerm) {
		for (int i = 0; i < parentTerm.getChildCount(); i++) {
			Visitable t = parentTerm.getChildAt(i);
			TermNode n = new TermNode(t, i);
			parent.addChild(n);
			buildTree(n, t);
		}
	}
}
