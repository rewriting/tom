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

package xml.dommapping;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class PersonSortName {
  private Document dom;

  %include{ dom_1_5.tom }

  public static void main (String args[]) {
    PersonSortName person = new PersonSortName();
    person.run("xml/dommapping/person.xml");
  }

  private void run(String filename) {
    try {
      dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
      Element e = dom.getDocumentElement();
      dom.replaceChild(sort(e),e);

      Transformer transform = TransformerFactory.newInstance().newTransformer();
      StreamResult result = new StreamResult(new File("xml/dommapping/Sorted.xml"));
      transform.transform(new DOMSource(dom), result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Node sort(Node subject) {
    %match(TNode subject) {
      <Persons>(X1*,p1,X2*,p2,X3*)</Persons> -> {
        if(`compare(p1,p2) > 0) {
          return sort(`xml(dom,<Persons>X1* p2 X2* p1 X3*</Persons>));
        }
      }
    }
    return subject;
  }

  /*
  private int compare(Node t1, Node t2) {
    %match(TNode t1, TNode t2) {
      <_ Age=a1></_>, <_ Age=a2></_> -> { return a1.compareTo(a2); }
    }
    return 0;
  }
  */

  private int compare(Node t1, Node t2) {
    %match(TNode t1, TNode t2) {
      <Person Age=_><FirstName>#TEXT(n1)</FirstName></Person>,
      <Person Age=_><FirstName>#TEXT(n2)</FirstName></Person>
      -> { return `n1.compareTo(`n2); }
    }
    return 0;
  }
}
