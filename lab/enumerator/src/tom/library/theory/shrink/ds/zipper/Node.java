package tom.library.theory.shrink.ds.zipper;

import java.util.List;

public interface Node {
	public List<? extends Node> getChildren();
}
