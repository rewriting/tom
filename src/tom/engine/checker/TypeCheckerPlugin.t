/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2010, INPL, INRIA
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
import java.util.Map;
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
import tom.engine.adt.code.types.*;

import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;
import tom.library.sl.*;

/**
 * The TomTypeChecker plugin.
 */
public class TypeCheckerPlugin extends CheckerPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='noTypeCheck' altName='' description='Do not perform type checking' value='false'/>" +
    "</options>";

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TypeCheckerPlugin.DECLARED_OPTIONS);
  }

  /** Constructor */
  public TypeCheckerPlugin() {
    super("TypeCheckerPlugin");
  }

  public void run(Map informationTracker) {
    //System.out.println("(debug) I'm in the Tom TypeChecker : TSM"+getStreamManager().toString());
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        try {
          Code subject = (Code) getWorkingTerm();
          //System.out.println("type checking: ");
          //System.out.println(subject);
          `TopDownCollect(checkTypeInference(this)).visitLight(subject);
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("strategy failed");
        }
        // verbose
        getLogger().log( Level.INFO, TomMessage.tomTypeCheckingPhase.getMessage(), Integer.valueOf((int)(System.currentTimeMillis()-startChrono)) );
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(), new Object[]{getClass().getName(), getStreamManager().getInputFileName(),e.getMessage()} );
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
  %typeterm TypeCheckerPlugin { implement { TypeCheckerPlugin } }

  %strategy checkTypeInference(tcp:TypeCheckerPlugin) extends Identity() {
    visit Instruction {
      Match(constraintInstructionList, oplist) -> {  
        tcp.currentTomStructureOrgTrack = TomBase.findOriginTracking(`oplist);
        tcp.verifyMatchVariable(`constraintInstructionList);
        throw new tom.library.sl.VisitFailure();// to stop the top-downd
      }
    }

    visit Declaration {
      Strategy(_,_,visitList,orgTrack) -> {
        tcp.currentTomStructureOrgTrack = `orgTrack;
        tcp.verifyStrategyVariable(`visitList);
        throw new tom.library.sl.VisitFailure();// to stop the top-downd
      }
    }

  }

  /* 
   * Collect unknown (not in symbol table) appls without ()
   */
  %strategy collectUnknownAppls(tcp:TypeCheckerPlugin) extends Identity() {
    visit TomTerm {
      app@TermAppl[] -> {
        if(tcp.getSymbolTable().getSymbolFromName(tcp.getName(`app))==null) {
          tcp.messageError(tcp.findOriginTrackingFileName(`app.getOption()),
              tcp.findOriginTrackingLine(`app.getOption()),
              TomMessage.unknownVariableInWhen,
              new Object[]{tcp.getName(`app)});
        }
        // else, it's actually app()
        // else, it's a unknown (ie : java) function
      }
    } 
  }

  private void verifyMatchVariable(ConstraintInstructionList constraintInstructionList) throws VisitFailure {
    %match(constraintInstructionList) {
      concConstraintInstruction(_*,ConstraintInstruction[Constraint=constraint,Action=action],_*) -> {

        // collect variables
        ArrayList<TomTerm> variableList = new ArrayList<TomTerm>();
        TomBase.collectVariable(variableList, `constraint, false); 
        verifyVariableTypeListCoherence(variableList);        

        // TODO: check in the action that a VariableStar is under the right symbol
        verifyListVariableInAction(`action);
        //System.out.println(`action);
      }
    }    
  }

  private void verifyVariableTypeListCoherence(ArrayList<TomTerm> list) {
    // compute multiplicities
    //System.out.println("list = " + list);
    HashMap<TomName,TomTerm> map = new HashMap<TomName,TomTerm>();
    for(TomTerm variable:list) {
      TomName name = variable.getAstName();
      if(map.containsKey(name)) {
        TomTerm var = map.get(name);
        //System.out.println("variable = " + variable);
        //System.out.println("var = " + var);
        TomType type1 = var.getAstType();
        TomType type2 = variable.getAstType();
        // we use getTomType because type1 may be a TypeWithSymbol and type2 a TomType
        if(!TomBase.getTomType(type1).equals(TomBase.getTomType(type2))) {
          messageError(findOriginTrackingFileName(variable.getOption()),
              findOriginTrackingLine(variable.getOption()),
              TomMessage.incoherentVariable,
              new Object[]{name.getString(), TomBase.getTomType(type1), TomBase.getTomType(type2)});
        }
      } else {
        map.put(name,variable);
      }
    }
  }

  private void verifyStrategyVariable(TomVisitList list) {
    %match(list) {
      concTomVisit(_*,VisitTerm(Type(strVisitType,EmptyTargetLanguageType()),_,options),_*) -> {
        String fileName = findOriginTrackingFileName(`options);
        messageError(fileName,
            findOriginTrackingLine(`options),
            TomMessage.unknownVisitedType,
            new Object[]{`(strVisitType)});
      }
    }
  }

  /*
   * forbid to use a X* under the wrong symbol
   * like in f(X*) -> g(X*)
   */
  private void verifyListVariableInAction(Instruction action) {
    //System.out.println("Action = " + action);
    try {
      `TopDown(checkVariableStar(this)).visitLight(`action);
    } catch(VisitFailure e) {
      System.out.println("strategy failed");
    }
  }
 
  %strategy checkVariableStar(tcp:TypeCheckerPlugin) extends Identity() {
    visit BQTerm {
      (BuildAppendList|BuildAppendArray)[AstName=Name(listName),HeadTerm=BQVariableStar[Option=options,AstName=Name(variableName),AstType=TypeWithSymbol[RootSymbolName=Name(rootName)]]] -> {
        if(!`listName.equals(`rootName)) {
          tcp.messageError(tcp.findOriginTrackingFileName(`options),
              tcp.findOriginTrackingLine(`options),
              TomMessage.incoherentVariableStar,
              new Object[]{ (`variableName),(`rootName),(`listName) });
        }
      }
    }
  }

}
