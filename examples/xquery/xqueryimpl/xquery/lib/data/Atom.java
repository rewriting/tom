//Source file: C:\\document\\codegen\\xquery\\lib\\data\\Atom.java

package xquery.lib.data;

import xquery.lib.data.type.XQueryAnyAtomicType;

import xquery.lib.data.type.XQueryString;
import xquery.lib.data.type.XQueryTypeException;




public class Atom 
{
  protected java.lang.String value = "";
  protected XQueryAnyAtomicType type;
   
  /**
   * @roseuid 4110B6750258
   */
  public Atom() 
  {
	value="";
	type=new XQueryAnyAtomicType();
  }
   
  /**
   * @param atomType
   * @param value
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410F97C201A3
   */
  public Atom(XQueryAnyAtomicType atomType, String value) throws XQueryTypeException 
  {
	if (!atomType.qualify(value)) {
	  throw new XQueryTypeException("Atom type failed"); 
	}
	else {
	  this.value = value;
	  this.type = atomType;
	}
  }
   
  /**
   * @param value
   * @roseuid 410F946E02F2
   */
  public Atom(String value) 
  {
	this.value = value;
	this.type = new XQueryAnyAtomicType();
  }
   
  /**
   * @return xquery.lib.data.type.String
   * @roseuid 410F8CD90193
   */
  public String getStringValue() 
  {
    return value;
  }
   
  /**
   * @return xquery.lib.data.type.AnyAtomicType
   * @roseuid 410F93930329
   */
  public XQueryAnyAtomicType getTypeObj() 
  {
	return type;
  }
   
  /**
   * @return int
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410F954000D7
   */
  public int getDecimalValue() throws XQueryTypeException 
  {
    try {
	  int val = Integer.parseInt(value); 
	  return val; 
	}
	catch (NumberFormatException e) {
	  throw new XQueryTypeException("Atom: cannot convert string: '" + value + "' to decimal value");
	}
  }
   
  /**
   * @return float
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410F958B0234
   */
  public float getFloatValue() throws XQueryTypeException 
  {
    return 0;
  }
   
  /**
   * @return double
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410F95960348
   */
  public double getDoubleValue() throws XQueryTypeException 
  {
    return 0;
  }
   
  /**
   * @param value
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410F976101DF
   */
  public void setValue(String value) throws XQueryTypeException 
  {
	
  }
   
  /**
   * @return int
   * @roseuid 410F983C0180
   */
  public XQueryAnyAtomicType getType() 
  {
    return type;
  }
   
   
  /**
   * @param type
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410F9A0B0013
   */
  public void setType(XQueryAnyAtomicType newtype) throws XQueryTypeException 
  {
	if (!newtype.qualify(value)) {
	  throw new XQueryTypeException("Atom type failed"); 
	}
	else {
	  this.type = newtype;
	}

  }
   
  /**
   * @return boolean
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410F9DD301B0
   */
  public boolean getBooleanValue() throws XQueryTypeException 
  {
	return true;
  }


  public boolean isTyped() 
  {
	XQueryAnyAtomicType atomtype=new XQueryAnyAtomicType(); 

	if (type.qualify(atomtype)) {
	  return false; 
	}
	else {
	  return true;
	}
  }
}
