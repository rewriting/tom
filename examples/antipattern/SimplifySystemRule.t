package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import tom.library.strategy.mutraveler.MuTraveler;

public class SimplifySystemRule{
	
	protected TermFactory factory;
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }
	
	protected final TermFactory getTermFactory() {
		return factory;
	}
	
	public SimplifySystemRule(){
		this.factory = TermFactory.getInstance(SingletonFactory.getInstance());   
	}
	
	%op Constraint AreSymbolsEqual(s1:Term, s2:Term){
		is_fsym(t) { false }
		make(t1,t2) { areSymbolsEqual(t1, t2) }
	}	
	
	%op Constraint DecomposeList(l1:TermList, l2:TermList){
		is_fsym(t) { false }
		make(t1,t2) { decomposeList(t1, t2) }
	}
	
	%op Constraint ContainsVariable(v:Term, l:ConstraintList){
		is_fsym(t) { false }
		make(t1,t2) { containsVariable(t1, t2) }
	}
	
	%op Constraint ApplyReplaceStrategy(var:Term,value:Term,l:ConstraintList){
		is_fsym(t) { false }
		make(t1,t2,t3) { applyReplaceStrategy(t1,t2,t3) }
	}
	
	private Constraint decomposeList(TermList l1, TermList l2){		
		
		// System.out.println("In decompose");
		ConstraintList l = `emptyConstraintList();
		
		while(!l1.isEmpty()) {
			l = `manyConstraintList(Match(l1.getHead(),l2.getHead()),l);
			l1 = l1.getTail();
			l2 = l2.getTail();					
		}
		return `And(l.reverseConstraintList());
	}
	
	private Constraint areSymbolsEqual(Term t1, Term t2){
		
		// System.out.println("In areSymbolsEq");
		
		if (t1.getName().equals(t2.getName())){
			return `True();
		}
		
		return `False();
	}
	
	private Constraint containsVariable(Term v, ConstraintList l){
		
		// not the most efficient method, because
		// it doesn't stop when an occurence is found
		ContainsTerm ct = new ContainsTerm(v,`Identity());
		try{
			MuTraveler.init(`InnermostId(ct)).visit(l);
		}catch(VisitFailure e){
			System.out.println("Exception:" + e.getMessage());
			System.exit(0);
		}
		
		System.out.println("FOUND=" + ct.getFound());
		
		return ct.getFound() == true ? `True():`False();
		// return l.match(v) == null ? `False():`True();
	}
	
	private Constraint applyReplaceStrategy(Term var, Term value, ConstraintList l){
		
		VisitableVisitor rule,ruleStrategy;            
		rule = new ReplaceSystem(var,value, `Identity());
		ruleStrategy = `InnermostId(rule);
		
		Constraint res = null;
		
		try{
			res = (Constraint) MuTraveler.init(ruleStrategy).visit(`And(l));
		}catch(VisitFailure e){
			System.out.println("Exception:" + e.getMessage());
			System.exit(0);
		}
		
		return res;
		
	}
	
	public Constraint applyRules(Constraint c){
		
		Constraint ret = null;
		
		%match(Constraint c){
			Match(a,b) -> {
				ret = `Match(a,b);
				System.out.println("RET=" + ret);
			}
		}
		//System.out.println("GIGI=" + `Match(Appl("a",concTerm()),Appl("a",concTerm())));
		return ret;
	}	
	
	%rule{
		
		// NegDef
		Match(Anti(Appl(name1, args1)),Appl(name2, args2)) -> Neg(Match(Appl(name1, args1),Appl(name2, args2)))
		
		// Decompose
		Match(Appl(name,a1),Appl(name,a2)) -> DecomposeList(a1,a2)
		
		// SymbolClash
		Match(a1@Appl(name1,args1),a2@Appl(name2,args2))   -> False() if False() == AreSymbolsEqual(a1,a2)	
		
		// Delete
		Match(Appl(name,concTerm()),Appl(name,concTerm())) -> True()		
	}
	
	%rule{
		// Replace
		And(concConstraint(X*,match@Match(var@Variable(name),s),Y*)) -> And(concConstraint(match,ApplyReplaceStrategy(var,s,concConstraint(X*,Y*))))
																			if True() == ContainsVariable(var,concConstraint(X*,Y*))
		// PropagateClash
		And(concConstraint(_*,False(),_*)) -> False()
		
		
		// PropagateSuccess
		And(concConstraint()) -> True()
		
		And(concConstraint(x)) -> x
		
		And(concConstraint(X*,True(),Y*)) -> And(concConstraint(X*,Y*))
		
	}
	
	%rule{
		// BooleanSimplification
		Neg(Neg(x)) -> x
		Neg(True()) -> False()
		Neg(False()) -> True()
	}
}

class ContainsTerm extends antipattern.term.TermVisitableFwd {
	
	private TermFactory factory;	
	
	private final TermFactory getTermFactory() {
		return factory;
	} 
	
	private boolean found = false;
	private ATerm objToSearchFor = null;
	
	public ContainsTerm(ATerm obj, VisitableVisitor visitor) {		
		super(visitor);
		this.objToSearchFor = obj;
		this.found = false;
		this.factory = TermFactory.getInstance(SingletonFactory.getInstance());      
	}
	
	public Term visit_Term(Term arg) throws VisitFailure { 
		if(arg == objToSearchFor) {
			found = true;
			System.out.println("!!FOUND!!");
		} 
		return arg;
	}
	
	public boolean getFound(){
		return found;
	}
}