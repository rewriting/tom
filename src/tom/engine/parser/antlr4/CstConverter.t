/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

package tom.engine.parser.antlr4;

import java.util.logging.Logger;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.typeconstraints.types.*;
import tom.engine.adt.cst.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

//import tom.library.sl.*;

public class CstConverter {
  // %include { ../../../library/mapping/java/sl.tom}
  %include { ../../adt/tomsignature/TomSignature.tom }

  private static Logger logger = Logger.getLogger("tom.engine.typer.CstConverter");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private SymbolTable symbolTable;

  public CstConverter(SymbolTable st) {
    this.symbolTable = st;
  }

  public Code convert(CstProgram cst) {
    %match(cst) {
      Cst_Program(list) -> { 
        return `Tom(convert(list));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Code convert(CstBlock cst) {
    %match(cst) {
      HOSTBLOCK(optionList,content) -> {
        CstOption ot = getOriginTracking(`optionList);
        return `TargetLanguageToCode(TL(content,
              TextPosition(ot.getstartLine(),ot.getstartColumn()),
              TextPosition(ot.getendLine(),ot.getendColumn())));
      }

      Cst_BQTermToBlock(bqterm) -> {
        return `BQTermToCode(convert(bqterm));
      }

      Cst_MatchConstruct(optionList, arguments, constraintActionList) -> {
        ConstraintInstructionList cil = convert(`constraintActionList,`arguments);
          return `InstructionToCode(Match(cil,addDefaultModule(convert(optionList,"Match"))));
      }

      Cst_TypetermConstruct(optionList, Cst_Type(typeName), extendsTypeName, operatorList) -> {
        CstOption ot = getOriginTracking(`optionList);
        TypeOptionList typeoptionList = `concTypeOption();
        DeclarationList declarationList = `concDeclaration();
        %match(extendsTypeName) {
          Cst_Type(supertypeName) -> { typeoptionList = `concTypeOption(SubtypeDecl(supertypeName)); }
        }
        %match(operatorList) {
          ConcCstOperator(_*,operator,_*) -> {
            %match(operator) {
              Cst_Equals(Cst_Name(name1),Cst_Name(name2),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                CstOption ot2 = getOriginTracking(`optionList2);
                String code = ASTFactory.abstractCode(`content,`name1,`name2);
                Declaration attribute = `EqualTermDecl(
                    makeBQVariableFromName(name1,typeName,optionList2),
                    makeBQVariableFromName(name2,typeName,optionList2),
                    Code(code), 
                    OriginTracking(Name(typeName),ot2.getstartLine(),ot2.getfileName())
                    );
                declarationList = `concDeclaration(attribute,declarationList*); 
              }

              Cst_IsSort(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                CstOption ot2 = getOriginTracking(`optionList2);
                String code = ASTFactory.abstractCode(`content,`name);
                Declaration attribute = `IsSortDecl(
                    makeBQVariableFromName(name,typeName,optionList2),
                    Code(code), 
                    OriginTracking(Name(typeName),ot2.getstartLine(),ot2.getfileName())
                    );
                declarationList = `concDeclaration(attribute,declarationList*); 
              }

              Cst_Implement(ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                TomType astType = `Type(typeoptionList,typeName,TLType(content));
                symbolTable.putType(`typeName, astType);
              }

            }
          }
        }

        return `DeclarationToCode(TypeTermDecl(
              Name(typeName),
              declarationList,
              OriginTracking(Name(typeName),ot.getstartLine(),ot.getfileName())
              ));
      }

      Cst_OpConstruct(optionList, Cst_Type(codomain), Cst_Name(opName), slotList, operatorList) -> {
        CstOption ot = getOriginTracking(`optionList);
        DeclarationList declarationList = `concDeclaration();
        List<PairNameDecl> pairNameDeclList = new LinkedList<PairNameDecl>();
        List<TomName> slotNameList = new LinkedList<TomName>();
        List<Option> options = new LinkedList<Option>();
        TomTypeList types = `concTomType();

        options.add(`OriginTracking(Name(opName),ot.getstartLine(),ot.getfileName()));

        %match(slotList) {
          ConcCstSlot(_*,Cst_Slot(Cst_Name(slotName),Cst_Type(slotType)),_*) -> {
            TomName astName = ASTFactory.makeName(`slotName);
            if(slotNameList.indexOf(astName) != -1) {
              TomMessage.error(getLogger(),ot.getfileName(), ot.getstartLine(),
                  TomMessage.repeatedSlotName,
                  `slotName);
            }
            slotNameList.add(astName);
            pairNameDeclList.add(`PairNameDecl(astName,EmptyDeclaration()));
            types = `concTomType(types*,Type(concTypeOption(),slotType,EmptyTargetLanguageType()));
            String typeOfSlot = getSlotType(`codomain,`slotName);
            if(typeOfSlot != null && !typeOfSlot.equals(`slotType)) {
              TomMessage.warning(getLogger(),ot.getfileName(), ot.getstartLine(),
                  TomMessage.slotIncompatibleTypes,`slotName,`slotType,typeOfSlot);
            } else {
              putSlotType(`codomain,`slotName,`slotType);
            }

          }
        }

        %match(operatorList) {
          ConcCstOperator(_*,operator,_*) -> {
            %match(operator) {

              Cst_IsFsym(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                CstOption ot2 = getOriginTracking(`optionList2);
                String code = ASTFactory.abstractCode(`content,`name);
                Declaration attribute = `IsFsymDecl(
                    Name(opName),
                    makeBQVariableFromName(name,codomain,optionList2),
                    Code(code), 
                    OriginTracking(Name(opName),ot2.getstartLine(),ot2.getfileName())
                    );
                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_GetSlot(Cst_Name(slotName),Cst_Name(argName),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                CstOption ot2 = getOriginTracking(`optionList2);
                String code = ASTFactory.abstractCode(`content,`argName);
                Declaration attribute = `GetSlotDecl(
                    Name(opName),
                    Name(slotName),
                    makeBQVariableFromName(argName,codomain,optionList2),
                    Code(code), 
                    OriginTracking(Name(opName),ot2.getstartLine(),ot2.getfileName())
                    );

                TomName sName = attribute.getSlotName();
                int index = slotNameList.indexOf(sName);
                /*
                 * ensure that sName appears in slotNameList, only once
                 * ensure that sName has not already been generated
                 */
                TomMessage msg = null;
                if(index == -1) {
                  msg = TomMessage.errorIncompatibleSlotDecl;
                } else {
                  PairNameDecl pair = pairNameDeclList.get(index);
                  %match(pair) {
                    PairNameDecl[SlotDecl=decl] -> {
                      if(`decl != `EmptyDeclaration()) {
                        msg = TomMessage.errorTwoSameSlotDecl;
                      }
                    }
                  }
                }
                if(msg != null) {
                  TomMessage.error(getLogger(),ot.getfileName(), ot.getstartLine(),
                      msg,
                      ot2.getfileName(), ot2.getstartLine(),
                      "%op "+ `codomain, ot.getstartLine(),sName.getString());
                } else {
                  pairNameDeclList.set(index,`PairNameDecl(sName,attribute));
                }
              }

              Cst_Make(nameList,ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                CstOption ot2 = getOriginTracking(`optionList2);
                ArrayList<String> varnameList = new ArrayList<String>();
                BQTermList args = `concBQTerm();
                int index = 0;

                %match(nameList) {
                  ConcCstName(_*,Cst_Name(argName),_*) -> {
                    varnameList.add(`argName);
                    TomType type = (types.length()>0)?TomBase.elementAt(types,index++):`EmptyType();
                    args = `concBQTerm(args*, BQVariable(concOption(), Name(argName), type));
                  }
                }

                String[] vars = new String[varnameList.size()]; // used only to give a type
                String code = ASTFactory.abstractCode(`content,varnameList.toArray(vars));

                Declaration attribute = `MakeDecl(
                    Name(opName),
                    Type(concTypeOption(),codomain,EmptyTargetLanguageType()),
                    args,
                    ExpressionToInstruction(Code(code)),
                    OriginTracking(Name(opName),ot2.getstartLine(),ot2.getfileName())
                    );

                options.add(`DeclarationToOption(attribute)); 
              }
               

            }
          }
        }
        TomSymbol astSymbol = ASTFactory.makeSymbol(`opName, `Type(concTypeOption(),codomain,EmptyTargetLanguageType()), types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
        symbolTable.putSymbol(`opName,astSymbol);
        return `DeclarationToCode(SymbolDecl(Name(opName)));
      }

    } // end %match
   
    //return `Tom(concCode());
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQTerm convert(CstBQTerm cst) {
    %match(cst) {

      Cst_BQDefault[] -> { 
        return `BQDefault(); 
      }

      Cst_BQAppl(ol,name,argList) -> {
        return `BQAppl(addDefaultModule(convert(ol,name)),Name(name),convert(argList));
      }

      Cst_BQRecordAppl(ol,name,argList) -> {
        return `BQRecordAppl(addDefaultModule(convert(ol,name)),Name(name),convert(argList));
      }

      Cst_BQVar(ol,name,Cst_TypeUnknown()) -> {
        return `BQVariable(addDefaultModule(convert(ol,name)),Name(name),SymbolTable.TYPE_UNKNOWN);
      }

      Cst_BQVar(ol,name,Cst_Type(typename)) -> {
        return `BQVariable(addDefaultModule(convert(ol,name)),Name(name),Type(concTypeOption(),typename,EmptyTargetLanguageType()));
      }

      Cst_BQVarStar(ol,name,Cst_TypeUnknown()) -> {
        return `BQVariableStar(addDefaultModule(convert(ol,name)),Name(name),SymbolTable.TYPE_UNKNOWN);
      }

      Cst_BQComposite(ol,argList) -> {
        BQTerm composite = `Composite();
        %match(argList) {
          ConcCstBQTerm(_*,bqterm,_*) -> {
            %match(bqterm) {
              Cst_ITL(ol2,code) -> { composite = `Composite(composite*,CompositeTL(ITL(code))); }
              !Cst_ITL[]        -> { composite = `Composite(composite*,CompositeBQTerm(convert(bqterm))); }
            }
          }
        }
        return composite;
      }
    }

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQSlot convert(CstPairSlotBQTerm cst) {
    %match(cst) {
      Cst_PairSlotBQTerm(ol,Cst_Name(slotName),bqterm) -> {
        return `PairSlotBQTerm(Name(slotName),convert(bqterm));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Option convert(CstOption cst, String subject) {
    %match(cst) {
      Cst_OriginTracking[fileName=fileName,startLine=startLine] -> {
        return `OriginTracking(Name(subject),startLine,fileName);
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public ConstraintInstruction convert(CstConstraintAction cst, CstBQTermList subjectList) {
    %match(cst) {
      Cst_ConstraintAction(constraint, action, optionList) -> {
        int currentIndex = 0; // index of the current subject of subjectList
        OptionList newoptionList = convert(`optionList,"ConstraintAction");
        //newoptionList = `concOption(newoptionList*);
        return `ConstraintInstruction(
            convert(constraint,subjectList,currentIndex), 
            CodeToInstruction(Tom(convert(action))), 
            newoptionList);
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Constraint convert(CstConstraint cst, CstBQTermList subjectList, int subjectIndex) {
    %match(cst) {
      Cst_MatchArgumentConstraint(pattern) -> {
        CstBQTermList l = `subjectList;
        if (0 > subjectIndex || subjectIndex > subjectList.length()) {
          throw new IllegalArgumentException("illegal list index: " + subjectIndex);
        }
        for (int i = 0; i < subjectIndex; i++) {
          l = l.getTailConcCstBQTerm();
        }
        BQTerm currentSubject = convert(l.getHeadConcCstBQTerm());
        TomType type = getTomType(currentSubject);
        return `MatchConstraint(convert(pattern), currentSubject, type);
      }

      Cst_MatchTermConstraint(pattern,subject,typeConstraint) -> {
        BQTerm currentSubject = convert(`subject);
        TomType type = null;
        %match (typeConstraint) {
          Cst_TypeUnknown -> { type = getTomType(`currentSubject); }
          Cst_Type(name) -> { 
            TomSymbol symbol = symbolTable.getSymbolFromName(`name);
            if(symbol!=null) {
              type = symbol.getTypesToType().getCodomain();
            }
          }
        }
        return `MatchConstraint(convert(pattern), currentSubject , type);
      }

      Cst_AndConstraint() -> {
        return `TrueConstraint();
      }

      Cst_AndConstraint(head,tail*) -> {
        Constraint chead = convert(`head,subjectList,subjectIndex);
        Constraint ctail = convert(`tail,subjectList,subjectIndex);
        return `AndConstraint(chead,ctail); 
      }

      Cst_OrConstraint(head,tail*) -> {
        Constraint chead = convert(`head,subjectList,subjectIndex);
        Constraint ctail = convert(`tail,subjectList,subjectIndex);
        return `OrConstraint(chead,ctail); 
      }

    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomTerm convert(CstPattern cst) {
    // TODO: define option
    %match(cst) {
      Cst_Variable(name) -> {
        return `Variable(concOption(),Name(name),SymbolTable.TYPE_UNKNOWN,concConstraint());
      }
      Cst_VariableStar(name) -> {
        return `VariableStar(concOption(),Name(name),SymbolTable.TYPE_UNKNOWN,concConstraint());
      }
      Cst_UnamedVariable() -> {
        return `Variable(concOption(),EmptyName(),SymbolTable.TYPE_UNKNOWN,concConstraint());
      }
      Cst_UnamedVariableStar() -> {
        return `VariableStar(concOption(),EmptyName(),SymbolTable.TYPE_UNKNOWN,concConstraint());
      }

      Cst_Appl(symbolList, patternList) -> {
        OptionList optionList = `concOption();
        TomNameList nameList = convert(`symbolList); 
        TomList argList = convert(`patternList); 
        ConstraintList constraintList = `concConstraint();
        return `TermAppl(optionList,nameList,argList,constraintList);
      }

      Cst_RecordAppl(symbolList, pairPatternList) -> {
        OptionList optionList = `concOption();
        TomNameList nameList = convert(`symbolList); 
        SlotList slotList = convert(`pairPatternList); 
        ConstraintList constraintList = `concConstraint();
        return `RecordAppl(optionList,nameList,slotList,constraintList);
      }

      Cst_AnnotatedPattern(cst_pattern,cst_alias) -> {
        TomTerm pattern = convert(`cst_pattern);
        int line = 0;
        Constraint constraint =  ASTFactory.makeAliasTo(`Name(cst_alias), line, "unknown file");
        %match(pattern) {
          (TermAppl|RecordAppl|XMLAppl|Variable|VariableStar)[Constraints=constraints] -> {
            return pattern.setConstraints(`concConstraint(constraint,constraints*));
          }

          AntiTerm(t) -> {
            ConstraintList constraints = `t.getConstraints();
            return `AntiTerm(t.setConstraints(concConstraint(constraint,constraints*)));
          }
        }
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Slot convert(CstPairPattern cst) {
    %match(cst) {
      Cst_PairPattern(slotName,pattern) -> {
        return `PairSlotAppl(Name(slotName),convert(pattern));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomName convert(CstSymbol cst) {
    List optionList = new LinkedList();
    %match(cst) {
      Cst_Symbol(name,theory) -> {
        return `Name(name);
      }

      Cst_ConstantInt(name) -> {
        ASTFactory.makeIntegerSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }
      Cst_ConstantLong(name) -> {
        ASTFactory.makeLongSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }
      Cst_ConstantChar(name) -> {
        ASTFactory.makeCharSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }
      Cst_ConstantDouble(name) -> {
        ASTFactory.makeDoubleSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }
      Cst_ConstantString(name) -> {
        ASTFactory.makeStringSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }

    }

    throw new TomRuntimeException("convert: strange term: " + cst);
  }


  /*
   * List conversion
   */

  public CodeList convert(CstBlockList cst) {
    %match(cst) {
      ConcCstBlock() -> { 
        return `concCode();
      }
      ConcCstBlock(head,tail*) -> {
        return `concCode(convert(head),convert*(tail));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQTermList convert(CstBQTermList cst) {
    %match(cst) {
      ConcCstBQTerm() -> { 
        return `concBQTerm();
      }
      ConcCstBQTerm(head,tail*) -> {
        return `concBQTerm(convert(head),convert*(tail));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQSlotList convert(CstPairSlotBQTermList cst) {
    %match(cst) {
      ConcCstPairSlotBQTerm() -> { 
        return `concBQSlot();
      }
      ConcCstPairSlotBQTerm(head,tail*) -> {
        return `concBQSlot(convert(head),convert*(tail));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public OptionList convert(CstOptionList cst, String subject) {
    %match(cst) {
      ConcCstOption() -> { 
        return `concOption();
      }
      ConcCstOption(head,tail*) -> {
        OptionList ol = convert(`tail,subject);
        return `concOption(convert(head,subject),ol*);
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public ConstraintInstructionList convert(CstConstraintActionList cst, CstBQTermList subjectList) {
    %match(cst) {
      ConcCstConstraintAction() -> { 
        return `concConstraintInstruction();
      }
      ConcCstConstraintAction(head,tail*) -> {
        ConstraintInstructionList ol = convert(`tail,subjectList);
        return `concConstraintInstruction(convert(head,subjectList),ol*);
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomNameList convert(CstSymbolList cst) {
    %match(cst) {
      ConcCstSymbol() -> { 
        return `concTomName();
      }
      ConcCstSymbol(head,tail*) -> {
        return `concTomName(convert(head),convert*(tail));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomList convert(CstPatternList cst) {
    %match(cst) {
      ConcCstPattern() -> { 
        return `concTomTerm();
      }
      ConcCstPattern(head,tail*) -> {
        return `concTomTerm(convert(head),convert*(tail));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public SlotList convert(CstPairPatternList cst) {
    %match(cst) {
      ConcCstPairPattern() -> { 
        return `concSlot();
      }
      ConcCstPairPattern(head,tail*) -> {
        return `concSlot(convert(head),convert*(tail));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  /*
   * Utilities
   */
  private CstOption getOriginTracking(CstOptionList optionlist) {
    %match(optionlist) {
      ConcCstOption(_*,ot@Cst_OriginTracking[],_*) -> { return `ot; }
    }
    throw new TomRuntimeException("info not found: " + optionlist);
  }

  private BQTerm makeBQVariableFromName(String name, String type, CstOptionList optionList) {
    //if more information is needed: concOption(OriginTracking(Name(name),getStartLine(optionList),getFileName(optionList))),
    return `BQVariable(
        concOption(),
        Name(name),
        Type(concTypeOption(),type,EmptyTargetLanguageType()));
  }

  private HashMap<String,String> usedSlots = new HashMap<String,String>();
  private void putSlotType(String codomain, String slotName, String slotType) {
    String key = codomain+slotName;
    usedSlots.put(key,slotType);
  }

  private String getSlotType(String codomain, String slotName) {
    String key = codomain+slotName;
    return usedSlots.get(key);
  }

  private OptionList addDefaultModule(OptionList ol) {
    return `concOption(ol*,ModuleName("default"));
  }

  private TomType getTomType(BQTerm bqt) {
    %match(bqt) {
      (BQVariable|BQVariableStar)[AstType=type] -> { 
        return `type; 
      }
      
      (BQAppl|BQRecordAppl)[AstName=Name(name)] -> {
        TomSymbol symbol = symbolTable.getSymbolFromName(`name);
        if(symbol!=null) {
          return symbol.getTypesToType().getCodomain();
        }
      }

    }
    return symbolTable.TYPE_UNKNOWN;
  }

}
