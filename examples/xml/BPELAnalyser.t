/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - Neither the name of the INRIA nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
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

package xml;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import tom.library.sl.*;

import java.util.*;

public class BPELAnalyser {

  %include{ adt/tnode/TNode.tom }
  %include{ sl.tom }
  %include{ util/ArrayList.tom}

  private XmlTools xtools;

  public static void main (String args[]) {
    BPELAnalyser analyser = new BPELAnalyser();
    analyser.run("xml/exampleBPEL.xml");
  }

  private void run(String filename) {
    xtools = new XmlTools();
    TNode term = xtools.convertXMLToTNode(filename);
    ArrayList controlDep = new ArrayList();
    try {
      term = (TNode) `TopDown(removeTextNode()).visit(term);
      `TopDown(analyse(controlDep)).visit(term);
      System.out.println("Control dependence : "+controlDep);
    } catch(VisitFailure e) {
      System.out.println("failure");
    }
  }

  %strategy removeTextNode() extends Identity(){
    visit TNodeList {
      concTNode(head,tail*)-> {
        %match(TNode head){
          TextNode(_) -> {return (TNodeList) this.visitLight(`tail);} 
          _ -> {
            TNodeList newTail = (TNodeList) this.visitLight(`tail); 
            return `concTNode(head,newTail*);
          } 
        }
      }
   }
  }

  %strategy analyse(controlDep:ArrayList) extends `Identity() {
    visit TNode {
      <sequence>(_*,elt1,_,_*)</sequence> -> {
        ArrayList leaveList = new ArrayList();
        `leaves(leaveList).visit(`elt1);
        Iterator leaveIter = leaveList.iterator();
        ArrayList rootList = new ArrayList();
        `roots(rootList).visit(`elt1);
        while(leaveIter.hasNext()){
          String leave = (String) leaveIter.next();
          Iterator rootIter = rootList.iterator();
          while(rootIter.hasNext()){
            controlDep.add(leave+"-"+rootIter.next());
          }
        }
      }
    }
  }



  %strategy roots(l:ArrayList) extends `Identity() {
    visit TNode {
      <sequence>(x,_*)</sequence> -> {
        this.visitLight(`x);
      }
      <flow>(_*,x,_*)</flow> -> {
        this.visitLight(`x);
      }
      <invoke>(_*,<activity name=x/>,_*)</invoke> -> {
        l.add(`x);
      }
    }
  }

 %strategy leaves(l:ArrayList) extends `Identity() {
    visit TNode {
      <sequence>(_*,x)</sequence> -> {
        this.visitLight(`x);
      }
      <flow>(_*,x,_*)</flow> -> {
        this.visitLight(`x);
      }
      <invoke>(_*,<activity name=x/>,_*)</invoke> -> {
        l.add(`x);
      }
    }
  }

}
