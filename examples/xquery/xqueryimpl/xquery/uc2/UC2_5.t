package xquery.uc2;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC2_5 {
    
  %include {TNode.tom}
  private XmlTools xtools;
 private GenericTraversal traversal = new GenericTraversal();

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book

  private TNode _s =null;
  private TNodeList _sList=null;
  private TNodeList _fList=null;
  //  private TNodeList _figureList=null;
  

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC2_5 uc = new UC2_5();
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
	System.out.println("<section_list>");
	_sList=_collectData01(documentNode, "section");

	_forLoop01(_sList);
	System.out.println("</section_list>");
	
  }

  private void _forLoop01(TNodeList nodelist) 
  {
	%match (TNodeList nodelist) {
	  (_*, section@<section></section>, _*) -> {
		_s = section;
		_fList = _collectData02(_s, "figure");
		_createResult01(_s, _fList);
	  }
	}
  }

  private int _count(TNodeList nodelist) 
  {
	return nodelist.getLength();
  }
  
  private void _createResult01(TNode section, TNodeList figurelist) 
  {
	%match (TNode section) {
	  <section><title>#TEXT(title)</title></section> -> {
		 System.out.println("<section title = \"" + title + "\" figcount = \"" + _count(figurelist) + "\"/>" );
		 
	   }
	}
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
			
			if (tnode.isElementNode() && tnode.hasName() && (tnode.getName()==clauseName)) {
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