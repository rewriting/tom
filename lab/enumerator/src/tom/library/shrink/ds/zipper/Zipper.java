package tom.library.shrink.ds.zipper;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * An implementation of the <a href="http://www.st.cs.uni-sb.de/edu/seminare/2005/advanced-fp/docs/huet-zipper.pdf">
 * Zipper</a> data structure. It supports two traversal operations: up and down.
 * 
 * </p>
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
	
	/**
	 * Zips an instance of {@code Node} and returns the {@code Zipper} of the 
	 * given tree.
	 * @param tree
	 * @return
	 */
	public static <T extends Node> Zipper<T> zip(T tree) {
		return new Zipper<T>(new InternalNode<T>(tree), Context.TOP);
	}
	
	/**
	 * Returns a {@code Zipper} that represents a location at one level above
	 * the original location (the parent location).
	 * @return
	 * @throws ZipperException
	 */
	public Zipper<T> up() throws ZipperException {
		if (isTop()) {
			throw new ZipperException("You are at root node, cannot move up");
		}
		return new Zipper<T>(newInternalNodeFromParent(), context.getParentContext());
	}

	/**
	 * Returns new internal node that represents the parent location.
	 * @return
	 */
	protected InternalNode<T> newInternalNodeFromParent() {
		return new InternalNode<T>(getParentSource(), buildChildrenForUp());
	}

	/**
	 * Returns the object contained in the parent location.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T getParentSource() {
		return (T) context.getParentNode().getSource();
	}
	
	/**
	 * Builds a list of children when the up operation is called.
	 * It is done by collecting all the siblings of the original
	 * location and itself as well.
	 * @return
	 */
	protected List<Node> buildChildrenForUp() {
		List<Node> children = new ArrayList<Node>();
		children.addAll(context.getLeft());
		children.add(node);
		children.addAll(context.getRight());
		return children;
	}
	
	/**
	 * Moves down from a parent to a child's location, the default
	 * is the first child (child at index 0).
	 * @return
	 * @throws ZipperException
	 */
	public Zipper<T> down() throws ZipperException {
		return down(0);
	}
	
	/**
	 * Moves down from a parent to a child's location at a given index.
	 * 
	 * @param index
	 * @return
	 * @throws ZipperException
	 */
	public Zipper<T> down(int index) throws ZipperException {
		if (isIndexInbound(index)) {
			return new Zipper<T>(newInternalNodeFromChild(index), newContextForChildAt(index));
		}
		throw new ZipperException("you are at leaf node or index out of bound");
	}

	/**
	 * Builds new {@code Context} for a child at a given index
	 * @param index
	 * @return
	 */
	protected Context newContextForChildAt(int index) {
		Context ctx = new Context(node, context, buildLeft(index), buildRight(index));
		return ctx;
	}

	/**
	 * Returns new internal node that represents a child location
	 * @param index
	 * @return
	 */
	protected InternalNode<T> newInternalNodeFromChild(int index) {
		return new InternalNode<T>(getNodeChildAt(index));
	}

	/**
	 * Returns a list of nodes that represent siblings whose have
	 * greater index than the current location
	 * @param index
	 * @return
	 */
	protected List<T> buildRight(int index) {
		return new ArrayList<T>(getChildrenFromIndexToEnd(index));
	}

	/**
	 * Returns a list of children whose index is greater than 
	 * the given index.
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> getChildrenFromIndexToEnd(int index) {
		return (List<T>) node.getChildren().subList(node.getChildren().size() - index - 1, 
				node.getChildren().size());
	}
	
	/**
	 * Returns a list of nodes that represent siblings whose have
	 * lesser index than the current location
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> buildLeft(int index) {
		return new ArrayList<T>((List<T>) getChildrenFromStartToIndex(index));
	}

	/**
	 * Returns a list of children whose index is lesser than 
	 * the given index.
	 * @param index
	 * @return
	 */
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
