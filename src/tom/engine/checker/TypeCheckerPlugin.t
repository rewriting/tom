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
import tom.platform.OptionParser;
import tom.platform.PlatformLogRecord;

import aterm.ATerm;
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
  %include { ../../library/mapping/java/sl.tom }

  %typeterm TypeCheckerPlugin { implement { TypeCheckerPlugin } }

  // Different kind of structures
  protected final static int TERM_APPL               = 0;
  protected final static int UNAMED_APPL             = 1;
  protected final static int APPL_DISJUNCTION        = 2;
  protected final static int RECORD_APPL             = 3;
  protected final static int RECORD_APPL_DISJUNCTION = 4;
  protected final static int XML_APPL                = 5;
  protected final static int VARIABLE_STAR           = 6;
  protected final static int UNAMED_VARIABLE_STAR    = 7;
  protected final static int UNAMED_VARIABLE         = 8;
  protected final static int VARIABLE                = 9;

  protected boolean strictType = false;
  protected Option currentTomStructureOrgTrack;

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='noTypeCheck' altName='' description='Do not perform type checking' value='false'/>" +
    "</options>";

  /** Constructor */
  public TypeCheckerPlugin() {
    super("TypeCheckerPlugin");
  }

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TypeCheckerPlugin.DECLARED_OPTIONS);
  }

  protected void reinit() {
    currentTomStructureOrgTrack = null;
  }

  public int getClass(TomTerm term) {
    %match(term) {
      TermAppl[NameList=(Name(""))] -> { return UNAMED_APPL;}
      TermAppl[NameList=(Name(_))] -> { return TERM_APPL;}
      TermAppl[NameList=(Name(_), _*)] -> { return APPL_DISJUNCTION;}
      RecordAppl[NameList=(Name(_))] -> { return RECORD_APPL;}
      RecordAppl[NameList=(Name(_), _*)] -> { return RECORD_APPL_DISJUNCTION;}
      XMLAppl[] -> { return XML_APPL;}
      UnamedVariable[] -> { return UNAMED_VARIABLE;}
      VariableStar[] -> { return VARIABLE_STAR;}
      Variable[] -> { return VARIABLE;}
      UnamedVariableStar[] -> { return UNAMED_VARIABLE_STAR;}
    }
    throw new TomRuntimeException("Invalid Term");
  }

  public String getName(TomTerm term) {
    String dijunctionName = "";
    %match(term) {
      TermAppl[NameList=(Name(name))] -> { return `name;}
      TermAppl[NameList=nameList] -> {
        String head;
        dijunctionName = `nameList.getHeadconcTomName().getString();
        while(!`nameList.isEmptyconcTomName()) {
          head = `nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTailconcTomName();
        }
        return dijunctionName;
      }
      RecordAppl[NameList=(Name(name))] -> { return `name;}
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
      XMLAppl[NameList=(Name(name), _*)] ->{ return `name;}
      XMLAppl[NameList=nameList] -> {
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
      UnamedVariable[] -> { return "_";}
      UnamedVariableStar[] -> { return "_*";}
      AntiTerm(t) -> { return getName(`t); }
    }
    throw new TomRuntimeException("Invalid Term:" + term);
  }

  /**
   * Shared Functions 
   */
  protected String extractType(TomSymbol symbol) {
    TomType type = TomBase.getSymbolCodomain(symbol);
    return TomBase.getTomType(type);
  }

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

  protected void ensureOriginTrackingLine(int line) {
    if(line < 0) {
      TomMessage.error(getLogger(),
          getStreamManager().getInputFileName(), 0,
          TomMessage.findOTL);
      //System.out.println("findOriginTrackingLine: not found ");
    }
  }

  public void run(Map informationTracker) {
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
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
        TomMessage.info(getLogger(),null,0,TomMessage.tomTypeCheckingPhase,
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
      TomMessage.info(getLogger(),null,0,TomMessage.typeCheckerInactivated);
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
          TomMessage.error(tcp.getLogger(),
              tcp.findOriginTrackingFileName(`app.getOption()),
              tcp.findOriginTrackingLine(`app.getOption()),
              TomMessage.unknownVariableInWhen,
              tcp.getName(`app));
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
        // we use getTomType because type1 may be a TypeWithSymbol and type2 a TomType
        if(!TomBase.getTomType(type1).equals(TomBase.getTomType(type2))) {
          TomMessage.error(getLogger(),
              findOriginTrackingFileName(variable.getOption()),
              findOriginTrackingLine(variable.getOption()),
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
      (BuildAppendList|BuildAppendArray)[AstName=Name(listName),HeadTerm=BQVariableStar[Option=options,AstName=Name(variableName),AstType=TypeWithSymbol[RootSymbolName=Name(rootName)]]] -> {
        if(!`listName.equals(`rootName)) {
          TomMessage.error(tcp.getLogger(),
              tcp.findOriginTrackingFileName(`options),
              tcp.findOriginTrackingLine(`options),
              TomMessage.incoherentVariableStar,
              `variableName, `rootName, `listName);
        }
      }
    }
  }

  private void verifyStrategyVariable(TomVisitList list) {
    %match(list) {
      concTomVisit(_*,VisitTerm(Type(strVisitType,EmptyTargetLanguageType()),_,options),_*) -> {
        TomMessage.error(getLogger(),
            findOriginTrackingFileName(`options),
            findOriginTrackingLine(`options),
            TomMessage.unknownVisitedType,
            `strVisitType);
      }
    }
  }

}
