package polygraphicprogram;

import polygraphicprogram.*;
import polygraphicprogram.types.*;
import tom.library.sl.*;

import java.util.HashSet;
import java.util.Iterator;

public class PolygraphicProgram{

%include { polygraphicprogram/PolygraphicProgram.tom }
%include { sl.tom }

private static HashSet<ThreePath> rewritingRules=new HashSet<ThreePath>();

public static void main(String[] args) {

//-----------------------------------------------------------------------------
// jeu de cellules pour les tests :
//-----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
	OnePath nat=`OneCell("nat");
// constructeurs sur les entiers naturels
	TwoPath zero=`TwoCell("zero",Id(),nat,Constructor());
	TwoPath succ =`TwoCell("succ",nat,nat,Constructor());
// 2-cellules de structure
	TwoPath eraser= `TwoCell("eraser",nat,Id(),Function());
	TwoPath duplication= `TwoCell("duplication",nat,OneC0(nat,nat),Function());
	TwoPath permutation = `TwoCell("permutation",OneC0(nat,nat),OneC0(nat,nat),Function());
// addition, soustraction et division
	TwoPath plus = `TwoCell("plus",OneC0(nat,nat),nat,Function());
	TwoPath minus = `TwoCell("minus",OneC0(nat,nat),nat,Function());
	TwoPath division = `TwoCell("division",OneC0(nat,nat),nat,Function());
	TwoPath multiplication = `TwoCell("multiplication",OneC0(nat,nat),nat,Function());
// 3-cellules de structure
	ThreePath zeroPerm1 = `ThreeCell("zeroPerm1",TwoC1(TwoC0(zero,TwoId(nat)),permutation),TwoC0(TwoId(nat),zero),Function());
	ThreePath zeroPerm2 = `ThreeCell("zeroPerm2",TwoC1(TwoC0(TwoId(nat),zero),permutation),TwoC0(zero,TwoId(nat)),Function());
	ThreePath zeroDup = `ThreeCell("zeroDup",TwoC1(zero,duplication),TwoC0(zero,zero),Function());
	ThreePath zeroEraz = `ThreeCell("zeroEraz",TwoC1(zero,eraser),TwoId(Id()),Function());
	ThreePath succPerm1 = `ThreeCell("succPerm1",TwoC1(TwoC0(succ,TwoId(nat)),permutation),TwoC1(permutation,TwoC0(TwoId(nat),succ)),Function());
	ThreePath succPerm2 = `ThreeCell("succPerm2",TwoC1(TwoC0(TwoId(nat),succ),permutation),TwoC1(permutation,TwoC0(succ,TwoId(nat))),Function());
	ThreePath succDup = `ThreeCell("succDup",TwoC1(succ,duplication),TwoC1(duplication,TwoC0(succ,succ)),Function());
	ThreePath succEraz = `ThreeCell("succEraz",TwoC1(succ,eraser),TwoC1(TwoId(nat),eraser),Function());
// regles
	ThreePath plusZero = `ThreeCell("plusZero",TwoC1(TwoC0(zero,TwoId(nat)),plus),TwoId(nat),Function());
	ThreePath plusSucc = `ThreeCell("plusSucc",TwoC1(TwoC0(succ,TwoId(nat)),plus),TwoC1(plus,succ),Function());
	ThreePath minusZero1 = `ThreeCell("minusZero1",TwoC1(TwoC0(zero,TwoId(nat)),minus),TwoC1(eraser,zero),Function());
	ThreePath minusZero2 = `ThreeCell("minusZero2",TwoC1(TwoC0(TwoId(nat),zero),minus),TwoId(nat),Function());
	ThreePath minusDoubleSucc = `ThreeCell("minusDoubleSucc",TwoC1(TwoC0(succ,succ),minus),minus,Function());
	ThreePath divZero = `ThreeCell("divZero",TwoC1(TwoC0(zero,TwoId(nat)),division),TwoC1(eraser,zero),Function());	
	ThreePath divSucc = `ThreeCell("divSucc",TwoC1(TwoC0(succ,TwoId(nat)),division),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(minus,TwoId(nat)),division,succ),Function());
	ThreePath multZero = `ThreeCell("multZero",TwoC1(TwoC0(zero,TwoId(nat)),multiplication),TwoC1(eraser,zero),Function());	
	ThreePath multSucc = `ThreeCell("multSucc",TwoC1(TwoC0(succ,TwoId(nat)),multiplication),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(multiplication,TwoId(nat)),plus),Function());
//-----------------------------------------------------------------------------
// on regroupe les regles en les ajoutant a une collection
//-> l'ordre peut etre important afin d'aller plus vite (mais ici je n'ai pas fait attention)
//-----------------------------------------------------------------------------
rewritingRules.add(zeroPerm1);
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

Compile(test);
}
//-----------------------------------------------------------------------------
// fin du main
//-----------------------------------------------------------------------------

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
  	  	TwoC1(head*,top@TwoC0(X*),down@TwoC0(Y*),f@TwoCell(_,_,_,Function()),tail*) -> {
  	  		int length=`top.length();
  	  		TwoPath myNewPath=`TwoId(Id());
  	  		for(int i=0;i<length;i++){
  	  			TwoPath newC1=`TwoC1((TwoPath)top.getChildAt(i),(TwoPath)down.getChildAt(i));
  	  			if(i==0){myNewPath=`newC1;}
  	  			else if(i==1){myNewPath=`TwoC0(myNewPath,newC1);}
  	  			else{myNewPath.setChildAt(i,newC1);}
  	  		}
  	  		myNewPath=`TwoC1(head,myNewPath,f,tail);
  	  		return myNewPath;
  	  		}
 	 } 
}

public static void applyRule(ThreePath myRule){
int sourcesize=myRule.getSource().sourcesize();

}

public static String formatPattern(TwoPath ruleSource){


return ruleSource.toString();
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
System.out.println("BEFORE\n"+myPath);
myPath=(TwoPath) `RepeatId(TopDown(Normalize())).visit(myPath);
System.out.println("RESULT\n"+myPath);
//tom.library.utils.Viewer.display(myPath);
}
catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("strange term: " + myPath);
    }
}

