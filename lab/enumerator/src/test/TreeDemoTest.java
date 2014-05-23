package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import examples.adt.TreeDemo;
import examples.adt.TreeDemo.EmptyQueueException;
import examples.adt.tree.types.Node;
import examples.adt.tree.types.Tree;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.TomCheck;
import tom.library.theory.ForSome;

@RunWith(TomCheck.class)
public class TreeDemoTest {
	
	@Theory
	public void testCreateNotEmpty(
			@ForSome(minSampleSize=20, maxSampleSize = 30) Node node,
			@ForSome(minSampleSize=20, maxSampleSize = 30) Tree left,
			@ForSome(minSampleSize=20, maxSampleSize = 30) Tree right) {
		Tree result = TreeDemo.createTree(node, left, right);
		assertThat(TreeDemo.isEmpty(result), equalTo(false));
	}
	
	@Theory
	public void testGetRootFromEmpty() {
		try {
			TreeDemo.getRootNode(Tree.fromString("empty()"));
		} catch (Exception e) {
			assertThat(e, instanceOf(TreeDemo.EmptyQueueException.class));
		}
	}
	
	@Theory
	public void testGetRootFromCreateTree(
			@ForSome(minSampleSize=20, maxSampleSize = 30) Node node,
			@ForSome(minSampleSize=20, maxSampleSize = 30) Tree left,
			@ForSome(minSampleSize=20, maxSampleSize = 30) Tree right) throws EmptyQueueException {
		Tree result = TreeDemo.createTree(node, left, right);
		Node root = TreeDemo.getRootNode(result);
		assertThat(root, equalTo(node));
	}
	
	@Theory
	public void testDetachLeftSubtreeFromCreateTree(
			@ForSome(minSampleSize=20, maxSampleSize = 30) Node node,
			@ForSome(minSampleSize=20, maxSampleSize = 30) Tree left,
			@ForSome(minSampleSize=20, maxSampleSize = 30) Tree right) throws EmptyQueueException {
		Tree result = TreeDemo.createTree(node, left, right);
		Tree l = TreeDemo.detachLeftSubtree(result);
		assertThat(l, equalTo(left));
	}
	
	@Theory
	public void testDetachRightSubtreeFromCreateTree(
			@ForSome(minSampleSize=20, maxSampleSize = 30) Node node,
			@ForSome(minSampleSize=20, maxSampleSize = 30) Tree left,
			@ForSome(minSampleSize=20, maxSampleSize = 30) Tree right) throws EmptyQueueException {
		Tree result = TreeDemo.createTree(node, left, right);
		Tree r = TreeDemo.detachRightSubtree(result);
		assertThat(r, equalTo(right));
	}
}
