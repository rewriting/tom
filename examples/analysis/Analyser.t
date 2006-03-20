/*
 * Copyright (c) 2004-2006, INRIA
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

import analysis.ast.*;
import analysis.ast.types.*;
import analysis.node.*;
import analysis.node.types.*;


import tom.library.strategy.mutraveler.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;

import org._3pq.jgrapht.edge.*;

public class Analyser{

	%include {mutraveler.tom }
	%include {node/Node.tom}


	//Définition des types

	%typeterm ControlFlowGraphList {
		implement { List }
		equals(t1,t2) { t1.equals(t2) } 
		visitor_fwd { CFGVisitor }
	}

	%typeterm VariableList {
		implement { List }
		equals(t1,t2) { t1.equals(t2) } 
		visitor_fwd { analysis.node.NodeBasicStrategy }
	}

	%typeterm ControlFlowGraph {
		implement {ControlFlowGraph}
		equals(t1,t2) { t1.equals(t2) } 
		visitor_fwd { analysis.ControlFlowGraphBasicStrategy }
	}


	%typeterm Vertex {
		implement {Vertex}
		equals(t1,t2) { t1.equals(t2) } 
		visitor_fwd { analysis.ast.AstBasicStrategy }
	}


  %op VisitableVisitor ControlFlowGraphBasicStrategy(v:VisitableVisitor){
    make(v){new ControlFlowGraphBasicStrategy(v)}
  }

	//Définition des constructeurs

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

	public final static void main(String[] args) {
		Analyser test = new Analyser();
		test.run();
	}

	// Méthode de test des stratégies

	public void run() {
		Variable var_x = `Name("x");
		Variable var_y = `Name("y");
		Variable var_z = `Name("z");

		InstructionList subject = `concInstruction(If(True,Nop(),Let(var_x,g(a),Let(var_y,g(Var(var_x)),Nop()))),Let(var_z,f(a,b),Nop()));

		System.out.println("subject          = " + subject);
		try{
			ControlFlowGraph cfg = constructCFG(subject);
			System.out.println("correpsonding cfg = " + cfg);
			System.out.println("deadcode detection...........");

			// On recherche les noeuds qui correspondent a des assign
			Iterator iter = cfg.vertexSet().iterator();
			while(iter.hasNext()){
				Vertex n = (Vertex) (iter.next());
				Node nn = n.getNode();
				%match(Node nn){
					affect(var,_) -> {
						VisitableVisitor notUsedStrat = `NotUsed(var);
						VisitableVisitor freeStrat = new Free(`var);
						//test de la cond temporel A(notUsed(var)Ufree(var)) au noeud nn du cfg
						if(cfg.verify(`mu(MuVar("x"),Choice(freeStrat,Sequence(notUsedStrat,All(MuVar("x"))))),n)) System.out.println("Variable "+`var+" not used");
					}
				}	
			}	

		}catch(Exception e){
			System.out.println(e);
		}

	}

	// Méthode d'ajout d'un élément dans une liste (utilisée dans la définition des constructeurs de liste)

	private List myAdd(Object e,List l) {
		l.add(e);
		return l;
	}

	/**
	  Définition des prédicats 	 	 
	*/


	// Prédicat Used(v:Variable) qui teste si une variable est utilisée dans un terme
  
	%strategy InnerNotUsed(v:Variable) extends `Identity(){
   visit Node {
      n -> {
        return (Node) MuTraveler.init(`TopDown(this)).visit(`n);
      }
   }
   visit Term {
			t@Var(var) -> {
        if(`var.equals(v)) return (Term) MuTraveler.init(`Fail()).visit(`t);
			}
		}
  }
  %op VisitableVisitor NotUsed(v:Variable) {
    make(v) { new ControlFlowGraphBasicStrategy(new InnerNotUsed(v))}
  }


	//remarque : on est obligé d'avoir deux stratégies car les types gom et ceux écrits à la main n'ont pas le même VisitorFwd

	// Prédicat Free(v:Variable) qui teste si une variable est libéré au noeud racine d'un cfg

	%strategy Free(v:Variable) extends `Fail() {
		visit ControlFlowGraph {
			cfg -> {
				Node node = `cfg.getRoot().getNode();
				%match(Node node) {
					free(var) -> {
						if(`var.equals(v)) {
							return `cfg;
						}
					}	
				}
			}
		}

	}

	//Construction du CFG à partir de l'Ast
	public  ControlFlowGraph constructCFG(AstAbstractType arg) throws Exception{ 
		%match(Instruction arg) {
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
				Vertex n = new Vertex(`affect(var,term));
				return `conc(graph(n),list(instrGraph));
			}

			LetAssign(var,term,instr) -> {	
				ControlFlowGraph instrGraph = constructCFG(`instr);
				Vertex n = new Vertex(`affect(var,term));
				return `conc(graph(n),list(instrGraph));
			}

			Nop() -> {Vertex n = new Vertex(`nil()); return `graph(n);
			}

		}

		%match(InstructionList arg){
			concInstruction(instr,tail*) -> {
				ControlFlowGraph instrGraph = constructCFG(`instr);
				ControlFlowGraph tailGraph = constructCFG(`tail);
				return `conc(instrGraph,list(tailGraph));}

				concInstruction() -> {Vertex n = new Vertex(`nil()); return `graph(n);}
		}

		throw new Exception("Error during Cfg construction");
	}


}
