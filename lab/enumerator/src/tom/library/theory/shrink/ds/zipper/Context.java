package tom.library.theory.shrink.ds.zipper;

import java.util.ArrayList;
import java.util.List;

public class Context {
	protected static final Context TOP = new Context(null, null, null, null);
	
	private final List<? extends Node> left;
	private final List<? extends Node> right;

	private final InternalNode<?> parentNode;
	private final Context parentContext;
	
	protected Context(final InternalNode<?> parentNode, 
			final Context parentContext, 
			final List<? extends Node> left, 
			final List<? extends Node> right) {
		this.parentNode = parentNode;
		this.parentContext = parentContext;
		this.left = left == null? new ArrayList<Node>() : left;
		this.right = right == null? new ArrayList<Node>() : right;
	}

	protected List<? extends Node> getLeft() {
		return left;
	}

	public List<? extends Node> getRight() {
		return right;
	}

	protected InternalNode<?> getParentNode() {
		return parentNode;
	}

	protected Context getParentContext() {
		return parentContext;
	}
}
