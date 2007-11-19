package polygraphesnat;

import polygraphesnat.*;
import polygraphesnat.types.*;
import tom.library.sl.*;

import java.util.HashSet;
import java.util.Iterator;

public class PolygraphesNat{

%include { polygraphesnat/PolygraphesNat.tom }
%include { sl.tom }

private static HashSet<ThreePath> rewritingRules=new HashSet<ThreePath>();


//-----------------------------------------------------------------------------
// jeu de cellules pour les tests : inutile pour le compilateur final, c'esr juste plus pratique
//-----------------------------------------------------------------------------
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
// 3-cellules de structure
private static	ThreePath zeroPerm1 = `ThreeCell("zeroPerm1",TwoC1(TwoC0(zero,TwoId(nat)),permutation),TwoC0(TwoId(nat),zero),Function());
private static	ThreePath zeroPerm2 = `ThreeCell("zeroPerm2",TwoC1(TwoC0(TwoId(nat),zero),permutation),TwoC0(zero,TwoId(nat)),Function());
private static	ThreePath zeroDup = `ThreeCell("zeroDup",TwoC1(zero,duplication),TwoC0(zero,zero),Function());
private static	ThreePath zeroEraz = `ThreeCell("zeroEraz",TwoC1(zero,eraser),TwoId(Id()),Function());
private static	ThreePath succPerm1 = `ThreeCell("succPerm1",TwoC1(TwoC0(succ,TwoId(nat)),permutation),TwoC1(permutation,TwoC0(TwoId(nat),succ)),Function());
private static	ThreePath succPerm2 = `ThreeCell("succPerm2",TwoC1(TwoC0(TwoId(nat),succ),permutation),TwoC1(permutation,TwoC0(succ,TwoId(nat))),Function());
private static	ThreePath succDup = `ThreeCell("succDup",TwoC1(succ,duplication),TwoC1(duplication,TwoC0(succ,succ)),Function());
private static	ThreePath succEraz = `ThreeCell("succEraz",TwoC1(succ,eraser),TwoC1(TwoId(nat),eraser),Function());
// regles
private static	ThreePath plusZero = `ThreeCell("plusZero",TwoC1(TwoC0(zero,TwoId(nat)),plus),TwoId(nat),Function());
private static	ThreePath plusSucc = `ThreeCell("plusSucc",TwoC1(TwoC0(succ,TwoId(nat)),plus),TwoC1(plus,succ),Function());
private static	ThreePath minusZero1 = `ThreeCell("minusZero1",TwoC1(TwoC0(zero,TwoId(nat)),minus),TwoC1(eraser,zero),Function());
private static	ThreePath minusZero2 = `ThreeCell("minusZero2",TwoC1(TwoC0(TwoId(nat),zero),minus),TwoId(nat),Function());
private static	ThreePath minusDoubleSucc = `ThreeCell("minusDoubleSucc",TwoC1(TwoC0(succ,succ),minus),minus,Function());
private static	ThreePath divZero = `ThreeCell("divZero",TwoC1(TwoC0(zero,TwoId(nat)),division),TwoC1(eraser,zero),Function());	
private static	ThreePath divSucc = `ThreeCell("divSucc",TwoC1(TwoC0(succ,TwoId(nat)),division),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(minus,TwoId(nat)),division,succ),Function());
private static	ThreePath multZero = `ThreeCell("multZero",TwoC1(TwoC0(zero,TwoId(nat)),multiplication),TwoC1(eraser,zero),Function());	
private static	ThreePath multSucc = `ThreeCell("multSucc",TwoC1(TwoC0(succ,TwoId(nat)),multiplication),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(multiplication,TwoId(nat)),plus),Function());
//-----------------------------------------------------------------------------
// on regroupe les regles en les ajoutant a une collection
//-> l'ordre peut etre important afin d'aller plus vite (mais ici je n'ai pas fait attention)
//-----------------------------------------------------------------------------

public static void main(String[] args) {
/*ewritingRules.add(zeroPerm1);
rewritingRules.add(zeroPerm2);
rewritingRules.add(zeroDup);
rewritingRules.add(zeroEraz);
rewritingRules.add(succPerm1);
rewritingRules.add(succPerm2);
rewritingRules.add(succDup);
rewritingRules.add(succEraz);
rewritingRules.add(plusZero);
rewritingRules.add(plusSucc);
rewritingRules.add(minusZero1);
rewritingRules.add(minusZero2);
rewritingRules.add(minusDoubleSucc);
rewritingRules.add(divZero);
rewritingRules.add(divSucc);
rewritingRules.add(multZero);
rewritingRules.add(multSucc);

NormalizeRules();
//-----------------------------------------------------------------------------
// idee de methode pour le compilateur:
// -explorer le xml et extraire tous les chemins comme ci-dessus
// -regrouper tous les 3-cellules au sein d'une collection
// alterner gravity-verticalmerging-applyRules jusuq'ˆ atteindre un point fixe
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
// TESTS
//-----------------------------------------------------------------------------
*/
// tom.library.utils.Viewer.display(testGravity);
TwoPath test=`TwoC1(TwoC0(TwoC1(zero,TwoId(nat),succ,TwoId(nat),TwoC0(zero,duplication),TwoC0(TwoId(nat),TwoId(nat),succ),TwoC0(plus,TwoId(nat)),TwoC0(TwoId(nat),eraser)),TwoC1(zero,TwoId(nat),TwoId(nat),succ,TwoId(nat),eraser),TwoC0(TwoC1(zero,TwoId(nat),TwoId(nat)),TwoC1(succ,TwoId(nat),eraser))),TwoC0(TwoC1(succ,TwoId(nat),succ),TwoC1(TwoId(nat),eraser)));
//<--marche
TwoPath test2=`TwoC1(TwoC0(TwoC1(TwoC0(zero,TwoId(nat)),TwoC0(TwoId(nat),succ),TwoC0(plus,zero),TwoC0(TwoId(nat),succ),TwoC0(division,zero),TwoC0(succ,TwoId(nat))),TwoC1(TwoId(nat),succ,succ,duplication,TwoC0(TwoId(nat),succ),TwoC0(succ,TwoId(nat))),TwoC1(zero,TwoId(nat),TwoId(nat)),TwoC1(TwoC1(zero,TwoId(nat),succ),succ)),TwoC0(TwoC1(TwoId(nat),succ,eraser),TwoC0(TwoC1(plus,succ),succ),TwoC1(minus,eraser)));
//<--ne marche pas encore ˆ cause des chevauchementsˆ l'intersection des deux etages
TwoPath test3 = `TwoC1(TwoC0(TwoC1(zero,TwoC0(succ,zero),TwoC0(succ,succ),minus),TwoC0(TwoC1(zero,TwoC0(TwoId(nat),zero),TwoC0(succ,succ),TwoC0(succ,succ),minus),TwoC1(zero,TwoC0(TwoId(nat),zero),TwoC0(succ,TwoId(nat)),TwoC0(succ,succ),plus)),TwoC0(TwoC1(TwoC0(zero,zero),TwoC0(succ,TwoId(nat)),TwoC0(succ,TwoId(nat)),plus),TwoC1(zero,TwoC0(succ,zero),TwoC0(succ,TwoId(nat)),minus))),TwoC0(permutation,TwoId(nat),TwoId(nat),TwoId(nat)),TwoC0(TwoId(nat),TwoId(nat),TwoId(nat),eraser,eraser));
//<--marche bien
TwoPath test4 = `TwoC1(zero,succ,succ,succ,TwoC0(succ,zero),TwoC0(succ,succ),TwoC0(succ,succ),division);
//<--resultat faux
TwoPath test5=`TwoC1(TwoC0(TwoC1(zero,succ),TwoC1(zero,succ)),plus);
TwoPath test6=`TwoC1(TwoC0(zero,zero),TwoC0(succ,succ),plus);
TwoPath test7=`TwoC1(TwoC0(test6,test6),plus);
TwoPath test8=`TwoC1(TwoC0(test5,test5),plus);

TwoPath rule=`TwoC1(TwoC0(zero,zero),TwoC0(succ,succ),permutation,minus,eraser);
TwoPath rule2=`TwoC1(zero,TwoC0(succ,zero),division,succ);
TwoPath rule3=`TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),TwoC1(zero,succ,succ)),multiplication);
TwoPath rule4=`TwoC1(zero,succ,succ,TwoC0(succ,zero),TwoC0(succ,succ),TwoC0(succ,succ),division);
TwoPath rule5=`TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),TwoC1(zero,succ,succ)),multiplication);


