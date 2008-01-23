package compiler;

import polygraphicprogram.*;
import polygraphicprogram.types.*;
import polygraphicprogram.types.twopath.*;
import tom.library.sl.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.regex.*;

public class MakeInput{

	%include { polygraphicprogram/PolygraphicProgram.tom }
	%include { sl.tom }
	%include{ dom.tom }

	
	public static String path="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/";


	//here we define some cells to generate some test inputs

	// -----------------------------------------------------------------------------
	// NAT
	// -----------------------------------------------------------------------------
	// 1-Cell defining the nat type
	private static	OnePath nat=`OneCell("nat");
	// Constructors for nat
	private static	TwoPath zero=`TwoCell("zero",Id(),nat,Constructor(),0);
	private static	TwoPath succ =`TwoCell("succ",nat,nat,Constructor(),0);
	// Structure 2-Cells for nat
	private static	TwoPath eraser= `TwoCell("eraser",nat,Id(),Function(),0);
	private static	TwoPath duplication= `TwoCell("duplication",nat,OneC0(nat,nat),Function(),0);
	private static	TwoPath permutation = `TwoCell("permutation",OneC0(nat,nat),OneC0(nat,nat),Function(),0);
	// Function cells for nat
	private static	TwoPath plus = `TwoCell("plus",OneC0(nat,nat),nat,Function(),0);
	private static	TwoPath minus = `TwoCell("minus",OneC0(nat,nat),nat,Function(),0);
	private static	TwoPath division = `TwoCell("division",OneC0(nat,nat),nat,Function(),0);
	private static	TwoPath multiplication = `TwoCell("multiplication",OneC0(nat,nat),nat,Function(),0);

	// -----------------------------------------------------------------------------
	// LISTS
	// -----------------------------------------------------------------------------
	// 1-Cell defining the list type
	private static 	OnePath list=`OneCell("list");
	// Structure 2-Cells for lists
	private static	TwoPath consList=`TwoCell("consList",Id(),list,Constructor(),0);
	private static	TwoPath add =`TwoCell("add",OneC0(nat,list),list,Constructor(),0);
	private static  TwoPath append=`TwoCell("append",OneC0(list,nat),list,Constructor(),0);
	// Structure 2-Cells for list
	private static	TwoPath eraserList= `TwoCell("eraserList",list,Id(),Function(),0);
	private static	TwoPath duplicationList= `TwoCell("duplicationList",list,OneC0(list,list),Function(),0);
	private static	TwoPath permutationList = `TwoCell("permutationList",OneC0(list,list),OneC0(list,list),Function(),0);
	private static	TwoPath permutationNL = `TwoCell("permutationNL",OneC0(nat,list),OneC0(list,nat),Function(),0);
	private static	TwoPath permutationLN = `TwoCell("permutationLN",OneC0(list,nat),OneC0(nat,list),Function(),0);
	// Function cells for lists
	private static	TwoPath sort = `TwoCell("sort",list,list,Function(),0);
	private static	TwoPath split = `TwoCell("split",list,OneC0(list,list),Function(),0);
	private static	TwoPath merge = `TwoCell("merge",OneC0(list,list),list,Function(),0);

	// -----------------------------------------------------------------------------
	// BOOLEANS
	// -----------------------------------------------------------------------------
	//  1-Cell defining the boolean type
	private static 	OnePath bool=`OneCell("boolean");
	// Structure 2-Cells for booleans
	private static	TwoPath vrai=`TwoCell("true",Id(),bool,Constructor(),0);
	private static	TwoPath faux =`TwoCell("false",Id(),bool,Constructor(),0);
	// Structure 2-Cells for booleans
	private static	TwoPath eraserBool= `TwoCell("eraserBool",bool,Id(),Function(),0);
	private static	TwoPath duplicationBool= `TwoCell("duplicationBool",bool,OneC0(bool,bool),Function(),0);
	private static	TwoPath permutationBool = `TwoCell("permutationBool",OneC0(bool,bool),OneC0(bool,bool),Function(),0);
	// Function cells for lists
	private static	TwoPath not = `TwoCell("not",bool,bool,Function(),0);
	private static	TwoPath and = `TwoCell("and",OneC0(bool,bool),bool,Function(),0);
	private static	TwoPath or = `TwoCell("or",OneC0(bool,bool),bool,Function(),0);
	private static	TwoPath mergeSwitch = `TwoCell("mergeSwitch",OneC0(bool,nat,list,nat,list),list,Function(),0);

