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
 * Cláudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.typer;

import java.util.Map;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ArrayList;
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

  %typeterm NewTyper { implement { NewTyper } }

  /** some output suffixes */
  public static final String TYPED_SUFFIX       = ".tfix.typed";
  public static final String TYPED_TABLE_SUFFIX = ".tfix.typed.table";
  private static Logger logger = Logger.getLogger("tom.engine.typer.NewTyper");

  /** the declared options string */
  public static final String DECLARED_OPTIONS =
    "<options>" +
    "<boolean name='newtyper' altName='nt' description='New version of Typer (working in progress)' value='false'/>" +
    "</options>";

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(NewTyper.DECLARED_OPTIONS);
  }

  /** the kernel typer acting at very low level */
  private static NewKernelTyper newKernelTyper;

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

    if(getOptionBooleanValue("newtyper")) {
 
      Code typedCode = null;
      try {
        newKernelTyper.setSymbolTable(getStreamManager().getSymbolTable()); 

        updateSymbolTable();

        //DEBUG System.out.println("\nSymbolTable before type inference = \n");
        //DEBUG newKernelTyper.getSymbolTable().printMapSymbolName();

        /**
          * Typing variables whose types are unknown with fresh type variables before
          * start inference
          */
        Code typedCodeWithTypeVariables = collectKnownTypes((Code)getWorkingTerm());
        
        /**
          * Start by typing variables with fresh type variables
          * Perform type inference over patterns 
          */
        //DEBUG System.out.println("Code before inference = \n" + typedCodeWithTypeVariables);
        Code inferredTypeForCode = newKernelTyper.inferCode(typedCodeWithTypeVariables);
        //DEBUGSystem.out.println("Code after inference = \n" + inferredTypeForCode);
        
        /** Transform each BackQuoteTerm into its compiled form --> maybe to
          * desugarer phase before perform type inference 
          */
        // Code backQuoteExpandedCode = `TopDownIdStopOnSuccess(typeBQAppl(this)).visitLight(`variableExpandedCode);
        /** Transform a string into an array of characters before type inference, 
          * so we can apply inference just once on all terms 
          */
        //Code stringExpandedCode = `TopDownIdStopOnSuccess(typeString(this)).visitLight(backQuoteExpandedCode);

        // Update type information for codomain in symbol table
        typedCode = `TopDownIdStopOnSuccess(updateCodomain(this)).visitLight(inferredTypeForCode);
        //DEBUG System.out.println("Code after updateCodomain = \n" + typedCode);

        /** 
         * TOMOVE to a post phase: transform each BackQuoteTerm into its compiled form
         */
        //TODO typeString
        
        typedCode = `TopDownIdStopOnSuccess(typeBQAppl(this)).visitLight(typedCode);
        System.out.println("\nCode after type inference = \n" + typedCode);

        //DEBUG System.out.println("\nSymbolTable after type inference = \n");
        //DEBUG newKernelTyper.getSymbolTable().printMapSymbolName();

        //Propagate type information for all variables with same name
        //typedCode = newKernelTyper.propagateVariablesTypes(typedCode);
        setWorkingTerm(typedCode);
        //getStreamManager().setSymbolTable(newKernelTyper.getSymbolTable());

        // verbose
        getLogger().log(Level.INFO, TomMessage.tomTypingPhase.getMessage(),
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));    } catch (Exception e) {
        getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
            new Object[]{getClass().getName(), getStreamManager().getInputFileName(), e.getMessage()} );
        e.printStackTrace();
        return;
      }
      /* Add a suffix for the compilation option --intermediate during typing phase*/
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_SUFFIX, typedCode);
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_TABLE_SUFFIX, symbolTable().toTerm());
      }
    } else {
      // not active plugin
      logger.log(Level.INFO, "The new typer is not in use.");
    }
  }

  /*
   * Type unknown types with fresh type variables 
   */
  private TomTerm collectKnownTypes(TomTerm subject) {
    try {
      return `TopDownIdStopOnSuccess(collectKnownTypes(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }

  /*
   * Type unknown types with fresh type variables 
   */
  private Code collectKnownTypes(Code subject) {
    try {
      return `TopDownIdStopOnSuccess(collectKnownTypes(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }

  %strategy collectKnownTypes(newTyper:NewTyper) extends Identity() {
    visit TomType {
      Type(typeName,javaType@EmptyType()) -> {
        //DEBUG System.out.println("in NewTyper, the type to get javaClassType = " + `type);
        // "getType" gets the java type class refered by tomType
        // e.g. typeName = A and javaClassType = Type("A",TLType(" test.test.types.A "))
        TomType javaClassType = `javaType;
        if (`typeName != "unknown type") {
          javaClassType = newTyper.symbolTable().getType(`typeName);
        }
        if(javaClassType == null || javaClassType == `EmptyType()) {
          // This happens when typeName = unknown type and javaClassType = null 
          javaClassType = newKernelTyper.getFreshTypeVar(); 
          newTyper.symbolTable().putType(`typeName,javaClassType);
          javaClassType = `Type(typeName,javaClassType);
        }
        //DEBUG System.out.println("in NewTyper, type to return = " + `javaClassType);
        return javaClassType;
      }
    }
  }

  /**
   * updateSymbol is called after a first syntax expansion phase
   * this phase updates the symbolTable according to the typeTable
   * this is performed by recursively traversing each symbol
   * - each Type(_,EmptyType()) is replaced by Type(_,TypeVar(i))
   */
  private void updateSymbolTable() {
    SymbolTable symbolTable = getStreamManager().getSymbolTable();
    Iterator<String> it = symbolTable.keySymbolIterator();

    while(it.hasNext()) {
      String tomName = it.next();
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      try {
        tomSymbol = collectKnownTypes(`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
        tomSymbol = `TopDownIdStopOnSuccess(typeBQAppl(this)).visitLight(`tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  /*
   * this post-processing phase replaces untyped (universalType) codomain
   * by their precise type (according to the symbolTable)
   */
  %strategy updateCodomain(newTyper:NewTyper) extends `Identity() {
    visit Declaration {
      GetHeadDecl[] -> {
          throw new TomRuntimeException("updateCodomain");
      }
      // In case of constants, where the domain is the codomain 
      decl@GetHeadDecl[Opname=Name(opName)] -> {
        TomSymbol tomSymbol = newTyper.getSymbolFromName(`opName);
        TomTypeList codomain = TomBase.getSymbolDomain(tomSymbol);
        if(codomain.length()==1) {
          Declaration t = (Declaration)`decl;
          t = t.setCodomain(codomain.getHeadconcTomType());
          return t;
        } else {
          throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
        }
      }

      decl@GetHeadDecl[Variable=BQVariable[AstType=domain]] -> {
        TomSymbol tomSymbol = newTyper.getSymbolFromType(`domain);
        if(tomSymbol != null) {
          TomTypeList codomain = TomBase.getSymbolDomain(tomSymbol);

          if(codomain.length()==1) {
            Declaration t = (Declaration)`decl;
            t = t.setCodomain(codomain.getHeadconcTomType());
            return t;
          } else {
            throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
          }
        }
      }
    } // end match
  }

  /*
   * transform a BQAppl into its compiled form
   */
  %strategy typeBQAppl(newTyper:NewTyper) extends Identity() {
    visit BQTerm {
      BQAppl[Option=optionList,AstName=name@Name(tomName),Args=l] -> {
        TomSymbol tomSymbol = newTyper.getSymbolFromName(`tomName);
        BQTermList args  = `TopDownIdStopOnSuccess(typeBQAppl(newTyper)).visitLight(`l);
        //System.out.println("BackQuoteTerm: " + `tomName);
        //System.out.println("tomSymbol: " + tomSymbol);
        if(TomBase.hasConstant(`optionList)) {
          return `BuildConstant(name);
        } else if(tomSymbol != null) {
          if(TomBase.isListOperator(tomSymbol)) {
            return ASTFactory.buildList(`name,args,newTyper.symbolTable());
          } else if(TomBase.isArrayOperator(tomSymbol)) {
            return ASTFactory.buildArray(`name,args,newTyper.symbolTable());
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
