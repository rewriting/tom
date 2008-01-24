package tools;

import polygraphicprogram.*;
import polygraphicprogram.types.*;
import polygraphicprogram.types.twopath.*;
import tom.library.sl.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
/*import java.util.Vector;
import java.util.HashSet;
import java.util.Iterator;*/

public class ReadOutput{

%include { polygraphicprogram/PolygraphicProgram.tom }
%include { sl.tom }
%include{ dom.tom }

public static String path="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/";

public static void main(String[] args) {
System.out.println("RESULT:\n----------------------------");
System.out.println(resultList(path+"XMLoutput.xml"));
System.out.println("----------------------------");
}

public static OnePath makeOnePath(Node node){
String nodeName =node.getNodeName();
		// System.out.println(nodeName);
		if(nodeName.equals("OnePath")){
			NodeList nodeChilds=node.getChildNodes();
			for (int i = 0; i < nodeChilds.getLength(); i++) {
				Node nodeChild=nodeChilds.item(i);
				if(!nodeChild.getNodeName().equals("#text")){return makeOnePath(nodeChild);}
			}
		}
		if(nodeName.equals("OneCell")){
			NamedNodeMap attributes=node.getAttributes();
				String name=attributes.getNamedItem("Name").getNodeValue();
				return `OneCell(name);
		}
		/*
		 * if(nodeName.equals("Id")){ return `Id(); }
		 */// useless
		if(nodeName.equals("OneC0")){
			NodeList oneC0s=node.getChildNodes();
			OnePath res=`Id();
			for (int j =oneC0s.getLength()-1 ; j > 0; j--) {
				Node oneC0Element = oneC0s.item(j);
				if(!oneC0Element.getNodeName().contains("#text")){
					res=`OneC0(makeOnePath(oneC0Element),res);
				}				
			}		
			return res;
		}
