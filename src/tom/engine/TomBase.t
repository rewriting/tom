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
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.exception.TomRuntimeException;

import tom.platform.adt.platformoption.*;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;


/**
 * Base class for most tom files in the compiler.
 * Provides access to the TomSignatureFactory and helper methods.
 */
public class TomBase {

  %include { adt/tomsignature/TomSignature.tom }
  %include { mustrategy.tom }
	%typeterm Collection {
		implement { java.util.Collection }
	}

 public final static String DEFAULT_MODULE_NAME = "default"; 
  
  /** shortcut */
  
  %include { adt/platformoption/PlatformOption.tom }
  
  protected static TomList cons(TomTerm t, TomList l) {
    if(t!=null) {
      return `concTomTerm(t,l*);
    } else {
      System.out.println("cons: Warning t == null");
      return l;
    }
  }

  protected static TomNumberList appendNumber(int n, TomNumberList path) {
    return `concTomNumber(path*,Number(n));
  }
    
  protected static TomList append(TomTerm t, TomList l) {
    if(l.isEmptyconcTomTerm()) {
      return cons(t,l);
    } else {
      return cons(l.getHeadconcTomTerm(), append(t,l.getTailconcTomTerm()));
    }
  }

  protected static TomList concat(TomList l1, TomList l2) {
    if(l1.isEmptyconcTomTerm()) {
      return l2;
    } else {
      return cons(l1.getHeadconcTomTerm(), concat(l1.getTailconcTomTerm(),l2));
    }
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

  protected static String getTLType(TomType type) {
    %match(TomType type) {
      TLType[]  -> { return getTLCode(type); }
      Type[TlType=tlType] -> { return getTLCode(`tlType); }
    }
		throw new TomRuntimeException("getTLType error on term: " + type);
  }

  protected static String getTLCode(TomType type) {
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
      //System.out.println("getSymbolCodomain: symbol = " + symbol);
      return `EmptyType();
    }
  }   

  protected static TomTypeList getSymbolDomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getDomain();
    } else {
      //System.out.println("getSymbolDomain: symbol = " + symbol);
      return `concTomType();
    }
  }

  private static HashMap numberListToIdentifierMap = new HashMap();

  private static String elementToIdentifier(TomNumber subject) {
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
      NameNumber(PositionName(numberList)) -> { return `numberListToIdentifier(numberList); }
      RuleVar() -> { return "_rulevar"; }
      Number(i) -> { return "_" + `i; }
    }
		return subject.toString(); 
  }

  protected static String numberListToIdentifier(TomNumberList l) {
    String res = (String)numberListToIdentifierMap.get(l);
    if(res == null) {
      TomNumberList key = l;
      StringBuffer buf = new StringBuffer(30);
      while(!l.isEmptyconcTomNumber()) {
        TomNumber elt = l.getHeadconcTomNumber();
        //buf.append("_");
        buf.append(elementToIdentifier(elt));
        l = l.getTailconcTomNumber();
      }
      res = buf.toString();
      numberListToIdentifierMap.put(key,res);
    }
    return res;
  }

  protected static boolean isListOperator(TomSymbol subject) {
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

  protected static boolean isArrayOperator(TomSymbol subject) {
    //%variable
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

  protected boolean isAnnotedVariable(TomTerm t) {
    %match(TomTerm t) {
      (RecordAppl|Variable|VariableStar|UnamedVariable|UnamedVariableStar)[Constraints=constraintList] -> {
        return getAssignToVariable(`constraintList)!=null;
      }
    }
    return false;
  }

  public static TomTerm getAssignToVariable(ConstraintList constraintList) {
    %match(ConstraintList constraintList) {
      concConstraint(_*,AssignTo(var@Variable[]),_*) -> { return `var; }
    }
    return null;
  }

  protected static Declaration getIsFsymDecl(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DeclarationToOption(decl@IsFsymDecl[]),_*) -> { return `decl; }
    }
    return null;
  }
  
	protected static String getModuleName(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,ModuleName(moduleName),_*) -> { return `moduleName; }
    }
    return null;
  }

  protected static String getDebug(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,Debug(Name(str)),_*) -> { return `str; }
    }
    return null;
  }

  protected static boolean hasGeneratedMatch(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,GeneratedMatch(),_*) -> { return true; }
    }
    return false;
  }

  protected static boolean hasConstant(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,Constant[],_*) -> { return true; }
    }
    return false;
  }

  protected static boolean hasDefinedSymbol(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,DefinedSymbol(),_*) -> { return true; }
    }
    return false;
  }

  protected static boolean hasImplicitXMLAttribut(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,ImplicitXMLAttribut(),_*) -> { return true; }
    }
    return false;
  }

  protected static boolean hasImplicitXMLChild(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,ImplicitXMLChild(),_*) -> { return true; }
    }
    return false;
  } 

  /*
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
*/

  protected static TomName getSlotName(TomSymbol symbol, int number) {
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

  protected static int getSlotIndex(TomSymbol tomSymbol, TomName slotName) {
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

  protected static boolean isDefinedSymbol(TomSymbol subject) {
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

  protected static boolean isDefinedGetSlot(TomSymbol symbol, TomName slotName) {
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
  protected static Option findOriginTracking(OptionList optionList) {
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

  protected static TomSymbol getSymbolFromName(String tomName, SymbolTable symbolTable) {
    return symbolTable.getSymbolFromName(tomName);
  }
  
  protected static TomSymbol getSymbolFromType(TomType tomType, SymbolTable symbolTable) {
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
      RecordAppl[NameList=(Name(tomName),_*)] -> {
        TomSymbol tomSymbol = symbolTable.getSymbolFromName(`tomName);
        return tomSymbol.getTypesToType().getCodomain();
      }

      (Variable|VariableStar|UnamedVariable|UnamedVariableStar)[AstType=type] -> { 
        return `type; 
      }

      Ref(term) -> { return getTermType(`term, symbolTable); }

      TargetLanguageToTomTerm[Tl=(TL|ITL)[]] -> { return `EmptyType(); }

      FunctionCall[] -> { return `EmptyType(); }
      
      AntiTerm(term) -> { return getTermType(`term,symbolTable);}
    }
		System.out.println("getTermType error on term: " + t);
		throw new TomRuntimeException("getTermType error on term: " + t);
  }
  
  protected static TomType getTermType(Expression t, SymbolTable symbolTable){
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

  protected static SlotList tomListToSlotList(TomList tomList) {
    return tomListToSlotList(tomList,1);
  }

  protected static SlotList tomListToSlotList(TomList tomList, int index) {
    %match(TomList tomList) {
      concTomTerm() -> { return `concSlot(); }
      concTomTerm(head,tail*) -> { 
        TomName slotName = `PositionName(concTomNumber(Number(index)));
        SlotList sl = tomListToSlotList(`tail,index+1);
        return `concSlot(PairSlotAppl(slotName,head),sl*); 
      }
    }
    throw new TomRuntimeException("tomListToSlotList: " + tomList);
  }

  protected static SlotList mergeTomListWithSlotList(TomList tomList, SlotList slotList) {
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

  protected static TomList slotListToTomList(SlotList tomList) {
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
