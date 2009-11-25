import prologterms.types.*;

public class Unification {

	%include {prologterms/PrologTerms.tom}

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
	
	public SubstitutionList getSubstituions(){
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
	
	public static Unification unify(Fact factToUnify, Fact goal, Unification unif){
		//System.out.println("Trying "+factToUnify+" against "+goal);
		%match(factToUnify,goal) {
			fact(n1,a1,_),fact(n2,a2,_) -> {
				if (`n1!=`n2 || `a1!=`a2) {return new Unification(false);}
			}
			
			fact(n1,a1,tList()),fact(n2,a2,tList()) -> {
				return unif;
			}
			
			fact(n1,a1,tList(constant(constant1),tail1*)),fact(n2,a2,tList(constant(constant2),tail2*)) -> {
				if (`constant1==`constant2) {return unify(`fact(n1,a1,tail1*),`fact(n2,a2,tail2*),unif);} else
				{return new Unification(false);}
			}
			
			fact(n1,a1,tList(variable(variable1),tail1*)),fact(n2,a2,tList(constant(constant2),tail2*)) -> {
				Unification res = unify(`fact(n1,a1,tail1*),`fact(n2,a2,tail2*),unif);
				if (res.succeed()) {
					return res.unificationAfterAddingSubstitution(`subs(variable(variable1),constant(constant2)));
				} else {
					return new Unification(false); 
				}
			}
			
			fact(n1,a1,tList(constant(constant1),tail1*)),fact(n2,a2,tList(variable(variable2),tail2*)) -> {
				Unification res = unify(`fact(n1,a1,tail1*),`fact(n2,a2,tail2*),unif);
				if (res.succeed()) {
					return res.unificationAfterAddingSubstitution(`subs(variable(variable2),constant(constant1)));
				} else {
					return new Unification(false); 
				}
			}
			
			fact(n1,a1,tList(variable(variable1),tail1*)),fact(n2,a2,tList(variable(variable2),tail2*)) -> {
				Unification res = unify(`fact(n1,a1,tail1*),`fact(n2,a2,tail2*),unif);
				if (res.succeed()) {
					return res.unificationAfterAddingSubstitution(`subs(variable(variable2),variable(variable1)));
				} else {
					return new Unification(false); 
				}
			}
		}
		return null;
	}
	
	public FactList substitutesVariables(FactList factList){
		%match(factList,substitutions){
			fList(X1*,fact(n,a,tList(X*,variable(name),Y*)),Y1*),sList(X2*,subs(variable(name),atomToSubstitute),Y2*) -> {
				substitutions = `sList(X2*,Y2*);
				return `substitutesVariables(fList(X1*,fact(n,a,tList(X*,atomToSubstitute,Y*)),Y1*));
			}
		}
		return factList;
	}
	
	public void display(FactList factsToSolve){
		String result = "";
		if (!unificationSucceed) {
			result = "Unification failed";
		} else {
			%match(substitutions,factsToSolve) {
				sList(_*,subs(variable(variable),constant(constant)),_*),fList(_*,fact(_,_,tList(_*,variable(variable),_*)),_*) -> {
					result += `variable+" = "+`constant+" , ";
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
