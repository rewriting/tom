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

public class P3PEvaluator1 {

  %include{ adt/tnode/TNode.tom }

  private XmlTools xtools;

  public static void main (String args[]) {
    P3PEvaluator1 evaluator = new P3PEvaluator1();
    evaluator.run("p3p/server.xml","p3p/client.xml");
  }

  private void run(String polfile,String clientfile){
    xtools = new XmlTools();
    TNode pol = (TNode)xtools.convertXMLToTNode(polfile);
    TNode client = (TNode)xtools.convertXMLToTNode(clientfile);
    boolean res = compareDataGroup(getDataGroup(pol.getDocElem()),getDataGroup(client.getDocElem()));
    System.out.println("res = " + res);
    //xtools.printXMLFromATerm(term);
  }

  private TNode getDataGroup(TNode doc) {
    %match(TNode doc) {
   	  <POLICIES>
        <POLICY>
          <STATEMENT>
            datagroup@<DATA-GROUP></DATA-GROUP>
          </STATEMENT>
        </POLICY>
     </POLICIES> -> { return `datagroup; }

  	  <RULESET>
        <RULE>
          <POLICY>
            <STATEMENT>
              datagroup@<DATA-GROUP></DATA-GROUP>
            </STATEMENT>
          </POLICY>
        </RULE>
     </RULESET> -> { return `datagroup; }

    }

	return `xml(<DATA-GROUP/>);
  }

  private boolean compareDataGroup(TNode pol, TNode client) {
    System.out.println("pol = ");
    xtools.printXMLFromTNode(pol);
    System.out.println();
    System.out.println("client = ");
    xtools.printXMLFromTNode(client);
    System.out.println();

    boolean res = true;
    %match(TNode client) {
      <DATA-GROUP><DATA ref=ref></DATA></DATA-GROUP> -> {
        System.out.println("ref = " + `ref);
        res = res && appearsIn(`ref,pol);
      }
    }
    return res;
  }

  private boolean appearsIn(String refclient, TNode pol) {
    %match(TNode pol) {
      <DATA-GROUP><DATA ref=ref></DATA></DATA-GROUP> -> {
        if(`ref.equals(refclient)) {
          return true;
        }
      }
    }
    return false;
  }
}
