package Propp;

import aterm.*;
import aterm.pure.*;
import java.util.*;
import jtom.runtime.*;
import Propp.seq.*;
import Propp.seq.types.*;
import java.io.*;
import antlr.CommonAST;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPropp extends TestCase {
  private Propp1 test;
  private Factory factory;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestPropp.class));
	}

  public void setUp() {
    factory = new Factory(new PureFactory());
    test = new Propp1(factory);
  }

  %include { seq.tom }

  public Factory getSeqFactory() {
    return factory;
  }

  public void testMakeQuery() {
		assertEquals("makeQuery should builds the corresponding sequent tree",
								 `seq(concPred(impl(A,wedge(B,C))),concPred(vee(C,D))),
								 test.makeQuery("seq([impl(A,wedge(B,C))],[vee(C,D)])"));
  }

	public void testComparePair1() {
		assertEquals("pair(3,\"something\") is greater than pair(2,\"anything\")",
								 1,
								 test.comparePair(`pair(3,"something"),`pair(2,"anything")));
	}

	public void testComparePair2() {
		assertEquals("pair(4,\"something\") is greater than pair(8,\"anything\")",
								 -1,
								 test.comparePair(`pair(4,"something"),`pair(8,"anything")));
	}

}
