import prologterms.types.*;
import java.util.*;
import java.io.*;
import tom.library.sl.*;

public class Context {

	%include {prologterms/PrologTerms.tom}
	%include {sl.tom}

	private int maxDepth = 1000;
	private RuleList ruleList ;
	private FactList FactsToSolve ;
	
	public Context(RuleList ruleList){
		this.ruleList = ruleList;
	}
	
	public Context(RuleList ruleList, int maxDepth){
		this.ruleList = ruleList;
		this.maxDepth = maxDepth;
	}
	
	public void ask(FactList Facts){
		long start = System.currentTimeMillis();
		this.FactsToSolve = Facts;
		try {
			process(this.ruleList,Facts,Unification.emptyUnification(),0);
		} catch (MaxDepthReachedException e) {
			System.out.println("Maximum depth ("+maxDepth+") reached : search aborted.");
		}
		System.out.println("Request done in "+(System.currentTimeMillis()-start)/1000+" s.");
	}

	public void process(RuleList rules, FactList FactsToTest, Unification unif, int depth) throws MaxDepthReachedException{
		if (depth > maxDepth){
			throw new MaxDepthReachedException();
		}
		%match(FactsToTest,rules) {
			fList(),_ -> {
				unif.display();
				waitForUser();
			}
			fList(Fact,tail1*),rList(_*,rule,_*) -> { 
				Rule renamedRule = renamesVariables(`rule,depth);
				%match(renamedRule){
					rule(goal,body) -> {
						Unification unifAttempt = Unification.unify(`Fact,`goal,unif);
						if (unifAttempt.succeed()) {
							FactList newBody = unifAttempt.substitutesVariables(`body);
							FactList newTail = unifAttempt.substitutesVariables(`tail1);
							//System. out.println(newBody);
							`process(this.ruleList,fList(newBody*,newTail*),unifAttempt,depth+1);
						}	
					}
				}
			}
		}
	}
	
	public static Rule renamesVariables(Rule rule, int depth) {
		String suffix = String.valueOf(depth);
		try {
			return `renameRule(suffix).visit(rule);
		} catch (VisitFailure vf) {
    		System.out.println("Visit failure");
    		return null;
    	}
	}
	
	%strategy renameTerm(String suffix) extends Identity(){
  		visit Term {
  			variable(name,_) -> {String newName = `name+suffix; return `variable(newName,1);}
  		}
    }
    
    %strategy renameFact(String suffix) extends Identity(){
  		visit Fact {
  			fact(name,arity,terms)  -> {
				try {
					return `fact(name,arity,(TermList) TopDown(renameTerm(suffix)).visit(terms));
				} catch (VisitFailure vf) {
    				System.out.println("Visit failure");
    			}
			}
  		}
    }
	
	%strategy renameRule(String suffix) extends Identity(){
	visit Rule {
  			rule(goal,body) -> {
				try {
					return `rule(renameFact(suffix).visit(goal),(FactList) TopDown(renameFact(suffix)).visit(body));
				} catch (VisitFailure vf) {
    				System.out.println("Visit failure");
    			}
			}
  		}
    }
	
	public static void waitForUser(){
		System.out.println("Press ENTER to continue");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			reader.readLine();
		} catch (IOException e) {}
	}

	public static String termToString(Term t){
		%match(t){
			constant(name) -> {return `name;}
			variable(name,_) -> {return `name;}
			function(name,termList) -> {return `name+"("+termListToString(`termList)+")";}
		}
		return null;
	}
	
	public static String termListToString(TermList tl){
		String result ="";
		%match(tl){
			tList(_*,t,_*) -> {result+=termToString(`t)+",";}
		}
		return result.substring(0,result.length()-1);
	}

}
