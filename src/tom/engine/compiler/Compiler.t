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
 *
 **/

package tom.engine.compiler;

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

import tom.engine.adt.tominstruction.types.constraintinstructionlist.concConstraintInstruction;
import tom.engine.adt.tomslot.types.slotlist.concSlot;
import tom.engine.adt.tomsignature.types.tomvisitlist.concTomVisit;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.sl.*;

/**
 * The Compiler plugin.
 */
public class Compiler extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../../library/mapping/java/util/types/Map.tom}
  
  %typeterm Compiler {
    implement { Compiler }
    is_sort(t) { ($t instanceof Compiler) }
  }

  %op Strategy ChoiceTopDown(s1:Strategy) {
    make(v) { (`mu(MuVar("x"),ChoiceId(v,All(MuVar("x"))))) }
  }

  /** some output suffixes */
  public static final String COMPILED_SUFFIX = ".tfix.compiled";

  /** the declared options string*/
  public static final String DECLARED_OPTIONS = "<options>" +
    "<boolean name='compile' altName='' description='Compiler (activated by default)' value='true'/>" +
    "<boolean name='autoDispatch' altName='ad' description='The content of \"visitor_fwd\" is ignored, and a dispatch mechanism is automatically generated in %strategy ' value='false'/>" +
    "</options>";
  
  private static final String basicStratName = "tom.library.sl.BasicStrategy";
  private static final TomType objectType = `TomTypeAlone("Object");
  private static final TomType introspectorType = `TomTypeAlone("tom.library.sl.Introspector");
  // introspector argument of visitLight
  private static final TomTerm introspectorVar = `Variable(concOption(),Name("introspector"),introspectorType,concConstraint());
    
  /** if the flag is true, the class generated from %strategy inherits from BasicStrategy and handles the dispatch*/
  private static boolean autoDispatch = false;

  /** unicity var counter */
  private static int absVarNumber;

  /** Constructor */
  public Compiler() {
    super("Compiler");
  }

  public void run() {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");    
    try {
      // reinit absVarNumber to generate reproducible output
      absVarNumber = 0;
      TomTerm compiledTerm = ConstraintCompiler.compile((TomTerm)getWorkingTerm(),getStreamManager().getSymbolTable());
      //System.out.println("compiledTerm = \n" + compiledTerm);            
      Collection hashSet = new HashSet();
      TomTerm renamedTerm = (TomTerm) `TopDown(findRenameVariable(hashSet)).visitLight(compiledTerm);
      //System.out.println("renamedTerm = \n" + renamedTerm);
      // verbose
      getLogger().log(Level.INFO, TomMessage.tomCompilationPhase.getMessage(),
          new Integer((int)(System.currentTimeMillis()-startChrono)) );
      setWorkingTerm(renamedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + COMPILED_SUFFIX, (TomTerm)getWorkingTerm());
      }
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getStreamManager().getInputFileName(), "Compiler", e.getMessage()} );
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(Compiler.DECLARED_OPTIONS);
  }
 
  /*
   * add a prefix (tom_) to back-quoted variables which comes from the lhs
   */
  %strategy findRenameVariable(context:Collection) extends `Identity() {
    visit TomTerm {
      var@(Variable|VariableStar)[AstName=astName@Name(name)] -> {
        if(context.contains(`astName)) {          
          return `var.setAstName(`Name(ASTFactory.makeTomVariableName(name)));
        }
      }
    }

    visit Instruction {
      CompiledPattern(patternList,instruction) -> {
        // only variables found in LHS have to be renamed (this avoids that the JAVA ones are renamed)
        Collection newContext = new ArrayList();
        `TopDown(CollectLHSVars(newContext)).visitLight(`patternList);        
        newContext.addAll(context);
        return (Instruction)`TopDown(findRenameVariable(newContext)).visitLight(`instruction);
      }
    }  
  }  
  
  %strategy CollectLHSVars(Collection bag) extends Identity() {
    visit Constraint {
      MatchConstraint(p,_) -> {        
        Map map = TomBase.collectMultiplicity(`p);
        Collection newContext = new HashSet(map.keySet());
        bag.addAll(newContext);
      }
    }
  }
}
