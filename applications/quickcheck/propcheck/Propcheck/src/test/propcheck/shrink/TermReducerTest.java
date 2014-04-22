package test.propcheck.shrink;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import propcheck.shrink.ShrinkException;
import propcheck.shrink.TNode;
import propcheck.shrink.TermReducer;
import propcheck.shrink.TreeTools;
import tom.library.sl.Visitable;
import zipper.Zipper;
import zipper.ZipperException;
import examples.data.types.types.D;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.alist.con;

public class TermReducerTest {
	D term;
	TermReducer<D> classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		term = D.fromString("fd1(fd1(d(),c1()),fc1(c1(),d()))");
		classUnderTest = new TermReducer<D>();
	}

	@Test
	public void testExplode() throws ZipperException, ShrinkException {
		List<D> terms = (List<D>) classUnderTest.explode(term);
		D ex1 = D.fromString("fd1(d(),fc1(c1(),d()))");
		D ex2 = D.fromString("fd1(fd1(d(),c1()),c1())");
		assertTrue(terms.size() == 2);
		assertThat(terms.get(0), is(ex1));
		assertThat(terms.get(1), is(ex2));
	}
	
	@Test
	public void testExplodeCon() throws ZipperException, ShrinkException {
		AList con = AList.fromString("con(cs(-1),con(cs(-32),con(cs(13),con(cs(-4),con(cs(1),con(cs(-4),con(cs(12),empty())))))))");
		TermReducer<AList> classUnderTest = new TermReducer<AList>();
		List<AList> terms = (List<AList>) classUnderTest.explode(con);
		for (AList aList : terms) {
			System.out.println("alist: " + aList);
		}
	}

	@Test
	public void testGetConstants() throws ShrinkException {
		D term = D.fromString("fd1(d(), c1())");
		D expected = D.fromString("d()");
		List<D> c = (List<D>) classUnderTest.getConstants(term);
		assertThat(c.get(0), is(expected));
		assertTrue(c.size() == 1);
	}

	@Test
	public void testBuildTerm() throws ZipperException, ShrinkException {
		D t = D.fromString("fd1(fd1(fd1(d(),c1()), fc1(c1(), d())),fc1(c1(),d()))");
		Visitable expected = D.fromString("fd1(fd1(d(),fc1(c1(),d())),fc1(c1(),d()))");
		Zipper<TNode> zip = Zipper.zip(TreeTools.buildTree(t));
		zip = zip.down();
		zip = zip.down();
		List<D> c = (List<D>) classUnderTest.getConstants(term);
		List<Visitable> result = classUnderTest.buildTerm(zip, c);
		
		assertTrue(result.size() > 0);
		assertThat(result.get(0), is(expected));
	}

	@Test
	public void testReduce() throws ShrinkException {
		D expected1 = D.fromString("d()");
		D expected2 = D.fromString("fd1(d(),c1())");
		List<D> results = (List<D>) classUnderTest.reduce(term);
		for (D d : results) {
			System.out.println(d);
		}
		assertThat(results.get(0), is(expected1));
		assertThat(results.get(1), is(expected2));
	}
}
