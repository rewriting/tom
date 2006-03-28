package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Collection;

import tom.library.strategy.mutraveler.MuTraveler;

public class ReverseEngAp extends antipattern.term.TermVisitableFwd {
    
	%include{ term/Term.tom }
	%include{ mutraveler.tom }

	protected boolean isIdentity;
	
    public ReverseEngAp(VisitableVisitor vis) {
      super(vis);      
      this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
  		  true : false );      
    }
   
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {        
        Neg(Match(a,b)) -> {             
            return `Match(Anti(a),b);
        }        
        And(concConstraint(X*,Match(q1,t1),Z*,Match(q2,t2),Y*)) -> {
        	//return `Match(conc);
        	System.out.println("ici");
        	return `And(concConstraint(X*,Match(Appl("conc",concTerm(q1,q2)),Appl("conc",concTerm(t1,t2))),Z*,Y*));
        }
        And(concConstraint(Match(a,b))) ->{
        	return `Match(a,b);
        }
      }
      
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  
  }
