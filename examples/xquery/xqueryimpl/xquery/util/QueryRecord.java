package xquery.util; 


import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;


import java.util.*;

public class QueryRecord  {
  
  protected Object fields[];
  
  public QueryRecord(int fieldCount)
  {
	fields = new Object[fieldCount];	
  }

  public QueryRecord(Object fields[])
  {
	this.fields = new Object[fields.length];
	for (int  i=0; i< fields.length; i++) {
	  this.fields[i]=fields[i];
	}
  }

  public void setField(Object obj, int fieldPosition) 
  {
	if (fields.length <= fieldPosition) {
	  return; 
	}

	fields[fieldPosition]=obj;
  }


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