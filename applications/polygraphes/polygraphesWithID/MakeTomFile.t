package compiler;

import compiler.XMLProgramHandler;
import tools.XMLhandler;
import java.io.File;

//contains the entry point for the compiler
public class MakeTomFile {

	public static String path="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/";
	
	//entry point, get the whole program and saves it into a .t file
	public static void main (String args[]) {
		String filename=path+"testprogramv2.xml";
		try{XMLhandler.save(makeTomFile(filename),new File(path+XMLProgramHandler.getProgramName(filename)+".t"));}
		catch(Exception e){e.printStackTrace();}
	 }
	
	//constructs the program and particularly the common part of each program
	public static String makeTomFile(String filename){
		String programName=XMLProgramHandler.getProgramName(filename);
		String ruleStrategy=XMLProgramHandler.makeRuleStrategy(filename);//key point, gets the specific part of the file
		String lowerProgramName=programName.toLowerCase();
		//here lies the part that each program has in common : 
		String tomFile=%[
package myprogram;

import myprogram.@lowerProgramName@.polygraph.types.*;
import myprogram.@lowerProgramName@.polygraph.types.twopath.*;
import tom.library.sl.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.*;

public class @programName@{
	%include { sl.tom }
	//gom part
	%gom{
	module Polygraph
	imports int String
	abstract syntax
	
OnePath = Id() //means null, empty
		| OneCell (Name:String)
		| OneC0 (OnePath*)

	//IDs were added for a future purpose though they are not necessary to do the calculations : 
TwoPath = TwoId (onePath:OnePath) //to be able to cast wires as cells when needed
		| TwoCell (Name:String,Source:OnePath,Target:OnePath,Type:CellType,ID:int)
		| TwoC0 (TwoPath*)
		| TwoC1 (TwoPath*)

ThreePath = ThreeId (twoPath:TwoPath)//useless for the moment
		| ThreeCell (Name:String,Source:TwoPath,Target:TwoPath,Type:CellType)
		| ThreeC0 (ThreePath*)//useless
		| ThreeC1 (ThreePath*)//useless
		| ThreeC2 (ThreePath*)//useless

CellType = Constructor()
		| Function()

		
//HOOKS on variadic operators		

//ONEC0

//defines variadic operator OneC0 as associative and unitary with neutral element Id() 
OneC0:AU() { `Id() }


//TWOC0

//desactivates the default hook
TwoC0:Free() {}

//defines how to create an empty TwoC0
TwoC0:make_empty() { return `TwoId(Id()); }

//defines variadic operator TwoC0 as associative and unitary with neutral element TwoId(Id()) 
TwoC0:make_insert(x,y) {
%match(x,y){
	TwoId(Id()),tail -> {return `tail; }
	head,TwoId(Id())  -> {return `head; }
	TwoC0(h,t),tail -> {return `TwoC0(h,TwoC0(t,tail)); }
	TwoC1(),g -> { return `g; }
	g,TwoC1() -> { return `g; }
	}
}

//TWOC1

//desactivates the default hook
TwoC1:Free() {}

//defines how to create an empty TwoC0
TwoC1:make_empty() { return `TwoId(Id()); }

//defines variadic operator TwoC0 as associative and unitary with neutral element TwoId(Id()) 
TwoC1:make_insert(x,y) {
	//first we test if x and y are compatible for the *1 association
	if(y!=`TwoId(Id())&&x.target()!=y.source())
	{	//System.out.println(x);System.out.println(x.target());
		//System.out.println(y);System.out.println(y.source());
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

//the same for variadic operators for 3-paths which we never use
//useless for the use we have of them now

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

//verifies if a three-cell is valid before creating it
ThreeCell:make(name,source,target,type) {
	if(source.source()!=target.source()||source.target()!=target.target())
	{
	throw new RuntimeException ("three-cell unvalid, the source two-path and the target two-path should have the same one-source and one-target");	
	}
	else{
		realMake(name,source,target,type);
		}
}



//METHODS for polygraphic terms

//set of methods for 1-paths
sort OnePath:block(){
	
	//returns true if the path is valid (=well-constructed) (always true for a 1-path)
	//actually useless with our custom make methods
	public boolean defined(){return true;}
	
	//methods to print 1-path in a more "readable" way
	//top level
	public String prettyPrint(){
		%match (this){
			OneC0(left,right*) -> { return "OneC0("+`left.prettyPrint()+","+`OneC0(right*).prettyPrint()+")";}
		}
		return this.toString();
	}
	//other levels
	public String prettyPrintBis(){
		%match (this){
			o@@OneCell(_) -> { return `o.getName(); }
			OneC0(left,right*) -> { return `left.prettyPrintBis()+","+`OneC0(right*).prettyPrintBis();}
		}
		return this.toString();
	}
	//called method
	public void print(){System.out.println(this.prettyPrint());}
}

//set of methods for 2-paths
sort TwoPath:block(){
	
	//returns the source 1-Path of any 2-Path
	public OnePath source(){
		%match (this){
					TwoId(X) -> { return `X; }
					TwoCell[Source=x] -> { return `x; }
					TwoC0(head,tail*) -> { return `OneC0(head.source(),tail*.source()); }
					TwoC1(head,tail*) -> { return `head.source(); }
		}
		//return `OneId();//actually useless
		throw new tom.engine.exception.TomRuntimeException("strange term: "+this);
		}

	//returns the target 1-Path of any 2-Path
	public OnePath target(){
		%match (this){
					TwoId(X) -> { return `X; }
					TwoCell[Target=x] -> { return `x; }
					TwoC0(head,tail*) -> { return `OneC0(head.target(),tail*.target()); }
					TwoC1(head*,tail) -> { return `tail.target(); }
		}
		//return `OneId();//actually useless
		throw new tom.engine.exception.TomRuntimeException("strange term: "+this);
		}
	
	//returns true if the path is valid (=well-constructed)
	//actually useless with our custom make methods
	public boolean defined(){
		%match (this){
					TwoCell[] -> { return true; }
					TwoC0(_*) -> { return true; }
					TwoC1(head, tail*) -> { return `head.target()==`tail.source()&&`tail.defined();}
		}
		return false;
		}
	
	//return the size of the source defined as the number of 1-Cells in the input
	public int sourcesize(){
		OnePath source=this.source();
		%match(source){
			Id() -> { return 0; }
			OneCell(_) -> { return 1; }
			o@@OneC0(_*) -> { return `o.length(); }
		}
		return 0;
	}
	
	//return the size of the target defined as the number of 1-Cells in the output
	public int targetsize(){
		OnePath target=this.target();
		%match(target){
			Id() -> { return 0; }
			OneCell(_) -> { return 1; }
			o@@OneC0(_*) -> { return `o.length(); }
		}
		return 0;
	}
	
	//methods to print 2-path in a more "readable" way
	//top level
	public  String prettyPrint (){
		%match (this){
					TwoC0(left,right*) -> { return "TwoC0("+`left.prettyPrint()+","+`TwoC0(right*).prettyPrint()+")";}
					TwoC1(left,right*) -> { return "TwoC1("+`left.prettyPrint()+","+`TwoC0(right*).prettyPrint()+")";}
		}
		return this.prettyPrintBis();
		}
	//other levels
	public  String prettyPrintBis(){
		%match (this){
						t@@TwoCell(_,_,_,_,_) -> { return `t.getName(); }
						TwoId(o@@OneCell(_)) -> { return `o.prettyPrintBis(); }
						TwoC0(left,right*) -> { return `left.prettyPrintBis()+","+`TwoC0(right*).prettyPrintBis();}
		}
		return this.toString();
		}
	//called method
	public void print(){System.out.println(this.prettyPrint());}
}


//set of methods for 3-paths (never used in this project except for the print method)
sort ThreePath:block(){
	
	//returns the source 1-Path of any 3-Path (same 1-source and 1-target for the source and the target of a 3-Cell) 
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

	//returns the source 1-Path of any 3-Path (same 1-source and 1-target for the source and the target of a 3-Cell) 
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
	
	//returns the source 2-Path of any 3-Path
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

	//returns the target 2-Path of any 3-Path
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
	
	//returns true if the path is valid (=well-constructed)
	//actually useless with our custom make methods
	public boolean defined(){
			%match (this){
						ThreeCell[] -> { return true; }
						ThreeC0(_*) -> { return true; }
						ThreeC1(ThreeCell[Source=source1],ThreeCell[Source=source2],tail*) -> { return `source1.target()==`source2.source()&&`tail.defined();}
						ThreeC2(ThreeCell[Target=target1],ThreeCell[Source=source2],tail*) -> { return `target1==`source2&&`tail.defined();}
				}
			return false;
		}
	
	//print method
	public void print(){System.out.println("ThreeCell("+this.getName()+","+this.getSource().prettyPrint()+","+this.getTarget().prettyPrint()+")");}
		
}
}


	public static void main (String args[]) {
		try {
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/XMLinput.xml");
			Element e = dom.getDocumentElement();
			TwoPath input=makeTwoPath(e);
			updateLogPath(input);
			//saves the new twopath in another xml file
			save(twoPath2XML(eval(input)),new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/XMLoutput.xml"));
			//saves the history
			save(log+"</Log>",new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/log.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//counter for IDs
	private static int idCounter=0;
	
	//getter&&increment
	private static int setID(){
		return idCounter++;
	}

	//counter for number of rewritting steps
	private static int stepCounter=0;

	private static int getStep() {
		return stepCounter++;
	}
	
	//String for the log
	private static String log="<Log>\n";

	private static void updateLogFile(String update) {
		log+=update;
	}
	
	//used to write the different steps in the log
	private static TwoPath logPath=`TwoId(Id());

	private static void updateLogPath(TwoPath path) {
		logPath=path;
		String logStep="<Step time=\""+getStep()+"\">\n"+updateLog(path,0,0)+"</Step>\n";
		updateLogFile(logStep);
	}

	//strategy called in the eval function visit
	%strategy MakeLog() extends Identity(){ 
		visit TwoPath {
			x -> {if(`x!=logPath){ updateLogPath(`x);}} 
		} 
	}

	//computes X and Y (coordinates) for the different cells
	private static String updateLog(TwoPath path, int x, int y)//calculer x et y pour chaque cellule
	{
		%match (TwoPath path){
			TwoId(Id()) -> { }
			TwoC1(top*,bottom) -> {return updateLog(`top,x,y+1)+updateLog(`bottom,x,y); }
			TwoC0(left*,right) -> {return updateLog(`left,x,y)+updateLog(`right,x+1,y);}
			TwoCell(name,source,target,type,id) ->{ return "<Cell Name=\""+`name+"\" X=\""+x+"\" Y=\""+y+"\" id=\""+`id+"\"></Cell>\n";}	
		}
		return "";
	}

	%strategy Normalize() extends Identity(){ 
		visit TwoPath {
			TwoC1(TwoC0(head1*,TwoC1(top*),tail1*),TwoC0(head2*,bottom*,tail2*)) -> {if(`head1*.target()==`head2*.source()&&`top*.target()==`bottom*.source()){System.out.println("1");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top*,bottom*),TwoC1(tail1*,tail2*));}} 
			TwoC1(TwoC0(head1*,top@@TwoCell(_,_,_,_,_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("2");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  		TwoC1(TwoC0(head1*,top@@TwoId(_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("3");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  		TwoC1(TwoC0(head*,TwoC1(top*),tail*),bottom*) -> {if(`top*.target()==`bottom*.source()){System.out.println("4");return `TwoC0(head*,TwoC1(top*,bottom*),tail*);}} 
  	  		TwoC1(TwoC0(head*,top@@TwoCell(_,_,_,_,_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("5");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  		TwoC1(TwoC0(head*,top@@TwoId(_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("6");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  		TwoC1(head*,TwoC0(topleft*,top*,topright*),TwoC0(left*,f@@TwoCell(_,_,_,Function(),_),right*),tail*) -> {//marche pas vraiment quand ya une fonction a plusieurs entrees dans y
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
  	  		TwoC1(head*,top,TwoC0(left*,X,right*),tail*) -> {
  	  			if(`left*.source()==`Id()&&`right*.source()==`Id()&&`X.source()==`top.target()){	 
  	  				TwoPath myNewPath=`TwoId(Id());//insuffisant, il pourrait yen avoir plusieurs : crenelage
  	  				if(`head*!=`TwoId(Id())){myNewPath=`TwoC1(head*,TwoC0(left*,TwoC1(top,X),right*),tail*);}else{myNewPath=`TwoC1(TwoC0(left*,TwoC1(top,X),right*),tail*);}
  	  				if(myNewPath!=`TwoId(Id())){
  	  					System.out.println("9");
  	  					return myNewPath;}
  	  			}
  	  		}
  	  		p@@TwoC1(head*,top@@TwoC0(X*),down@@TwoC0(Y*),f@@TwoCell(_,_,_,Function(),_),tail*) -> {//extension du cas 7
  	  			int sourcelength=`f.sourcesize();
  	  			TwoPath myNewPath=`TwoId(Id());
  	  			int index=0;
  	  			if(sourcelength!=`down.length()){break;}
  	  			TwoPath[] topArray=toArray((TwoC0)`top);
  	  			TwoPath[] downArray=toArray((TwoC0)`down);
  	  			for(int i=0;i<sourcelength;i++){
  	  				int downsourcelength=downArray[i].sourcesize();	
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
  	  		p@@TwoC1(head*,top@@TwoC0(X*),down@@TwoC0(Y*),tail*) -> {//extension du cas 10
  	  			int sourcelength=`down.targetsize();
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
  	  					myNewPath=`TwoC1(head,myNewPath,tail);}
  	  				else{myNewPath=`TwoC1(myNewPath,tail);}}
  	  			if(myNewPath!=`TwoId(Id())){	
  	  				System.out.println("11");
  	  				return myNewPath;}
  	  		}
  	  		TwoC1(head*,X@@TwoC0(_*),tail*)->{if(isTwoC0Id(`X)){System.out.println("supprTwoIds");if(`head==`TwoId(Id())){return `tail;}return `TwoC1(head,tail);}
  	  		}
  	  	
  	  		TwoC1(TwoC1(X*),Y*) -> {System.out.println("doubleC1");return `TwoC1(X*,Y*); }
  	  		//a part, retransforme les onec0 en twoC0
  	  		TwoId(OneC0(head,tail*)) -> { System.out.println("onetotwo");return `TwoC0(TwoId(head),TwoId(tail*)); } //correction en meme temps
  	  	
  	  		TwoC1(head*,t@@TwoId(_),TwoId(_),tail*) -> { if(`head!=`TwoId(Id())){return `TwoC1(head,t,tail);}else{return `TwoC1(t,tail);}}
  	  		//encore experimental, pour les split
  	  		TwoC1(head*,TwoC0(left*,right*),TwoC0(bottomleft*,bottomright*))->{if(`left!=`TwoId(Id())&&`right!=`TwoId(Id())&&`bottomleft!=`TwoId(Id())&&`bottomright!=`TwoId(Id())&&`left.target()==`bottomleft.source()){System.out.println("split paths");if(`head==`TwoId(Id())){return `TwoC0(TwoC1(left,bottomleft),TwoC1(right,bottomright));}else{return `TwoC1(head,TwoC0(TwoC1(left,bottomleft),TwoC1(right,bottomright)));}}}
		} 
	}


	public static boolean isTwoC0Id(TwoPath path){
		%match (TwoPath path){
			TwoC0(TwoId(_),tail*) -> { return  isTwoC0Id(`tail);}
			TwoId(_)->{return true;}
		}
		return false;
	}

	//print strategy, harmless and good for controlling
	%strategy Print() extends Identity(){
		visit TwoPath {
			x -> { System.out.print("STEP ");`x.print(); } 
		} 
	}


	%strategy Gravity() extends Identity(){ 
		visit TwoPath {
			TwoC1(head*,f@@TwoCell(_,_,_,Constructor(),_),g@@TwoId(_),tail*)->{
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
			TwoC1(head*,TwoC0(head1*,f@@TwoCell(_,_,_,Constructor(),_),tail1*),TwoC0(head2*,g@@TwoId(_),tail2*),tail*) -> { 
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
			TwoC1(head*,f@@TwoCell(_,_,_,Constructor(),_),TwoC0(head2*,g@@TwoId(_),tail2*),tail*) -> { 
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
			TwoC1(head*,TwoC0(head1*,f@@TwoCell(_,_,_,Constructor(),_),tail1*),g@@TwoId(_),tail*) -> { 
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

	// save a String in a new File
 	public static void save(String fileContent,File file) throws IOException {
		PrintWriter printWriter = new PrintWriter(new FileOutputStream(
				file));
		printWriter.print(fileContent);
		printWriter.flush();
		printWriter.close();
	}

 	// make a 1-path term from its xml description
 	public static OnePath makeOnePath(Node node){
	  String nodeName =node.getNodeName();
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

  // make a 2-path term from its xml description
  public static TwoPath makeTwoPath(Node node){
	  String nodeName =node.getNodeName();
	  if(nodeName.equals("TwoPath")){
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
				  res=`TwoC0(makeTwoPath(twoC0Element),res);
			  }				
		  }
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

  // make a 3-path term from its xml description
  public static ThreePath makeThreeCell(Node node){
	  NamedNodeMap attributes=node.getAttributes();
	  String name=attributes.getNamedItem("Name").getNodeValue();
	  String type=attributes.getNamedItem("Type").getNodeValue();
	  CellType celltype=null;
	  if(type.equals("Function")){celltype=`Function();}
	  if(type.equals("Constructor")){celltype=`Constructor();}//never used
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

	//Set of functions converting polygraphic terms in strings based on the xml format we chose
	//return the xml description of a 2-Path
	public static String twoPath2XML(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoPath>\n<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n</TwoPath>\n";}
			TwoCell(name,source,target,type,id) -> { return "<TwoPath>\n<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n</TwoPath>\n"; }
			TwoC0(head,tail*) -> {return "<TwoPath>\n<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n</TwoPath>\n";}
			TwoC1(head,tail*) -> {return "<TwoPath>\n<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n</TwoPath>\n";}
		}
		return "";
	}
	//sub-function of the previous one to handle twoC0s
	public static String twoC02XML(TwoPath path){
	%match (TwoPath path){
					TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
					TwoCell(name,source,target,type,id) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
					TwoC0(head,tail*) -> {return twoC02XML(`head)+twoC02XML(`tail);}
					TwoC1(head,tail*) -> {return "<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n";}
		}
	return "";
	}
	//the same for twoC1s
	public static String twoC12XML(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type,id) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
			TwoC0(head,tail*) -> {return "<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n";}
			TwoC1(head,tail*) -> {return twoC12XML(`head)+twoC12XML(`tail);}
		}
		return "";
	}
	//same idea with 1-paths
	public static String onePath2XML(OnePath path){
		%match (OnePath path){
			Id() -> {return "<OnePath>\n<Id></Id>\n</OnePath>\n";}
			OneCell(name) -> { return "<OnePath>\n<OneCell Name=\""+`name+"\"></OneCell>\n</OnePath>\n"; }
			OneC0(head,tail*)->{ return "<OnePath>\n<OneC0>\n"+oneC02XML(`head)+oneC02XML(`tail)+"</OneC0>\n</OnePath>\n";}
		}
		return "";
	}
	//and for OneC0s
	public static String oneC02XML(OnePath path){
		%match (OnePath path){
			Id() -> {return "<Id></Id>\n";}
			OneCell(name) -> { return "<OneCell Name=\""+`name+"\"></OneCell>\n"; }
			OneC0(head,tail*)->{ return oneC02XML(`head)+oneC02XML(`tail);}
		}
		return "";
	}
	//return the xml description of a 3-Cell
	public static String threePath2XML(ThreePath path){
		if(path instanceof ThreePath){
			return "<ThreeCell Name=\""+path.getName()+"\" Type=\""+path.getType().toString().replace("()","")+"\">\n<Source>\n"+twoPath2XML(path.getSource())+"</Source>\n<Target>\n"+twoPath2XML(path.getTarget())+"</Target>\n</ThreeCell>\n";
		}
		else {System.out.println("this is not a ThreeCell !"+path);return null;} 
	}

//-----------------------------------------------------------------------------
// specific part of each programs with all the rule strategies
//-----------------------------------------------------------------------------
@ruleStrategy@

}]%;
	//end of the program, we now return it
	 return tomFile;
  }

}