NodeList childs=node.getChildNodes();
for (int i = 0; i < childs.getLength(); i++) {
	Node child = childs.item(i);
		if(!child.getNodeName().equals("#text")){
			return makeOnePath(child);}
}
return `Id();
}

public static TwoPath makeTwoPath(Node node){
String nodeName =node.getNodeName();
		if(nodeName.equals("TwoPath")){// en prevision des 3-cellules
			NodeList nodeChilds=node.getChildNodes();
			for (int i = 0; i < nodeChilds.getLength(); i++) {
				Node nodeChild=nodeChilds.item(i);
				if(!nodeChild.getNodeName().equals("#text")){return makeTwoPath(nodeChild);}
			}
		}
		if(nodeName.equals("TwoId")){
			NodeList nodeChilds=node.getChildNodes();
			for (int i = 0; i < nodeChilds.getLength(); i++) {
				Node nodeChild=nodeChilds.item(i);
				if(!nodeChild.getNodeName().equals("#text")){return `TwoId(makeOnePath(nodeChild));}
			}
		}
		if(nodeName.equals("TwoCell")){
				NamedNodeMap attributes=node.getAttributes();
				String name=attributes.getNamedItem("Name").getNodeValue();
				String type=attributes.getNamedItem("Type").getNodeValue();
		CellType celltype=null;
		if(type.equals("Function")){celltype=`Function();}
		if(type.equals("Constructor")){celltype=`Constructor();}
		NodeList io=node.getChildNodes();
		OnePath source=`Id();
		OnePath target=`Id();
		for (int j = 0; j < io.getLength(); j++) {
			Node ioChild=io.item(j);
			String ioName =ioChild.getNodeName();
			if(ioName.equals("Source")){source=makeOnePath(ioChild);}
			if(ioName.equals("Target")){target=makeOnePath(ioChild);}
		}
		return `TwoCell(name,source,target,celltype,0);
		}
		if(nodeName.equals("TwoC0")){
			NodeList twoC0s=node.getChildNodes();
			TwoPath res=`TwoId(Id());
			for (int j = twoC0s.getLength()-1; j >0; j--) {
				Node twoC0Element = twoC0s.item(j);
				if(!twoC0Element.getNodeName().contains("#text")){
					// System.out.println(twoC0Element.getNodeName());
					res=`TwoC0(makeTwoPath(twoC0Element),res);
				}				
			}
			// System.out.println(res);
			return res;
		}
		if(nodeName.equals("TwoC1")){
			NodeList twoC1s=node.getChildNodes();
			TwoPath res=`TwoId(Id());
			for (int j = twoC1s.getLength()-1; j >0; j--) {
				Node twoC1Element = twoC1s.item(j);
				if(!twoC1Element.getNodeName().contains("#text")){
					if(res==`TwoId(Id())){res=`makeTwoPath(twoC1Element);}
					else{
					res=`TwoC1(makeTwoPath(twoC1Element),res);
					}
				}				
			}		
			return res;
		}
NodeList childs=node.getChildNodes();
for (int i = 0; i < childs.getLength(); i++) {
	Node child = childs.item(i);
	if(!child.getNodeName().equals("#text")){
		return makeTwoPath(child);
		}
}
return `TwoId(Id());
}

public static int resultNat(String filename){
TwoPath resultpath=`TwoId(Id());
try {

	Document dom = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder().parse(filename);
      Element e = dom.getDocumentElement();
resultpath=makeTwoPath(e);
return resultNat(resultpath);
}catch(Exception e){e.printStackTrace();}
return 0;
}
public static int resultNat(TwoPath resultat){
%match (TwoPath resultat){
			TwoId(Id()) -> { return 0; }
			TwoCell("zero",_,_,_,_) -> {return 0;}
			TwoCell("succ",_,_,_,_) -> {return 1;}
			TwoC1(TwoCell("zero",_,_,_,_),X*) -> {return resultNat(`X*);}
			TwoC1(TwoCell("succ",_,_,_,_),X*) -> {return 1+resultNat(`X*);}
			TwoC1(TwoCell("succ",_,_,_,_),TwoCell("succ",_,_,_,_)) -> {return 2; }
			}
System.out.println("RESULTAT NON CONFORME NAT");resultat.print();
return 0;
}

public static String resultList(String filename){
TwoPath resultpath=`TwoId(Id());
try {

	Document dom = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder().parse(filename);
      Element e = dom.getDocumentElement();
resultpath=makeTwoPath(e);
return resultList(resultpath);
}catch(Exception e){e.printStackTrace();}
resultpath.print();
return "Error-->non implemented yet";
}

public static String resultList(TwoPath resultat){
%match (TwoPath resultat){
			TwoId(Id()) -> { return "0"; }
			TwoC1(TwoC0(TwoCell("consList",_,_,Constructor(),_)),TwoCell("add",_,_,_,_),NAT*) -> {return ""+resultNat(`NAT);}
			TwoC1(TwoC0(NAT*,LIST*),TwoCell("add",_,_,_,_)) -> {if(`NAT!=`TwoId(Id())){return resultNat(`NAT)+" "+resultList(`LIST);}}
			TwoC0(LeftSplit*,RightSplit*) -> {if (`LeftSplit!=`TwoId(Id())){return resultList(`LeftSplit)+ "|| " + resultList(`RightSplit);}}
			TwoCell("consList",_,_,Constructor(),_) -> {return "";}
			}
if(resultat.target()==`OneCell("nat")){return " "+resultNat(resultat);}
if(resultat.target()==`OneCell("boolean")){return " "+resultBool(resultat);}
System.out.println("RESULTAT NON CONFORME LIST");resultat.print();
//tom.library.utils.Viewer.display(resultat);
return "Error";
}

public static String resultBool(TwoPath resultat){
%match (TwoPath resultat){
			TwoCell(name,_,OneCell("boolean"),Constructor(),_) -> {return `name;}
			}
System.out.println("RESULTAT NON CONFORME BOOL");resultat.print();
//tom.library.utils.Viewer.display(resultat);
return "Error";
}

}