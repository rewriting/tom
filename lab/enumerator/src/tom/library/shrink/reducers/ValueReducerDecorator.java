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

	@Override
	public Collection<Object> reduce() {
		Zipper<TermNode> zipper = Zipper.zip(TermTreeBuilder.buildTree(term));
		reduceValue(zipper);
		Collection<Object> terms = reducer.reduce();
		//terms.removeAll(results);
		terms.addAll(results);
		return terms;
	}

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

	private Zipper<TermNode> moveDown(Zipper<TermNode> zip, int i){
		try {
			return zip.down(i);
		} catch (ZipperException e) {
			return zip;
		}
	}

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

	private Zipper<TermNode> moveUp(Zipper<TermNode> zip) {
		try {
			return zip.up();
		} catch (ZipperException e) {
			return zip;
		}
	}

	private Collection<Visitable> calculateReducedValue(Visitable term) {
		Collection<Visitable> values = new HashSet<Visitable>();
		Reducer reducer = new PrimitiveVisitableReducerDecorator(new BaseReducer(term));
		addValues(values, reducer.reduce());
		return values;
	}
	
	private void addValues(Collection<Visitable> values, Collection<Object> reducedTerms) {
		for (Object object : reducedTerms) {
			values.add((Visitable) object);
		}
	}

	private Visitable getTerm(Zipper<TermNode> zipper) {
		return zipper.getNode().getSource().getTerm();
	}
}
