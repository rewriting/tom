/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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


  public Sequence removeDuplicated(Sequence seq)
  {
	Sequence result = new Sequence(); 
	Object objs[]=seq.toArray(); 
	int ints[]=new int[objs.length];

	for(int i=0; i<objs.length; i++) {
	  ints[i]=0;
	}

	for (int i=0; i<objs.length; i++) {
	  Object obj1=objs[i];
	  for (int j=i+1; j<objs.length; j++) {
		Object obj2=objs[j];

		if (obj2==obj1) {
		  ints[j]=1;
		}
	  } // for nested
	} // for
	
	for(int i=0; i<objs.length; i++) {
	  if (ints[i]==1) {
		// nothing tod o
	  }
	  else {
		result.add(objs[i]);
	  }
	}
	
	return result;
  }

}
