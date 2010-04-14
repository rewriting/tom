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
 *
 **/

package tom.engine.typer;

import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;
import tom.engine.adt.code.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

import tom.library.sl.*;

/**
 * The Typer plugin.
 * Perform syntax expansion and more.
 */
public class TyperPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  %typeterm TyperPlugin { implement { TyperPlugin } }

  /** some output suffixes */
  public static final String TYPED_SUFFIX       = ".tfix.typed";
  public static final String TYPED_TABLE_SUFFIX = ".tfix.typed.table";
  private static Logger logger = Logger.getLogger("tom.engine.typer.TyperPlugin");

  /** the declared options string */
  public static final String DECLARED_OPTIONS =
    "<options>" +
    "<boolean name='type' altName='' description='TyperPlugin (activated by default)' value='true'/>" +
    "</options>";

  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TyperPlugin.DECLARED_OPTIONS);
  }

  /** the kernel typer acting at very low level */
  private KernelTyper kernelTyper;

  /** Constructor*/
  public TyperPlugin() {
    super("TyperPlugin");
    kernelTyper = new KernelTyper();
  }

  /**
   * The run() method performs expansion for tom syntax, variables,...
   */
  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    boolean newtyper = getOptionBooleanValue("newtyper");

    if(newtyper==false) {
      Code typedCode = null;
      try {
        kernelTyper.setSymbolTable(getStreamManager().getSymbolTable());

        updateSymbolTable();

<<<<<<< ours
        System.out.println("In code = " + getWorkingTerm());
        Code syntaxExpandedCode = expandType((Code)getWorkingTerm());
        Code variableExpandedCode = (Code) kernelTyper.typeVariable(`EmptyType(), syntaxExpandedCode);
=======
        Code variableExpandedCode = (Code) kernelTyper.typeVariable(`EmptyType(), (Code)getWorkingTerm());
>>>>>>> theirs

        typedCode = kernelTyper.propagateVariablesTypes(variableExpandedCode);

        /* transform each BackQuoteTerm into its compiled form */
<<<<<<< ours
        typedCode = `TopDownIdStopOnSuccess(typeBQAppl(this)).visitLight(typedCode);
        System.out.println("\nCode after type inference = \n" + typedCode);
=======
        typedCode = `TopDownIdStopOnSuccess(TransformBQAppl(this)).visitLight(typedCode);
>>>>>>> theirs

        setWorkingTerm(typedCode);      
        // verbose
        getLogger().log(Level.INFO, TomMessage.tomTypingPhase.getMessage(),
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
            new Object[]{getClass().getName(), getStreamManager().getInputFileName(), e.getMessage()} );
        e.printStackTrace();
        return;
      }
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_SUFFIX, typedCode);
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_TABLE_SUFFIX, getSymbolTable().toTerm());
      }
    } else {
      // not active plugin
      logger.log(Level.INFO, "The default typer is not in use.");
    }
<<<<<<< ours

  }

  /*
   * Replace a TomTypeAlone by its expanded form (TomType)
   */
  private Code expandType(Code subject) {
    try {
      return `TopDownIdStopOnSuccess(expandType(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeType: failure on " + subject);
    }
  }

  /*
   * Replace a TomTypeAlone by its expanded form (TomType)
   */
  private TomTerm expandType(TomTerm subject) {
    try {
      return `TopDownIdStopOnSuccess(expandType(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeType: failure on " + subject);
    }
  }

  %strategy expandType(typer:TyperPlugin) extends Identity() {
    visit TomType {
      subject@Type(tomType,EmptyTargetLanguageType()) -> {
        if (!typer.getSymbolTable().isUnknownType(`tomType)) {
          if (typer.getSymbolTable().getType(`tomType) == null) {
            System.out.println("type known = " + `tomType + " with tltype null.");
          }
        }
        TomType type = typer.getSymbolTable().getType(`tomType);
        if(type != null) {
          return type;
        } else {
          return `subject; // useful for SymbolTable.TYPE_UNKNOWN
        }
      }
    }
=======
>>>>>>> theirs
  }

  /*
   * updateSymbol is called after a first syntax expansion phase
   * this phase updates the symbolTable according to the typeTable
   * this is performed by recursively traversing each symbol
   * - backquote are typed
   * - replace a TomType(_,EmptyTargetLanguageType()) by its expanded form (TomType)
   * - default IsFsymDecl and MakeDecl are added
   */
  public void updateSymbolTable() {
    SymbolTable symbolTable = getStreamManager().getSymbolTable();

    for(String tomName:symbolTable.keySymbolIterable()) {
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      try {
        tomSymbol = ((TomTerm) kernelTyper.typeVariable(`EmptyType(),`TomSymbolToTomTerm(tomSymbol))).getAstSymbol();
        tomSymbol = `TopDownIdStopOnSuccess(TransformBQAppl(this)).visitLight(tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  /*
   * transform a BQAppl into its compiled form (BuildConstant, BuildList, FunctionCall, BuildTerm)
   * can only be done after typing because BQAppl are treated by the typing algorithm
   */
  %strategy TransformBQAppl(typer:TyperPlugin) extends Identity() {
    visit BQTerm {
      BQAppl[Option=optionList,AstName=name@Name(tomName),Args=l] -> {
        TomSymbol tomSymbol = typer.getSymbolFromName(`tomName);
        BQTermList args  = `TopDownIdStopOnSuccess(TransformBQAppl(typer)).visitLight(`l);
        //System.out.println("BackQuoteTerm: " + `tomName);
        //System.out.println("tomSymbol: " + tomSymbol);
        if(TomBase.hasConstant(`optionList)) {
          return `BuildConstant(name);
        } else if(tomSymbol != null) {
          if(TomBase.isListOperator(tomSymbol)) {
            return ASTFactory.buildList(`name,args,typer.getSymbolTable());
          } else if(TomBase.isArrayOperator(tomSymbol)) {
            return ASTFactory.buildArray(`name,args,typer.getSymbolTable());
          } else if(TomBase.isDefinedSymbol(tomSymbol)) {
            return `FunctionCall(name,TomBase.getSymbolCodomain(tomSymbol),args);
          } else {
            String moduleName = TomBase.getModuleName(`optionList);
            if(moduleName==null) {
              moduleName = TomBase.DEFAULT_MODULE_NAME;
            }
            return `BuildTerm(name,args,moduleName);
          }
        } else {
          return `FunctionCall(name,EmptyType(),args);
        }
      }
    }
  }

}
