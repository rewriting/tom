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
  %include {mustrategy.tom}
  %include {strategy/graph.tom}
  %include {adt/tnode/TNode.tom }

  %strategy Combine(wfg:Wfg) extends `Identity(){
    visit Wfg{
      Empty() -> {
        return wfg; 
      }

      node@WfgNode(leaf@Activity[],refList*) -> {
        %match(Wfg refList){
          WfgNode(_*,!refWfg[],_*) -> {
             // node is not a leaf
                return `node;
            }
          }
        // node is a leaf ( contains only chidren of type ref)
          return `WfgNode(leaf,refList*,wfg);
        }
      }
    }

  private static int whileCounter = 0;

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
      node@<(invoke|receive|reply) operation=operation>linklist*</(invoke|receive|reply)> -> {
        wfg = `WfgNode(Activity(operation,node.hashCode(),noCond())); 
        %match(TNodeList linklist){
          (_*,<source linkName=linkName/>,_*) -> {
            wfg = `WfgNode(wfg*,refWfg(linkName));
          }
          (_*,<target linkName=linkName/>,_*) -> {
            wfg = `labWfg(linkName,wfg);
          }
        }
      }
      node@<(while|repeatUntil)>(<condition></condition>, activity)</(while|repeatUntil)> -> {
        whileCounter++;
        String label = "loop" + whileCounter;
        Wfg middle = bpelToWfg(`activity);
        Wfg begin = `labWfg(label, WfgNode(Activity("begin "+node.getName(),node.hashCode(),noCond()), middle ));
        Wfg end = `WfgNode(Activity("end "+node.getName(),-node.hashCode(),noCond()), refWfg(label));
        wfg = (Wfg) `mu(MuVar("x"),ChoiceId(Combine(end),All(MuVar("x")))).apply(begin);
      }
      node@ElementNode("if",_,(<condition></condition>,activity,elses*)) -> {
        Wfg res = bpelToWfg(`activity);
        wfglist = `ConcWfg(wfglist*,res);
        %match(TNodeList elses) {
          (_*,<(else|elseif)>altenate_activity</(else|elseif)>,_*) -> {
            res = bpelToWfg(`altenate_activity);
            wfglist = `ConcWfg(wfglist*,res);
          }
          _ -> {
            return wfglist;
          }
        }
      }
      node@<assign>list*</assign> -> {
        StringBuffer buffer = new StringBuffer();

        %match(TNodeList list){
          (_*,<copy>fromnode@<from /> <to variable=to /></copy>,_*) -> {
            String from = "";

            %match(fromnode){
              <from variable=variable /> -> {from = `variable;}
              <from expression=expression /> -> {from = `expression;}
            }

            buffer.append(%[ @from@ -> @`to@\n ]%);
          }  
          (_*) -> {        
            wfg = `WfgNode(Activity(buffer.toString(),node.hashCode(),noCond())); 
          }
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
    make(wfg,s) {
      `mu(MuVar("x"),Sequence(One(RelativeRef(wfg,s)),Try(_ConsWfgNode(Identity(),MuVar("x")))))
    }
  }

  %op Strategy PrintWfgNode(wfg:Visitable,node:Node,visited:HashSet) {
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
    Node node = new Node();
    HashSet visited = new HashSet();
    System.out.println("digraph g{");
    `PrintWfg(wfg,node,visited).apply(wfg);    
    //StratDebugger.applyGraphicalDebug(wfg,`PrintWfg(wfg,node,visited));    
    System.out.println("}");
  }

  %strategy Debug(s: String) extends `Identity() {
    visit Wfg {
      _ -> {
        System.out.println("- " + s +": " + getPosition());
      }
    }
  }


  %strategy Print(root:Node) extends `Identity(){
    visit Wfg{
      WfgNode(Activity[name=name,code=code],_*) ->{
        String id = ("" + `code).replaceAll("-","Z");
        System.out.println(root.id + "[label=\""+ `root.name +"\"];");
        System.out.println(id + "[label=\""+ `name +"\"];");
        if (`name.startsWith("begin") || `name.startsWith("end"))  System.out.println(id + "[shape=box];");
        System.out.println(root.id + " -> " + id + ";");
      }
    }
  }

  %strategy GetRoot(root:Node,visited:HashSet) extends `Fail(){
    visit Wfg{
      a@Activity[name=name,code=code] ->{
        if (!visited.contains(new Integer(`code))) {
          visited.add(new Integer(`code));
          root.name = `name;
          root.id = ("" + `code).replaceAll("-","Z");
          return `a;
        }
      }
    }
  }

  %typeterm Node{
    implement {Node}
  }
  static class Node{
    public String name="";
    public String id="";
  }

  public static void main(String[] args){
    XmlTools xtools = new XmlTools();
    xtools.setDeletingWhiteSpaceNodes(true);
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
    PatternAnalyser.printWfg(wfg);
  }
}//class PatternAnalyser
