/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 **/

package tom.engine.verifier;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.*;
import java.util.logging.Level;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomMessage;
import tom.engine.tools.Tools;
import tom.engine.tools.TomGenericPlugin;
import tom.library.traversal.Collect2;
import tom.library.traversal.Replace1;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;
import tom.engine.adt.il.types.*;
import tom.engine.adt.zenon.types.*;
/**
 * The TomVerifier plugin.
 */
public class TomVerifier extends TomGenericPlugin {
  
  %include{ adt/tomsignature/TomSignature.tom }
  
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='verify' altName='' description='Verify correctness of match compilation' value='false'/>" +
    "<boolean name='noReduce' altName='' description='Do not simplify extracted constraints (depends on --verify)' value='false'/>" +
    "<boolean name='camlSemantics' altName='' description='Verify with caml semantics for match' value='false'/>" +
    "</options>";

  public static final String ZENON_SUFFIX = ".zv";
  public static final String INTERMEDIATE_SUFFIX = ".tfix.zenon";
  
  protected Verifier verif;
  protected ZenonOutput zenon;

  public TomVerifier() {
    super("TomVerifier");
  }

  public void run() {
    boolean camlsemantics = getOptionBooleanValue("camlSemantics");
    boolean intermediate = getOptionBooleanValue("intermediate");
    verif = new Verifier(camlsemantics);
    verif.setSymbolTable(this.symbolTable());
    // delay the zenonoutput creation, as it needs the verifiers
    // symboltable to be properly set
    if(isActivated()) {
      zenon = new ZenonOutput(verif);
      long startChrono = System.currentTimeMillis();
      try {

        Collection matchingCode = getMatchingCode();

        // Collection derivations = getDerivations(matchingCode);
        // System.out.println("Derivations : " + derivations);

        Map rawConstraints = getRawConstraints(matchingCode);
        //System.out.println(rawConstraints);

        // reduce constraints
        verif.mappingReduce(rawConstraints);
        if (!getOptionBooleanValue("noReduce")) {
          verif.booleanReduce(rawConstraints);
        }

        Collection zspecSet = zenon.zspecSetFromConstraintMap(rawConstraints);
        if(intermediate) {
          Tools.generateOutputFromCollection(getStreamManager().getOutputFileNameWithoutSuffix() + INTERMEDIATE_SUFFIX, zspecSet);
        }

        // the latex output stuff
        // LatexOutput output;
        // output = new LatexOutput(this);
        // String latex = output.build_latex(derivations);
        // System.out.println(latex);

        // the zenon output stuff
        // Collection zen = zenon.zspecSetFromDerivationTreeSet(derivations);

        ZenonBackend back = new ZenonBackend(verif);
        //System.out.println(back.genZSpecCollection(zen));
        String output = back.genZSpecCollection(zspecSet);
        
        // do not generate a file if there is no proof to do
        if (!zspecSet.isEmpty()) {
          try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
                      getStreamManager().getOutputFileNameWithoutSuffix() + ZENON_SUFFIX
                      ))));
            writer.write(output);
            writer.close();
          } catch (IOException e) {
            getLogger().log( Level.SEVERE, TomMessage.backendIOException.getMessage(),
                new Object[]{getStreamManager().getOutputFile().getName(), e.getMessage()} );
            return;
          }
        }

        // The stats output stuff
        // StatOutput stats;
        // stats = new StatOutput(this);
        // String statistics = stats.build_stats(derivations);
        // System.out.println(statistics);

        // verbose
        getLogger().log(Level.INFO, TomMessage.tomVerificationPhase.getMessage(),
                        new Integer((int)(System.currentTimeMillis()-startChrono)));
        
      } catch (Exception e) {
        getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
                         new Object[]{getClass().getName(),
                                      getStreamManager().getInputFile().getName(),
                                      e.getMessage()} );
        e.printStackTrace();
      }
    } else {      
      // Not active plugin
      getLogger().log(Level.INFO, TomMessage.verifierInactivated.getMessage());
    }
  }

  protected Collection getMatchingCode() {
        // here the extraction stuff
        Collection matchSet = collectMatch((TomTerm)getWorkingTerm());

        Collection purified = purify(matchSet);
        // System.out.println("Purified : " + purified);        

        // removes all associative patterns
        filterAssociative(purified);

        return purified;
  }
  
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomVerifier.DECLARED_OPTIONS);
  }

  private boolean isActivated() {
    return getOptionBooleanValue("verify");
  }
  
  private Collect2 matchCollector = new Collect2() {
      public boolean apply(ATerm subject, Object astore) {
        Collection store = (Collection)astore;
				%match(Instruction subject) {
					CompiledMatch[automataInst=automata]  -> {
						store.add(`automata);
					}
				}//end match
				return true;
      }//end apply
    }; //end new
  
  public Collection collectMatch(TomTerm subject) {
    Collection result = new HashSet();
    traversal().genericCollect(subject,matchCollector,result);
    return result;
  }

  public Collection purify(Collection subject) {
    Collection purified = new HashSet();
    Iterator it = subject.iterator();
    while (it.hasNext()) {
      Instruction cm = (Instruction)it.next();
      // simplify the IL automata
      purified.add((simplifyIl(cm)));
    }
    return purified;
  }
  
  Replace1 ilSimplifier = new Replace1() {
      public ATerm apply(ATerm subject) {
				%match(Expression subject) {
					Or(cond,FalseTL()) -> {
						return traversal().genericTraversal(`cond,this);
					}
				}
				// the checkstamp should be managed another way 
				%match(Instruction subject) {
					If(TrueTL(),success,Nop()) -> {
						return traversal().genericTraversal(`success,this);
					}
					(UnamedBlock|AbstractBlock)(concInstruction(CheckStamp[],inst)) -> {
						return traversal().genericTraversal(`inst,this);
					}
					(Let|LetRef|LetAssign)[variable=(UnamedVariable|UnamedVariableStar)[],astInstruction=body] -> {
						return traversal().genericTraversal(`body,this);
					}

					CompiledPattern[automataInst=inst] -> {
						return traversal().genericTraversal(`inst,this);
					}
          CheckInstance[instruction=inst] -> {
						return traversal().genericTraversal(`inst,this);
          }
				}
        /*
         * Default case : Traversal
         */
        return traversal().genericTraversal(subject,this);
      }//end apply
    };//end new Replace1 ilSimplifier
  
  private Instruction simplifyIl(Instruction subject) {
    return (Instruction) ilSimplifier.apply(subject);
  }
  
  void filterAssociative(Collection c) {
    for (Iterator i = c.iterator(); i.hasNext(); )
      if (containsAssociativeOperator((Instruction) i.next()))
        i.remove();
  }

  boolean containsAssociativeOperator(Instruction subject) {
    Collection result = new HashSet();
    traversal().genericCollect(subject,associativeOperatorCollector,result);
    return !result.isEmpty();   
  }

  private Collect2 associativeOperatorCollector = new Collect2() {
      public boolean apply(ATerm subject, Object astore) {
        Collection store = (Collection)astore;
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
				%match(Expression subject) {
					// we filters also patterns containing or() constructs
					Or(_,_) -> {
						store.add(subject);
					}
        }
        return true;
      }//end apply
    }; //end new  

  public Collection getDerivations(Collection subject) {
    Collection derivations = new HashSet();
    Iterator it = subject.iterator();
    
    while (it.hasNext()) {
      Instruction automata = (Instruction) it.next();
      Collection trees = verif.build_tree(automata);
      derivations.addAll(trees);
    }
    return derivations;
  }

  public Map getRawConstraints(Collection subject) {
    Map rawConstraints = new HashMap();
    Iterator it = subject.iterator();
    
    while (it.hasNext()) {
      Instruction automata = (Instruction) it.next();
      Map trees = verif.getConstraints(automata);
      rawConstraints.putAll(trees);
    }
    return rawConstraints;
  }
  
  public String patternToString(ATerm patternList) {
    return patternToString((PatternList) patternList);
  }

  public String patternToString(PatternList patternList) {
    StringBuffer result = new StringBuffer();
    Pattern h = null;
    PatternList tail = patternList;
    if(!tail.isEmpty()) {
      h = tail.getHead();
      tail = tail.getTail();
      result.append(patternToString(h));
    }

    while(!tail.isEmpty()) {
      h = tail.getHead();
      result.append("," + patternToString(h));
      tail = tail.getTail();
    }
    return result.toString();
  }

  public String patternToString(Pattern pattern) {
    String result = "";
    %match(Pattern pattern) {
      Pattern[tomList=tomList] -> {
        return patternToString(`tomList);
      }
    }
    return result;
  }

  public String patternToString(TomList tomList) {
    StringBuffer result = new StringBuffer();
    TomTerm h = null;
    TomList tail = tomList;
    if(!tail.isEmpty()) {
      h = tail.getHead();
      tail = tail.getTail();
      result.append(patternToString(h));
    }

    while(!tail.isEmpty()) {
      h = tail.getHead();
      result.append("," + patternToString(h));
      tail = tail.getTail();
    }
    return result.toString();
  }
  public String patternToString(TomTerm tomTerm) {
    %match(TomTerm tomTerm) {
      TermAppl[nameList=concTomName(Name(name),_*),args=childrens] -> {
        if (`childrens.isEmpty()) {
          return `name;
        } else {
          `name = `name + "(";
          TomTerm head = `childrens.getHead();
          `name += patternToString(head);
          TomList tail = `childrens.getTail();
          while(!tail.isEmpty()) {
            head = tail.getHead();
            `name += "," + patternToString(head);
            tail = tail.getTail();
          }
          `name += ")";
          return `name;
        }
      }
      Variable[astName=Name(name)] -> {
        return `name;
      }
      UnamedVariable[] -> {
        return "\\_";
      }
    }
    return "StrangePattern" + tomTerm;
  }

} // class TomVerifier
