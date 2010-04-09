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

        System.out.println("In code = " + getWorkingTerm());
        Code syntaxExpandedCode = expandType((Code)getWorkingTerm());
        Code variableExpandedCode = (Code) kernelTyper.typeVariable(`EmptyType(), syntaxExpandedCode);

        Code stringExpandedCode = `TopDownIdStopOnSuccess(typeString(this)).visitLight(variableExpandedCode);
        typedCode = `TopDownIdStopOnSuccess(updateCodomain(this)).visitLight(stringExpandedCode);
        typedCode = kernelTyper.propagateVariablesTypes(typedCode);

        /* transform each BackQuoteTerm into its compiled form */
        typedCode = `TopDownIdStopOnSuccess(typeBQAppl(this)).visitLight(typedCode);
        System.out.println("\nCode after type inference = \n" + typedCode);

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
  }

  /*
   * updateSymbol is called after a first syntax expansion phase
   * this phase updates the symbolTable according to the typeTable
   * this is performed by recursively traversing each symbol
   * - backquote are typed
   * - each TomTypeAlone is replaced by the corresponding TomType
   * - default IsFsymDecl and MakeDecl are added
   */
  public void updateSymbolTable() {
    SymbolTable symbolTable = getStreamManager().getSymbolTable();

    for(String tomName:symbolTable.keySymbolIterable()) {
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      /*
       * add default IsFsymDecl unless it is a builtin type
       * add default IsFsymDecl and MakeDecl unless:
       *  - it is a builtin type
       *  - another option (if_sfsym, get_slot, etc) is already defined for this operator
       */
      //if(!getStreamManager().getSymbolTable().isBuiltinType(TomBase.getTomType(TomBase.getSymbolCodomain(tomSymbol)))) {
      //  tomSymbol = addDefaultMake(tomSymbol);
      //  tomSymbol = addDefaultIsFsym(tomSymbol);
      //}
      try {
        //tomSymbol = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this)).visitLight(tomSymbol);
        tomSymbol = expandType(`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
        tomSymbol = ((TomTerm) kernelTyper.typeVariable(`EmptyType(),`TomSymbolToTomTerm(tomSymbol))).getAstSymbol();
        tomSymbol = `TopDownIdStopOnSuccess(typeBQAppl(this)).visitLight(`tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  private TomSymbol addDefaultIsFsym(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Option=(_*,DeclarationToOption(IsFsymDecl[]),_*)] -> {
        return tomSymbol;
      }
      Symbol(name,t@TypesToType(_,codom),l,concOption(X1*,origin@OriginTracking(_,line,file),X2*)) -> {
        Declaration isfsym = `IsFsymDecl(name,BQVariable(concOption(OriginTracking(Name("t"),line,file)),Name("t"),codom),FalseTL(),OriginTracking(Name("is_fsym"),line,file));
        return `Symbol(name,t,l,concOption(X1*,origin,DeclarationToOption(isfsym),X2*));
      }
    }
    return tomSymbol;
  }

  private TomSymbol addDefaultMake(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Option=(_*,DeclarationToOption((MakeDecl|MakeEmptyList|MakeEmptyArray|MakeAddList|MakeAddArray|IsFsymDecl|GetImplementationDecl|GetSlotDecl|GetHeadDecl|GetTailDecl|IsEmptyDecl|GetElementDecl|GetSizeDecl)[]),_*)] -> {
        return tomSymbol;
      }
      Symbol(name,t@TypesToType(domain,codomain),l,concOption(X1*,origin@OriginTracking(_,line,file),X2*)) -> {
        //build variables for make
        BQTermList argsAST = `concBQTerm();
        int index = 0;
        for(TomType subtermType:(concTomType)`domain) {
          BQTerm variable = `BQVariable(concOption(),Name("t"+index),subtermType);
          argsAST = `concBQTerm(argsAST*,variable);
          index++;
        }
        BQTerm functionCall = `FunctionCall(name,codomain,argsAST);
        Declaration make = `MakeDecl(name,codomain,argsAST,BQTermToInstruction(functionCall),
            OriginTracking(Name("make"),line,file));
        return `Symbol(name,t,l,concOption(X1*,origin,DeclarationToOption(make),X2*));
      }
    }
    return tomSymbol;
  }
  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TyperPlugin.DECLARED_OPTIONS);
  }

  /*
   * The 'typeTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its typed record form:
   *    placeholders are not removed
   *    slotName are attached to arguments
   */
  %strategy typeTermApplTomSyntax(typer:TyperPlugin) extends Identity() {
    visit TomTerm {
      TermAppl[Option=option,NameList=nameList,Args=args,Constraints=constraints] -> {
        return typer.typeTermAppl(`option,`nameList,`args,`constraints);
      }

      XMLAppl[Option=optionList,NameList=nameList,AttrList=list1,ChildList=list2,Constraints=constraints] -> {
        //System.out.println("typeXML in:\n" + subject);
        return typer.typeXMLAppl(`optionList, `nameList, `list1, `list2,`constraints);
      }
    }
  }

    /*
     * this post-processing phase replaces untyped (universalType) codomain
     * by their precise type (according to the symbolTable)
     */
    %strategy updateCodomain(typer:TyperPlugin) extends `Identity() {
      visit Declaration {
        GetHeadDecl[] -> {
            throw new TomRuntimeException("updateCodomain");
        }

        decl@GetHeadDecl[Opname=Name(opName)] -> {
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

        decl@GetHeadDecl[Variable=BQVariable[AstType=domain]] -> {
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
     * replace conc('abc') by conc('a','b','c')
     */
    %strategy typeString(typer:TyperPlugin) extends `Identity() {
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

    /*
     * replaces 'TermAppl' by its 'RecordAppl' form
     * when no slotName exists, the position becomes the slotName
     */
    protected TomTerm typeTermAppl(OptionList option, TomNameList nameList, TomList args, ConstraintList constraints) {
      TomName headName = nameList.getHeadconcTomName();
      if(headName instanceof AntiName) {
        headName = ((AntiName)headName).getName();
      }
      String opName = headName.getString();
      TomSymbol tomSymbol = getSymbolFromName(opName);


      //System.out.println("typeTermAppl: " + tomSymbol);
      //System.out.println("  nameList = " + nameList);

      if(tomSymbol==null && args.isEmptyconcTomTerm()) {
        return `RecordAppl(option,nameList,concSlot(),constraints);
      }

      SlotList slotList = `concSlot();
      Strategy typeStrategy = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this));
      if(opName.equals("") || tomSymbol==null || TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
        for(TomTerm arg:(concTomTerm)args) {
          try {
            TomTerm subterm = typeStrategy.visitLight(arg);
            TomName slotName = `EmptyName();
            /*
             * we cannot optimize when subterm.isUnamedVariable
             * since it can be constrained
             */	  
            slotList = `concSlot(slotList*,PairSlotAppl(slotName,subterm));
          } catch(tom.library.sl.VisitFailure e) {
            System.out.println("should not be there");
          }
        }
      } else {
        PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
        for(TomTerm arg:(concTomTerm)args) {
          try{
            TomTerm subterm = typeStrategy.visitLight(arg);
            TomName slotName = pairNameDeclList.getHeadconcPairNameDecl().getSlotName();
            /*
             * we cannot optimize when subterm.isUnamedVariable
             * since it can be constrained
             */	  
            slotList = `concSlot(slotList*,PairSlotAppl(slotName,subterm));
            pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
          } catch(tom.library.sl.VisitFailure e) {
            System.out.println("should not be there");
          }
        }
      }

      return `RecordAppl(option,nameList,slotList,constraints);
    }

    /*
     * transform a BQAppl into its compiled form
     */
    %strategy typeBQAppl(typer:TyperPlugin) extends Identity() {
      visit BQTerm {
        BQAppl[Option=optionList,AstName=name@Name(tomName),Args=l] -> {
          TomSymbol tomSymbol = typer.getSymbolFromName(`tomName);
          BQTermList args  = `TopDownIdStopOnSuccess(typeBQAppl(typer)).visitLight(`l);
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

    private TomList sortAttributeList(TomList attrList) {
      %match(attrList) {
        concTomTerm() -> { return attrList; }
        concTomTerm(X1*,e1,X2*,e2,X3*) -> {
          %match(e1, e2) {
            TermAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name1))],_*)],
              TermAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name2))],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            TermAppl[Args=concTomTerm(TermAppl[NameList=(Name(name1))],_*)],
              TermAppl[Args=concTomTerm(TermAppl[NameList=(Name(name2))],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            RecordAppl[Slots=concSlot(PairSlotAppl(slotName,RecordAppl[NameList=(Name(name1))]),_*)],
              RecordAppl[Slots=concSlot(PairSlotAppl(slotName,RecordAppl[NameList=(Name(name2))]),_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            RecordAppl[Slots=concSlot(PairSlotAppl(slotName,TermAppl[NameList=(Name(name1))]),_*)],
              RecordAppl[Slots=concSlot(PairSlotAppl(slotName,TermAppl[NameList=(Name(name2))]),_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }
          
            /**
            BQAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name1))],_*)],
              BQAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name2))],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            BQAppl[Args=concTomTerm(TermAppl[NameList=(Name(name1))],_*)],
              BQAppl[Args=concTomTerm(TermAppl[NameList=(Name(name2))],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            BQAppl[Args=concTomTerm(BQAppl[AstName=Name(name1)],_*)],
              BQAppl[Args=concTomTerm(BQAppl[AstName=Name(name2)],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }
              */
          }
        }
      }
      return attrList;
    }

    private OptionList convertOriginTracking(String name,OptionList optionList) {
      Option originTracking = TomBase.findOriginTracking(optionList);
      %match(originTracking) {
        OriginTracking[Line=line, FileName=fileName] -> {
          return `concOption(OriginTracking(Name(name),line,fileName));
        }
      }
      System.out.println("Warning: no OriginTracking information");
      return `concOption();
    }

    protected TomTerm typeXMLAppl(OptionList optionList, TomNameList nameList,
        TomList attrList, TomList childList, ConstraintList constraints) {
      boolean implicitAttribute = TomBase.hasImplicitXMLAttribut(optionList);
      boolean implicitChild     = TomBase.hasImplicitXMLChild(optionList);

      TomList newAttrList  = `concTomTerm();
      TomList newChildList = `concTomTerm();
      TomTerm star = `UnamedVariableStar(convertOriginTracking("_*",optionList),getSymbolTable().TYPE_UNKNOWN,concConstraint());
      if(implicitAttribute) { newAttrList  = `concTomTerm(star,newAttrList*); }
      if(implicitChild)     { newChildList = `concTomTerm(star,newChildList*); }

      /*
       * the list of attributes should not be typed before the sort
       * the sortAttribute is extended to compare RecordAppl
       */

      //System.out.println("attrList = " + attrList);
      attrList = sortAttributeList(attrList);
      //System.out.println("sorted attrList = " + attrList);

      /*
       * Attributes: go from implicit notation to explicit notation
       */
      Strategy typeStrategy = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this));
      for(TomTerm attr:(concTomTerm)attrList) {
        try {
          TomTerm newPattern = typeStrategy.visitLight(attr);
          newAttrList = `concTomTerm(newPattern,newAttrList*);
          if(implicitAttribute) {
            newAttrList = `concTomTerm(star,newAttrList*);
          }
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("should not be there");
        }
      }
      newAttrList = newAttrList.reverse();

      /*
       * Childs: go from implicit notation to explicit notation
       */
      for(TomTerm child:(concTomTerm)childList) {
        try {
          TomTerm newPattern = typeStrategy.visitLight(child);
          newChildList = `concTomTerm(newPattern,newChildList*);
          if(implicitChild) {
            if(newPattern.isVariableStar()) {
              // remove the previously inserted pattern
              newChildList = newChildList.getTailconcTomTerm();
              if(newChildList.getHeadconcTomTerm().isUnamedVariableStar()) {
                // remove the previously inserted star
                newChildList = newChildList.getTailconcTomTerm();
              }
              // re-insert the pattern
              newChildList = `concTomTerm(newPattern,newChildList*);
            } else {
              newChildList = `concTomTerm(star,newChildList*);
            }
          }
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("should not be there");
        }
      }
      newChildList = newChildList.reverse();

      /*
       * encode the name and put it into the table of symbols
       */
      TomNameList newNameList = `concTomName();
matchBlock: 
      {
        %match(nameList) {
          concTomName(Name("_")) -> {
            break matchBlock;
          }

          concTomName(_*,Name(name),_*) -> {
            newNameList = `concTomName(newNameList*,Name(ASTFactory.encodeXMLString(getSymbolTable(),name)));
          }
        }
      }

      /*
       * a single "_" is converted into an UnamedVariable to match
       * any XML node
       */
      TomTerm xmlHead;

      if(newNameList.isEmptyconcTomName()) {
        xmlHead = `UnamedVariable(concOption(),getSymbolTable().TYPE_UNKNOWN,concConstraint());
      } else {
        xmlHead = `TermAppl(convertOriginTracking(newNameList.getHeadconcTomName().getString(),optionList),newNameList,concTomTerm(),concConstraint());
      }
      try {
        SlotList newArgs = `concSlot(
            PairSlotAppl(Name(Constants.SLOT_NAME),
              typeStrategy.visitLight(xmlHead)),
            PairSlotAppl(Name(Constants.SLOT_ATTRLIST),
              typeStrategy.visitLight(TermAppl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newAttrList,concConstraint()))),
            PairSlotAppl(Name(Constants.SLOT_CHILDLIST),
              typeStrategy.visitLight(TermAppl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newChildList,concConstraint()))));

        TomTerm result = `RecordAppl(optionList,concTomName(Name(Constants.ELEMENT_NODE)),newArgs,constraints);

        //System.out.println("typeXML out:\n" + result);
        return result;
      } catch(tom.library.sl.VisitFailure e) {
        //must never be executed
        return star;
      }
    }
  }
