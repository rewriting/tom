package tom.library.shrink.reducers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import tom.library.shrink.ds.TermNode;
import tom.library.shrink.ds.TermTreeBuilder;
import tom.library.shrink.ds.zipper.Zipper;
import tom.library.shrink.tools.TermWrapper;
import tom.library.sl.Visitable;

/**
 * A {@code Reducer} that represents the second rule of the
 * mutation rule: replace subterms with their constants.
 * @author nauval
 *
 */
public class ConstantsReducerDecorator extends ReducerDecorator {

	/** The resulting terms from the process **/
	private Collection<Visitable> results;
	/** The constants **/
	private Collection<Visitable> terminals;
	private Visitable term;
	
	public ConstantsReducerDecorator(Reducer reducer) {
		super(reducer);
		results = new HashSet<Visitable>();
		terminals = new HashSet<Visitable>();
		term = (Visitable) reducer.getTerm();
	}

	@Override
	public Object getTerm() {
		return term;
	}

	/**
	 * <p>
	 * Returns a {@code Collection} of terms from a given term.
	 * A term will be traversed down and for each subterm, the class
	 * gets the constants of with the same type as the subterm
	 * and replaces it with the retrieved constants.
	 *</p>
	 *<p>
	 * A {@code Zipper} is used in the traversal and the replacement
	 * process.
	 *</p>
	 */
	@Override
	public Collection<Object> reduce() {
		Zipper<TermNode> zipper = Zipper.zip(TermTreeBuilder.buildTree(term));
		try {
			explode(zipper);
		} catch (Exception e) {
			// do nothing, return empty result
		}
		Collection<Object> terms = reducer.reduce();
		terms.addAll(results);
		return terms;
	}
	
	/**
	 * A method where traversal process take place and for each
	 * subterm it replaces the traversed subterm with its constants
	 * (terminal constructors)
	 * @param zip
	 * @throws Exception
	 */
	private void explode(Zipper<TermNode> zip) throws Exception {
		TermNode node = zip.getNode().getSource();
		for (int i = 0; i < node.getChildren().size(); i++) {
			TermNode child = (TermNode) node.getChildren().get(i);
			Zipper<TermNode> z = zip.down(i);
			if (!z.isLeaf()) {
				buildTerminalFormTerm(child.getTerm());
				if (!isTerminalEmpty()) {
					results.addAll(buildTermFromZip(z));
				}
				explode(z);
			}
		}
	}
	
	protected boolean isTerminalEmpty() {
		return terminals.isEmpty();
	}
	
	/**
	 * Retrieve the constants (terminal constructors) ot a given term
	 * @param term
	 */
	protected void buildTerminalFormTerm(Visitable term) {
		terminals.clear();
		terminals.addAll(TermWrapper.build(term).getTerminalConstructors());
	}
	
	/**
	 * Returns a list of {@code Visitable} out of a {@code Zipper}.
	 * For each constants of the given type and a {@code Zipper} 
	 * that represents the subterm at a position, the method 
	 * creates a new term by replacing the subterm with a constant
	 * and it moves up to its parent subterm and replaces the old subterm 
	 * with the new on. It is repeatedly done until it reaches the root
	 * of the term.
	 * 
	 * @param zipper
	 * @return
	 * @throws Exception
	 */
	protected List<Visitable> buildTermFromZip(Zipper<TermNode> zipper) throws Exception {
		List<Visitable> results = new ArrayList<Visitable>();
		Visitable tmp = null;
		for (Visitable t : terminals) {
			Zipper<TermNode> z = zipper;
			while (!z.isTop()) {
				int idx = z.getNode().getSource().getIndex();
				z = z.up();
				TermNode n = z.getNode().getSource();
				if (tmp == null) {
					tmp = n.getTerm().setChildAt(idx, t);
				} else {
					tmp = n.getTerm().setChildAt(idx, tmp);
				}
			}
			results.add(tmp);
		}
		return results;
	}
}
