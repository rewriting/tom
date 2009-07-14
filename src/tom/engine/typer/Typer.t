/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ArrayList;

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


/* TODO: remove */
import tom.library.utils.Viewer;

/**
 * The Typer plugin.
 * Perform syntax expansion and more.
 */
public class Typer extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  %typeterm Typer { implement { Typer } }


  /** some output suffixes */
  public static final String TYPED_SUFFIX       = ".tfix.typed";
  public static final String TYPED_TABLE_SUFFIX = ".tfix.typed.table";

  /** the declared options string */
  public static final String DECLARED_OPTIONS =
    "<options>" +
    "<boolean name='type' altName='' description='Typer (activated by default)' value='true'/>" +
    "</options>";

  /** the kernel typer acting at very low level */
  private KernelTyper kernelTyper;

  /** Constructor*/
  public Typer() {
    super("Typer");
    kernelTyper = new KernelTyper();
  }

  /**
   * The run() method performs expansion for tom syntax, variables,...
   */
  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    TomTerm typedTerm = null;
    try {
       kernelTyper.setSymbolTable(getStreamManager().getSymbolTable());
      // replaces (infers) unknown types of pattern variables into Types
      // and propagates them in the action part
      TomTerm variableExpandedTerm = (TomTerm) kernelTyper.typeVariable(`EmptyType(), (TomTerm) getWorkingTerm());

       /* transform each BackQuoteTerm into its compiled form */
       // BackquoteAppl --> BuildTerm || FunctionCall || BuildList || BuildArray 
       TomTerm backQuoteExpandedTerm = `TopDownIdStopOnSuccess(typeBackQuoteAppl(this)).visitLight(variableExpandedTerm);
       //Viewer.toTree(backQuoteExpandedTerm);

       // replace "abc" by conc('a','b','c')
       TomTerm stringExpandedTerm = `TopDownIdStopOnSuccess(typeString(this)).visitLight(backQuoteExpandedTerm);


      //Viewer.toTree(stringExpandedTerm);
      typedTerm = `TopDownIdStopOnSuccess(updateCodomain(this)).visitLight(stringExpandedTerm);
      //Viewer.toTree(typedTerm);

      // TODO: seems useless 
      //typedTerm = kernelTyper.propagateVariablesTypes(typedTerm);

      setWorkingTerm(typedTerm);      

      // verbose
      getLogger().log(Level.INFO, TomMessage.tomTypingPhase.getMessage(),
          new Integer((int)(System.currentTimeMillis()-startChrono)));
    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getClass().getName(), getStreamManager().getInputFileName(), e.getMessage()} );
      e.printStackTrace();
      return;
    }
    if(intermediate) {
      Tools.generateOutput(getStreamManager().getOutputFileName()
          + TYPED_SUFFIX, typedTerm);
      Tools.generateOutput(getStreamManager().getOutputFileName()
          + TYPED_TABLE_SUFFIX, symbolTable().toTerm());
    }
  }


  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(Typer.DECLARED_OPTIONS);
  }

    /*
     * this post-processing phase replaces untyped (universalType) codomain
     * by their precise type (according to the symbolTable)
     */
    %strategy updateCodomain(typer:Typer) extends `Identity() {
      visit Declaration {
        decl@GetHeadDecl[Opname=Name(opName)] -> {
          System.out.println("LA1");
          TomSymbol tomSymbol = typer.getSymbolFromName(`opName);
          TomTypeList codomain = TomBase.getSymbolDomain(tomSymbol);
          if(codomain.length()==1) {
            Declaration t = (Declaration)`decl;
            t = t.setCodomain(codomain.getHeadconcTomType());
            return t;
          } else {
            throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
          }
        }

        decl@GetHeadDecl[Variable=Variable[AstType=domain]] -> {
          System.out.println("LA2");
          TomSymbol tomSymbol = typer.getSymbolFromType(`domain);
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
     * transform a BackQuoteAppl into its compiled form
     */
    %strategy typeBackQuoteAppl(typer:Typer) extends Identity() {
      visit TomTerm {
        BackQuoteAppl[Option=optionList,AstName=name@Name(tomName),Args=l] -> {
          TomSymbol tomSymbol = typer.getSymbolFromName(`tomName);
          TomList args  = `TopDownIdStopOnSuccess(typeBackQuoteAppl(typer)).visitLight(`l);

          //System.out.println("BackQuoteTerm: " + `tomName);
          //System.out.println("tomSymbol: " + tomSymbol);
          if(TomBase.hasConstant(`optionList)) {
            return `BuildConstant(name);
          } else if(tomSymbol != null) {
            if(TomBase.isListOperator(tomSymbol)) {
              return ASTFactory.buildList(`name,args,typer.symbolTable());
            } else if(TomBase.isArrayOperator(tomSymbol)) {
              return ASTFactory.buildArray(`name,args,typer.symbolTable());
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
     * replace 'abc' by conc('a','b','c')
     */
    %strategy typeString(typer:Typer) extends `Identity() {
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
          PairSlotAppl(slotName,RecordAppl[Option=optionList,NameList=(Name(tomName)),Slots=concSlot(),Constraints=constraintList]) -> {
            /*
             * ensure that the argument contains at least 1 character and 2 single quotes
             */
            TomSymbol stringSymbol = getSymbolFromName(`tomName);
            TomType termType = stringSymbol.getTypesToType().getCodomain();
            String type = termType.getTomType().getString();
            if(symbolTable().isCharType(type) && `tomName.length()>3) {
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
                  symbolTable().putSymbol(newName,newSymbol);

                  Slot newHead = `PairSlotAppl(slotName,RecordAppl(optionList,concTomName(Name(newName)),concSlot(),concConstraint()));
                  newArgs = `concSlot(newHead,newArgs*);
                  //System.out.println("newHead = " + newHead);
                  //System.out.println("newSymb = " + getSymbolFromName(newName));
                }
                ConstraintList newConstraintList = `concConstraint();
                %match(constraintList) {
                  concConstraint(AssignTo(var@Variable[AstType=vartype])) -> {
                    if(symbolTable().isCharType(TomBase.getTomType(`vartype))) {
                      newConstraintList = `concConstraint(AssignTo(var.setAstType(symbolTable().getStringType())));
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
