package xquery.util; 


import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*;
import jtom.runtime.*;


public class SequenceTool {
  public static int count(Sequence seq)
  {
	return seq.size(); 
  }


  public void sort(QueryRecordSet queryRecordSet, Comparator comparator) 
	throws XQueryGeneralException
  {
	
	Object recordArray[]=queryRecordSet.toArray(); 
	
	Arrays.sort(recordArray, comparator);
	
	queryRecordSet.clear();
	queryRecordSet.addAll(recordArray);
  }

  
  public boolean contain(Sequence seq, Object obj, Comparator comparator) 
  {
	Enumeration enum = seq.elements(); 
	
	while (enum.hasMoreElements()) {
	  if (comparator.compare(enum.nextElement(), obj) ==0 ) {
		return true; 
	  }
	}
	return false; 
  }



  public  boolean empty(Sequence seq) 
  {
	return seq.size()==0; 
  }

  public boolean existes(Sequence seq)
  {
	return seq.size()!=0;
  }

  public   int indexOf(Sequence seq, Object obj) 
  {
	return seq.indexOf(obj);
  }

  public   boolean insertBefore(TNode item, int position) 
  {
	return false; 
  }

  
  public   boolean remove(Sequence seq, TNode item)
  {
	return false;
  }

  public   Sequence reverse(Sequence seq) 
  {
	return seq; 
  }

  public   Sequence subSequence(Sequence seq, int start, int end) 
  {
	return seq; 
  }
  
  public   void unordered(Sequence seq)
  {
	return ; 
  }
}