//-----------------------------------------------------------------------------
// STRATEGIES version 1 
//-----------------------------------------------------------------------------

%strategy Print() extends Identity(){ //ne sert a rien pour l'instant
  	visit TwoPath {
  	  x -> { System.out.println(`x); } 
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
				return `TwoC1(head*,TwoId(f.source()),f,tail*);
				}
}
  		TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor()),tail1*),TwoC0(head2*,g@TwoId(_),tail2*),tail*) -> { 

			if(`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`g.source()){
				//en fait, on n'a pas vraiment besoins de tester les tails si on teste les heads
																											
				if(`head*==`TwoId(Id())){
					if(`tail*==`TwoId(Id())){return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
					return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
				}
				if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
				return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
			}
  	  }
  		TwoC1(head*,f@TwoCell(_,_,_,Constructor()),TwoC0(head2*,g@TwoId(_),tail2*),tail*) -> { 

			if(`f.target()==`g.source()){

																											
				if(`head*==`TwoId(Id())){
					if(`tail*==`TwoId(Id())){return `TwoC0(head2*,f,tail2*);}
					return `TwoC1(TwoC0(head2*,f,tail2*),tail*);
				}
				if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head2*,f,tail2*));}
				return `TwoC1(head*,TwoC0(head2*,f,tail2*),tail*);
			}
  	  }
  		  TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor()),tail1*),g@TwoId(_),tail*) -> { 

				if(`f.target()==`g.source()){
				if(`head*==`TwoId(Id())){
					if(`tail*==`TwoId(Id())){return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),f);}
					return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),f,tail*);
				}
				if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),f);}
				return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),f,tail*);
			}
  	  }
  	} 
}

%strategy VerticalSplitting() extends Identity(){ 
  	visit TwoPath {
  	 TwoC0(head*,TwoC1(f@TwoCell(_,_,_,_),g@TwoCell(_,_,_,_)),tail*) -> {return `TwoC1(TwoC0(TwoId(head*.source()),f,TwoId(tail*.source())),TwoC0(head*,g,tail*));} 
 	 TwoId(OneC0(head,tail*)) -> { return `TwoC0(TwoId(head),TwoId(tail*)); } //correction en mme temps
  	} 
}

%strategy RefactorSource() extends Identity(){ 
  	visit TwoPath {
  	 TwoId(OneC0(head,tail*)) -> { return `TwoC0(TwoId(head),TwoId(tail*)); } //correction en mme temps
  	} 
}


