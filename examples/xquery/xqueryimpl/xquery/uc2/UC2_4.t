package xquery.uc2;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC2_4 {
    
  %include {TNode.tom}
  private XmlTools xtools;
 private GenericTraversal traversal = new GenericTraversal();

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book


  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC2_4 uc = new UC2_4();
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
	int count=0;
	%match(TNode documentNode) {
	  
	  <book>section@<section></section></book> ->{
		 count ++; 
	   }
	}
	
	_createResult01(count);
  }

  private void _createResult01(int sectionCount) 
  {
	System.out.print("<top_section_count>");
	System.out.print(sectionCount);
	System.out.println("</top_section_count>");
  }

}