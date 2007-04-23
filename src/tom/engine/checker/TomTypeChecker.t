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
 * Julien Guyon
 *
 **/

package tom.engine.checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.platform.OptionParser;

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

import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * The TomTypeChecker plugin.
 */
public class TomTypeChecker extends TomChecker {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { mustrategy.tom }

  /** the declared options string */
  public static final String DECLARED_OPTIONS = "<options><boolean name='noTypeCheck' altName='' description='Do not perform type checking' value='false'/></options>";

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomTypeChecker.DECLARED_OPTIONS);
  }

  /** Constructor */
  public TomTypeChecker() {
    super("TomTypeChecker");
  }

  public void run() {
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        try {
          MuTraveler.init(`mu(MuVar("x"),Try(Sequence(checkTypeInference(this),All(MuVar("x")))))).visit((TomTerm)getWorkingTerm());
        } catch(jjtraveler.VisitFailure e) {
          System.out.println("strategy failed");
        }
        // verbose
        getLogger().log( Level.INFO, TomMessage.tomTypeCheckingPhase.getMessage(),
            new Integer((int)(System.currentTimeMillis()-startChrono)) );
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
            new Object[]{getClass().getName(), getStreamManager().getInputFileName(),e.getMessage()} );
        e.printStackTrace();
      }
    } else {
      // type checker desactivated    
      getLogger().log(Level.INFO, TomMessage.typeCheckerInactivated.getMessage());
    }
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noTypeCheck");
  }

  /**
   * Main type checking entry point:
   * We check all Match
   */
  %typeterm TomTypeChecker { implement { TomTypeChecker } }

  %strategy checkTypeInference(ttc:TomTypeChecker) extends `Identity() {
    visit Instruction {
      Match(_, patternInstructionList, oplist) -> {  
        ttc.currentTomStructureOrgTrack = TomBase.findOriginTracking(`oplist);
        ttc.verifyMatchVariable(`patternInstructionList);
        `Fail().visit(null);
      }
    }
    visit Declaration {
      Strategy(_,_,visitList,orgTrack) -> {
        ttc.currentTomStructureOrgTrack = `orgTrack;
        ttc.verifyStrategyVariable(`visitList);
        `Fail().visit(null);
      }
    }
  } //checkTypeInference

  /* 
   * Collect unknown (not in symbol table) appls without ()
   */
  %strategy collectUnknownAppls(ttc:TomTypeChecker) extends `Identity() {
    visit TomTerm {
      app@TermAppl[] -> {
        if(ttc.symbolTable().getSymbolFromName(ttc.getName(`app))==null) {
          ttc.messageError(findOriginTrackingFileName(`app.getOption()),
              findOriginTrackingLine(`app.getOption()),
              TomMessage.unknownVariableInWhen,
              new Object[]{ttc.getName(`app)});
        }
        // else, it's actually app()
        // else, it's a unknown (ie : java) function
      }
    } 
  }

  private void verifyMatchVariable(PatternInstructionList patternInstructionList) {
    while(!patternInstructionList.isEmptyconcPatternInstruction()) {
      PatternInstruction pa = patternInstructionList.getHeadconcPatternInstruction();
      Pattern pattern = pa.getPattern();
      // collect variables
      ArrayList variableList = new ArrayList();
      TomBase.collectVariable(variableList, pattern);
      verifyVariableTypeListCoherence(variableList);
      // verify variables in WHEN instruction
      // collect unknown variables
      try {
        MuTraveler.init(`TopDown(collectUnknownAppls(this))).visit(pattern.getGuards());
      } catch(jjtraveler.VisitFailure e) {
        System.out.println("strategy failed");
      }

      patternInstructionList = patternInstructionList.getTailconcPatternInstruction();
    }
  }

  private void verifyStrategyVariable(TomVisitList list) {
    TomForwardType visitorFwd = null;
    TomForwardType currentVisitorFwd = null;
    while(!list.isEmptyconcTomVisit()) {
      TomVisit visit = list.getHeadconcTomVisit();
      %match(TomVisit visit) {
        VisitTerm(visitType,patternInstructionList,options) -> {
          String fileName =findOriginTrackingFileName(`options);
          %match(TomType visitType) {
            TomTypeAlone(strVisitType) -> {
              messageError(fileName,
                  findOriginTrackingLine(`options),
                  TomMessage.unknownVisitedType,
                  new Object[]{`(strVisitType)});
            }
            Type(ASTTomType(ASTVisitType),TLType(TLVisitType)) -> {
              //check that all visitType have same visitorFwd

              currentVisitorFwd = symbolTable().getForwardType(`ASTVisitType);

              //noVisitorFwd defined for visitType
              if (currentVisitorFwd == null || currentVisitorFwd == `EmptyForward()){ 
                messageError(fileName,`TLVisitType.getStart().getLine(),
                    TomMessage.noVisitorForward,
                    new Object[]{`(ASTVisitType)});
              } else if (visitorFwd == null) {
                //first visit 
                visitorFwd = currentVisitorFwd;
              } else {
                //check if current visitor equals to previous visitor
                if (currentVisitorFwd != visitorFwd){ 
                  messageError(fileName,`TLVisitType.getStart().getLine(),
                      TomMessage.differentVisitorForward,
                      new Object[]{visitorFwd.getString(),currentVisitorFwd.getString()});
                }
              }
              verifyMatchVariable(`patternInstructionList);
            } 
          }
        }
      }
      // next visit
      list = list.getTailconcTomVisit();
    }
  }

  private void verifyVariableTypeListCoherence(ArrayList list) {
    // compute multiplicities
    //System.out.println("list = " + list);
    HashMap map = new HashMap();
    Iterator it = list.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      if(map.containsKey(name)) {
        TomTerm var = (TomTerm)map.get(name);
        //System.out.println("variable = " + variable);
        //System.out.println("var = " + var);
        TomType type1 = var.getAstType();
        TomType type2 = variable.getAstType();
        if(!(type1==type2)) {
          messageError(findOriginTrackingFileName(variable.getOption()),
              findOriginTrackingLine(variable.getOption()),
              TomMessage.incoherentVariable,
              new Object[]{name.getString(), TomBase.getTomType(type1), TomBase.getTomType(type2)});
        }
      } else {
        map.put(name, variable);
      }
    }
  }

} // class TomTypeChecker
