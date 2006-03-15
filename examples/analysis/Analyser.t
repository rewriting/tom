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


//Définition des types
 
  %typeterm GraphList {
    implement { List }
    equals(t1,t2) { t1.equals(t2) } 
  }

 %typeterm VariableList {
    implement { List }
    equals(t1,t2) { t1.equals(t2) } 
  }

  %typeterm Graph {
    implement {ControlFlowGraph}
    equals(t1,t2) { t1.equals(t2) } 
  }


  %typeterm NodeEmilie {
    implement {NodeEmilie}
    equals(t1,t2) { t1.equals(t2) } 
  }

//Définition des constructeurs

  %op Graph conc(root:Graph,subterm:GraphList) {
    is_fsym(t)  { t instanceof ControlFlowGraph }
    make(root,subterm) { new ControlFlowGraph(root, subterm) }
  }


  %op Graph graph(node:NodeEmilie){
    is_fsym(t)  { t instanceof ControlFlowGraph }
    get_slot(node,t) { t.getRoot() }
    make(node) { new ControlFlowGraph(node) }
  }

 
  %oparray GraphList list( Graph* ) {
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



  %op NodeEmilie NodeEmilie(node:Node){
	  is_fsym(t)  { t instanceof NodeEmilie }
    	  make(node) { new NodeEmilie(node) }
  }


/**
Classe provisoire d'encapsulation des Node
pour éviter le partage maximale des ATerms
*/

public class NodeEmilie{ 
  private Node node ;
  
  public NodeEmilie(Node node){this.node=node;}

  public String toString(){return node.toString();}

  public Node getNode(){return node;}
}

/**
Repérsentation du graphe de flot de contrôle
en utilisant la bibliothèque jgrapht
*/

class ControlFlowGraph extends DefaultDirectedGraph implements Visitable {

  private NodeEmilie root;
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
      NodeEmilie vertex = (NodeEmilie) (iter.next());
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
      NodeEmilie rootNeighbour = (NodeEmilie)(((Edge)iter.next()).getTarget());
      subterms.add(subGraph(rootNeighbour));
    }
  }


  public ControlFlowGraph subGraph(NodeEmilie startNode){
       ControlFlowGraph cfg = new ControlFlowGraph(startNode);
       cfg.addAllVertices(connectedNodes(startNode));
       cfg.addAllEdges(connectedEdges(startNode));
       return cfg;

  }

  public List connectedNodes(NodeEmilie startNode){
    Iterator iter = outgoingEdgesOf(startNode).iterator();
    List connectedNodes = new ArrayList();
    while(iter.hasNext()){
      NodeEmilie rootNeighbour = (NodeEmilie)(((Edge)iter.next()).getTarget());
     if(! connectedNodes.contains(rootNeighbour)){
	connectedNodes.add(rootNeighbour);
      	connectedNodes.addAll(connectedNodes(rootNeighbour));
    	}
    }
    return connectedNodes;
  }

  public List connectedEdges(NodeEmilie startNode){
    List connectedEdges = new ArrayList();
    List connectedNodes = connectedNodes(startNode);
    ListIterator iter = connectedNodes.listIterator();
    while(iter.hasNext()){
	NodeEmilie n = (NodeEmilie) iter.next();
        connectedEdges.addAll(outgoingEdgesOf(n));
   }
   return connectedEdges;
  }

  public ControlFlowGraph(NodeEmilie node){
	super();
	root = node;
	addVertex(node);
	subterms = new ArrayList();
  }

 
  public NodeEmilie getRoot(){
     return root;
  }  

  public int getChildCount() {
    return subterms.size();
  }

  public Visitable getChildAt(int i) {
   return (ControlFlowGraph) subterms.get(i);
  }

  public Visitable setChildAt(int i, Visitable child) {
    subterms.set(i,child);  
    return this;  
  }

}


// Main de la classe

  public final static void main(String[] args) {
   Analyser test = new Analyser();
    test.run();
  }

// Méthode de test des stratégies

  public void run() {
    InstructionList subject = `concInstruction(If(True,Nop(),Let(Name("y"),g(a),Nop())),Let(Name("x"),f(a,b),Nop()));
    VisitableVisitor rule = new Cfg();
    VisitableVisitor deadcode = new DeadCode(); 
    try {
      System.out.println("subject          = " + subject);
      System.out.println("correpsonding cfg = " + rule.visit(subject));
      ControlFlowGraph cfg = (ControlFlowGraph)(rule.visit(subject));
      System.out.println("deadcode = " + MuTraveler.init(`BottomUp(deadcode)).visit(cfg));
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }

  }
  
