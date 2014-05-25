package tom.library.theory.shrink.suppliers.reducers;

import static tom.library.theory.shrink.tools.VisitableTools.*;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.ds.TermNode;
import tom.library.theory.shrink.ds.TermTreeBuilder;
import tom.library.theory.shrink.ds.zipper.Zipper;
import tom.library.theory.shrink.ds.zipper.ZipperException;

public class ValueReducer implements Reducer<Visitable> {
	
	private List<Visitable> results;
	private Visitable root;

	private void initialize(Visitable root) {
		this.root = root;
		if (results == null) {
			results = new ArrayList<Visitable>();
		} else {
			results.clear();
		}
	}
	
	@Override
	public List<Visitable> reduce(Visitable term) {
		initialize(term);
		Zipper<TermNode> zipper = Zipper.zip(TermTreeBuilder.buildTree(root));
		reduceValue(zipper);
		return results;
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

	private List<Visitable> calculateReducedValue(Visitable term) {
		List<Visitable> values = new ArrayList<Visitable>();
		PrimitiveReducer reducer = new PrimitiveReducer();
		if (isValueInstanceOfInteger(term)) {
			values.addAll(reducer.reduce(term));
		} else if (isValueInstanceOfString(term)) {
			values.addAll(reducer.reduce(term));
		}
		return values;
	}
	
	private Visitable getTerm(Zipper<TermNode> zipper) {
		return zipper.getNode().getSource().getTerm();
	}
}
