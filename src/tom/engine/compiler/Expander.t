/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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

package tom.engine.compiler;

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
 * The Expander plugin.
 * Perform syntax expansion and more.
 */
public class Expander extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  %typeterm Expander {
    implement { Expander }
    is_sort(t) { t instanceof Expander }
  }

  %op Strategy ChoiceTopDown(s1:Strategy) {
    make(v) { `mu(MuVar("x"),ChoiceId(v,All(MuVar("x")))) }
  }

  /** some output suffixes */
  public static final String EXPANDED_SUFFIX       = ".tfix.expanded";
  public static final String EXPANDED_TABLE_SUFFIX = ".tfix.expanded.table";

  /** the declared options string */
  public static final String DECLARED_OPTIONS =
    "<options>" +
    "<boolean name='expand' altName='' description='Expander (activated by default)' value='true'/>" +
    "</options>";

  /** the kernel expander acting at very low level */
  private KernelExpander tomKernelExpander;
  /** the tomfactory for creating intermediate terms */

  /** Constructor*/
  public Expander() {
    super("Expander");
    tomKernelExpander = new KernelExpander();
  }

  /**
   * The run() method performs expansion for tom syntax, variables,...
   */
  public void run() {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    TomTerm expandedTerm = null;
    try {
      tomKernelExpander.setSymbolTable(getStreamManager().getSymbolTable());
      TomTerm syntaxExpandedTerm = (TomTerm) `ChoiceTopDown(expandTermApplTomSyntax(this)).visit((TomTerm)getWorkingTerm());
      updateSymbolTable();

      TomTerm variableExpandedTerm = expandVariable(`EmptyType(), syntaxExpandedTerm);
      TomTerm backQuoteExpandedTerm = (TomTerm) `ChoiceTopDown(expandBackQuoteAppl(this)).visit(`variableExpandedTerm);
      TomTerm stringExpandedTerm = (TomTerm) `ChoiceTopDown(expandString(this)).visit(backQuoteExpandedTerm);
      expandedTerm = (TomTerm) `ChoiceTopDown(updateCodomain(this)).visit(stringExpandedTerm);
      setWorkingTerm(expandedTerm);
      // verbose
      getLogger().log(Level.INFO, TomMessage.tomExpandingPhase.getMessage(),
          new Integer((int)(System.currentTimeMillis()-startChrono)));
    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getClass().getName(), getStreamManager().getInputFileName(), e.getMessage()} );
      e.printStackTrace();
      return;
    }
    if(intermediate) {
      Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix()
          + EXPANDED_SUFFIX, expandedTerm);
      Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix()
          + EXPANDED_TABLE_SUFFIX, symbolTable().toTerm());
    }
  }

  /*
   * updateSymbol is called after a first syntax expansion phase
   * this phase updates the symbolTable according to the typeTable
   * this is performed by recursively traversing each symbol
   * - backquote are expanded
   * - each TomTypeAlone is replaced by the corresponding TomType
   * - default IsFsymDecl and MakeDecl are added
   */
  public void updateSymbolTable() {
    SymbolTable symbolTable = getStreamManager().getSymbolTable();
    Iterator it = symbolTable.keySymbolIterator();
    Strategy expandStrategy = `ChoiceTopDown(expandTermApplTomSyntax(this));

    while(it.hasNext()) {
      String tomName = (String)it.next();
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      /*
       * add default IsFsymDecl and MakeDecl, unless it is a builtin type
       */
      if(!getStreamManager().getSymbolTable().isBuiltinType(TomBase.getTomType(TomBase.getSymbolCodomain(tomSymbol)))) {
        tomSymbol = addDefaultIsFsym(tomSymbol);
        tomSymbol = addDefaultMake(tomSymbol);
      }
      try {
        tomSymbol = (TomSymbol) `ChoiceTopDown(expandTermApplTomSyntax(this)).visit(`tomSymbol);
        tomSymbol = expandVariable(`EmptyType(),`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
        tomSymbol = (TomSymbol) `ChoiceTopDown(expandBackQuoteAppl(this)).visit(`tomSymbol);
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
        Declaration isfsym = `IsFsymDecl(name,Variable(concOption(OriginTracking(Name("t"),line,file)),Name("t"),codom,concConstraint()),Return(ExpressionToTomTerm(FalseTL())),OriginTracking(Name("is_fsym"),line,file));
        return `Symbol(name,t,l,concOption(X1*,origin,DeclarationToOption(isfsym),X2*));
      }
    }
    return tomSymbol;
  }

  private TomSymbol addDefaultMake(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Option=(_*,DeclarationToOption((MakeDecl|MakeEmptyList|MakeEmptyArray)[]),_*)] -> {
        return tomSymbol;
      }
      Symbol(name,t@TypesToType(domain,codomain),l,concOption(X1*,origin@OriginTracking(_,line,file),X2*)) -> {
        //build variables for make
        TomTypeList typesList = `domain;
        TomList argsAST = `concTomTerm();
        int index = 0;
        while(!typesList.isEmptyconcTomType()) {
          TomType subtermType = typesList.getHeadconcTomType();
          TomTerm variable = `Variable(concOption(),Name("t"+index),subtermType,concConstraint());
          argsAST = `concTomTerm(argsAST*,variable);
          typesList = typesList.getTailconcTomType();
          index++;
        }
        TomTerm functionCall = `FunctionCall(name,codomain,argsAST);
        Declaration make = `MakeDecl(name,codomain,argsAST,TomTermToInstruction(functionCall),
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
    return OptionParser.xmlToOptionList(Expander.DECLARED_OPTIONS);
  }

  private TomTerm expandVariable(TomType contextType, TomTerm subject) {
    return (TomTerm)tomKernelExpander.expandVariable(contextType,subject);
  }

  /*
   * The 'expandTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its expanded record form:
   *    placeholders are not removed
   *    slotName are attached to arguments
   * - each BackQuoteTerm by its compiled form
   */

  /*
  %strategy expandBackQuote(expander:Expander) extends `Identity() {
    visit TomTerm {
      backQuoteTerm@BackQuoteAppl[] -> {
        TomTerm t = (TomTerm) `ChoiceTopDown(expandBackQuoteAppl(expander)).visit(`backQuoteTerm);
        System.out.println("t = " + t);
        return t;
      }
    }
  }
*/

  %strategy expandTermApplTomSyntax(expander:Expander) extends `Identity() {
    visit TomTerm {
      //backQuoteTerm@BackQuoteAppl[] -> {
      //  TomTerm t = (TomTerm) `ChoiceTopDown(expandBackQuoteAppl(expander)).visit(`backQuoteTerm);
      //System.out.println("t = " + t);
      //  return t;
      //}

      TermAppl[Option=option,NameList=nameList,Args=args,Constraints=constraints] -> {
        return expander.expandTermAppl(`option,`nameList,`args,`constraints);
      }

      XMLAppl[Option=optionList,NameList=nameList,AttrList=list1,ChildList=list2,Constraints=constraints] -> {
        //System.out.println("expandXML in:\n" + subject);
        return expander.expandXMLAppl(`optionList, `nameList, `list1, `list2,`constraints);
      }
    }
  }

    /*
     * this post-processing phase replaces untyped (universalType) codomain
     * by their precise type (according to the symbolTable)
     */
    %strategy updateCodomain(expander:Expander) extends `Identity() {
      visit Declaration {
        decl@GetHeadDecl[Opname=Name(opName)] -> {
          TomSymbol tomSymbol = expander.getSymbolFromName(`opName);
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
          TomSymbol tomSymbol = expander.getSymbolFromType(`domain);
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
     * replace 'abc' by conc('a','b','c')
     */
    %strategy expandString(expander:Expander) extends `Identity() {
      visit TomTerm {
        appl@RecordAppl[NameList=(Name(tomName),_*),Slots=args] -> {
          TomSymbol tomSymbol = expander.getSymbolFromName(`tomName);
          //System.out.println("appl = " + subject);
          if(tomSymbol != null) {
            if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
              //System.out.println("appl = " + subject);
              SlotList newArgs = expander.expandChar(`args);
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
     * and expand it into a list of char: 'a','b','c'
     */
    private SlotList expandChar(SlotList args) {
      if(args.isEmptyconcSlot()) {
        return args;
      } else {
        Slot head = args.getHeadconcSlot();
        SlotList tail = expandChar(args.getTailconcSlot());
        %match(head) {
          PairSlotAppl(slotName,appl@RecordAppl[NameList=(Name(tomName)),Slots=()]) -> {
            /*
             * ensure that the argument contains at least 1 character and 2 single quotes
             */
            TomSymbol tomSymbol = getSymbolFromName(`tomName);
            TomType termType = tomSymbol.getTypesToType().getCodomain();
            String type = termType.getTomType().getString();
            if(symbolTable().isCharType(type) && `tomName.length()>3) {
              if(`tomName.charAt(0)=='\'' && `tomName.charAt(`tomName.length()-1)=='\'') {
                SlotList newArgs = tail;
                //System.out.println("bingo -> " + tomSymbol);
                for(int i=`tomName.length()-2 ; i>0 ;  i--) {
                  char c = `tomName.charAt(i);
                  String newName = "'" + c + "'";
                  TomSymbol newSymbol = tomSymbol.setAstName(`Name(newName));
                  symbolTable().putSymbol(newName,newSymbol);
                  Slot newHead = `PairSlotAppl(slotName,appl.setNameList(concTomName(Name(newName))));
                  newArgs = `concSlot(newHead,newArgs*);
                  //System.out.println("newHead = " + newHead);
                  //System.out.println("newSymb = " + getSymbolFromName(newName));
                }
                return newArgs;
              } else {
                throw new TomRuntimeException("expandChar: strange char: " + `tomName);
              }
            }
          }
        }
        return `concSlot(head,tail*);
      }
    }

    /*
     * replaces 'TermAppl' by its 'RecordAppl' form
     * when no slotName exits, the position becomes the slotName
     */
    protected TomTerm expandTermAppl(OptionList option, TomNameList nameList, TomList args, ConstraintList constraints) {
      TomName headName = nameList.getHeadconcTomName();
      if(headName instanceof AntiName) {
        headName = ((AntiName)headName).getName();
      }
      String opName = headName.getString();
      TomSymbol tomSymbol = getSymbolFromName(opName);


      //System.out.println("expandTermAppl: " + tomSymbol);
      //System.out.println("  nameList = " + nameList);

      if(tomSymbol==null && args.isEmptyconcTomTerm()) {
        return `RecordAppl(option,nameList,concSlot(),constraints);
      }

      /*
         if(tomSymbol==null && !args.isEmpty() && !opName.equals("")) {
         System.out.println("expandTermAppl: " + tomSymbol);
         System.out.println("  opName = " + opName);
         System.out.println("  args = " + args);
         throw new TomRuntimeException("expandTermAppl: unknown symbol");
         }
       */

      SlotList slotList = `concSlot();
      Strategy expandStrategy = `ChoiceTopDown(expandTermApplTomSyntax(this));
      if(opName.equals("") || tomSymbol==null || TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
        while(!args.isEmptyconcTomTerm()) {
          try {
            TomTerm subterm = (TomTerm) expandStrategy.visit(args.getHeadconcTomTerm());
            TomName slotName = `EmptyName();
            /*
             * we cannot optimize when subterm.isUnamedVariable
             * since it can be constrained
             */	  
            slotList = `concSlot(slotList*,PairSlotAppl(slotName,subterm));
            args = args.getTailconcTomTerm();
          } catch(tom.library.sl.VisitFailure e) {}
        }
      } else {
        PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
        while(!args.isEmptyconcTomTerm()) {
          try{
            TomTerm subterm = (TomTerm) expandStrategy.visit(args.getHeadconcTomTerm());
            TomName slotName = pairNameDeclList.getHeadconcPairNameDecl().getSlotName();
            /*
             * we cannot optimize when subterm.isUnamedVariable
             * since it can be constrained
             */	  
            slotList = `concSlot(slotList*,PairSlotAppl(slotName,subterm));
            args = args.getTailconcTomTerm();
            pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
          }catch(tom.library.sl.VisitFailure e){}
        }
      }

      return `RecordAppl(option,nameList,slotList,constraints);
    }

    %strategy expandBackQuoteAppl(expander:Expander) extends `Identity() {
      visit TomTerm {
        BackQuoteAppl[Option=optionList,AstName=name@Name(tomName),Args=l] -> {
          TomSymbol tomSymbol = expander.getSymbolFromName(`tomName);
          TomList args  = (TomList) (`ChoiceTopDown(expandBackQuoteAppl(expander))).visit(`l);

          //System.out.println("BackQuoteTerm: " + `tomName);
          //System.out.println("tomSymbol: " + tomSymbol);
          if(TomBase.hasConstant(`optionList)) {
            return `BuildConstant(name);
          } else if(tomSymbol != null) {
            if(TomBase.isListOperator(tomSymbol)) {
              return ASTFactory.buildList(`name,args,expander.symbolTable());
            } else if(TomBase.isArrayOperator(tomSymbol)) {
              return ASTFactory.buildArray(`name,args,expander.symbolTable());
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
      } // end match
    }

    private static TomList sortAttributeList(TomList attrList) {
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

            BackQuoteAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name1))],_*)],
              BackQuoteAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name2))],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            BackQuoteAppl[Args=concTomTerm(TermAppl[NameList=(Name(name1))],_*)],
              BackQuoteAppl[Args=concTomTerm(TermAppl[NameList=(Name(name2))],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            BackQuoteAppl[Args=concTomTerm(BackQuoteAppl[AstName=Name(name1)],_*)],
              BackQuoteAppl[Args=concTomTerm(BackQuoteAppl[AstName=Name(name2)],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }
          }
        }
      }
      return attrList;
    }

    private static OptionList convertOriginTracking(String name,OptionList optionList) {
      Option originTracking = TomBase.findOriginTracking(optionList);
      %match(originTracking) {
        OriginTracking[Line=line, FileName=fileName] -> {
          return `concOption(OriginTracking(Name(name),line,fileName));
        }
      }
      System.out.println("Warning: no OriginTracking information");
      return `concOption();
    }

    protected TomTerm expandXMLAppl(OptionList optionList, TomNameList nameList,
        TomList attrList, TomList childList, ConstraintList constraints) {
      boolean implicitAttribute = TomBase.hasImplicitXMLAttribut(optionList);
      boolean implicitChild     = TomBase.hasImplicitXMLChild(optionList);

      TomList newAttrList  = `concTomTerm();
      TomList newChildList = `concTomTerm();
      TomTerm star = `UnamedVariableStar(convertOriginTracking("_*",optionList),TomTypeAlone("unknown type"),concConstraint());
      if(implicitAttribute) { newAttrList  = `concTomTerm(star,newAttrList*); }
      if(implicitChild)     { newChildList = `concTomTerm(star,newChildList*); }

      /*
       * the list of attributes should not be expanded before the sort
       * the sortAttribute is extended to compare RecordAppl
       */

      //System.out.println("attrList = " + attrList);
      attrList = sortAttributeList(attrList);
      //System.out.println("sorted attrList = " + attrList);

      /*
       * Attributes: go from implicit notation to explicit notation
       */
      Strategy expandStrategy = `ChoiceTopDown(expandTermApplTomSyntax(this));
      while(!attrList.isEmptyconcTomTerm()) {
        try {
          TomTerm newPattern = (TomTerm) expandStrategy.visit(attrList.getHeadconcTomTerm());
          newAttrList = `concTomTerm(newPattern,newAttrList*);
          if(implicitAttribute) {
            newAttrList = `concTomTerm(star,newAttrList*);
          }
          attrList = attrList.getTailconcTomTerm();
        } catch(tom.library.sl.VisitFailure e) {}
      }
      newAttrList = ASTFactory.reverse(newAttrList);

      /*
       * Childs: go from implicit notation to explicit notation
       */
      while(!childList.isEmptyconcTomTerm()) {
        try {
          TomTerm newPattern = (TomTerm) expandStrategy.visit(childList.getHeadconcTomTerm());
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
          childList = childList.getTailconcTomTerm();
        }catch(tom.library.sl.VisitFailure e){}
      }
      newChildList = ASTFactory.reverse(newChildList);

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
            newNameList = `concTomName(newNameList*,Name(ASTFactory.encodeXMLString(symbolTable(),name)));
          }
        }
      }

      /*
       * a single "_" is converted into an UnamedVariable to match
       * any XML node
       */
      TomTerm xmlHead;

      if(newNameList.isEmptyconcTomName()) {
        xmlHead = `UnamedVariable(concOption(),TomTypeAlone("unknown type"),concConstraint());
      } else {
        xmlHead = `TermAppl(convertOriginTracking(newNameList.getHeadconcTomName().getString(),optionList),newNameList,concTomTerm(),concConstraint());
      }
      try {
        //VisitableVisitor expandStrategy = (ChoiceTopDown(expandTermApplTomSyntax(this)));
        SlotList newArgs = `concSlot(
            PairSlotAppl(Name(Constants.SLOT_NAME),
              (TomTerm) expandStrategy.visit(xmlHead)),
            PairSlotAppl(Name(Constants.SLOT_ATTRLIST),
              (TomTerm) expandStrategy.visit(TermAppl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newAttrList,concConstraint()))),
            PairSlotAppl(Name(Constants.SLOT_CHILDLIST),
              (TomTerm) expandStrategy.visit(TermAppl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newChildList,concConstraint()))));

        TomTerm result = `RecordAppl(optionList,concTomName(Name(Constants.ELEMENT_NODE)),newArgs,constraints);

        //System.out.println("expandXML out:\n" + result);
        return result;
      } catch(tom.library.sl.VisitFailure e) {
        //must never be executed
        return star;
      }
    }
  }
