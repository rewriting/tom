package tom.library.theory.shrink.ds.zipper;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Too much generics, simplify
 * @author nauval
 *
 * @param <T>
 */
public class Zipper<T extends Node> {
	private final InternalNode<T> node;
	private final Context context;
	
	protected Zipper(final InternalNode<T> node, final Context context) {
		this.node = node;
		this.context = context;
	}
	
	public static <T extends Node> Zipper<T> zip(T tree) {
		return new Zipper<T>(new InternalNode<T>(tree), Context.TOP);
	}
	
	public Zipper<T> up() throws ZipperException {
		if (isTop()) {
			throw new ZipperException("You are at root node, cannot move up");
		}
		return new Zipper<T>(newInternalNodeFromParent(), context.getParentContext());
	}

	protected InternalNode<T> newInternalNodeFromParent() {
		return new InternalNode<T>(getParentSource(), buildChildrenForUp());
	}

	@SuppressWarnings("unchecked")
	protected T getParentSource() {
		return (T) context.getParentNode().getSource();
	}
	
	protected List<Node> buildChildrenForUp() {
		List<Node> children = new ArrayList<Node>();
		children.addAll(context.getLeft());
		children.add(node);
		children.addAll(context.getRight());
		return children;
	}
	
	public Zipper<T> down() throws ZipperException {
		return down(0);
	}
	
	public Zipper<T> down(int index) throws ZipperException {
		if (isIndexInbound(index)) {
			return new Zipper<T>(newInternalNodeFromChild(index), newContextForChildAt(index));
		}
		throw new ZipperException("you are at leaf node or index out of bound");
	}

	protected Context newContextForChildAt(int index) {
		Context ctx = new Context(node, context, buildLeft(index), buildRight(index));
		return ctx;
	}

	protected InternalNode<T> newInternalNodeFromChild(int index) {
		return new InternalNode<T>(getNodeChildAt(index));
	}

	protected List<T> buildRight(int index) {
		return new ArrayList<T>(getChildrenFromIndexToEnd(index));
	}

	@SuppressWarnings("unchecked")
	protected List<T> getChildrenFromIndexToEnd(int index) {
		return (List<T>) node.getChildren().subList(node.getChildren().size() - index - 1, 
				node.getChildren().size());
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> buildLeft(int index) {
		return new ArrayList<T>((List<T>) getChildrenFromStartToIndex(index));
	}

	protected List<? extends Node> getChildrenFromStartToIndex(int index) {
		return node.getChildren().subList(0, index);
	}

	@SuppressWarnings("unchecked")
	protected T getNodeChildAt(int index) {
		return (T) node.getChildren().get(index);
	}

	protected boolean isIndexInbound(int index) {
		return index < node.getChildren().size();
	}
	
	public boolean isTop() {
		return context.getParentNode() == null;
	}
	
	public boolean isLeaf() {
		return !hasChildren();
	}
	
	protected boolean hasChildren() {
		return !node.getChildren().isEmpty();
	}
	
	public InternalNode<T> getNode() {
		return node;
	}

	protected Context getContext() {
		return context;
	}
}
