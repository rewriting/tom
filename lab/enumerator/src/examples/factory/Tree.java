package examples.factory;

import tom.library.factory.EnumerateGenerator;

public class Tree<E> {
	private E elem;
	private Tree<E> left, right;

	// private Color color;

	// constructors
	public Tree(E elem) {
		super();
		this.elem = elem;
		this.left = null;
		this.right = null;
	}
	
	@EnumerateGenerator
	public Tree(E elem, Tree<E> left, Tree<E> right) {
		super();
		this.elem = elem;
		this.left = left;
		this.right = right;
	}

	// setters and getters
	public E getElem() {
		return elem;
	}

	public void setElem(E elem) {
		this.elem = elem;
	}

	public Tree<E> getLeft() {
		return left;
	}

	public void setLeft(Tree<E> left) {
		this.left = left;
	}

	public Tree<E> getRight() {
		return right;
	}

	public void setRight(Tree<E> right) {
		this.right = right;
	}

	// print
	@Override
	public String toString() {
		String ls,rs;
		if (this.left == null) {
			ls = "_";
		}else{
			ls = left.toString();
		}
		if (this.right == null) {
			rs = "_";
		}else{
			rs = right.toString();
		}
		return "Tree[" + elem + ", " + ls + ", " + rs + "]";
	}

}
