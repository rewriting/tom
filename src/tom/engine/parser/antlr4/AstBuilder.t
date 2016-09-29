/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2016, Universite de Lorraine
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

public class AstBuilder {
  // %include { ../../../library/mapping/java/sl.tom}
  %include { ../../adt/tomsignature/TomSignature.tom }

  private static Logger logger = Logger.getLogger("tom.engine.typer.AstBuilder");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private SymbolTable symbolTable;

  public AstBuilder(SymbolTable st) {
    this.symbolTable = st;
  }

  public Code convert(CstProgram cst) {
    %match(cst) {
      Cst_Program(list) -> { 
        return `Tom(convertToCodeList(list));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Instruction convert(CstBlock cst) {
    %match(cst) {
      HOSTBLOCK(optionList,content) -> {
        CstOption ot = getOriginTracking(`optionList);
        String newline = System.getProperty("line.separator");
        return `CodeToInstruction(TargetLanguageToCode(TL(content + newline,
              TextPosition(ot.getstartLine(),ot.getstartColumn()),
              TextPosition(ot.getendLine()+1,0)))); // add a newline after TL
        /* without extra newline
        return `CodeToInstruction(TargetLanguageToCode(TL(content,
              TextPosition(ot.getstartLine(),ot.getstartColumn()),
              TextPosition(ot.getendLine(),ot.getendColumn()))));
              */
      }

      Cst_BQTermToBlock(bqterm) -> {
        return `BQTermToInstruction(convert(bqterm));
      }

      Cst_MatchConstruct(optionList, arguments, constraintActionList) -> {
        ConstraintInstructionList cil = convert(`constraintActionList,`arguments);
        return `Match(cil,addDefaultModule(convert(optionList,"Match")));
      }

      Cst_StrategyConstruct(optionList, Cst_Name(name), arguments, extendsTerm, visitList) -> {
        TomName strategyName = `Name(name);
        CstOption ot = getOriginTracking(`optionList);
        BQTerm extendsBQTerm = convert(`extendsTerm);
        TomVisitList astVisitList = convert(`visitList);
        Declaration strategyDecl = `Strategy(strategyName,extendsBQTerm,astVisitList,concDeclaration(),convert(ot,name));
        return `CodeToInstruction(DeclarationToCode(strategyDecl));
      }

