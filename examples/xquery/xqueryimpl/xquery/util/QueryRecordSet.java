package xquery.util; 


import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;


import java.util.*;

public class QueryRecordSet extends Sequence {
  
  protected int cursor=0; 

  public int getRecordCount() 
  {
	return this.size();
  }

  public QueryRecord getRecord(int recordPosition) 
  {
	if (this.size() <= recordPosition) {
	  return null; 
	}
	else {
	  return (QueryRecord)(this.elementAt(recordPosition));
	}
  }

//   public QueryRecord getNextRecord()
//   {
// 	return null;
//   }

//   public void seek(int amount, int fromPosition) 
//   {
	
//   }


  public void addRecord(QueryRecord record)
  {
	this.add(record);
  }

  public void addAll(Object objects[])
  {
	for (int i=0; i<objects.length; i++) {
	  this.add(objects[i]);
	}
  }
}

