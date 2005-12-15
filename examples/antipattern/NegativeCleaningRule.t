package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Collection;

import tom.library.strategy.mutraveler.MuTraveler;

public class NegativeCleaningRule {
	
	protected TermFactory factory;
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }
	
	protected final TermFactory getTermFactory() {
		return factory;
	}
	
	protected boolean isIdentity;
	
	public NegativeCleaningRule() {		
		this.factory = TermFactory.getInstance(SingletonFactory.getInstance());		
	}
	
	public Constraint applyRules(Constraint c){
		return c;
	}
	
	%rule{
		Neg(Match(Variable(name),s)) -> False()	
	}  
}
