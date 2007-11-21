package polygraphesnat;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import polygraphesnat.*;
import polygraphesnat.types.*;
import polygraphesnat.types.onepath.*;
import polygraphesnat.types.twopath.*;
import tom.library.sl.*;
import java.util.HashSet;
import java.util.Iterator;


public class polygraphicXML {
  private static Document dom;
  %include{ dom.tom }
  %include { polygraphesnat/PolygraphesNat.tom }
  %include { sl.tom }

  public static void main (String args[]) {
 try {
      dom = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder().parse("polygraphes/src/test.xml");
      Element e = dom.getDocumentElement();
      System.out.println(makeTwoPath(e));      


    } catch (Exception e) {
      e.printStackTrace();
    }
	

  }
public static OnePath makeOnePath(Node node){
NodeList childs=node.getChildNodes();
for (int i = 0; i < childs.getLength(); i++) {
	Node child = childs.item(i);
		String nodeName =child.getNodeName();
		// System.out.println(nodeName);
		if(nodeName.equals("OnePath")){
			return makeOnePath(child);
		}
		if(nodeName.equals("OneCell")){
			NamedNodeMap attributes=child.getAttributes();
				String name=attributes.getNamedItem("Name").getNodeValue();
				return `OneCell(name);
		}
		if(nodeName.equals("Id")){
			return `Id();
		}
		if(nodeName.equals("OneC0")){
			NodeList oneC0s=child.getChildNodes();
			HashSet<OnePath> oneC0list=new HashSet<OnePath>();
			for (int j = 0; j < oneC0s.getLength(); j++) {
				Node oneC0Element = oneC0s.item(j);
				if(!oneC0Element.getNodeName().contains("#text")){
					oneC0list.add(makeOnePath(oneC0Element));//marche pas
					//System.out.println(oneC0Element.getNodeName());
					//System.out.println(makeOnePath(oneC0Element));
					
					
					
				}
			//Object[] test=oneC0list.toArray();
			//System.out.println(test[0]);
			//System.out.println(oneC0list.getClass());
			//System.out.println(oneC0list.toArray().getClass());
			//((OneC0)c0).fromArray((OnePath[])oneC0list.toArray());
			//return OneC0.fromArray((OnePath[])oneC0list.toArray());

			}
		}

}
return `Id();
}

public static TwoPath makeTwoPath(Node node){
NodeList childs=node.getChildNodes();
for (int i = 0; i < childs.getLength(); i++) {
	Node child = childs.item(i);
		String nodeName =child.getNodeName();
		// System.out.println(nodeName);
		if(nodeName.equals("TwoCell")){
				NamedNodeMap attributes=child.getAttributes();
				String name=attributes.getNamedItem("Name").getNodeValue();
				String type=attributes.getNamedItem("Type").getNodeValue();
		CellType celltype=null;
		if(type.equals("Function")){celltype=`Function();}
		if(type.equals("Constructor")){celltype=`Constructor();}
		NodeList io=child.getChildNodes();
		OnePath source=`Id();
		OnePath target=`Id();
		for (int j = 0; j < io.getLength(); j++) {
			Node ioChild=io.item(j);
			String ioName =ioChild.getNodeName();
			if(ioName.equals("Source")){source=makeOnePath(ioChild);}
			if(ioName.equals("Target")){target=makeOnePath(ioChild);}
		}
		// System.out.println(name);System.out.println(source);System.out.println(target);System.out.println(celltype);
		return `TwoCell(name,source,target,celltype);
		}	
}
return `TwoId(Id());
}

}