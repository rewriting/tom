package tools;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import polygraphicprogram.*;
import polygraphicprogram.types.*;
import polygraphicprogram.types.onepath.*;
import polygraphicprogram.types.twopath.*;
import polygraphicprogram.types.threepath.*;
import tom.library.sl.*;
import java.io.*;
import java.util.Vector;
import java.util.Iterator;


public class ClassHandler {
  %include{ dom.tom }
  %include { polygraphicprogram/PolygraphicProgram.tom }
  %include { sl.tom }
  
public static String path="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/";

public static void main(String[] args) {
	makeClasses(path+"testprogramv2.xml");
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
			for (int j = oneC0s.getLength()-1; j > 0; j--) {
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

public static ThreePath makeThreeCell(Node node){
		NamedNodeMap attributes=node.getAttributes();
		String name=attributes.getNamedItem("Name").getNodeValue();
		String type=attributes.getNamedItem("Type").getNodeValue();
		CellType celltype=null;
		if(type.equals("Function")){celltype=`Function();}
		if(type.equals("Constructor")){celltype=`Constructor();}
		NodeList io=node.getChildNodes();
		TwoPath source=`TwoId(Id());
		TwoPath target=`TwoId(Id());
		for (int j = 0; j < io.getLength(); j++) {
			Node ioChild=io.item(j);
			String ioName =ioChild.getNodeName();
			if(ioName.equals("Source")){source=makeTwoPath(ioChild);}
			if(ioName.equals("Target")){target=makeTwoPath(ioChild);}
		}
		return `ThreeCell(name,source,target,celltype);
}

public static void save(String fileContent,File file) throws IOException {

		PrintWriter printWriter = new PrintWriter(new FileOutputStream(
				file));
		printWriter.print(fileContent);
		printWriter.flush();
		printWriter.close();
	}

%strategy Normalize() extends Identity(){ 
  	visit TwoPath {
  		TwoC1(TwoC0(head1*,TwoC1(top*),tail1*),TwoC0(head2*,bottom*,tail2*)) -> {if(`head1*.target()==`head2*.source()&&`top*.target()==`bottom*.source()){System.out.println("1");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top*,bottom*),TwoC1(tail1*,tail2*));}} 
  		TwoC1(TwoC0(head1*,top@TwoCell(_,_,_,_,_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("2");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head1*,top@TwoId(_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("3");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head*,TwoC1(top*),tail*),bottom*) -> {if(`top*.target()==`bottom*.source()){System.out.println("4");return `TwoC0(head*,TwoC1(top*,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@TwoCell(_,_,_,_,_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("5");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@TwoId(_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("6");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(head*,TwoC0(topleft*,top*,topright*),TwoC0(left*,f@TwoCell(_,_,_,Function(),_),right*),tail*) -> {//marche pas vraiment quand ya une fonction a plusieurs entrees dans y
  	  		if(`topleft*.target()==`left*.source()&&`top.target()==`f.source()){
  	  			TwoPath myNewPath=`TwoId(Id());
  	  		if(`head*!=`TwoId(Id())){myNewPath= `TwoC1(head*,TwoC0(TwoC1(topleft*,left*),TwoC1(top*,f),TwoC1(topright*,right*)),tail*);}
  	  		else{myNewPath= `TwoC1(TwoC0(TwoC1(topleft*,left*),TwoC1(top*,f),TwoC1(topright*,right*)),tail*);}
  	  		if(myNewPath!=`TwoId(Id())){
  	  		System.out.println("8");
  	  		return myNewPath;
  	  		}
  	  		}
  	  		}
 	  	TwoC1(head*,TwoC0(topleft*,top*,topright*),TwoC0(left*,TwoC1(topf*,f@TwoCell(_,_,_,_,_),right*)),tail*) -> {//marche pas vraiment quand ya une fonction a plusieurs entrees dans y
 	  		if(`topleft*.target()==`left*.source()&&`top.target()==`topf.source()){
  	  			TwoPath myNewPath=`TwoId(Id());
  	  		if(`head*!=`TwoId(Id())){myNewPath= `TwoC1(head*,TwoC0(TwoC1(topleft*,left*),TwoC1(top*,topf*,f),TwoC1(topright*,right*)),tail*);}
  	  		else{myNewPath= `TwoC1(TwoC0(TwoC1(topleft*,left*),TwoC1(top*,topf*,f),TwoC1(topright*,right*)),tail*);}
  	  		if(myNewPath!=`TwoId(Id())){
  	  		System.out.println("8bis");
  	  		return myNewPath;
  	  		}
  	  		}
  	  		}
  	  	TwoC1(head*,top,TwoC0(left*,X,right*),tail*) -> {if(`left*.source()==`Id()&&`right*.source()==`Id()&&`X.source()==`top.target()){	 
  	  		TwoPath myNewPath=`TwoId(Id());//peut etre verifier compatibilite de top et X?
  	  		if(`head*!=`TwoId(Id())){myNewPath=`TwoC1(head*,TwoC0(left*,TwoC1(top,X),right*),tail*);}else{myNewPath=`TwoC1(TwoC0(left*,TwoC1(top,X),right*),tail*);}
  	  			if(myNewPath!=`TwoId(Id())){
  	  		System.out.println("9");
  	  		return myNewPath;}
  	  	}}
  	  	p@TwoC1(head*,top@TwoC0(X*),down@TwoC0(Y*),f@TwoCell(_,_,_,Function(),_),tail*) -> {//extension du cas 7
  	  		int sourcelength=`f.sourcesize();
  	  		TwoPath myNewPath=`TwoId(Id());
  	  		int index=0;
  	  		if(sourcelength!=`down.length()){break;}
  	  		TwoPath[] topArray=toArray((TwoC0)`top);
  	  		TwoPath[] downArray=toArray((TwoC0)`down);
  	  		for(int i=0;i<sourcelength;i++){
  	  			
 	  			int downsourcelength=downArray[i].sourcesize();//nouveau
  	  				
   	  			TwoPath topPart=`TwoId(Id());
  	  			for(int j=index;j<downsourcelength+index;j++){
  	  				
  	  				try{TwoPath newC0 = (TwoPath)topArray[j];
  	  				
  	  				if(j==index){topPart=newC0;}
  	  			else {topPart=`TwoC0(topPart,newC0);}


  	  				}catch (Exception e){//cas ou il n y a pas que des constructeurs au dessus comme des cellules avec plusieurs sorties, duplication par exemple
  	  				return `p;
  	  				}
  	  			}
  	  			
  	  			index+=downsourcelength;
  	  			if(topPart.target()==downArray[i].source()){
  	  			TwoPath newC1=`TwoC1(topPart,downArray[i]);
  	  		
  	  			if(i==0){myNewPath=`newC1;}
  	  			else {myNewPath=`TwoC0(myNewPath,newC1);}
  	  			
  	  		}  	  			
  	  		}
  	  		if(myNewPath!=`TwoId(Id())){
  	  		if(`head!=`TwoId(Id())){
  	  		myNewPath=`TwoC1(head,myNewPath,f,tail);}
  	  		else{myNewPath=`TwoC1(myNewPath,f,tail);}}
  	  		if(myNewPath!=`TwoId(Id())){
  	  		
  	  		System.out.println("10");
  	  		return myNewPath;}
  	  		}
  	  		//a part, retransforme les onec0 en twoC0
  	  	TwoId(OneC0(head,tail*)) -> { System.out.println("onetotwo");return `TwoC0(TwoId(head),TwoId(tail*)); } //correction en mme temps
  	  	
  	  	TwoC1(head*,t@TwoId(_),TwoId(_),tail*) -> { if(`head!=`TwoId(Id())){return `TwoC1(head,t,tail);}else{return `TwoC1(t,tail);}}
 	 } 
}

%strategy Gravity() extends Identity(){ 
  	visit TwoPath {
  		TwoC1(head*,f@TwoCell(_,_,_,Constructor(),_),g@TwoId(_),tail*)->{
				if(`f.target()==`g.source()){
				if(`head*==`TwoId(Id())){
				if(`tail*==`TwoId(Id())){return `TwoC1(TwoId(f.source()),f);}
				return `TwoC1(TwoId(f.source()),f,tail*);
				}
				if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoId(f.source()),f);}
				System.out.println("GravityA");
				return `TwoC1(head*,TwoId(f.source()),f,tail*);
				}
}
  		TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor(),_),tail1*),TwoC0(head2*,g@TwoId(_),tail2*),tail*) -> { 

			if(`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`g.source()){
																											
				if(`head*==`TwoId(Id())){
					if(`tail*==`TwoId(Id())){return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
					System.out.println("GravityB1");
					return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
				}
				if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
				System.out.println("GravityB2");
				return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
			}
  	  }
  		TwoC1(head*,f@TwoCell(_,_,_,Constructor(),_),TwoC0(head2*,g@TwoId(_),tail2*),tail*) -> { 

			if(`f.target()==`g.source()){

																											
				if(`head*==`TwoId(Id())){
					if(`tail*==`TwoId(Id())){return `TwoC0(head2*,f,tail2*);}
					System.out.println("GravityC1");
					return `TwoC1(TwoC0(head2*,f,tail2*),tail*);
				}
				if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head2*,f,tail2*));}
				System.out.println("GravityC2");
				return `TwoC1(head*,TwoC0(head2*,f,tail2*),tail*);
			}
  	  }
  		  TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor(),_),tail1*),g@TwoId(_),tail*) -> { 

				if(`f.target()==`g.source()){
				if(`head*==`TwoId(Id())){
					if(`tail*==`TwoId(Id())){System.out.println("GravityD1");return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),f);}
					System.out.println("GravityD2");return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),f,tail*);
				}
				if(`tail*==`TwoId(Id())){System.out.println("GravityD3");return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),f);}
				System.out.println("GravityD4");return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),f,tail*);
			}
  	  }
  	} 
}

