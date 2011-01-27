/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
import bpel.wfg.types.wfg.*;
import bpel.wfg.strategy.wfg.*;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import tom.library.sl.*;
import java.util.*;


public class PatternAnalyser{
  %include {util/HashSet.tom}
  %include {util/HashMap.tom}
  %include {wfg/Wfg.tom}
  //%include {wfg/_Wfg.tom}
  %include {sl.tom}
  %include {sl/graph.tom}
  %include {adt/tnode/TNode.tom }

  %strategy Combine(wfg:Wfg) extends `Fail(){
    visit Wfg{
      EmptyWfg() -> {
        return wfg; 
      }

      node@WfgNode(leaf@Activity[],refList*) -> {
        %match(Wfg refList){
          WfgNode(_*,!RefWfg[],_*) -> {
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

  private static class ExplicitConditions {
    public HashMap sourceToName = new HashMap();
    public HashMap nameToCondition = new HashMap();

    %strategy Substitute(sourceToName:HashMap) extends `Identity() {
      visit Condition {
        Label(n) -> {
          return `Cond(RefWfg( (String) sourceToName.get(n) ));
        }
      }
    }

    public void putCondition(String name, Condition cond) {
      nameToCondition.put(name,cond);
    }

    public void putLinkName(String source, String name) {
      sourceToName.put(source,name);
    }

    public void substitute() {
      %match(HashMap nameToCondition) {
        concHashMap(_*,mapEntry(k,v),_*) -> {
          try {
            Condition newCond = `TopDown(Substitute(sourceToName)).visit((Condition)`v);
            nameToCondition.put(`k,newCond);
          } catch(VisitFailure e) {
            throw new tom.engine.exception.TomRuntimeException();
          }
        }
      }
    }
  }

  public static Wfg bpelToWfg(TNode term, ExplicitConditions explicitCond){
    Wfg wfg  = `EmptyWfg();
    Wfg wfglist = `ConcWfg();

    %match(TNode term){
      <flow>proc</flow> ->{
        Wfg res = bpelToWfg(`proc, explicitCond);
        wfglist = `ConcWfg(wfglist*,res);
      }
      <flow></flow> ->{
        return wfglist;
      }
      <sequence>proc</sequence> ->{
        try {
          wfg = (Wfg) `mu(MuVar("x"),Choice(Combine(bpelToWfg(proc,explicitCond)),All(MuVar("x")))).visit(wfg);
        } catch(VisitFailure e) {
          throw new tom.engine.exception.TomRuntimeException();
        }
      }
      <(invoke|receive|reply) operation=operation>linklist*</(invoke|receive|reply)> -> {
        wfg = `WfgNode(Activity(operation,NoCond(),NoCond())); 
        %match(TNodeList linklist){
          concTNode(_*,<joincondition>cond</joincondition>,_*) -> {
            explicitCond.putCondition(`operation,CondParser.parse(`cond.getData())); 
          }
          concTNode(_*,<source linkName=linkName/>,_*) -> {
            explicitCond.putLinkName(`linkName,`operation);
            wfg = `WfgNode(wfg*,RefWfg(linkName));
          }
          concTNode(_*,<target linkName=linkName/>,_*) -> {
            wfg = `LabWfg(linkName,wfg);
          }
        }
      }
      node@<(while|repeatUntil)>(<condition></condition>, activity)</(while|repeatUntil)> -> {
        whileCounter++;
        String label = "loop" + whileCounter;
        Wfg middle = bpelToWfg(`activity,explicitCond);
        Wfg begin = `LabWfg(label,WfgNode(Activity("begin "+node.getName(),NoCond(),NoCond()), middle ));
        Wfg end = `WfgNode(Activity("end "+node.getName(),NoCond(),NoCond()), RefWfg(label));
        try {
          wfg = (Wfg) `mu(MuVar("x"),Choice(Combine(end),All(MuVar("x")))).visit(begin);
        } catch(VisitFailure e) {
          throw new tom.engine.exception.TomRuntimeException();
        }
      }
      ElementNode("if",_,concTNode(<condition></condition>,activity,elses*)) -> {
        Wfg res = bpelToWfg(`activity,explicitCond);
        wfglist = `ConcWfg(wfglist*,res);
        %match(TNodeList elses) {
          concTNode(_*,<(else|elseif)>altenate_activity</(else|elseif)>,_*) -> {
            res = bpelToWfg(`altenate_activity, explicitCond);
            wfglist = `ConcWfg(wfglist*,res);
          }
          _ -> {
            return wfglist;
          }
        }
      }
      <assign>list*</assign> -> {
        StringBuilder buffer = new StringBuilder();

        %match(TNodeList list){
          concTNode(_*,<copy>fromnode@<from /> <to variable=to /></copy>,_*) -> {
            String from = "";

            %match(fromnode){
              <from variable=variable /> -> {from = `variable;}
              <from expression=expression /> -> {from = `expression;}
            }

            buffer.append(%[ @from@ -> @`to@\n ]%);
          }
          concTNode(_*) -> {        
            wfg = `WfgNode(Activity(buffer.toString(),NoCond(),NoCond())); 
          }
          concTNode(_*,<source linkName=linkName/>,_*) -> {
            wfg = `WfgNode(wfg*,RefWfg(linkName));
          }
          concTNode(_*,<target linkName=linkName/>,_*) -> {
            wfg = `LabWfg(linkName,wfg);
          }
        }
      }
    }
    return wfg; 
  }

  %op Strategy CurrentNode(s:Strategy) {
    make(s) {
      `_ConsWfgNode(DeRef(s),Identity())
    }
  }

  %op Strategy AllWfg(s:Strategy) {
    make(s) {
      `_ConsWfgNode(Identity(),Choice(_EmptyWfgNode(),_WfgNode(DeRef(s))))
    }
  }


  %op Strategy PrintWfgNode(wfg:Wfg,node:Info,visited:HashSet) {
    make(wfg,node,visited) {
      `mu(MuVar("y"),Try(Sequence(CurrentNode(GetRoot(node,visited)),AllWfg(CurrentNode(Print(node,wfg))),AllWfg(MuVar("y")))))
    }
  }

  %op Strategy PrintWfg(wfg:Wfg,node:Info,visited:HashSet) {
    make(wfg,node,visited) {
      `Choice(_ConcWfg(PrintWfgNode(wfg,node,visited)),PrintWfgNode(wfg,node,visited))
    }
  }

  public static void printWfg(Wfg wfg){
    Info node = new Info();
    node.pos = null;
    HashSet visited = new HashSet();
    System.out.println("digraph g{");
    try {
      `PrintWfg(wfg,node,visited).visit(wfg) ;    
    } catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException();
    }
    //StratDebugger.applyDebug(wfg,`PrintWfg(wfg,node,visited));    
    System.out.println("}");
  }

  %strategy Debug(label:String) extends `Identity() {
    visit Wfg {
      x -> {
        System.out.println(label);
        System.out.println(`x);
      }
    }
  }

  %strategy LabelActivityByItsName(visited:HashSet) extends `Identity() {
    visit Wfg {
      a@Activity[name=name] -> {
        if (!visited.contains(`name)){
          visited.add(`name);
          return `LabWfg(name,a);
        }
      }
    }
  }


  // fails when finding the right ref
  %strategy FindRef(node:Info) extends `Identity() {
    visit Wfg {
      RefWfg(s) -> {
        Activity act = (Activity) node.pos.getSubterm().visit((Visitable) getEnvironment().getRoot());
        if (`s.equals(act.getname())){
          throw new VisitFailure();
        }
      }
    }
  }

  %strategy DefaultCond(node:Info) extends `Identity() {
    visit Wfg {
      a@Activity(name,incond,outcond) -> {
        Activity act = (Activity) node.pos.getSubterm().visit( (Visitable) getEnvironment().getRoot());
        String root_name = act.getname();
        if (root_name.equals("")) return `a;
        return `Activity(name,And(Cond(RefWfg(root_name)),incond),outcond);
      }
    }
  }

  /* adds default condition concerning node to the visited WfgNode 
     if it doesn't already contain a condition about node */
  %op Strategy AddDefaultCond(node:Info) {
    make(node) { `Choice(Not(TopDown(FindRef(node))),DefaultCond(node)) }
  }

  // adds the explicit condition if present in the hashmap
  %strategy AddExplicitCond(nameToCondition:HashMap) extends Identity() {
    visit Wfg {
      node@Activity(name,incond,outcond) -> {
        Condition newcond = (Condition) nameToCondition.get(`name);
        if (newcond == null) return `node;
        return `Activity(name,newcond,outcond);
      }
    }
  }


  %op Strategy AddCondWfgNode(node:Info,visited:HashSet,nameToCondition:HashMap) {
    make(node,visited,nameToCondition) {
      `mu(MuVar("y"),Try(Sequence(
              CurrentNode(GetRoot(node,visited)),
              AllWfg(CurrentNode(
                  Sequence(
                    AddExplicitCond(nameToCondition),
                    AddDefaultCond(node)
                    ))),
              AllWfg(MuVar("y")))))
    }
  }

  %op Strategy _ConcWfgSeq(s:Strategy){
    make(s){
      `Choice(When_ConsConcWfg(AllSeq(s)),Is_EmptyConcWfg())
    }
  }

  %op Strategy AddCondWfg(node:Info,visited:HashSet,nameToCondition:HashMap) {
    make(node,visited,nameToCondition){
      `Choice(_ConcWfgSeq(AddCondWfgNode(node,visited,nameToCondition)),AddCondWfgNode(node,visited,nameToCondition))
    }
  }

  public static Wfg addConditionsWfg(Wfg wfg, HashMap nameToCondition){
    Info node = new Info();
    node.pos = null;
    HashSet visited = new HashSet();
    try {
      return (Wfg) `AddCondWfg(node,visited,nameToCondition).visit(wfg);
    } catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException();
    }
    //return (Wfg) StratDebugger.applyGraphicalDebug(wfg,`AddCondWfg(wfg,node,visited,nameToCondition));    
  }


  %strategy Print(node:Info,root:Wfg) extends `Identity(){
    visit Wfg{
      Activity[name=name,incond=incond] ->{
        Position pos = node.pos;
        if(pos != null){
          Activity act = (Activity) node.pos.getSubterm().visit(root);
          System.out.println(act.getname()+ "[label=\""+ act.getname() + "\\n" + act.getincond() +"\"];");
          System.out.println(`name+ "[label=\""+ `name + "\\n" + `incond +"\"];");
          if (`name.startsWith("begin") || `name.startsWith("end"))  System.out.println(`name + "[shape=box];");
          System.out.println(act.getname() + " -> " + `name + ";");
        }
      }
    }
  }

  %strategy GetRoot(node:Info,visited:HashSet) extends `Fail(){
    visit Wfg{
      a@Activity[] ->{
        Position newPos = getEnvironment().getPosition();
        if (!visited.contains(newPos)) {
          node.pos = getEnvironment().getPosition();
          visited.add(newPos);
          return `a;
        }
      }
    }
  }

  %typeterm Info{
    implement {Info}
    is_sort(t) { t instanceof Info } 
  }

  public static class Info {
    Position pos;
  }

  public static void main(String[] args){
    XmlTools xtools = new XmlTools();
    xtools.setDeletingWhiteSpaceNodes(true);
    TNode term = xtools.convertXMLToTNode(args[0]);
    Wfg wfg = null;
    %match(TNode term){
      DocumentNode(_,ElementNode("process",_,concTNode(_*,elt@<(sequence|flow)></(sequence|flow)>,_*))) -> {
        ExplicitConditions conds = new ExplicitConditions();
        // graph construction
        wfg = bpelToWfg(`elt, conds);
        // graph expansion
        System.out.println("\nWfg with labels:\n" + wfg);
        wfg = (Wfg) wfg.expand();
        System.out.println("\nWfg after expansion:\n" + wfg);
        // substituting link names by node names in the explicit conditions
        conds.substitute();
        // adding explicit and implicit conditions 
        wfg = addConditionsWfg(wfg,conds.nameToCondition);
        System.out.println("\nWfg after adding explicit conditions:\n" + wfg);
        //dot code generation
        printWfg(wfg);
        //VisitableViewer.visitableToDotStdout(wfg);
      }
    }
  }
}//class PatternAnalyser
