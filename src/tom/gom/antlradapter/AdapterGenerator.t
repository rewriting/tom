/*
 * Gom
 *
 * Copyright (c) 2006-2008, INRIA
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

package tom.gom.antlradapter;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.backend.CodeGen;
import tom.gom.tools.error.GomRuntimeException;

import tom.gom.adt.code.types.*;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.library.sl.VisitFailure;
import tom.library.sl.Strategy;

public class AdapterGenerator {

  /* Attributes needed to call tom properly */
  private File tomHomePath;
  private GomStreamManager streamManager;
  private String grammarPkg = "";
  private String grammarName = "";

  AdapterGenerator(File tomHomePath, GomStreamManager streamManager, String grammar) {
    this.tomHomePath = tomHomePath;
    this.streamManager = streamManager;
    int lastDot = grammar.lastIndexOf('.');
    if (-1 != lastDot) {
      // the grammar is in a package different from the gom file
      this.grammarPkg = grammar.substring(0,lastDot);
      this.grammarName = grammar.substring(lastDot+1,grammar.length());
    } else {
      this.grammarPkg = streamManager.getDefaultPackagePath();
      this.grammarName = grammar;
    }
  }

  %include { ../adt/gom/Gom.tom}
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom }
  %typeterm Writer {
    implement { Writer }
  }

  public void generate(ModuleList moduleList, HookDeclList hookDecls) {
    writeTokenFile(moduleList);
    writeAdapterFile(moduleList);
    //writeTreeFile(moduleList);
  }

  public int writeTokenFile(ModuleList moduleList) {
    try {
       File output = tokenFileToGenerate();
       // make sure the directory exists
       output.getParentFile().mkdirs();
       Writer writer =
         new BufferedWriter(
             new OutputStreamWriter(
               new FileOutputStream(output)));
       generateTokenFile(moduleList, writer);
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }

  public int writeAdapterFile(ModuleList moduleList) {
    try {
       File output = adaptorFileToGenerate();
       // make sure the directory exists
       output.getParentFile().mkdirs();
       Writer writer =
         new BufferedWriter(
             new OutputStreamWriter(
               new FileOutputStream(output)));
       generateAdapterFile(moduleList, writer);
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }

  private String adapterPkg() {
    String packagePrefix = streamManager.getDefaultPackagePath();
    return ((packagePrefix=="")?filename():packagePrefix+"."+filename()).toLowerCase();
  }

  public void generateAdapterFile(ModuleList moduleList, Writer writer)
    throws java.io.IOException {
    Collection operatorset = new HashSet();
    try {
      `TopDown(CollectOperators(operatorset)).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("CollectOperators should not fail");
    }
    writer.write(
    %[
package @adapterPkg()@;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
]%);
    if (!"".equals(grammarPkg)) {
    writer.write(%[
import @grammarPkg@.@grammarName@Parser;
]%);
    }
    writer.write(%[
public class @filename()@Adaptor {
  public static shared.SharedObject getTerm(CommonTree tree) {
    shared.SharedObject res = null;
    if(tree.isNil()) {
      throw new RuntimeException("nil term");
    }
    if(tree.getType()==Token.INVALID_TOKEN_TYPE) {
      throw new RuntimeException("bad type");
    }
    if(tree.getToken()==null) {
      throw new RuntimeException("null token");
    }

    switch (tree.getType()) {
]%);

    Iterator it = operatorset.iterator();
    while(it.hasNext()) {
      OperatorDecl opDecl = (OperatorDecl) it.next();
      %match(opDecl) {

        op@OperatorDecl[Name=opName,Prod=Variadic[Sort=domainSort]] -> {
          Code child = genGetChild("t","i");
          Code cast = genGetTerm(`domainSort,"t");
          Code code =
            `CodeList(
                Code("      case "+grammarName+"Parser."),
                Code(opName),
                Code(":\n"),
                Code("        {\n"),
                /* create empty list */
                Code("          res = "),
                Empty(op),
                Code(".make();\n"),
                /* add elements */
                Code("          for(int i = 0; i < tree.getChildCount(); i++) {\n"),
                Code("            "),
                child*,
                Code(";\n"),
                Code("            "),
                FullSortClass(domainSort),
                Code(" elem = "),
                cast*,
                Code(";\n"),
                Code("            "),
                FullOperatorClass(op),
                Code(" list = ("),
                FullOperatorClass(op),
                Code(") res;\n"),
                Code("            "),
                Code("res = list.append(elem);\n"),
                Code("          }\n"),
                Code("          break;\n"),
                Code("        }\n"));
          CodeGen.generateCode(code,writer);
        }

      op@OperatorDecl[Name=opName,Sort=sortDecl,Prod=prod@!Variadic[]] -> {
        Code code =
          `CodeList(
              Code("      case "+grammarName+"Parser."),
              Code(opName),
              Code(":\n"),
              Code("        {\n")
              );
        %match(prod) {
          Slots[Slots=slotList] -> {
            int idx = 0;
            SlotList sList = `slotList;
            while(sList.isConsConcSlot()) {
              Slot slot = sList.getHeadConcSlot();
              sList = sList.getTailConcSlot();
              Code child = genGetChild("child"+idx,""+idx);
              Code cast = genGetTerm(slot.getSort(),"child"+idx);
              code = `CodeList(code,
                  Code("          "),
                  child*,
                  Code(";\n"),
                  Code("          "),
                  FullSortClass(slot.getSort()),
                  Code(" field" + idx + " = "),
                  cast*,
                  Code(";\n")
                  );
              idx++;
            }
            code = `CodeList(code,
                Code("          res = "),
                FullOperatorClass(op),
                Code(".make("),
                Code(genArgsList(slotList)),
                Code(");\n"),
                Code("          break;\n"),
                Code("        }\n")
                );
              }

          }
          CodeGen.generateCode(code,writer);
        }

      }
    }

    writer.write(%[
    }
    return res;
  }
}
]%);
  }

  public void generateTokenFile(ModuleList moduleList, Writer writer)
    throws java.io.IOException {
    Collection bag = new HashSet();
    try {
      `TopDown(CollectOperatorNames(bag)).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("CollectOperatorNames should not fail");
    }
    Iterator it = bag.iterator();
    while(it.hasNext()) {
      String op = (String) it.next();
      writer.write(op + ";\n");
    }
  }

  %strategy CollectOperators(bag:Collection) extends Identity() {
    visit OperatorDecl {
      op@OperatorDecl[] -> {
        bag.add(`op);
      }
    }
  }

  %strategy CollectOperatorNames(bag:Collection) extends Identity() {
    visit OperatorDecl {
      OperatorDecl[Name=name] -> {
        bag.add(`name);
      }
    }
  }

  protected Code genGetTerm(SortDecl sort, String tree) {
    Code code = `CodeList();
    %match(sort) {
      SortDecl[] -> {
        code = `CodeList(code,
            Code("("),
            FullSortClass(sort),
            Code(")" + filename() + "Adaptor.getTerm(" + tree + ")"));
      }
      BuiltinSortDecl[Name=name] -> {
        if("int".equals(`name)) {
          code = `CodeList(code,
              Code("Integer.parseInt(" + tree + ".getText())"));
        } else if ("String".equals(`name)) {
          code = `CodeList(code,
              Code(tree + ".getText()"));
        } else {
          throw new RuntimeException("Unsupported builtin");
        }
      }
    }
    return code;
  }

  protected Code genGetChild(String var, String index) {
    Code code = `CodeList(
        Code("CommonTree " + var + " = (CommonTree) tree.getChild(" + index + ")")
        );
    return code;
  }

  protected String genArgsList(SlotList slots) {
    String res = "";
    SlotList sList = slots;
    int idx=0;
    while(sList.isConsConcSlot()) {
      Slot slot = sList.getHeadConcSlot();
      sList = sList.getTailConcSlot();
      res += "field" + idx;
      if(sList.isConsConcSlot()) {
        res += ", ";
      }
      idx++;
    }
    return res;
  }

  protected String fullFileName() {
    return (adapterPkg() + "." + filename()).replace('.',File.separatorChar);
  }

  protected String filename() {
    String filename = (new File(streamManager.getOutputFileName())).getName();
    int dotidx = filename.indexOf('.');
    if(-1 != dotidx) {
      filename = filename.substring(0,dotidx);
    }
    return filename;
  }

  protected File tokenFileToGenerate() {
    File output = new File(
        streamManager.getDestDir(),
        fullFileName()+"TokenList.txt");
    return output;
  }

  protected File adaptorFileToGenerate() {
    File output = new File(
        streamManager.getDestDir(),
        fullFileName()+"Adaptor.java");
    return output;
  }

  protected File treeFileToGenerate() {
    File output = new File(
        streamManager.getDestDir(),
        fullFileName()+"Tree.java");
    return output;
  }
}
