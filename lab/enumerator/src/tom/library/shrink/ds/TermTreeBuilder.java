package tom.library.shrink.ds;

import tom.library.sl.Visitable;

/**
 * A Helper class to build a {@link TermNode} out of a {@code Visitable} term.
 * @author nauval
 *
 */
public class TermTreeBuilder {

	/**
	 * <p>
	 * Returns a {@code TermNode} out of a {@code Visitable} term.
	 * The given term will be traversed down for and the appropriate
	 * tree of {@code TermNode} is created from the process.
	 * </p>
	 * @param term
	 * @return
	 */
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
