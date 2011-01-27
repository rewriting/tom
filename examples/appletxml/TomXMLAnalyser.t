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

package appletxml;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import java.io.*;

public class TomXMLAnalyser {

  %include{ adt/tnode/TNode.tom }

  public static void main (String args[]) {
    TomXMLAnalyser test = new TomXMLAnalyser();
    try {
      test.run(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java TomXMLAnalyser <Valid XML string>");
    }
  }
  
  public String run(String analysedString) {  
    if (analysedString != null && !analysedString.equals("")) {
      analysedString = analysedString.trim();
      try {
        XmlTools xtools = new XmlTools();
        TNode term = xtools.convertXMLToTNode(new ByteArrayInputStream(analysedString.getBytes()));
        String res = extractEMail(term.getDocElem());
        return res;
      } catch (Exception e) {
        StringWriter swriter = new StringWriter();
        PrintWriter writer = new PrintWriter(swriter);
        e.printStackTrace(writer);
        return "Exception catched: Enter a Valid XML string\n"+swriter;
      }
    } else {
      return "Enter a Valid XML string";
    }
  }

  private String extractEMail(TNode authors) {
    String res = "";
    %match(TNode authors) {
      <authors domain=thedomain> 
         <author firstname=fnval lastname=lnval />
         </authors> -> {
         res += `fnval+"."+`lnval+"@"+`thedomain+"\n";
       }
    }
    return res;
  }
}
