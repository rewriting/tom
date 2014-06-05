package tom.library.shrink.reducers;

import java.util.ArrayList;
import java.util.List;

import tom.library.shrink.ds.TermNode;
import tom.library.shrink.ds.TermTreeBuilder;
import tom.library.shrink.ds.zipper.Zipper;
import tom.library.shrink.tools.TermWrapper;
import tom.library.sl.Visitable;

public class ExplotionReducer implements Reducer<Visitable> {

	private List<Visitable> results;
	private List<Visitable> terminalConstructors;
	
	private void initialize() {
		if (results == null) {
			results = new ArrayList<Visitable>();
		} else {
			results.clear();
		}
		
		if (terminalConstructors == null) {
			terminalConstructors = new ArrayList<Visitable>();
		} else {
			terminalConstructors.clear();
		}
	}
	
	@Override
	public List<Visitable> reduce(Visitable term) {
		initialize();
		Zipper<TermNode> zipper = Zipper.zip(TermTreeBuilder.buildTree(term));
		try {
			explode(zipper);
		} catch (Exception e) {
			// do nothing, return empty result
		}
		return results;
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
		return terminalConstructors.isEmpty();
	}
	
	protected void buildTerminalFormTerm(Visitable term) {
		terminalConstructors.clear();
		terminalConstructors.addAll(getConstructorTerminalsFromTerm(term));
	}
	
	protected List<Visitable> getConstructorTerminalsFromTerm(Visitable term) {
		return TermWrapper.build(term).getTerminalConstructor();
	}
	
	protected List<Visitable> buildTermFromZip(Zipper<TermNode> zipper) throws Exception {
		List<Visitable> results = new ArrayList<Visitable>();
		Visitable tmp = null;
		for (Visitable t : terminalConstructors) {
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
