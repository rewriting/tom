package xquery.uc1;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC1_9 {
    
  %include {TNode.tom}
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  
  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _xmlDocument01 = null;
  
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _t = null;
  private TNodeList _tList =null;


    

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_9 uc = new UC1_9();
	String filename;

	if (args.length == 0) {
	  filename="xquery/data/books.xml";
	}
	else {
	  filename=args[0];
	}

	uc.run(filename);
  }


  private void run(String filename) 
  {
	xtools = new XmlTools();

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename); 

	try {
	  executeQuery(_xmlDocument01.getDocElem());
	}
	catch (ObjectIncompatibleException e) {
	  System.out.println("error message here");
	}
	

  }



  private void executeQuery(TNode documentNode) 
	throws ObjectIncompatibleException 
  {
	try {
	  System.out.println("<results>");
	  
	  _tList = _collectData01(documentNode);
	  // System.out.println(_bList);
	  _forLoop01(_tList);
	  System.out.println("</results>");
	  
	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop01(TNodeList nodelist) 
	throws ObjectIncompatibleException 
  {
	try {
	  
	  %match (TNodeList nodelist) {
		(_*, aTitle@<title></title>, _*) -> {
		   // assign value to class-scope variable _b																	
		   _t = aTitle;
		   
		  
		   if (_isContain(_t,"XML")) {
			 _createResult01(_t);
		   }
		 }	   
	  }

	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }

  
  private void _createResult01(TNode b) 
  {
	xtools.printXMLFromATerm(b);
	System.out.println();
	
  }
  

  private boolean _isContain(TNode node,  String str) 
	throws ObjectIncompatibleException
  {

	ObjectIncompatibleException e=new ObjectIncompatibleException();
	boolean result = false; 
	
	if (node.isTextNode()) {
	  String text = node.getData();
	  if (text.indexOf(str) != -1) {
		return true; 
	  }
	}

	else if (node.hasChildList()) {
	  TNodeList nodelist= node.getChildList();
	  int len = nodelist.getLength();
	  for(int i=0; i<len; i++) {
		TNode onenode = nodelist.getTNodeAt(i);
		if (_isContain(onenode, str)) 
		  return true; 
	  }
	}
	else {
	  return false;
	}

	return false;
  }


  protected TNodeList _collectData01(TNode subject) { 
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
			
			%match (TNode t) {
			  <chapter>tChild@<title></title></chapter> -> {
				 vector.add(tChild);
				 return true;
			   }

			   <section>tChild@<title></title></section> -> {
				 vector.add(tChild);
				 return true;
			   }
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