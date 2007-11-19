package polygrapheslist;

import polygrapheslist.*;
import polygrapheslist.types.*;
import tom.library.sl.*;

import java.util.HashSet;
import java.util.Iterator;

public class PolygraphesList{

%include { polygrapheslist/PolygraphesList.tom }
%include { sl.tom }

private static HashSet<ThreePath> rewritingRules=new HashSet<ThreePath>();


//-----------------------------------------------------------------------------
// jeu de cellules pour les tests : inutile pour le compilateur final, c'esr juste plus pratique
//-----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static	OnePath nat=`OneCell("nat");
private static 	OnePath list=`OneCell("list");
// constructeurs sur les entiers naturels
private static	TwoPath consList=`TwoCell("consList",Id(),list,Constructor());
private static 	TwoPath zero = `TwoCell("0",Id(),nat,Constructor());
private static 	TwoPath un = `TwoCell("1",Id(),nat,Constructor());
private static 	TwoPath deux = `TwoCell("2",Id(),nat,Constructor());
private static 	TwoPath trois = `TwoCell("3",Id(),nat,Constructor());
private static 	TwoPath quatre = `TwoCell("4",Id(),nat,Constructor());
private static 	TwoPath cinq = `TwoCell("5",Id(),nat,Constructor());
private static 	TwoPath six = `TwoCell("6",Id(),nat,Constructor());
private static 	TwoPath sept = `TwoCell("7",Id(),nat,Constructor());
private static 	TwoPath huit = `TwoCell("8",Id(),nat,Constructor());
private static 	TwoPath neuf = `TwoCell("9",Id(),nat,Constructor());
private static	TwoPath add =`TwoCell("add",OneC0(nat,list),list,Constructor());
private static  TwoPath append=`TwoCell("append",OneC0(list,nat),list,Constructor());
// 2-cellules de structure
private static	TwoPath eraserNat= `TwoCell("eraserNat",nat,Id(),Function());
private static	TwoPath eraserList= `TwoCell("eraserList",list,Id(),Function());
private static	TwoPath duplicationNat= `TwoCell("duplicationNat",nat,OneC0(nat,nat),Function());
private static	TwoPath duplicationList= `TwoCell("duplicationList",list,OneC0(list,list),Function());
private static	TwoPath permutationNat = `TwoCell("permutationNat",OneC0(nat,nat),OneC0(nat,nat),Function());
private static	TwoPath permutationList = `TwoCell("permutationList",OneC0(list,list),OneC0(list,list),Function());
private static	TwoPath permutationNL = `TwoCell("permutationNL",OneC0(nat,list),OneC0(list,nat),Function());
private static	TwoPath permutationLN = `TwoCell("permutationLN",OneC0(list,nat),OneC0(nat,list),Function());
// addition, soustraction et division
private static	TwoPath sort = `TwoCell("sort",list,list,Function());
private static	TwoPath split = `TwoCell("split",nat,OneC0(nat,nat),Function());
private static	TwoPath merge = `TwoCell("merge",OneC0(list,list),list,Function());

public static void main(String[] args) {

TwoPath test = `TwoC1(TwoC0(TwoC1(TwoC0(un,TwoC1(TwoC0(deux,consList),add)),add),TwoC1(TwoC0(zero,TwoC1(TwoC0(cinq,consList),add)),add)),merge);

test.print();
test(test);
}

public static void print (TwoPath path){
System.out.println(path.prettyPrint());
}

