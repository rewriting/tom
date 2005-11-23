package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Collection;

import tom.library.strategy.mutraveler.MuTraveler;

public class SolveSystemExtended extends antipattern.SolveSystem {    
	
	%include{ term/term.tom }
	%include{ mutraveler.tom }
	
    public SolveSystemExtended(Collection c,VisitableVisitor vis) {
      super(c,vis);            
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
    	      	        
        GreaterThan(Appl(var1,_),Appl(var2,_)) ->{
        	if (Integer.parseInt(var1) > Integer.parseInt(var2)){
        		return `True();
        	}
        	
        	return `False();
        }
        Neg(GreaterThan(Appl(var1,_),Appl(var2,_))) ->{
        	if (Integer.parseInt(var1) > Integer.parseInt(var2)){
        		return `False();
        	}
        	
        	return `True();
        }
        LessThan(Appl(var1,_),Appl(var2,_)) ->{
        	if (Integer.parseInt(var1) < Integer.parseInt(var2)){
        		return `True();
        	}
        	
        	return `False();
        }        
        Neg(LessThan(Appl(var1,_),Appl(var2,_))) ->{
        	if (Integer.parseInt(var1) < Integer.parseInt(var2)){
        		return `False();
        	}
        	
        	return `True();
        }
        
      }
      
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }  
    
  }