/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * Jean-Christophe Bach e-mail: Jeanchristophe.Bach@loria.fr
 * Julien Guyon
 *
 **/

package tom.engine.checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.exception.TomRuntimeException;
import tom.platform.PlatformLogRecord;

import tom.library.sl.*;
import tom.platform.adt.platformoption.types.PlatformOptionList;

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

/**
 * The TomTypeChecker plugin.
 */
public class TypeCheckerPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../platform/adt/platformoption/PlatformOption.tom }
  %include { ../../library/mapping/java/sl.tom }

  %typeterm TypeCheckerPlugin { implement { TypeCheckerPlugin } }

  // Different kind of structures
  protected final static int TERM_APPL               = 0;
  protected final static int UNAMED_APPL             = 1;
  protected final static int APPL_DISJUNCTION        = 2;
  protected final static int RECORD_APPL             = 3;
  protected final static int RECORD_APPL_DISJUNCTION = 4;
  protected final static int VARIABLE_STAR           = 6;
  protected final static int UNAMED_VARIABLE_STAR    = 7;
  protected final static int UNAMED_VARIABLE         = 8;
  protected final static int VARIABLE                = 9;

  protected Option currentTomStructureOrgTrack;

  /** the declared options string */
  public static final PlatformOptionList PLATFORM_OPTIONS =
    `concPlatformOption(
        PluginOption("noTypeCheck", "", "Do not perform type checking", BooleanValue(False()), "")
        );

  /** Constructor */
  public TypeCheckerPlugin() {
    super("TypeCheckerPlugin");
  }

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS; 
  }

  protected void reinit() {
    currentTomStructureOrgTrack = null;
  }

  public int getClass(TomTerm term) {
    %match(term) {
      /* TermAppl does not exists after Desugarer phase */
      RecordAppl[NameList=concTomName(Name(_))] -> { return RECORD_APPL;}
      RecordAppl[NameList=concTomName(Name(_), _*)] -> { return RECORD_APPL_DISJUNCTION;}
      VariableStar[] -> { return VARIABLE_STAR;}
      Variable[] -> { return VARIABLE;}
    }
    throw new TomRuntimeException("Invalid Term");
  }

  public String getName(TomTerm term) {
    String dijunctionName = "";
    %match(term) {
      /* TermAppl does not exists after Desugarer phase */
      RecordAppl[NameList=concTomName(Name(name))] -> { return `name;}
      RecordAppl[NameList=nameList] -> {
        String head;
        dijunctionName = `nameList.getHeadconcTomName().getString();
        while(!`nameList.isEmptyconcTomName()) {
          head = `nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTailconcTomName();
        }
        return dijunctionName;
      }
      Variable[AstName=Name(name)] -> { return `name;}
      VariableStar[AstName=Name(name)] -> { return `name+"*";}
      AntiTerm(t) -> { return getName(`t); }
    }
    throw new TomRuntimeException("Invalid Term:" + term);
  }

  /**
   * Shared Functions 
   */
  protected String findOriginTrackingFileName(OptionList optionList) {
    %match(optionList) {
      concOption(_*,OriginTracking[FileName=fileName],_*) -> { return `fileName; }
    }
    return "unknown filename";
  }

  protected int findOriginTrackingLine(OptionList optionList) {
    %match(optionList) {
      concOption(_*,OriginTracking[Line=line],_*) -> { return `line; }
    }
    return -1;
  }

  public void run(Map informationTracker) {
    if(isActivated()) {
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        try {
          Code subject = (Code) getWorkingTerm();
          `TopDownCollect(checkTypeInference(this)).visitLight(subject);
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("strategy failed");
        }
        // verbose
        TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
            TomMessage.tomTypeCheckingPhase,
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch (Exception e) {
        TomMessage.error(getLogger(), 
            getStreamManager().getInputFileName(), 0,
            TomMessage.exceptionMessage,
            getClass().getName(), 
            getStreamManager().getInputFileName(),
            e.getMessage() );
        e.printStackTrace();
      }
    } else {
      // type checker desactivated
      TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          TomMessage.typeCheckerInactivated);
    }
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noTypeCheck");
  }

  /**
   * Main type checking entry point:
   * We check all Match
   */

  %strategy checkTypeInference(tcp:TypeCheckerPlugin) extends Identity() {
    visit Instruction {
      Match(constraintInstructionList, oplist) -> {  
        tcp.currentTomStructureOrgTrack = TomBase.findOriginTracking(`oplist);
        tcp.verifyMatchVariable(`constraintInstructionList);
        throw new tom.library.sl.VisitFailure();/* to stop the top-down */
      }
    }

    visit Declaration {
      Strategy(_,_,visitList,_,orgTrack) -> {
        tcp.currentTomStructureOrgTrack = `orgTrack;
        tcp.verifyStrategyVariable(`visitList);
        throw new tom.library.sl.VisitFailure();/* to stop the top-down */
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
      }
    }    
  }

  /**
   * check that variables that appear more than once have coherent types
   * @param list the list of variables
   */
  private void verifyVariableTypeListCoherence(ArrayList<TomTerm> list) {
    // compute multiplicities
    HashMap<TomName,TomTerm> map = new HashMap<TomName,TomTerm>();
    for(TomTerm variable:list) {
      TomName name = variable.getAstName();
      if(map.containsKey(name)) {
        TomTerm var = map.get(name);
        TomType type1 = var.getAstType();
        TomType type2 = variable.getAstType();
        // we use getTomType because type1 may be a
        // Type(concTypeOption(WithSymbol(...)) and type2 a Type
        if(!TomBase.getTomType(type1).equals(TomBase.getTomType(type2))) {
          TomMessage.error(getLogger(),
              findOriginTrackingFileName(variable.getOptions()),
              findOriginTrackingLine(variable.getOptions()),
              TomMessage.incoherentVariable,
              name.getString(),
              TomBase.getTomType(type1),
              TomBase.getTomType(type2));
        }
      } else {
        map.put(name,variable);
      }
    }
  }

  /*
   * forbid to use a X* under the wrong symbol
   * like in f(X*) -> g(X*)
   */
  private void verifyListVariableInAction(Instruction action) {
    try {
      `TopDown(checkVariableStar(this)).visitLight(`action);
    } catch(VisitFailure e) {
      System.out.println("strategy failed");
    }
  }

  %strategy checkVariableStar(tcp:TypeCheckerPlugin) extends Identity() {
    visit BQTerm {
      (BuildAppendList|BuildAppendArray)[AstName=Name(listName),HeadTerm=BQVariableStar[Options=optionList,AstName=Name(variableName),AstType=Type[TypeOptions=tOptions]]]
        && concTypeOption(_*,WithSymbol[RootSymbolName=Name(rootName)],_*) << tOptions -> {
        /* This error is equivalent to the "invalidVariableStarArgument"
         * signaled by the SyntaxCheckerPlugin 
         */
        if(!`listName.equals(`rootName)) {
          TomMessage.error(tcp.getLogger(),
              tcp.findOriginTrackingFileName(`optionList),
              tcp.findOriginTrackingLine(`optionList),
              TomMessage.incoherentVariableStar,
              `variableName, `rootName, `listName);
        }
      }
    }
  }

  private void verifyStrategyVariable(TomVisitList list) {
    /* %strategy: error if there is no visit */
    if(`list.isEmptyconcTomVisit()) {
      TomMessage.error(getLogger(),null,0,TomMessage.emptyStrategy);
    }
        /* This error is alreay signaled by the SyntaxCheckerPlugin with error
         * "unknownType
         */
    %match(list) {
      concTomVisit(_*,VisitTerm(Type[TomType=strVisitType,TlType=EmptyTargetLanguageType()],_,optionList),_*) -> {
        TomMessage.error(getLogger(),
            findOriginTrackingFileName(`optionList),
            findOriginTrackingLine(`optionList),
            TomMessage.unknownVisitedType,
            `strVisitType);
      }
    }
  }

}
