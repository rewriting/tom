package zipper;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Too much generics, simplify
 * @author nauval
 *
 * @param <T>
 */
public class Zipper<T extends Node> {
	private final ZNode<T> node;
	private final Context context;
	
	protected Zipper(final ZNode<T> node, final Context context) {
		this.node = node;
		this.context = context;
	}
	
	public static <T extends Node> Zipper<T> zip(T tree) {
		return new Zipper<T>(new ZNode<T>(tree), Context.TOP);
	}
	
	public Zipper<T> down() throws ZipperException {
		return down(0);
	}
	
	@SuppressWarnings("unchecked")
	public Zipper<T> down(int index) throws ZipperException {
		if (hasChildren() && index < node.getChildren().size()) {
			T child = (T) node.getChildren().get(index);
			List<T> left = new ArrayList<T>((List<T>) node.getChildren().subList(0, index));
			List<T> right = new ArrayList<T>(
					(List<T>) node.getChildren()
					.subList(node.getChildren().size() - index - 1, node.getChildren().size()));
 			Context ctx = new Context(node, context, left, right);
			return new Zipper<T>(new ZNode<T>(child), ctx);
		}
		throw new ZipperException("you are at leaf node or index out of bound");
	}
	
	@SuppressWarnings("unchecked")
	public Zipper<T> up() throws ZipperException {
		if (context.getNode() == null) {
			throw new ZipperException("You are at root node, cannot move up");
		}
		List<Node> children = new ArrayList<Node>();
		children.addAll(context.getLeft());
		children.add(node);
		children.addAll(context.getRight());
		ZNode<T> n = new ZNode<T>((T) context.getNode().getSource(), children);
		return new Zipper<T>(n, context.getContext());
	}
	
	public boolean isTop() {
		return context.getNode() == null;
	}
	
	public boolean isLeaf() {
		return !hasChildren();
	}
	
	protected boolean hasChildren() {
		return !node.getChildren().isEmpty();
	}
	
	/**
	 * @return the node
	 */
	public ZNode<T> getNode() {
		return node;
	}

	/**
	 * @return the context
	 */
	protected Context getContext() {
		return context;
	}
}
