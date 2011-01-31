/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this conc of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this conc of conditions and the following disclaimer in the
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

package termgraph;

import tom.library.sl.*;
import tom.library.utils.Viewer;
import termgraph.term.*;
import termgraph.term.types.*;
import termgraph.term.strategy.term.*;
import termgraph.term.types.term.*;
import termgraph.term.types.list.*;

public class TermGraphAction {

  %include {sl.tom }
  %include {term/term.tom}
  //%include {term/_term.tom}

  %op Strategy GlobalRedirection(oldlabel:String,newlabel:String) {
    make(oldlabel,newlabel) {
      `TopDown(Redirection(oldlabel,newlabel))
    }
  }

  %strategy Redirection(oldlabel:String,newlabel:String) extends Identity() {
    visit List {
      RefList[labelList=l] -> {
        if (`l.equals(oldlabel)) {
          return `RefList(newlabel);
        }
      }
    }
  }


  %op Strategy LocalRedirection(label:String,child:int,newlabel:String) {
    make(label,child,newlabel) {
      `TopDown(Redirection2(label,child,newlabel))
    }
  }


  %strategy Redirection2(label:String,child:int,newlabel:String) extends Identity() {
    visit List {
      LabList[labelList=l] -> {
        if (`l.equals(label)) {
          getEnvironment().down(child);
          getEnvironment().setSubject(`RefList(newlabel));
          getEnvironment().up();
          return (List)getEnvironment().getSubject();
        }
      }
    }
  }


  %op Strategy Replacement(label:String,newvalue:List) {
    make(label,newvalue) {
      `TopDown(FindAndReplace(label,newvalue))
    }
  }

  %strategy FindAndReplace(label:String,newvalue:List) extends Identity() {
    visit List {
      LabList[labelList=l] -> {
        if (`l.equals(label)) {
          return `LabList(l,newvalue);
        }
      }
    }
  }

  %strategy Insertion() extends Identity() {
    visit List {
      LabList(gamma,insert(x,LabList(lambda,doublelinkedlist(RefList(alpha),y,next)))) -> {
        `LocalRedirection(alpha,3,gamma).visit(getEnvironment());
        List newvalue = `doublelinkedlist(RefList(alpha),x,LabList(lambda,doublelinkedlist(RefList(gamma),y,next)));
        return (List) `Replacement(gamma,newvalue).visit(getEnvironment());
      }
    }
  }

  public static void main(String[] args) {
    List abcd = `LabList("1", doublelinkedlist(nil(),a(),LabList("2",insert(b(),LabList("3",doublelinkedlist(RefList("1"),c(),doublelinkedlist(RefList("3"),d(),nil())))))));
    System.out.println("Original subject");
    Viewer.toDot((List)abcd.expand());
    try {
      System.out.println("Insertion with term-graph actions (Rachid Echahed's formalism)");
      Viewer.toDot(((List)`TopDown(Insertion()).visit(abcd)).expand());
      System.out.println("Insertion with term-graph rules from Gom");
      Viewer.toDot((`TopDown(List.Insert()).visit(abcd.expand())));
    } catch (VisitFailure e) {
      System.out.println("Unexpected visit failure during insertion");
    }
  }

}
