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
  %typeterm NewTyper { implement { NewTyper } }

  private int freshTypeVarCounter = 0;

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

  protected int getFreshTlTIndex() {
    return freshTypeVarCounter++;
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
    boolean lazyType = getOptionBooleanValue("lazyType");

    if(newtyper) {
 
      Code typedCode = null;
      try {
        newKernelTyper.setSymbolTable(getStreamManager().getSymbolTable()); 
        newKernelTyper.setCurrentInputFileName(getStreamManager().getInputFileName()); 
        if(lazyType) {
          newKernelTyper.setLazyType();
        }

        updateSymbolTable();

        /**
         * Typing variables whose types are unknown with fresh type variables before
         * start inference
         */
        //typedCode = collectKnownTypesFromCode((Code)getWorkingTerm());

        //DEBUG System.out.println("\nCode before type inference = \n" + typedCode);

        /**
         * Start by typing variables with fresh type variables
         * Perform type inference over patterns 
         */
        //typedCode = newKernelTyper.inferCode((Code)getWorkingTerm());
        typedCode =
          newKernelTyper.inferAllTypes((Code)getWorkingTerm(),newKernelTyper.getUnknownFreshTypeVar());
        //DEBUG System.out.println("\nCode after type inference before desugarString = \n" + typedCode);

        /**
         * Replace all remains of type variables by
         * Type(tomType,EmptyTargetLanguageType()) in Code and in SymbolTable
         */
        typedCode = replaceInCode(typedCode);
        replaceInSymbolTable();

        /** 
         * TOMOVE to a post phase: 
         * - transform each BackQuoteTerm into its compiled form
         * - replace 'abc' by concString('a','b','c')
         */
        typedCode = `TopDownIdStopOnSuccess(desugarString(this)).visitLight(typedCode);
        typedCode =
          `TopDownIdStopOnSuccess(TransformBQAppl(newKernelTyper)).visitLight(typedCode);

        //DEBUG System.out.println("\nCode after type inference = \n" + typedCode);

        setWorkingTerm(typedCode);

        // verbose
        TomMessage.info(logger, null, 0, TomMessage.tomTypingPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));    
      } catch (Exception e) {
        TomMessage.error(logger, newKernelTyper.getCurrentInputFileName(), 
            0, TomMessage.exceptionMessage, getClass().getName(), 
            newKernelTyper.getCurrentInputFileName(), e.getMessage());
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
      TomMessage.info(logger, null, 0, TomMessage.newTyperNotUsed);
    }
  }


  /*
   * Type unknown types with fresh type variables 
   */
  private TomSymbol collectKnownTypesFromTomSymbol(TomSymbol subject) {
    try {
      return `TopDownIdStopOnSuccess(CollectKnownTypes(this,newKernelTyper)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }
/*
  private Code collectKnownTypesFromCode(Code subject) {
    try {
      return `TopDownIdStopOnSuccess(CollectKnownTypes(this,newKernelTyper)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }
  */

  /*
   * Type(name, EmptyTargetLanguageType()) -> Type(name, foundType) if name in TypeTable
   * Type(name, EmptyTargetLanguageType()) -> TypeVar(name, Index(i)) if name not in TypeTable
   */
  %strategy CollectKnownTypes(typer:NewTyper,nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      Type(tomType,EmptyTargetLanguageType()) -> {
        TomType newType = null;
        newType = nkt.getSymbolTable().getType(`tomType);
        if (newType == null) {
          // This happens when :
          // * tomType != unknown type AND (newType == null)
          // * tomType == unknown type
          newType = `TypeVar(tomType,typer.getFreshTlTIndex());
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
        TomSymbol tSymbol = getSymbolFromName(tomName);
        tSymbol = collectKnownTypesFromTomSymbol(tSymbol);
        tSymbol =
          `TopDownIdStopOnSuccess(TransformBQAppl(newKernelTyper)).visitLight(`tSymbol);
        getSymbolTable().putSymbol(tomName,tSymbol);
        newKernelTyper.setLimTVarSymbolTable(freshTypeVarCounter);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("should not be there");
      }
      //System.out.println("symbol = " + tSymbol);
      //getStreamManager().getSymbolTable().putSymbol(tomName,tSymbol);
    }
  }

  /**
   * transform a BQAppl into its compiled form
   */
  // pem: why this strategy ? there is a similar code in ExpanderPlugin
  %strategy TransformBQAppl(nkt:NewKernelTyper) extends Identity() {
    visit BQTerm {
      BQAppl[Options=optionList,AstName=name@Name(tomName),Args=l] -> {
        TomSymbol tSymbol = nkt.getSymbolFromName(`tomName);
        BQTermList args  = `TopDownIdStopOnSuccess(TransformBQAppl(nkt)).visitLight(`l);
        //System.out.println("BackQuoteTerm: " + `tomName);
        //System.out.println("tSymbol: " + tSymbol);
        if(TomBase.hasConstant(`optionList)) {
          return `BuildConstant(name);
        } else if(tSymbol != null) {
          if(TomBase.isListOperator(tSymbol)) {
            //DEBUG System.out.println("A list operator '" + `tomName + "' : " +
            //DEBUG     `tSymbol + '\n');
            return ASTFactory.buildList(`name,args,nkt.getSymbolTable());
          } else if(TomBase.isArrayOperator(tSymbol)) {
            //DEBUG System.out.println("An array operator '" + `tomName + "' : " +
            //DEBUG     `tSymbol + '\n');
            return ASTFactory.buildArray(`name,args,nkt.getSymbolTable());
          } else if(TomBase.isDefinedSymbol(tSymbol)) {
            //DEBUG System.out.println("A defined symbol '" + `tomName + "' : " +
            //DEBUG     `tSymbol + '\n');
            return `FunctionCall(name,TomBase.getSymbolCodomain(tSymbol),args);
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

  private Code replaceInCode(Code code) {
    Code replacedCode = code;
    try {
      replacedCode =
        `RepeatId(TopDown(replaceFreshTypeVar())).visitLight(code);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInCode: failure on " +
          replacedCode);
    }
    return replacedCode;
  }

  private void replaceInSymbolTable() {
    for(String tomName:newKernelTyper.getSymbolTable().keySymbolIterable()) {
      //DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
      TomSymbol tSymbol = newKernelTyper.getSymbolFromName(tomName);
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol before strategy: "
      //DEBUG     + tSymbol);
      try {
        tSymbol = `RepeatId(TopDown(replaceFreshTypeVar())).visitLight(tSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " +
            tSymbol);
      }
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol after strategy: "
      //DEBUG     + tSymbol);
      newKernelTyper.getSymbolTable().putSymbol(tomName,tSymbol);
    }
  }

  %strategy replaceFreshTypeVar() extends Identity() {
    visit TomType {
      TypeVar(tomType,_) -> {
        return `Type(tomType,EmptyTargetLanguageType());
      }    
    }
  }

  /*
   * replace conc('abc') by conc('a','b','c')
   * cannot be performed in the desugarer since anonymous symbols have to be already typed and expanded
   * indeed (_,x@'bc') is expanded into (_,x@('b','c')): the nested anonymous list cannot be resolved  
   */
  %strategy desugarString(typer:NewTyper) extends Identity() {
    visit TomTerm {
      appl@RecordAppl[NameList=concTomName(Name(tomName),_*),Slots=args] -> {
        TomSymbol tSymbol = typer.getSymbolFromName(`tomName);
        //System.out.println("appl = " + subject);
        if(tSymbol != null) {
          if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) {
            //System.out.println("appl = " + subject);
            SlotList newArgs = typer.typeChar(tSymbol,`args);
            if(newArgs!=`args) {
              return `appl.setSlots(newArgs);
            }
          }
        }
      }
    } // end match
  }

  /*
   * detect ill-formed char: 'abc'
   * and type it into a list of char: 'a','b','c'
   */
  private SlotList typeChar(TomSymbol tSymbol,SlotList args) {
    if(args.isEmptyconcSlot()) {
      return args;
    } else {
      Slot head = args.getHeadconcSlot();
      SlotList tail = typeChar(tSymbol,args.getTailconcSlot());
      %match(head) {
        PairSlotAppl(slotName,RecordAppl[Options=optionList,NameList=concTomName(Name(tomName)),Slots=concSlot(),Constraints=constraintList]) -> {
          /*
           * ensure that the argument contains at least 1 character and 2 single quotes
           */
          TomSymbol stringSymbol = getSymbolFromName(`tomName);
          TomType termType = stringSymbol.getTypesToType().getCodomain();
          String type = termType.getTomType();
          if(getSymbolTable().isCharType(type) && `tomName.length()>3) {
            if(`tomName.charAt(0)=='\'' && `tomName.charAt(`tomName.length()-1)=='\'') {
              SlotList newArgs = `concSlot();
              String substring = `tomName.substring(1,`tomName.length()-1);
              //System.out.println("bingo -> " + substring);
              substring = substring.replace("\\'","'"); // replace backslash-quote by quote
              substring = substring.replace("\\\\","\\"); // replace backslash-backslash by backslash
              //System.out.println("after encoding -> " + substring);

              for(int i=substring.length()-1 ; i>=0 ;  i--) {
                char c = substring.charAt(i);
                String newName = "'" + c + "'";
                TomSymbol newSymbol = stringSymbol.setAstName(`Name(newName));
                getSymbolTable().putSymbol(newName,newSymbol);

                Slot newHead = `PairSlotAppl(slotName,RecordAppl(optionList,concTomName(Name(newName)),concSlot(),concConstraint()));
                newArgs = `concSlot(newHead,newArgs*);
                //System.out.println("newHead = " + newHead);
                //System.out.println("newSymb = " + getSymbolFromName(newName));
              }
              ConstraintList newConstraintList = `concConstraint();
              %match(constraintList) {
                concConstraint(AliasTo(var@Variable[AstType=vartype])) -> {
                  if(getSymbolTable().isCharType(TomBase.getTomType(`vartype))) {
                    newConstraintList = `concConstraint(AliasTo(var.setAstType(getSymbolTable().getStringType())));
                  }
                }
              }

              TomTerm newSublist = `RecordAppl(concOption(),concTomName(tSymbol.getAstName()),newArgs,newConstraintList);
              Slot newSlot = `PairSlotAppl(slotName,newSublist);
              return `concSlot(newSlot,tail*);
            } else {
              throw new TomRuntimeException("typeChar: strange char: " + `tomName);
            }
          }
        }
      }
      return `concSlot(head,tail*);
    }
  }
}
