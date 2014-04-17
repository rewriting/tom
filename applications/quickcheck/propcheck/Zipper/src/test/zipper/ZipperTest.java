package test.zipper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import tom.library.sl.Visitable;
import zipper.Node;
import zipper.Zipper;
import zipper.ZipperException;
import examples.data.types.types.C;
import examples.data.types.types.D;

public class ZipperTest {

	D term1;
	
	Zipper<TNode> classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		term1 = D.fromString("fd1(fd1(d(),c1()),fc1(c1(),d()))");
		TNode tree = new TNode(term1, 0);
		TNode tree0 = new TNode(term1.getChildAt(0), 0);
		TNode tree1 = new TNode(term1.getChildAt(1), 1);
		tree.addChild(tree0);
		tree.addChild(tree1);
		TNode tree01 = new TNode(term1.getChildAt(0).getChildAt(0), 0);
		TNode tree02 = new TNode(term1.getChildAt(0).getChildAt(1), 1);
		TNode tree11 = new TNode(term1.getChildAt(1).getChildAt(0), 0);
		TNode tree12 = new TNode(term1.getChildAt(1).getChildAt(1), 1);
		tree0.addChild(tree01);
		tree0.addChild(tree02);
		tree1.addChild(tree11);
		tree1.addChild(tree12);
		classUnderTest = Zipper.zip(tree);
	}

	@Test
	public void testDown() throws ZipperException {
		Visitable expected = D.fromString("fd1(d(),c1())");
		Zipper<TNode> z = classUnderTest.down();
		assertThat(z.getNode().getSource().term, CoreMatchers.is(expected));
		
		Zipper<TNode> z1 = z.down();
		expected = D.fromString("d()");
		assertThat(z1.getNode().getSource().term, CoreMatchers.is(expected));
	}

	@Test
	public void testDownInt() throws ZipperException {
		Zipper<TNode> z = classUnderTest.down(1);
		Visitable expected = C.fromString("fc1(c1(),d())");
		assertThat(z.getNode().getSource().term, CoreMatchers.is(expected));
	}

	@Test
	public void testUp() throws ZipperException {
		Zipper<TNode> z = classUnderTest.down().up();
		assertThat(z.getNode().getSource().term, CoreMatchers.is((Visitable)term1));
		
		z = classUnderTest.down().down().up();
		Visitable expected = D.fromString("fd1(d(),c1())");
		assertThat(z.getNode().getSource().term, CoreMatchers.is(expected));
	}
	
	@Test(expected=ZipperException.class)
	public void testUpException() throws ZipperException {
		classUnderTest.up();
	}

	@Test
	public void testIsTop() throws ZipperException {
		assertThat(classUnderTest.isTop(), CoreMatchers.is(true));
		assertThat(classUnderTest.isLeaf(), CoreMatchers.is(false));
		assertThat(classUnderTest.down().isTop(), CoreMatchers.is(false));
	}

	@Test
	public void testIsLeaf() throws ZipperException {
		assertThat(classUnderTest.down().down().isLeaf(), CoreMatchers.is(true));
		assertThat(classUnderTest.down().down().isTop(), CoreMatchers.is(false));
		assertThat(classUnderTest.down(1).down(1).isLeaf(), CoreMatchers.is(true));
		assertThat(classUnderTest.down(1).down().isLeaf(), CoreMatchers.is(true));
	}

	class TNode implements Node {
		private Visitable term;
		private int index;
		private List<TNode> children;
		
		public TNode(Visitable term, int index) {
			this.term = term;
			this.index = index;
			this.children = new ArrayList<TNode>();
		}
		
		public TNode(Visitable term, int index, TNode... children) {
			this.term = term;
			this.index = index;
			this.children = Arrays.asList(children);
		}
		
		public void addChild(TNode child) {
			children.add(child);
		}
		
		public void addChild(List<TNode> children) {
			this.children.addAll(children);
		}
		
		@Override
		public List<? extends Node> getChildren() {
			return children;
		}
	}
}
