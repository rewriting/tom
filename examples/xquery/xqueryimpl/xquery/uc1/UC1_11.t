package xquery.uc1;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC1_11 {
    
  %include {TNode.tom}
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  
  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _xmlDocument01 = null;
  
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _b = null;
  private TNodeList _bList =null;

    

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_11 uc = new UC1_11();
	String filename;

	if (args.length == 0) {
	  filename="xquery/data/bib.xml";
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



	executeQuery(_xmlDocument01.getDocElem());
	

  }



  private void executeQuery(TNode documentNode) 
  {
	System.out.println("<bib>");
	  

	  // System.out.println(_bList);
	  _bList=_collectData01(documentNode);

	  _forLoop01(_bList);

	  _bList=_collectData02(documentNode);

	  _forLoop02(_bList);
	  System.out.println("</bib>");
	  
  }


  private void _forLoop01(TNodeList nodelist) 

  {
	%match (TNodeList nodelist) {
	  (_*, aBook@<book></book>, _*) -> {
		 _b=aBook;
		 _createResult01(_b);
	   }
	}
  }


  private void _forLoop02(TNodeList nodelist) 
  {
	%match (TNodeList nodelist) {
	  (_*, aBook@<book></book>, _*) -> {
		 _b=aBook;
		 _createResult02(_b);
	   }
	}
  }

  private void _createResult01(TNode t) 
  {
	System.out.print("<book>");

	%match (TNode t) {
	  <book>title@<title></title></book> -> {
		 xtools.printXMLFromATerm(title);
		 System.out.println();
		 %match (TNode t) {
		   <book>author@<author></author></book> -> {
			  
			  xtools.printXMLFromATerm(author);
			  System.out.println();
			}
		 }
	   }
	}
	System.out.println("</book>");
  }

  private void _createResult02(TNode t) 
  {
	System.out.print("<book>");

	%match (TNode t) {
	  <book>title@<title></title>editor@<editor></editor></book> -> {
		 xtools.printXMLFromATerm(title);
		 System.out.println();
		 xtools.printXMLFromATerm(editor);
		 System.out.println();
		 
	   }
	}
	System.out.println("</book>");
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
						
			%match (TNode t) {
			  <book>tChild@<author></author></book> -> {
				 vector.add(t);
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
	  nodeList= nodeList.insert(n);
	}			   		
	return nodeList; 
  }

  

  protected TNodeList _collectData02(TNode subject) { 
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
						
			%match (TNode t) {
			  <book>tChild@<editor></editor></book> -> {
				 vector.add(t);
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
	  nodeList= nodeList.insert(n);
	}			   		
	return nodeList; 
	//	return `xml(<dashdash> nodeList* </dashdash>);
  }
}
