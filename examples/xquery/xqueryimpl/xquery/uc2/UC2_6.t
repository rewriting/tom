package xquery.uc2;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC2_6 {
    
  %include {TNode.tom}
  private XmlTools xtools;
 private GenericTraversal traversal = new GenericTraversal();

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _s = null;
  private TNode _section =null;
  

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC2_6 uc = new UC2_6();
	String filename;

	if (args.length == 0) {
	  filename="xquery/data/book.xml";
	}
	else {
	  filename=args[0];
	}

	uc.run(filename);
  }


  private void run(String filename) 
  {
	xtools = new XmlTools();

	_XmlDocument01 = (TNode)xtools.convertXMLToATerm(filename); 
	
	executeQuery(_XmlDocument01.getDocElem());
  }


 
  private void executeQuery(TNode documentNode) 
  {

	//	  _bookList = _collectData01(documentNode,"book");
	//System.out.println("<results>");
	System.out.println("<toc>");
	
	_forLoop01(documentNode);
	System.out.println("</toc>");
	
  }


  private void _forLoop01(TNode doc) 
  {
	%match (TNode doc) {
	  <book>section@<section></section></book> -> {
		_s = section; 
		_sectionSumary(_s);
	  }
	}
  }

  private void _forLoop02(TNode section) 
  {
	%match (TNode section) {
	  <section>nestedsection@<section></section></section> -> {
		 _sectionSumary(nestedsection);
	   }
	}
  }

  private void _sectionSumary(TNode section) 
  {
	%match (TNode section) {
	  nestedsection@<section></section> -> {
		 _section = nestedsection; 
		 _createResult01(_section);
	   }
	}
  }


  private void _createResult01(TNode section) 
  {
	System.out.println("<section " + _getAllAttributesOfElement(section) + ">");
	%match (TNode section) {
	  <section>title@<title></title></section> -> {
		 xtools.printXMLFromATerm(title);
		 System.out.println();
	   }
	}
	System.out.print("<figcount>");

	int count=0;
	%match (TNode section) {
	  <section><figure></figure></section> -> {
		 count++;
	   }
	}
	System.out.print(count);
	System.out.println("</figcount>");

	_forLoop02(section);

	System.out.println("</section>");
  }


  private String  _getAllAttributesOfElement(TNode element) 
  {
	TNodeList attrlist = element.getAttrList(); 
	String result="";
	for (int i= attrlist.getLength()-1;  i >=0 ; i--) {
	  TNode attr=attrlist.getTNodeAt(i);
	  //	  System.out.println(attr.getName());
	  
	  //	  result = result + attr.getName() + "\"" + attr.getChildList().getHead().getData() + "\""; 
	  result = result + attr.getName() + "=\"" + attr.getValue() + "\" "; 
	}
	return result;
  }


  protected TNodeList _collectData02(TNode subject,String clauseName) { 
	TNodeList nodeList = getTNodeFactory().makeTNodeList();
	
	%match (TNode subject) {
	  <_>tnode@<_></_></_> -> {
		 if (tnode.hasName() && (tnode.getName()==clauseName)) {
		   nodeList=nodeList.append(tnode);
		 }
	   } 
	}
	
	return nodeList; 
	//	return `xml(<dashdash> nodeList* </dashdash>);
  }
}