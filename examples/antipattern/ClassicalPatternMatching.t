package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

public class ClassicalPatternMatching extends antipattern.term.TermBasicStrategy {
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }

	protected boolean isIdentity;
	
	public ClassicalPatternMatching(VisitableVisitor vis) {
      super(vis);
      this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
    		  true : false ); 
    }
    
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {
                
        // Delete
        Match(Appl(name,concTerm()),Appl(name,concTerm())) -> {
          return `True();
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
          return `And(l);
        }
        
        // SymbolClash
        Match(Appl(name1,args1),Appl(name2,args2)) -> {
          if(`name1 != `name2) {
            return `False();
          }
        }
        
        // MergingClash
        And(concAnd(_*,Match(Variable(x),t1@Appl(_,_)),_*,Match(Variable(x),t2@Appl(_,_)),_*)) -> {
          if(`t1 != `t2) {
            return `False();
          }
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
        
        //new introduced variables
        Match(Variable(name),_) ->{
        	if (`name.startsWith("x_")){
        		return `True();
        	}
        }

      }
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  }
