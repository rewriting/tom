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

package tom.engine.tools;

import java.util.*;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.*;

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
import tom.engine.adt.code.types.*;
import tom.engine.adt.code.types.bqterm.Composite;

import tom.engine.exception.TomRuntimeException;
import aterm.ATerm;

public class ASTFactory {
  %include { ../adt/tomsignature/TomSignature.tom }
   // Suppresses default constructor, ensuring non-instantiability.
  private ASTFactory() {}

  public static CodeList makeCodeList(Collection<Code> c) {
    CodeList list = `concCode();
    for(Code code: c) {
      list = `concCode(list*,code);
    }
    return list;
  }

  public static BQTermList makeBQTermList(Collection<BQTerm> c) {
    BQTermList list = `concBQTerm();
    for(BQTerm term: c) {
      list = `concBQTerm(list*,term);
    } 	     
    return list;
  }

  public static BQSlotList makeBQSlotList(Collection<BQSlot> c) {
    BQSlotList list = `concBQSlot();
    for(BQSlot slot: c) {
      list = `concBQSlot(list*,slot);
    }
    return list;
  }

  public static Composite makeComposite(Collection<BQTerm> c) {
    BQTerm list = `Composite();
    for(BQTerm term: c) {
      list = `Composite(list*,CompositeBQTerm(term));
    } 	     
    return (Composite) list;
  }

  public static TomList makeTomList(Collection<TomTerm> c) {
    TomList list = `concTomTerm();
    for(TomTerm term: c) {
      list = `concTomTerm(list*,term);
    } 	     
    return list;
  }

  public static InstructionList makeInstructionList(Collection<Code> c) {
    InstructionList list = `concInstruction();
    for(Code code: c) {
      %match(code) {
        TargetLanguageToCode(tl) -> { 
          list = `concInstruction(list*,CodeToInstruction(TargetLanguageToCode(tl))); 
        }
        InstructionToCode(i) -> { 
          list = `concInstruction(list*,i); 
        }
        BQTermToCode(t) -> { 
          list = `concInstruction(list*,BQTermToInstruction(t)); 
        }
      }
    }
    return list;
  }

  public static OptionList makeOptionList(List argumentList) {
    OptionList list = `concOption();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      Object elt = argumentList.get(i);
      Option term;
      if(elt instanceof TomName) {
        term = `TomNameToOption((TomName)elt);
      } else if(elt instanceof Declaration) {
        term = `DeclarationToOption((Declaration)elt);
      } else if(elt instanceof TomTerm) {
        term = `TomTermToOption((TomTerm)elt);
      } else {
        term = (Option) elt;
      }
      list = `concOption(term,list*);
    }
    return list;
  }

  public static ConstraintList makeConstraintList(List<Constraint> argumentList) {
    ConstraintList list = `concConstraint();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concConstraint(argumentList.get(i),list*);
    }
    return list;
  }

  public static ConstraintInstructionList makeConstraintInstructionList(List<ConstraintInstruction> argumentList) {
    ConstraintInstructionList list = `concConstraintInstruction();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concConstraintInstruction(argumentList.get(i),list*);
    }
    return list;
  }

  public static Constraint makeAndConstraint(List<Constraint> argumentList) {
    Constraint list = `AndConstraint();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `AndConstraint(argumentList.get(i),list*);
    }
    return list;
  }

  public static Constraint makeOrConstraint(List<Constraint> argumentList) {
    Constraint list = `OrConstraint();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `OrConstraint(argumentList.get(i),list*);
    }
    return list;
  }

  public static TomNameList makeNameList(List<TomName> argumentList) {
    TomNameList list = `concTomName();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concTomName(argumentList.get(i),list*);
    }
    return list;
  }

  public static SlotList makeSlotList(List<Slot> argumentList) {
    SlotList list = `concSlot();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concSlot(argumentList.get(i),list*);
    }
    return list;
  }

  public static PairNameDeclList makePairNameDeclList(List<PairNameDecl> argumentList) {
    PairNameDeclList list = `concPairNameDecl();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concPairNameDecl(argumentList.get(i),list*);
    }
    return list;
  }

  public static TomVisitList makeTomVisitList(List<TomVisit> argumentList) {
    TomVisitList list = `concTomVisit();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concTomVisit(argumentList.get(i),list*);
    }
    return list;
  }
///////////
  /*public static TomWithToList makeTomWithToList(List<TomWithTo> argumentList){
    TomWithToList list = `concTomWithTo();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concTomWithTo(argumentList.get(i),list*);
    }
    return list;
  }*/
  public static ElementaryTransformationList makeElementaryTransformationList(List<ElementaryTransformation> argumentList) {
    ElementaryTransformationList list = `concElementaryTransformation();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concElementaryTransformation(argumentList.get(i),list*);
    }
    return list;
  }

  public static RuleInstructionList makeRuleInstructionList(List<RuleInstruction> argumentList) {
    RuleInstructionList list = `concRuleInstruction();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concRuleInstruction(argumentList.get(i),list*);
    }
    return list;
  }

  public static InstructionList makeInstructionListFromInstructionCollection(List<Instruction> argumentList) {
    InstructionList list = `concInstruction();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concInstruction(argumentList.get(i),list*);
    }
    return list;
  }

  public static RefClassTracelinkInstructionList makeRefClassTracelinkInstructionList(List<RefClassTracelinkInstruction> argumentList) {
    RefClassTracelinkInstructionList list = `concRefClassTracelinkInstruction();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concRefClassTracelinkInstruction(argumentList.get(i),list*);
    }
    return list;
  }

  public static ResolveStratElementList makeResolveStratElementList(List<ResolveStratElement> argumentList){
    ResolveStratElementList list = `concResolveStratElement();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concResolveStratElement(argumentList.get(i),list*);
    }
    return list;
  }

  public static ResolveStratBlockList makeResolveStratBlockList(List<ResolveStratBlock> argumentList){
    ResolveStratBlockList list = `concResolveStratBlock();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concResolveStratBlock(argumentList.get(i),list*);
    }
    return list;
  }

  public static DeclarationList makeDeclarationList(List<Declaration> argumentList){
    DeclarationList list = `concDeclaration();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list = `concDeclaration(argumentList.get(i),list*);
    }
    return list;
  }
