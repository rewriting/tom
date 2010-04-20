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
 * Claudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.typer;

import java.util.Map;
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
 * The NewTyper plugin.
 * Perform type inference.
 */
public class NewTyper extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  %typeterm NewKernelTyper { implement { NewKernelTyper } }

  /** some output suffixes */
  public static final String TYPED_SUFFIX       = ".tfix.typed";
  public static final String TYPED_TABLE_SUFFIX = ".tfix.typed.table";
  private static Logger logger = Logger.getLogger("tom.engine.typer.NewTyper");

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='newtyper' altName='nt' description='New TyperPlugin (not activated by default)' value='false'/>" +
    "</options>";

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(NewTyper.DECLARED_OPTIONS);
  }

  /** the kernel typer acting at very low level */
  private NewKernelTyper newKernelTyper;

  /** Constructor */
  public NewTyper() {
    super("NewTyper");
    newKernelTyper = new NewKernelTyper();
  }

 /**
   * The run() method performs type inference for variables
   * occurring in patterns and propagate this information for
   * variables occurring in actions (BQVariables).
   */
  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    boolean newtyper = getOptionBooleanValue("newtyper");

    if(newtyper) {
 
      Code typedCode = null;
      try {
        newKernelTyper.setSymbolTable(getStreamManager().getSymbolTable()); 

        updateSymbolTable();

        //DEBUG System.out.println("\nSymbolTable before type inference = \n");

        /**
         * Typing variables whose types are unknown with fresh type variables before
         * start inference
         */
        typedCode = collectKnownTypesFromCode((Code)getWorkingTerm());

        /**
         * Start by typing variables with fresh type variables
         * Perform type inference over patterns 
         */
        typedCode = newKernelTyper.inferCode(typedCode);

        /** 
         * TOMOVE to a post phase: 
         * - transform each BackQuoteTerm into its compiled form
         */
        typedCode =
          `TopDownIdStopOnSuccess(TransformBQAppl(newKernelTyper)).visitLight(typedCode);

        System.out.println("\nCode after type inference = \n" + typedCode);

        setWorkingTerm(typedCode);

        // verbose
        TomMessage.info(logger, null, 0, TomMessage.tomTypingPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));    
      } catch (Exception e) {
        TomMessage.error(logger, getStreamManager().getInputFileName(), 
            0, TomMessage.exceptionMessage, getClass().getName(), 
            getStreamManager().getInputFileName(), e.getMessage());
        e.printStackTrace();
        return;
      }
      /* Add a suffix for the compilation option --intermediate during typing phase*/
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_SUFFIX, typedCode);
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_TABLE_SUFFIX, getSymbolTable().toTerm());
      }
    } else {
      // not active plugin
      logger.log(Level.INFO, "The new typer is not in use.");
    }
  }

  /*
   * Type unknown types with fresh type variables 
   */
  private TomSymbol collectKnownTypesFromTomSymbol(TomSymbol subject) {
    try {
      return `TopDownIdStopOnSuccess(CollectKnownTypes(newKernelTyper)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }

  private Code collectKnownTypesFromCode(Code subject) {
    try {
      return `TopDownIdStopOnSuccess(CollectKnownTypes(newKernelTyper)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }

  /*
   * Type(name, EmptyTargetLanguageType()) -> Type(name, foundType) if name in TypeTable
   * Type(name, EmptyTargetLanguageType()) -> TypeVar(name, Index(i)) if name not in TypeTable
   */
  %strategy CollectKnownTypes(nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      Type(tomType,EmptyTargetLanguageType()) -> {
        TomType newType = null;
        newType = nkt.getSymbolTable().getType(`tomType);
        if (newType == null) {
          // This happens when :
          // * tomType != unknown type AND (newType == null)
          // * tomType == unknown type
          newType = `TypeVar(tomType,nkt.getFreshTlTIndex());
          if (!nkt.getSymbolTable().isUnknownType(`tomType)) {
            // A type typeVar will be add to the typeTable only for the first
            // occurence, because for the other ones, newType will not be 'null'
            nkt.getSymbolTable().putType(`tomType,newType);
          }
          return newType;
        }
        return newType;
      }
    }
  }
  /**
   * updateSymbol is called after a first syntax expansion phase
   * this phase updates the symbolTable according to the typeTable
   * this is performed by recursively traversing each symbol
   * - each Type(name,EmptyTargetLanguageType()) is replaced by TypeVar(name,i)
   */
  private void updateSymbolTable() {
    for(String tomName:newKernelTyper.getSymbolTable().keySymbolIterable()) {      
      try {
        TomSymbol tomSymbol = getSymbolFromName(tomName);
        tomSymbol = collectKnownTypesFromTomSymbol(tomSymbol);
        getSymbolTable().putSymbol(tomName,tomSymbol);
        tomSymbol = `TopDownIdStopOnSuccess(TransformBQAppl(newKernelTyper)).visitLight(`tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      //getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  /**
   * transform a BQAppl into its compiled form
   */
  // pem: why this strategy ? there is a similar code in ExpanderPlugin
  %strategy TransformBQAppl(nkt:NewKernelTyper) extends Identity() {
    visit BQTerm {
      BQAppl[Option=optionList,AstName=name@Name(tomName),Args=l] -> {
        TomSymbol tomSymbol = nkt.getSymbolFromName(`tomName);
        BQTermList args  = `TopDownIdStopOnSuccess(TransformBQAppl(nkt)).visitLight(`l);
        //System.out.println("BackQuoteTerm: " + `tomName);
        //System.out.println("tomSymbol: " + tomSymbol);
        if(TomBase.hasConstant(`optionList)) {
          return `BuildConstant(name);
        } else if(tomSymbol != null) {
          if(TomBase.isListOperator(tomSymbol)) {
            //DEBUG System.out.println("A list operator '" + `tomName + "' : " +
            //DEBUG     `tomSymbol + '\n');
            return ASTFactory.buildList(`name,args,nkt.getSymbolTable());
          } else if(TomBase.isArrayOperator(tomSymbol)) {
            //DEBUG System.out.println("An array operator '" + `tomName + "' : " +
            //DEBUG     `tomSymbol + '\n');
            return ASTFactory.buildArray(`name,args,nkt.getSymbolTable());
          } else if(TomBase.isDefinedSymbol(tomSymbol) || `tomName == "realMake") {
            //DEBUG System.out.println("A defined symbol '" + `tomName + "' : " +
            //DEBUG     `tomSymbol + '\n');
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
