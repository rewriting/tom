package xquery.util; 

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

public class ObjectComparison {
  
  %include {TNode.tom}

  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }
		
  public boolean isGreater(Object object, Object objectToCompare) 
	throws ObjectIncompatibleException 
  {
	try {
	  int val = compareObject(object, objectToCompare); 
	  if (val >0) { 
		return true; 
	  }
	  else {
		return false; 
	  }
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}
  }

  public boolean isEqual(Object object, Object objectToCompare) 
	throws ObjectIncompatibleException 
  {
	try {
	  int val = compareObject(object, objectToCompare); 
	  if (val == 0) { 
		return true; 
	  }
	  else {
		return false; 
	  }
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}	
  }

  public boolean isSmaller(Object object, Object objectToCompare) 
	throws ObjectIncompatibleException 
  {
	try {
	  int val = compareObject(object, objectToCompare); 
	  if (val < 0) { 
		return true; 
	  }
	  else {
		return false; 
	  }
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}
  }


  // return >0 if greater
  //        0 if equal
  //       <0 if smaller
  private int compareObject(Object object, Object objectToCompare) 
	throws ObjectIncompatibleException 
  {
	// compare String and String
	if ((object instanceof java.lang.String)
		&& (objectToCompare instanceof java.lang.String)) {
	  return compareStringAndString((java.lang.String)object, (java.lang.String)objectToCompare); 
	}
	// compare String and "int"
	else if ((object instanceof java.lang.String)
			 && (objectToCompare instanceof Integer)) {
	  return compareStringAndInt((java.lang.String)object, ((java.lang.Integer)objectToCompare).intValue()); 
	}
	// compare two TNode
	else if ((object instanceof TNode )
			 && (objectToCompare instanceof TNode)) {
	  return compareTNodeAndTNode((jtom.adt.tnode.types.TNode)object, (jtom.adt.tnode.types.TNode)objectToCompare);
	}

	else if ((object instanceof TNode )
			 && (objectToCompare instanceof String)) {
	  return compareTNodeAndString((jtom.adt.tnode.types.TNode)object, (java.lang.String)objectToCompare);
	}
				
	else if ((object instanceof TNode )
			 && (objectToCompare instanceof Integer)) {
	  return compareTNodeAndInt((jtom.adt.tnode.types.TNode)object, ((java.lang.Integer)objectToCompare).intValue());
	}
	else {
	  throw new ObjectIncompatibleException();
	}
  }


  private int compareStringAndInt(String string, int valueToCompare) 
  {
	int value = Integer.parseInt(string); 
	return value - valueToCompare;
  }


  private int compareStringAndString(String string, String stringToCompare) 
  {
	return string.compareTo(stringToCompare);
  }

  private int compareTNodeAndTNode(TNode object, TNode objectToCompare) 
  {
	return 1;  // now always greater
  }

  private int compareTNodeAndString(TNode object, String str) 
	throws ObjectIncompatibleException
  {
	//		System.out.println(object.getName());
	//	System.out.println(object.getValue());
	%match (TNode object) {
	  <a>#TEXT(text)</a> -> {
		 return compareStringAndString(text,str);
	   }
	   _ -> {
		 //					return 0;
		 throw new ObjectIncompatibleException();
	   }
	}
  }

  private int compareTNodeAndInt(TNode object, int value) 
	throws ObjectIncompatibleException
  {
				
	%match (TNode object) {
	  <a>#TEXT(text)</a> -> {
		 return compareStringAndInt(text,value);
	   }
	   _ -> {
		 throw new ObjectIncompatibleException();
	   }
	}

  }


  
}