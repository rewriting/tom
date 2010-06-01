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
        //System.out.println("\nCode before type inference = \n" + getWorkingTerm());

        Code variableExpandedCode = (Code) kernelTyper.typeVariable(`EmptyType(), (Code)getWorkingTerm());

        typedCode = kernelTyper.propagateVariablesTypes(variableExpandedCode);
        //DEBUG System.out.println("\nCode after type inference before desugarString = \n" + typedCode);

        // replace 'abc' by concString('a','b','c')
        typedCode = `TopDownIdStopOnSuccess(desugarString(this)).visitLight(typedCode);

        /* transform each BackQuoteTerm into its compiled form */
        typedCode = `TopDownIdStopOnSuccess(TransformBQAppl(this)).visitLight(typedCode);
        //System.out.println("\nCode after type inference = \n" + typedCode);

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
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_SUFFIX, typedCode);
        Tools.generateOutput(getStreamManager().getOutputFileName()
            + TYPED_TABLE_SUFFIX, getSymbolTable().toTerm());
      }
    } else {
      // not active plugin
      TomMessage.info(logger,null,0,TomMessage.typerNotUsed);
    }
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
        `TopDownIdStopOnSuccess(FindEmptyTLType(this)).visitLight(tomSymbol);
        tomSymbol = ((TomTerm) kernelTyper.typeVariable(`EmptyType(),`TomSymbolToTomTerm(tomSymbol))).getAstSymbol();
        tomSymbol = `TopDownIdStopOnSuccess(TransformBQAppl(this)).visitLight(tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  %strategy FindEmptyTLType(typer:TyperPlugin) extends Identity() {
    visit TomType {
      Type(tomType,EmptyTargetLanguageType()) -> {
        TomType newType = null;
        newType = typer.getSymbolTable().getType(`tomType);
        %match(newType) {
         Type(tomType,EmptyTargetLanguageType()) -> {
          System.out.println("Found an EmptyTargetLanguageType!!");
         }
        }
      }
    }
  }

  /*
   * transform a BQAppl into its compiled form (BuildConstant, BuildList, FunctionCall, BuildTerm)
   * can only be done after typing because BQAppl are treated by the typing algorithm
   */
  %strategy TransformBQAppl(typer:TyperPlugin) extends Identity() {
    visit BQTerm {
      BQAppl[Options=optionList,AstName=name@Name(tomName),Args=l] -> {
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

    /*
     * replace conc('abc') by conc('a','b','c')
     * cannot be performed in the desugarer since anonymous symbols have to be already typed and expanded
     * indeed (_,x@'bc') is expanded into (_,x@('b','c')): the nested anonymous list cannot be resolved  
     */
    %strategy desugarString(typer:TyperPlugin) extends Identity() {
      visit TomTerm {
        appl@RecordAppl[NameList=(Name(tomName),_*),Slots=args] -> {
          TomSymbol tomSymbol = typer.getSymbolFromName(`tomName);
          //System.out.println("appl = " + subject);
          if(tomSymbol != null) {
            if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
              //System.out.println("appl = " + subject);
              SlotList newArgs = typer.typeChar(tomSymbol,`args);
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
    private SlotList typeChar(TomSymbol tomSymbol,SlotList args) {
      if(args.isEmptyconcSlot()) {
        return args;
      } else {
        Slot head = args.getHeadconcSlot();
        SlotList tail = typeChar(tomSymbol,args.getTailconcSlot());
        %match(head) {
          PairSlotAppl(slotName,RecordAppl[Options=optionList,NameList=(Name(tomName)),Slots=concSlot(),Constraints=constraintList]) -> {
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

                TomTerm newSublist = `RecordAppl(concOption(),concTomName(tomSymbol.getAstName()),newArgs,newConstraintList);
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
