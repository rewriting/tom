import aterm.*;
import aterm.pure.*;
import jtom.runtime.*;
import adt.rho.rho.*;
import adt.rho.rho.types.*;

public class Rho {

  private Factory factory;
  private GenericTraversal traversal;
  
    //Signature de mon langage, utilise pour les tests.
  private RTerm f = null;
  private RTerm g = null;
  private RTerm X = null;

    //On suppose la conjonction de contraintes representee par des listes de contraintes (donc assoc). Dans les regles on se rend compte que en fait la commutativite du \land n'est pas utile.
  %include { rho.tom }

  public Rho(Factory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }
	
  public Factory getRhoFactory() {
    return factory;
  }
//UTILISER LES GENERIQUES TRAVERSALES OU/ET REPLACE POUR REDUCTION SOUS-TERME

//toutes les contraintes sont des listes. Ca simplifie pour le filtrage. Du coup, la regle rho genere l'application d'un singleton contrainte a un terme.

    public RTerm evaluate(RTerm pgme) {
       %match(RTerm pgme){
				 //RHO
				 app(abs(A,B),C) -> {return `appC(concConstraint(match(A,C)),B);}
				 //DELTA
				 app(struct(A,B),C) -> {return `struct(app(A,C),app(B,C));}
				 appC(L@concConstraint(match(X@var(x),A)), B) -> 
					 {return `appSt(L,B);}
				 appC((L1*,F@match(X@var(x),A),L2*),B) -> {
					 return `appC(concConstraint(L1*,L2*),appSt(concConstraint(F),B));
				 }
				 _ -> {return pgme;}

			 }
		}  
	
	public ListConstraint evaluateConst(ListConstraint C){
		%match(ListConstraint C){
			//Decompose_{Struct}
	    (X*,match(struct(A1,A2),struct(B1,B2)),Y*) -> 
				{return `concConstraint(X*,match(A1,B1),match(A2,B2),Y*);}
			//Decompose_{App}
	    l:(X*,match(T1@app(A1,A2),T2@app(B1,B2)),Y*) -> {
				ListConstraint head_is_constant = headConst(`concConstraint(matchH(T1,T2)));
				%match(ListConstraint head_is_constant){
					(X1*,matchH(A,B),Y1*) -> {break l;}
					_ -> { return `concConstraint(X*,head_is_constant*,Y*);}
				}
			}
// 			//Good Labels
// 			(X*,match(A@var(x),B),Y*,match(C1@var(y),D1),Z*) ->{
// 				return `concConstraint(X*,g(concConstraint(matchg(A,B),matchg(C1,D1))),Y*,Z*);
// 			}
// 			(X*,match(A@var(x),B),Y*,g(c1),Z*) -> {
			//				return `concConstraint(X*,g(c1));

//concConstraint(matchg(A,B),c1*)),Y*,Z*);
// 			}
// 			(X*,g(c2),Y*,g(c1),Z*) -> {
// 				return `concConstraint(X*,g(concConstraint(c2*,c1*)),Y*,Z*);
// 			}
// 			//NGood Labels
// 	    l:(X*,match(T1@app(A1,A2),T2@app(B1,B2)),Y*) -> {
// 					ListConstraint head_is_constant_diff = headConstDiff(`concConstraint(matchH(T1,T2)));
// 					%match(ListConstraint head_is_constant_diff){
// 						(X1*,matchH(A,B),Y1*) -> 
// 							{break l;}
// 						_ -> { return `concConstraint(X*,matchng(T1,T2)Y*);}
// 					}
// 			}
			//NGood pas propage
	    _ -> {return C;}
			
		}
	}
	//pour pouvoir tester si les deux termes ont le meme symbole de tete qui est une constante
	public ListConstraint headConst(ListConstraint C) {
	    %match(ListConstraint C){
		(X*,matchH(app(T1@app(A1,A2),A3),app(T2@app(B1,B2),B3)),Y*) -> 
		    {return headConst(`concConstraint(X*,matchH(T1,T2),match(A3,B3),Y*));}
		
		(X*,matchH(app(F@const(f),A),app(F,B)),Y*) -> 
		    {return `concConstraint(X*,match(A,B),Y*);}
		 
		_ -> {return C;}
	    } 

	}
	//pour pouvoir tester si les deux termes ont  symbole de tete qui est une constante differente
	public ListConstraint headConstDiff(ListConstraint C) {
	    %match(ListConstraint C){
		(X*,matchH(app(T1@app(A1,A2),A3),app(T2@app(B1,B2),B3)),Y*) -> 
		    {return headConstDiff(`concConstraint(X*,matchH(T1,T2),match(A3,B3),Y*));}
		
		(X*,matchH(app(F@const(f1),A),app(G@const(f2),B)),Y*) -> 

		    {
					if (f2.equals(f1))
								return `concConstraint(X*,match(A,B),Y*);
				}
		 
		_ -> {return C;}
	    } 

	}
	
	
    public void run(){
	f = `const("f");
	g = `const("g");
	X = `var("X");
	RTerm pgme = `app(abs(X,X),const("a"));
	RTerm pgme2 = `app(struct(f,f),const("a"));
	ListConstraint cont = `concConstraint(match(app(X,X),app(f,f)));
	while (true){
	    cont = evaluateConst(cont);
	    System.out.println(cont);	
	}
    }
    public final static void main(String[] args) {
	Rho rhoEngine = new Rho(new Factory(new PureFactory()));
	rhoEngine.run();


  }
}
