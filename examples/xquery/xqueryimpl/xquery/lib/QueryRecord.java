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
