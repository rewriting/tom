package xquery.uc2;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC2_2 {
    
  %include {TNode.tom}
  private XmlTools xtools;
 private GenericTraversal traversal = new GenericTraversal();

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _f = null;
  private TNodeList _fList=null;
  

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC2_2 uc = new UC2_2();
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
	System.out.println("<figlist>");
	_fList=_collectData01(documentNode,"figure");
	_forLoop01(_fList);
	System.out.println("</figlist>");
	
  }


  private void _forLoop01(TNodeList nodelist) 
  {
	%match (TNodeList nodelist) {
	  (_*, figure@<figure></figure>, _*) -> {
		_f = figure; 
		_createResult01(_f);
	  }
	}
  }


  private void _createResult01(TNode f) 
  {
	System.out.println("<figure " + _getAllAttributesOfElement(f) + ">");
	%match (TNode f) {
	  <figure>title@<title></title></figure> -> {
		 xtools.printXMLFromATerm(title);
		 System.out.println();
	   }
	}
	
	System.out.println("</figure>");
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


  protected TNodeList _collectData01(TNode subject,final String clauseName) { 
	final Vector vector=new Vector(); 

	Collect1 collect = new Collect1() { 
		public boolean apply(ATerm t) 
		{ 
		  if(t instanceof TNode) { 
			// %match(TNode t) { 
// 			  <author> </author> -> {
// 				 vector.add(t); 
// 				 return true; 
// 			   } 
// 			}
			TNode tnode=(TNode)t; 
			
			
			if (tnode.hasName() && (tnode.getName()==clauseName)) {
			  vector.add(t);
			  return true;
			}
		  } 
		  return true; 
		} // end apply 
	  }; // end new 
	traversal.genericCollect(subject, collect); 
	
	//	System.out.println(vector.size());
	
	TNodeList nodeList = getTNodeFactory().makeTNodeList();
	Enumeration e = vector.elements();
	while (e.hasMoreElements()) {
	  TNode n=(TNode)(e.nextElement());
	  //	  xtools.printXMLFromATerm(n);
	  //System.out.println("__");
	  nodeList= nodeList.append(n);
	}			   		

	return nodeList; 
	//	return `xml(<dashdash> nodeList* </dashdash>);
  }
}