/*
 * Copyright (c) 2004, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

package p3p;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;
import jtom.runtime.*;
import java.util.*;

public class P3PEvaluator2 {
   
  %include{ TNode.tom }
 
  private GenericTraversal traversal;
  public P3PEvaluator2() {
    this.traversal = new GenericTraversal();
  }
  
  private XmlTools xtools;
  private Factory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }
 
  public static void main (String args[]) {
    P3PEvaluator2 P3PEvaluator2 = new P3PEvaluator2();
    P3PEvaluator2.run("server.xml","client.xml");
  }

  private void run(String polfile,String clientfile){
    xtools = new XmlTools();
    TNode pol = (TNode)xtools.convertXMLToATerm(polfile);
    TNode client = (TNode)xtools.convertXMLToATerm(clientfile);
    boolean res = compareDataGroup(getDataGroup(pol),getDataGroup(client));
    System.out.println("res = " + res);
  }
     
  private TNode getDataGroup(TNode doc) {
    HashSet c = new HashSet();
    collectDatagroup(c,doc);
    Iterator it = c.iterator();
    while(it.hasNext()) {
      TNode datagroup = (TNode)it.next();
      return datagroup;
    }

    return `xml(<DATA-GROUP/>);
  }

  private boolean compareDataGroup(TNode pol, TNode client) {
    boolean res = true;
    %match(TNode client) {
      <DATA-GROUP><DATA ref=ref></DATA></DATA-GROUP> -> { res = res && appearsIn(`ref,pol); }
    }
    return res;
  }
 
  private boolean appearsIn(String refclient, TNode pol) {
    %match(TNode pol) {
      <DATA-GROUP><DATA ref=ref></DATA></DATA-GROUP>
         -> { 
         if(`ref.equals(refclient)) {
           return true;
         }
       }
    }
    return false;
  }

  protected void collectDatagroup(final Collection collection, TNode subject) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          if(t instanceof TNode) {
            %match(TNode t) { 
              <DATA-GROUP> </DATA-GROUP> -> {
                 collection.add(t);
                 return false;
               }
            }
          } 
          return true;
        } // end apply
      }; // end new
    
    traversal.genericCollect(subject, collect);
  }

  
}
