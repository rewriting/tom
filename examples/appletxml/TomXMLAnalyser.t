package appletxml;

import aterm.*;

import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import jtom.runtime.xml.*;

import java.io.*;

public class TomXMLAnalyser {

  %include{ TNode.tom }
  
  private XmlTools xtools;

  private Factory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

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
      try {
        xtools = new XmlTools();
        TNode term = (TNode)xtools.convertXMLToATerm(new ByteArrayInputStream(analysedString.getBytes()));
        String res = extractEMail(term.getDocElem());
        return res;
      } catch (Exception e) {
        return "Exception catched: Enter a Valid XML string";
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
         res += fnval+"."+lnval+"@"+thedomain+"\n";
       }
    }
    return res;
  }
}
