/*
   
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom;
  
import java.util.*;

import aterm.*;

import jtom.tools.*;
import jtom.adt.*;
import jtom.runtime.*;

//import jtom.runtime.set.SharedSet;

public class TomBase {
  private TomEnvironment tomEnvironment;
  private TomList empty;
  private GenericTraversal traversal;
  
  protected final static boolean debug = false;

  public TomBase(TomEnvironment tomEnvironment) {
    this.tomEnvironment = tomEnvironment;
    this.empty = tsf().makeTomList();
    this.traversal = new GenericTraversal();
  }

  protected ASTFactory ast() {
    return tomEnvironment.getASTFactory();
  }

  protected TomSignatureFactory tsf() {
    return tomEnvironment.getTomSignatureFactory();
  }

  public GenericTraversal traversal() {
    return this.traversal;
  }

  protected final TomSignatureFactory getTomSignatureFactory() {
    return tsf();
  }
  
  protected SymbolTable symbolTable() {
    return tomEnvironment.getSymbolTable();
  }

  protected boolean isIntType(String type) {
    return type.equals("int");
  }

  protected boolean isStringType(String type) {
    return type.equals("String");
  }

  protected boolean isBoolType(String type) {
    return type.equals("bool");
  }

  protected boolean isDoubleType(String type) {
    return type.equals("double");
  }
  
  protected TomType getIntType() {
    return symbolTable().getType("int");
  }

  protected TomType getDoubleType() {
    return symbolTable().getType("double");
  }

  protected TomType getBoolType() {
    return symbolTable().getType("bool");
  }
  
  protected TomType getUniversalType() {
    return symbolTable().getType("universal");
  }

  protected TomNumber makeNumber(int n) {
    return tsf().makeTomNumber_Number(n);
  }
  
  protected TomList empty() {
    return empty;
  }

  protected TomList cons(TomTerm t, TomList l) {
    if(t!=null) {
      return tsf().makeTomList(t,l);
    } else {
      System.out.println("cons: Warning t == null");
      return l;
    }
  }

  protected TomNumberList appendNumber(int n, TomNumberList path) {
    return (TomNumberList) path.append(makeNumber(n));
  }
  

  protected TomList appendInstruction(Instruction t, TomList l) {
    return append(`InstructionToTomTerm(t), l);
  }
  
  protected TomList append(TomTerm t, TomList l) {
    if(l.isEmpty()) {
      return cons(t,l);
    } else {
      return cons(l.getHead(), append(t,l.getTail()));
    }
  }
  
  protected TomList concat(TomList l1, TomList l2) {
    if(l1.isEmpty()) {
      return l2;
    } else {
      return cons(l1.getHead(), concat(l1.getTail(),l2));
    }
  }

  protected TomList reverse(TomList l) {
    TomList reverse = empty();
    while(!l.isEmpty()){
      reverse = cons(l.getHead(),reverse);
      l = l.getTail();
    }
    return reverse;
  }

  protected int length(TomList l) {
    if(l.isEmpty()) {
      return 0;
    } else {
      return 1 + length(l.getTail());
    }
  }
  
// ------------------------------------------------------------
  %include { adt/TomSignature.tom }
// ------------------------------------------------------------

  protected String getTomType(TomType type) {
    %match(TomType type) {
      TomType(s) -> {return s;}
      TomTypeAlone(s) -> {return s;}
      Type(TomType(s),_) -> {return s;}
      EmptyType() -> {return null;}
      _ -> {
        System.out.println("getTomType error on term: " + type);
        System.exit(1);
      }
    }
    return null;
  }

  protected String getTLType(TomType type) {
    return getTLCode(type.getTlType());
  }

  protected String getTLCode(TomType type) {
    %match(TomType type) {
      TLType(TL[code=tlType])  -> { return tlType; }
      TLType(ITL[code=tlType]) -> { return tlType; }
      _ -> {
        System.out.println("getTLCode error on term: " + type);
        System.exit(1);
      }
    }
    return null;
  }

  protected TomSymbol getSymbol(String tomName) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    return tomSymbol;
  }
  
  protected TomType getSymbolCodomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getCodomain();
    } else {
        //System.out.println("getSymbolCodomain: symbol = " + symbol);
      return `EmptyType();
    }
  }   

  protected TomTypeList getSymbolDomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getDomain();
    } else {
        //System.out.println("getSymbolDomain: symbol = " + symbol);
      return tsf().makeTomTypeList();
    }
  }

  protected String getSymbolCode(TomSymbol symbol) {
      //%variable
    %match(TomSymbol symbol) {
      Symbol[tlCode=TL[code=tlCode]]  -> { return tlCode; }
      Symbol[tlCode=ITL[code=tlCode]] -> { return tlCode; }
      _ -> {
        System.out.println("getSymbolCode error on term: " + symbol);
        System.exit(1);
      }
    }
    return null;
  }

  protected TomType getTermType(TomTerm t) {
      //%variable
    %match(TomTerm t) {
      Appl(option, Name(tomName),subterms) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        return tomSymbol.getTypesToType().getCodomain();
      }
      
      Variable[astType=type] |
      VariableStar[astType=type] |
      UnamedVariable[astType=type] |
      UnamedVariableStar[astType=type] -> { return type; }

      _ -> {
        System.out.println("getTermType error on term: " + t);
        System.exit(1);
      }
    }
    return null;
  }

  private HashMap numberListToIdentifierMap = new HashMap();

  private String elementToIdentifier(TomNumber subject) {
    %match(TomNumber subject) {
      Begin(Number(i)) -> { return "begin" + i; }
      End(Number(i)) -> { return "end" + i; }
      MatchNumber(Number(i)) -> { return "match" + i; }
      PatternNumber(Number(i)) -> { return "pattern" + i; }
      ListNumber(Number(i)) -> { return "list" + i; }
      IndexNumber(Number(i)) -> { return "index" + i; }
      AbsVar(Number(i)) -> { return "absvar" + i; }
      RenamedVar(Name(name)) -> { return "renamedvar_" + name; }
      RuleVar() -> { return "rulevar"; }
      Number(i) -> { return "" + i; }
      _ -> { return subject.toString(); }
    }
  }
  
  protected String numberListToIdentifier(TomNumberList l) {
    String res = (String)numberListToIdentifierMap.get(l);
    if(res == null) {
      TomNumberList key = l;
      StringBuffer buf = new StringBuffer(30);
      while(!l.isEmpty()) {
        TomNumber elt = l.getHead();
        buf.append("_");
        buf.append(elementToIdentifier(elt));
        l = l.getTail();
      }
      res = buf.toString();
      numberListToIdentifierMap.put(key,res);
    }
    return res;
  }

  protected boolean isListOperator(TomSymbol subject) {
    //%variable
    if(subject==null) {
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[option=Option(optionList)] -> {
        while(!optionList.isEmpty()) {
          Option opt = optionList.getHead();
          %match(Option opt) {
            DeclarationToOption(MakeEmptyList[]) -> { return true; }
            DeclarationToOption(MakeAddList[])   -> { return true; }
          }
          optionList = optionList.getTail();
        }
        return false;
      }
      
      _ -> {
        System.out.println("isListOperator: strange case: '" + subject + "'");
        System.exit(1);
      }
    }
    return false;
  }

  protected boolean isArrayOperator(TomSymbol subject) {
    //%variable
    if(subject==null) {
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[option=Option(optionList)] -> {
        while(!optionList.isEmpty()) {
          Option opt = optionList.getHead();
          %match(Option opt) {
            DeclarationToOption(MakeEmptyArray[]) -> { return true; }
            DeclarationToOption(MakeAddArray[])   -> { return true; }
          }
          optionList = optionList.getTail();
        }
        return false;
      }
      
      _ -> {
        System.out.println("isArrayOperator: strange case: '" + subject + "'");
        System.exit(1);
      }
    }
    return false;
  }

  protected TomList tomListMap(TomList subject, Replace1 replace) {
    TomList res = subject;
    try {
      if(!subject.isEmpty()) {
        TomTerm term = (TomTerm) replace.apply(subject.getHead());
        TomList list = tomListMap(subject.getTail(),replace);
        res = cons(term,list);
      }
    } catch(Exception e) {
      System.out.println("tomListMap error: " + e);
      e.printStackTrace();
      System.exit(0);
    }
    return res;
  }
  
    // ------------------------------------------------------------
  protected void collectVariable(final Collection collection, TomTerm subject) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
            //%variable
          if(t instanceof TomTerm) {
            TomTerm annotedVariable = null;
            %match(TomTerm t) { 
              Variable[option=Option(optionList)] |
              VariableStar[option=Option(optionList)] -> {
                collection.add(t);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
              }
              
              UnamedVariable[option=Option(optionList)] |
              UnamedVariableStar[option=Option(optionList)] -> {
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
              }
              
                // to collect annoted nodes but avoid collect variables in optionSymbol
              Appl[option=Option(optionList), args=subterms] -> {
                collectVariable(collection,`Tom(subterms));
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
              }
              
              _ -> { return true; }
            }
          } else {
            return true;
          }
        } // end apply
      }; // end new
    
    traversal().genericCollect(subject, collect);
  }

  protected boolean isAnnotedVariable(TomTerm t) {
    %match(TomTerm t) {
      Appl[option=Option(optionList)] |
      Variable[option=Option(optionList)] |
      VariableStar[option=Option(optionList)] |
      UnamedVariable[option=Option(optionList)] |
      UnamedVariableStar[option=Option(optionList)] -> {
        return getAnnotedVariable(optionList)!=null;
      }
    }
    return false;
  }
   
  protected TomTerm getAnnotedVariable(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,TomTermToOption(var@Variable[]),_*) -> { return var; }
    }
    return null;
  }
  
  protected Declaration getIsFsymDecl(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DeclarationToOption(decl@IsFsymDecl[]),_*) -> { return decl; }
    }
    return null;
  }

  protected String getDebug(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,Debug(Name(str)),_*) -> { return str; }
    }

    return null;
  }

  protected boolean hasConstructor(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,Constructor[],_*) -> { return true; }
    }
    return false;
  }
  
  protected boolean hasGeneratedMatch(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,GeneratedMatch(),_*) -> { return true; }
    }
    return false;
  }
  
  protected boolean hasDefaultCase(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DefaultCase(),_*) -> { return true; }
    }
    return false;
  }

  protected boolean hasDefinedSymbol(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DefinedSymbol(),_*) -> { return true; }
    }
    return false;
  }

  protected boolean hasImplicitXMLAttribut(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,ImplicitXMLAttribut(),_*) -> { return true; }
    }
    return false;
  }

  protected boolean hasImplicitXMLChild(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,ImplicitXMLChild(),_*) -> { return true; }
    }
    return false;
  } 
  
  protected TomName getSlotName(TomSymbol symbol, int number) {
    //%variable
    SlotList slotList = symbol.getSlotList();
    for(int index = 0; !slotList.isEmpty() && index<number ; index++) {
      slotList = slotList.getTail();
    }
    if(slotList.isEmpty()) {
      System.out.println("getSlotName: bad index error");
      System.exit(0);
    }

    Declaration decl = slotList.getHead().getSlotDecl();
    %match(Declaration decl) {
      GetSlotDecl[slotName=name] -> { return name; }
    }
    return null;
  }
  
  protected int getSlotIndex(SlotList slotList, TomName slotName) {
    //%variable
    int index = 0;
    while(!slotList.isEmpty()) {
      TomName name = slotList.getHead().getSlotName();
        // System.out.println("index = " + index + " name = " + name);
      if(slotName.equals(name)) {
        return index; 
      }
      slotList = slotList.getTail();
      index++;
    }
    return -1;
  }

  protected boolean isDefinedSymbol(TomSymbol subject) {
      //%variable
    if(subject==null) {
      System.out.println("isDefinedSymbol: subject == null");
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[option=Option(optionList)] -> {
        return hasDefinedSymbol(optionList);
      }
    }
    return false;
  }
  
    // findOriginTracking(_) return the option containing OriginTracking information
  protected Option findOriginTracking(OptionList optionList) {
    if(optionList.isEmpty()) {
      return ast().makeOption();
    }
    while(!optionList.isEmpty()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        orgTrack@OriginTracking[] -> {
          return orgTrack;
        }
      }
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTracking:  not found" + optionList);
      //System.exit(1);
    return null;
  }
   
  protected void addError(TomTaskInput taskInput, String msg, String file, int line, int level) {
    TomError err = `Error(msg,file,line,level);
    taskInput.setErrors(tsf().makeTomErrorList(err, taskInput.getErrors()));
  }
}
