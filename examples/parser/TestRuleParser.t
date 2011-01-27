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
import org.junit.Test;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.tree.Tree;
import parser.rule.RuleAdaptor;

import parser.rule.types.*;

public class TestRuleParser {

  %include { rule/Rule.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestRuleParser.class.getName());
  }

  private Term getTerm(String code) {
    try {
      RuleLexer lexer = new RuleLexer(new ANTLRStringStream(code));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      RuleParser parser = new RuleParser(tokens);
      Tree tree = (Tree) parser.term().getTree();
      Term term = (Term) RuleAdaptor.getTerm(tree);
      return term;
    } catch (Exception e) {
      fail("Parse failed, with exception "+e);
      return null;
    }
  }

  @Test
  public void testSimpleTerm() {
    String termCode = "a()";
    Term term = getTerm(termCode);
    assertEquals(`Appl("a",TermList()), term);
  }

  @Test
  public void testVariable() {
    String termCode = "x";
    Term term = getTerm(termCode);
    assertEquals(`Var("x"), term);
  }

  @Test
  public void testBranchTerm() {
    String termCode = "Node(x,y)";
    Term term = getTerm(termCode);
    assertEquals(`Appl("Node",TermList(Var("x"),Var("y"))), term);
  }

  @Test
  public void testIntTerm() {
    String termCode = "123";
    Term term = getTerm(termCode);
    assertEquals(`BuiltinInt(123), term);
  }

  @Test
  public void testIntApplTerm() {
    String termCode = "Int(123)";
    Term term = getTerm(termCode);
    assertEquals(`Appl("Int",TermList(BuiltinInt(123))), term);
  }

  @Test
  public void testStringTerm() {
    String termCode = "\"foo\"";
    Term term = getTerm(termCode);
    assertEquals(`BuiltinString("\"foo\""), term);
  }

  @Test
  public void testStringApplTerm() {
    String termCode = "foo(\"bar\")";
    Term term = getTerm(termCode);
    assertEquals(`Appl("foo",TermList(BuiltinString("\"bar\""))), term);
  }

  private RuleList getRuleList(String code) {
    try {
      RuleLexer lexer = new RuleLexer(new ANTLRStringStream(code));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      RuleParser parser = new RuleParser(tokens);
      Tree tree = (Tree) parser.ruleset().getTree();
      RuleList ruleList = (RuleList) RuleAdaptor.getTerm(tree);
      return ruleList;
    } catch (Exception e) {
      fail("Parse failed, with exception "+e);
      return null;
    }
  }

  @Test
  public void testSimpleRule() {
    String ruleCode = "a() -> b()\n";
    RuleList rl = getRuleList(ruleCode);
    assertEquals(
        `RuleList(Rule(Appl("a",TermList()),Appl("b",TermList()))),
        rl);
  }

  @Test
  public void testConditionalRule() {
    String ruleCode = "a(x,y) -> b() if x==y\n";
    RuleList rl = getRuleList(ruleCode);
    assertEquals(
        `RuleList(
          ConditionalRule(
            Appl("a",TermList(Var("x"),Var("y"))),
            Appl("b",TermList()),
            CondEquals(Var("x"),Var("y")))),
        rl);
  }
}
