package test.propcheck.shrink;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import propcheck.shrink.TNode;
import propcheck.shrink.TreeTools;
import tom.library.sl.Visitable;
import examples.data.types.types.D;

public class TreeBuilderTest {

	D term1;

	@Before
	public void setUp() throws Exception {
		term1 = D.fromString("fd1(fd1(d(),c1()),fc1(c1(),d()))");
	}

	@Test
	public void testBuild() {
		TNode tree = TreeTools.buildTree(term1);
		assertTrue(tree != null);
		assertThat(tree.getTerm(), is((Visitable)term1));
		// t|1
		Visitable t1 = term1.getChildAt(0);
		Visitable expected = ((TNode) tree.getChildren().get(0)).getTerm();
		assertThat(expected, is(t1));

		// t|11
		Visitable t11 = term1.getChildAt(0).getChildAt(0);
		expected = ((TNode) tree.getChildren().get(0).getChildren().get(0)).getTerm();
		assertThat(expected, is(t11));

		// t|11
		Visitable t12 = term1.getChildAt(0).getChildAt(1);
		expected = ((TNode) tree.getChildren().get(0).getChildren().get(1)).getTerm();
		assertThat(expected, is(t12));

		// t|2
		Visitable t2 = term1.getChildAt(1);
		expected = ((TNode) tree.getChildren().get(1)).getTerm();
		assertThat(expected, is(t2));

		// t|11
		Visitable t21 = term1.getChildAt(1).getChildAt(0);
		expected = ((TNode) tree.getChildren().get(1).getChildren().get(0)).getTerm();
		assertThat(expected, is(t21));

		// t|11
		Visitable t22 = term1.getChildAt(1).getChildAt(1);
		expected = ((TNode) tree.getChildren().get(1).getChildren().get(1)).getTerm();
		assertThat(expected, is(t22));
	}
	
	@Test
	public void testSize() {
		int size = TreeTools.size(term1);
		assertThat(size, is(7));
	}
}