// sorte de normalisation en stratifiant de faon verticale
%strategy VerticalMerging() extends Identity(){ //ne prend pas en compte les croisement entre twoC1 de deux niveaux differents
  	visit TwoPath {
  	  //TwoC1(TwoC0(head1*,TwoC1(top*),tail1*),TwoC0(head2*,TwoC1(bottom*),tail2*)) -> { if(`head1*.target()==`head2*.source()&&`top*.target()==`bottom*.source()){return `TwoC0(TwoC1(head1*,head2*),TwoC1(top*,bottom*),TwoC1(tail1*,tail2*));}} 
  		TwoC1(TwoC0(head1*,TwoC1(top*),tail1*),TwoC0(head2*,bottom*,tail2*)) -> {if(`head1*.target()==`head2*.source()&&`top*.target()==`bottom*.source()){System.out.println("1");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top*,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head1*,top@TwoCell(_,_,_,_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("2");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head1*,top@TwoId(_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.println("3");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head*,TwoC1(top*),tail*),bottom*) -> {if(`top*.target()==`bottom*.source()){System.out.println("4");return `TwoC0(head*,TwoC1(top*,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@TwoCell(_,_,_,_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("5");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@TwoId(_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.println("6");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  //un peu ˆ part, permet de remettre les choses bien apres application de regles, sinon on a des TwoC1 dans des TwoC1 parce que les
  	  //reles on ete appliquees au milieu de l'arbre lors de son parcourt
  	  	TwoC1(head*,TwoC1(X*),tail*)->{if(`head*!=`TwoId(Id())) return `TwoC1(head*,X*,tail*);}
  	  //meme principe, correction d'un autre probleme avec des TwoC0(TwoC1(eraz,succ),TwoCell)
  	  	TwoC0(left*,TwoC1(s*,e@TwoCell(_,_,Id(),_),z@TwoCell(_,Id(),_,_)),right*)->{if(`s.isTwoId()){return `TwoC1(TwoC0(TwoId(left.source()),e,TwoId(right.source())),TwoC0(left*,z,right*));}}
} 
}

// strategie pour detecter les sources des 3-cellules
//attention : limite aux arguments de hauteur egale a un
%strategy ApplyRules() extends Identity(){//il reste encore  des cas non couverts
  	visit TwoPath {
  		TwoC1(head*,args,TwoC0(X*,f@TwoCell(_,_,_,_),Y*),tail*)->{//necessaire pour les cas similaires a la duplication
  		if(`args.target()==`f.source()){//peut etre inutile a present avec le match suivant
  		TwoPath target=checkRules(`TwoC1(args,f));if(target!=null){
  		  %match(target){
  		  TwoC1(A,B)->{
  		    		  if(`head*==`TwoId(Id())){
  		  return `TwoC1(A,TwoC0(X*,B,Y*),tail*);}
  		  	return `TwoC1(head*,A,TwoC0(X*,B,Y*),tail*);
  		  	}
  		  TwoC0(A*)->{
  			  if(`head*==`TwoId(Id())){
  		  return `TwoC1(TwoC0(X,A*,Y*),tail*);}
  		  	return `TwoC1(head*,TwoC0(X*,A*,Y*),tail*);
  		  	}
  		  }
  		  }
  		}//pas suffisant, d'ou la suite
  		%match (args){//le * a args2 est important
			TwoC0(H*,args2*,T*) -> { if(`args2*.target()==`f.source()){
				TwoPath target=checkRules(`TwoC1(args2,f));if(target!=null){
				TwoPath targetSource=`TwoId(target.source());
				targetSource =(TwoPath) `RepeatId(RefactorSource()).visit(targetSource);		
  		  %match(target){
  		  TwoC1(A,B)->{
  		    		  if(`head*==`TwoId(Id())){
  		  return `TwoC1(TwoC0(H*,A,T*),TwoC0(X*,B,Y*),tail*);}
  		  System.out.println("***"+`TwoC1(head*,TwoC0(H*,A,T*),TwoC0(X*,B,Y*),tail*));
  		  	return `TwoC1(head*,TwoC0(H*,A,T*),TwoC0(X*,B,Y*),tail*);
  		  	}
  		  TwoC0(A*)->{
  			  if(`head*==`TwoId(Id())){
  		  return `TwoC1(TwoC0(H*,targetSource,T*),TwoC0(X*,A*,Y*),tail*);}
  		  	return `TwoC1(head*,TwoC0(H*,targetSource,T*),TwoC0(X*,A*,Y*),tail*);
  		  	}
  		  A->{
  			  if(`head*==`TwoId(Id())){
  		  return `TwoC1(TwoC0(H*,targetSource,T*),TwoC0(X*,A,Y*),tail*);}
  		  	return `TwoC1(head*,TwoC0(H*,targetSource,T*),TwoC0(X*,A,Y*),tail*);
  		  	}
  		  }
  		  }
			}
			}
  		}
  		}
  	  TwoC1(head*,args,f@TwoCell(_,_,_,_/*Function()*/),tail*) -> {
  	  TwoPath target=checkRules(`TwoC1(args,f));if(target!=null){
  		  if(`head*==`TwoId(Id())){
  		  return `TwoC1(target,tail*);}
  		  	return `TwoC1(head*,target,tail*);}
  	  		}// marche mais pas tout le temps
  	  	 } // chercher pour chaque 3 cellule au niveau des sources, retourner
			// la target
}

// fonction pour explorer les regles de reecriture
private static TwoPath checkRules(TwoPath source){
for (Iterator<ThreePath> iterator = rewritingRules.iterator(); iterator.hasNext();) {
	ThreePath rewritingRule = (ThreePath) iterator.next();

	TwoPath ruleSource=rewritingRule.getSource();
		%match (source,ruleSource){
			TwoC1(TwoC0(head*,X,tail*),f@TwoCell(_,_,_,_)),TwoC1(TwoC0(head*,TwoId(Y),tail*),g@TwoCell(_,_,_,_)) -> {
				if(`X.target()==`Y&&`f==`g){
				System.out.println(rewritingRule.getName());
				/*System.out.println(`head*);
				System.out.println(`X);
				System.out.println(`tail*);
				System.out.println(`TwoC0(TwoId(head*.source()),X,TwoC0(TwoId(tail*.source()))));
				System.out.println(rewritingRule.getTarget());*/
				TwoPath ruleTarget=rewritingRule.getTarget();
				%match(ruleTarget){//on fait un peu de travail de simplification ˆ la volŽe-->ya moyen de faire mieux
					TwoId(_)->{return `TwoC0(TwoId(head*.source()),X,TwoC0(TwoId(tail*.source())));}
					TwoC1(A@_*,TwoId(_))->{return `TwoC1(TwoC0(TwoId(head*.source()),X,TwoC0(TwoId(tail*.source()))),A);}
					TwoC1(A@_*)->{return `TwoC1(TwoC0(TwoId(head*.source()),X,TwoC0(TwoId(tail*.source()))),A);}
				}
				return `TwoC1(TwoC0(TwoId(head*.source()),X,TwoC0(TwoId(tail*.source()))),ruleTarget);			
				}
			}

		}
	if(rewritingRule.getSource()==source){//cas triviaux, cellules de structures
		System.out.println(rewritingRule.getName());
		TwoPath ruleTarget=rewritingRule.getTarget();
		%match(ruleTarget){//on fait un peu de travail de simplification ˆ la volŽe
					TwoC1(TwoId(_),E)->{return `E;}//cas particulier des erasers
				}
		return ruleTarget;
	}
}
return null;
} 



//-----------------------------------------------------------------------------
private static void Compile(TwoPath myPath){//fonction pour tester la combinaison de toutes les strategies
try{
System.out.println("BEFORE\n"+myPath);
myPath=(TwoPath) `RepeatId(Sequence(RepeatId(TopDown(ApplyRules())),RepeatId(TopDown(VerticalMerging())),RepeatId(TopDown(Gravity())))).visit(myPath);
//myPath=(TwoPath) `RepeatId(Sequence(RepeatId(TopDown(ApplyRules())),RepeatId(TopDown(VerticalSplitting())),RepeatId(TopDown(VerticalMerging())),RepeatId(TopDown(Gravity())))).visit(myPath);
System.out.println("RESULT\n"+myPath);
//tom.library.utils.Viewer.display(myPath);
}
catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("strange term: " + myPath);
    }
}


}