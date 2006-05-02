package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Collection;

import tom.library.strategy.mutraveler.MuTraveler;

public class SimplifySystemExtended extends antipattern.SimplifySystem {

	%include{ term/Term.tom }
	%include{ mutraveler.tom }
	
	public SimplifySystemExtended(VisitableVisitor vis) {
		super(vis);      
    }
    
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
		Constraint result = null;
    	
    	try{
    		result = super.visit_Constraint(arg);
    		
    		//if it didn't entered the match of the super.visit_Constraint
    		//than maybe it will enter the one of the extendedVisit
    		if (isIdentity && arg == result){
    			return extendedVisit(arg);
    		}
    		
    		return result;
    		
    	}catch(VisitFailure vf){ //can only happen when the visit failed
    		return extendedVisit(arg);
    	}
    }
    
    private Constraint extendedVisit(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {
                
        // Decompose the constraint pattern
        Match(ApplCons(name,a1,constraint),Appl(name,a2)) -> {
          TermList args1 = `a1;
          TermList args2 = `a2;
          AConstraintList argCons = `constraint;
          AConstraintList l = `concAnd();
          while(!`args2.isEmptyconcTerm()) {
            l = `concAnd(Match(args1.getHeadconcTerm(),args2.getHeadconcTerm()),l*);
            args1 = args1.getTailconcTerm();
            args2 = args2.getTailconcTerm();
          }
          
          while(!argCons.isEmptyconcAnd()) {
	    	l = `concAnd(argCons.getHeadconcAnd(),l*);
	        argCons = argCons.getTailconcAnd();
          }
          
          return `And(l);
        }
        
        // SymbolClash with ApplCons
        Match(ApplCons(name1,args1,argCons),Appl(name2,args2))-> {
          if(`name1 != `name2) {
            return `False();
          }
        }
       
        // PropagateSuccess
        And(concAnd(X*,GreaterThan(Appl(var1,_),Appl(var2,_)),Y*)) ->{
        	return (Integer.parseInt(`var1) > Integer.parseInt(`var2) ? 
        			`And(concAnd(X*,Y*)):`False());
        }
        And(concAnd(X*,LessThan(Appl(var1,_),Appl(var2,_)),Y*)) ->{
        	return (Integer.parseInt(`var1) < Integer.parseInt(`var2) ? 
        			`And(concAnd(X*,Y*)):`False());
        }
      }
      
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  }
