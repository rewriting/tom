package xquery.util;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class MainNodeCount {
    
  %include {TNode.tom}
  private XmlTools xtools;

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	MainNodeCount uc = new MainNodeCount();
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

	TNode _XmlDocument01 = (TNode)xtools.convertXMLToATerm(filename); 
	int result; 
	  result = executeQuery(_XmlDocument01.getDocElem());

	  System.out.print("main node count:");
	  
	System.out.println(result);
  }


 
  private int executeQuery(TNode documentNode) 

  {
	int result =  _forLoop01(documentNode);
	  
	return result;
  }


  private int _forLoop01(TNode node) 
	
  {
	
	  int result=0;

	  %match (TNode node) {
		<_>aChildNode@<_></_></_> -> {
		   // assign value to class-scope variable _b																	
		   result ++;
		 }	   
	  }

	  return result;
	
  }
  

	
}