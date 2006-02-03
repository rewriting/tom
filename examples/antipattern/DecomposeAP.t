package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import tom.library.strategy.mutraveler.MuTraveler;

public class DecomposeAP extends antipattern.term.TermVisitableFwd {
	protected TermFactory factory;
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }
	
	protected final TermFactory getTermFactory() {
		return factory;
	}
	protected boolean isIdentity;
	
	public static int varCounter = 0;
	
	public DecomposeAP(VisitableVisitor vis) {
		super(vis);
		this.factory = TermFactory.getInstance(SingletonFactory.getInstance());
		this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
				true : false ); 
	}

	public Term visit_Term(Term arg) throws VisitFailure {		
		
		%match(Term arg){			
			//first rule
			Anti(p) ->{
				return `TermDiff(Variable("x_" + varCounter++),p);
			}
			//second one
			Appl(name, concTerm(X*,TermDiff(firstTerm,secondTerm),Y*)) -> {
				return `TermDiff(Appl(name, concTerm(X*,firstTerm,Y*)),
							Appl(name, concTerm(X*,secondTerm,Y*)));
			}			
		}
		return  (isIdentity ? arg : (Term)`Fail().visit(arg));
	}		
}
