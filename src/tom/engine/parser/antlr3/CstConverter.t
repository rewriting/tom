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

import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class CstConverter {
  %include { ../../../library/mapping/java/sl.tom}
  %include { ../../adt/tomsignature/TomSignature.tom }

  private static Logger logger = Logger.getLogger("tom.engine.typer.CstConverter");

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
                    BQVariable(concOption(),Name(name1),Type(concTypeOption(),typeName,EmptyTargetLanguageType())),
                    BQVariable(concOption(),Name(name2),Type(concTypeOption(),typeName,EmptyTargetLanguageType())),
                    Code(code), 
                    OriginTracking(Name(typeName),getStartLine(optionList2),getFileName(optionList2))
                    );
                declarationList = `concDeclaration(attribute,declarationList*); 
              }

              Cst_IsSort(Cst_Name(name),ConcCstBlock(HOSTBLOCK(optionList2,content))) -> {
                String code = ASTFactory.abstractCode(`content,`name);
                Declaration attribute = `IsSortDecl(
                    BQVariable(concOption(),Name(name),Type(concTypeOption(),typeName,EmptyTargetLanguageType())),
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

  private int[] extractPositionInfo(CstOptionList optionlist) {
    int startline=0; 
    int startcolumn=0;
    int endline=0;
    int endcolumn=0;
    %match(optionlist) {
      ConcCstOption(_*,option,_*) -> {
        %match(option) {
          Cst_StartLine(value)   -> { startline = `value; }
          Cst_StartColumn(value) -> { startcolumn = `value; }
          Cst_EndLine(value)     -> { endline = `value; }
          Cst_EndColumn(value)   -> { endcolumn = `value; }
        }
      }
    }
    return new int[] {startline,startcolumn,endline,endcolumn};
  }

}