	// comparison
	private static	TwoPath lessOrEqual = `TwoCell("lessOrEqual",OneC0(nat,nat),bool,Function(),0);
	// square and cube
	private static TwoPath carre = `TwoCell("square",nat,nat,Function(),0);
	private static TwoPath cube = `TwoCell("cube",nat,nat,Function(),0);
	// nat equality
	private static TwoPath equal=`TwoCell("equal",OneC0(nat,nat),bool,Function(),0);
	// list equality
	private static TwoPath lEqual=`TwoCell("lEqual",OneC0(list,list),bool,Function(),0);


	//main : creates a xml file ("input.xml") describing a 2-Path 
	public static void main(String[] args) {
	//tests	
		TwoPath un=`TwoC1(zero,succ);
		TwoPath deux=`TwoC1(un,succ);
		TwoPath trois=`TwoC1(deux,succ);
		TwoPath quatre=`TwoC1(trois,succ);
		TwoPath cinq=`TwoC1(quatre,succ);
		TwoPath six=`TwoC1(cinq,succ);
		TwoPath sept=`TwoC1(six,succ);
		TwoPath huit=`TwoC1(sept,succ);
		TwoPath neuf=`TwoC1(huit,succ);
		TwoPath dix=`TwoC1(neuf,succ);

		TwoPath rule=`TwoC1(TwoC0(zero,zero),TwoC0(succ,succ),permutation,minus,eraser);
		TwoPath rule2=`TwoC1(zero,TwoC0(succ,zero),division);
		TwoPath rule3=`TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),TwoC1(zero,succ,succ)),multiplication);
		TwoPath rule4=`TwoC1(zero,succ,succ,TwoC0(succ,zero),TwoC0(succ,succ),TwoC0(succ,succ),division);
		TwoPath rule5=`TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),TwoC1(zero)),division);
		TwoPath addit=`TwoC1(TwoC0(TwoC1(zero,succ,succ,succ,succ,succ),TwoC1(zero,succ,succ,succ)),plus);
		TwoPath div=`TwoC1(TwoC0(addit,TwoC1(zero,succ,succ,succ)),division);
		TwoPath total=`TwoC1(TwoC0(div,TwoC1(zero,succ,succ,succ,succ)),multiplication);
		TwoPath nine=`TwoC1(TwoC0(TwoC1(TwoC0(deux,six),multiplication),trois),division);
		TwoPath testnatlist = `TwoC1(TwoC0(deux,TwoC1(TwoC0(consList,un),append)),add,sort);
		TwoPath testBool = `TwoC1(TwoC0(quatre,six),lessOrEqual);
		TwoPath testCarre = `TwoC1(makeNat(12),carre);
		TwoPath testSimple = `TwoC1(TwoC0(trois,zero),plus);
		TwoPath comparatifCarre=`TwoC1(TwoC0(makeNat(12),makeNat(12)),multiplication);
		TwoPath testCube = `TwoC1(quatre,cube);
		TwoPath testEqual = `TwoC1(TwoC0(TwoC1(TwoC0(trois,huit),multiplication),TwoC1(TwoC0(six,quatre),multiplication)),equal);
		TwoPath testsort = `TwoC1(TwoC0(TwoC1(zero,TwoC1(succ,succ)),TwoC0(TwoC1(zero,TwoC1(succ,succ)),TwoC0(consList,TwoC0(TwoC1(zero,succ),TwoC1(zero,succ))))),TwoC1(TwoC0(TwoC0(TwoId(nat),TwoC0(TwoId(nat),permutationLN)),TwoId(nat)),TwoC1(TwoC0(TwoC0(TwoId(nat),TwoC0(permutation,TwoId(list))),TwoId(nat)),TwoC1(TwoC0(TwoC0(lessOrEqual,TwoC0(TwoId(nat),TwoId(list))),TwoC0(TwoId(nat),consList)),mergeSwitch))));

		int[] list1={7,8,3,2,6};
		int[] list2={9,5,7,4,1};
		TwoPath testEqualList = `TwoC1(TwoC0(TwoC1(makeList(list1),sort),TwoC1(makeList(list2),sort)),lEqual);
		TwoPath testsortcomplex= `TwoC1(TwoC0(makeList(list1),makeList(list2)),merge,sort);


		//defines the input
		String input=twoPath2XML(testSimple);
		//saves it
		if(!input.equals("")){
			try{
				save(input,new File(path+"XMLinput.xml"));
			}catch(Exception e){e.printStackTrace();}
		}
	}

	//save string in a file
	public static void save(String fileContent,File file) throws IOException {

		PrintWriter printWriter = new PrintWriter(new FileOutputStream(
				file));
		printWriter.print(fileContent);
		printWriter.flush();
		printWriter.close();
	}

	//Set of functions converting polygraphic terms in strings based on the xml format we chose
	public static String twoPath2XML(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoPath>\n<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n</TwoPath>\n";}
			TwoCell(name,source,target,type,id) -> { return "<TwoPath>\n<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n</TwoPath>\n"; }
			TwoC0(head,tail*) -> {return "<TwoPath>\n<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n</TwoPath>\n";}
			TwoC1(head,tail*) -> {return "<TwoPath>\n<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n</TwoPath>\n";}
		}
		return "";
	}

	public static String twoC02XML(TwoPath path){
	%match (TwoPath path){
					TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
					TwoCell(name,source,target,type,id) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
					TwoC0(head,tail*) -> {return twoC02XML(`head)+twoC02XML(`tail);}
					TwoC1(head,tail*) -> {return "<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n";}
		}
	return "";
	}

	public static String twoC12XML(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type,id) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
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

	//make a polygraphic list from an array of int
	public static TwoPath makeList(int[] array){
		TwoPath list=`TwoC1(TwoC0(makeNat(array[array.length-1]),TwoCell("consList",Id(),OneCell("list"),Constructor(),0)),add);
		for (int i = array.length-2; i>=0 ;i--) {
			list=`TwoC1(TwoC0(makeNat(array[i]),list),add);
		}
		return list;
	}

	//make a polygraphic nat from an int
	public static TwoPath makeNat(int nat){
		TwoPath natPath=`TwoCell("zero",Id(),OneCell("nat"),Constructor(),0);
		for (int i = nat; i >0; i--) {
			natPath=`TwoC1(natPath,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor(),0));
		}
		return natPath;
	}

	//aborted work supposed to make polygraphes from a keyboard input : 
