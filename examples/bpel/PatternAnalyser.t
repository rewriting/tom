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

 %strategy Print() extends `Identity() {
    visit Wfg{
      inst@_ -> {
        System.out.println("---print---"+`inst);      
      }
    }
  }


  public static Wfg bpelToWfg(TNode term,int index){
    Wfg wfg = `ConcWfg();
    %match(TNode term){
      <process> process </process> -> {
        %match(TNode process){
          <flow>p</flow> ->{
            wfg = `ConcWfg(wfg*,bpelToWfg(p,index++)*);
          }
          <sequence>p</sequence> ->{
            wfg = `ConcWfg(wfg*,bpelToWfg(p,index++)*);
          }
          <activity><source name=label /></activity> -> {
            //wfg = `();
          }
        }
      }

    }
    return wfg;
  }

  public static void main(String[] args){
    /*
       XmlTools xtools = new XmlTools();
       TNode term = xtools.convertXMLToTNode(args[0]);
     */
    Wfg wfg = `expWfg(ConcWfg(
        Wfg(Activity("empty"),refWfg("A"),refWfg("C"),refWfg("G")), 
        labWfg("A",Wfg(Activity("A"),refWfg("B"),refWfg("F"))), 
        labWfg("C",Wfg(Activity("C"),refWfg("D"),refWfg("E"))), 
        labWfg("G",Wfg(Activity("G"),refWfg("E"),refWfg("empty"))), 
        labWfg("B",Wfg(Activity("B"),refWfg("empty"))), 
        labWfg("D",Wfg(Activity("D"),refWfg("F"))), 
        labWfg("E",Wfg(Activity("E"),refWfg("F"))), 
        labWfg("F",Wfg(Activity("F"),refWfg("empty"))), 
        labWfg("empty",Wfg(Activity("empty"))) 
        ));

    PatternAnalyser analyser = new PatternAnalyser();
    try{
      System.out.println("\nWfg with positions:\n" + wfg);
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}//class PatternAnalyser
