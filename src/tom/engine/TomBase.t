/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
import java.util.logging.Logger;

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
import tom.engine.adt.code.types.*;

import tom.engine.exception.TomRuntimeException;
import tom.engine.TomMessage;

import tom.platform.adt.platformoption.*;

import tom.library.sl.*;
import tom.library.sl.VisitFailure;


/**
 * Provides access to the TomSignatureFactory and helper methods.
 */
public final class TomBase {

  %include { ./adt/tomsignature/TomSignature.tom }
  %include { sl.tom }
  %include { ../platform/adt/platformoption/PlatformOption.tom }
  %include { ../library/mapping/java/boolean.tom }

  %typeterm Collection {
    implement { java.util.Collection }
    is_sort(t) { ($t instanceof java.util.Collection) }
  }

  public final static String DEFAULT_MODULE_NAME = "default";
  //size of cache
  private final static int LRUCACHE_SIZE = 5000;

  private static Logger logger = Logger.getLogger("tom.engine.TomBase");

  /** shortcut */

  /**
   * Returns the name of a <code>TomType</code>
   */
  public static String getTomType(TomType type) {
    %match {
      Type[TomType=s] << type -> { return `s; }
      EmptyType() << type||TypeVar[] << type -> { return null; }
    }
    throw new TomRuntimeException("getTomType error on term: " + type);
  }

  /**
   * Returns the implementation-type of a <code>TomType</code>
   */
  public static String getTLType(TomType type) {
    %match(type) {
      Type[TlType=tlType] -> { return getTLCode(`tlType); }
    }
    throw new TomRuntimeException("getTLType error on term: " + type);
  }

  /**
   * Returns the implementation-type of a <code>TLType</code>
   */
  public static String getTLCode(TargetLanguageType type) {
    %match(type) {
      TLType(str)  -> { return `str; }
    }
    throw new TomRuntimeException("getTLCode error on term: " + type);
  }

  /**
   * Returns the codomain of a given symbol
   */
  public static TomType getSymbolCodomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getCodomain();
    } else {
      return `EmptyType();
    }
  }

  /**
   * Returns the domain of a given symbol
   */
  public static TomTypeList getSymbolDomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getDomain();
    } else {
      return `concTomType();
    }
  }

  private static LRUCache<TomNumberList,String> tomNumberListToStringMap =
    new LRUCache<TomNumberList,String>(LRUCACHE_SIZE);
  public static String tomNumberListToString(TomNumberList numberList) {
    String result = tomNumberListToStringMap.get(numberList);
    if(result == null) {
      TomNumberList key = numberList;
      StringBuilder buf = new StringBuilder(30);
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
            %match(tomName) {
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
            %match(tomName) {
              Name(name) -> {
                identifier = `name;
              }
              PositionName(localNumberList) -> {
                identifier = tomNumberListToString(`localNumberList);
              }
            }
            //buf.append("NameNumber");
            buf.append(identifier);
          }
        }
      }
      result = buf.toString();
      tomNumberListToStringMap.put(key,result);
    }
    return result;
  }

  /**
   * Returns <code>true</code> if the symbol corresponds to a %oplist
   */
  public static boolean isListOperator(TomSymbol symbol) {
    if(symbol==null) {
      return false;
    }
    %match(symbol) {
      Symbol[Options=optionList] -> {
        OptionList optionList = `optionList;
        while(!optionList.isEmptyconcOption()) {
          Option opt = optionList.getHeadconcOption();
          %match(opt) {
            DeclarationToOption(MakeEmptyList[]) -> { return true; }
            DeclarationToOption(MakeAddList[])   -> { return true; }
          }
          optionList = optionList.getTailconcOption();
        }
        return false;
      }
    }
    throw new TomRuntimeException("isListOperator -- strange case: '" + symbol + "'");
  }

  /**
   * Returns <code>true</code> if the symbol corresponds to a %oparray
   */
  public static boolean isArrayOperator(TomSymbol symbol) {
    if(symbol==null) {
      return false;
    }
    %match(symbol) {
      Symbol[Options=optionList] -> {
        OptionList optionList = `optionList;
        while(!optionList.isEmptyconcOption()) {
          Option opt = optionList.getHeadconcOption();
          %match(opt) {
            DeclarationToOption(MakeEmptyArray[]) -> { return true; }
            DeclarationToOption(MakeAddArray[])   -> { return true; }
          }
          optionList = optionList.getTailconcOption();
        }
        return false;
      }
    }
    throw new TomRuntimeException("isArrayOperator -- strange case: '" + symbol + "'");
  }

  /**
   * Returns <code>true</code> if the symbol corresponds to a %op
   */
  public static boolean isSyntacticOperator(TomSymbol subject) {
    return (!(isListOperator(subject) || isArrayOperator(subject)));
  }

  // ------------------------------------------------------------
  /**
   * Collects the variables that appears in a term
   * @param collection the bag which collect the results
   * @param subject the term to traverse
   */
  public static void collectVariable(Collection<TomTerm> collection, 
          tom.library.sl.Visitable subject, boolean considerBQVars) {
    try {
      //TODO: replace TopDownCollect by continuations
      `TopDownCollect(collectVariable(collection,considerBQVars)).visitLight(`subject);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("Should not be there");
    }
  }

  %strategy collectVariable(collection:Collection, considerBQVars:boolean) extends `Identity() {
    visit BQTerm {
        v@BQVariable[] -> {
          if (considerBQVars) { collection.add(convertFromBQVarToVar(`v)); } 
        }
    }
    
    visit TomTerm {
      v@(Variable|VariableStar)[Constraints=constraintList] -> {
        collection.add(`v);
        TomTerm annotedVariable = getAliasToVariable(`constraintList);
        if(annotedVariable!=null) {
          collection.add(annotedVariable);
        }
        `Fail().visitLight(`v);
      }

      // to collect annoted nodes but avoid collect variables in optionSymbol
      t@RecordAppl[Slots=subterms, Constraints=constraintList] -> {
        collectVariable(collection,`subterms,considerBQVars);
        TomTerm annotedVariable = getAliasToVariable(`constraintList);
        if(annotedVariable!=null) {
          collection.add(annotedVariable);
        }
        `Fail().visitLight(`t);
      }

    }
  }

  /**
   * Returns a Map which associates an integer to each variable name
   */
  public static Map<TomName,Integer> collectMultiplicity(tom.library.sl.Visitable subject) {
    // collect variables
    Collection<TomTerm> variableList = new HashSet<TomTerm>();
    collectVariable(variableList,`subject,true);
    // compute multiplicities
    HashMap<TomName,Integer> multiplicityMap = new HashMap<TomName,Integer>();
    for(TomTerm variable:variableList) {
      TomName name = variable.getAstName();
      if(multiplicityMap.containsKey(name)) {
        int value = multiplicityMap.get(name);
        multiplicityMap.put(name, 1+value);
      } else {
        multiplicityMap.put(name, 1);
      }
    }
    return multiplicityMap;
  }

  private static TomTerm getAliasToVariable(ConstraintList constraintList) {
    %match(constraintList) {
      concConstraint(_*,AliasTo(var@Variable[]),_*) -> { return `var; }
    }
    return null;
  }

  public static boolean hasTheory(TomTerm term, ElementaryTheory elementaryTheory) {
    return hasTheory(getTheory(term),elementaryTheory);
  }

  public static boolean hasTheory(Theory theory, ElementaryTheory elementaryTheory) {
    %match(theory) {
      concElementaryTheory(_*,x,_*) -> { if(`x==elementaryTheory) { return true; } }
    }
    return false;
  }

  public static Theory getTheory(TomTerm term) {
    %match(term) {
      RecordAppl[Options=concOption(_*,MatchingTheory(theory),_*)] -> { return `theory; }
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
    %match(optionList) {
      concOption(_*,DeclarationToOption(decl@IsFsymDecl[]),_*) -> { return `decl; }
    }
    return null;
  }

  public static boolean hasIsFsymDecl(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Options=concOption(_*,DeclarationToOption(IsFsymDecl[]),_*)] -> {
        return true;
      }
    }
    return false;
  }

  public static String getModuleName(OptionList optionList) {
    %match(optionList) {
      concOption(_*,ModuleName(moduleName),_*) -> { return `moduleName; }
    }
    return null;
  }

  /*
  public static boolean hasConstant(OptionList optionList) {
    %match(optionList) {
      concOption(_*,Constant[],_*) -> { return true; }
    }
    return false;
  }
*/

  public static boolean hasDefinedSymbol(OptionList optionList) {
    %match(optionList) {
      concOption(_*,DefinedSymbol(),_*) -> { return true; }
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
    %match(decl) {
      GetSlotDecl[SlotName=name] -> { return `name; }
    }

    return pairNameDecl.getSlotName();
  }

  public static int getSlotIndex(TomSymbol tomSymbol, TomName slotName) {
    int index = 0;
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
    ArrayList<String> nameList = new ArrayList<String>();
    while(!pairNameDeclList.isEmptyconcPairNameDecl()) {
      TomName name = pairNameDeclList.getHeadconcPairNameDecl().getSlotName();
      if(slotName.equals(name)) {
        return index;
      }
      nameList.add(name.getString());
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
    %match(symbol) {
      Symbol[TypesToType=TypesToType(typeList,_)] -> {
        int index = getSlotIndex(symbol,slotName);
        return elementAt(`typeList,index);
      }
    }
    return null;
    //throw new TomRuntimeException("getSlotType: bad slotName error: " + slotName);
  }

  public static boolean isDefinedSymbol(TomSymbol subject) {
    /*This test seems to be useless
       if(subject==null) {
      System.out.println("isDefinedSymbol: subject == null");
      return false;
    }*/
    %match(subject) {
      Symbol[Options=optionList] -> {
        return hasDefinedSymbol(`optionList);
      }
    }
    return false;
  }

  public static boolean isDefinedGetSlot(TomSymbol symbol, TomName slotName) {
    if(symbol==null) {
      //System.out.println("isDefinedSymbol: symbol == null");
      return false;
    }
    %match(symbol) {
      Symbol[PairNameDeclList=concPairNameDecl(_*,PairNameDecl[SlotName=name,SlotDecl=decl],_*)] -> {
        if(`name==slotName && `decl!=`EmptyDeclaration()) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * Return the option containing OriginTracking information
   */
  public static Option findOriginTracking(OptionList optionList) {
    if(optionList.isEmptyconcOption()) {
      return `noOption();
    }
    while(!optionList.isEmptyconcOption()) {
      Option subject = optionList.getHeadconcOption();
      %match(subject) {
        orgTrack@OriginTracking[] -> {
          return `orgTrack;
        }
      }
      optionList = optionList.getTailconcOption();
    }
    throw new TomRuntimeException("findOriginTracking:  not found: " + optionList);
  }

  public static TomSymbol getSymbolFromName(String tomName, SymbolTable symbolTable) {
    return symbolTable.getSymbolFromName(tomName);
  }

  public static TomType getTermType(TomTerm t, SymbolTable symbolTable) {
    %match(t) {
      (TermAppl|RecordAppl)[NameList=concTomName(headName,_*)] -> {
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

      (Variable|VariableStar)[AstType=type] -> {
        return `type;
      }

      AntiTerm(term) -> { return getTermType(`term,symbolTable);}

    }
    //System.out.println("getTermType error on term: " + t);
    //throw new TomRuntimeException("getTermType error on term: " + t);
    return `EmptyType();
  }

  public static TomType getTermType(BQTerm t, SymbolTable symbolTable) {
    %match(t) {
      (BQVariable|BQVariableStar)[AstType=type] -> {
        return `type;
      }

      FunctionCall[AstType=type] -> { return `type; }

      ExpressionToBQTerm(expr) -> { return getTermType(`expr,symbolTable); }

      ListHead[Codomain=type] -> { return `type; }

      ListTail[Variable=term] -> { return getTermType(`term, symbolTable); }

      Subterm[AstName=Name(name),SlotName=slotName] -> {
        TomSymbol tomSymbol = symbolTable.getSymbolFromName(`name);
        return getSlotType(tomSymbol, `slotName);
      }

      (BuildConstant|BuildTerm|BuildEmptyList|BuildConsList|BuildAppendList|BuildEmptyArray|BuildConsArray|BuildAppendArray)[AstName=Name(name)]
        -> {
          TomSymbol tomSymbol = symbolTable.getSymbolFromName(`name); 
          if(tomSymbol!=null) {
            return tomSymbol.getTypesToType().getCodomain();
          } else {
            return `EmptyType();
          }
        }
   }
    //System.out.println("getTermType error on term: " + t);
    //throw new TomRuntimeException("getTermType error on term: " + t);
    return `EmptyType();
  }

  public static TomType getTermType(Expression t, SymbolTable symbolTable) {
    %match(t) {
      (GetHead|GetSlot|GetElement)[Codomain=type] -> { return `type; }

      BQTermToExpression[AstTerm=bqterm] -> { return getTermType(`bqterm, symbolTable); }
      Conditional[Cond=cond] -> { return getTermType(`cond, symbolTable); } 
      IsFsym[Variable=bqterm] ->  { return getTermType(`bqterm, symbolTable); }
      GetTail[Variable=term] -> { return getTermType(`term, symbolTable); }
      GetSliceList[VariableBeginAST=term] -> { return getTermType(`term, symbolTable); }
      GetSliceArray[SubjectListName=term] -> { return getTermType(`term, symbolTable); }
      Cast[AstType=type] -> { return `type; }
      AddOne[Variable=bqterm] -> { return getTermType(`bqterm, symbolTable); }
      Integer[] -> { return symbolTable.getType("int"); }
    }
    System.out.println("getTermType error on term: " + t);
    throw new TomRuntimeException("getTermType error on term: " + t);
  }

  public static TomSymbol getSymbolFromTerm(TomTerm t, SymbolTable symbolTable) {
    %match(t) {
      (TermAppl|RecordAppl)[NameList=concTomName(headName,_*)] -> {
        String tomName = null;
        if(`(headName) instanceof AntiName) {
          tomName = ((AntiName)`headName).getName().getString();
        } else {
          tomName = ((TomName)`headName).getString();
        }
        return symbolTable.getSymbolFromName(tomName);
      }

      (Variable|VariableStar)[AstName=Name(tomName)] -> {
        return symbolTable.getSymbolFromName(`tomName);
      }

      AntiTerm(term) -> { return getSymbolFromTerm(`term,symbolTable);}
    }
    return null;
  }

  public static TomSymbol getSymbolFromTerm(BQTerm t, SymbolTable symbolTable) {
    %match(t) {
      (BQVariable|BQVariableStar)[AstName=Name(tomName)] -> {
        return symbolTable.getSymbolFromName(`tomName);
      }

      FunctionCall[AstName=Name(tomName)] -> { return symbolTable.getSymbolFromName(`tomName); }
    }
    return null;
  }


  public static SlotList tomListToSlotList(TomList tomList) {
    return tomListToSlotList(tomList,1);
  }

  public static SlotList tomListToSlotList(TomList tomList, int index) {
    %match(tomList) {
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
    %match(tomList, SlotList slotList) {
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

  public static BQTermList slotListToBQTermList(SlotList tomList) {
    %match(tomList) {
      concSlot() -> { return `concBQTerm(); }
      concSlot(PairSlotAppl[Appl=head],tail*) -> {
        BQTermList tl = slotListToBQTermList(`tail);
        return `concBQTerm(convertFromVarToBQVar(head),tl*);
      }
    }
    throw new TomRuntimeException("slotListToTomList: " + tomList);
  }

  public static int getArity(TomSymbol symbol) {
    if (isListOperator(symbol) || isArrayOperator(symbol)) {
      return 2;
    } else {
      return ((Collection) symbol.getPairNameDeclList()).size();
    }
  }

  /**
   * builds a BQVariable from a TomTerm Variable
   */
  public static BQTerm convertFromVarToBQVar(TomTerm variable) {
    %match(variable) {
      Variable[Options=optionList,AstName=name,AstType=type] -> {
        return `BQVariable(optionList, name, type);
      }
      VariableStar[Options=optionList,AstName=name,AstType=type] -> {
        return `BQVariableStar(optionList, name, type);
      }
    }
    throw new TomRuntimeException("cannot convert into a bq variable the term "+variable);
  }

  /**
   * builds a Variable from a BQVariable
   */
  public static TomTerm convertFromBQVarToVar(BQTerm variable) {
    %match(variable) {
      BQVariable[Options=optionList,AstName=name,AstType=type] -> {
        return `Variable(optionList, name, type, concConstraint());
      }
      BQVariableStar[Options=optionList,AstName=name,AstType=type] -> {
        return `VariableStar(optionList, name, type, concConstraint());
      }
    }
    throw new TomRuntimeException("cannot convert into a variable the term " + variable);
  }

} // class TomBase
