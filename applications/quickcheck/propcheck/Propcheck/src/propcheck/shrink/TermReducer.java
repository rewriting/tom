package propcheck.shrink;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;
import zipper.Zipper;
import zipper.ZipperException;

public class TermReducer<T> {
	private List<T> terms;
	
	public TermReducer() {
		terms = new ArrayList<T>();
	}
	
	/**
	 * Returns list of reduced terms from a term
	 * @param term
	 * @return list of reduced term
	 * @throws ShrinkException 
	 */
	public List<T> reduce(T term) throws ShrinkException {
		terms.clear();
		if (term instanceof Visitable) {
			
			Visitable t = (Visitable) term;
			List<T> constants = getConstants(t);
			if (!constants.contains(t)) {
				terms.addAll(getConstants(t));
				getReducedTerms((List<Visitable>) terms, t, t.getClass().getSuperclass());
				if (terms.isEmpty()) {
					terms.add(term);
				}
			}
			return terms;
		} else {
			terms.add(term);
			return terms;
		}
	}
	
	/**
	 * Explodes the term to many smaller terms but not all possible smaller terms.
	 * 
	 * @param term
	 * @return list of smaller terms
	 * @throws ShrinkException 
	 * @throws ZipperException 
	 */
	public List<T> explode(T term) throws ZipperException, ShrinkException {
		terms.clear();
		if (term instanceof Visitable) {
			Visitable t = (Visitable) term;
			Zipper<TNode> tree = Zipper.zip(TreeTools.buildTree(t));
			explodeRec(tree);
			if (terms.isEmpty()) {
				terms.add(term);
			}
			return terms;
		} else {
			terms.add(term);
			return terms;
		}
		
	}
	
	public void explodeRec(Zipper<TNode> zip) throws ZipperException, ShrinkException {
		TNode node = zip.getNode().getSource();
		for (int i = 0; i < node.getChildren().size(); i++) {
			TNode child = (TNode) node.getChildren().get(i);
			Zipper<TNode> z = zip.down(i);
			if (!z.isLeaf()) {
				List<? extends Visitable> constants = (List<? extends Visitable>) getConstants(child.getTerm());
				if (constants.size() > 0) {
					terms.addAll((List<T>)buildTerm(z, constants));
				}
				explodeRec(z);
			}
		}
	}
	
	public List<T> getConstants(Visitable child) {
		List<T> res = null;
		try {
			res = (List<T>) TermClass.make(child).getConstants();
		} catch (ShrinkException e) {
			if (res == null) {
				res = new ArrayList<T>();
			}
		}
		return res;
	}
	
	protected List<? extends Visitable> getReducedTerms(List<Visitable> results, Visitable term, Class<?> sort) throws ShrinkException {
		for (int i = 0; i < term.getChildCount(); i++) {
			Visitable v = term.getChildAt(i);
			List<? extends Visitable> constants = (List<? extends Visitable>) getConstants(v);
			if (v.getClass().getSuperclass().equals(sort) && !constants.contains(v)) {
				results.add(v);
			} else if (!constants.contains(v)) {
				getReducedTerms(results, v, sort);
			}
		}
		return results;
	}
	
	public List<Visitable> buildTerm(Zipper<TNode> zip, List<? extends Visitable> terms) throws ZipperException {
		List<Visitable> results = new ArrayList<Visitable>();
		Visitable tmp = null;
		for (Visitable t : terms) {
			Zipper<TNode> z = zip;
			while (!z.isTop()) {
				int idx = z.getNode().getSource().getIndex();
				z = z.up();
				TNode n = z.getNode().getSource();
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
