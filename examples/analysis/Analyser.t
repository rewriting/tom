/*
 * Copyright (c) 2004-2006, INRIA
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

package analysis;

import analysis.language.*;
import analysis.language.types.*;

import tom.library.strategy.mutraveler.*;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;


import org._3pq.jgrapht.*;
import org._3pq.jgrapht.edge.*;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;

public class Analyser{

  %include {mutraveler.tom }
  %include {language/language.tom}
  
  %typeterm GraphList {
    implement { List }
    equals(t1,t2) { t1.equals(t2) } 
  }

  %typeterm Graph {
    implement {ControlFlowGraph}
    equals(t1,t2) { t1.equals(t2) } 
  }

  %op Graph conc(root:Graph,subterm:GraphList) {
    is_fsym(t)  { t instanceof ControlFlowGraph }
    make(root,subterm) { new ControlFlowGraph(root, subterm) }
  }


  %op Graph graph(node:Node){
    is_fsym(t)  { t instanceof ControlFlowGraph }
    make(node) { new ControlFlowGraph(node) }
  }

 
  %oparray GraphList list( Graph* ) {
    is_fsym(t)       { t instanceof List }
    make_empty(n)    { new ArrayList(n)       }
    make_append(e,l) { myAdd(e,(ArrayList)l)  }
    get_element(l,n) { (ControlFlowGraph)(l.get(n))  }
    get_size(l)      { l.size()               }
  }

  private List myAdd(Object e,List l) {
    l.add(e);
    return l;
  }


class ControlFlowGraph extends DefaultDirectedGraph implements Visitable {

  private Node root;
  private List subterms;

  public ControlFlowGraph(ControlFlowGraph first, List graphList)  {
    super();
    root = first.getRoot();
    addAllVertices(first.vertexSet());
    addAllEdges(first.edgeSet()); 
 
    // il faut ajouter les arretes qui relient first aux root des cfg suivants de subterm
    // Je cherche les feuilles du graphe first
    Iterator iter = first.vertexSet().iterator();
    ArrayList leaves = new ArrayList();
    while(iter.hasNext()){
      Node vertex = (Node) (iter.next());
      if (first.outDegreeOf(vertex)==0) leaves.add(vertex) ;
    }
   iter = graphList.iterator();
    while(iter.hasNext()){
        ControlFlowGraph next =  ((ControlFlowGraph)iter.next());
	addAllVertices(next.vertexSet());
        addAllEdges(next.edgeSet());
	Iterator iter2 = leaves.iterator();
        while(iter2.hasNext()){
            DirectedEdge e = new DirectedEdge(iter2.next(),next.getRoot());
	    addEdge(e);
        }
    }
    iter = outgoingEdgesOf(root).iterator();
    subterms = new ArrayList();
    while(iter.hasNext()){
      subterms.add(((Edge)iter.next()).getTarget()) ;
    }
  }

  public ControlFlowGraph(Node node){
	super();
	root = node;
	addVertex(node);
  }

 
  public Node getRoot(){
     return root;
  }  

  public int getChildCount() {
    return subterms.size();
  }

  public Visitable getChildAt(int i) {
   return (Node) subterms.get(i);
  }

  public Visitable setChildAt(int i, Visitable child) {
    subterms.set(i,child);  
    return this;  
  }

}

  public final static void main(String[] args) {
   Analyser test = new Analyser();
    test.run();
  }

  public void run() {
    InstructionList subject = `concInstruction(If(True,Nop(),Let(Name("y"),g(a),Nop())),Let(Name("x"),f(a,b),Nop()));
    VisitableVisitor rule = new Cfg();

    try {
      System.out.println("subject          = " + subject);
      System.out.println("correpsonding cfg = " + rule.visit(subject));
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }

  }
  
class DeadCode extends languageVisitableFwd {
    public DeadCode() {
      super(`Fail());
    }
    
    public Visitable visit(Visitable arg) throws VisitFailure { 
      %match(Graph arg) {
      }
      return (Term)`Fail().visit(arg);
      //throw new VisitFailure();
    }
  }

class Cfg extends languageVisitableFwd{
    public Cfg() {
      super(`Fail());
    }
    
    public  Visitable visit(Visitable arg) throws VisitFailure { 
      %match(Instruction arg) {
	
	If(cond,succesInst,failureInst) -> { 
		ControlFlowGraph suc = (ControlFlowGraph)(visit(`succesInst));
		ControlFlowGraph fail= (ControlFlowGraph)(visit(`failureInst));
		ControlFlowGraph succesGraph  = `conc(suc,list(graph(endIf)));
		ControlFlowGraph failureGraph = `conc(fail,list(graph(endIf)));
		return `conc(graph(beginIf(cond)),list(succesGraph,failureGraph));} 

	WhileDo(cond,doInst) -> {
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`doInst));
		return `conc(graph(beginWhile(cond)),list(conc(instrGraph,list(conc(graph(endWhile()),list()))),graph(failWhile())));}

	Let(var,term,instr) -> {
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`instr));
		return `conc(graph(affect(var,term)),list(conc(instrGraph,list(graph(free(var))))));}

	LetRef(var,term,instr) -> {
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`instr));
		return `conc(graph(affect(var,term)),list(instrGraph));}

	LetAssign(var,term,instr) -> {	
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`instr));
		return `conc(graph(affect(var,term)),list(instrGraph));}

	Nop() -> {return `graph(Nil());}

	}

      %match(InstructionList arg){
	concInstruction(instr,tail*) -> {
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`instr));
		ControlFlowGraph tailGraph = (ControlFlowGraph)(visit(`tail));
		return `conc(instrGraph,list(tailGraph));}
        
	concInstruction() -> {return `graph(Nil());}
	}

      return (Instruction)`Fail().visit(arg);
      //throw new VisitFailure();
    }
  }




}

 


