package jtom.verifier;

import jtom.*;

import aterm.*;
import java.util.*;
import jtom.tools.*;
import jtom.runtime.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;
import jtom.TomMessage;

/**
 * The TomVerifier plugin.
 */
public class TomVerifier extends TomGenericPlugin
{
    %include{ ../adt/TomSignature.tom }
    %include{ ../adt/Options.tom }

    protected Verifier verif;

    public TomVerifier()
    {
	verif = new Verifier();
    }

    public void run()
    {
	if(isActivated())
	    {
		try
		    {
			long startChrono = System.currentTimeMillis();
			boolean verbose = getServer().getOptionBooleanValue("verbose");

			TomTerm extractTerm = `emptyTerm();
			// here the extraction stuff
			
			Collection matchSet = collectMatch(term);
			// System.out.println("Extracted : " + matchSet);
		
			Collection purified = purify(matchSet);
			// System.out.println("Purified : " + purified);
			
			Collection derivations = getDerivations(purified);

			if(verbose)
			    System.out.println("TOM verification phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");
	    
			environment().printAlertMessage("TomVerifier");
		
			if(!environment().isEclipseMode())
			    {
				// remove all warning (in command line only)
				environment().clearWarnings();
			    }
		    }
		catch (Exception e) 
		    {
			environment().messageError("Exception occured in TomVerifierExtract: " + e.getMessage(),
						   environment().getInputFile().getName(), 
						   TomMessage.DEFAULT_ERROR_LINE_NUMBER);
			e.printStackTrace();
		    }
	    }
	else
	    {
		boolean verbose = getServer().getOptionBooleanValue("verbose");

		if(verbose)
		    System.out.println("The verifier is not activated and thus WILL NOT RUN.");
	    }
    }

     public TomOptionList declaredOptions()
     {
 	return `concTomOption(OptionBoolean("verify", "", "Verify correctness of match compilation", False()) // activation flag
			      );
     }

  private boolean isActivated() {
    return getServer().getOptionBooleanValue("verify");
  }

    private Collect2 collect_match = new Collect2() {
	    public boolean apply(ATerm subject, Object astore) {
		Collection store = (Collection)astore;
		if (subject instanceof Instruction) {
		    %match(Instruction subject) {
			CompiledMatch(automata, (_*,TomTermToOption(PatternList(_)),_*))  -> {
			    store.add(subject);
			}

			// default rule
			_ -> {
			    return true;
			}
		    }//end match
		} else { 
		    return true;
		}
	    }//end apply
	}; //end new

    public Collection collectMatch(TomTerm subject) {
	Collection result = new HashSet();
	traversal().genericCollect(subject,collect_match,result);
	//collect_matching.apply(subject, result);
	return result;
    }

    public Collection purify(Collection subject) {
	Collection purified = new HashSet();
	Iterator it = subject.iterator();
	while (it.hasNext()) {
	    Instruction cm = (Instruction)it.next();
	    %match(Instruction cm) {
		CompiledMatch(automata, options)  -> {
		    // simplify the IL automata
		    purified.add(`CompiledMatch(simplify_il(automata),options));
		}
	    }
	}
	return purified;
    }

    Replace1 replace_simplify_il = new Replace1() {
	    public ATerm apply(ATerm subject) {
		if (subject instanceof Expression) {
		    %match(Expression subject) {
			Or(cond,FalseTL()) -> {
			    return traversal().genericTraversal(`cond,this);
			}
		    }
		} // end instanceof Expression
		else if (subject instanceof Instruction) {
		    %match(Instruction subject) {
			IfThenElse(TrueTL(),success,Nop()) -> {
			    return traversal().genericTraversal(`success,this);
			}
		    }
		} // end instanceof Instruction
		/*
		 * Default case : Traversal
		 */
		return traversal().genericTraversal(subject,this);
	    }//end apply
	};//end new Replace1 simplify_il
	
    private Instruction simplify_il(Instruction subject) {
	return (Instruction) replace_simplify_il.apply(subject);
    }

    public Collection getDerivations(Collection subject) {
	Collection derivations = new HashSet();
	Iterator it = subject.iterator();
	while (it.hasNext()) {
	    Instruction cm = (Instruction)it.next();
	    %match(Instruction cm) {
		CompiledMatch(automata, options)  -> {
		    derivations.add(`CompiledMatch(apply_replace_getDerivations(automata),options));
		}
	    }
	}
	return derivations;
    }

    private Instruction apply_replace_getDerivations(Instruction subject) {
	return (Instruction) replace_getDerivations.apply(subject);
    }

    Replace1 replace_getDerivations = new Replace1() {
	    public ATerm apply(ATerm subject) {
		if (subject instanceof Instruction) {
		    %match(Instruction subject) {
			CompiledPattern(patterns,automata) -> {
			    verif.build_tree(automata);
			    // do not modify the term (for now at least)
			    return traversal().genericTraversal(subject,this);
			}
		    }
		}// end instanceof Instruction
		/*
		 * Default case : Traversal
		 */
		return traversal().genericTraversal(subject,this);
	    }//end apply
	};//end new Replace1 

}
