/**
 *
 * The TomVerifier plugin.
 *
 */

package jtom.verifier;

import jtom.*;
import jtom.adt.tomsignature.types.*;

import aterm.*;
import java.util.*;
import jtom.tools.*;
import jtom.runtime.*;
import jtom.adt.tomsignature.types.*;
import jtom.TomMessage;

public class TomVerifier extends TomBase implements TomPlugin
{
    %include{ ../adt/TomSignature.tom }

    private TomTerm term;
    private OptionList myOptions;

    protected Verifier verif;

    public TomVerifier()
    {
	myOptions = `concOption(OptionBoolean("verify", "", "Verify correctness of match compilation", False()) // activation flag
				);
	verif = new Verifier();
    }

    public void setInput(ATerm term)
    {
	if (term instanceof TomTerm)
	    this.term = (TomTerm)term;
	else
	    environment().messageError(TomMessage.getString("TomTermExpected"),
				       "TomParserPlugin", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    }

    public ATerm getOutput()
    {
	return term;
    }

    public void run()
    {
	try{
	    long startChrono = System.currentTimeMillis();
	    OptionList list = `concOption(myOptions*);
	    boolean willRun = true;
	
	    boolean verbose = ((Boolean)getServer().getOptionValue("verbose")).booleanValue();

	    while(!(list.isEmpty()))
		{
		    Option h = list.getHead();
		    %match(Option h)
			{
			    OptionBoolean[name="verify", valueB=val] -> 
				{ 
				    %match(TomBoolean val)
					{
					    True() -> { willRun = true; }
					    False() -> { willRun = false; }
					}
				}
			}

		    list = list.getTail();
		}
	    if(willRun)
		{
		    TomTerm extractTerm = `emptyTerm();
		    // here the extraction stuff
		
		    Collection matchSet = collectMatch(term);
		    // System.out.println("Extracted : " + matchSet);
		
		    Collection purified = purify(matchSet);
		    // System.out.println("Purified : " + purified);
		
		    Collection derivations = getDerivations(purified);

		    if(verbose)
			System.out.println("TOM verification phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");
		}
	    else
		{
		    if(verbose)
			System.out.println("The verifier is not activated and thus WILL NOT RUN.");
		}
	    
	    environment().printAlertMessage("TomVerifier");
		if(!environment().isEclipseMode()) {
		    // remove all warning (in command line only)
		    environment().clearWarnings();
		}
	}catch (Exception e) {
	    environment().messageError("Exception occured in TomVerifierExtract: " + e.getMessage(),
				       environment().getInputFile().getName(), 
				       TomMessage.DEFAULT_ERROR_LINE_NUMBER);
	    e.printStackTrace();
	}
    }

    public OptionList declareOptions()
    {
// 	int i = 0;
// 	OptionList list = `concOption(myOptions*);
// 	while(!(list.isEmpty()))
// 	    {
// 		i++;
// 		list = list.getTail();
// 	    }

	//System.out.println("1.8. The verifier declares " +i+ " options.");
	return myOptions;
    }

    public OptionList requiredOptions()
    {
	return `emptyOptionList();
    }

    public void setOption(String optionName, String optionValue)
    {
 	%match(OptionList myOptions)
 	    {
		concOption(av*, OptionBoolean(n, alt, desc, val), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			{
			    %match(String optionValue)
				{
				    ('true') ->
					{ myOptions = `concOption(av*, ap*, OptionBoolean(n, alt, desc, True())); }
				    ('false') ->
					{ myOptions = `concOption(av*, ap*, OptionBoolean(n, alt, desc, False())); }
				}
			}
		}
		concOption(av*, OptionInteger(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concOption(av*, ap*, OptionInteger(n, alt, desc, Integer.parseInt(optionValue), attr));
		}
		concOption(av*, OptionString(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concOption(av*, ap*, OptionString(n, alt, desc, optionValue, attr));
		}
	    }
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
