//Source file: C:\\document\\codegen\\xquery\\lib\\QueryRecordSet.java

package xquery.lib;

import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryString;

public class QueryRecordSet extends Sequence 
{
  //  private String name;
   
  /**
   * @roseuid 4110B632000D
   */
  public QueryRecordSet() 
  {
    
  }
   
  /**
   * @return int
   * @roseuid 410F8B2903B8
   */
  public int getRecordCount() 
  {
	return this.size();
  }
   
  /**
   * @param position
   * @return xquery.lib.QueryRecord
   * @roseuid 410F8B3603AD
   */
  public QueryRecord getRecord(int position) 
  {
	if (this.size() <= position) {
	  return null; 
	}
	else {
	  return (QueryRecord)(this.elementAt(position));
	}
	
  }
   
  /**
   * @param record
   * @roseuid 410F8B7F0394
   */
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