      Cst_TypetermConstruct(optionList, Cst_Type(typeName), extendsTypeName, operatorList) -> {
        TypeOptionList typeoptionList = `concTypeOption();
        DeclarationList declarationList = `concDeclaration();
        %match(extendsTypeName) {
          Cst_Type(supertypeName) -> { typeoptionList = `concTypeOption(SubtypeDecl(supertypeName)); }
        }
        %match(operatorList) {
          ConcCstOperator(_*,operator,_*) -> {
            %match(operator) {
              Cst_Equals(Cst_Name(name1),Cst_Name(name2),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content,`name1,`name2);
                Declaration attribute = `EqualTermDecl(
                    makeBQVariableFromName(name1,typeName,optionList2),
                    makeBQVariableFromName(name2,typeName,optionList2),
                    Code(code), 
                    makeOriginTracking(typeName,optionList2)
                    );
                declarationList = `concDeclaration(attribute,declarationList*); 
              }

              Cst_IsSort(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content,`name);
                Declaration attribute = `IsSortDecl(
                    makeBQVariableFromName(name,typeName,optionList2),
                    Code(code), 
                    makeOriginTracking(typeName,optionList2)
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

        return `CodeToInstruction(DeclarationToCode(TypeTermDecl(
                Name(typeName),
                declarationList,
                makeOriginTracking(typeName,optionList)
                )));
      }

      Cst_OpConstruct(optionList, Cst_Type(codomain), Cst_Name(opName), slotList, operatorList) -> {
        List<PairNameDecl> pairNameDeclList = new LinkedList<PairNameDecl>();
        List<TomName> slotNameList = new LinkedList<TomName>();
        List<Option> options = new LinkedList<Option>();
        TomTypeList types = `concTomType();

        options.add(`makeOriginTracking(opName,optionList));

        %match(slotList) {
          ConcCstSlot(_*,Cst_Slot(Cst_Name(slotName),Cst_Type(slotType)),_*) -> {
            TomName astName = ASTFactory.makeName(`slotName);
            if(slotNameList.indexOf(astName) != -1) {
              CstOption ot = getOriginTracking(`optionList);
              TomMessage.error(getLogger(),ot.getfileName(), ot.getstartLine(),
                  TomMessage.repeatedSlotName,
                  `slotName);
            }
            slotNameList.add(astName);
            pairNameDeclList.add(`PairNameDecl(astName,EmptyDeclaration()));
            types = `concTomType(types*,makeType(slotType));
            String typeOfSlot = getSlotType(`codomain,`slotName);
            if(typeOfSlot != null && !typeOfSlot.equals(`slotType)) {
              CstOption ot = getOriginTracking(`optionList);
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
                String code = ASTFactory.abstractCode(`content,`name);
                Declaration attribute = `IsFsymDecl(
                    Name(opName),
                    makeBQVariableFromName(name,codomain,optionList2),
                    Code(code), 
                    makeOriginTracking(opName,optionList2)
                    );
                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_GetSlot(Cst_Name(slotName),Cst_Name(argName),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content,`argName);
                Declaration attribute = `GetSlotDecl(
                    Name(opName),
                    Name(slotName),
                    makeBQVariableFromName(argName,codomain,optionList2),
                    Code(code), 
                    makeOriginTracking(opName,optionList2)
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
                  CstOption ot = getOriginTracking(`optionList);
                  CstOption ot2 = getOriginTracking(`optionList2);
                  TomMessage.error(getLogger(),ot.getfileName(), ot.getstartLine(),
                      msg,
                      ot2.getfileName(), ot2.getstartLine(),
                      "%op " + `codomain, ot.getstartLine(),sName.getString());
                } else {
                  pairNameDeclList.set(index,`PairNameDecl(sName,attribute));
                }
              }

              Cst_Make(nameList,blockList@ConcCstBlock(head,_*)) -> {
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

                Declaration attribute = `EmptyDeclaration();
matchblock: {
              %match(blockList) {
                ConcCstBlock(HOSTBLOCK(optionList2,content)) -> {
                  String[] vars = new String[varnameList.size()]; // used only to give a type
                  String code = ASTFactory.abstractCode(`content,varnameList.toArray(vars));
                  attribute = `MakeDecl(
                      Name(opName),
                      makeType(codomain),
                      args,
                      ExpressionToInstruction(Code(code)),
                      makeOriginTracking(opName,optionList2)
                      );
                  break matchblock;
                }

                ConcCstBlock(head,_*) -> {
                  InstructionList il = convert(`blockList);
                  //System.out.println("il: " + il);
                  attribute = `MakeDecl(
                      Name(opName),
                      makeType(codomain),
                      args,
                      AbstractBlock(il),
                      makeOriginTracking(opName,optionList)
                      );
                  break matchblock;
                }

              }
            }

                options.add(`DeclarationToOption(attribute)); 
              }

            }
          }
        }

        TomSymbol astSymbol = ASTFactory.makeSymbol(`opName, `makeType(codomain), types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
        symbolTable.putSymbol(`opName,astSymbol);
        return `CodeToInstruction(DeclarationToCode(SymbolDecl(Name(opName))));
      }

      Cst_OpArrayConstruct(optionList, Cst_Type(codomain), Cst_Name(opName), Cst_Type(domain), operatorList) -> {
        List<Option> options = new LinkedList<Option>();
        options.add(`makeOriginTracking(opName,optionList));

        %match(operatorList) {
          ConcCstOperator(_*,operator,_*) -> {
            %match(operator) {
              Cst_IsFsym(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content,`name);
                Declaration attribute = `IsFsymDecl(
                    Name(opName),
                    makeBQVariableFromName(name,codomain,optionList2),
                    Code(code), 
                    makeOriginTracking(opName,optionList2)
                    );
                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_MakeEmptyArray(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content, `name);
                Instruction instruction = `ExpressionToInstruction(Code(code));
                Declaration attribute = `MakeEmptyArray(
                    Name(opName),
                    makeBQVariableFromName(name,codomain,optionList2),
                    instruction,
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_MakeAppend(Cst_Name(name1),Cst_Name(name2),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content, `name1, `name2);
                Instruction instruction = `ExpressionToInstruction(Code(code));
                Declaration attribute = `MakeAddArray(
                    Name(opName),
                    makeBQVariableFromName(name1,domain,optionList2),
                    makeBQVariableFromName(name2,codomain,optionList2),
                    instruction,
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_GetSize(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content, `name);
                Declaration attribute = `GetSizeDecl(
                    Name(opName),
                    makeBQVariableFromName(name,codomain,optionList2),
                    Code(code),
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_GetElement(Cst_Name(name1),Cst_Name(name2),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content, `name1, `name2);
                Declaration attribute = `GetElementDecl(
                    Name(opName),
                    makeBQVariableFromName(name1,codomain,optionList2),
                    makeBQVariableFromName(name2,"int",optionList2),
                    Code(code),
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }
            }
          }
        }

        PairNameDeclList pairNameDeclList = `concPairNameDecl(PairNameDecl(EmptyName(), EmptyDeclaration()));
        TomTypeList types = `concTomType(makeType(domain));
        TomSymbol astSymbol = ASTFactory.makeSymbol(`opName, `makeType(codomain), types, pairNameDeclList, options);
        symbolTable.putSymbol(`opName,astSymbol);
        return `CodeToInstruction(DeclarationToCode(ArraySymbolDecl(Name(opName))));
      }

      Cst_OpListConstruct(optionList, Cst_Type(codomain), Cst_Name(opName), Cst_Type(domain), operatorList) -> {
        List<Option> options = new LinkedList<Option>();
        options.add(`makeOriginTracking(opName,optionList));

        %match(operatorList) {
          ConcCstOperator(_*,operator,_*) -> {
            %match(operator) {
              Cst_IsFsym(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content,`name);
                Declaration attribute = `IsFsymDecl(
                    Name(opName),
                    makeBQVariableFromName(name,codomain,optionList2),
                    Code(code), 
                    makeOriginTracking(opName,optionList2)
                    );
                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_MakeEmptyList(ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = `content;
                Instruction instruction = `ExpressionToInstruction(Code(code));
                Declaration attribute = `MakeEmptyList(
                    Name(opName),
                    instruction,
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_MakeInsert(Cst_Name(name1),Cst_Name(name2),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content, `name1, `name2);
                Instruction instruction = `ExpressionToInstruction(Code(code));
                Declaration attribute = `MakeAddList(
                    Name(opName),
                    makeBQVariableFromName(name1,domain,optionList2),
                    makeBQVariableFromName(name2,codomain,optionList2),
                    instruction,
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_GetHead(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content, `name);
                Declaration attribute = `GetHeadDecl(
                    Name(opName),
                    symbolTable.getUniversalType(),
                    makeBQVariableFromName(name,codomain,optionList2),
                    Code(code),
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }

              Cst_GetTail(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content, `name);
                Declaration attribute = `GetTailDecl(
                    Name(opName),
                    makeBQVariableFromName(name,codomain,optionList2),
                    Code(code),
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }   
              
              Cst_IsEmpty(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content, `name);
                Declaration attribute = `IsEmptyDecl(
                    Name(opName),
                    makeBQVariableFromName(name,codomain,optionList2),
                    Code(code),
                    makeOriginTracking(opName,optionList2)
                    );

                options.add(`DeclarationToOption(attribute)); 
              }

            }
          }
        }

        PairNameDeclList pairNameDeclList = `concPairNameDecl(PairNameDecl(EmptyName(), EmptyDeclaration()));
        TomTypeList types = `concTomType(makeType(domain));
        TomSymbol astSymbol = ASTFactory.makeSymbol(`opName, `makeType(codomain), types, pairNameDeclList, options);
        symbolTable.putSymbol(`opName,astSymbol);

        return `CodeToInstruction(DeclarationToCode(ListSymbolDecl(Name(opName))));
      }


      // TODO
      //Cst_MetaQuoteConstruct
      Cst_IncludeConstruct(blocks) -> {
        return `CodeToInstruction(TomInclude(convertToCodeList(blocks)));
      }


      Cst_AbstractBlock(blocks) -> {
        return `AbstractBlock(convert(blocks));
      }

    } // end %match
   
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
        return `BQVariable(addDefaultModule(convert(ol,name)),Name(name),makeType(typename));
      }

      Cst_BQVarStar(ol,name,Cst_TypeUnknown()) -> {
        return `BQVariableStar(addDefaultModule(convert(ol,name)),Name(name),SymbolTable.TYPE_UNKNOWN);
      }

      Cst_BQConstant(ol, name) -> {
        return `Composite(CompositeTL(ITL(name)));
      }
      
      //Cst_ITL(ol, code) -> {
      //  return `Composite(CompositeTL(ITL(code)));
      //}

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
        InstructionList instructionList = convert(`action);

        return `ConstraintInstruction(
            convert(constraint,subjectList,currentIndex), 
            RawAction(If(TrueTL(),AbstractBlock(instructionList),Nop())),
            newoptionList);
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Constraint convert(CstConstraint cst, CstBQTermList subjectList, int subjectIndex) {
    %match(cst) {
      Cst_MatchArgumentConstraint(pattern) -> {
        CstBQTermList l = `subjectList;
        if (subjectIndex < 0 || subjectIndex > subjectList.length()) {
          throw new IllegalArgumentException("illegal list index: " + subjectIndex);
        }
        for(int i = 0 ; i < subjectIndex ; i++) {
          l = l.getTailConcCstBQTerm();
        }
        BQTerm currentSubject = convert(l.getHeadConcCstBQTerm());
        TomType type = getTomType(currentSubject);

        //System.out.println("pattern = " + `pattern);
        //System.out.println("subjectIndex = " + subjectIndex);
        //System.out.println("currentSubject = " + currentSubject);
        //System.out.println("type = " + type);

        return `MatchConstraint(convert(pattern), currentSubject, type);
      }

      Cst_MatchTermConstraint(pattern,subject,typeConstraint) -> {
        BQTerm currentSubject = convert(`subject);
        TomType type = null;
        %match(typeConstraint) {
          Cst_TypeUnknown -> { type = getTomType(`currentSubject); }
          Cst_Type(name) -> { 
            TomSymbol symbol = symbolTable.getSymbolFromName(`name);
            if(symbol != null) {
              type = symbol.getTypesToType().getCodomain();
            }
          }
        }
        if(type != null) {
          return `MatchConstraint(convert(pattern), currentSubject , type);
        }
      }

      Cst_AndConstraint() -> {
        return `TrueConstraint();
      }

      Cst_AndConstraint(head,tail*) -> {
        Constraint chead = convert(`head,subjectList,subjectIndex);
        Constraint ctail = convert(`tail,subjectList,subjectIndex+1);
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

      Cst_Constant(symbol) -> {
        OptionList optionList = `concOption();
        ConstraintList constraintList = `concConstraint();
        //return `Variable(concOption(),Name(name),SymbolTable.TYPE_UNKNOWN,concConstraint());
        //return `RecordAppl(concOption(),concTomName(Name(name)),concSlot(),concConstraint());
        TomName name = convert(`symbol); // add symbol to symbolTable
        return `TermAppl(concOption(),concTomName(name),concTomTerm(),concConstraint());
      }
      
      Cst_ConstantStar(symbol) -> {
        OptionList optionList = `concOption();
        ConstraintList constraintList = `concConstraint();
        TomName name = convert(`symbol); // add symbol to symbolTable
        return `VariableStar(concOption(),name,SymbolTable.TYPE_UNKNOWN,concConstraint());
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

  public TomVisit convert(CstVisit cst) {
    %match(cst) {
      Cst_VisitTerm(type@Cst_Type(typename), constraintActionList, ol) -> {
        //TomType matchType = getOptionBooleanValue("newtyper"?SymbolTable.TYPE_UNKNOWN:type);
        // !! the name tom__arg is fixed
        CstBQTermList arguments = `ConcCstBQTerm(Cst_BQVar(ConcCstOption(),"tom__arg",type));
        return `VisitTerm(makeType(typename), convert(constraintActionList,arguments), convert(ol, "VisitTerm"));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }
  /*
   * get the value of a constant
   * and add the constant into the SymbolTable
   */
  public TomName convert(CstSymbol cst) {
    List optionList = new LinkedList();
    %match(cst) {
      Cst_Symbol(name,theory) -> {
        return `Name(name);
      }

      Cst_SymbolInt(name) -> {
        ASTFactory.makeIntegerSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }
      Cst_SymbolLong(name) -> {
        ASTFactory.makeLongSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }
      Cst_SymbolChar(name) -> {
        ASTFactory.makeCharSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }
      Cst_SymbolDouble(name) -> {
        ASTFactory.makeDoubleSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }
      Cst_SymbolString(name) -> {
        ASTFactory.makeStringSymbol(symbolTable,`name,optionList);
        return `Name(name);
      }

    }

    throw new TomRuntimeException("convert: strange term: " + cst);
  }


  /*
   * List conversion
   */

  public CodeList convertToCodeList(CstBlockList cst) {
    %match(cst) {
      ConcCstBlock() -> { 
        return `concCode();
      }
      ConcCstBlock(head,tail*) -> {
        return `concCode(InstructionToCode(convert(head)),convertToCodeList*(tail));
      }
    }
    throw new TomRuntimeException("convert: strange term: " + cst);
  }
 
  public InstructionList convert(CstBlockList cst) {
    %match(cst) {
      ConcCstBlock() -> { 
        return `concInstruction();
      }
      ConcCstBlock(head,tail*) -> {
        return `concInstruction(convert(head),convert*(tail));
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

  public TomVisitList convert(CstVisitList cst) {
    %match(cst) {
      ConcCstVisit() -> { 
        return `concTomVisit();
      }
      ConcCstVisit(head,tail*) -> {
        return `concTomVisit(convert(head),convert*(tail));
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
    Option info = `makeOriginTracking(name,optionList);
    return `BQVariable(concOption(info), Name(name), makeType(type));
  }

  private Option makeOriginTracking(String name, CstOptionList optionList) {
    CstOption ot = getOriginTracking(optionList);
    return `OriginTracking(Name(name),ot.getstartLine(),ot.getfileName());
  }

  private TomType makeType(String type) {
    return `Type(concTypeOption(),type,EmptyTargetLanguageType());
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
