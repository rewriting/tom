package xquery.uc1;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 

import xquery.util.*;

import java.io.*; 

public class UC1_2 {

  %include {TNode.tom}
  private XmlTools xtools;

  private TNode _b=null;
  private TNode _t =null;
  private TNode _a=null;

  private TNode _xmlDocument01=null;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_2 uc = new UC1_2();
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

	//String result = "";
	try {
	  executeQuery(_xmlDocument01.getDocElem());
	}
	catch (ObjectIncompatibleException e) {
	  System.out.println("error message here");
	}
	
	//System.out.println(result);
  }


  
  private void executeQuery(TNode documentNode) 
	throws ObjectIncompatibleException 
  {
	try {
	  System.out.println("<results>");

	  _forLoop01( documentNode);

	  System.out.println("</results>");
	  
	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop01(TNode node) 
	throws ObjectIncompatibleException 
  {
	try {


	  %match (TNode node) {
		<bib>aBook@<book></book></bib> -> {
		   _b=aBook;
		   _forLoop02(_b);
		 }	   
	  }


	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop02(TNode node) 
	throws ObjectIncompatibleException 
  {
	try {


	  %match (TNode node) {
		<book>aTitle@<title></title></book> -> {
		   _t=aTitle;
		   _forLoop03(node);
		 }	   
	  }


	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }

  private void _forLoop03(TNode node) 
	throws ObjectIncompatibleException 
  {
	try {

	  %match (TNode node) {
		<book>aAuthor@<author></author></book> -> {
		   _a=aAuthor; 
		   if (_isMatched(null)) {
			 _createResult01(_t, _a);
		   }
		 }
	  }

	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private boolean _isMatched(TNode objectNode) 
	throws ObjectIncompatibleException 
  {
	ObjectComparison comparor = new ObjectComparison(); 
	// nothing to do;
	return true;
  }


  private void _createResult01(TNode title, TNode author)
  {
	System.out.println("<result>");
	xtools.printXMLFromATerm(title);
	System.out.println();
	
	xtools.printXMLFromATerm(author);
	System.out.println();
	
	System.out.println("</result>");
	// result = XMLText.createCascadeXML("<result>",
// 									  getXMLFromTNode(title) + "\n"
// 									  + getXMLFromTNode(author),
// 									  "</result>",
// 									  1);
// 	return result; 
  }
   

//   private String getXMLFromTNode(TNode node)
//   {
// 	//First, redirect System.out so we can retrieve it as a string.
// 	PrintStream defaultOut = System.out;
// 	ByteArrayOutputStream outArray = new ByteArrayOutputStream();
	
// 	System.setOut(new PrintStream(outArray));
 
// 	xtools.printXMLFromATerm(node);
// 	String result = outArray.toString();

// 	System.setOut(defaultOut);
// 	return result; 
//   } 	
}