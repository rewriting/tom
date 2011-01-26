/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import java.util.*;
import java.io.InputStream;
import tom.library.sl.VisitFailure;

public class P3PEvaluator2 {

  %include{ adt/tnode/TNode.tom }
  %include{ sl.tom }
  %include{ java/util/types/Collection.tom }

  private XmlTools xtools;

  public static void main (String args[]) {
    P3PEvaluator2 P3PEvaluator2 = new P3PEvaluator2();
    boolean res = P3PEvaluator2.run(
        P3PEvaluator2.class.getResourceAsStream("server.xml"),
        P3PEvaluator2.class.getResourceAsStream("client.xml"));
    System.out.println("res = " + res);
  }

  public boolean run(InputStream polfile, InputStream clientfile) {
    xtools = new XmlTools();
    TNode pol = (TNode)xtools.convertXMLToTNode(polfile);
    TNode client = (TNode)xtools.convertXMLToTNode(clientfile);
    return compareDataGroup(getDataGroup(pol),getDataGroup(client));
  }

  private TNode getDataGroup(TNode doc) {
    HashSet<TNode> c = new HashSet<TNode>();
    try {
      `TopDownCollect(collectDatagroup(c)).visitLight(doc);
    } catch(VisitFailure e) {}
    Iterator<TNode> it = c.iterator();
    while(it.hasNext()) {
      return it.next();
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

  %typeterm CollectionTNode {
    implement { Collection<TNode> }
  }
  %strategy collectDatagroup(collection:CollectionTNode) extends `Identity() {
    visit TNode {
      t@<DATA-GROUP> </DATA-GROUP> -> {
        collection.add(`t);
        throw new VisitFailure();
      }
    }
  }
}
