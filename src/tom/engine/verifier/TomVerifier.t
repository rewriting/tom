/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package jtom.verifier;

import jtom.*;
import jtom.tools.*;
import jtom.adt.tomsignature.types.*;

import aterm.*;
import java.util.*;
import java.util.logging.*;

import tom.library.traversal.*;
import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;
import tom.platform.OptionParser;


/**
 * The TomVerifier plugin.
 */
public class TomVerifier extends TomGenericPlugin {
  
  %include{ adt/TomSignature.tom }
  
  public static final String DECLARED_OPTIONS = "<options><boolean name='verify' altName='' description='Verify correctness of match compilation' value='false'/></options>";

  protected Verifier verif;

  public TomVerifier() {
    super("TomVerifier");
    verif = new Verifier();
  }

  public void run() {
    if(isActivated()) {
      try {
        int errorsAtStart = getStatusHandler().nbOfErrors();
        int warningsAtStart = getStatusHandler().nbOfWarnings();

        long startChrono = System.currentTimeMillis();

        TomTerm extractTerm = `emptyTerm();
        // here the extraction stuff
			
        Collection matchSet = collectMatch((TomTerm)getWorkingTerm());
        // System.out.println("Extracted : " + matchSet);
		
        Collection purified = purify(matchSet);
        // System.out.println("Purified : " + purified);
			
        Collection derivations = getDerivations(purified);

        getLogger().log( Level.INFO,
                         "TomVerificationPhase",
                         new Integer((int)(System.currentTimeMillis()-startChrono)) );
	    
        printAlertMessage(errorsAtStart, warningsAtStart);
	
      } catch (Exception e) {
        getLogger().log( Level.SEVERE,
                         "ExceptionMessage",
                         new Object[]{getStreamManager().getInputFile().getName(), "TomVerifier", e.getMessage()} );
        e.printStackTrace();
      }
    } else {	    
      getLogger().log(Level.INFO, "The verifier is not activated and thus WILL NOT RUN.");
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomVerifier.DECLARED_OPTIONS);
  }

  private boolean isActivated() {
    return getOptionBooleanValue("verify");
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
  
} // class TomVerifier
