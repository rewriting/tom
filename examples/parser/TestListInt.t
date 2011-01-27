/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package parser;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Test;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.tree.Tree;
import parser.listint.ListIntAdaptor;
import parser.listint.types.*;

import java.util.Collection;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class TestListInt {

  %include { listint/ListInt.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestListInt.class.getName());
  }

  private ListInt getTerm(String code) {
    try {
      ListIntLexer lexer = new ListIntLexer(new ANTLRStringStream(code));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      ListIntParser parser = new ListIntParser(tokens);
      Tree tree = (Tree) parser.listint().getTree();
      ListInt listint = (ListInt) ListIntAdaptor.getTerm(tree);
      return listint;
    } catch (Exception e) {
      fail("Parse failed, with exception "+e);
      return null;
    }
  }

  private String query;
  private ListInt list;
  public TestListInt(String query, ListInt list) {
    this.query = query;
    this.list = list;
  }

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
      { "[]", `ConcInt() },
      { "[1]", `ConcInt(1) },
      { "[1,2]", `ConcInt(1,2) },
      { "[1,2,3]", `ConcInt(1,2,3) },
      { "[1,2,3]", `ConcInt(1,2,3) },
      { "[1 , 2 , 3]", `ConcInt(1,2,3) },
      });
  }

  @Test
  public void testCompare() {
    ListInt result = getTerm(query);
    assertEquals(list, result);
  }
}
