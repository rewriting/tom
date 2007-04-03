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

import org._3pq.jgrapht.*;
import org._3pq.jgrapht.edge.*;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;

import analysis.node.*;
import analysis.node.types.*;

import java.util.*;
import tom.library.sl.*;


public class ControlFlowGraph extends DefaultDirectedGraph implements tom.library.sl.Visitable{

	private Vertex root;
	private List subterms;

	public ControlFlowGraph(Vertex node){
		super();
		root = node;
		addVertex(node);
		subterms = new ArrayList();
	}

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
			Vertex vertex = (Vertex) (iter.next());
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
			Vertex rootNeighbour = (Vertex)(((Edge)iter.next()).getTarget());
			subterms.add(subGraph(rootNeighbour));
		}
	}


	public ControlFlowGraph subGraph(Vertex startNode){
		ControlFlowGraph cfg = new ControlFlowGraph(startNode);
		cfg.addAllVertices(connectedNodes(startNode));
		cfg.addAllEdges(connectedEdges(startNode));
		Iterator iter = outgoingEdgesOf(startNode).iterator();
		List l = new ArrayList();
		while(iter.hasNext()){
			Vertex rootNeighbour = (Vertex)(((Edge)iter.next()).getTarget());
			l.add(subGraph(rootNeighbour));
		}
		cfg.setSubterms(l);
		return cfg;

	}

	public void setSubterms(List l){subterms=l;}

	public List connectedNodes(Vertex startNode){
		Iterator iter = outgoingEdgesOf(startNode).iterator();
		List connectedNodes = new ArrayList();
		while(iter.hasNext()){
			Vertex rootNeighbour = (Vertex)(((Edge)iter.next()).getTarget());
			if(! connectedNodes.contains(rootNeighbour)){
				connectedNodes.add(rootNeighbour);
				connectedNodes.addAll(connectedNodes(rootNeighbour));
			}
		}
		return connectedNodes;
	}

	public List connectedEdges(Vertex startNode){
		List connectedEdges = new ArrayList();
		List connectedNodes = connectedNodes(startNode);
		ListIterator iter = connectedNodes.listIterator();
		while(iter.hasNext()){
			Vertex n = (Vertex) iter.next();
			connectedEdges.addAll(outgoingEdgesOf(n));
		}
		return connectedEdges;
	}



	public Vertex getRoot(){
		return root;
	}  

	public int getChildCount() {
		return subterms.size();
	}

	public jjtraveler.Visitable getChildAt(int i) {
		return (ControlFlowGraph) subterms.get(i);
	}

	public jjtraveler.Visitable setChildAt(int i, jjtraveler.Visitable child) {
		subterms.set(i,child);  
		return this;  
	}

  public jjtraveler.Visitable setChildren(jjtraveler.Visitable[] children){
    subterms.clear();
    for(int i=0; i<children.length;i++) {
      setChildAt(i,children[i]);
    }
    return this;
  }

  public jjtraveler.Visitable[] getChildren(){
    return (jjtraveler.Visitable[]) subterms.toArray();
  }


  public boolean verify(Strategy temporalCond,Vertex n){
    try{
      temporalCond.fire(subGraph(n));
      return true;
    }catch(FireException e){return false;}
  }

}


