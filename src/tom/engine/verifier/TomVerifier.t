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
 * **/

package jtom.verifier;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;

import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.tools.TomGenericPlugin;
import tom.library.traversal.Collect2;
import tom.library.traversal.Replace1;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;
import jtom.verifier.il.types.*;
/**
 * The TomVerifier plugin.
 */
public class TomVerifier extends TomGenericPlugin {
  
  %include{ adt/TomSignature.tom }
  
  public static final String DECLARED_OPTIONS = "<options><boolean name='verify' altName='' description='Verify correctness of match compilation' value='false'/></options>";

  protected Verifier verif;
  protected LatexOutput output;
  protected StatOutput stats;

  public TomVerifier() {
    super("TomVerifier");
    init();
  }

  void init () {
    verif = new Verifier();
    output = new LatexOutput(this);
    stats = new StatOutput(this);
  }

  public void run() {
    if(isActivated()) {
      long startChrono = System.currentTimeMillis();
      try {
        // here the extraction stuff
        Collection matchSet = collectMatch((TomTerm)getWorkingTerm());

        Collection purified = purify(matchSet);
        // System.out.println("Purified : " + purified);        

        // removes all associative patterns
        filterAssociative(purified);

        Collection derivations = getDerivations(purified);
        // System.out.println("Derivations : " + derivations);

        // the latex output stuff
        String latex = output.build_latex(derivations);
        System.out.println(latex);

        // the stats output stuff
//          String statistics = stats.build_stats(derivations);
//          System.out.println(statistics);

        // verbose
        getLogger().log(Level.INFO, "TomVerificationPhase",
                        new Integer((int)(System.currentTimeMillis()-startChrono)));
        
      } catch (Exception e) {
        getLogger().log(Level.SEVERE, "ExceptionMessage",
                         new Object[]{getClass().getName(),
                                      getStreamManager().getInputFile().getName(),
                                      e.getMessage()} );
        e.printStackTrace();
      }
    } else {      
      // Not active plugin
      getLogger().log(Level.INFO, "VerifierInactivated");
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
            CompiledMatch(automata, _)  -> {
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
    return result;
  }

  public Collection purify(Collection subject) {
    Collection purified = new HashSet();
    Iterator it = subject.iterator();
    while (it.hasNext()) {
      Instruction cm = (Instruction)it.next();
      // simplify the IL automata
      purified.add((simplify_il(cm)));
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
          // the checkstamp should be managed another way 
          %match(Instruction subject) {
            IfThenElse(TrueTL(),success,Nop()) -> {
              return traversal().genericTraversal(`success,this);
            }
            (UnamedBlock|AbstractBlock)(concInstruction(CheckStamp[],inst)) -> {
              return traversal().genericTraversal(`inst,this);
            }
            (Let|LetRef|LetAssign)((UnamedVariable|UnamedVariableStar)[],_,body) -> {
              return traversal().genericTraversal(`body,this);
            }

            CompiledPattern(patterns,inst) -> {
              return traversal().genericTraversal(`inst,this);
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
  
  void filterAssociative(Collection c) {
    for (Iterator i = c.iterator(); i.hasNext(); )
      if (containsAssociativeOperator((Instruction) i.next()))
        i.remove();
  }

  boolean containsAssociativeOperator(Instruction subject) {
    Collection result = new HashSet();
    traversal().genericCollect(subject,collect_associativeOps,result);
    return !result.isEmpty();   
  }

  private Collect2 collect_associativeOps = new Collect2() {
      public boolean apply(ATerm subject, Object astore) {
        Collection store = (Collection)astore;
        if (subject instanceof Instruction) {
          %match(Instruction subject) {
            LetRef[]  -> {
              store.add(subject);
            }
            WhileDo[] -> {
              store.add(subject);
            }
            DoWhile[] -> {
              store.add(subject);
            }
          }//end match
        } 
        else if (subject instanceof Expression) {
          %match(Expression subject) {
            // we filters also patterns containing or() constructs (but we could handle it easily
            Or(_,_) -> {
              store.add(subject);
            }
          }
        }
        return true;
      }//end apply
    }; //end new  

  public Collection getDerivations(Collection subject) {
    Collection derivations = new HashSet();
    Iterator it = subject.iterator();
    
    while (it.hasNext()) {
      Instruction cm = (Instruction) it.next();
			%match(Instruction cm) {
        CompiledMatch(automata, options)  -> {
					derivations.add(verif.build_tree(automata));
        }
			}
		}
    return derivations;
  }
  
  public String pattern_to_string(ATerm patternList) {
    return pattern_to_string((PatternList) patternList);
  }

  public String pattern_to_string(PatternList patternList) {
    String result = "";
    Pattern h = null;
    PatternList tail = patternList;
    if(!tail.isEmpty()) {
      h = tail.getHead();
      tail = tail.getTail();
      result = pattern_to_string(h);
    }

    while(!tail.isEmpty()) {
      h = tail.getHead();
      result = "," + pattern_to_string(h);
      tail = tail.getTail();
    }
    return result;
  }

  public String pattern_to_string(Pattern pattern) {
    String result = "";
    %match(Pattern pattern) {
      Pattern(tomList) -> {
        return pattern_to_string(tomList);
      }
    }
    return result;
  }
  public String pattern_to_string(TomList tomList) {
    String result = "";
    TomTerm h = null;
    TomList tail = tomList;
    if(!tail.isEmpty()) {
      h = tail.getHead();
      tail = tail.getTail();
      result = pattern_to_string(h);
    }

    while(!tail.isEmpty()) {
      h = tail.getHead();
      result = "," + pattern_to_string(h);
      tail = tail.getTail();
    }
    return result;
  }
  public String pattern_to_string(TomTerm tomTerm) {
    %match(TomTerm tomTerm) {
      Appl(_,concTomName(Name(name),_*),childrens,_) -> {
        if (childrens.isEmpty()) {
          return name;
        } else {
          name = name + "(";
          TomTerm head = childrens.getHead();
          name += pattern_to_string(head);
          TomList tail = childrens.getTail();
          while(!tail.isEmpty()) {
            head = tail.getHead();
            name += "," + pattern_to_string(head);
            tail = tail.getTail();
          }
          name += ")";
          return name;
        }
      }
      Variable(_,Name(name),_,_) -> {
        return name;
      }
      UnamedVariable[] -> {
        return "\\_";
      }
    }
    return "StrangePattern" + tomTerm;
  }
  
} // class TomVerifier
