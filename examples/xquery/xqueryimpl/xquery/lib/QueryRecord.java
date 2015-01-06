/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
//Source file: C:\\document\\codegen\\xquery\\lib\\QueryRecord.java

package xquery.lib;


public class QueryRecord 
{
  protected Object fields[];
   
  /**
   * @roseuid 4110B6310387
   */
  public QueryRecord() 
  {
	fields=null;
  }
   
  /**
   * @param objs[]
   * @roseuid 410F8AE102D8
   */
  public QueryRecord(Object objs[]) 
  {
	this.fields = new Object[objs.length];
	for (int  i=0; i< objs.length; i++) {
	  this.fields[i]=objs[i];
	}
  }
   
  /**
   * @param fieldCount
   * @roseuid 410F8A27015E
   */
  public QueryRecord(int fieldCount) 
  {
	fields = new Object[fieldCount];
  }
   
  /**
   * @param obj
   * @param fieldPosition
   * @return boolean
   * @roseuid 410F8A6500A9
   */
  public boolean setField(Object obj, int fieldPosition) 
  {
	if (fields.length <= fieldPosition) {
	  return false; 
	}
	 
	fields[fieldPosition]=obj;	 
	return true;
  }
   
  /**
   * @param fieldPosition
   * @return java.lang.Object
   * @roseuid 410F8AA6037E
   */
  public Object getField(int fieldPosition) 
  {
	if (fields.length <= fieldPosition) {
	  return null; 
	}
	else {
	  return fields[fieldPosition];
	}
  }


  public String toString()
  {
	String result ="";
	for(int i=0; i< fields.length; i++) {
	  result += fields[i].toString();
	}
	return result;
  }

}