test(rule5);

}

//-----------------------------------------------------------------------------
// STRATEGIES version 2 
//-----------------------------------------------------------------------------

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


// le Y a la fin est important(meme si le * est inutil pour ces exemples)
%strategy ApplyRules() extends Identity(){ 
  	visit TwoPath {
  	  TwoC1(TwoC0(TwoCell("zero",Id(),OneCell("nat"),Constructor()),X1*),TwoCell("permutation",OneC0(OneCell("nat"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("ZeroPerm1");return `TwoC1(TwoC0(X1,TwoCell("zero",Id(),OneCell("nat"),Constructor())),Y); } 
  	  TwoC1(TwoC0(X1*,TwoCell("zero",Id(),OneCell("nat"),Constructor())),TwoCell("permutation",OneC0(OneCell("nat"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("ZeroPerm2");return `TwoC1(TwoC0(TwoCell("zero",Id(),OneCell("nat"),Constructor()),X1),Y); } 
  	  TwoC1(TwoCell("zero",Id(),OneCell("nat"),Constructor()),TwoCell("duplication",OneCell("nat"),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> { System.out.println("ZeroDup"); return `TwoC1(TwoC0(TwoCell("zero",Id(),OneCell("nat"),Constructor()),TwoCell("zero",Id(),OneCell("nat"),Constructor())),Y*);}
  	  TwoC1(TwoCell("zero",Id(),OneCell("nat"),Constructor()),TwoCell("eraser",OneCell("nat"),Id(),Function()),Y*) -> {System.out.println("ZeroEraz"); return `Y*;}
  	  TwoC1(TwoC0(TwoC1(X1*,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor())),X2*),TwoCell("permutation",OneC0(OneCell("nat"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("SuccPerm1"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutation",OneC0(OneCell("nat"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("nat")),Function()),TwoC0(TwoId(OneCell("nat")),TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor())),Y*);}
  	  TwoC1(TwoC0(X1,TwoC1(X2*,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()))),TwoCell("permutation",OneC0(OneCell("nat"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("SuccPerm2"); return `TwoC1(TwoC0(X1,X2),TwoCell("permutation",OneC0(OneCell("nat"),OneCell("nat")),OneC0(OneCell("nat"),OneCell("nat")),Function()),TwoC0(TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()),TwoId(OneCell("nat"))),Y*);}
  	  TwoC1(TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()),TwoCell("duplication",OneCell("nat"),OneC0(OneCell("nat"),OneCell("nat")),Function()),Y*) -> {System.out.println("SuccDup"); return `TwoC1(TwoCell("duplication",OneCell("nat"),OneC0(OneCell("nat"),OneCell("nat")),Function()),TwoC0(TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()),TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor())),Y*);}
  	  TwoC1(TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()),TwoCell("eraser",OneCell("nat"),Id(),Function()),Y*) -> { System.out.println("SuccEraz"); return `TwoC1(TwoCell("eraser",OneCell("nat"),Id(),Function()),Y*);}
  	  TwoC1(TwoC0(TwoC1(X1*,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor())),X2*),TwoCell("plus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*)->{System.out.println("plusSucc");return `TwoC1(TwoC0(X1,X2),TwoCell("plus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()),Y*);}
  	  TwoC1(TwoC0(TwoCell("zero",Id(),OneCell("nat"),Constructor()),X1*),TwoCell("plus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*) -> { System.out.println("plusZero"); return `TwoC1(X1,Y*);}
  	  TwoC1(TwoC0(TwoCell("zero",Id(),OneCell("nat"),Constructor()),X1*),TwoCell("minus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*) -> { System.out.println("minusZero1"); return `TwoC1(X1,TwoCell("eraser",OneCell("nat"),Id(),Function()),TwoCell("zero",Id(),OneCell("nat"),Constructor()),Y*);}
  	  TwoC1(TwoC0(X1*,TwoCell("zero",Id(),OneCell("nat"),Constructor())),TwoCell("minus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*) -> { System.out.println("minusZero2"); return `TwoC1(X1,Y*);}
  	  TwoC1(TwoC0(TwoC1(X1*,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor())),TwoC1(X2*,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()))),TwoCell("minus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*) -> {System.out.println("minusDoubleSucc");return `TwoC1(TwoC0(X1,X2),TwoCell("minus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*);}
  	  TwoC1(TwoC0(TwoCell("zero",Id(),OneCell("nat"),Constructor()),X1*),TwoCell("division",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*) -> { System.out.println("divZero"); return `TwoC1(X1,TwoCell("eraser",OneCell("nat"),Id(),Function()),TwoCell("zero",Id(),OneCell("nat"),Constructor()),Y*);}
  	  TwoC1(TwoC0(TwoC1(X1*,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor())),X2*),TwoCell("division",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*) -> { System.out.println("divSucc"); return `TwoC1(TwoC0(TwoC1(X1,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor())),TwoC1(X2,TwoCell("duplication",OneCell("nat"),OneC0(OneCell("nat"),OneCell("nat")),Function()))),TwoC0(TwoCell("minus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),TwoId(OneCell("nat"))),TwoCell("division",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor()),Y*);}
  	  TwoC1(TwoC0(TwoCell("zero",Id(),OneCell("nat"),Constructor()),X1*),TwoCell("multiplication",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*) -> { System.out.println("multZero"); return `TwoC1(X1,TwoCell("eraser",OneCell("nat"),Id(),Function()),TwoCell("zero",Id(),OneCell("nat"),Constructor()),Y*);}
  	  TwoC1(TwoC0(TwoC1(X1*,TwoCell("succ",OneCell("nat"),OneCell("nat"),Constructor())),X2*),TwoCell("multiplication",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*) -> { System.out.println("multSucc"); return `TwoC1(X2,TwoC0(X1,TwoCell("duplication",OneCell("nat"),OneC0(OneCell("nat"),OneCell("nat")),Function())),TwoC0(TwoCell("multiplication",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),TwoId(OneCell("nat"))),TwoCell("plus",OneC0(OneCell("nat"),OneCell("nat")),OneCell("nat"),Function()),Y*);}
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