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
  
  %include {mustrategy.tom }
  %include {util/HashMap.tom}
  %include {util/ArrayList.tom}
  %include {wfg/Wfg.tom}
  %include {Ctl.tom}
  %include {adt/tnode/TNode.tom }

  %strategy Collect(list:ArrayList) extends `Identity() {
    visit Wfg{
      x@_ -> {
        list.add(`x); 
      }
    }
  }



 %strategy Combine(wfg:Wfg) extends `Identity(){
   visit Wfg{
     Empty() -> {
       return wfg; 
     }

     WfgNode(leaf@Activity[]) -> {
       return `WfgNode(leaf,wfg);
     }
   }
 }

 public static Wfg bpelToWfg(TNode term){
   Wfg wfg  = `Empty();
   Wfg wfglist = `ConcWfg();
   %match(TNode term){
     <flow>p</flow> ->{
       Wfg res = bpelToWfg(`p);
       wfglist = `ConcWfg(wfglist*,res);
     }
     <flow></flow> ->{
       return wfglist;
     }
     <sequence>p</sequence> ->{
       wfg = (Wfg) `mu(MuVar("x"),ChoiceId(Combine(bpelToWfg(p)),All(MuVar("x")))).apply(wfg);
     }
     <activity name=name /> -> {
       return `WfgNode(Activity(name,noCond(),noCond())); 
     }
   }
   return wfg; //wfg;
 }

 %strategy removeTextNode() extends Identity(){
   visit TNodeList {
     concTNode(head,tail*)-> {
       %match(TNode head){
         TextNode(_) -> {return (TNodeList) this.visit(`tail);} 
         _ -> {
           TNodeList newTail = (TNodeList) this.visit(`tail); 
           return `concTNode(head,newTail*);
         } 
       }
     }
   }
 }

 /*
    %strategy getNode(root:Wfg,currentPos:Position) extends `Identity(){
    visit Wfg{
    pos@posWfg(_*) ->{
    RelativePosition posRelative = new RelativePosition(((bpel.wfg.types.wfg.posWfg)`pos).toArray());
    System.out.println("Position relative:"+posRelative);
    Position position = posRelative.getAbsolutePosition(currentPos);
    System.out.println("Position :"+position);
    return (Wfg) position.getSubterm().apply(root);
    }
    }
    }
  */

 %op Strategy _ConsWfgNode(s1:Strategy,s2:Strategy){
   is_fsym(t) { (t instanceof _ConsWfgNode) }
   make(s1,s2) { new _ConsWfgNode(s1,s2) }
 }

 %op Strategy _ConsConcWfg(s1:Strategy,s2:Strategy){
   is_fsym(t) { (t instanceof _ConsConcWfg) }
   make(s1,s2) { new _ConsConcWfg(s1,s2) }
 }


 %op Strategy VisitWfgNode(v:Visitable,s:Strategy) {
   make(v,s) {`mu(MuVar("x"),Sequence(OneRefSensitive(v,s),Try(_ConsWfgNode(Identity(),MuVar("x")))))}
 }

 public static void printWfg(Wfg wfg){
   Node root = new Node("");
   System.out.println("digraph g{");
   %match(Wfg wfg) {
     ConcWfg(e,_*) -> {
       `mu(MuVar("y"),_ConsWfgNode(GetRoot(root),Sequence(VisitWfgNode(wfg,Print(root)),VisitWfgNode(wfg,Try(MuVar("y")))))).apply(`e);
     }
   }
   System.out.println("}");
 }

 %strategy Debug() extends `Identity(){
   visit Wfg{
     _ -> {
       System.out.println("debug");
     }
   }
 }

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

 %strategy GetRoot(root:Node) extends `Identity(){
   visit Wfg{
     Activity[name=name] ->{
       root.name = `name; 
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
   term = (TNode) `TopDown(removeTextNode()).apply(term);
   Wfg wfg = null; //`ConcWfg(WfgNode(Activity("_start",noCond(),noCond())));
   %match(TNode term){
     DocumentNode(_,ElementNode("process",_,concTNode(_*,process,_*))) -> {
       wfg = bpelToWfg(`process); 
       wfg = `ConcWfg(wfg);
     }
   }
   /* 
      Wfg wfg = `expWfg(ConcWfg(
      WfgNode(Activity("start",noCond(),noCond()),refWfg("A"),refWfg("C")),
      labWfg("A",WfgNode(Activity("A",noCond(),noCond()),refWfg("end"))), 
      labWfg("C",WfgNode(Activity("C",noCond(),noCond()),refWfg("end"))), 
      labWfg("end",WfgNode(Activity("end",noCond(),noCond()))) 
      ));
      Wfg wfg = `expWfg(ConcWfg(
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
   System.out.println("\nWfg with positions:\n" + wfg);
   PatternAnalyser.printWfg(wfg);
 }
}//class PatternAnalyser
