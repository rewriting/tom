import polygraphicprogram.*;
import polygraphicprogram.types.*;
import polygraphicprogram.types.twopath.*;
import tom.library.sl.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.regex.*;

public class MakeQBFInput{

%include { polygraphicprogram/PolygraphicProgram.tom }
%include { sl.tom }
%include{ dom.tom }

 
// -----------------------------------------------------------------------------
// NAT
// -----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static	OnePath nat=`OneCell("nat");
// constructeurs sur les entiers naturels
private static	TwoPath zero=`TwoCell("zero",Id(),nat,Constructor());
private static	TwoPath succ =`TwoCell("succ",nat,nat,Constructor());
// 2-cellules de structure
private static	TwoPath eraser= `TwoCell("eraser",nat,Id(),Function());
private static	TwoPath duplication= `TwoCell("duplication",nat,OneC0(nat,nat),Function());
private static	TwoPath permutation = `TwoCell("permutation",OneC0(nat,nat),OneC0(nat,nat),Function());
// addition, soustraction et division
private static	TwoPath plus = `TwoCell("plus",OneC0(nat,nat),nat,Function());
private static	TwoPath minus = `TwoCell("minus",OneC0(nat,nat),nat,Function());
private static	TwoPath division = `TwoCell("division",OneC0(nat,nat),nat,Function());
private static	TwoPath multiplication = `TwoCell("multiplication",OneC0(nat,nat),nat,Function());
// -----------------------------------------------------------------------------
// LISTS
// -----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static 	OnePath list=`OneCell("list");
// constructeurs sur les entiers naturels
private static	TwoPath consList=`TwoCell("consList",Id(),list,Constructor());
private static	TwoPath add =`TwoCell("add",OneC0(nat,list),list,Constructor());
private static  TwoPath append=`TwoCell("append",OneC0(list,nat),list,Constructor());
// 2-cellules de structure
private static	TwoPath eraserList= `TwoCell("eraserList",list,Id(),Function());
private static	TwoPath duplicationList= `TwoCell("duplicationList",list,OneC0(list,list),Function());
private static	TwoPath permutationList = `TwoCell("permutationList",OneC0(list,list),OneC0(list,list),Function());
private static	TwoPath permutationNL = `TwoCell("permutationNL",OneC0(nat,list),OneC0(list,nat),Function());
private static	TwoPath permutationLN = `TwoCell("permutationLN",OneC0(list,nat),OneC0(nat,list),Function());
// addition, soustraction et division
private static	TwoPath sort = `TwoCell("sort",list,list,Function());
private static	TwoPath split = `TwoCell("split",list,OneC0(list,list),Function());
private static	TwoPath merge = `TwoCell("merge",OneC0(list,list),list,Function());
// -----------------------------------------------------------------------------
// BOOLEANS
// -----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static 	OnePath bool=`OneCell("boolean");
// constructeurs sur les entiers naturels
private static	TwoPath vrai=`TwoCell("true",Id(),bool,Constructor());
private static	TwoPath faux =`TwoCell("false",Id(),bool,Constructor());
// 2-cellules de structure
private static	TwoPath eraserBool= `TwoCell("eraserBool",bool,Id(),Function());
private static	TwoPath duplicationBool= `TwoCell("duplicationBool",bool,OneC0(bool,bool),Function());
private static	TwoPath permutationBool = `TwoCell("permutationBool",OneC0(bool,bool),OneC0(bool,bool),Function());
// Et il faudrait mettre toutes les permutations possibles ici
// addition, soustraction et division
private static	TwoPath not = `TwoCell("not",bool,bool,Function());
private static	TwoPath and = `TwoCell("and",OneC0(bool,bool),bool,Function());
private static	TwoPath or = `TwoCell("or",OneC0(bool,bool),bool,Function());
private static	TwoPath mergeSwitch = `TwoCell("mergeSwitch",OneC0(bool,nat,list,nat,list),list,Function());
// introduction de la comparaison
private static	TwoPath lessOrEqual = `TwoCell("lessOrEqual",OneC0(nat,nat),bool,Function());
// carre et cube
private static TwoPath carre = `TwoCell("carre",nat,nat,Function());
private static TwoPath cube = `TwoCell("cube",nat,nat,Function());
// egalité nat
private static TwoPath equal=`TwoCell("equal",OneC0(nat,nat),bool,Function());
// egalite list
private static TwoPath lEqual=`TwoCell("lEqual",OneC0(list,list),bool,Function());
//qbf
private static TwoPath var=`TwoCell("var",nat,bool,Constructor());
private static TwoPath exists=`TwoCell("exists",OneC0(nat,bool),bool,Constructor());
private static TwoPath isIn=`TwoCell("isIn",OneC0(nat,list),bool,Function());
private static TwoPath verify=`TwoCell("verify",OneC0(bool,list),bool,Function());
private static TwoPath isInTest=`TwoCell("isInTest",OneC0(bool,nat,list),bool,Function());


public static void main(String[] args) {

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
int[] list1={8,9};
TwoPath qbftest=`TwoC1(TwoC0(TwoC1(TwoC0(deux,TwoC1(deux,var)),exists),makeList(list1)),verify);

String input=twoPath2XML(qbftest);

if(!input.equals("")){
try{
save(input,new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/src/XMLinput.xml"));
}catch(Exception e){e.printStackTrace();}
}
}
public static void save(String fileContent,File file) throws IOException {

		PrintWriter printWriter = new PrintWriter(new FileOutputStream(
				file));
		printWriter.print(fileContent);
		printWriter.flush();
		printWriter.close();
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


public static TwoPath makeList(int[] array){
TwoPath list=`TwoC1(TwoC0(makeNat(array[array.length-1]),TwoCell("consList",Id(),OneCell("list"),Constructor())),add);
for (int i = array.length-2; i>=0 ;i--) {
	list=`TwoC1(TwoC0(makeNat(array[i]),list),add);
}
return list;
}

public static TwoPath makeNat(int nat){
TwoPath natPath=`TwoCell("zero",Id(),OneCell("nat"),Constructor());
for (int i = nat; i >0; i--) {
	natPath=`TwoC1(natPath,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()));
}
return natPath;
}

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
//travail en cours pour les tests, ça semble facile mais c pas mal de travail pour pas grand chose quand il faut parser les saisies clavier
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
		return twoPath2XML(path);
}
	public static String parseList(String userInput){
		TwoPath path=`TwoId(Id());
		return twoPath2XML(path);
}
	public static String parseBoolean(String userInput){
		TwoPath path=`TwoId(Id());
		return twoPath2XML(path);
}
	public static String splitNat(String userInput){//en fait il faudrait redéfinir un petit langage
		try{
		Pattern splitOperator=Pattern.compile("^\\W");
		String[] split=splitOperator.split(userInput);
		String operator=split[0];
		//Pattern leftOperand=Pattern.compile("^\\W\(");
		Pattern rightOperand;

		//tester si ya un membre de droite
		}
		catch(Exception e){System.out.println("error : invalide expression");}
		return null;
		

}
}