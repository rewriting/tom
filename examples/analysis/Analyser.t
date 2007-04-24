/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the INRIA nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

import analysis.node.*;
import analysis.node.types.*;
import analysis.ast.*;
import analysis.ast.types.*;



import tom.library.sl.*;

import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;

import org._3pq.jgrapht.edge.*;

public class Analyser{

  %include {sl.tom }
  %include {node/Node.tom}

  //Definition des types

  %typeterm ControlFlowGraphList {
    implement { List }
    is_sort(t) { t instanceof List } 
    equals(t1,t2) { t1.equals(t2) } 
    visitor_fwd { CFGVisitor }
  }

  %typeterm VariableList {
    implement { List }
    is_sort(t) { t instanceof List } 
    equals(t1,t2) { t1.equals(t2) } 
    visitor_fwd { analysis.node.NodeBasicStrategy }
  }

  %typeterm ControlFlowGraph {
    implement {ControlFlowGraph}
    is_sort(t) { t instanceof ControlFlowGraph } 
    equals(t1,t2) { t1.equals(t2) } 
    visitor_fwd { analysis.ControlFlowGraphBasicStrategy }
  }


  %typeterm Vertex {
    implement {Vertex}
    is_sort(t) { t instanceof Vertex } 
    equals(t1,t2) { t1.equals(t2) } 
    visitor_fwd { analysis.ast.AstBasicStrategy }
  }
  //Definition des constructeurs

  %op ControlFlowGraph conc(root:ControlFlowGraph,subterm:ControlFlowGraphList) {
    is_fsym(t)  { t instanceof ControlFlowGraph }
    make(root,subterm) { new ControlFlowGraph(root, subterm) }
  }


  %op ControlFlowGraph graph(node:Vertex){
    is_fsym(t)  { t instanceof ControlFlowGraph }
    get_slot(node,t) { t.getRoot() }
    make(node) { new ControlFlowGraph(node) }
  }


  %oparray ControlFlowGraphList list( ControlFlowGraph* ) {
    is_fsym(t)       { t instanceof List }
    make_empty(n)    { new ArrayList(n)       }
    make_append(e,l) { myAdd(e,(ArrayList)l)  }
    get_element(l,n) { (ControlFlowGraph)(l.get(n))  }
    get_size(l)      { l.size()               }
  }

  %oparray VariableList varList( Variable* ) {
    is_fsym(t)       { t instanceof List }
    make_empty(n)    { new ArrayList(n)       }
    make_append(e,l) { myAdd(e,(ArrayList)l)  }
    get_element(l,n) { (Variable)(l.get(n))  }
    get_size(l)      { l.size()               }
  }



  %op Vertex Vertex(node:Node){
    is_fsym(t)  { t instanceof Vertex }
    make(node) { new Vertex(node) }
  }


 // Methode d'ajout d'un element dans une liste (utilisee dans la definition des constructeurs de liste)

  private static List myAdd(Object e,List l) {
    l.add(e);
    return l;
  }

  /**
    Definition des predicats 	 	 
   */


