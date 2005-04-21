/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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

package jtom.compiler;

import java.util.logging.Level;
import java.util.Iterator;

import jtom.adt.tomsignature.types.*;
import jtom.exception.TomRuntimeException;
import jtom.tools.TomFactory;
import jtom.tools.TomGenericPlugin;
import jtom.tools.Tools;
import jtom.xml.Constants;
import tom.library.traversal.Replace1;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

/**
 * The TomExpander plugin.
 * Perform syntax expansion and more.
 */
public class TomExpander extends TomGenericPlugin {

  %include { adt/tomsignature/TomSignature.tom }

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
  private TomFactory tomFactory;
  
  /** Constructor*/
  public TomExpander() {
    super("TomExpander");
    tomKernelExpander = new TomKernelExpander();
    tomFactory = new TomFactory();
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
      TomTerm syntaxExpandedTerm   = expandTermApplTomSyntax( (TomTerm)getWorkingTerm() );
      updateSymbolTable();
      TomTerm context = `emptyTerm();
      
      TomTerm variableExpandedTerm = expandVariable(context, syntaxExpandedTerm);
      TomTerm stringExpandedTerm   = expandString(variableExpandedTerm);
      expandedTerm = updateCodomain(stringExpandedTerm);
      
      setWorkingTerm(expandedTerm);
      // verbose
      getLogger().log(Level.INFO, "TomExpandingPhase",
                      new Integer((int)(System.currentTimeMillis()-startChrono)));
    } catch (Exception e) {
      getLogger().log( Level.SEVERE, "ExceptionMessage",
                       new Object[]{getClass().getName(), getStreamManager().getInputFile().getName(), e.getMessage()} );
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
   * - each TomTypeAlone is replace by the corresponding TomType
   */
  public void updateSymbolTable() {
    Iterator it = getStreamManager().getSymbolTable().keySymbolIterator();
    while(it.hasNext()) {
      String tomName = (String)it.next();
      TomTerm emptyContext = `emptyTerm();
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      tomSymbol = expandTermApplTomSyntax(`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
      //System.out.println("symbol = " + tomSymbol);
      tomSymbol = expandVariable(emptyContext,`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }
  
  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomExpander.DECLARED_OPTIONS);
  }

  private TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {
    return tomKernelExpander.expandVariable(contextSubject,subject); 
  }
 
  /*
   * The 'expandTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its expanded record form:
   *    placeholders are removed
   *    slotName are attached to arguments
   * - each BackQuoteTerm by its compiled form
   */
  
  public TomTerm expandTermApplTomSyntax(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {

              backQuoteTerm@BackQuoteAppl[] -> {
                TomTerm t = expandBackQuoteAppl(`backQuoteTerm);
                //System.out.println("t = " + t);
                return t;
              }

              TermAppl[option=option,nameList=nameList,args=args,constraints=constraints] -> {
                return expandTermAppl(`option,`nameList,`args,`constraints);
              }

              XMLAppl[option=optionList,nameList=nameList,attrList=list1,childList=list2,constraints=constraints] -> {
                //System.out.println("expandXML in:\n" + subject);
                return expandXMLAppl(`optionList, `nameList, `list1, `list2,`constraints);
              }
              
              _ -> {
                return traversal().genericTraversal(subject,this);
              }
            } // end match
          } else {
            return traversal().genericTraversal(subject,this);
          }
        } // end apply
      }; // end new

    return (TomTerm) replace.apply(subject);
  }

  /*
   * this post-processing phase replaces untyped (universalType) codomain
   * by their precise type (according to the symbolTable)
   */
  private TomTerm updateCodomain(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof Declaration) {
            %match(Declaration subject) {
              GetHeadDecl[opname=Name(opName)] -> {
                TomSymbol tomSymbol = getSymbolFromName(`opName);
                TomTypeList codomain = getSymbolDomain(tomSymbol);
                if(codomain.isSingle()) {
                  Declaration t = (Declaration)subject;
                  t = t.setCodomain(codomain.getHead());
                  return t;
                } else {
                  throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
                }
              }
            
              GetHeadDecl[variable=Variable[astType=domain]] -> {
                TomSymbol tomSymbol = getSymbolFromType(`domain);
                if(tomSymbol != null) {
                  TomTypeList codomain = getSymbolDomain(tomSymbol);
                  //System.out.println("tomSymbol = " + tomSymbol);
                  //System.out.println("domain    = " + domain);
                  //System.out.println("codomain  = " + codomain);
                  
                  if(codomain.isSingle()) {
                    Declaration t = (Declaration)subject;
                    t = t.setCodomain(codomain.getHead());
                    return t;
                  } else {
                    throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
                  }
                }
              }
            
              // default rule
              _ -> {
                return traversal().genericTraversal(subject,this);
              }
            } // end match
          } else {
            // not instance of Declaration
            return traversal().genericTraversal(subject,this);
          }
        
        } // end apply
      }; // end new
  
  return (TomTerm) replace.apply(subject);
}

  /*
   * replace 'abc' by conc('a','b','c')
   */
  private TomTerm expandString(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
              appl@RecordAppl[nameList=(Name(tomName),_*),slots=args] -> {
                TomSymbol tomSymbol = getSymbolFromName(`tomName);
                //System.out.println("appl = " + subject);
                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
                    //System.out.println("appl = " + subject);
                    SlotList newArgs = expandChar(`args);
                    if(newArgs!=`args) {
                      return `appl.setSlots(newArgs);
                    }
                  }
                }
              }

              // default rule
              _ -> {
                return traversal().genericTraversal(subject,this);
              }
            } // end match
          } else {
            // not instance of Declaration
            return traversal().genericTraversal(subject,this);
          }

        } // end apply
      }; // end new

    return (TomTerm) replace.apply(subject);
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

        _ -> {
          return `manySlotList(head,tail);
        }
      }

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
    if(opName.equals("") || tomSymbol==null || isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
      while(!args.isEmpty()) {
        TomTerm subterm = expandTermApplTomSyntax(args.getHead());
        TomName slotName = `EmptyName();
        if(subterm.isUnamedVariable()) {
          // do nothing
        } else {
          slotList = (SlotList)slotList.append(`PairSlotAppl(slotName,subterm));
        }
        args = args.getTail();
      }
    } else {
      PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
      while(!args.isEmpty()) {
        TomTerm subterm = expandTermApplTomSyntax(args.getHead());
        TomName slotName = pairNameDeclList.getHead().getSlotName();
        if(subterm.isUnamedVariable()) {
          // do nothing
        } else {
          slotList = (SlotList)slotList.append(`PairSlotAppl(slotName,subterm));
        }
        args = args.getTail();
        pairNameDeclList = pairNameDeclList.getTail();
      }
    }

    return `RecordAppl(option,nameList,slotList,constraints);
  }

  protected TomTerm expandBackQuoteAppl(TomTerm t) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
          if(t instanceof TomTerm) {
            %match(TomTerm t) {
              BackQuoteAppl[option=optionList,astName=name@Name(tomName),args=l] -> {
                TomSymbol tomSymbol = getSymbolFromName(`tomName);
                TomList args  = (TomList) traversal().genericTraversal(`l,this);
                
                //System.out.println("BackQuoteTerm: " + `tomName);
                //System.out.println("tomSymbol: " + tomSymbol);

                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol)) {
                    return tomFactory.buildList(`name,args);
                  } else if(isArrayOperator(tomSymbol)) {
                    return tomFactory.buildArray(`name,args);
                  } else if(isStringOperator(tomSymbol)) {
                    return `BuildVariable(name,emptyTomList());
                  } else if(isDefinedSymbol(tomSymbol)) {
                    return `FunctionCall(name,args);
                  } else {
                    return `BuildTerm(name,args);
                  }
                } else if(args.isEmpty() && !hasConstructor(`optionList)) {
                  return `BuildVariable(name,emptyTomList());
                } else {
                  return `FunctionCall(name,args);
                }
              }
            } // end match 
          }
          return traversal().genericTraversal(t,this);
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(t);
  }


  private TomList sortAttributeList(TomList attrList) {
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

  private OptionList convertOriginTracking(String name,OptionList optionList) {
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

    TomTerm star = getAstFactory().makeUnamedVariableStar(convertOriginTracking("_*",optionList),"unknown type",`concConstraint());
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
    while(!attrList.isEmpty()) {
      TomTerm newPattern = expandTermApplTomSyntax(attrList.getHead());
      newAttrList = `manyTomList(newPattern,newAttrList);
      if(implicitAttribute) { 
        newAttrList = `manyTomList(star,newAttrList); 
      }
      attrList = attrList.getTail();
    }
    newAttrList = (TomList) newAttrList.reverse();
    
    /*
     * Childs: go from implicit notation to explicit notation
     */
    while(!childList.isEmpty()) {
      TomTerm newPattern = expandTermApplTomSyntax(childList.getHead());
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
          newNameList = (NameList)newNameList.append(`Name(tomFactory.encodeXMLString(symbolTable(),name)));
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

    SlotList newArgs = `concSlot(
        PairSlotAppl(Name(Constants.SLOT_NAME),
                     expandTermApplTomSyntax(xmlHead)),
        PairSlotAppl(Name(Constants.SLOT_ATTRLIST),
                     expandTermApplTomSyntax(TermAppl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newAttrList,concConstraint()))),
        PairSlotAppl(Name(Constants.SLOT_CHILDLIST),
                     expandTermApplTomSyntax(TermAppl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newChildList,concConstraint()))));
    
    TomTerm result = `RecordAppl(optionList,concTomName(Name(Constants.ELEMENT_NODE)),newArgs,constraints);

    //System.out.println("expandXML out:\n" + result);
    return result;
   
  }
  
 
} // class TomExpander
