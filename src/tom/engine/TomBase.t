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

package jtom;

import java.util.*;

import aterm.*;

import jtom.tools.*;
import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.exception.TomRuntimeException;

import tom.platform.adt.platformoption.*;
import tom.library.traversal.*;

import jjtraveler.reflective.VisitableVisitor;

/**
 * Base class for most tom files in the compiler.
 * Provides access to the TomSignatureFactory and helper methods.
 */
public class TomBase {

  %include { adt/tomsignature/TomSignature.tom }
  %include{ mutraveler.tom }

  public static final TomSignatureFactory getTomSignatureFactory() {
    return TomEnvironment.getInstance().getTomSignatureFactory();
  }
  /** shortcut */
  protected static TomSignatureFactory tsf() {
    return TomEnvironment.getInstance().getTomSignatureFactory();
  }
  
  %include { adt/platformoption/PlatformOption.tom }
  
  public static final PlatformOptionFactory getPlatformOptionFactory() {
    return TomEnvironment.getInstance().getPlatformOptionFactory();
  }
  
  public static final ASTFactory getAstFactory() {
    return TomEnvironment.getInstance().getAstFactory();
  }
  
  private TomList empty;
  private GenericTraversal traversal;
  
  public TomBase() {
    this.empty = getTomSignatureFactory().makeTomList();
    this.traversal = new GenericTraversal();
  }

  /**
   * Gets the TomEnvironment instance.
   * @return the TomEnvironment instance
   */    
  protected TomEnvironment environment() {
    return TomEnvironment.getInstance();
  }
  
  public GenericTraversal traversal() {
    return this.traversal;
  }
  
  protected TomNumber makeNumber(int n) {
    return tsf().makeTomNumber_Number(n);
  }
  
  protected OptionList emptyOption() {
    return getAstFactory().makeOption();
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

  protected String getTomType(TomType type) {
    %match(TomType type) {
      ASTTomType(s) -> {return `s;}
      TomTypeAlone(s) -> {return `s;}
      Type(ASTTomType(s),_) -> {return `s;}
      EmptyType() -> {return null;}
      _ -> {
        System.out.println("getTomType error on term: " + type);
        throw new TomRuntimeException("getTomType error on term: " + type);
      }
    }
  }

  protected String getTLType(TomType type) {
    %match(TomType type) {
      TLType[]  -> { return getTLCode(type); }
      Type[tlType=tlType] -> { return getTLCode(`tlType); }
      _ -> {
        throw new TomRuntimeException("getTLType error on term: " + type);
      }
    }
  }

  protected String getTLCode(TomType type) {
    %match(TomType type) {
      TLType(TL[code=tlType])  -> { return `tlType; }
      TLType(ITL[code=tlType]) -> { return `tlType; }
      _ -> {
        System.out.println("getTLCode error on term: " + type);
        throw new TomRuntimeException("getTLCode error on term: " + type);
      }
    }
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

  private HashMap numberListToIdentifierMap = new HashMap();

  private String elementToIdentifier(TomNumber subject) {
    %match(TomNumber subject) {
      Begin(Number(i)) -> { return "_begin" + `i; }
      End(Number(i)) -> { return "_end" + `i; }
      MatchNumber(Number(i)) -> { return "_match" + `i; }
      PatternNumber(Number(i)) -> { return "_pattern" + `i; }
      ListNumber(Number(i)) -> { return "_list" + `i; }
      IndexNumber(Number(i)) -> { return "_index" + `i; }
      AbsVar(Number(i)) -> { return "_absvar" + `i; }
      RenamedVar(Name(name)) -> { return "_renamedvar_" + `name; }
      NameNumber(Name(name)) -> { return "_" + `name; }
      NameNumber(PositionName(numberList)) -> { return numberListToIdentifier(numberList); }
      RuleVar() -> { return "_rulevar"; }
      Number(i) -> { return "_" + `i; }
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
        //buf.append("_");
        buf.append(elementToIdentifier(elt));
        l = l.getTail();
      }
      res = buf.toString();
      numberListToIdentifierMap.put(key,res);
    }
    return res;
  }

  protected boolean isListOperator(TomSymbol subject) {
    if(subject==null) {
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[option=l] -> {
        OptionList optionList = `l;
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
        throw new TomRuntimeException("isListOperator: strange case: '" + subject + "'");
      }
    }
  }

  protected boolean isArrayOperator(TomSymbol subject) {
    //%variable
    if(subject==null) {
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[option=l] -> {
        OptionList optionList = `l;
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
        throw new TomRuntimeException("isArrayOperator: strange case: '" + subject + "'");
      }
    }
  }

