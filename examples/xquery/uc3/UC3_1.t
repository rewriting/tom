
// In the Procedure section of Report1, what Instruments were used in the second Incision?

// Solution in XQuery:

// for $s in doc("report1.xml")//section[section.title = "Procedure"]
// return ($s//incision)[2]/instrument

// Expected Result:

// <instrument>electrocautery</instrument>


import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 


public class UC3_1 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC3_1 uc1 = new UC3_1();
	uc1.run("report1.xml");
  }

  private void run(String xmlfile1) 
  {
	xtools = new XmlTools();

	TNode xmldocument1 = (TNode)xtools.convertXMLToATerm(xmlfile1); 

	String result = executeQuery(xmldocument1.getDocElem());
	System.out.println(result);	
  }

  private String executeQuery(TNode book) 
  {
	String result = "";
	int count=0;
	%match (TNode book) {
	  <report><section><section_title>#TEXT(sectiontitle)</section_title><section_content><incision>everything*</incision></section_content></section></report> -> 
	   {
		 if (sectiontitle=="Procedure") {
		   count++;
		   if (count==2) {
			 TNode n = `xml(<tmp> everything* </tmp>); 
			 result = getInstrument(n);
			 return result;
		   }
		 }
	   }
	}
	
	return result;
  }

  private String getInstrument(TNode n) {
	%match (TNode n) {
	  <tmp><instrument>#TEXT(instrument)</instrument></tmp> -> 
	   {
		 return createXML("<instrument>",instrument,"</instrument>",0);
	   }
	}
	return "";
  }


  private String calculIndent(int indentlevel)
  {
	String indent = "";
	for (int i=0; i<indentlevel; i++) {
	  indent = indent + "  ";
	}
	return indent; 
  }

  private String createCascadeXML(String openClause, String data, String closeClause, int indentLevel)
  {
	String indent = calculIndent(indentLevel); 
	String xmlString = "";
	xmlString = openClause + "\n";
	xmlString = xmlString + "  " + data + "\n"; 
	xmlString = xmlString + closeClause;
	xmlString = indentXMLBlock(xmlString, indentLevel);
	return xmlString; 
  }

  private String indentXMLBlock(String xml, int indent) 
  {
	return xml;
  }

  private String createXML(String openClause, String data, String closeClause, int indentLevel) 
  {
	String indent = calculIndent(indentLevel); 
	String xmlString = "";
	xmlString = openClause;
	xmlString = xmlString + data; 
	xmlString = xmlString + closeClause ;
	xmlString = indentXMLBlock(xmlString, indentLevel);
	return xmlString; 
  }
}



