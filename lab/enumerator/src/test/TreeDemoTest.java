package test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.adt.tree.TreeDemo;
import examples.adt.tree.TreeDemo.EmptyQueueException;
import examples.adt.tree.tree.types.Node;
import examples.adt.tree.tree.types.Tree;

@RunWith(PropCheck.class)
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
			@ForSome(minSampleSize=49, maxSampleSize = 50) Tree left,
			@ForSome(minSampleSize=49, maxSampleSize = 50) Tree right) throws EmptyQueueException {
		Tree result = TreeDemo.createTree(node, left, right);
		Tree l = TreeDemo.detachLeftSubtree(result);
		assertThat(l, equalTo(left));
	}
	
	@Theory
	public void testDetachRightSubtreeFromCreateTree(
			@ForSome(minSampleSize=20, maxSampleSize = 30) Node node,
			@ForSome(minSampleSize=49, maxSampleSize = 50) Tree left,
			@ForSome(minSampleSize=49, maxSampleSize = 50) Tree right) throws EmptyQueueException {
		Tree result = TreeDemo.createTree(node, left, right);
		Tree r = TreeDemo.detachRightSubtree(result);
		assertThat(r, equalTo(right));
	}
	
	@Theory public void testATerm(
			@ForSome(maxSampleSize = 1000, numberOfSamples=2000) Tree t) {
		assertThat(Tree.fromTerm(t.toATerm()), is(t));
	}
}
