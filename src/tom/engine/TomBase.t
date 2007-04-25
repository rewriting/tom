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

package tom.engine;

import java.util.*;

import aterm.*;

import tom.engine.tools.*;

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
import tom.engine.adt.theory.types.*;

import tom.engine.exception.TomRuntimeException;

import tom.platform.adt.platformoption.*;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;


/**
 * Base class for most tom files in the compiler.
 * Provides access to the TomSignatureFactory and helper methods.
 */
public final class TomBase {

  %include { adt/tomsignature/TomSignature.tom }
  %include { mustrategy.tom }
  %typeterm Collection {
    implement { java.util.Collection }
  }

 public final static String DEFAULT_MODULE_NAME = "default"; 
  
  /** shortcut */
  
  %include { adt/platformoption/PlatformOption.tom }
  
  public static TomNumberList appendNumber(int n, TomNumberList path) {
    /*
     * concTomNumber(path*,Position(n))
     * can be used instead of 
     * concTomNumber(path*,NameNumber(PositionName(concTomNumber(Position(n)))))
     * thanks to hooks defined in TomName.gom
     */
    return `concTomNumber(path*,Position(n));
    //return `concTomNumber(path*,NameNumber(PositionName(concTomNumber(Position(n)))));
  }
    
  public static String getTomType(TomType type) {
    %match(TomType type) {
      ASTTomType(s) -> {return `s;}
      TomTypeAlone(s) -> {return `s;}
      Type(ASTTomType(s),_) -> {return `s;}
      EmptyType() -> {return null;}
    }
    System.out.println("getTomType error on term: " + type);
    throw new TomRuntimeException("getTomType error on term: " + type);
  }

  public static String getTLType(TomType type) {
    %match(TomType type) {
      TLType[]  -> { return getTLCode(type); }
      Type[TlType=tlType] -> { return getTLCode(`tlType); }
    }
    throw new TomRuntimeException("getTLType error on term: " + type);
  }

  public static String getTLCode(TomType type) {
    %match(TomType type) {
      TLType(TL[Code=tlType])  -> { return `tlType; }
      TLType(ITL[Code=tlType]) -> { return `tlType; }
    }
    System.out.println("getTLCode error on term: " + type);
    throw new TomRuntimeException("getTLCode error on term: " + type);
  }

  public static TomType getSymbolCodomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getCodomain();
    } else {
      return `EmptyType();
    }
  }   

  public static TomTypeList getSymbolDomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getDomain();
    } else {
      return `concTomType();
    }
  }

  private static HashMap tomNumberListToStringMap = new HashMap();
  public static String tomNumberListToString(TomNumberList numberList) {
    String result = (String)tomNumberListToStringMap.get(numberList);
    if(result == null) {
      TomNumberList key = numberList;
      StringBuffer buf = new StringBuffer(30);
      while(!numberList.isEmptyconcTomNumber()) {
	TomNumber number = numberList.getHeadconcTomNumber();
	numberList = numberList.getTailconcTomNumber();
	%match(number) {
	  Position(n) -> {
	    buf.append("Position");
	    buf.append(Integer.toString(`n));
	  }
	  MatchNumber(n) -> {
	    buf.append("Match");
	    buf.append(Integer.toString(`n));
	  }
	  PatternNumber(n) -> {
	    buf.append("Pattern");
	    buf.append(Integer.toString(`n));
	  }
	  ListNumber(n) -> {
	    buf.append("List");
	    buf.append(Integer.toString(`n));
	  }
	  IndexNumber(n) -> {
	    buf.append("Index");
	    buf.append(Integer.toString(`n));
	  }
	  Begin(n) -> {
	    buf.append("Begin");
	    buf.append(Integer.toString(`n));
	  }
	  End(n) -> {
	    buf.append("End");
	    buf.append(Integer.toString(`n));
	  }
	  Save(n) -> {
	    buf.append("Save");
	    buf.append(Integer.toString(`n));
	  }
	  AbsVar(n) -> {
	    buf.append("AbsVar");
	    buf.append(Integer.toString(`n));
	  }
	  RenamedVar(tomName) -> {
	    String identifier = "Empty";
	    %match(TomName tomName) {
	      Name(name) -> {
		identifier = `name;
	      }
	      PositionName(localNumberList) -> {
		identifier = tomNumberListToString(`localNumberList);
	      }
	    }
	    buf.append("RenamedVar");
	    buf.append(identifier);
	  }
	  NameNumber(tomName) -> {
	    String identifier = "Empty";
	    %match(TomName tomName) {
	      Name(name) -> {
		identifier = `name;
	      }
	      PositionName(localNumberList) -> {
		identifier = tomNumberListToString(`localNumberList);
	      }
	    }
	    buf.append("NameNumber");
	    buf.append(identifier);
	  }
	}
      }
      result = buf.toString();
      tomNumberListToStringMap.put(key,result);
    }
    return result;
  }

  public static boolean isListOperator(TomSymbol subject) {
    if(subject==null) {
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[Option=l] -> {
        OptionList optionList = `l;
        while(!optionList.isEmptyconcOption()) {
          Option opt = optionList.getHeadconcOption();
          %match(Option opt) {
            DeclarationToOption(MakeEmptyList[]) -> { return true; }
            DeclarationToOption(MakeAddList[])   -> { return true; }
          }
          optionList = optionList.getTailconcOption();
        }
        return false;
      }
    }
    System.out.println("isListOperator: strange case: '" + subject + "'");
    throw new TomRuntimeException("isListOperator: strange case: '" + subject + "'");
  }

  public static boolean isArrayOperator(TomSymbol subject) {
    if(subject==null) {
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[Option=l] -> {
        OptionList optionList = `l;
        while(!optionList.isEmptyconcOption()) {
          Option opt = optionList.getHeadconcOption();
          %match(Option opt) {
            DeclarationToOption(MakeEmptyArray[]) -> { return true; }
            DeclarationToOption(MakeAddArray[])   -> { return true; }
          }
          optionList = optionList.getTailconcOption();
        }
        return false;
      }
    }
    System.out.println("isArrayOperator: strange case: '" + subject + "'");
    throw new TomRuntimeException("isArrayOperator: strange case: '" + subject + "'");
  }
  
  public static boolean isSyntacticOperator(TomSymbol subject) {
    return (!(isListOperator(subject) || isArrayOperator(subject)));
  }

  // ------------------------------------------------------------
  public static void collectVariable(Collection collection, jjtraveler.Visitable subject) {
    `TopDownCollect(collectVariable(collection)).apply(`subject);
  }

  %strategy collectVariable(collection:Collection) extends `Identity() {
    visit TomTerm {
      v@(Variable|VariableStar)[Constraints=constraintList] -> {
        collection.add(`v);
        TomTerm annotedVariable = getAssignToVariable(`constraintList);
        if(annotedVariable!=null) {
          collection.add(annotedVariable);
        }
        `Fail().visit(`v);
      }

      v@(UnamedVariable|UnamedVariableStar)[Constraints=constraintList] -> {
        TomTerm annotedVariable = getAssignToVariable(`constraintList);
        if(annotedVariable!=null) {
          collection.add(annotedVariable);
        }
        `Fail().visit(`v);
      }

      // to collect annoted nodes but avoid collect variables in optionSymbol
      t@RecordAppl[Slots=subterms, Constraints=constraintList] -> {
        collectVariable(collection,`subterms);
        TomTerm annotedVariable = getAssignToVariable(`constraintList);
        if(annotedVariable!=null) {
          collection.add(annotedVariable);
        }
        `Fail().visit(`t);
      }

    }
  }

  public static Map collectMultiplicity(jjtraveler.Visitable subject) {
    // collect variables
    ArrayList variableList = new ArrayList();
    collectVariable(variableList,`subject);
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

  public static TomTerm getAssignToVariable(ConstraintList constraintList) {
    %match(ConstraintList constraintList) {
      concConstraint(_*,AssignTo(var@Variable[]),_*) -> { return `var; }
    }
    return null;
  }

  public static boolean hasTheory(Theory theory, ElementaryTheory elementaryTheory) {
    %match(theory) {
      concElementaryTheory(_*,x,_*) -> { if(`x==elementaryTheory) return true; }
    }
    return false;
  }
 
  public static Theory getTheory(TomTerm term) {
    %match(term) {
      RecordAppl[Option=concOption(_*,MatchingTheory(theory),_*)] -> { return `theory; }
    }
    return `concElementaryTheory(Syntactic());
  }
  public static Theory getTheory(OptionList optionList) {
    %match(optionList) {
      concOption(_*,MatchingTheory(theory),_*) -> { return `theory; }
    }
    return `concElementaryTheory(Syntactic());
  }

  public static Declaration getIsFsymDecl(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DeclarationToOption(decl@IsFsymDecl[]),_*) -> { return `decl; }
    }
    return null;
  }

  public static boolean hasIsFsymDecl(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Option=concOption(_*,DeclarationToOption(decl@IsFsymDecl[]),_*)] -> {
        return true;
      }
    }
    return false;
  }
  
  
  public static String getModuleName(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,ModuleName(moduleName),_*) -> { return `moduleName; }
    }
    return null;
  }

  public static boolean hasConstant(OptionList optionList) {
    %match(optionList) {
      concOption(_*,Constant[],_*) -> { return true; }
    }
    return false;
  }

  public static boolean hasDefinedSymbol(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DefinedSymbol(),_*) -> { return true; }
    }
    return false;
  }

  public static boolean hasImplicitXMLAttribut(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,ImplicitXMLAttribut(),_*) -> { return true; }
    }
    return false;
  }

  public static boolean hasImplicitXMLChild(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,ImplicitXMLChild(),_*) -> { return true; }
    }
    return false;
  } 

  public static TomName getSlotName(TomSymbol symbol, int number) {
    PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
    for(int index = 0; !pairNameDeclList.isEmptyconcPairNameDecl() && index<number ; index++) {
      pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
    }
    if(pairNameDeclList.isEmptyconcPairNameDecl()) {
      System.out.println("getSlotName: bad index error");
      throw new TomRuntimeException("getSlotName: bad index error");
    }
    PairNameDecl pairNameDecl = pairNameDeclList.getHeadconcPairNameDecl();

    Declaration decl = pairNameDecl.getSlotDecl();
    %match(Declaration decl) {
      GetSlotDecl[SlotName=name] -> { return `name; }
    }

    return pairNameDecl.getSlotName();
  }

  public static int getSlotIndex(TomSymbol tomSymbol, TomName slotName) {
    int index = 0;
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
    while(!pairNameDeclList.isEmptyconcPairNameDecl()) {
      TomName name = pairNameDeclList.getHeadconcPairNameDecl().getSlotName();
      // System.out.println("index = " + index + " name = " + name);
      if(slotName.equals(name)) {
        return index; 
      }
      pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
      index++;
    }
    return -1;
  }

  public static TomType elementAt(TomTypeList l, int index) {
    if (0 > index || index > l.length()) {
      throw new IllegalArgumentException("illegal list index: " + index);
    }
    for (int i = 0; i < index; i++) {
      l = l.getTailconcTomType();
    }
    return l.getHeadconcTomType();
  }

  public static TomType getSlotType(TomSymbol symbol, TomName slotName) {
    %match(TomSymbol symbol) {
      Symbol[TypesToType=TypesToType(typeList,codomain)] -> {
        int index = getSlotIndex(symbol,slotName);
        return elementAt(`typeList,index);
      }
    }
    throw new TomRuntimeException("getSlotType: bad slotName error");
  }

  public static boolean isDefinedSymbol(TomSymbol subject) {
    if(subject==null) {
      System.out.println("isDefinedSymbol: subject == null");
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[Option=optionList] -> {
        return hasDefinedSymbol(`optionList);
      }
    }
    return false;
  }

  public static boolean isDefinedGetSlot(TomSymbol symbol, TomName slotName) {
    if(symbol==null) {
      System.out.println("isDefinedSymbol: symbol == null");
      return false;
    }
    %match(TomSymbol symbol) {
      Symbol[PairNameDeclList=concPairNameDecl(_*,PairNameDecl[SlotName=name,SlotDecl=decl],_*)] -> {
        if(`name==slotName && `decl!=`EmptyDeclaration()) {
          return true;
        }
      }
    }
    return false;
  }


  // findOriginTracking(_) return the option containing OriginTracking information
  public static Option findOriginTracking(OptionList optionList) {
    if(optionList.isEmptyconcOption()) {
      return `noOption();
    }
    while(!optionList.isEmptyconcOption()) {
      Option subject = optionList.getHeadconcOption();
      %match(Option subject) {
        orgTrack@OriginTracking[] -> {
          return `orgTrack;
        }
      }
      optionList = optionList.getTailconcOption();
    }
    System.out.println("findOriginTracking:  not found" + optionList);
    throw new TomRuntimeException("findOriginTracking:  not found" + optionList);
  }

  public static TomSymbol getSymbolFromName(String tomName, SymbolTable symbolTable) {
    return symbolTable.getSymbolFromName(tomName);
  }
  
  public static TomSymbol getSymbolFromType(TomType tomType, SymbolTable symbolTable) {
    TomSymbolList list = symbolTable.getSymbolFromType(tomType);
    TomSymbolList filteredList = `concTomSymbol();
    // Not necessary since checker ensure the uniqueness of the symbol
    while(!list.isEmptyconcTomSymbol()) {
      TomSymbol head = list.getHeadconcTomSymbol();
      if(isArrayOperator(head) || isListOperator(head)) {
        filteredList = `concTomSymbol(head,filteredList*);
      }
      list = list.getTailconcTomSymbol();
    }
    return filteredList.getHeadconcTomSymbol();
  }

  public static TomType getTermType(TomTerm t, SymbolTable symbolTable) {
    %match(TomTerm t) {
      (TermAppl|RecordAppl)[NameList=(headName,_*)] -> {
        String tomName = null;
        if(`(headName) instanceof AntiName) {
          tomName = ((AntiName)`headName).getName().getString(); 
        } else {
          tomName = ((TomName)`headName).getString();
        }
        TomSymbol tomSymbol = symbolTable.getSymbolFromName(tomName);
        if(tomSymbol!=null) {
          return tomSymbol.getTypesToType().getCodomain();
        } else {
          return `EmptyType();
        }
      }

      (Variable|VariableStar|UnamedVariable|UnamedVariableStar)[AstType=type] -> { 
        return `type; 
      }

      Ref(term) -> { return getTermType(`term, symbolTable); }

      TargetLanguageToTomTerm[Tl=(TL|ITL)[]] -> { return `EmptyType(); }

      FunctionCall[AstType=type] -> { return `type; }

      AntiTerm(term) -> { return getTermType(`term,symbolTable);}
      
      ExpressionToTomTerm(GetSlot[Codomain=type]) -> { return `type; } 
      
      Subterm(Name(name), slotName, term) -> {
        TomSymbol tomSymbol = symbolTable.getSymbolFromName(name);
        return getSlotType(tomSymbol, `slotName);
      }
    }
    //System.out.println("getTermType error on term: " + t);
    //throw new TomRuntimeException("getTermType error on term: " + t);
    return `EmptyType();
  }
  
  public static TomType getTermType(Expression t, SymbolTable symbolTable){
    %match(Expression t) {
      (GetHead|GetSlot|GetElement)[Codomain=type] -> { return `type; }

      TomTermToExpression(term) -> { return getTermType(`term, symbolTable); }
      GetTail[Variable=term] -> { return getTermType(`term, symbolTable); }
      GetSliceList[VariableBeginAST=term] -> { return getTermType(`term, symbolTable); }
      GetSliceArray[SubjectListName=term] -> { return getTermType(`term, symbolTable); }
    }
    System.out.println("getTermType error on term: " + t);
    throw new TomRuntimeException("getTermType error on term: " + t);
  }

  public static SlotList tomListToSlotList(TomList tomList) {
    return tomListToSlotList(tomList,1);
  }

  public static SlotList tomListToSlotList(TomList tomList, int index) {
    %match(TomList tomList) {
      concTomTerm() -> { return `concSlot(); }
      concTomTerm(head,tail*) -> { 
        TomName slotName = `PositionName(concTomNumber(Position(index)));
        SlotList sl = tomListToSlotList(`tail,index+1);
        return `concSlot(PairSlotAppl(slotName,head),sl*); 
      }
    }
    throw new TomRuntimeException("tomListToSlotList: " + tomList);
  }

  public static SlotList mergeTomListWithSlotList(TomList tomList, SlotList slotList) {
    %match(TomList tomList, SlotList slotList) {
      concTomTerm(), concSlot() -> { 
        return `concSlot(); 
      }
      concTomTerm(head,tail*), concSlot(PairSlotAppl[SlotName=slotName],tailSlotList*) -> { 
        SlotList sl = mergeTomListWithSlotList(`tail,`tailSlotList);
        return `concSlot(PairSlotAppl(slotName,head),sl*); 
      }
    }
    throw new TomRuntimeException("mergeTomListWithSlotList: " + tomList + " and " + slotList);
  }

  public static TomList slotListToTomList(SlotList tomList) {
    %match(SlotList tomList) {
      concSlot() -> { return `concTomTerm(); }
      concSlot(PairSlotAppl[Appl=head],tail*) -> {
        TomList tl = slotListToTomList(`tail);
        return `concTomTerm(head,tl*);
      }
    }
    throw new TomRuntimeException("slotListToTomList: " + tomList);
  }

} // class TomBase
