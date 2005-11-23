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

	%include{ term/term.tom }
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
        Match(ApplCons(name,args1,argCons),Appl(name,args2)) -> {
        	
          ConstraintList l = `emptyConstraintList();
          while(!args2.isEmpty()) {
            l = `manyConstraintList(Match(args1.getHead(),args2.getHead()),l);
            args1 = args1.getTail();
            args2 = args2.getTail();
          }
          
          while(!argCons.isEmpty()) {
	    	l = `manyConstraintList(argCons.getHead(),l);
	        argCons = argCons.getTail();
          }
          
          return `And(l);
        }
        
        // SymbolClash with ApplCons
        Match(ApplCons(name1,args1,argCons),Appl(name2,args2))-> {
          if(name1 != name2) {
            return `False();
          }
        }
       
        // PropagateSuccess
        And(concConstraint(X*,GreaterThan(Appl(var1,_),Appl(var2,_)),Y*)) ->{
        	return (Integer.parseInt(var1) > Integer.parseInt(var2) ? 
        			`And(concConstraint(X*,Y*)):`False());
        }
        And(concConstraint(X*,LessThan(Appl(var1,_),Appl(var2,_)),Y*)) ->{
        	return (Integer.parseInt(var1) < Integer.parseInt(var2) ? 
        			`And(concConstraint(X*,Y*)):`False());
        }
      }
      
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  }