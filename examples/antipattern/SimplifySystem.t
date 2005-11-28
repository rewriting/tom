package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

public class SimplifySystem extends antipattern.term.TermVisitableFwd {
	protected TermFactory factory;
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }

	protected final TermFactory getTermFactory() {
		return factory;
	}
	protected boolean isIdentity;
	
	public SimplifySystem(VisitableVisitor vis) {
      super(vis);
      this.factory = TermFactory.getInstance(SingletonFactory.getInstance());
      this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
    		  true : false ); 
    }
    
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {
        // AntiMatch
        Match(Anti(p),s) -> {
          return `Neg(Match(p,s));
        }
        
        // Delete
        Match(Appl(name,concTerm()),Appl(name,concTerm())) -> {
          return `True();
        }

        // Decompose
        Match(Appl(name,args1),Appl(name,args2)) -> {
          ConstraintList l = `emptyConstraintList();
          while(!args1.isEmpty()) {
            l = `manyConstraintList(Match(args1.getHead(),args2.getHead()),l);
            args1 = args1.getTail();
            args2 = args2.getTail();
          }
          return `And(l);
        }
        
        // SymbolClash
        Match(Appl(name1,args1),Appl(name2,args2)) -> {
          if(name1 != name2) {
            return `False();
          }
        }
       
        // PropagateClash
        And(concConstraint(_*,False(),_*)) -> {
          return `False();
        }
        
        // PropagateSuccess
        And(concConstraint()) -> {
          return `True();
        }
        And(concConstraint(x)) -> {
          return `x;
        }
        And(concConstraint(X*,True(),Y*)) -> {
          return `And(concConstraint(X*,Y*));
        }

        // BooleanSimplification
        Neg(Neg(x)) -> { return `x; }
        Neg(True()) -> { return `False(); }
        Neg(False()) -> { return `True(); }
        And(concConstraint(X*,c,Y*,c,Z*)) -> {
          return `And(concConstraint(X*,c,Y*,Z*));
        }

      }
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  }
