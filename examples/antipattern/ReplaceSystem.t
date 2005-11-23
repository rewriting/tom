package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

public class ReplaceSystem extends antipattern.term.TermVisitableFwd {
	
	private TermFactory factory;
	
	%include{ term/term.tom }
	%include{ mutraveler.tom }

	private final TermFactory getTermFactory() {
		return factory;
	} 
      
    private Term variable;
    private Term value;
    private boolean isIdentity;

    public ReplaceSystem(Term variable, Term value, VisitableVisitor visitor) {
      super(visitor);
      this.variable = variable;
      this.value = value;
      this.factory = TermFactory.getInstance(SingletonFactory.getInstance());      
      this.isIdentity = (visitor.getClass().equals(`Identity().getClass()) ? 
    		  true : false );       
    }
   
    public Term visit_Term(Term arg) throws VisitFailure { 
      if(arg==variable) {
        return value;
      } 
      return (isIdentity ? arg : (Term)`Fail().visit(arg));
    }
  }  
