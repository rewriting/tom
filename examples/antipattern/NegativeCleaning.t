package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Collection;

import tom.library.strategy.mutraveler.MuTraveler;

public class NegativeCleaning extends antipattern.term.TermVisitableFwd {
    
	protected TermFactory factory;
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }

	protected final TermFactory getTermFactory() {
		return factory;
	}
	
	protected boolean isIdentity;
	
    public NegativeCleaning(VisitableVisitor vis) {
      super(vis);      
      this.factory = TermFactory.getInstance(SingletonFactory.getInstance());
      this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
  		  true : false );      
    }
   
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {        
        Neg(Match(Variable(name),s)) -> {             
            return `False();
        }
        //if the conjunction contains negative 
        //then it shoudn't be replaced by false
        Neg(And(concConstraint(X*,Neg(a),Y*))) -> {
        	return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
        }
        Neg(And(concConstraint(X*))) -> {
        	return `False();
        }
      }
      
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  
  }