/*
	 public static int inputInt() {
	      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	      int i =0; 
	      try { 
	          i = Integer.parseInt(input.readLine());
	      } 
	      catch (Exception e) { 
	          e.printStackTrace(); 
	      } 
	      return i; 
	    }
	
	
	 public static String inputString() {
	      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	      String string="";
	      try { 
	          string = input.readLine();
	      } 
	      catch (Exception e) { 
	          e.printStackTrace(); 
	      } 
	      return string; 
	    }

	 public static String menu(){
		 String input="";
		 System.out.println("-----------------------\nchoose your input mode : \n1\tNat only\n2\tList only\n3\tBoolean only\n4\tCustom\n5\tExit\n-----------------------");
		 int choix=inputInt();
		 if(choix==1){System.out.println("*****Nat Only Mode*****\nEnter a 2-Path : \n");input=parseNat(inputString());}
		 else if(choix==2){System.out.println("*****List Only Mode*****\nEnter a 2-Path : \n");input=parseList(inputString());}
		 else if(choix==3){System.out.println("*****Boolean Only Mode*****\nEnter a 2-Path : \n");input=parseBoolean(inputString());}
		 else if(choix==4){System.out.println("*****Custom Mode*****\nEnter a 2-Path : \n");input=inputString();}
		 else if(choix==5){}
		 else{System.out.println("unvalid choice !!\nTRY AGAIN");menu();}
		 return input;
	 	}

	public static String parseNat(String userInput){
		TwoPath path=`TwoId(Id());
		//todo
		return twoPath2XML(path);
	}

	public static String parseList(String userInput){
		TwoPath path=`TwoId(Id());
		//todo
		return twoPath2XML(path);
	}

	public static String parseBoolean(String userInput){
		TwoPath path=`TwoId(Id());
		//todo
		return twoPath2XML(path);
	}

	public static String splitNat(String userInput){//aborted
		try{Pattern splitOperator=Pattern.compile("^\\W");
		String[] split=splitOperator.split(userInput);
		String operator=split[0];
		//Pattern leftOperand=Pattern.compile("^\\W\(");
		Pattern rightOperand;
		//then test if there is a right operand
		
		}
		catch(Exception e){System.out.println("error : invalide expression");}
		return null;
		}
*/


}