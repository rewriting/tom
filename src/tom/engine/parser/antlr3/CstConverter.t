/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2011, INPL, INRIA
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
 **/

package tom.engine.parser.antlr3;

import java.util.logging.Logger;
import java.util.*;

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

import tom.library.sl.*;

public class CstConverter {
  %include { ../../../library/mapping/java/sl.tom}
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
        return `TargetLanguageToCode(TL(content,TextPosition(getStartLine(optionList),getStartColumn(optionList)),TextPosition(getEndLine(optionList),getEndColumn(optionList))));
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
                    OriginTracking(Name(typeName),getStartLine(optionList2),getFileName(optionList2))
                    );
                declarationList = `concDeclaration(attribute,declarationList*); 
              }

              Cst_IsSort(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content,`name);
                Declaration attribute = `IsSortDecl(
                    makeBQVariableFromName(name,typeName,optionList2),
                    Code(code), 
                    OriginTracking(Name(typeName),getStartLine(optionList2),getFileName(optionList2))
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
              OriginTracking(Name(typeName),getStartLine(optionList),getFileName(optionList))
              ));
      }

      Cst_OpConstruct(optionList, Cst_Type(codomain), Cst_Name(opName), slotList, operatorList) -> {
        DeclarationList declarationList = `concDeclaration();
        List<PairNameDecl> pairNameDeclList = new LinkedList<PairNameDecl>();
        List<TomName> slotNameList = new LinkedList<TomName>();
        List<Option> options = new LinkedList<Option>();
        TomTypeList types = `concTomType();

        %match(slotList) {
          ConcCstSlot(_*,Cst_Slot(Cst_Name(slotName),Cst_Type(slotType)),_*) -> {
            TomName astName = ASTFactory.makeName(`slotName);
            if(slotNameList.indexOf(astName) != -1) {
              TomMessage.error(getLogger(),getFileName(`optionList), getStartLine(`optionList),
                  TomMessage.repeatedSlotName,
                  `slotName);
            }
            slotNameList.add(astName);
            pairNameDeclList.add(`PairNameDecl(astName,EmptyDeclaration()));
            types = `concTomType(types*,Type(concTypeOption(),slotType,EmptyTargetLanguageType()));
            String typeOfSlot = getSlotType(`codomain,`slotName);
            if(typeOfSlot != null && !typeOfSlot.equals(`slotType)) {
              TomMessage.warning(getLogger(),getFileName(`optionList), getStartLine(`optionList),
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
                    OriginTracking(Name(codomain),getStartLine(optionList2),getFileName(optionList2))
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
                    OriginTracking(Name(codomain),getStartLine(optionList2),getFileName(optionList2))
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
                  TomMessage.error(getLogger(),getFileName(`optionList), getStartLine(`optionList),
                      msg,
                      getFileName(`optionList2), getStartLine(`optionList2),
                      "%op "+ `codomain, getStartLine(`optionList),sName.getString());
                } else {
                  pairNameDeclList.set(index,`PairNameDecl(sName,attribute));
                }
              }

              Cst_Make(nameList,ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
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
                    OriginTracking(Name(codomain),getStartLine(optionList2),getFileName(optionList2))
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



    }
    return `Tom(concCode());
    //throw new TomRuntimeException("convert: strange term: " + cst);
  }

  /*
  public Declaration convert(CstOperator cst) {
    %match(cst) {
      Cst_IsSort(arg,blockList) -> {
        return `IsSortDecl(

              OriginTracking(Name(typeName),getStartLine(optionList),getFileName(optionList))
            );
      }

    }

    return `Tom(concCode());
    //throw new TomRuntimeException("convert: strange term: " + cst);
  }
  */


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

  /*
   * Utilities
   */

  private int getStartLine(CstOptionList optionlist) {
    %match(optionlist) {
      ConcCstOption(_*,Cst_StartLine(value),_*) -> { return `value; }
    }
    throw new TomRuntimeException("info not found: " + optionlist);
  }

  private int getEndLine(CstOptionList optionlist) {
    %match(optionlist) {
      ConcCstOption(_*,Cst_EndLine(value),_*) -> { return `value; }
    }
    throw new TomRuntimeException("info not found: " + optionlist);
  }

  private int getStartColumn(CstOptionList optionlist) {
    %match(optionlist) {
      ConcCstOption(_*,Cst_StartColumn(value),_*) -> { return `value; }
    }
    throw new TomRuntimeException("info not found: " + optionlist);
  }

  private int getEndColumn(CstOptionList optionlist) {
    %match(optionlist) {
      ConcCstOption(_*,Cst_EndColumn(value),_*) -> { return `value; }
    }
    throw new TomRuntimeException("info not found: " + optionlist);
  }

  private String getFileName(CstOptionList optionlist) {
    %match(optionlist) {
      ConcCstOption(_*,Cst_SourceFile(value),_*) -> { return `value; }
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
}
