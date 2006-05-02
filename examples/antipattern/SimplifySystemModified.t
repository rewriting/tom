package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import tom.library.strategy.mutraveler.MuTraveler;

public class SimplifySystemModified extends antipattern.term.TermBasicStrategy {
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }
	
	protected boolean isIdentity;
	
	public SimplifySystemModified(VisitableVisitor vis) {
		super(vis);
		this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
				true : false ); 
	}
	
	public Constraint visit_Constraint(Constraint arg) throws VisitFailure {

		%match(Constraint arg) {
			
			// NegDef
			Match(Anti(p),s) -> {
				return `Neg(Match(p,s));
			}
			
			// Decompose
			Match(Appl(name,a1),Appl(name,a2)) -> {
				AConstraintList l = `concAnd();
				TermList args1 = `a1;
				TermList args2 = `a2;
				while(!args1.isEmptyconcTerm()) {
					l = `concAnd(Match(args1.getHeadconcTerm(),args2.getHeadconcTerm()),l*);
					args1 = args1.getTailconcTerm();
					args2 = args2.getTailconcTerm();					
				}
				return `And(l/*.reverseConstraintList()*/);
			}
			
			// SymbolClash
			Match(Appl(name1,args1),Appl(name2,args2)) -> {
				if(`name1 != `name2) {
					return `False();
				}
			}
			
			// Replace
			input@And(concAnd(X*,match@Match(var@Variable(name),s),Y*)) -> {	            
	            VisitableVisitor rule,ruleStrategy;            
	            if (isIdentity){
	            	rule = new ReplaceSystem(`var,`s, `Identity());
	            	ruleStrategy = `InnermostId(rule);
	            }else{
	            	rule = new ReplaceSystem(`var,`s, `Fail());
	            	ruleStrategy = `Innermost(rule);
	            }            
	            Constraint res = (Constraint) MuTraveler.init(ruleStrategy).visit(`And(concAnd(X*,Y*)));
	            if (res != `And(concAnd(X*,Y*))){
	            	return `And(concAnd(match,res));
	            }
	        }
			
			// Delete
			Match(Appl(name,concTerm()),Appl(name,concTerm())) -> {
				return `True();
			}
			
			// PropagateClash
			And(concAnd(_*,False(),_*)) -> {
				return `False();
			}
			
			// PropagateSuccess
			And(concAnd()) -> {
				return `True();
			}
			And(concAnd(x)) -> {
				return `x;
			}
			
			And(concAnd(X*,True(),Y*)) -> {
				return `And(concAnd(X*,Y*));
			}
			
			// BooleanSimplification
			Neg(Neg(x)) -> { return `x; }
			Neg(True()) -> { return `False(); }
			Neg(False()) -> { return `True(); }
			
		}
		return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
	}
}