public static TwoPath[] toArray(TwoC0 twoc0) {
    int size = twoc0.length();
    TwoPath[] array = new TwoPath[size];
    int i=0;
    if(twoc0 instanceof ConsTwoC0) {
      TwoPath cur = twoc0;
      while(cur instanceof ConsTwoC0) {
        TwoPath elem = ((ConsTwoC0)cur).getHeadTwoC0();
        array[i] = elem;
        i++;
        cur = ((ConsTwoC0)cur).getTailTwoC0();
        
      }
      array[i] = cur;
    }
    return array;
  }

public static TwoPath formatPath(TwoPath path){
		try{path=(TwoPath) `RepeatId(Sequence(TopDown(Normalize()),TopDown(Gravity()))).visit(path);
			}
	catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("strange term: "+path);
    }
	return path;
}

public static String capitalize(String string){
return string.substring(0,1).toUpperCase()+string.substring(1);
}

public static String makeType(OneCell type)
{
String typeName=type.getName();
typeName=typeName.substring(0, 1).toUpperCase()+typeName.substring(1);
String typeClass=%[package polygraph;

abstract public class @typeName@ extends Constructors{

}
]%;
try{
save(typeClass,new File(path+"polygraph/"+typeName+".java"));}
catch(IOException e){e.printStackTrace();}
return typeName;
}

public static void makeConstructor(TwoCell constructor,String typeName)
{
String constructorName=constructor.getName();
constructorName=capitalize(constructorName);

String constructorClass=%[package polygraph;

public class @constructorName@ extends @typeName@{
	
	public @constructorName@(Port[] ports) {
		n_arg=@constructor.sourcesize()@;
		n_coarg=@constructor.targetsize()@;
		connect_up(ports);
		connect_down(@constructor.sourcesize()@);
		name = "n"+Counter.next();
		}

	public @constructorName@ clone(){
		return new @constructorName@(args);
	}
	
	public String toString(){
		return "@constructorName.toLowerCase()@";
	}

}
]%;
try{
save(constructorClass,new File(path+"polygraph/"+constructorName+".java"));}
catch(IOException e){e.printStackTrace();}
}

public static void makeFunction(TwoCell function,Vector<ThreeCell> rules)
{
String functionName=function.getName();
functionName=capitalize(functionName);

String functionClass=%[package polygraph;
import java.util.*;

public class @functionName@ extends Functions{
	
	public @functionName@(Port[] ports) {
		n_arg=@function.sourcesize()@;
		n_coarg=@function.targetsize()@;
		connect_up(ports);
		connect_down(@function.sourcesize()@);
		name = "n"+Counter.next();
		}

	public Set<Polygraph> compute() {
		HashSet<Polygraph> result = new HashSet<Polygraph>();
		
		@makeCompute(rules)@
		else{
			result.add(this);}
		return result;
	}

	public boolean isComputable(Set<Set<String>> redex) {

		return false;
	}

	public String toString(){
		return "@functionName.toLowerCase()@";
	}

}
]%;
/*
try{
save(functionClass,new File(path+"polygraph/"+functionName+".java"));}
catch(IOException e){e.printStackTrace();}*/
if(functionName.equals("Plus"))System.out.println(functionClass);
}

public static String makeCompute(Vector<ThreeCell> rules){
String compute="";
for (Iterator iterator = rules.iterator(); iterator.hasNext();) {
	ThreeCell rule = (ThreeCell) iterator.next();
	compute+=getSourcePattern(rule)+"\n\t\t";
	compute+=getTargetAction(rule)+"\n\t\t\t";

}
return compute;
}

public static String getSourcePattern(ThreeCell rule)
{
TwoPath source=rule.getSource();
source=formatPath(source);
%match (TwoPath source){
			TwoC1(TwoCell(name,_,_,_,_),TwoCell(_,_,_,Function(),_)) -> {  return "if(args[0].getIn() instanceof"+capitalize(`name)+")";}
			TwoC1(t@TwoC0(_*),TwoCell(_,_,_,Function(),_)) -> { 
			TwoPath[] functionSource=toArray((TwoC0)`t);
			String condition="if(";
			boolean first=true;
			for(int i=0;i<functionSource.length;i++){
			if(functionSource[i].isTwoCell()){
			if(first){first=false;condition+="args["+i+"] instanceof "+capitalize(functionSource[i].getName());}
			else{condition+="&&args["+i+"] instanceof "+capitalize(functionSource[i].getName()); }
			}
			}
			return condition+")";	
			}
		}
return "";
}

public static String getTargetAction(ThreeCell rule)
{
//to connect the cells of top and bottom parts
TwoPath target=rule.getTarget();
target=formatPath(target); 
%match (TwoPath target){
		TwoId(!Id()) -> {return "{"+"connect_down(coargs[0], 0);"+"}";}
		t@TwoCell(_,_,_,_,_) -> {
			String targetAction= cellAction((TwoCell)`t,"TODO",0)+"cell.getName()";
			for(int i=0;i<`t.targetsize();i++){
			 targetAction+=`t.getName()+0+".connectDown(coargs,"+i+")";}			
			return targetAction;
		} 
		TwoC1(top@TwoCell(_,_,_,_,_),middle*,bottom@TwoCell(_,_,_,_,_)) -> {
		String targetAction ="";
		targetAction+="Port[] ports"+0+"= new Port["+`top.sourcesize()+"]\n";
		for(int i=0;i<`top.sourcesize();i++){
		targetAction+="ports"+0+"["+i+"]";//= a completer
		}
		targetAction+=cellAction((TwoCell)`top,"ports0",0);
		String bottomPorts="";
		if(`middle==`TwoId(Id())){bottomPorts=`top.getName()+0+".coargs";}
		else{
			//TODO a partir de 2 comme compteur
			//et changer bottomports
		}
		targetAction+=cellAction((TwoCell)`bottom,bottomPorts,1);//pb if it not a cell...
		for(int i=0;i<`bottom.targetsize();i++){
			 targetAction+=`bottom.getName()+1+".connectDown(coargs,"+i+")";}	
		return targetAction;
		}

		TwoC1(t0@TwoC0(),c@TwoCell(_,_,_,_,_)) -> {
		String actionTarget="";
		actionTarget+=cellAction((TwoCell)`c,"TODO2",0);
		for(int i=0;i<`c.targetsize();i++){
			 actionTarget+=`c.getName()+0+".connectDown(coargs,"+i+")";}	
		}
		TwoC0(X*) -> {
			
			}
		}
