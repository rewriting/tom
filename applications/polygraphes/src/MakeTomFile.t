package compiler;

//import tom.library.sl.*;
import compiler.XMLhandler;
import java.io.File;

public class MakeTomFile {

  public static void main (String args[]) {
	 String filename="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/src/testprogram.xml";
	 try{XMLhandler.save(makeTomFile(filename),new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/src/"+XMLhandler.getProgramName(filename)+".t"));}
	 catch(Exception e){e.printStackTrace();}
	 

}
public static String makeTomFile(String filename){
	 String programName=XMLhandler.getProgramName(filename);
	 String ruleStrategy=XMLhandler.makeRuleStrategy(filename);
	 String lowerProgramName=programName.toLowerCase();
	 System.out.println(ruleStrategy);
	 String tomFile=%[
import @lowerProgramName@.nat.types.*;
import @lowerProgramName@.nat.types.twopath.*;
import tom.library.sl.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.*;

public class @programName@{
	%include { sl.tom }
	%gom{ 
	module Nat
	imports String
	abstract syntax
OnePath = Id()
		| OneCell (Name:String)
		| OneC0 (OnePath*)
	
TwoPath = TwoId (onePath:OnePath)
		| TwoCell (Name:String,Source:OnePath,Target:OnePath,Type:CellType)
		| TwoC0 (TwoPath*)
		| TwoC1 (TwoPath*)

ThreePath = ThreeId (twoPath:TwoPath)//inutile
		| ThreeCell (Name:String,Source:TwoPath,Target:TwoPath,Type:CellType)
		| ThreeC0 (ThreePath*)//idem
		| ThreeC1 (ThreePath*)//idem
		| ThreeC2 (ThreePath*)//idem
		
CellType = Constructor()
		| Function()

OneC0:AU() { `Id() }

TwoC0:Free() {}
TwoC0:make_empty() { return `TwoId(Id()); }
TwoC0:make_insert(x,y) {
%match(x,y){
	TwoId(Id()),tail -> {return `tail; }
	head,TwoId(Id())  -> {return `head; }
	TwoC0(h,t),tail -> {return `TwoC0(h,TwoC0(t,tail)); }
	TwoC1(),g -> { return `g; }
	g,TwoC1() -> { return `g; }
	}
}

TwoC1:Free() {}
TwoC1:make_empty() { return `TwoId(Id()); }
TwoC1:make_insert(x,y) {
	if(y!=`TwoId(Id())&&x.target()!=y.source())
	{	System.out.println(x);//System.out.println(x.target());
		System.out.println(y);//System.out.println(y.source());
		throw new RuntimeException("composition of incompatible 2-Paths");
	}
%match(x,y){
	TwoId(Id()),tail -> {return `tail; }
	head,TwoId(Id())  -> {return `head; }
	TwoC1(h,t),tail -> {return `TwoC1(h,TwoC1(t,tail)); }
	TwoC0(),g -> { return `g; }
	g,TwoC0() -> { return `g; }
	}
}

ThreeC0:Free() {}
ThreeC0:make_empty() { return `ThreeId(TwoId(Id())); }
ThreeC0:make_insert(x,y) {
%match(x,y){
	ThreeId(TwoId(Id())),tail -> {return `tail; }
	head,ThreeId(TwoId(Id()))  -> {return `head; }
	ThreeC0(h,t),tail -> {return `ThreeC0(h,ThreeC0(t,tail)); }
	ThreeC1(),g -> { return `g; }
	g,ThreeC1() -> { return `g; }
	ThreeC2(),g -> { return `g; }
	g,ThreeC2() -> { return `g; }
	}
}

ThreeC1:Free() {}
ThreeC1:make_empty() { return `ThreeId(TwoId(Id())); }
ThreeC1:make_insert(x,y) {
	if(y!=`ThreeId(TwoId(Id()))&&x.source().target()!=y.source().source())
	{
		throw new RuntimeException("composition of incompatible 3-Paths");
	}
%match(x,y){
	ThreeId(TwoId(Id())),tail -> {return `tail; }
	head,ThreeId(TwoId(Id()))  -> {return `head; }
	ThreeC1(h,t),tail -> {return `ThreeC1(h,ThreeC1(t,tail)); }
	ThreeC0(),g -> { return `g; }
	g,ThreeC0() -> { return `g; }
	ThreeC2(),g -> { return `g; }
	g,ThreeC2() -> { return `g; }
	}
}

ThreeC2:Free() {}
ThreeC2:make_empty() { return `ThreeId(TwoId(Id())); }
ThreeC2:make_insert(x,y) {
	if(y!=`ThreeId(TwoId(Id()))&&x.target()!=y.source())
	{
		throw new RuntimeException("composition of incompatible 3-Paths");
	}
%match(x,y){
	ThreeId(TwoId(Id())),tail -> {return `tail; }
	head,ThreeId(TwoId(Id()))  -> {return `head; }
	ThreeC2(h,t),tail -> {return `ThreeC2(h,ThreeC2(t,tail)); }
	ThreeC0(),g -> { return `g; }
	g,ThreeC0() -> { return `g; }
	ThreeC1(),g -> { return `g; }
	g,ThreeC1() -> { return `g; }
	}
}

ThreeCell:make(name,source,target,type) {
	if(source.source()!=target.source()||source.target()!=target.target())
	{
	throw new RuntimeException ("three-cell unvalid, the source two-path and the target two-path should have the same one-source and one-target");	
	}
	else{
		realMake(name,source,target,type);
		}
}
sort OnePath:block(){
	public boolean defined(){return true;}
	public String prettyPrint(){
		%match (this){
			OneC0(left,right*) -> { return "OneC0("+`left.prettyPrint()+","+`OneC0(right*).prettyPrint()+")";}
		}
		return this.toString();
	}
	public String prettyPrintBis(){
		%match (this){
			o@@OneCell(_) -> { return `o.getName(); }
			OneC0(left,right*) -> { return `left.prettyPrintBis()+","+`OneC0(right*).prettyPrintBis();}
		}
		return this.toString();
	}
}

sort TwoPath:block(){
	public OnePath source(){
		%match (this){
					TwoId(X) -> { return `X; }
					TwoCell[Source=x] -> { return `x; }
					TwoC0(head,tail*) -> { return `OneC0(head.source(),tail*.source()); }
					TwoC1(head,tail*) -> { return `head.source(); }
		}
		//return `OneId();
		throw new tom.engine.exception.TomRuntimeException("strange term: "+this);
		}

	public OnePath target(){
		%match (this){
					TwoId(X) -> { return `X; }
					TwoCell[Target=x] -> { return `x; }
					TwoC0(head,tail*) -> { return `OneC0(head.target(),tail*.target()); }
					TwoC1(head*,tail) -> { return `tail.target(); }
		}
		//return `OneId();
		throw new tom.engine.exception.TomRuntimeException("strange term: "+this);
		}
	public boolean defined(){
		%match (this){
					TwoCell[] -> { return true; }
					TwoC0(_*) -> { return true; }
					TwoC1(head, tail*) -> { return `head.target()==`tail.source()&&`tail.defined();}
		}
		return false;
		}
	public int sourcesize(){
		OnePath source=this.source();
		%match(source){
			Id() -> { return 0; }
			OneCell(_) -> { return 1; }
			o@@OneC0(_*) -> { return `o.length(); }
		}
		return 0;
	}
	public  String prettyPrint (){
		%match (this){
					TwoC0(left,right*) -> { return "TwoC0("+`left.prettyPrint()+","+`TwoC0(right*).prettyPrint()+")";}
					TwoC1(left,right*) -> { return "TwoC1("+`left.prettyPrint()+","+`TwoC0(right*).prettyPrint()+")";}
		}
		return this.prettyPrintBis();
		}

	public  String prettyPrintBis(){
		%match (this){
						t@@TwoCell(_,_,_,_) -> { return `t.getName(); }
						TwoId(o@@OneCell(_)) -> { return `o.prettyPrintBis(); }
						TwoC0(left,right*) -> { return `left.prettyPrintBis()+","+`TwoC0(right*).prettyPrintBis();}
		}
		return this.toString();
		}
	public void print(){System.out.println(this.prettyPrint());}
	
}

sort ThreePath:block(){
	public OnePath oneSource(){
		%match (this){	
					ThreeId(X) -> { return `X.source(); }
					ThreeCell[Source=x] -> { return `x.source(); }
					ThreeC0(head,tail*) -> { return `OneC0(head.oneSource(),tail*.oneSource()); }
					ThreeC1(head,tail*) -> { return `head.oneSource(); }
					ThreeC2(head,tail*) -> { return `head.oneSource(); }
		}
		//return `OneId();
		throw new tom.engine.exception.TomRuntimeException("strange term: "+this);
		}

		public OnePath oneTarget(){
		%match (this){
					ThreeId(X) -> { return `X.target(); }
					ThreeCell[Target=x] -> { return `x.target(); }
					ThreeC0(head,tail*) -> { return `OneC0(head.oneTarget(),tail*.oneTarget()); }
					ThreeC1(head*,tail) -> { return `tail.oneTarget(); }
					ThreeC2(head,tail*) -> { return `head.oneTarget(); }
		}
		//return `OneId();
		throw new tom.engine.exception.TomRuntimeException("strange term: "+this);
		}

		public TwoPath source(){
		%match (this){
					ThreeId(X) -> { return `X; }
					ThreeCell[Source=x] -> { return `x; }
					ThreeC0(head,tail*) -> { return `TwoC0(head.source(),tail*.source()); }
					ThreeC1(head,tail*) -> { return `TwoC1(head.source(),tail*.source()); }
					ThreeC2(head,tail*) -> { return `head.source(); }
		}
		//return `TwoId(OneId());
		throw new tom.engine.exception.TomRuntimeException("strange term: "+this);
		}

		public TwoPath target(){
		%match (this){
					ThreeId(X) -> { return `X; }
					ThreeCell[Target=x] -> { return `x; }
					ThreeC0(head,tail*) -> { return `TwoC0(head.target(),tail*.target()); }
					ThreeC1(head,tail*) -> { return `TwoC1(head.target(),tail*.target()); }
					ThreeC2(head*,tail) -> { return `tail.target(); }
		}
		//return `TwoId(OneId());
		throw new tom.engine.exception.TomRuntimeException("strange term: "+this);
		}
		
		public boolean defined(){
			%match (this){
						ThreeCell[] -> { return true; }
						ThreeC0(_*) -> { return true; }
						ThreeC1(ThreeCell[Source=source1],ThreeCell[Source=source2],tail*) -> { return `source1.target()==`source2.source()&&`tail.defined();}
						ThreeC2(ThreeCell[Target=target1],ThreeCell[Source=source2],tail*) -> { return `target1==`source2&&`tail.defined();}
				}
			return false;
			}
		public void print(){System.out.println("ThreeCell("+this.getName()+","+this.getSource().prettyPrint()+","+this.getTarget().prettyPrint()+")");}
		}
}




%strategy Normalize() extends Identity(){ 
  	visit TwoPath {
  		TwoC1(TwoC0(head1*,TwoC1(top*),tail1*),TwoC0(head2*,bottom*,tail2*)) -> {if(`head1*.target()==`head2*.source()&&`top*.target()==`bottom*.source()){System.out.println("1");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top*,bottom*),TwoC1(tail1*,tail2*));}} 
  		TwoC1(TwoC0(head1*,top@@TwoCell(_,_,_,_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("2");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head1*,top@@TwoId(_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("3");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head*,TwoC1(top*),tail*),bottom*) -> {if(`top*.target()==`bottom*.source()){System.out.println("4");return `TwoC0(head*,TwoC1(top*,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@@TwoCell(_,_,_,_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("5");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@@TwoId(_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("6");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(head*,TwoC0(topleft*,top*,topright*),TwoC0(left*,f@@TwoCell(_,_,_,Function()),right*),tail*) -> {//marche pas vraiment quand ya une fonction a plusieurs entrees dans y
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
  	  	TwoC1(head*,top,TwoC0(left*,X,right*),tail*) -> {if(`left*.source()==`Id()&&`right*.source()==`Id()&&`X.source()==`top.target()){	 
  	  		TwoPath myNewPath=`TwoId(Id());//peut etre verifier compatibilite de top et X?
  	  		if(`head*!=`TwoId(Id())){myNewPath=`TwoC1(head*,TwoC0(left*,TwoC1(top,X),right*),tail*);}else{myNewPath=`TwoC1(TwoC0(left*,TwoC1(top,X),right*),tail*);}
  	  			if(myNewPath!=`TwoId(Id())){
  	  		System.out.println("9");
  	  		return myNewPath;}
  	  	}}
  	  	TwoC1(head*,top@@TwoC0(X*),down@@TwoC0(Y*),f@@TwoCell(_,_,_,Function()),tail*) -> {//extension du cas 7
  	  		int sourcelength=`f.sourcesize();
  	  		TwoPath myNewPath=`TwoId(Id());
  	  		int index=0;
  	  		if(sourcelength!=`down.length()){break;}
  	  		TwoPath[] array=toArray((TwoC0)`top);
  	  		for(int i=0;i<sourcelength;i++){
  	  			int downsourcelength=`((TwoPath)down.getChildAt(i)).sourcesize();
  	  			
   	  			TwoPath topPart=`TwoId(Id());
  	  			for(int j=index;j<downsourcelength+index;j++){
  	  				
  	  				try{TwoPath newC0 = (TwoPath)array[j];
  	  				
  	  				if(j==index){topPart=newC0;}
  	  			else if(j==index+1){topPart=`TwoC0(topPart,newC0);}
  	  			else{topPart.setChildAt(j,newC0);}

  	  				}catch (ArrayIndexOutOfBoundsException e){//cas ou il n y a pas que des constructeurs au dessus, duplication par example
  	  				}
  	  			}
  	  			index=downsourcelength;
  	  			if(topPart.target()==`((TwoPath)down.getChildAt(i)).source()){
  	  			TwoPath newC1=`TwoC1(topPart,(TwoPath)down.getChildAt(i));
  	  			if(i==0){myNewPath=`newC1;}
  	  			else if(i==1){myNewPath=`TwoC0(myNewPath,newC1);}
  	  			else{myNewPath.setChildAt(i,newC1);}
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
  	  	
  	  	TwoC1(head*,t@@TwoId(_),TwoId(_),tail*) -> { if(`head!=`TwoId(Id())){return `TwoC1(head,t,tail);}else{return `TwoC1(t,tail);}}
 	 } 
}

%strategy Print() extends Identity(){
  	visit TwoPath {
  	  x -> { System.out.print("STEP ");`x.print(); } 
 	 } 
}

 public static void main (String args[]) {
 try {

	 Document dom = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder().parse("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/src/XMLinput.xml");
      Element e = dom.getDocumentElement();
      save(twoPath2XML(eval(makeTwoPath(e))),new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/src/XMLoutput.xml"));
    
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


%strategy Gravity() extends Identity(){ 
  	visit TwoPath {
  		TwoC1(head*,f@@TwoCell(_,_,_,Constructor()),g@@TwoId(_),tail*)->{
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
  		TwoC1(head*,TwoC0(head1*,f@@TwoCell(_,_,_,Constructor()),tail1*),TwoC0(head2*,g@@TwoId(_),tail2*),tail*) -> { 

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
  		TwoC1(head*,f@@TwoCell(_,_,_,Constructor()),TwoC0(head2*,g@@TwoId(_),tail2*),tail*) -> { 

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
  		  TwoC1(head*,TwoC0(head1*,f@@TwoCell(_,_,_,Constructor()),tail1*),g@@TwoId(_),tail*) -> { 

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
			for (int j = oneC0s.getLength()-1; j >0; j--) {
				Node oneC0Element = oneC0s.item(j);
				if(!oneC0Element.getNodeName().contains("#text")){
					res=`OneC0(res,makeOnePath(oneC0Element));
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
		return `TwoCell(name,source,target,celltype);
		}
		if(nodeName.equals("TwoC0")){
			NodeList twoC0s=node.getChildNodes();
			TwoPath res=`TwoId(Id());
			for (int j = twoC0s.getLength()-1; j >0; j--) {
				Node twoC0Element = twoC0s.item(j);
				if(!twoC0Element.getNodeName().contains("#text")){
					// System.out.println(twoC0Element.getNodeName());
					res=`TwoC0(res,makeTwoPath(twoC0Element));
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

public static String twoPath2XML(TwoPath path){
%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoPath>\n<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n</TwoPath>\n";}
			TwoCell(name,source,target,type) -> { return "<TwoPath>\n<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n</TwoPath>\n"; }
			TwoC0(head,tail*) -> {return "<TwoPath>\n<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n</TwoPath>\n";}
			TwoC1(head,tail*) -> {return "<TwoPath>\n<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n</TwoPath>\n";}
}
return "";
}
public static String twoC02XML(TwoPath path){
%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
			TwoC0(head,tail*) -> {return twoC02XML(`head)+twoC02XML(`tail);}
			TwoC1(head,tail*) -> {return "<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n";}
}
return "";
}
public static String twoC12XML(TwoPath path){
%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
			TwoC0(head,tail*) -> {return "<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n";}
			TwoC1(head,tail*) -> {return twoC12XML(`head)+twoC12XML(`tail);}
}
return "";
}
public static String onePath2XML(OnePath path){
%match (OnePath path){
			Id() -> {return "<OnePath>\n<Id></Id>\n</OnePath>\n";}
			OneCell(name) -> { return "<OnePath>\n<OneCell Name=\""+`name+"\"></OneCell>\n</OnePath>\n"; }
			OneC0(head,tail*)->{ return "<OnePath>\n<OneC0>\n"+oneC02XML(`head)+oneC02XML(`tail)+"</OneC0>\n</OnePath>\n";}
}
return "";
}
public static String oneC02XML(OnePath path){
%match (OnePath path){
			Id() -> {return "<Id></Id>\n";}
			OneCell(name) -> { return "<OneCell Name=\""+`name+"\"></OneCell>\n"; }
			OneC0(head,tail*)->{ return oneC02XML(`head)+oneC02XML(`tail);}
}
return "";
}

public static void save(String fileContent,File file) throws IOException {

		PrintWriter printWriter = new PrintWriter(new FileOutputStream(
				file));
		printWriter.print(fileContent);
		printWriter.flush();
		printWriter.close();
	}
//-----------------------------------------------------------------------------
// partie specifique
//-----------------------------------------------------------------------------
@ruleStrategy@

}]%;
	 return tomFile;

}
}