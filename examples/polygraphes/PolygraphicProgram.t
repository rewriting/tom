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
	TwoPath eraser= `TwoCell("eraser",nat,Id(),Structure());
	TwoPath duplication= `TwoCell("duplication",nat,OneC0(nat,nat),Structure());
	TwoPath permutation = `TwoCell("permutation",OneC0(nat,nat),OneC0(nat,nat),Structure());
// addition, soustraction et division
	TwoPath plus = `TwoCell("plus",OneC0(nat,nat),nat,Function());
	TwoPath minus = `TwoCell("minus",OneC0(nat,nat),nat,Function());
	TwoPath division = `TwoCell("division",OneC0(nat,nat),nat,Function());
	TwoPath multiplication = `TwoCell("multiplication",OneC0(nat,nat),nat,Function());
// 3-cellules de structure
	ThreePath zeroPerm1 = `ThreeCell("zeroPerm1",TwoC1(TwoC0(zero,TwoId(nat)),permutation),TwoC0(TwoId(nat),zero),Structure());
	ThreePath zeroPerm2 = `ThreeCell("zeroPerm2",TwoC1(TwoC0(TwoId(nat),zero),permutation),TwoC0(zero,TwoId(nat)),Structure());
	ThreePath zeroDup = `ThreeCell("zeroDup",TwoC1(zero,duplication),TwoC0(zero,zero),Structure());
	ThreePath zeroEraz = `ThreeCell("zeroEraz",TwoC1(zero,eraser),TwoId(Id()),Structure());
	ThreePath succPerm1 = `ThreeCell("succPerm1",TwoC1(TwoC0(succ,TwoId(nat)),permutation),TwoC1(permutation,TwoC0(TwoId(nat),succ)),Structure());
	ThreePath succPerm2 = `ThreeCell("succPerm2",TwoC1(TwoC0(TwoId(nat),succ),permutation),TwoC1(permutation,TwoC0(succ,TwoId(nat))),Structure());
	ThreePath succDup = `ThreeCell("succDup",TwoC1(succ,duplication),TwoC1(duplication,TwoC0(succ,succ)),Structure());
	ThreePath succEraz = `ThreeCell("succEraz",TwoC1(succ,eraser),TwoC1(TwoId(nat),eraser),Structure());
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
//<--OBJECTIF : obtenir un resultat bon pour test, la duplication ne passe pas dans cet exemple
TwoPath test2=`TwoC1(TwoC0(TwoC1(TwoC0(zero,TwoId(nat)),TwoC0(TwoId(nat),succ),TwoC0(plus,zero),TwoC0(TwoId(nat),succ),TwoC0(division,zero),TwoC0(succ,TwoId(nat))),TwoC1(TwoId(nat),succ,succ,duplication,TwoC0(TwoId(nat),succ),TwoC0(succ,TwoId(nat))),TwoC1(zero,TwoId(nat),TwoId(nat)),TwoC1(TwoC1(zero,TwoId(nat),succ),succ)),TwoC0(TwoC1(TwoId(nat),succ,eraser),TwoC0(TwoC1(plus,succ),succ),TwoC1(minus,eraser)));
//<--ne marche pas encore ˆ cause des chevauchementsˆ l'intersection des deux etages
TwoPath test3 = `TwoC1(TwoC0(TwoC1(zero,TwoC0(succ,zero),TwoC0(succ,succ),minus),TwoC0(TwoC1(zero,TwoC0(TwoId(nat),zero),TwoC0(succ,succ),TwoC0(succ,succ),minus),TwoC1(zero,TwoC0(TwoId(nat),zero),TwoC0(succ,TwoId(nat)),TwoC0(succ,succ),plus)),TwoC0(TwoC1(TwoC0(zero,zero),TwoC0(succ,TwoId(nat)),TwoC0(succ,TwoId(nat)),plus),TwoC1(zero,TwoC0(succ,zero),TwoC0(succ,TwoId(nat)),minus))),TwoC0(TwoId(nat),TwoId(nat),TwoId(nat),eraser,eraser));
//<--marche bien




Compile(test3);

}
//-----------------------------------------------------------------------------
// fin du main
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
// des fonctions servant a preparer les strategies-->inutile de lire cette partie
//-----------------------------------------------------------------------------
public static void splitting(TwoPath t){// splitting
%match (t){
		 TwoC0(head*,TwoC1(f@TwoCell(_,_,_,_),g@TwoCell(_,_,_,_)),tail*) -> {System.out.println(`TwoC1(TwoC0(head,f,tail),TwoC0(TwoId(head.target()),g,TwoId(tail.target()))));} 
		
}
// TwoC1(X*,TwoC0(head*,TwoC1(f@TwoCell(_,_,_,_),g@TwoCell(_,_,_,_)),tail*),Y*)->{System.out.println(`TwoC1(X*,TwoC0(head,f,tail),TwoC0(TwoId(head.target()),g,TwoId(tail.target())),Y*));}
}
public static void splitting2(TwoPath t){// splitting plus rapide : on fait
											// tomber un peu de gravity dans les
											// head et tail
%match (t){
		 TwoC0(head*,TwoC1(f@TwoCell(_,_,_,_),g@TwoCell(_,_,_,_)),tail*) -> {System.out.println(`TwoC1(TwoC0(TwoId(head.source()),f,TwoId(tail.source())),TwoC0(head,g,tail)));} 

}
// TwoC1(X*,TwoC0(head*,TwoC1(f@TwoCell(_,_,_,_),g@TwoCell(_,_,_,_)),tail*),Y*)->
// {System.out.println(`TwoC1(X*,TwoC0(TwoId(head.source()),f,TwoId(tail.source())),TwoC0(head,g,tail),Y*));}
}
public static void simplify(TwoPath t){// marche bien mais qu'une fois et il
										// faut que le motif soit ˆ la racine
%match (t){
			TwoId(OneC0(head,tail*)) -> { System.out.println(`TwoC0(TwoId(head),TwoId(tail*))); }
}
}
public static void gravity(TwoPath t){// gravity ajouterhead* et tail* apres
										// dans le TwoC1()?
%match (t){
			TwoC1(TwoC0(head1*,f@TwoCell(_,_,_,Constructor()),tail1*),TwoC0(head2*,X,tail2*)) -> { if(`X.isTwoId()&&`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`X.source()){System.out.println(`TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*)));}}
			}
// TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor()),tail1*),TwoC0(head2*,X,tail2*),tail*)
// -> {
// if(`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`X.source()){System.out.println(`TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*));}}
}
public static TwoPath gravity2(TwoPath t){// gravity ajouterhead* et tail*
											// apres dans le TwoC1()?
%match (t){

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

			TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor()),tail1*),TwoC0(head2*,X,tail2*),tail*) -> { 
			/*
			 * if(`X.isTwoId()&&`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`X.source()){System.out.println(`f);
			 * System.out.println(`X); System.out.println(`head1*);
			 * System.out.println(`head2*); System.out.println(`tail1*);
			 * System.out.println(`tail2*); System.out.println(`head*);
			 * System.out.println(`tail*);
			 *  }
			 */
			if(`X.isTwoId()&&`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`X.source()){
if(`head*==`TwoId(Id())){
if(`tail*==`TwoId(Id())){return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
}
if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
}// END IF
}
			}
return `TwoId(Id());
}

//-----------------------------------------------------------------------------
// STRATEGIES
//-----------------------------------------------------------------------------

%strategy Print() extends Identity(){ 
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
				//en fait, on n'a pas vraiment besoins de tester les tails si on teste les heads
																											
				if(`head*==`TwoId(Id())){
					if(`tail*==`TwoId(Id())){return `TwoC0(head2*,f,tail2*);}
					return `TwoC1(TwoC0(head2*,f,tail2*),tail*);
				}
				if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head2*,f,tail2*));}
				return `TwoC1(head*,TwoC0(head2*,f,tail2*),tail*);
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
} 
}

// strategie pour detecter les sources des 3-cellules
%strategy ApplyRules() extends Identity(){//ca marche un peu mais pas trop, probleme avec les fils dans les sources des 3 cellules
  	visit TwoPath {
  	  TwoC1(head*,args,f@TwoCell(_,_,_,_/*Function()*/),tail*) -> {TwoPath target=checkRules(`TwoC1(args,f));if(target!=null){
  		  if(`head*==`TwoId(Id())){
  		  return `TwoC1(target,tail*);}
  		  	return `TwoC1(head*,target,tail*);}
  	  		}// marche mais pas tout le temps
  	  	 } // chercher pour chaque 3 cellule au niveau des sources, retourner
			// la target
}

// fonction pour explorer les regles de reecriture
private static TwoPath checkRules2(TwoPath source){

for (Iterator<ThreePath> iterator = rewritingRules.iterator(); iterator.hasNext();) {
	ThreePath rewritingRule = (ThreePath) iterator.next();
	if(rewritingRule.getSource()==source){return rewritingRule.getTarget();}
// en fait il faut etre plus fin ici quand il y a des fils dans les sources des
// 3 cellules :
// il faut "etirer" les fils de la cible en checkant la compatibilitŽ des
// fils->%match
// puis il faut retourner la target du 3-cellules en gardant au dessus de
// celle-ci les 2-cellules non affectees
}
return null;
} 

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
				//System.out.println(`tail*);
				System.out.println(`TwoC0(TwoId(head*.source()),X,TwoC0(TwoId(tail*.source()))));
				//System.out.println(rewritingRule.getTarget());*/
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
myPath=(TwoPath) `RepeatId(Sequence(RepeatId(TopDown(ApplyRules())),RepeatId(TopDown(VerticalMerging())),RepeatId(TopDown(Gravity())))).visit(myPath);
System.out.println("RESULT");
System.out.println(myPath);
}
catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("strange term: " + myPath);
    }
}


}