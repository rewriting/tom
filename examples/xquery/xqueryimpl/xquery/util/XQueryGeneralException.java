package xquery.util; 


public class XQueryGeneralException extends Exception {

  public XQueryGeneralException()
  {
	super();
  }
  public XQueryGeneralException(String message) 
  {
	super(message);
  }

  public XQueryGeneralException(String message, Throwable cause) 
  {
	super(message, cause);
  }

  public XQueryGeneralException(Throwable cause)
  {
	super(cause);
  }

}