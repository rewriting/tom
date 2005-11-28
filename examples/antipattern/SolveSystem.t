package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Collection;

import tom.library.strategy.mutraveler.MuTraveler;

public class SolveSystem extends antipattern.term.TermVisitableFwd {
    
	protected TermFactory factory;
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }

	protected final TermFactory getTermFactory() {
		return factory;
	}
	
	protected Collection solution; 
	protected boolean isIdentity;
	
    public SolveSystem(Collection c,VisitableVisitor vis) {
      super(vis);
      this.solution = c;
      this.factory = TermFactory.getInstance(SingletonFactory.getInstance());
      this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
  		  true : false );      
    }
   
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {
        match@Match(var@Variable(name),s) -> {
            solution.add(match); 
            Constraint res = `True();
            //System.out.println("[solve1] -> [" + match + "," + res + "]");
            return res;
        }
        Neg(match@Match(var@Variable(name),s)) -> {
            solution.add(match); 
            Constraint res = `False();
            //System.out.println("[solve1] -> [" + match + "," + res + "]");
            return res;
        }
        And(concConstraint(X*,match@Match(var@Variable(name),s),Y*)) -> {
            solution.add(match);
            VisitableVisitor rule,ruleStrategy;            
            if (isIdentity){
            	rule = new ReplaceSystem(var,s, `Identity());
            	ruleStrategy = `InnermostId(rule);
            }else{
            	rule = new ReplaceSystem(var,s, `Fail());
            	ruleStrategy = `Innermost(rule);
            }            
            Constraint res = (Constraint) MuTraveler.init(ruleStrategy).visit(`And(concConstraint(X*,Y*)));
            //System.out.println("[solve3] -> [" + match + "," + res + "]");
            return res;
        }
        Neg(And(concConstraint(X*,match@Match(var@Variable(name),s),Y*))) -> {
            solution.add(match); 
            VisitableVisitor rule,ruleStrategy;            
            if (isIdentity){
            	rule = new ReplaceSystem(var,s, `Identity());
            	ruleStrategy = `InnermostId(rule);
            }else{
            	rule = new ReplaceSystem(var,s, `Fail());
            	ruleStrategy = `Innermost(rule);
            }
            Constraint res = (Constraint) MuTraveler.init(ruleStrategy).visit(`Neg(And(concConstraint(X*,Y*))));
            //System.out.println("[solve4] -> [" + match + "," + res + "]");
            return res;
        }
      }
      
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  
  }