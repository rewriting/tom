package zipper;

import java.util.ArrayList;
import java.util.List;

public class Context {
	protected static final Context TOP = new Context(null, null, null, null);
	private final List<? extends Node> left;
	private final List<? extends Node> right;

	// node of the context, parent of the focused node
	private final ZNode<?> node;
	private final Context context;
	
	protected Context(final ZNode<?> node, final Context context, final List<? extends Node> left, final List<? extends Node> right) {
		this.node = node;
		this.context = context;
		this.left = left == null? new ArrayList<Node>() : left;
		this.right = right == null? new ArrayList<Node>() : right;
	}

	/**
	 * @return the left
	 */
	protected List<? extends Node> getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public List<? extends Node> getRight() {
		return right;
	}

	/**
	 * @return the parentNode
	 */
	protected ZNode<?> getNode() {
		return node;
	}

	/**
	 * @return the context
	 */
	protected Context getContext() {
		return context;
	}
	
}
