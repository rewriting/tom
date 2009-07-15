/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2009, INRIA
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
import tom.engine.xml.Constants;

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

import tom.engine.exception.TomRuntimeException;
import aterm.ATerm;

public class ASTFactory {
  %include { ../adt/tomsignature/TomSignature.tom }
   // Suppresses default constructor, ensuring non-instantiability.
  private ASTFactory() {}

  public static CodeList makeList(Collection<Code> c) {
    Object array[] = c.toArray();
    TomList list = `concTomTerm();
    for(int i=array.length-1; i>=0 ; i--) {
     Object elt = array[i];
     TomTerm term;
     if(elt instanceof TargetLanguage) { 	 
        term = `TargetLanguageToCode((TargetLanguage)elt); 	 
      } else if(elt instanceof Declaration) { 	 
        term = `DeclarationToCode((Declaration)elt); 	 
        /* seems to never happen
      } else if(elt instanceof Expression) { 
        term = `ExpressionToTomTerm((Expression)elt); 	 
        */
      } else if(elt instanceof Instruction) { 	 
        term = `InstructionToCode((Instruction)elt); 	 
      } else { 	 
        term = (TomTerm)elt; 	 
      } 	 
      list = `concCode(term,list*);
    } 	     
    return list;
  }

  public static InstructionList makeInstructionList(Collection c) {
    Object array[] = c.toArray();
    InstructionList list = `concInstruction();
    for(int i=array.length-1; i>=0 ; i--) {
      Object elt = array[i];
      Instruction term;
      if(elt instanceof TargetLanguage) {
        term = `TargetLanguageToInstruction((TargetLanguage)elt);
      } else if(elt instanceof TomTerm) {
        term = `TomTermToInstruction((TomTerm)elt);
          //System.out.println("term   = " + term);
      } else if(elt instanceof Instruction) {
        term = (Instruction)elt;
      } else {
        /* XXX: is this an error ? if yes, it should not be that silent */
        System.out.println("elt   = " + elt);
        term = (Instruction) elt;
      }
      list = `concInstruction(term,list*);
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
    return `AssignPositionTo(Variable(makeOption(makeOriginTracking(name.getString(), line , fileName)),
          name,
          SymbolTable.TYPE_UNKNOWN,
          concConstraint()));
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

  protected static TomType makeType(String typeNameTom, String typeNametGL) {
    TomType sortTL  = `TLType(typeNametGL);
    return `Type(typeNameTom,sortTL);
  }

    /*
     * create an <sort> symbol
     * where <sort> could be int. double or String
     */
  private static void makeSortSymbol(SymbolTable symbolTable,
                             String sort,
                             String value, List optionList) {
    TomTypeList typeList = `concTomType();
    PairNameDeclList pairSlotDeclList = `concPairNameDecl();
    TomSymbol astSymbol = makeSymbol(value,`Type(sort,EmptyType()),typeList,pairSlotDeclList,optionList);
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
        OptionList optionList = symbol.getOption();
        optionList = `concOption(optionList*,DefinedSymbol());
        symbolTable.putSymbol(key,symbol.setOption(optionList));
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

  public static String encodeXMLString(SymbolTable symbolTable, String name) {
    name = "\"" + name + "\"";
    makeStringSymbol(symbolTable,name, new LinkedList());
    return name;
  }

  public static String makeTomVariableName(String name) {
    return "tom_" + name;
  }

  public static TomList metaEncodeTermList(SymbolTable symbolTable,TomList list) {
    %match(list) {
      concTomTerm() -> { return `concTomTerm();}
      concTomTerm(head,tail*) -> {
        TomList tl = metaEncodeTermList(symbolTable,`tail);
        return `concTomTerm(metaEncodeXMLAppl(symbolTable,head),tl*);
      }
    }
    return list;
  }

  public static TomTerm encodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * encode a String into a quoted-string
       * Appl(...,Name("string"),...) becomes
       * Appl(...,Name("\"string\""),...)
       */
    TomNameList newNameList = `concTomName();
    %match(term) {
      RecordAppl[NameList=(_*,Name(name),_*)] -> {
        newNameList = `concTomName(newNameList*,Name(encodeXMLString(symbolTable,name)));
      }
    }
    term = term.setNameList(newNameList);
      //System.out.println("encodeXMLAppl = " + term);
    return term;
  }

  public static TomTerm metaEncodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * meta-encode a String into a TextNode
       * Appl(...,Name("\"string\""),...) becomes
       * Appl(...,Name("TextNode"),[Appl(...,Name("\"string\""),...)],...)
       */
      //System.out.println("metaEncode: " + term);
    %match(term) {
      RecordAppl[NameList=(Name(tomName))] -> {
          //System.out.println("tomName = " + tomName);
        TomSymbol tomSymbol = symbolTable.getSymbolFromName(`tomName);
        if(tomSymbol != null) {
          if(symbolTable.isStringType(TomBase.getTomType(TomBase.getSymbolCodomain(tomSymbol)))) {
            Option info = `OriginTracking(Name(Constants.TEXT_NODE),-1,"unknown filename");
            term = `RecordAppl(ASTFactory.makeOption(info),
                               concTomName(Name(Constants.TEXT_NODE)),concSlot(PairSlotAppl(Name(Constants.SLOT_DATA),term)),
                          concConstraint());
              //System.out.println("metaEncodeXmlAppl = " + term);
          }
        }
      }
    }
    return term;
  }

  public static boolean isExplicitTermList(List childs) {
    if(childs.size() == 1) {
      TomTerm term = (TomTerm) childs.get(0);
      %match(term) {
        (RecordAppl|TermAppl)[NameList=(Name(""))] -> {
          return true;
        }
      }
    }
    return false;
  }

  public static List<TomTerm> metaEncodeExplicitTermList(SymbolTable symbolTable, TomTerm term) {
    LinkedList<TomTerm> list = new LinkedList<TomTerm>();
    %match(term) {
      RecordAppl[NameList=(Name("")),Slots=args] -> {
        while(!`args.isEmptyconcSlot()) {
          list.add(metaEncodeXMLAppl(symbolTable,`args.getHeadconcSlot().getAppl()));
          `args = `args.getTailconcSlot();
        }
        return list;
      }

      TermAppl[NameList=(Name("")),Args=args] -> {
        while(!`args.isEmptyconcTomTerm()) {
          list.add(metaEncodeXMLAppl(symbolTable,`args.getHeadconcTomTerm()));
          `args = `args.getTailconcTomTerm();
        }
        return list;
      }
    }
    //System.out.println("metaEncodeExplicitTermList: strange case: " + term);
    list.add(term);
    return list;
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

      /* FIXME : seems useless
      concBQTerm(head@Composite(concBQTerm(VariableStar[],_*)),tail*) -> {
        TomTerm subList = buildList(name,`tail,symbolTable);
        return `BuildAppendList(name,head,subList);
      }
       */

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
        BQTerm subList = buildList(name,`tail,symbolTable);
        /* a Variable is flattened if type and codomain are equals */
        if(topDomain != topCodomain) {
          if(TomBase.getTomType(`varType) == topCodomain) {
            return `BuildAppendList(name,head,subList);
          }
        }
        return `BuildConsList(name,head,subList);
      }

      /* FIXME : seems useless
      concBQTerm(head@Composite(concBQTerm(Variable[AstType=varType],_*)),tail*) -> {
        TomTerm subList = buildList(name,`tail,symbolTable);
        if(topDomain != topCodomain) {
          if(TomBase.getTomType(`varType) == topCodomain) {
            return `BuildAppendList(name,head,subList);
          }
        }
        return `BuildConsList(name,head,subList);
      }
      */

      concBQTerm(head@BuildConsList[AstName=opName],tail*) -> {
        BQTerm subList = buildList(name,`tail,symbolTable);
        /* Flatten nested lists, unless domain and codomain are equals */
        if(topDomain != topCodomain) {
          if(name.equals(`opName)) {
            return `BuildAppendList(name,head,subList);
          }
        }
        return `BuildConsList(name,head,subList);
      }

      concBQTerm(head@BuildTerm[AstName=Name(tomName)],tail*) -> {
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

      concBQTerm(head@(BuildTerm|BuildConstant|BQVariable|BuildAppendList|BuildConsList)[],tail*) -> {
        BQTerm subList = buildList(name,`tail,symbolTable);
        return `BuildConsList(name,head,subList);
      }

      concBQTerm(BQTL[],tail*) -> {
        BQTerm subList = buildList(name,`tail,symbolTable);
        return subList;
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

      /* FIXME : useless ?
      concBQTerm(head@Composite(concBQTerm(VariableStar[],_*)),tail*) -> {
        TomTerm subList = buildArray(name,`tail,size+1,symbolTable);
        return `BuildAppendArray(name,head,subList);
      }
      */

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

      concBQTerm(BQTL[],tail*) -> {
        BQTerm subList = buildArray(name,`tail,size,symbolTable);
        return subList;
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
