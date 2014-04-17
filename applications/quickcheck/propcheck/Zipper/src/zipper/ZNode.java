package zipper;

import java.util.ArrayList;
import java.util.List;

public class ZNode<T extends Node> implements Node {

	private final T node;
	private List<? extends Node> children;
	
	protected ZNode(final T node) {
		this.node = node;
		children = node.getChildren() == null? new ArrayList<Node>() : node.getChildren();
	}
	
	protected ZNode(final T node, List<? extends Node> children) {
		this.node = node;
		this.children = children;
	}

	@Override
	public List<? extends Node> getChildren() {
		return children;
	}

	public T getSource() {
		return node;
	}
}
