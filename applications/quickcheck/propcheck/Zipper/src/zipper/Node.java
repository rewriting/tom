package zipper;

import java.util.List;

public interface Node {
	public List<? extends Node> getChildren();
}