%strategy Normalize() extends Identity(){ 
  	visit TwoPath {
  		TwoC1(TwoC0(head1*,TwoC1(top*),tail1*),TwoC0(head2*,bottom*,tail2*)) -> {if(`head1*.target()==`head2*.source()&&`top*.target()==`bottom*.source()){System.out.println("1");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top*,bottom*),TwoC1(tail1*,tail2*));}} 
  		TwoC1(TwoC0(head1*,top@TwoCell(_,_,_,_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("2");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head1*,top@TwoId(_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("3");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head*,TwoC1(top*),tail*),bottom*) -> {if(`top*.target()==`bottom*.source()){System.out.println("4");return `TwoC0(head*,TwoC1(top*,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@TwoCell(_,_,_,_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("5");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@TwoId(_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("6");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(head*,top@TwoC0(X*),down@TwoC0(Y*),f@TwoCell(_,_,_,Function()),tail*) -> {//marche pas vraiment quand ya une fonction a plusieurs entrees dans y
  	  		int length=`f.sourcesize();
  	  		TwoPath myNewPath=`TwoId(Id());
  	  		for(int i=0;i<length;i++){
  	  			if(`((TwoPath)top.getChildAt(i)).target()==`((TwoPath)down.getChildAt(i)).source()){
  	  			TwoPath newC1=`TwoC1((TwoPath)top.getChildAt(i),(TwoPath)down.getChildAt(i));
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
  	  		System.out.println("7");
  	  		return myNewPath;}
  	  		}
  	  	TwoC1(head*,TwoC0(topleft*,top*,topright*),TwoC0(left*,f@TwoCell(_,_,_,Function()),right*),tail*) -> {//marche pas vraiment quand ya une fonction a plusieurs entrees dans y
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
 	 } 
}

%strategy Print() extends Identity(){
  	visit TwoPath {
  	  x -> { System.out.print("STEP ");print(`x); } 
 	 } 
}


// pour gagner du temps et de l'espace, mais pas en rigueur, la seul mention des noms des fonctions serait suffisante quand on decrit les 2-cellules dans les regles suivantes
%strategy ApplyRules() extends Identity(){ 
  	visit TwoPath {
  	 TwoC1(TwoCell(name,Id(),_,Constructor()),TwoCell(_,_,Id(),Function()),Y*) -> {System.out.println("eraz "+`name);return `Y*;}
  	 TwoC1(t@TwoCell(name,Id(),OneCell("nat"),Constructor()),TwoCell("duplicationNat",OneCell("nat"),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("dupplication nat "+`name);return `TwoC1(TwoC0(t,t),Y*);}
  	 TwoC1(t@TwoCell("consList",Id(),OneCell("list"),Constructor()),TwoCell("duplicationList",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("dupplication list ");return `TwoC1(TwoC0(t,t),Y*);}
  	 TwoC1(TwoC0(TwoCell(name,Id(),OneCell("nat"),Constructor()),X1*),TwoCell("permutationNat",OneC0(OneCell("nat"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("NatPerm1");return `TwoC1(TwoC0(X1,TwoCell(name,Id(),OneCell("nat"),Constructor())),Y); } 
  	 TwoC1(TwoC0(X1*,TwoCell(name,Id(),OneCell("nat"),Constructor())),TwoCell("permutationNat",OneC0(OneCell("nat"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("NatPerm2");return `TwoC1(TwoC0(TwoCell(name,Id(),OneCell("nat"),Constructor()),X1),Y); } 
  	 TwoC1(TwoC0(TwoCell(name,Id(),OneCell("list"),Constructor()),X1*),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("ListPerm1");return `TwoC1(TwoC0(X1,TwoCell(name,Id(),OneCell("list"),Constructor())),Y); } 
  	 TwoC1(TwoC0(X1*,TwoCell(name,Id(),OneCell("list"),Constructor())),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("ListPerm2");return `TwoC1(TwoC0(TwoCell(name,Id(),OneCell("list"),Constructor()),X1),Y); } 
  	 TwoC1(TwoC0(TwoCell(name,Id(),OneCell("nat"),Constructor()),X1*),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),Y*) -> {System.out.println("NatPerm1");return `TwoC1(TwoC0(X1,TwoCell(name,Id(),OneCell("nat"),Constructor())),Y); } 
  	 TwoC1(TwoC0(X1*,TwoCell(name,Id(),OneCell("nat"),Constructor())),TwoCell("permutationLN",OneC0(OneCell("list"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("list")),Function()),Y*) -> {System.out.println("NatPerm2");return `TwoC1(TwoC0(TwoCell(name,Id(),OneCell("nat"),Constructor()),X1),Y); } 
  	 TwoC1(TwoC0(TwoCell(name,Id(),OneCell("list"),Constructor()),X1*),TwoCell("permutationLN",OneC0(OneCell("list"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("list")),Function()),Y*) -> {System.out.println("ListPerm1");return `TwoC1(TwoC0(X1,TwoCell(name,Id(),OneCell("list"),Constructor())),Y); } 
  	 TwoC1(TwoC0(X1*,TwoCell(name,Id(),OneCell("list"),Constructor())),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),Y*) -> {System.out.println("ListPerm2");return `TwoC1(TwoC0(TwoCell(name,Id(),OneCell("list"),Constructor()),X1),Y); } 
  	 TwoC1(TwoC0(X1*,X2),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("eraserList",OneCell("list"),Id(),Function()),Y*) -> {System.out.println("AddEraser");return `Y;}
  	 TwoC1(TwoC0(X1*,X2),TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),TwoCell("eraserList",OneCell("list"),Id(),Function()),Y*) -> {System.out.println("AppendEraser");return `Y;}
  	 TwoC1(TwoC0(TwoC1(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),X1*),X2*),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("AddPerm1"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("list"),OneCell("list")),Function()),TwoC0(TwoId(OneCell("list")),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),Y);} 
  	 TwoC1(X1*,TwoC0(TwoC1(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),X2*)),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("AddPerm2"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("list"),OneCell("list")),Function()),TwoC0(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoId(OneCell("list"))),Y);} 
  	 TwoC1(TwoC0(TwoC1(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),X1*),X2*),TwoCell("permutationLN",OneC0(OneCell("list"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("list")),Function()),Y*) -> {System.out.println("AddPermLN"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutationLN",OneC0(OneCell("list"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("list")),Function()),TwoC0(TwoId(OneCell("list")),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),Y);} 
  	 TwoC1(X1*,TwoC0(TwoC1(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),X2*)),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),Y*) -> {System.out.println("AddPermNL"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),TwoC0(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoId(OneCell("list"))),Y);} 
  	 TwoC1(TwoC0(TwoC1(TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("nat"),Constructor()),X1*),X2*),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("AppendPerm1"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("list"),OneCell("list")),Function()),TwoC0(TwoId(OneCell("list")),TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor())),Y);} 
  	 TwoC1(X1*,TwoC0(TwoC1(TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("nat"),Constructor()),X2*)),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("AppendPerm2"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutationList",OneC0(OneCell("list"),OneCell("list")),OneC0(OneCell("list"),OneCell("list")),Function()),TwoC0(TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),TwoId(OneCell("list"))),Y);} 
  	 TwoC1(TwoC0(TwoC1(TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),X1*),X2*),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),Y*) -> {System.out.println("AppendPermNL"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),TwoC0(TwoId(OneCell("nat")),TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor())),Y);} 
  	 TwoC1(X1*,TwoC0(TwoC1(TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),X2*)),TwoCell("permutationLN",OneC0(OneCell("list"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("list")),Function()),Y*) -> {System.out.println("AppendPermLN"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutationLN",OneC0(OneCell("list"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("list")),Function()),TwoC0(TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),TwoId(OneCell("nat"))),Y);} 
  	 TwoC1(TwoC0(X1,X2),TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),TwoCell("duplicationList",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("dupplicationAppend ");return `TwoC1(TwoC0(X1,X2),TwoC0(TwoCell("duplicationList",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()),TwoCell("duplicationNat",OneCell("nat"),OneC0(OneCell("nat"),OneCell("nat")),Function())),TwoC0(TwoId(OneCell("list")),TwoCell("permutationLN",OneC0(OneCell("list"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("list")),Function()),TwoId(OneCell("nat"))),TwoC0(TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor())),Y*);}
  	 TwoC1(TwoC0(X1,X2),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("duplicationList",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("dupplicationAdd ");return `TwoC1(TwoC0(X1,X2),TwoC0(TwoCell("duplicationNat",OneCell("nat"),OneC0(OneCell("nat"),OneCell("nat")),Function()),TwoCell("duplicationList",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function())),TwoC0(TwoId(OneCell("nat")),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),TwoId(OneCell("list"))),TwoC0(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),Y*);}
  	 TwoC1(TwoCell("consList",Id(),OneCell("list"),Constructor()),TwoCell("sort",OneCell("list"),OneCell("list"),Function()),Y*) -> {System.out.println("consListSort"); return `TwoC1(TwoCell("consList",Id(),OneCell("list"),Constructor()),Y*);}
  	 TwoC1(TwoC0(X1,TwoCell("consList",Id(),OneCell("list"),Constructor())),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("sort",OneCell("list"),OneCell("list"),Function()),Y*) -> { System.out.println("addSort"); return `TwoC1(TwoC0(X1,TwoCell("consList",Id(),OneCell("list"),Constructor())),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),Y*);}
  	 TwoC1(TwoC0(X1,TwoC1(TwoC0(X2,X3),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()))),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("sort",OneCell("list"),OneCell("list"),Function()),Y*) -> { System.out.println("DoubleAddSort"); return `TwoC1(TwoC0(X1,X2,TwoC1(X3,TwoCell("split",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()))),TwoC0(TwoId(OneCell("nat")),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),TwoId(OneCell("list"))),TwoC0(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),TwoC0(TwoCell("sort",OneCell("list"),OneCell("list"),Function()),TwoCell("sort",OneCell("list"),OneCell("list"),Function())),TwoCell("merge",OneC0(OneCell("list"),OneCell("list")),OneCell("list"),Function()),Y*);}
  	 TwoC1(TwoCell("consList",Id(),OneCell("list"),Constructor()),TwoCell("split",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("consListSplit"); return `TwoC1(TwoC0(TwoCell("consList",Id(),OneCell("list"),Constructor()),TwoCell("consList",Id(),OneCell("list"),Constructor())),Y*);}
  	 TwoC1(TwoC0(X1,TwoCell("consList",Id(),OneCell("list"),Constructor())),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("split",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("AddSplit"); return `TwoC1(TwoC0(TwoC1(TwoC0(X1,TwoCell("consList",Id(),OneCell("list"),Constructor())),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),TwoCell("consList",Id(),OneCell("list"),Constructor())),Y*);}
  	 TwoC1(TwoC0(X1,TwoC1(TwoC0(X2,X3),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()))),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("split",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()),Y*) -> {System.out.println("DoubleAddSplit"); return `TwoC1(TwoC0(X1,X2,TwoC1(X3,TwoCell("split",OneCell("list"),OneC0(OneCell("list"),OneCell("list")),Function()))),TwoC0(TwoId(OneCell("nat")),TwoCell("permutationNL",OneC0(OneCell("nat"),OneCell("list")),OneC0(OneCell("list"),OneCell("nat")),Function()),TwoId(OneCell("list"))),TwoC0(TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),Y*);}
  	 TwoC1(TwoC0(TwoCell("consList",Id(),OneCell("list"),Constructor()),X1),TwoCell("merge",OneC0(OneCell("list"),OneCell("list")),OneCell("list"),Function()),Y*) -> {System.out.println("ConsListMerge1"); return `TwoC1(X1,Y*);}
  	 TwoC1(TwoC0(X1,TwoCell("consList",Id(),OneCell("list"),Constructor())),TwoCell("merge",OneC0(OneCell("list"),OneCell("list")),OneCell("list"),Function()),Y*) -> {System.out.println("ConsListMerge2"); return `TwoC1(X1,Y*);}
  	 TwoC1(TwoC0(TwoCell("consList",Id(),OneCell("list"),Constructor()),X1),TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),Y*) -> { System.out.println("appendToAdd"); return `TwoC1(TwoC0(X1,TwoCell("consList",Id(),OneCell("list"),Constructor())),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),Y);}
  	 TwoC1(TwoC0(TwoC1(TwoC0(X1,X2),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),X3),TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()),Y*) -> { System.out.println("AddAppend"); return `TwoC1(TwoC0(X1,TwoC1(TwoC0(X2,X3),TwoCell("append",OneC0(OneCell("list"),OneCell("nat")),OneCell("list"),Constructor()))),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),Y*);}
  	 //peuvent pas etre generees automatiquement faute d'avoir une modelisation tenant compte des conditions
  	 TwoC1(TwoC0(TwoC1(TwoC0(TwoCell(i1,Id(),OneCell("nat"),Constructor()),X1),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),TwoC1(TwoC0(TwoCell(i2,Id(),OneCell("nat"),Constructor()),X2),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()))),TwoCell("merge",OneC0(OneCell("list"),OneCell("list")),OneCell("list"),Function()),Y*) -> {if(Integer.parseInt(`i1)<=Integer.parseInt(`i2)){System.out.println("merge1 "+`i1+"<="+`i2);return `TwoC1(TwoC0(TwoCell(i1,Id(),OneCell("nat"),Constructor()),TwoC1(TwoC0(X1,TwoC1(TwoC0(TwoCell(i2,Id(),OneCell("nat"),Constructor()),X2),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()))),TwoCell("merge",OneC0(OneCell("list"),OneCell("list")),OneCell("list"),Function()))),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),Y*);}else{System.out.println("merge2 "+`i1+">"+`i2);return `TwoC1(TwoC0(TwoCell(i1,Id(),OneCell("nat"),Constructor()),TwoC1(TwoC0(TwoC1(TwoC0(TwoCell(i2,Id(),OneCell("nat"),Constructor()),X1),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor())),X2),TwoCell("merge",OneC0(OneCell("list"),OneCell("list")),OneCell("list"),Function()))),TwoCell("add",OneC0(OneCell("nat"),OneCell("list")),OneCell("list"),Constructor()),Y*);}}
}
}



