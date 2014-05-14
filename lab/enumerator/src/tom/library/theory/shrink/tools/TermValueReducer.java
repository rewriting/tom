package tom.library.theory.shrink.tools;

import static tom.library.theory.shrink.tools.VisitableTools.getValueFromTermInteger;
import static tom.library.theory.shrink.tools.VisitableTools.getValueFromTermString;
import static tom.library.theory.shrink.tools.VisitableTools.isValueInstanceOfInteger;
import static tom.library.theory.shrink.tools.VisitableTools.isValueInstanceOfString;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.ShrinkException;
import tom.library.theory.shrink.ds.TermNode;
import tom.library.theory.shrink.ds.TermTreeBuilder;
import tom.library.theory.shrink.ds.zipper.Zipper;
import tom.library.theory.shrink.ds.zipper.ZipperException;

public class TermValueReducer {
	private Visitable root;
	private List<Object> reducedTerms;
	
	private TermValueReducer(Object term) throws ShrinkException {
		initialize(term);
	}

	public static TermValueReducer build(Object term) throws ShrinkException {
		return new TermValueReducer(term);
	}
	
	private void initialize(Object term) throws ShrinkException {
		if (!isInstanceOfVisitable(term)) {
			throw new ShrinkException(term + "is not a term");
		}
		reducedTerms = new ArrayList<Object>();
		root = (Visitable) term;
	}

	private boolean isInstanceOfVisitable(Object term) {
		return term instanceof Visitable;
	}
	
	public List<Object> buildTermsWithReducedValue() {
		Zipper<TermNode> zipper = Zipper.zip(TermTreeBuilder.buildTree(root));
		reduceValue(zipper);
		if (reducedTerms.isEmpty()) {
			reducedTerms.add(root);
		}
		return reducedTerms;
	}
	
	private void reduceValue(Zipper<TermNode> zip) {
		TermNode node = zip.getNode().getSource();
		for (int i = 0; i < node.getChildren().size(); i++) {
			Zipper<TermNode> z = moveDown(zip, i);
			if (z.isLeaf()) {
				reducedTerms.addAll(buildTerms(z));
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

	private List<Object> buildTerms(Zipper<TermNode> zipper){
		List<Object> results = new ArrayList<Object>();
		for (Visitable t : calculateReducedValue(getTerm(zipper))) {
			Zipper<TermNode> z = zipper;
			Visitable tmp = null;
			while (!z.isTop()) {
				int idx = z.getNode().getSource().getIndex();
				z = moveUp(z);
				TermNode n = z.getNode().getSource();
				if (tmp == null) {
					Visitable[] children = getTerm(z).getChildren();
					children[idx] = t;
					tmp = getTerm(z).setChildren(children);
				} else {
					tmp = n.getTerm().setChildAt(idx, tmp);
				}
			}
			results.add(tmp);
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
		if (isValueInstanceOfInteger(term)) {
			int value = getValueFromTermInteger(term);
			values.addAll(IntegerReducer.build().getReducedVisitableValue(value));
		} else if (isValueInstanceOfString(term)) {
			String value = getValueFromTermString(term);
			values.addAll(StringReducer.build().getReducedVisitableValue(value));
		}
		return values;
	}
	
	private Visitable getTerm(Zipper<TermNode> zipper) {
		return zipper.getNode().getSource().getTerm();
	}
}