//////////

  public static TomSymbol makeSymbol(String symbolName, TomType resultType, TomTypeList typeList,
      PairNameDeclList pairNameDeclList, List optionList) {
    return `Symbol(Name(symbolName),TypesToType(typeList,resultType),pairNameDeclList,makeOptionList(optionList));
  }

  public static OptionList makeOption(Option arg) {
    OptionList list = `concOption();
    if(arg!= null) {
      list = `concOption(arg,list*);
    }
    return list;
  }

  public static ConstraintList makeConstraint(Constraint arg) {
    ConstraintList list = `concConstraint();
    if(arg!= null) {
      list = `concConstraint(arg,list*);
    }
    return list;
  }

  public static Constraint makeAliasTo(TomName name,int line, String fileName) {
    return `AliasTo(Variable(makeOption(makeOriginTracking(name.getString(), line , fileName)),
          name,
          SymbolTable.TYPE_UNKNOWN,
          concConstraint()));
  }

  public static Constraint makeStorePosition(TomName name,int line, String fileName) {
    return `AssignPositionTo(BQVariable(makeOption(makeOriginTracking(name.getString(), line , fileName)),
          name,
          SymbolTable.TYPE_UNKNOWN));
  }

  public static OptionList makeOption(Option arg, Option info) {
    OptionList list = `concOption();
    if(arg!= null) {
      list = `concOption(arg,list*);
    }
    list = `concOption(info,list*);
    return list;
  }

  private static Option makeOriginTracking(String name, int line , String fileName) {
    return `OriginTracking(Name(name), line, fileName);
  }

  public static TomType makeType(TypeOptionList tOptionList, String typeNameTom, String typeNametGL) {
    return `Type(tOptionList,typeNameTom,TLType(typeNametGL));
  }

    /*
     * create an <sort> symbol
     * where <sort> could be int. double or String
     */
  private static void makeSortSymbol(SymbolTable symbolTable,
                             String sort,
                             String value, List optionList) {
    TypeOptionList tOptionList = `concTypeOption();
    TomTypeList typeList = `concTomType();
    PairNameDeclList pairSlotDeclList = `concPairNameDecl();
    TomSymbol astSymbol = makeSymbol(value,`Type(tOptionList,sort,EmptyTargetLanguageType()),typeList,pairSlotDeclList,optionList);
    symbolTable.putSymbol(value,astSymbol);
  }

    /*
     * create an integer symbol
     */
  public static void makeIntegerSymbol(SymbolTable symbolTable,
                                String value, List optionList) {
    String sort = "int";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a long symbol
     */
  public static void makeLongSymbol(SymbolTable symbolTable,
                             String value, List optionList) {
    String sort = "long";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a char symbol
     */
  public static void makeCharSymbol(SymbolTable symbolTable,
                             String value, List optionList) {
    String sort = "char";
    makeSortSymbol(symbolTable, sort, value, optionList);
    //System.out.println("makeCharSymbol: -" + value + "-"); 
  }
    /*
     * create a double symbol
     */
  public static void makeDoubleSymbol(SymbolTable symbolTable,
                               String value, List optionList) {
    String sort = "double";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a string symbol
     */
  public static void makeStringSymbol(SymbolTable symbolTable,
                               String value, List optionList) {
    String sort = "String";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * update the root of lhs: it becomes a defined symbol
     */
  public static TomSymbol updateDefinedSymbol(SymbolTable symbolTable, TomTerm term) {
    if(term.isTermAppl() || term.isRecordAppl()) {
      String key = term.getNameList().getHeadconcTomName().getString();
      TomSymbol symbol = symbolTable.getSymbolFromName(key);
      if(symbol != null) {
        OptionList optionList = symbol.getOptions();
        optionList = `concOption(optionList*,DefinedSymbol());
        symbolTable.putSymbol(key,symbol.setOptions(optionList));
        return symbol;
      }
    }
    return null;
  }

  public static String makeSingleLineCode(String code, boolean pretty) {
    if(!pretty) {
      code = code.replace('\n', ' ');
      code = code.replace('\t', ' ');
      code = code.replace('\r', ' ');
    }
    return code;
  }

  public static TomName makeName(String slotName) {
    if(slotName.length()>0) {
      return `Name(slotName);
    } else {
      return `EmptyName();
    }
  }

  public final static String TOM_VARIABLE_SEPARATOR = "___";
  public static String makeTomVariableName(String prefix,String name) {
    return prefix + TOM_VARIABLE_SEPARATOR + name;
  }

  public static String extractRealNameFromTomVariableName(String fullName) {
    int index = fullName.indexOf(TOM_VARIABLE_SEPARATOR);
    if(index < 0) {
      return fullName;
    }

    return fullName.substring(index + TOM_VARIABLE_SEPARATOR.length());
  }

  public static BQTerm buildList(TomName name, BQTermList args, SymbolTable symbolTable) {
    TomSymbol topListSymbol = symbolTable.getSymbolFromName(name.getString());
    String topDomain = 
      TomBase.getTomType(TomBase.getSymbolDomain(topListSymbol).getHeadconcTomType());
    String topCodomain = TomBase.getTomType(TomBase.getSymbolCodomain(topListSymbol));
   
    %match(args) {
      concBQTerm() -> {
        return `BuildEmptyList(name);
      }

      /* buildList "l" [X*,a2,...,an] => append */
      concBQTerm(head@BQVariableStar[],tail*) -> {
        BQTerm subList = `buildList(name,tail,symbolTable);
        /* a VariableStar is always flattened */
        return `BuildAppendList(name,head,subList);
      }

      concBQTerm(head@Composite(CompositeBQTerm(BQVariableStar[]),_*),tail*) -> {
        BQTerm subList = buildList(name,`tail,symbolTable);
        return `BuildAppendList(name,head,subList);
      }

      /* buildList "l" [X,a2,...,an] = 
           if l: B* -> A then
              if X:A then 
                => append
              else 
                => cons
           else 
             => cons       
       */
      concBQTerm(head@BQVariable[AstType=varType],tail*) -> {
        //System.out.println("topDomain = " + topDomain);
        //System.out.println("topCodomain = " + topCodomain);
        //System.out.println("varType = " + TomBase.getTomType(`varType));

        BQTerm subList = buildList(name,`tail,symbolTable);
        /* a Variable is flattened if type and codomain are equals */
        if(topDomain != topCodomain) {
          if(TomBase.getTomType(`varType) == topCodomain) {
            return `BuildAppendList(name,head,subList);
          }
        }
        return `BuildConsList(name,head,subList);
      }

      concBQTerm(head@Composite(CompositeBQTerm(BQVariable[AstType=varType]),_*),tail*) -> {
        //System.out.println("topDomain = " + topDomain);
        //System.out.println("topCodomain = " + topCodomain);
        //System.out.println("varType = " + TomBase.getTomType(`varType));
        BQTerm subList = buildList(name,`tail,symbolTable);
        if(topDomain != topCodomain) {
          if(TomBase.getTomType(`varType) == topCodomain) {
            return `BuildAppendList(name,head,subList);
          }
        }
        return `BuildConsList(name,head,subList);
      }
      
      concBQTerm(head@Composite(CompositeBQTerm(BuildConsList[AstName=opName]),_*),tail*) -> {
        BQTerm subList = buildList(name,`tail,symbolTable);
        /* Flatten nested lists, unless domain and codomain are equals */
        if(topDomain != topCodomain) {
          if(name.equals(`opName)) {
            return `BuildAppendList(name,head,subList);
          }
        }
        return `BuildConsList(name,head,subList);
      }

      concBQTerm(head@Composite(CompositeBQTerm(BuildTerm[AstName=Name(tomName)]),_*),tail*) -> {
        BQTerm subList = buildList(name,`tail,symbolTable);
        if(topDomain != topCodomain) {
        /*
         * compare the codomain of tomName with topDomain
         * if the codomain of the inserted element is equal to the codomain
         * of the list operator, a BuildAppendList is performed
         */
          TomSymbol symbol = symbolTable.getSymbolFromName(`tomName);
          String codomain = TomBase.getTomType(TomBase.getSymbolCodomain(symbol));
          if(codomain == topCodomain) {
            return `BuildAppendList(name,head,subList);
          }
        }
        return `BuildConsList(name,head,subList);
      }

      concBQTerm(head@(FunctionCall|BuildTerm|BuildConstant|BQVariable|BuildAppendList|BuildConsList|BuildEmptyList)[],tail*) -> {
        BQTerm subList = buildList(name,`tail,symbolTable);
        return `BuildConsList(name,head,subList);
      }

      concBQTerm(head@Composite(X*),tail*) -> {
       BQTerm subList = buildList(name,`tail,symbolTable);
       // TODO: avoid this particular case
        %match(X) {
         Composite(CompositeTL(ITL(s))) -> {
            if (`s.trim().equals("")) {
              return subList;
            }
          }
        }
        return `BuildConsList(name,head,subList);
      }

    }
    throw new TomRuntimeException("buildList strange term: " + args);
  }

  public static BQTerm buildArray(TomName name, BQTermList args, SymbolTable symbolTable) {
    return buildArray(name,args.reverse(),0, symbolTable);
  }

  private static BQTerm buildArray(TomName name, BQTermList args, int size, SymbolTable symbolTable) {
    TomSymbol topListSymbol = symbolTable.getSymbolFromName(name.getString());
    String topDomain = TomBase.getTomType(TomBase.getSymbolDomain(topListSymbol).getHeadconcTomType());
    String topCodomain = TomBase.getTomType(TomBase.getSymbolCodomain(topListSymbol));

    %match(args) {
      concBQTerm() -> {
        return `BuildEmptyArray(name,ExpressionToBQTerm(Integer(size)));
      }

      concBQTerm(head@BQVariableStar[],tail*) -> {
        BQTerm subList = buildArray(name,`tail,size+1,symbolTable);
        /* a VariableStar is always flattened */
        return `BuildAppendArray(name,head,subList);
      }

      concBQTerm(head@Composite(CompositeBQTerm(BQVariableStar[]),_*),tail*) -> {
        BQTerm subList = buildArray(name,`tail,size+1,symbolTable);
        return `BuildAppendArray(name,head,subList);
      }

      concBQTerm(head@BuildConsArray[AstName=opName],tail*) -> {
        BQTerm subList = buildArray(name,`tail,size+1,symbolTable);
        /* Flatten nested lists, unless domain and codomain are equals */
        if(topDomain != topCodomain) {
          if(name.equals(`opName)) {
            return `BuildAppendArray(name,head,subList);
          }
        }
        return `BuildConsArray(name,head,subList);
      }

      concBQTerm(head@BuildTerm[AstName=Name(tomName)],tail*) -> {
        BQTerm subList = buildArray(name,`tail,size+1,symbolTable);
        if(topDomain != topCodomain) {
        /*
         * compare the codomain of tomName with topDomain
         * if the codomain of the inserted element is equal to the codomain
         * of the list operator, a BuildAppendArray is performed
         */
          TomSymbol symbol = symbolTable.getSymbolFromName(`tomName);
          String codomain = TomBase.getTomType(TomBase.getSymbolCodomain(symbol));
          if(codomain == topCodomain) {
            return `BuildAppendArray(name,head,subList);
          }
        }
        return `BuildConsArray(name,head,subList);
      }

      concBQTerm(head@(BuildTerm|BuildConstant|BQVariable)[],tail*) -> {
        BQTerm subList = buildArray(name,`tail,size+1,symbolTable);
        return `BuildConsArray(name,head,subList);
      }

      concBQTerm(head@Composite(X*),tail*) -> {
        BQTerm subList = buildArray(name,`tail,size+1,symbolTable);
        // TODO: avoid this particular case
        %match(X) {
          Composite(CompositeTL(ITL(s))) -> {
            if (`s.trim().equals("")) {
              return subList;
            }
          }
        }
        return `BuildConsArray(name,head,subList);
      }
    }

    throw new TomRuntimeException("buildArray strange term: " + args);
  }

  /*
   * transform a string "...$t...$u..." into "...{0}...{1}..."
   */
  public static String abstractCode(String code, String... vars) {
    int index=0;
    for(String var:vars) {
      code = code.replace("$"+var,"{"+index+"}");
      index++;
    }
    return code;
  }

}