public static void NormalizeRules(){
HashSet<ThreePath> normalizedRewritingRules=new HashSet<ThreePath>();
for (Iterator iterator = rewritingRules.iterator(); iterator.hasNext();) {
	ThreePath rule = (ThreePath) iterator.next();
	try{
	TwoPath normalizedSource=(TwoPath)`TopDown(NormalizeSource()).visit(rule.getSource());
	ThreePath normalizedRule=`ThreeCell(rule.getName(),normalizedSource,rule.getTarget(),rule.getType());
	normalizedRewritingRules.add(normalizedRule);
	}
	catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("strange term: "+rule);
    }
}
rewritingRules=normalizedRewritingRules;
}

%strategy NormalizeSource() extends Identity(){ 
  	visit TwoPath {
  	  TwoC0(left*,X,right*) -> {if(`X.sourcesize()>0){ return `TwoC0(left*,TwoC1(TwoId(X.source()),X),right);} } 
 	 } 
}


private static void test(TwoPath myPath){//fonction pour tester la combinaison de toutes les strategies
try{
System.out.println("BEFORE");
print(myPath);
myPath=(TwoPath) `RepeatId(Sequence(RepeatId(TopDown(Gravity())),RepeatId(TopDown(Normalize())),RepeatId(Sequence(TopDown(ApplyRules()),Print())))).visit(myPath);
System.out.println("RESULT");
print(myPath);
//tom.library.utils.Viewer.display(myPath);
}
catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("strange term: " + myPath);
    }
}

// fait tomber les constructeurs
%strategy Gravity() extends Identity(){ 
  	visit TwoPath {
  		TwoC1(head*,f@TwoCell(_,_,_,Constructor()),g@TwoId(_),tail*)->{
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
  		TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor()),tail1*),TwoC0(head2*,g@TwoId(_),tail2*),tail*) -> { 

			if(`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`g.source()){
				//en fait, on n'a pas vraiment besoins de tester les tails si on teste les heads
																											
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
  		TwoC1(head*,f@TwoCell(_,_,_,Constructor()),TwoC0(head2*,g@TwoId(_),tail2*),tail*) -> { 

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
  		  TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor()),tail1*),g@TwoId(_),tail*) -> { 

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



}