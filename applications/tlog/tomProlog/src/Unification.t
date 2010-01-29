import prologterms.types.*;
import tom.library.sl.*;

public class Unification {

	%include {prologterms/PrologTerms.tom}
	%include {sl.tom}

	private boolean unificationSucceed ;
	private SubstitutionList substitutions = `sList();
	
	private Unification(boolean success) {
		unificationSucceed = success ;
	}
	
	private Unification(SubstitutionList substitutions) {
		this.substitutions = substitutions ;
		unificationSucceed = true ;
	}
	
	public static Unification emptyUnification(){
		return new Unification(true);
	}
	
	public boolean succeed() {
		return unificationSucceed; 
	}
	
	public SubstitutionList getSubstitutions(){
		return substitutions;
	}
	
	private Unification unificationAfterAddingSubstitution(Substitution s){
		%match(s,substitutions) {
			subs(variable,constant1),sList(_*,subs(variable,constant2),_*) -> {
				if (`constant1==`constant2) {return new Unification(this.substitutions);} else {return new Unification(false);}
			}
			ss,sList(S*) -> {
				SubstitutionList newSubs = `sList(ss,S*);
				return new Unification(newSubs);
			}
		}
		return null;
	}
	
	private Unification unificationAfterAddingSubstitutionList(SubstitutionList sl){
		Unification result = new Unification(this.substitutions);
		%match(sl) {
			sList(_*,sub,_*) -> {result=result.unificationAfterAddingSubstitution(`sub);}
			}
		return result;
	}
	
	public static Unification unify(Fact factToUnify, Fact goal, Unification unif){
	
		%match(factToUnify,goal) {
			fact(n1,a1,_),fact(n2,a2,_) -> {
				if (`n1!=`n2 || `a1!=`a2) {return new Unification(false);}
			}
			
			fact(n1,a1,tl1),fact(n2,a2,tl2) -> {
				return unify(`tl1,`tl2,unif);
			}
		}
		return null;
	}
	
	private static Unification unify(TermList termToUnify, TermList goal, Unification unif){
		
		%match(termToUnify,goal) {
			tList(),tList() -> {return unif;}
			tList(t1,tail1*),tList(t2,tail2*) -> {
				Unification res = unify(`t1,`t2);
				if (res.succeed()) {
					return unify(`tail1*,`tail2*,unif).unificationAfterAddingSubstitutionList(res.getSubstitutions());
				} else {
					return new Unification(false);
				}
			}
		}
		return null;
	}
	
	private static Unification unify(Term termToUnify, Term goal){
		
		%match(termToUnify,goal) {
			
			constant(constant1),constant(constant2) -> {
				if (`constant1==`constant2) {return new Unification(true);} else {return new Unification(false);}
			}
			
			variable(variable1,t),constant(constant2) -> {
				return new Unification(`sList(subs(variable(variable1,t),constant(constant2))));
			}
			
			constant(constant1),variable(variable2,t) -> {
				return new Unification(`sList(subs(variable(variable2,t),constant(constant1))));
			}
			
			variable(variable1,t1),variable(variable2,t2) -> {
				return new Unification(`sList(subs(variable(variable2,t2),variable(variable1,t1))));
			}
			
			variable(variable1,t1),function(name,termList) -> {
				return new Unification(`sList(subs(variable(variable1,t1),function(name,termList))));
			}
			
			function(name,termList),variable(variable2,t2) -> {
				return new Unification(`sList(subs(variable(variable2,t2),function(name,termList))));
			}
			
			function(name1,termList1),function(name2,termList2) -> {
				if (`name1 != `name2) {return new Unification(false);} else {
					return unify(`termList1,`termList2,new Unification(true));
				}
			}
			
		}
		
		return new Unification(false);
	}
	
	public FactList substitutesVariables(FactList factList){
		FactList result = null;
		try {
			result = (FactList) `TopDown(substitutesVariablesInFact(substitutions)).visit(factList);
		} catch (VisitFailure vf) {System.out.println("Visit failure");}
		return result;
	}
	
	%strategy substitutesVariablesInFact(SubstitutionList subs) extends Identity(){
  		visit Fact {
  			fact(n,a,tl) -> {
  				try {
					return `fact(n,a,(TermList) TopDown(substitutesVariablesInTerm(subs)).visit(tl));
				} catch (VisitFailure vf) {System.out.println("Visit failure");}
			}
			
  		}
        }
        
        %strategy substitutesVariablesInTerm(SubstitutionList subs) extends Identity(){
  		visit Term {
  			constant(name) -> {return `constant(name);}
			variable(name1,t1) -> {
				%match(subs) {
					sList(_*,subs(variable(name2,t2),atomToSubstitute),_*) -> {
						if (`name1==`name2) {return `atomToSubstitute;}
					}
				}
				return `variable(name1,t1);
			}
			function(name,terms) -> {
				try {
					return `function(name,(TermList) TopDown(substitutesVariablesInTerm(subs)).visit(terms));
				} catch (VisitFailure vf) {System.out.println("Visit failure");}
			}
  		}
        }
	
	public void display(){
		String result = "";
		if (!unificationSucceed) {
			result = "Unification failed";
		} else {
			%match(substitutions) {
				sList(_*,subs(variable(variableName,0),term),_*) -> {
					try {
						Term newTerm = `TopDown(substitutesVariablesInTerm(substitutions)).visit(`term);
						result += `variableName+" = "+Context.termToString(newTerm)+" , ";
					} catch (VisitFailure vf) {System.out.println("Visit failure");}
				}
			}
			if (result.length()==0) {
				result = "TRUE";
			} else {
				result = result.substring(0,result.length()-3);
			}
		}
		System.out.println(result);
	}



}
