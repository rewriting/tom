/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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

import tom.engine.adt.tomsignature.types.*;
import tom.engine.exception.TomRuntimeException;
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

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;


/**
 * The TomExpander plugin.
 * Perform syntax expansion and more.
 */
public class TomExpander extends TomGenericPlugin {

  %include { adt/tomsignature/TomSignature.tom }
  %include { mutraveler.tom }

  %typeterm TomExpander {
    implement { TomExpander }
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
  private TomKernelExpander tomKernelExpander;
  /** the tomfactory for creating intermediate terms */

  /** Constructor*/
  public TomExpander() {
    super("TomExpander");
    tomKernelExpander = new TomKernelExpander();
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
      TomTerm context = `emptyTerm();

      TomTerm variableExpandedTerm = expandVariable(context, syntaxExpandedTerm);
      TomTerm stringExpandedTerm = (TomTerm) `ChoiceTopDown(expandString(this)).visit(variableExpandedTerm);
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
   */
  public void updateSymbolTable() {
    SymbolTable symbolTable = getStreamManager().getSymbolTable();
    Iterator it = symbolTable.keySymbolIterator();
    VisitableVisitor expandStrategy = (`ChoiceTopDown(expandTermApplTomSyntax(this)));

    while(it.hasNext()) {
      String tomName = (String)it.next();
      TomTerm emptyContext = `emptyTerm();
      TomSymbol tomSymbol = getSymbolFromName(tomName);

      /*
       * we update codomains which a constrained by a symbolName
       * (come from the %strategy operator)
       */
      tomSymbol = updateConstrainedSymbolCodomain(tomSymbol, symbolTable);
      /*
       * add default isFsym and make HERE
       */
      tomSymbol = addDefaultIsFSym(tomSymbol);
      try {
        //TomTerm term = (TomTerm) expandStrategy.visit(`TomSymbolToTomTerm(tomSymbol));
        //tomSymbol = term.getAstSymbol();
        tomSymbol = (TomSymbol) expandStrategy.visit(`tomSymbol);
      } catch(VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      tomSymbol = expandVariable(emptyContext,`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  private TomSymbol updateConstrainedSymbolCodomain(TomSymbol symbol, SymbolTable symbolTable) {
    %match(TomSymbol symbol) {
      Symbol(name,TypesToType(domain,Codomain(Name(opName))),slots,options) -> {
        //System.out.println("update codomain: " + `name);
        //System.out.println("depend from : " + `opName);
        TomSymbol dependSymbol = symbolTable.getSymbolFromName(`opName);
        //System.out.println("1st depend codomain: " + getSymbolCodomain(dependSymbol));
        dependSymbol = updateConstrainedSymbolCodomain(dependSymbol,symbolTable);
        TomType codomain = getSymbolCodomain(dependSymbol);
        //System.out.println("2nd depend codomain: " + getSymbolCodomain(dependSymbol));
        OptionList newOptions = `options;
        %match(OptionList options) {
          concOption(O1*,DeclarationToOption(m@MakeDecl[astType=Codomain[]]),O2*) -> {
            Declaration newMake = `m.setAstType(codomain);
            //System.out.println("newMake: " + newMake);
            newOptions = `concOption(O1*,O2*,DeclarationToOption(newMake));
          }
        }
        TomSymbol newSymbol = `Symbol(name,TypesToType(domain,codomain),slots,newOptions);
        //System.out.println("newSymbol: " + newSymbol);
        symbolTable.putSymbol(`name.getString(),newSymbol);
        return newSymbol;
      }
    }
    return symbol;
  }

  private TomSymbol addDefaultIsFSym(TomSymbol tomSymbol) {
    %match(TomSymbol tomSymbol) {
      Symbol[option=(_*,DeclarationToOption(IsFsymDecl[]),_*)] -> {
        return tomSymbol;
      }
      Symbol(name,t@TypesToType(_,codom),l,(b*,origin@OriginTracking(_,line,file),a*)) -> {
        return `Symbol(name,t,l,concOption(b*,origin,DeclarationToOption(IsFsymDecl(name,Variable(concOption(OriginTracking(Name("t"),line,file)),Name("t"),codom,concConstraint()),Return(ExpressionToTomTerm(FalseTL())),OriginTracking(Name("is_fsym"),line,file))),a*));
      }
    }
    return tomSymbol;
  }

  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomExpander.DECLARED_OPTIONS);
  }

  private TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {
    return (TomTerm)tomKernelExpander.expandVariable(contextSubject,subject);
  }

  /*
   * The 'expandTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its expanded record form:
   *    placeholders are removed
   *    slotName are attached to arguments
   * - each BackQuoteTerm by its compiled form
   */

  %strategy expandTermApplTomSyntax(expander:TomExpander) extends `Identity(){
    visit TomTerm {
      backQuoteTerm@BackQuoteAppl[] -> {
        TomTerm t = (TomTerm) (`ChoiceTopDown(expandBackQuoteAppl(expander))).visit(`backQuoteTerm);
        //System.out.println("t = " + t);
        return t;
      }

      TermAppl[option=option,nameList=nameList,args=args,constraints=constraints] -> {
        return expander.expandTermAppl(`option,`nameList,`args,`constraints);
      }

      XMLAppl[option=optionList,nameList=nameList,attrList=list1,childList=list2,constraints=constraints] -> {
        //System.out.println("expandXML in:\n" + subject);
        return expander.expandXMLAppl(`optionList, `nameList, `list1, `list2,`constraints);
      }
    }
  }

  /*
   * this post-processing phase replaces untyped (universalType) codomain
   * by their precise type (according to the symbolTable)
   */
  %strategy updateCodomain(expander:TomExpander) extends `Identity() {
    visit Declaration {
      decl@GetHeadDecl[opname=Name(opName)] -> {
        TomSymbol tomSymbol = expander.getSymbolFromName(`opName);
        TomTypeList codomain = getSymbolDomain(tomSymbol);
        if(codomain.isSingle()) {
          Declaration t = (Declaration)`decl;
          t = t.setCodomain(codomain.getHead());
          return t;
        } else {
          throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
        }
      }

      decl@GetHeadDecl[variable=Variable[astType=domain]] -> {
        TomSymbol tomSymbol = expander.getSymbolFromType(`domain);
        if(tomSymbol != null) {
          TomTypeList codomain = getSymbolDomain(tomSymbol);

          if(codomain.isSingle()) {
            Declaration t = (Declaration)`decl;
            t = t.setCodomain(codomain.getHead());
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
  %strategy expandString(expander:TomExpander) extends `Identity() {
        visit TomTerm {
          appl@RecordAppl[nameList=(Name(tomName),_*),slots=args] -> {
            TomSymbol tomSymbol = expander.getSymbolFromName(`tomName);
            //System.out.println("appl = " + subject);
            if(tomSymbol != null) {
              if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
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
    if(args.isEmpty()) {
      return args;
    } else {
      Slot head = args.getHead();
      SlotList tail = expandChar(args.getTail());
      %match(Slot head) {
        PairSlotAppl(slotName,appl@RecordAppl[nameList=(Name(tomName)),slots=()]) -> {
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
                newArgs = `manySlotList(newHead,newArgs);
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
      return `manySlotList(head,tail);
    }
  }

  /*
   * replaces 'TermAppl' by its 'RecordAppl' form
   * when no slotName exits, the position becomes the slotName
   */
  protected TomTerm expandTermAppl(OptionList option, NameList nameList, TomList args, ConstraintList constraints) {
    String opName = nameList.getHead().getString();
    TomSymbol tomSymbol = getSymbolFromName(opName);


    //System.out.println("expandTermAppl: " + tomSymbol);
    //System.out.println("  nameList = " + nameList);

    if(tomSymbol==null && args.isEmpty()) {
      return `RecordAppl(option,nameList,emptySlotList(),constraints);
    }

    /*
       if(tomSymbol==null && !args.isEmpty() && !opName.equals("")) {
       System.out.println("expandTermAppl: " + tomSymbol);
       System.out.println("  opName = " + opName);
       System.out.println("  args = " + args);
       throw new TomRuntimeException("expandTermAppl: unknown symbol");
       }
     */

    SlotList slotList = `emptySlotList();
    VisitableVisitor expandStrategy = (`ChoiceTopDown(expandTermApplTomSyntax(this)));
    if(opName.equals("") || tomSymbol==null || isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
      while(!args.isEmpty()) {
        try{
          TomTerm subterm = (TomTerm) expandStrategy.visit(args.getHead());
          TomName slotName = `EmptyName();
          if(subterm.isUnamedVariable()) {
            // do nothing
          } else {
            slotList = (SlotList)slotList.append(`PairSlotAppl(slotName,subterm));
          }
          args = args.getTail();
        }catch(VisitFailure e){}
      }
    } else {
      PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
      while(!args.isEmpty()) {
        try{
          TomTerm subterm = (TomTerm) expandStrategy.visit(args.getHead());
          TomName slotName = pairNameDeclList.getHead().getSlotName();
          if(subterm.isUnamedVariable()) {
            // do nothing
          } else {
            slotList = (SlotList)slotList.append(`PairSlotAppl(slotName,subterm));
          }
          args = args.getTail();
          pairNameDeclList = pairNameDeclList.getTail();
        }catch(VisitFailure e){}
      }
    }

    return `RecordAppl(option,nameList,slotList,constraints);
  }

  %strategy expandBackQuoteAppl(expander:TomExpander) extends `Identity() {
        visit TomTerm {
          BackQuoteAppl[option=optionList,astName=name@Name(tomName),args=l] -> {
            TomSymbol tomSymbol = expander.getSymbolFromName(`tomName);
            TomList args  = (TomList) (`ChoiceTopDown(expandBackQuoteAppl(expander))).visit(`l);

            //System.out.println("BackQuoteTerm: " + `tomName);
            //System.out.println("tomSymbol: " + tomSymbol);
            if(hasConstant(`optionList)) {
              return `BuildConstant(name);
            } else if(tomSymbol != null) {
              if(isListOperator(tomSymbol)) {
                return ASTFactory.buildList(`name,args);
              } else if(isArrayOperator(tomSymbol)) {
                return ASTFactory.buildArray(`name,args);
              } else if(isDefinedSymbol(tomSymbol)) {
                return `FunctionCall(name,args);
              } else {
                String moduleName = getModuleName(`optionList);
                if(moduleName==null) {
                  moduleName = TomBase.DEFAULT_MODULE_NAME;
                }
                return `BuildTerm(name,args,moduleName);
              }
            } else {
              return `FunctionCall(name,args);
            }
          }
        } // end match
    }

  private static TomList sortAttributeList(TomList attrList) {
    %match(TomList attrList) {
      concTomTerm() -> { return attrList; }
      concTomTerm(X1*,e1,X2*,e2,X3*) -> {
        %match(TomTerm e1, TomTerm e2) {
          TermAppl[args=manyTomList(RecordAppl[nameList=(Name(name1))],_)],
            TermAppl[args=manyTomList(RecordAppl[nameList=(Name(name2))],_)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          TermAppl[args=manyTomList(TermAppl[nameList=(Name(name1))],_)],
            TermAppl[args=manyTomList(TermAppl[nameList=(Name(name2))],_)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          RecordAppl[slots=manySlotList(PairSlotAppl(slotName,RecordAppl[nameList=(Name(name1))]),_)],
            RecordAppl[slots=manySlotList(PairSlotAppl(slotName,RecordAppl[nameList=(Name(name2))]),_)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          RecordAppl[slots=manySlotList(PairSlotAppl(slotName,TermAppl[nameList=(Name(name1))]),_)],
            RecordAppl[slots=manySlotList(PairSlotAppl(slotName,TermAppl[nameList=(Name(name2))]),_)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          BackQuoteAppl[args=manyTomList(RecordAppl[nameList=(Name(name1))],_)],
            BackQuoteAppl[args=manyTomList(RecordAppl[nameList=(Name(name2))],_)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          BackQuoteAppl[args=manyTomList(TermAppl[nameList=(Name(name1))],_)],
            BackQuoteAppl[args=manyTomList(TermAppl[nameList=(Name(name2))],_)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          BackQuoteAppl[args=manyTomList(BackQuoteAppl[astName=Name(name1)],_)],
            BackQuoteAppl[args=manyTomList(BackQuoteAppl[astName=Name(name2)],_)] -> {
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
    Option originTracking = findOriginTracking(optionList);
    %match(Option originTracking) {
      OriginTracking[line=line, fileName=fileName] -> {
        return `concOption(OriginTracking(Name(name),line,fileName));
      }
    }
    System.out.println("Warning: no OriginTracking information");
    return emptyOption();
  }

  protected TomTerm expandXMLAppl(OptionList optionList, NameList nameList,
      TomList attrList, TomList childList, ConstraintList constraints) {
    boolean implicitAttribute = hasImplicitXMLAttribut(optionList);
    boolean implicitChild     = hasImplicitXMLChild(optionList);

    TomList newAttrList  = `emptyTomList();
    TomList newChildList = `emptyTomList();
    TomTerm star = ASTFactory.makeUnamedVariableStar(convertOriginTracking("_*",optionList),"unknown type",`concConstraint());
    if(implicitAttribute) { newAttrList  = `manyTomList(star,newAttrList); }
    if(implicitChild)     { newChildList = `manyTomList(star,newChildList); }

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
    VisitableVisitor expandStrategy = (`ChoiceTopDown(expandTermApplTomSyntax(this)));
    while(!attrList.isEmpty()) {
      try{
        TomTerm newPattern = (TomTerm) expandStrategy.visit(attrList.getHead());
        newAttrList = `manyTomList(newPattern,newAttrList);
        if(implicitAttribute) {
          newAttrList = `manyTomList(star,newAttrList);
        }
        attrList = attrList.getTail();
      }catch(VisitFailure e){}
    }
    newAttrList = (TomList) newAttrList.reverse();

    /*
     * Childs: go from implicit notation to explicit notation
     */
    while(!childList.isEmpty()) {
      try{
        TomTerm newPattern = (TomTerm) expandStrategy.visit(childList.getHead());
        newChildList = `manyTomList(newPattern,newChildList);
        if(implicitChild) {
          if(newPattern.isVariableStar()) {
            // remove the previously inserted pattern
            newChildList = newChildList.getTail();
            if(newChildList.getHead().isUnamedVariableStar()) {
              // remove the previously inserted star
              newChildList = newChildList.getTail();
            }
            // re-insert the pattern
            newChildList = `manyTomList(newPattern,newChildList);
          } else {
            newChildList = `manyTomList(star,newChildList);
          }
        }
        childList = childList.getTail();
      }catch(VisitFailure e){}
    }
    newChildList = (TomList) newChildList.reverse();

    /*
     * encode the name and put it into the table of symbols
     */
    NameList newNameList = `concTomName();
matchBlock: {
              %match(NameList nameList) {
                (Name("_")) -> {
                  break matchBlock;
                }

                (_*,Name(name),_*) -> {
                  newNameList = (NameList)newNameList.append(`Name(ASTFactory.encodeXMLString(symbolTable(),name)));
                }
              }
            }

            /*
             * a single "_" is converted into a Placeholder to match
             * any XML node
             */
            TomTerm xmlHead;

            if(newNameList.isEmpty()){
              xmlHead = `Placeholder(emptyOption(),concConstraint());
            } else {
              xmlHead = `TermAppl(convertOriginTracking(newNameList.getHead().getString(),optionList),newNameList,empty(),concConstraint());
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
            } catch(VisitFailure e) {
              //must never be executed
              return star;
            }

  }


} // class TomExpander
