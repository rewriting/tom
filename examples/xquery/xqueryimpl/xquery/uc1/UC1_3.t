package xquery.uc1;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 

import xquery.util.*;

import java.io.*; 

public class UC1_3 {

  %include {TNode.tom}
  private XmlTools xtools;

  private TNode _b = null;
  private TNode _xmlDocument01 =null;
		
  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_3 uc = new UC1_3();
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
		   if (_isMatched01(null)) {
			 _createResult01(_b);
		   }
		 }	   
	  }
						

	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private boolean _isMatched01(TNode objectNode) 
	throws ObjectIncompatibleException 
  {
	ObjectComparison comparor = new ObjectComparison(); 
	// nothing to do;
	return true;
  }


  private void _createResult01(TNode book)
  {
	System.out.println("<result>");
	

	%match (TNode book) {
	  <book>theTitle@<title></title></book> -> {
		 xtools.printXMLFromATerm(theTitle);
		 System.out.println();
		 
		 //result = result + getXMLFromTNode(theTitle);
		 %match (TNode book) {
		   <book>oneAuthor@<author></author></book> -> {
			  xtools.printXMLFromATerm(oneAuthor);
			  System.out.println();
			  
			}
		 }
	   }
	}
	System.out.println("</result>");
	// result = result + "</result>";
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