return "";
}

public static String getTargetActionBis(TwoPath path,int counter){
//for the internal part
%match (TwoPath path){
	
}
return "";
}

public static String cellAction(TwoCell cell,String ports,int counter)
{
String action=capitalize(cell.getName())+" "+cell.getName()+counter+" = new "+capitalize(cell.getName())+"("+ports+")";
if(cell.getType().isFunction())action+="\nresult.add("+cell.getName()+counter+")";
return action+"\n";
}

public static void makeClasses(String filename){
		String strategy="";
		int n=0;
		Vector<OneCell> types=new Vector<OneCell>();
		Vector<TwoPath> constructors=new Vector<TwoPath>();
		Vector<ThreePath> structureRules=new Vector<ThreePath>();
	try{	
	Document dom = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder().parse(filename);
      Element e = dom.getDocumentElement();
      NodeList childs=e.getChildNodes();
      for (int i = 0; i < childs.getLength(); i++) {
	Node child = childs.item(i);
//recuperation des types et des constructeurs associes
if(child.getNodeName().equals("Type")){
String typeName="";
NodeList typeNodes=child.getChildNodes();
 for (int j = 0; j < typeNodes.getLength(); j++) {
	Node typeNode = typeNodes.item(j);
if(typeNode.getNodeName().equals("OnePath")){

typeName=makeType((OneCell)makeOnePath(typeNode));
}
if(typeNode.getNodeName().equals("Constructor")){
NodeList constructorNodes=typeNode.getChildNodes();
 for (int k = 0; k < constructorNodes.getLength(); k++) {
	Node constructorNode = constructorNodes.item(k);
if(!constructorNode.getNodeName().equals("#text")){

makeConstructor((TwoCell)makeTwoPath(constructorNode),typeName);
		}
}
}
}
}

//ajout des regles
if(child.getNodeName().equals("Function")){
NodeList functionNodes=child.getChildNodes();
TwoPath function=`TwoId(Id());
Vector<ThreeCell> rules= new Vector<ThreeCell>();
 for (int j = 0; j < functionNodes.getLength(); j++) {
	Node functionNode = functionNodes.item(j);
if(functionNode.getNodeName().equals("TwoPath")){
	function=(TwoCell)makeTwoPath(functionNode);
}
if(functionNode.getNodeName().equals("Rule")){
NodeList ruleNodes=functionNode.getChildNodes();
 for (int k = 0; k < ruleNodes.getLength(); k++) {
	Node ruleNode = ruleNodes.item(k);
if(!ruleNode.getNodeName().equals("#text")){
rules.add((ThreeCell)makeThreeCell(ruleNode));
		}
}
}
}
makeFunction((TwoCell)function,rules);
}

      }
/*
//construction des regles de structures a partir des types et constructeurs
for (Iterator iterator = constructors.iterator(); iterator.hasNext();) {
	TwoPath constructor = (TwoPath) iterator.next();
	Vector<ThreePath> constructorRules=StructureRuleHandler.makeStructureRules(constructor,types);
	for (Iterator iterator2 = constructorRules.iterator(); iterator2.hasNext();) {
		ThreePath rule = (ThreePath) iterator2.next();
		structureRules.add(rule);
	}
}

//ajout des regles de structure
for (Iterator iterator = structureRules.iterator(); iterator.hasNext();) {
	ThreePath rulePath = (ThreePath) iterator.next();
	String rule=makeRule(rulePath)+"\n";
//on enleve les noms dans la strategie

			 strategy+=%[%strategy ApplyRules@n@() extends Identity(){ 
  	visit TwoPath {
@rule@
  	}
}
  	]%;
		n++;
}

      //String evalStrategy="ApplyRules0()";
      String evalStrategy="RepeatId(TopDown(ApplyRules0())),MakeLog()";
      for(int i=1;i<n;i++){
    	 // evalStrategy+=",ApplyRules"+i+"()";
    	// evalStrategy+=",RepeatId(TopDown(ApplyRules"+i+"())),MakeLog()";
    	evalStrategy+=",TopDown(ApplyRules"+i+"()),MakeLog()";
      }
      			 strategy+=%[public static TwoPath eval(TwoPath myPath){
try{
System.out.println("BEFORE");
myPath.print();
System.out.println("LOG");//revoir l ordre encore une fois
myPath=(TwoPath) `RepeatId(Sequence(RepeatId(TopDown(Gravity())),RepeatId(TopDown(Normalize())),RepeatId(Sequence(@evalStrategy@,Print())))).visit(myPath);
//myPath=(TwoPath) `RepeatId(Sequence(RepeatId(TopDown(Gravity())),RepeatId(TopDown(Normalize())),RepeatId(Sequence(@evalStrategy@)))).visit(myPath);
System.out.println("RESULT");
myPath.print();
return myPath;
}
catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("strange term: " + myPath);
    }
}
  	]%;
  	*/
	}
	catch(Exception e){ e.printStackTrace();}
	 //return strategy;
}
	public static String getProgramName(String filename){
		String name="polygraphicprogram";
		try{		
		Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
		Element e = dom.getDocumentElement();
		name=e.getAttribute("Name");
		}
	catch(Exception e){ e.printStackTrace();}
	
      return name;
	}

}