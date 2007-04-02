/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2007, INRIA
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

import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.TomMessage;
import tom.engine.tools.Tools;
import tom.engine.tools.TomGenericPlugin;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.*;

import aterm.ATerm;
import tom.engine.adt.il.types.*;
import tom.engine.adt.zenon.types.*;
/**
 * The TomVerifier plugin.
 */
public class TomVerifier extends TomGenericPlugin {

  %include{ adt/tomsignature/TomSignature.tom }
  %include { mustrategy.tom }
  %typeterm Collection {
    implement { java.util.Collection }
  }

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

        // verbose
        getLogger().log(Level.INFO, TomMessage.tomVerificationPhase.getMessage(),
                        new Integer((int)(System.currentTimeMillis()-startChrono)));

      } catch (Exception e) {
        getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
                         new Object[]{getClass().getName(),
                                      getStreamManager().getInputFileName(),
                                      e.getMessage()} );
        e.printStackTrace();
      }
    } else {
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

  %strategy collectMatch(collection:Collection) extends `Identity() {
    visit Instruction {
      CompiledMatch[AutomataInst=automata]  -> {
        collection.add(`automata);
      }
    }
  }

  public static Collection collectMatch(TomTerm subject) {
    Collection result = new HashSet();
    try {
      `TopDown(collectMatch(result)).visit(subject);
    } catch (jjtraveler.VisitFailure e) {
      throw new TomRuntimeException("Strategy collectMatch failed");
    }
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

  %strategy ilSimplifier() extends `Identity() {
    visit Expression {
      Or(cond,FalseTL()) -> {
        return `cond;
      }
    }

    visit Instruction {
      If(TrueTL(),success,Nop()) -> {
        return `success;
      }
      (UnamedBlock|AbstractBlock)(concInstruction(inst)) -> {
        return `inst;
      }
      (Let|LetRef|LetAssign)[Variable=(UnamedVariable|UnamedVariableStar)[],AstInstruction=body] -> {
        return `body;
      }

      CompiledPattern[AutomataInst=inst] -> {
        return `inst;
      }
    }
  }

  private Instruction simplifyIl(Instruction subject) {
    try {
      subject = (Instruction) `TopDown(ilSimplifier()).visit(subject);
    } catch (jjtraveler.VisitFailure e) {
      throw new TomRuntimeException("Strategy simplifyIl failed");
    }
    return subject;
  }

  void filterAssociative(Collection c) {
    for (Iterator i = c.iterator(); i.hasNext(); )
      if (containsAssociativeOperator((Instruction) i.next()))
        i.remove();
  }

  boolean containsAssociativeOperator(Instruction subject) {
    Collection result = new HashSet();
    try {
      `TopDown(associativeOperatorCollector(result)).visit(subject);
    } catch (jjtraveler.VisitFailure e) {
      throw new TomRuntimeException("Strategy containsAssociativeOperator failed");
    }
    return !result.isEmpty();
  }

  %strategy associativeOperatorCollector(store:Collection) extends `Identity() {
    visit Instruction {
      subject@LetRef[]  -> {
        store.add(`subject);
      }
      subject@WhileDo[] -> {
        store.add(`subject);
      }
      subject@DoWhile[] -> {
        store.add(`subject);
      }
    }
    visit Expression {
      /* we filter also patterns containing or() constructs */
      subject@Or(_,_) -> {
        store.add(`subject);
      }
    }
  }

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
    if(!tail.isEmptyconcPattern()) {
      h = tail.getHeadconcPattern();
      tail = tail.getTailconcPattern();
      result.append(patternToString(h));
    }

    while(!tail.isEmptyconcPattern()) {
      h = tail.getHeadconcPattern();
      result.append("," + patternToString(h));
      tail = tail.getTailconcPattern();
    }
    return result.toString();
  }

  public String patternToString(Pattern pattern) {
    String result = "";
    %match(Pattern pattern) {
      Pattern[TomList=tomList] -> {
        return patternToString(`tomList);
      }
    }
    return result;
  }

  public String patternToString(TomList tomList) {
    StringBuffer result = new StringBuffer();
    TomTerm h = null;
    TomList tail = tomList;
    if(!tail.isEmptyconcTomTerm()) {
      h = tail.getHeadconcTomTerm();
      tail = tail.getTailconcTomTerm();
      result.append(patternToString(h));
    }

    while(!tail.isEmptyconcTomTerm()) {
      h = tail.getHeadconcTomTerm();
      result.append("," + patternToString(h));
      tail = tail.getTailconcTomTerm();
    }
    return result.toString();
  }
  public String patternToString(TomTerm tomTerm) {
    %match(TomTerm tomTerm) {
      TermAppl[NameList=concTomName(Name(name),_*),Args=childrens] -> {
        if (`childrens.isEmptyconcTomTerm()) {
          return `name;
        } else {
          `name = `name + "(";
          TomTerm head = `childrens.getHeadconcTomTerm();
          `name += patternToString(head);
          TomList tail = `childrens.getTailconcTomTerm();
          while(!tail.isEmptyconcTomTerm()) {
            head = tail.getHeadconcTomTerm();
            `name += "," + patternToString(head);
            tail = tail.getTailconcTomTerm();
          }
          `name += ")";
          return `name;
        }
      }
      Variable[AstName=Name(name)] -> {
        return `name;
      }
      UnamedVariable[] -> {
        return "\\_";
      }
    }
    return "StrangePattern" + tomTerm;
  }
}