  // Predicat NotUsed(v:Variable) qui teste si une variable n'est pas utilisee au noeud racine d'un cfg 
  %op Strategy NotUsed(v:Variable) {
    make(v) {new ControlFlowGraphBasicStrategy(`TopDown(InnerNotUsed(v)))}
  }


  %strategy InnerNotUsed(v:Variable) extends `Identity(){
    visit Term {
      t@Var(var) -> {
        if(`var.equals(v)) throw new VisitFailure();
      }
    }
  }

  // Predicat Free(v:Variable) qui teste si une variable est libere au noeud racine d'un cfg
  %strategy InnerFree(v:Variable) extends `Fail() {
    visit Node{
      n@free(var) -> {
        if(`var.equals(v)) {
          return `n;
        }
      }
    }
  }

  %op Strategy Free(v:Variable) {
    make(v) { new ControlFlowGraphBasicStrategy(`InnerFree(v))}
  }


  // Predicat Affect(v:Variable) qui teste si une variable est modifie (affected) au noeud racine d'un cfg
  %strategy InnerAffect(v:Variable) extends `Fail() {
    visit Node{
      n@affect(var,_) -> {
        if(`var.equals(v)) {
          return `n;
        }
      }
    }
  }

  %op Strategy Affect(v:Variable) {
    make(v) { new ControlFlowGraphBasicStrategy(`InnerAffect(v))}
  }

  //Construction du CFG à partir de l'Ast
  public  ControlFlowGraph constructCFG(AstAbstractType arg) throws Exception{ 
    %match(Ast arg) {
      If(cond,succesInst,failureInst) -> { 
        ControlFlowGraph suc = constructCFG(`succesInst);
        ControlFlowGraph fail= constructCFG(`failureInst);
        Vertex end = new Vertex(`endIf());
        Vertex beginIfNode = new Vertex(`beginIf(cond));
        ControlFlowGraph succesGraph  = `conc(suc,list(graph(end)));
        ControlFlowGraph failureGraph = `conc(fail,list(graph(end)));
        return `conc(graph(beginIfNode),list(succesGraph,failureGraph));
      } 


      WhileDo(cond,doInst) -> {
        ControlFlowGraph instrGraph = constructCFG(`doInst);
        Vertex beginWhileNode = new Vertex(`beginWhile(cond));
        Vertex endWhileNode = new Vertex(`endWhile());
        Vertex failWhileNode = new Vertex(`failWhile());
        ControlFlowGraph cfg = `conc(graph(beginWhileNode),list(conc(instrGraph,list(conc(graph(endWhileNode),list()))),graph(failWhileNode)));
        cfg.addEdge(new DirectedEdge(endWhileNode,beginWhileNode));
        return cfg;
      }

      Let(var,term,instr) -> {
        ControlFlowGraph instrGraph = constructCFG(`instr);
        Vertex n1 = new Vertex(`affect(var,term));
        Vertex n2 = new Vertex(`free(var));
        return `conc(graph(n1),list(conc(instrGraph,list(graph(n2)))));
      }

      LetRef(var,term,instr) -> {
        ControlFlowGraph instrGraph = constructCFG(`instr);
        Vertex n1 = new Vertex(`affect(var,term));
        Vertex n2 = new Vertex(`free(var));
        return `conc(graph(n1),list(conc(instrGraph,list(graph(n2)))));
      }

      LetAssign(var,term) -> {	
        Vertex n = new Vertex(`affect(var,term));
        return `graph(n);
      }

      Nop() -> {Vertex n = new Vertex(`nil()); return `graph(n);
      }

      concAst(instr,tail*) -> {
        ControlFlowGraph instrGraph = constructCFG(`instr);
        ControlFlowGraph tailGraph = constructCFG(`tail);
        return `conc(instrGraph,list(tailGraph));}

        concAst() -> {Vertex n = new Vertex(`nil()); return `graph(n);}
    }

    throw new Exception("Error during Cfg construction");
  }




  public List notUsedAffectations(ControlFlowGraph cfg){
    List l = new ArrayList();
    try{
      Iterator iter = cfg.vertexSet().iterator();
      while(iter.hasNext()){
        Vertex n = (Vertex) (iter.next());
        Node nn = n.getNode();
        %match(Node nn){
          affect(var,term) -> {
            //test de la cond temporel A(notUsed(var)Ufree(var)) au noeud nn du cfg
            //s1 = notUsed(var)
            Strategy s1 = `NotUsed(var);
            //s2 = free(var)
            Strategy s2 = `OrCtl(Free(var),Affect(var));
            Strategy notUsedCond = `AX(AU(s1,s2));
            if(cfg.verify(notUsedCond,n)) {
              System.out.println("Variable "+`var+" with the value "+`term+" is not used");
              l.add(n);
            }
          }
        }	
      }

    }catch(Exception e){
      System.out.println(e);
    }
    return l ;
  }

  public List onceUsedAffectations(ControlFlowGraph cfg){
    List l = new ArrayList();
    try{
      Iterator iter = cfg.vertexSet().iterator();
      while(iter.hasNext()){
        Vertex n = (Vertex) (iter.next());
        Node nn = n.getNode();
        %match(Node nn){
          affect(var,term) -> {
            //teste si une variable n'est utilise qu'une seule fois
            //AX(A(not(modified(var)U(used(var) and AX(notUsedCond(var)))


            //s1 = not(modified(var)
            Strategy s1 = `Not(Affect(var));

            //s2 = (used(var) and AX(notUsedCond(var)
            Strategy s2 =  
              `Sequence(
                  Not(NotUsed(var)),
                  All(mu(MuVar("x"),
                      Choice(
                        Free(var),
                        Sequence(NotUsed(var),All(MuVar("x")))
                        )
                      )
                    )
                  );
            //onceUsedCond AX(A(s1 U s2))  
            Strategy onceUsedCond = `AX(AU(s1,s2));

            //  `All(mu(MuVar("x"),Choice(s2,Sequence(s1,Sequence(All(MuVar("x")),One(Identity))))));

            if(cfg.verify(onceUsedCond,n)){
              System.out.println("Variable "+`var+" with the value "+`term+" is used once");
              l.add(n);
            }
          }
        }	
      }

    }catch(Exception e){
      System.out.println(e);
    }
    return l;
  }

}
