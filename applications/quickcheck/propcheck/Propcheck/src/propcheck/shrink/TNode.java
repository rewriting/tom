package propcheck.shrink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tom.library.sl.Visitable;
import zipper.Node;

public class TNode implements Node {

	private Visitable term;
	private int index;
	private List<TNode> children;
	
	public TNode(Visitable term, int index) {
		this.term = term;
		this.index = index;
		this.children = new ArrayList<TNode>();
	}
	
	public TNode(Visitable term, int index, TNode... children) {
		this.term = term;
		this.index = index;
		this.children = Arrays.asList(children);
	}
	
	public void addChild(TNode child) {
		children.add(child);
	}
	
	public void addChild(List<TNode> children) {
		this.children.addAll(children);
	}
	
	
	@Override
	public List<? extends Node> getChildren() {
		return children;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the term
	 */
	public Visitable getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(Visitable term) {
		this.term = term;
	}

}
