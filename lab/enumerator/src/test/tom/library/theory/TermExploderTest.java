package test.tom.library.theory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import tom.library.theory.shrink.ShrinkException;
import tom.library.theory.shrink.TermExploder;
import examples.data.mutual.recursive.types.D;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

public class TermExploderTest {
	@Test
	public void testExplodeTermToSmallerTerms() throws ShrinkException {
		D term = D.fromString("fd1(fd1(d(),c1()),fc1(c1(),d()))");
		List<Object> terms = TermExploder.build(term).explodeTermToSmallerTerms();
		Object ex1 = D.fromString("fd1(d(),fc1(c1(),d()))");
		Object ex2 = D.fromString("fd1(fd1(d(),c1()),c1())");
		assertTrue(terms.size() == 2);
		assertThat(terms.get(0), is(ex1));
		assertThat(terms.get(1), is(ex2));
	}

	@Ignore
	@Test
	public void testExplodeTermToSmallerTerms2() throws ShrinkException {
		AList con = AList.fromString("con(cs(0),con(cs(1),con(cs(1),con(cs(3),con(cs(-2),empty())))))");
		List<Object> terms = TermExploder.build(con).explodeTermToSmallerTerms();
		for (Object aList : terms) {
			System.out.println("alist: " + aList);
		}
	}
	
	@Test
	public void testExplodeTermToSmallerTerms3() throws ShrinkException {
		Object con = Elem.fromString("cs(0)");
		List<Object> terms = TermExploder.build(con).explodeTermToSmallerTerms();
		assertTrue(terms.size() == 1);
		assertThat(terms.get(0), is(con));
	}
}
