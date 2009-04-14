package gui;

import gui.*;
import java.io.File;

//contains the entry point for the CompilerXML
public class CompilerGUI {

	public static String programPath;
	public static String outputFolderPath;
	private static int OS_WIN = 0;
	private static int OS_LINUX = 1;

	//entry point, get the whole program and saves it into a .t file
	public static void main (String args[]) {
		if(args.length!=2){
			System.out.println("you must indicate the path of the xml program and the path of the destination folder of the program");
			}
		else{
			if(args[0].substring(args[0].length()-4, args[0].length()).equals(".xml")){
				programPath=args[0];
				if(args[1].substring(args[1].length()-1, args[1].length()).equals(File.separator)){
					outputFolderPath=args[1];
					String programName=XMLProgramHandlerGui.getProgramName(programPath);
					try{
						(new File(outputFolderPath)).mkdir();
						(new File(outputFolderPath+"gui"+File.separator)).mkdir();
						File programFile=new File(outputFolderPath+"gui"+File.separator+programName+".t");
						if(!programFile.exists()){programFile.createNewFile();}
						XMLhandlerGui.save(makeTomFile(programPath),programFile);
						//create gom file
						String srcpath=(new CompilerGUI()).getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
						//System.out.println("path = " + srcpath);
						File gomFile=new File(outputFolderPath+"gui/"+programName+".gom");
						(new File(outputFolderPath+"gui"+File.separator)).mkdir();
						if(!gomFile.exists()){gomFile.createNewFile();}
						String gomFileContent=XMLhandlerGui.load(srcpath+".." +File.separator+"src" + File.separator+ "gui" + File.separator + "PolygraphicProgram.gom");
						gomFileContent=gomFileContent.replace("PolygraphicProgram", programName);
						XMLhandlerGui.save(gomFileContent,gomFile);
						//create XMLhandlerGui file
						File XMLhandlerGuiFile=new File(outputFolderPath+"gui" + File.separator +programName+"Tools.t");
						(new File(outputFolderPath+"gui"+File.separator)).mkdir();
						if(!XMLhandlerGuiFile.exists()){XMLhandlerGuiFile.createNewFile();}
						String XMLhandlerGuiFileContent=XMLhandlerGui.load(srcpath+".."+File.separator+"src" + File.separator+ "gui" + File.separator + "XMLhandlerGui.t");
						XMLhandlerGuiFileContent=XMLhandlerGuiFileContent.replace("XMLhandlerGui",programName+"Tools");
						XMLhandlerGuiFileContent=XMLhandlerGuiFileContent.replace("package Compiler;","");
						XMLhandlerGuiFileContent=XMLhandlerGuiFileContent.replace("polygraphicprogramgui.types",programName.toLowerCase()+".types");
						//Warning for the gom import leave the linux separator
						XMLhandlerGuiFileContent=XMLhandlerGuiFileContent.replace("polygraphicprogramGUI/PolygraphicProgramgui.tom",programName.toLowerCase()+"/"+programName+".tom");
						XMLhandlerGui.save(XMLhandlerGuiFileContent,XMLhandlerGuiFile);
						//create script file
						File scriptFile;
						if (getOS() == OS_WIN) {
							scriptFile = new File(outputFolderPath+"build"+programName+".bat");
						} else {
							scriptFile = new File(outputFolderPath+"build"+programName+".sh");
						}
						if(!scriptFile.exists()){scriptFile.createNewFile();}
						XMLhandlerGui.save(makeScript(programName),scriptFile);
					}			
					catch(Exception e){e.printStackTrace();}	
					}
				else{
				System.out.println("the second argument must be a folder (ends with " + File.separator + ")");
				}
			}
			else{
				System.out.println("the first argument must be an xml file (*.xml)");
			}
		}
	}
	
	public static String makeScript(String programName){
		String entete ="@rem Script for Windows";
		String appelcall = "";
		if (getOS() != OS_WIN) {
			entete = "#!/bin/tcsh";
	    } else {
	    	appelcall = "call ";
	    }
		String mpath = File.separator + programName;
		return %[@entete@
@appelcall@gom gui@mpath@.gom
@appelcall@tom gui@mpath@.t
javac gui@mpath@.java
]%;
		}