  protected boolean isStringOperator(TomSymbol subject) {
    if(subject==null) { return false; }
    TomType type = subject.getTypesToType().getCodomain();
    %match(TomType type) {
      TomTypeAlone("String") -> {
        return true;
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
      throw new TomRuntimeException("tomListMap error: " + e);
    }
    return res;
  }

  protected InstructionList instructionListMap(InstructionList subject, Replace1 replace) {
    InstructionList res = subject;
    try {
      if(!subject.isEmpty()) {
        Instruction term = (Instruction) replace.apply(subject.getHead());
        InstructionList list = instructionListMap(subject.getTail(),replace);
        res = `manyInstructionList(term,list);
      }
    } catch(Exception e) {
      System.out.println("instructionListMap error: " + e);
      e.printStackTrace();
      throw new TomRuntimeException("instructionListMap error: " + e);
    }
    return res;
  }
  // ------------------------------------------------------------
  protected void collectVariable(final Collection collection, ATerm subject) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          if(t instanceof TomTerm) {
            TomTerm annotedVariable = null;
            %match(TomTerm t) { 
              (Variable|VariableStar)[constraints=constraintList] -> {
                collection.add(t);
                annotedVariable = getAssignToVariable(`constraintList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
              }
                
              (UnamedVariable|UnamedVariableStar)[constraints=constraintList] -> {
                annotedVariable = getAssignToVariable(`constraintList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
              }

              // to collect annoted nodes but avoid collect variables in optionSymbol
              RecordAppl[slots=subterms, constraints=constraintList] -> {
                collectVariable(collection,`subterms);
                annotedVariable = getAssignToVariable(`constraintList);
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

  protected Map collectMultiplicity(ATerm subject) {
    // collect variables
    ArrayList variableList = new ArrayList();
    collectVariable(variableList,subject);
    // compute multiplicities
    HashMap multiplicityMap = new HashMap();
    Iterator it = variableList.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      if(multiplicityMap.containsKey(name)) {
        Integer value = (Integer)multiplicityMap.get(name);
        multiplicityMap.put(name, new Integer(1+value.intValue()));
      } else {
        multiplicityMap.put(name, new Integer(1));
      }
    }
    return multiplicityMap;
  }

  protected boolean isAnnotedVariable(TomTerm t) {
    %match(TomTerm t) {
      (RecordAppl|Variable|VariableStar|UnamedVariable|UnamedVariableStar)[constraints=constraintList] -> {
        return getAssignToVariable(`constraintList)!=null;
      }
    }
    return false;
  }

  protected TomTerm getAssignToVariable(ConstraintList constraintList) {
    %match(ConstraintList constraintList) {
      concConstraint(_*,AssignTo(var@Variable[]),_*) -> { return `var; }
    }
    return null;
  }

  protected Declaration getIsFsymDecl(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DeclarationToOption(decl@IsFsymDecl[]),_*) -> { return `decl; }
    }
    return null;
  }

  protected String getDebug(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,Debug(Name(str)),_*) -> { return `str; }
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

  protected boolean hasGetHead(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DeclarationToOption(GetHeadDecl[]),_*) -> { return true; }
    }
    return false;
  } 

  protected boolean hasGetTail(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DeclarationToOption(GetTailDecl[]),_*) -> { return true; }
    }
    return false;
  } 

  protected boolean hasIsEmpty(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DeclarationToOption(IsEmptyDecl[]),_*) -> { return true; }
    }
    return false;
  } 

  protected TomName getSlotName(TomSymbol symbol, int number) {
    PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
    for(int index = 0; !pairNameDeclList.isEmpty() && index<number ; index++) {
      pairNameDeclList = pairNameDeclList.getTail();
    }
    if(pairNameDeclList.isEmpty()) {
      System.out.println("getSlotName: bad index error");
      throw new TomRuntimeException("getSlotName: bad index error");
    }

    Declaration decl = pairNameDeclList.getHead().getSlotDecl();
    %match(Declaration decl) {
      GetSlotDecl[slotName=name] -> { return `name; }
    }
    return null;
  }

  protected int getSlotIndex(TomSymbol tomSymbol, TomName slotName) {
    int index = 0;
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
    while(!pairNameDeclList.isEmpty()) {
      TomName name = pairNameDeclList.getHead().getSlotName();
      // System.out.println("index = " + index + " name = " + name);
      if(slotName.equals(name)) {
        return index; 
      }
      pairNameDeclList = pairNameDeclList.getTail();
      index++;
    }
    return -1;
  }

  protected TomType getSlotType(TomSymbol symbol, TomName slotName) {
    %match(TomSymbol symbol) {
      Symbol[typesToType=TypesToType(typeList,codomain)] -> {
        int index = getSlotIndex(symbol,slotName);
        return (TomType)typeList.elementAt(index);
      }
    }
    throw new TomRuntimeException("getSlotType: bad slotName error");
  }

  protected boolean isDefinedSymbol(TomSymbol subject) {
    if(subject==null) {
      System.out.println("isDefinedSymbol: subject == null");
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[option=optionList] -> {
        return hasDefinedSymbol(`optionList);
      }
    }
    return false;
  }

  protected boolean isDefinedGetSlot(TomSymbol symbol, TomName slotName) {
    if(symbol==null) {
      System.out.println("isDefinedSymbol: symbol == null");
      return false;
    }
    %match(TomSymbol symbol) {
      Symbol[pairNameDeclList=concPairNameDecl(_*,PairNameDecl[slotName=name,slotDecl=decl],_*)] -> {
        if(name==slotName && decl!=`EmptyDeclaration()) {
          return true;
        }
      }
    }
    return false;
  }


  // findOriginTracking(_) return the option containing OriginTracking information
  protected Option findOriginTracking(OptionList optionList) {
    if(optionList.isEmpty()) {
      return `noOption();
    }
    while(!optionList.isEmpty()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        orgTrack@OriginTracking[] -> {
          return `orgTrack;
        }
      }
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTracking:  not found" + optionList);
    throw new TomRuntimeException("findOriginTracking:  not found" + optionList);
  }

  protected TomSymbol getSymbolFromName(String tomName, SymbolTable symbolTable) {
    return symbolTable.getSymbolFromName(tomName);
  }

  
  protected TomSymbol getSymbolFromType(TomType tomType, SymbolTable symbolTable) {
    SymbolList list = symbolTable.getSymbolFromType(tomType);
    SymbolList filteredList = `emptySymbolList();
    // Not necessary since checker ensure the uniqueness of the symbol
    while(!list.isEmpty()) {
      TomSymbol head = list.getHead();
      if(isArrayOperator(head) || isListOperator(head)) {
        filteredList = `manySymbolList(head,filteredList);
      }
      list = list.getTail();
    }
    return filteredList.getHead();
  }

  protected TomType getTermType(TomTerm t, SymbolTable symbolTable){
    %match(TomTerm t) {
      RecordAppl[nameList=(Name(tomName),_*)] -> {
        TomSymbol tomSymbol = symbolTable.getSymbolFromName(`tomName);
        return tomSymbol.getTypesToType().getCodomain();
      }

      (Variable|VariableStar|UnamedVariable|UnamedVariableStar)[astType=type] -> { 
        return `type; 
      }

      Ref(term) -> { return getTermType(`term, symbolTable); }

      TargetLanguageToTomTerm[tl=(TL|ITL)[]] -> { return `EmptyType(); }

      FunctionCall[] -> { return `EmptyType(); }

      _ -> {
        System.out.println("getTermType error on term: " + t);
        throw new TomRuntimeException("getTermType error on term: " + t);
      }
    }
  }
  
  protected TomType getTermType(Expression t, SymbolTable symbolTable){
    %match(Expression t) {
      (GetSubterm|GetHead|GetSlot|GetElement)[codomain=type] -> { return `type; }

      TomTermToExpression(term) -> { return getTermType(`term, symbolTable); }
      GetTail[variable=term] -> { return getTermType(`term, symbolTable); }
      GetSliceList[variableBeginAST=term] -> { return getTermType(`term, symbolTable); }
      GetSliceArray[subjectListName=term] -> { return getTermType(`term, symbolTable); }
        
      _ -> {
        System.out.println("getTermType error on term: " + t);
        throw new TomRuntimeException("getTermType error on term: " + t);
      }
    }
  }

  protected SlotList tomListToSlotList(TomList tomList) {
    return tomListToSlotList(tomList,1);
  }

  protected SlotList tomListToSlotList(TomList tomList, int index) {
    %match(TomList tomList) {
      emptyTomList() -> { return `emptySlotList(); }
      manyTomList(head,tail) -> { 
        TomName slotName = `PositionName(concTomNumber(Number(index)));
        return `manySlotList(PairSlotAppl(slotName,head),tomListToSlotList(tail,index+1)); 
      }
    }
    throw new TomRuntimeException("tomListToSlotList: " + tomList);
  }

  protected SlotList mergeTomListWithSlotList(TomList tomList, SlotList slotList) {
    %match(TomList tomList, SlotList slotList) {
      emptyTomList(), emptySlotList() -> { 
        return `emptySlotList(); 
      }
      manyTomList(head,tail), manySlotList(PairSlotAppl[slotName=slotName],tailSlotList) -> { 
        return `manySlotList(PairSlotAppl(slotName,head),mergeTomListWithSlotList(tail,tailSlotList)); 
      }
    }
    throw new TomRuntimeException("mergeTomListWithSlotList: " + tomList + " and " + slotList);
  }

  protected TomList slotListToTomList(SlotList tomList) {
    %match(SlotList tomList) {
      emptySlotList() -> { return `emptyTomList(); }
      manySlotList(PairSlotAppl[appl=head],tail) -> { return `manyTomList(head,slotListToTomList(tail)); }
    }
    throw new TomRuntimeException("slotListToTomList: " + tomList);
  }

} // class TomBase