// Méthode d'ajout d'un élément dans une liste (utilisée dans la définition des constructeurs de liste)

  private List myAdd(Object e,List l) {
    l.add(e);
    return l;
  }

/**
 Définition des prédicats sur un node
---------------------------------------------------------
*/

// Prédicat isUsed(v:Variable) qui teste si une variable est utilisé à un noeud du cfg

class IsUsed extends languageBasicStrategy {

    Variable v;

    public IsUsed(Variable v) {
         super(`Fail());
	 this.v=v;
    }
    
    public Visitable visitNode(Node arg) throws VisitFailure { 
      %match(Node arg) {
/** 
	beginIf(expr:Expression) -> Node
	affect(var:Variable,term:Term) -> Node
	free(var:Variable) -> Node
	hostcode(list:VariableList) -> Node
	beginWhile(expr:Expression) -> Node
	endWhile() -> Node
	failWhile() -> Node
	Nil()-> Node	
*/
      } 
      return `Fail().visit(arg);
      //throw new VisitFailure();
    }


   public Visitable visitTerm(Term arg) throws VisitFailure { 
      %match(Term arg) {
 	

	} 
      return `Fail().visit(arg);
      //throw new VisitFailure();
   }

}
  
class DeadCode extends languageBasicStrategy{
    public DeadCode() {
      super(`Identity());
    }
    
    public Visitable visit(Visitable arg) throws VisitFailure { 
      %match(Graph arg) {
 	graph(node) -> {
	  Node n = `node.getNode();
	} 
      }
      return `Identity().visit(arg);
      //throw new VisitFailure();
    }
}


//Construction du CFG à partir de l'AST

class Cfg extends languageBasicStrategy{
    public Cfg() {
      super(`Fail());
    }
    
    public  Visitable visit(Visitable arg) throws VisitFailure { 
      %match(Instruction arg) {
	If(cond,succesInst,failureInst) -> { 
		ControlFlowGraph suc = (ControlFlowGraph)(visit(`succesInst));
		ControlFlowGraph fail= (ControlFlowGraph)(visit(`failureInst));
		NodeEmilie end = new NodeEmilie(`endIf());
		NodeEmilie beginIfNode = new NodeEmilie(`beginIf(cond));
		ControlFlowGraph succesGraph  = `conc(suc,list(graph(end)));
		ControlFlowGraph failureGraph = `conc(fail,list(graph(end)));
		return `conc(graph(beginIfNode),list(succesGraph,failureGraph));} 

	
	WhileDo(cond,doInst) -> {
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`doInst));
		NodeEmilie beginWhileNode = new NodeEmilie(`beginWhile(cond));
		NodeEmilie endWhileNode = new NodeEmilie(`endWhile());
		NodeEmilie failWhileNode = new NodeEmilie(`failWhile());
		ControlFlowGraph cfg = `conc(graph(beginWhileNode),list(conc(instrGraph,list(conc(graph(endWhileNode),list()))),graph(failWhileNode)));
		cfg.addEdge(new DirectedEdge(endWhileNode,beginWhileNode));
		return cfg;}

	Let(var,term,instr) -> {
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`instr));
		NodeEmilie n1 = new NodeEmilie(`affect(var,term));
		NodeEmilie n2 = new NodeEmilie(`free(var));
		return `conc(graph(n1),list(conc(instrGraph,list(graph(n2)))));}

	LetRef(var,term,instr) -> {
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`instr));
		NodeEmilie n = new NodeEmilie(`affect(var,term));
		return `conc(graph(n),list(instrGraph));}

	LetAssign(var,term,instr) -> {	
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`instr));
		NodeEmilie n = new NodeEmilie(`affect(var,term));
		return `conc(graph(n),list(instrGraph));}

	Nop() -> {NodeEmilie n = new NodeEmilie(`Nil()); return `graph(n);}

	}

      %match(InstructionList arg){
	concInstruction(instr,tail*) -> {
		ControlFlowGraph instrGraph = (ControlFlowGraph)(visit(`instr));
		ControlFlowGraph tailGraph = (ControlFlowGraph)(visit(`tail));
		return `conc(instrGraph,list(tailGraph));}
        
	concInstruction() -> {NodeEmilie n = new NodeEmilie(`Nil()); return `graph(n);}
	}

      return (Instruction)`Fail().visit(arg);
      //throw new VisitFailure();
    }

}


}
