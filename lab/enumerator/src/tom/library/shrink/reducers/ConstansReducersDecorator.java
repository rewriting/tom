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

public class ConstansReducersDecorator extends ReducerDecorator {

	private Collection<Visitable> results;
	private Collection<Visitable> terminals;
	private Visitable term;
	
	public ConstansReducersDecorator(Reducer reducer) {
		super(reducer);
		results = new HashSet<Visitable>();
		terminals = new HashSet<Visitable>();
		term = (Visitable) reducer.getTerm();
	}

	@Override
	public Object getTerm() {
		return term;
	}

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
	
	protected void buildTerminalFormTerm(Visitable term) {
		terminals.clear();
		terminals.addAll(getConstructorTerminalsFromTerm(term));
	}
	
	protected List<Visitable> getConstructorTerminalsFromTerm(Visitable term) {
		return TermWrapper.build(term).getTerminalConstructor();
	}
	
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
