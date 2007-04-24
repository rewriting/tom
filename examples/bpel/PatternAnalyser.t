/*
 * Copyright (c) 2004-2007, INRIA
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

import ted.*;
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
  %include {wfg/_Wfg.tom}
  %include {sl.tom}
  %include {strategy/graph_sl.tom}
  %include {adt/tnode/TNode.tom }

  %strategy Combine(wfg:Wfg) extends `Fail(){
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

  private static class ExplicitConditions {
    public HashMap sourceToName = new HashMap();
    public HashMap nameToCondition = new HashMap();

    %strategy Substitute(sourceToName:HashMap) extends `Identity() {
      visit Condition {
        label(n) -> {
          return `cond(refWfg( (String) sourceToName.get(n) ));
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
        (_*,mapEntry(k,v),_*) -> {
          Condition newCond = (Condition) ((Strategy) `TopDown(Substitute(sourceToName))).fire((Condition) `v);
          nameToCondition.put(`k,newCond);
        }
      }
    }
  }

  public static Wfg bpelToWfg(TNode term, ExplicitConditions explicitCond){
    Wfg wfg  = `Empty();
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
        wfg = (Wfg) `mu(MuVar("x"),Choice(Combine(bpelToWfg(proc,explicitCond)),All(MuVar("x")))).fire(wfg);
      }
      node@<(invoke|receive|reply) operation=operation>linklist*</(invoke|receive|reply)> -> {
        wfg = `WfgNode(Activity(operation,noCond(),noCond())); 
        %match(TNodeList linklist){
          (_*,<joincondition>cond</joincondition>,_*) -> {
            explicitCond.putCondition(`operation,ConditionParser.parse(`cond.getData())); 
          }
          (_*,<source linkName=linkName/>,_*) -> {
            explicitCond.putLinkName(`linkName,`operation);
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
        Wfg middle = bpelToWfg(`activity,explicitCond);
        Wfg begin = `labWfg(label,WfgNode(Activity("begin "+node.getName(),noCond(),noCond()), middle ));
        Wfg end = `WfgNode(Activity("end "+node.getName(),noCond(),noCond()), refWfg(label));
        wfg = (Wfg) `mu(MuVar("x"),Choice(Combine(end),All(MuVar("x")))).fire(begin);
      }
      node@ElementNode("if",_,(<condition></condition>,activity,elses*)) -> {
        Wfg res = bpelToWfg(`activity,explicitCond);
        wfglist = `ConcWfg(wfglist*,res);
        %match(TNodeList elses) {
          (_*,<(else|elseif)>altenate_activity</(else|elseif)>,_*) -> {
            res = bpelToWfg(`altenate_activity, explicitCond);
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
            wfg = `WfgNode(Activity(buffer.toString(),noCond(),noCond())); 
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
    `PrintWfg(wfg,node,visited).fire(wfg) ;    
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
          return `labWfg(name,a);
        }
      }
    }
  }


  // fails when finding the right ref
  %strategy FindRef(node:Info) extends `Identity() {
    visit Wfg {
      refWfg(s) -> {
        Activity act = (Activity) node.pos.getSubterm().fire(getEnvironment().getRoot());
        if (`s.equals(act.getname())){
          getEnvironment().setStatus(Environment.FAILURE);
        }
      }
    }
  }

  %strategy DefaultCond(node:Info) extends `Identity() {
    visit Wfg {
      a@Activity(name,incond,outcond) -> {
        Activity act = (Activity) node.pos.getSubterm().fire(getEnvironment().getRoot());
        String root_name = act.getname();
        if (root_name.equals("")) return `a;
        return `Activity(name,and(cond(refWfg(root_name)),incond),outcond);
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
    return (Wfg) `AddCondWfg(node,visited,nameToCondition).fire(wfg);
    //return (Wfg) StratDebugger.applyGraphicalDebug(wfg,`AddCondWfg(wfg,node,visited,nameToCondition));    
  }


  %strategy Print(node:Info,root:Wfg) extends `Identity(){
    visit Wfg{
      a@Activity[name=name,incond=incond] ->{
        Position pos = node.pos;
        if(pos != null){
          Activity act = (Activity) node.pos.getSubterm().fire(root);
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
      a@Activity[name=name,incond=incond] ->{
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
        wfg = (Wfg) WfgAbstractType.expand(wfg);
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
