/*
 * Copyright (c) 2004-2006, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce:w
 the above copyright
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

package bpel;

import bpel.wfg.*;
import bpel.wfg.types.*;
import bpel.wfg.strategy.wfg.*;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;


import tom.library.strategy.mutraveler.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;


public class PatternAnalyser{

  %include {util/HashSet.tom}
  %include {util/ArrayList.tom}
  %include {wfg/Wfg.tom}
  %include {wfg/_Wfg.tom}
  %include {Ctl.tom}
  %include {adt/tnode/TNode.tom }

  %strategy Combine(wfg:Wfg) extends `Identity(){
    visit Wfg{
      Empty() -> {
        return wfg; 
      }

      node@WfgNode(leaf@Activity[],refList*) -> {
        %match(Wfg refList){
          WfgNode(_*,x,_*) -> {
            %match(Wfg x){
              !refWfg(y) -> {
                return `node;
              }
            }
          }
        }
        return `WfgNode(leaf,refList*,wfg);
      }
    }
  }

  public static Wfg bpelToWfg(TNode term){
    Wfg wfg  = `Empty();
    Wfg wfglist = `ConcWfg();
    %match(TNode term){
      <flow>proc</flow> ->{
        Wfg res = bpelToWfg(`proc);
        wfglist = `ConcWfg(wfglist*,res);
      }
      <flow></flow> ->{
        return wfglist;
      }
      <sequence>proc</sequence> ->{
        wfg = (Wfg) `mu(MuVar("x"),ChoiceId(Combine(bpelToWfg(proc)),All(MuVar("x")))).apply(wfg);
      }
      <(assign|invoke|receive|reply) name=name>linklist*</(assign|invoke|receive|reply)> -> {
        wfg = `WfgNode(Activity(name,noCond(),noCond())); 
        %match(TNodeList linklist){
          (_*,<source linkName=linkName/>,_*) -> {
            wfg = `WfgNode(wfg*,refWfg(linkName));
          }
          (_*,<target linkName=linkName/>,_*) -> {
            wfg = `labWfg(linkName,wfg);
          }
        }
      }
    }
    return wfg; 
  }

  %op Strategy VisitWfgNode(wfg:Visitable,s:Strategy) {
    /*
       make(wfg,s) {
       `mu(MuVar("x"),Sequence(Debug("visitWfgNode"),Sequence(OneRelativeRefSensitive(wfg,Sequence(Debug("followed"),s)),Try(_ConsWfgNode(Identity(),MuVar("x"))))))
       }
     */
    make(wfg,s) {
      `mu(MuVar("x"),Sequence(One(RelativeRef(wfg,s)),Try(_ConsWfgNode(Identity(),MuVar("x")))))
    }
  }

  %op Strategy PrintWfgNode(wfg:Visitable,node:Node,visited:HashSet) {
    /*
       make(wfg,node,visited) {
       `mu(MuVar("y"),Sequence(Debug("printwfgnode"),Try(_ConsWfgNode(GetRoot(node,visited),Sequence(VisitWfgNode(wfg,Print(node)),VisitWfgNode(wfg,MuVar("y"))))))   )
       }
     */
    make(wfg,node,visited) {
      `mu(MuVar("y"),Try(_ConsWfgNode(GetRoot(node,visited),Sequence(VisitWfgNode(wfg,Print(node)),VisitWfgNode(wfg,MuVar("y"))))))
   }

  }

  %op Strategy PrintWfg(wfg:Visitable,node:Node,visited:HashSet) {
    make(wfg,node,visited) {
      `Choice(mu(MuVar("z"),_ConsConcWfg(PrintWfgNode(wfg,node,visited),MuVar("z"))),PrintWfgNode(wfg,node,visited))
    }
  }

  public static void printWfg(Wfg wfg){
    Node node = new Node("");
    HashSet visited = new HashSet();
    System.out.println("digraph g{");
    `PrintWfg(wfg,node,visited).apply(wfg);    
    System.out.println("}");
  }

  %strategy Debug(s: String) extends `Identity() {
    visit Wfg {
      _ -> {
        System.out.println("- " + s +": " + getPosition());
      }
    }
  }


  /*
     %strategy PrintWfg(wfg:Visitable,node:Node,visited:HashSet) extends `Identity(){
     visit Wfg{
     e@ConcWfg(_*) -> {
     `mu(MuVar("z"),Sequence(Debug("PrintWfg"), 
     Try(_ConsConcWfg(PrintWfgNode(wfg,node,visited),MuVar("z"))))  
     ).apply(`e);
     }
     e@WfgNode(_*) -> {
     `PrintWfgNode(wfg,node,visited).apply(`e);
     }
     }
     }
   */

  %strategy Print(root:Node) extends `Identity(){
    visit Wfg{
      WfgNode(Activity[name=name],_*) ->{
        System.out.println(root.name+" -> "+`name+";");
      }
      Activity[name=name] ->{
        System.out.println(root.name+" -> "+`name+";");
      }
    }
  }

  %strategy GetRoot(root:Node,visited:HashSet) extends `Fail(){
    visit Wfg{
      a@Activity[name=name] ->{
        if (!visited.contains(`name)) {
          visited.add(`name);
          root.name = `name;
          //System.out.println("getroot : " + `name);
          return `a;
        }
      }
    }
  }

  %typeterm Node{
    implement {Node}
  }
  static class Node{
    public String name;

    public Node(String name){
      this.name=name;
    }

  }
  public static void main(String[] args){
    XmlTools xtools = new XmlTools();
    TNode term = xtools.convertXMLToTNode(args[0]);
    Wfg wfg = null;
    %match(TNode term){
      DocumentNode(_,ElementNode("process",_,concTNode(_*,elt@<(sequence|flow)></(sequence|flow)>,_*))) -> {
        wfg = bpelToWfg(`elt); 
        //System.out.println("\nWfg with labels:\n" + wfg);
        wfg = `expWfg(wfg);
        //System.out.println("\nWfg with positions:\n" + wfg);
      }
    }
    /* 
       Wfg wfg = `expWfg(ConcWfg(
       WfgNode(Activity("start",noCond(),noCond()),refWfg("A"),refWfg("C")),
       labWfg("A",WfgNode(Activity("A",noCond(),noCond()),refWfg("end"))), 
       labWfg("C",WfgNode(Activity("C",noCond(),noCond()),refWfg("end"))), 
       labWfg("end",WfgNode(Activity("end",noCond(),noCond()))) 
       ));
       wfg = `expWfg(ConcWfg(
       WfgNode(Activity("start",noCond(),noCond()),refWfg("A"),refWfg("C"),refWfg("G")), 
       labWfg("A",WfgNode(Activity("A",noCond(),noCond()),refWfg("B"),refWfg("F"))), 
       labWfg("C",WfgNode(Activity("C",noCond(),noCond()),refWfg("D"),refWfg("E"))), 
       labWfg("G",WfgNode(Activity("G",noCond(),noCond()),refWfg("E"),refWfg("end"))), 
       labWfg("B",WfgNode(Activity("B",noCond(),noCond()),refWfg("end"))), 
       labWfg("D",WfgNode(Activity("D",noCond(),noCond()),refWfg("F"))), 
       labWfg("E",WfgNode(Activity("E",noCond(),noCond()),refWfg("F"))), 
       labWfg("F",WfgNode(Activity("F",noCond(),noCond()),refWfg("end"))), 
       labWfg("end",WfgNode(Activity("end",noCond(),noCond()))) 
       ));
     */
    PatternAnalyser.printWfg(wfg);
  }
}//class PatternAnalyser
