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


public class Birds2 {

  %include{ adt/tnode/TNode.tom }
  %include{ sl.tom }

  private XmlTools xtools;

  public static void main (String args[]) {
    Birds2 birds = new Birds2();
    birds.run("xml/birds.xml");
  }

  private void run(String filename) {
    xtools = new XmlTools();
    TNode term = xtools.convertXMLToTNode(filename);
    try {
      term = (TNode)`TopDown(replace()).visitLight(term);
    }catch (VisitFailure e) {}
    //xtools.printXMLFromTNode(term);
  }

  %strategy replace() extends `Identity() {
    visit TNode {
      <Species>#TEXT(data)</Species> -> {
        System.out.println("catched birds '" + `data + "'");
      }

      <Species>#TEXT("Yellow-billed Loon.")</Species> -> {
        System.out.println("Bingo catched bird");
      }

      <Species Scientific_Name="Gavia adamsii">[#TEXT("Yellow-billed Loon.")]</Species> -> {
        System.out.println("Double bingo catched bird");
      }
    }
  }

}
