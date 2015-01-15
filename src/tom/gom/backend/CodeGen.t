/*
 * Gom
 *
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.backend;

import java.io.Writer;
import java.io.StringWriter;
import java.util.logging.Logger;
import tom.gom.GomMessage;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.code.types.*;
import tom.gom.adt.objects.types.*;

public class CodeGen {

  %include { ../adt/code/Code.tom}

  private static Logger logger = Logger.getLogger("CodeGen");

  private CodeGen() {
    /* Prevent instantiation */
  }

  /**
   * Generate code in a String.
   *
   * @params code the Code to generate
   * @return the generated code
   */
  public static String generateCode(Code code) {
    StringWriter writer = new StringWriter();
    try {
      generateCode(code,writer);
    } catch (java.io.IOException e) {
      GomMessage.error(logger, null, 0, 
          GomMessage.codeGenerationFailure , code);
    }
    return writer.toString();
  }

  /**
   * Generate code in a writer.
   *
   * @params code the Code to generate
   * @params writer where to generate
   */
  public static void generateCode(Code code, Writer writer)
    throws java.io.IOException {
    %match(code) {
      Code(scode) -> {
        writer.write(`scode);
        return;
      }
      (Empty|Cons)[Operator=opdecl] -> {
        %match(opdecl) {
          OperatorDecl[Name=opName,
                       Sort=SortDecl[Name=sortName,ModuleDecl=moduleDecl],
                       Prod=Variadic[]] -> {
            String tName = `opName;
            %match(code) {
              Empty[] -> {
                tName = "Empty" + `opName;
              }
              Cons[] -> {
                tName = "Cons" + `opName;
              }
            }
            String sortNamePackage = `sortName.toLowerCase();
            ClassName className = `ClassName(
                tom.gom.compiler.Compiler.packagePrefix(
                  moduleDecl)+".types."+sortNamePackage,tName);
            writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
            return;
          }
        }
        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, `(opdecl));
        return;
      }
      (IsEmpty|IsCons)[Var=varName,Operator=opdecl] -> {
        %match(opdecl) {
          OperatorDecl[Name=opName,Prod=Variadic[]] -> {
            writer.write(`varName);
            %match(code) {
              IsEmpty[] -> {
                writer.write(".isEmpty");
              }
              IsCons[] -> {
                writer.write(".isCons");
              }
            }
            writer.write(`opName);
            writer.write("()");
            return;
          }
        }
        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, `(opdecl));
        return;
      }
      FullOperatorClass[
          Operator=OperatorDecl[
            Name=opName,
            Sort=SortDecl[
              Name=sortName,
              ModuleDecl=moduleDecl]]] -> {
        String sortNamePackage = `sortName.toLowerCase();
        ClassName className = `ClassName(
            tom.gom.compiler.Compiler.packagePrefix(
              moduleDecl)+".types."+sortNamePackage,opName);
        writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
        return;
      }
      FullSortClass(SortDecl[Name=sortName,ModuleDecl=moduleDecl]) -> {
        ClassName sortClassName = `ClassName(
            tom.gom.compiler.Compiler.packagePrefix(moduleDecl)+".types",sortName);
        writer.write(tom.gom.backend.TemplateClass.fullClassName(sortClassName));
        return;
      }
      ShortSortClass(SortDecl[Name=sortName]) -> {
        writer.write(`sortName);
        return;
      }
      (FullSortClass|ShortSortClass)(BuiltinSortDecl[Name=sortName]) -> {
        writer.write(`sortName);
        return;
      }
      Compare[LCode=lcode,RCode=rcode] -> {
        generateCode(`lcode, writer);
        writer.write(".compareTo(");
        generateCode(`rcode, writer);
        writer.write(")");
        return;
      }
      CodeList() -> { return ; }
      CodeList(h,t*) -> {
        generateCode(`h,writer);
        generateCode(`t,writer);
        return;
      }
    }
    throw new GomRuntimeException("Can't generate code for " + code);
  }
}
