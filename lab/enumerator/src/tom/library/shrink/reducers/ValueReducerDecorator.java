package tom.library.shrink.reducers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import tom.library.shrink.ds.TermNode;
import tom.library.shrink.ds.TermTreeBuilder;
import tom.library.shrink.ds.zipper.Zipper;
import tom.library.shrink.ds.zipper.ZipperException;
import tom.library.sl.Visitable;

/**
 * <p>
 * A {@code Reducer} that implements the third rule: reduces values of a term.
 * The resulting terms are having the same structures, i.e. the constructors are
 * not altered, only the values will be replaced by several smaller values.
 * <p>
 * <p>
 * A {@link Zipper} data structure is used to traverse the term and create new
 * terms out of it.
 * </p>
 * @author nauval
 *
 */
public class ValueReducerDecorator extends ReducerDecorator {

	private Collection<Visitable> results;
	private Visitable term;
	
	public ValueReducerDecorator(Reducer reducer) {
		super(reducer);
		results = new HashSet<Visitable>();
		term = (Visitable) reducer.getTerm();
	}

	@Override
	public Object getTerm() {
		return term;
	}

	/**
	 * <p>
	 * Reduces a term to several terms with smaller values.
	 * A zipper will be built to encapsulate the term and to
	 * perform traversals. This {@code Zipper} data structure
	 * is helpful when it comes to replacing a subterm with
	 * other terms as it creates an immutable object that can
	 * be changed locally but a tree still can be build out
	 * of it.
	 * </p>
	 * <p>
	 * The resulting term from this process is added to the
	 * result of the decorated {@code Reducer} and be returned.
	 * </p>
	 */
	@Override
	public Collection<Object> reduce() {
		//Zipper<TermNode> zipper = Zipper.zip(TermTreeBuilder.buildTree(term));
		reduceValue(Zipper.zip(TermTreeBuilder.buildTree(term)));
		Collection<Object> terms = reducer.reduce();
		terms.addAll(results);
		return terms;
	}

	/**
	 * Traverses down a {@code Zipper} recursively. When it reaches
	 * a leaf of a {@code Zipper}, i.e. the node has no children then
	 * this method calls {@code buildTerms()} method to build terms
	 * with some smaller values than what the leaf node has. 
	 * @param zip
	 */
	private void reduceValue(Zipper<TermNode> zip) {
		TermNode node = zip.getNode().getSource();
		for (int i = 0; i < node.getChildren().size(); i++) {
			Zipper<TermNode> z = moveDown(zip, i);
			if (z.isLeaf()) {
				results.addAll(buildTerms(z));
			} else {
				reduceValue(z);
			}
		}
	}

	/**
	 * Given a zipper which is a leaf, this method builds terms
	 * out of the reduced values contained in the zipper. 
	 * First it calculates the reduced values out of the terms
	 * and for each reduced value it replaces the current value of the 
	 * leaf with the reduced value.
	 * 
	 * @param zipper
	 * @return
	 */
	private List<Visitable> buildTerms(Zipper<TermNode> zipper){
		List<Visitable> results = new ArrayList<Visitable>();
		for (Visitable t : calculateReducedValue(getTerm(zipper))) {
			Zipper<TermNode> z = zipper;
			Visitable temp = null;
			while (!z.isTop()) {
				int idx = z.getNode().getSource().getIndex();
				z = moveUp(z);
				TermNode n = z.getNode().getSource();
				if (temp == null) {
					Visitable[] children = getTerm(z).getChildren();
					children[idx] = t;
					temp = getTerm(z).setChildren(children);
				} else {
					temp = n.getTerm().setChildAt(idx, temp);
				}
			}
			results.add(temp);
		}
		return results;
	}


	private Zipper<TermNode> moveDown(Zipper<TermNode> zip, int i){
		try {
			return zip.down(i);
		} catch (ZipperException e) {
			return zip;
		}
	}
	
	private Zipper<TermNode> moveUp(Zipper<TermNode> zip) {
		try {
			return zip.up();
		} catch (ZipperException e) {
			return zip;
		}
	}

	/**
	 * Given a term that contain a value (e.g. cs(3)), this method returns a {@code Collection} of {@code Visitable}
	 * which contain smaller values. To produce the smaller values, this method 
	 * uses the {@code PrimitiveVisitableReducerDecorator} which reduces a {@code Visitable} whose value is 
	 * a primitive type. In this current implementation the supported primitives are integer and string.
	 * a primitive
	 * @param term
	 * @return
	 */
	private Collection<Visitable> calculateReducedValue(Visitable term) {
		Collection<Visitable> values = new HashSet<Visitable>();
		addValues(values, ReducerFactory.getInstance(term).createReducer().reduce());
		return values;
	}
	
	/**
	 * Adds all values of the second prameter to the first parameter
	 * @param values
	 * @param reducedTerms
	 */
	private void addValues(Collection<Visitable> values, Collection<Object> reducedTerms) {
		for (Object object : reducedTerms) {
			values.add((Visitable) object);
		}
	}

	/**
	 * Returns a term from a given {@code Zipper}
	 * @param zipper
	 * @return
	 */
	private Visitable getTerm(Zipper<TermNode> zipper) {
		return zipper.getNode().getSource().getTerm();
	}
}