	//constructs the program and particularly the common part of each program
	public static String makeTomFile(String filePath){
		String programName=XMLProgramHandlerGui.getProgramName(filePath);
		String ruleStrategy=XMLProgramHandlerGui.makeRuleStrategy(filePath);//key point, gets the specific part of the file
		String lowerProgramName=programName.toLowerCase();
		//Warning linux separators for tom calls
		String tomPath="../gui/"+lowerProgramName+"/"+programName+".tom";//make tom path
		String sep = "File.separator";
		//here lies the part that each program has in common : 
		String tomFile=%[package program;

import adt.@lowerProgramName@.types.*;
import adt.@lowerProgramName@.types.twopath.*;
import tom.library.sl.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.*;
import library.@programName@Tools;

public class @programName@{
	%include { sl.tom }
	//include .tom
	%include { @tomPath@}
	

	public static void main (String args[]) {
		String inputPath="";
		String outputFolderPath="";
		if(args.length<1){
			System.out.println("you must indicate the path of the xml input and the path of the destination folder of the output");
			}
		else{
			if(args[0].substring(args[0].length()-4, args[0].length()).equals(".xml")){
				inputPath=args[0];
				if(args[1].substring(args[1].length()-1, args[1].length()).equals(@sep@)){
					outputFolderPath=args[1];
					try {
						(new File(outputFolderPath)).mkdir();
						Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputPath);
						Element e = dom.getDocumentElement();
						TwoPath input=makeTwoPathWithID(e);
						//updateLogPath(input);
						//saves the new twopath in another xml file
						@programName@Tools.save(@programName@Tools.twoPath2Graph(eval(input)),new File(outputFolderPath+"result.xml"));
						//saves the history
						//@programName@Tools.save(log+"</Log>",new File(outputFolderPath+"log.xml"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
				System.out.println("the second argument must be a folder (ends with " + @sep@);
				}
			}
			else{
				System.out.println("the first argument must be an xml file i.e. with the extension .xml");
			}
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
			TwoC1(TwoC0(head1*,TwoC1(top*),tail1*),TwoC0(head2*,bottom*,tail2*)) -> {if(`head1*.target()==`head2*.source()&&`top*.target()==`bottom*.source()){System.out.print(".");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top*,bottom*),TwoC1(tail1*,tail2*));}} 
			TwoC1(TwoC0(head1*,top@@TwoCell(_,_,_,_,_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.print(".");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  		TwoC1(TwoC0(head1*,top@@TwoId(_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.print(".");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  		TwoC1(TwoC0(head*,TwoC1(top*),tail*),bottom*) -> {if(`top*.target()==`bottom*.source()){System.out.print(".");return `TwoC0(head*,TwoC1(top*,bottom*),tail*);}} 
  	  		TwoC1(TwoC0(head*,top@@TwoCell(_,_,_,_,_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.print(".");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  		TwoC1(TwoC0(head*,top@@TwoId(_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.print(".");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  		TwoC1(head*,TwoC0(topleft*,top*,topright*),TwoC0(left*,f@@TwoCell(_,_,_,Function(),_),right*),tail*) -> {//marche pas vraiment quand ya une fonction a plusieurs entrees dans y
  	  			if(`topleft*.target()==`left*.source()&&`top.target()==`f.source()){
  	  				TwoPath myNewPath=`TwoId(Id());
  	  				if(`head*!=`TwoId(Id())){myNewPath= `TwoC1(head*,TwoC0(TwoC1(topleft*,left*),TwoC1(top*,f),TwoC1(topright*,right*)),tail*);}
  	  				else{myNewPath= `TwoC1(TwoC0(TwoC1(topleft*,left*),TwoC1(top*,f),TwoC1(topright*,right*)),tail*);}
  	  				if(myNewPath!=`TwoId(Id())){
  	  					System.out.print(".");
  	  					return myNewPath;
  	  				}
  	  			}
  	  		}
  	  		TwoC1(head*,top,TwoC0(left*,X,right*),tail*) -> {
  	  			if(`left*.source()==`Id()&&`right*.source()==`Id()&&`X.source()==`top.target()){	 
  	  				TwoPath myNewPath=`TwoId(Id());//insuffisant, il pourrait yen avoir plusieurs : crenelage
  	  				if(`head*!=`TwoId(Id())){myNewPath=`TwoC1(head*,TwoC0(left*,TwoC1(top,X),right*),tail*);}else{myNewPath=`TwoC1(TwoC0(left*,TwoC1(top,X),right*),tail*);}
  	  				if(myNewPath!=`TwoId(Id())){
  	  					System.out.print(".");
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
  	  				System.out.print(".");
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
  	  				System.out.print(".");
  	  				return myNewPath;}
  	  		}
  	  		TwoC1(head*,X@@TwoC0(_*),tail*)->{if(isTwoC0Id(`X)){if(`head==`TwoId(Id())){return `tail;}return `TwoC1(head,tail);}
  	  		}
  	  	
  	  		TwoC1(TwoC1(X*),Y*) -> {System.out.print(".");return `TwoC1(X*,Y*); }
  	  		//a part, retransforme les onec0 en twoC0
  	  		TwoId(OneC0(head,tail*)) -> { System.out.print(".");return `TwoC0(TwoId(head),TwoId(tail*)); } //correction en meme temps
  	  	
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
				System.out.print(".");
				return `TwoC1(head*,TwoId(f.source()),f,tail*);
				}
			}
			TwoC1(head*,TwoC0(head1*,f@@TwoCell(_,_,_,Constructor(),_),tail1*),TwoC0(head2*,g@@TwoId(_),tail2*),tail*) -> { 
				if(`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`g.source()){																								
					if(`head*==`TwoId(Id())){
						if(`tail*==`TwoId(Id())){return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
						System.out.print(".");
						return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
					}
					if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
					System.out.print(".");
					return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
				}
			}
			TwoC1(head*,f@@TwoCell(_,_,_,Constructor(),_),TwoC0(head2*,g@@TwoId(_),tail2*),tail*) -> { 
				if(`f.target()==`g.source()){																			
					if(`head*==`TwoId(Id())){
						if(`tail*==`TwoId(Id())){return `TwoC0(head2*,f,tail2*);}
						System.out.print(".");
						return `TwoC1(TwoC0(head2*,f,tail2*),tail*);
					}
					if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head2*,f,tail2*));}
					System.out.print(".");
					return `TwoC1(head*,TwoC0(head2*,f,tail2*),tail*);
				}
			}
			TwoC1(head*,TwoC0(head1*,f@@TwoCell(_,_,_,Constructor(),_),tail1*),g@@TwoId(_),tail*) -> { 
				if(`f.target()==`g.source()){
				if(`head*==`TwoId(Id())){
					if(`tail*==`TwoId(Id())){System.out.print(".");return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),f);}
					System.out.print(".");return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),f,tail*);
				}
				if(`tail*==`TwoId(Id())){System.out.print(".");return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),f);}
				System.out.print(".");return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),f,tail*);
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

  // make a 2-path term from its xml description
  public static TwoPath makeTwoPathWithID(Node node){
	  String nodeName =node.getNodeName();
	  if(nodeName.equals("TwoPath")){
		  NodeList nodeChilds=node.getChildNodes();
		  for (int i = 0; i < nodeChilds.getLength(); i++) {
			  Node nodeChild=nodeChilds.item(i);
			  if(!nodeChild.getNodeName().equals("#text")){return makeTwoPathWithID(nodeChild);}
		  }
	  }
	  if(nodeName.equals("TwoId")){
		  NodeList nodeChilds=node.getChildNodes();
		  for (int i = 0; i < nodeChilds.getLength(); i++) {
			  Node nodeChild=nodeChilds.item(i);
			  if(!nodeChild.getNodeName().equals("#text")){return `TwoId(@programName@Tools.makeOnePath(nodeChild));}
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
			  if(ioName.equals("Source")){source=@programName@Tools.makeOnePath(ioChild);}
			  if(ioName.equals("Target")){target=@programName@Tools.makeOnePath(ioChild);}
		  }
		  return `TwoCell(name,source,target,celltype,setID());
	  }
	  if(nodeName.equals("TwoC0")){
		  NodeList twoC0s=node.getChildNodes();
		  TwoPath res=`TwoId(Id());
		  for (int j = twoC0s.getLength()-1; j >0; j--) {
			  Node twoC0Element = twoC0s.item(j);
			  if(!twoC0Element.getNodeName().contains("#text")){
				  res=`TwoC0(makeTwoPathWithID(twoC0Element),res);
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
				  if(res==`TwoId(Id())){res=`makeTwoPathWithID(twoC1Element);}
				  else{
					  res=`TwoC1(makeTwoPathWithID(twoC1Element),res);
				  }
			  }				
		  }		
		  return res;
	  }
	  NodeList childs=node.getChildNodes();
	  for (int i = 0; i < childs.getLength(); i++) {
		  Node child = childs.item(i);
		  if(!child.getNodeName().equals("#text")){
			  return makeTwoPathWithID(child);
		  }	
	  }
	  return `TwoId(Id());
  }

//-----------------------------------------------------------------------------
// specific part of each programs with all the rule strategies
//-----------------------------------------------------------------------------
@ruleStrategy@

}]%;
	//end of the program, we now return it
	 return tomFile;
  }

	private static int getOS(){
		String name = System.getProperty ( "os.name" );
		System.out.println("DEBUG OS name = " + name);
		if (name.toUpperCase().startsWith("WINDOWS")){
			return OS_WIN;
		} else {
			return OS_LINUX; //assuming that this project would be only run under windows or linux
		}
	}
}
