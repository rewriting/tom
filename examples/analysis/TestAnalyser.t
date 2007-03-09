/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *      - Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.  
 *      - Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *      - Neither the name of the INRIA nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
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

package analysis;

import java.util.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import analysis.node.types.*;
import analysis.node.*;
import analysis.ast.*;
import analysis.ast.types.*;
import tom.library.sl.*;

public class TestAnalyser extends TestCase {

  %include {node/Node.tom}
  %include {sl.tom}

  private Analyser analyser;
  private Ast subject;
  private ControlFlowGraph correspondingCfg;
  private Variable var_x = `Name("x");
  private Variable var_y = `Name("y");
  private Variable var_z = `Name("z");

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestAnalyser.class));
  }

  public void setUp() {
    analyser = new Analyser();
    subject = `concAst(
        If(True(),
          concAst(),
          concAst(
            LetRef(var_x,g(a()),concAst(
                LetAssign(var_x,g(b())),
                LetAssign(var_x,f(a(),b())),
                Let(var_y,g(Var(var_x)),concAst())
                )
              )
            )
          ),
        Let(var_z,f(a(),b()),concAst())
        );

    System.out.println("subject          = " + subject);

    try{
      correspondingCfg = analyser.constructCFG(subject);
      System.out.println("correpsonding cfg = " + correspondingCfg);
    }catch(Exception e){
      System.out.println(e);
    }
  }

  public void testNotUsed() {
    List notUsedAffectations = analyser.notUsedAffectations(correspondingCfg);
    List attemptedResult = new ArrayList();
    Iterator iter = correspondingCfg.vertexSet().iterator();
    while(iter.hasNext()){
      Vertex n = (Vertex) (iter.next());
      Node nn = n.getNode();
      %match(Node nn){
        affect(Name("x"),g(a())) -> {attemptedResult.add(n);}
        affect(Name("x"),g(b())) -> {attemptedResult.add(n);}
        affect(Name("y"),g(Var(Name("x")))) -> {attemptedResult.add(n);}
        affect(Name("z"),f(a(),b())) -> {attemptedResult.add(n);}
      }
    }
    System.out.println("notUsedAffectations = " + notUsedAffectations);
    System.out.println("attemptedResult     = " + attemptedResult);
    assertEquals(notUsedAffectations,attemptedResult);
  }

  public void testOnceUsed() {
    List onceUsedAffectations = analyser.onceUsedAffectations(correspondingCfg);
    List attemptedResult = new ArrayList();
    Iterator iter = correspondingCfg.vertexSet().iterator();
    while(iter.hasNext()){
      Vertex n = (Vertex) (iter.next());
      Node nn = n.getNode();
      %match(Node nn){
        affect(Name("x"),f(a(),b())) -> {attemptedResult.add(n);}
      }
    }
    System.out.println("onceUsedAffectations = " + onceUsedAffectations);
    System.out.println("attemptedResult      = " + attemptedResult);
    assertEquals(onceUsedAffectations,attemptedResult);
  }

}
