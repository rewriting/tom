// List the item number and description of all bicycles that currently have an auction in progress, ordered by item number.

// Solution in XQuery:

// <result>
//   {
//     for $i in doc("items.xml")//item_tuple
//     where $i/start_date <= current-date()
//       and $i/end_date >= current-date() 
//       and contains($i/description, "Bicycle")
//     order by $i/itemno
//     return
//         <item_tuple>
//             { $i/itemno }
//             { $i/description }
//         </item_tuple>
//   }
// </result>

// Note:

// This solution assumes that the current date is 1999-01-31.

// Expected Result:

// <result>
//     <item_tuple>
//         <itemno>1003</itemno>
//         <description>Old Bicycle</description>
//     </item_tuple>
//     <item_tuple>
//         <itemno>1007</itemno>
//         <description>Racing Bicycle</description>
//     </item_tuple>
// </result>




import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 


public class UC4_1 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC4_1 uc1 = new UC4_1();
	uc1.run("items.xml");
  }

  private void run(String xmlfile1) 
  {
	xtools = new XmlTools();

	TNode xmldocument1 = (TNode)xtools.convertXMLToATerm(xmlfile1); 

	String result = executeQuery(xmldocument1.getDocElem(), new Date());
	System.out.println(result);	

	getDate("1999-11-11"); 
  }

  private String executeQuery(TNode book, Date date) 
  {
	String result = "<result>";
	int count=0;
	Collection a = new Collection();

	%match (TNode book) {
	  <items>(_*, item, _*)</items> -> 
	   {
		 a.add(createItem(item, date));
		 System.out.println(book.text());
	   }
	}

	result = result + "</result>";

	return result;
  }

  private Date getDate(String s) {
	%match (String s) {
	  (year*, 'a', month* ,'a',day*) -> {
		System.out.println(year); 
	  }
	}
	return null;
  }

  private TNode createItem(TNode item, Date date) 
  {
	String result = "";

	%match (TNode item) {
	  <item_tuple><itemno>#TEXT(itemno)</itemno><description>#TEXT(description)</description>
		 <start_date>#TEXT(startdate)</start_date><end_date>#TEXT(enddate)</end_date></item_tuple> -> 
	   {
		 return null;
	   }
	}
	
	return null; 
  }


  private String calculIndent(int indentlevel)
  {
	String indent = "";
	for (int i=0; i<indentlevel; i++) {
	  indent = indent + "  ";
	}
	return indent; 
  }

  private String createCascadeXML(String openClause, String data, String closeClause, int indentLevel)
  {
	String indent = calculIndent(indentLevel); 
	String xmlString = "";
	xmlString = openClause + "\n";
	xmlString = xmlString + "  " + data + "\n"; 
	xmlString = xmlString + closeClause;
	xmlString = indentXMLBlock(xmlString, indentLevel);
	return xmlString; 
  }

  private String indentXMLBlock(String xml, int indent) 
  {
	return xml;
  }

  private String createXML(String openClause, String data, String closeClause, int indentLevel) 
  {
	String indent = calculIndent(indentLevel); 
	String xmlString = "";
	xmlString = openClause;
	xmlString = xmlString + data; 
	xmlString = xmlString + closeClause ;
	xmlString = indentXMLBlock(xmlString, indentLevel);
	return xmlString; 
  }
}



