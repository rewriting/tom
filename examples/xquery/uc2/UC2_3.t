// How many sections are in Book1, and how many figures?

// Solution in XQuery:

// <section_count>{ count(doc("book.xml")//section) }</section_count>, 
// <figure_count>{ count(doc("book.xml")//figure) }</figure_count> 

// Expected Result:

// <section_count>7</section_count>
// <figure_count>3</figure_count> 




import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 


public class UC2_3 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC2_3 uc1 = new UC2_3();
	uc1.run("book.xml");
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
	String result = "<figure_count>";
	int figurecount=0; 
	%match (TNode book) {
	  <book>(_*, node, _*)</book> -> 
	   {
		 //		 xtools = new XmlTools();
		 figurecount = figurecount + countFigure(node); 
		 //		 xtools.printXMLFromATerm(node);
	   }
	}
	result = result + figurecount + "</figure_count>\n";	


	result = result + "<section_count>";
	int sectioncount=0; 
	%match (TNode book) {
	  <book>(_*, node, _*)</book> -> 
	   {
		 //		 xtools = new XmlTools();
		 sectioncount = sectioncount + countSection(node); 
		 //		 xtools.printXMLFromATerm(node);
	   }
	}
	result = result + sectioncount + "</section_count>\n";	

	return result;
  }


  private int countFigure(TNode node) 
  {
	int result =0; 
	%match (TNode node) {
	  <figure></figure> -> 
	   {
		 result++;
	   }
	   <section><figure></figure></section> -> {
		 result++;
	   }
	   <section><section>other</section></section> -> {
		 result = result + countFigure(other);
	   }
	   _ -> {
		 result = result ;
	   }
	}
	//	System.out.println("deo hieu the nao");
	return result;
  }

  private int countSection(TNode node) 
  {
	int result =0; 
	%match (TNode node) {
	  <section></section> -> {
		 result++;
	   }
	   <section>nestedsection@<section>other*</section></section> -> {
		 result = result + countSection(nestedsection);
	   }
	}
	//	System.out.println("deo hieu the nao");
	return result;
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



