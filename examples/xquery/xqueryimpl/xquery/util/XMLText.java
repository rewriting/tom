package xquery.util;



public class XMLText {
  public static String calculIndent(int indentlevel)
  {
	String indent = "";
	for (int i=0; i<indentlevel; i++) {
	  indent = indent + "  ";
	}
	return indent; 
  }



  public static String createCascadeXML(String openClause, String data, String closeClause, int indentLevel)
  {
	String indent = calculIndent(indentLevel); 
	String xmlString = "";
	xmlString = openClause + "\n";
	xmlString = xmlString + "  " + data + "\n"; 
	xmlString = xmlString + closeClause + "\n";
	xmlString = indentXMLBlock(xmlString, indentLevel);
	return xmlString; 
  }

  private static String indentXMLBlock(String xml, int indent) 
  {
	return xml;
  }

  public static String createXML(String openClause, String data, String closeClause, int indentLevel) 
  {
	String indent = calculIndent(indentLevel); 
	String xmlString = "";
	xmlString = indent + openClause;
	xmlString = xmlString + data; 
	xmlString = xmlString + closeClause + "\n";
	return xmlString; 
  }

}