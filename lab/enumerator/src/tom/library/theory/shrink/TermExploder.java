package tom.library.theory.shrink;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.ds.TermNode;
import tom.library.theory.shrink.ds.TermTreeBuilder;
import tom.library.theory.shrink.ds.zipper.Zipper;
import tom.library.theory.shrink.ds.zipper.ZipperException;
import tom.library.theory.shrink.tools.TermClass;

public class TermExploder {
	private List<Object> explodedTerms;
	private List<Visitable> termTerminals;
	private Visitable term;
	
	private TermExploder(Object term) throws ShrinkException {
		if (!isInstanceOfVisitable(term)) {
			throw new ShrinkException(term + " is not a term");
		}
		this.term = (Visitable) term;
		explodedTerms = new ArrayList<Object>();
		termTerminals = new ArrayList<Visitable>();
	}
	
	public static TermExploder build(Object term) throws ShrinkException {
		return new TermExploder(term);
	}
	
	protected boolean isInstanceOfVisitable(Object term) {
		return term instanceof Visitable;
	}

	public List<Object> explodeTermToSmallerTerms() throws ShrinkException {
		Zipper<TermNode> zipper = Zipper.zip(TermTreeBuilder.buildTree(term));
		try {
			explode(zipper);
		} catch (Exception e) {
			throw new ShrinkException("Cannot explode term " + term);
		}
		if (explodedTerms.isEmpty()) {
			explodedTerms.add(term);
		}
		return explodedTerms;
	}
	
	protected void explode(Zipper<TermNode> zip) throws Exception {
		TermNode node = zip.getNode().getSource();
		for (int i = 0; i < node.getChildren().size(); i++) {
			TermNode child = (TermNode) node.getChildren().get(i);
			Zipper<TermNode> z = zip.down(i);
			if (!z.isLeaf()) {
				buildTerminalFormTerm(child.getTerm());
				if (!isTerminalEmpty()) {
					explodedTerms.addAll(buildTermFromZip(z));
				}
				explode(z);
			}
		}
	}
	
	protected boolean isTerminalEmpty() {
		return termTerminals.isEmpty();
	}
	
	protected void buildTerminalFormTerm(Visitable term) throws Exception {
		termTerminals.clear();
		termTerminals.addAll(getConstructorTerminalsFromTerm(term));
	}
	
	protected List<Visitable> getConstructorTerminalsFromTerm(Visitable term) throws Exception {
		return TermClass.build(term).getTerminalConstructor();
	}
	
	protected List<Object> buildTermFromZip(Zipper<TermNode> zipper) throws ZipperException {
		List<Object> results = new ArrayList<Object>();
		Visitable tmp = null;
		for (Visitable t : termTerminals) {
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
