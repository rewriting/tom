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
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }
	
	protected boolean isIdentity;
	
	public Constraint applyRules(Constraint c){
		return c;
	}
	
	%rule{
		Neg(Match(Variable(name),s)) -> False()	
	}  
}
