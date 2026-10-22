/*
 * Gom
 *
 * Copyright (c) 2006-2017, Universite de Lorraine, Inria
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
  // Here, we use a GomEnvironment in spite of the fact that only a GomStreamManager is needed
  // But as other classes have a GomEnvironment, we keep the same model for the moment
  // It is better to stay with one single model whatever class is used
  private GomEnvironment gomEnvironment;
  private String grammarPkg = "";

  AdapterGenerator(File tomHomePath, GomEnvironment gomEnvironment) {
    this.tomHomePath = tomHomePath;
    this.gomEnvironment = gomEnvironment;
    this.grammarPkg = getStreamManager().getDefaultPackagePath();
  }

  private GomStreamManager getStreamManager() {
    return gomEnvironment.getStreamManager();
  }

  %include { ../adt/gom/Gom.tom}
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom }
  %typeterm Writer {
    implement { Writer }
  }

  public void generate(ModuleList moduleList, HookDeclList hookDecls) {
    final Map<OperatorDecl,Integer> operatormap = new HashMap<OperatorDecl,Integer>();
    IntRef intref = new IntRef(10);
    try {
      `TopDown(CollectOperators(operatormap, intref)).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("CollectOperators should not fail");
    }
    writeTokenFile(operatormap);
    writeAdapterFile(operatormap);
  }

  public int writeTokenFile(Map<OperatorDecl,Integer> operatormap) {
    try {
       File output = tokenFileToGenerate();
       // make sure the directory exists
       output.getParentFile().mkdirs();
       Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
       generateTokenFile(operatormap, writer);
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }

  public int writeAdapterFile(Map<OperatorDecl,Integer> operatormap) {
    try {
       File output = adaptorFileToGenerate();
       // make sure the directory exists
       output.getParentFile().mkdirs();
       Writer writer =
         new BufferedWriter(
             new OutputStreamWriter(
               new FileOutputStream(output)));
       generateAdapterFile(operatormap, writer);
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }

  private String adapterPkg() {
    String packagePrefix = getStreamManager().getDefaultPackagePath();
    return ((packagePrefix=="")?filename():packagePrefix+"."+filename()).toLowerCase();
  }

  public void generateAdapterFile(Map<OperatorDecl,Integer> operatormap, Writer writer)
    throws java.io.IOException {
    writer.write(
    %[
package @adapterPkg()@;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
]%);
    if (!"".equals(grammarPkg)) {
    writer.write(%[
]%);
    }
    writer.write(%[
public class @filename()@Adaptor {
  public static tom.library.sl.Visitable getTerm(Tree tree) {
    tom.library.sl.Visitable res = null;
    if (tree.isNil()) {
      throw new RuntimeException("nil term");
    }
    if (tree.getType()==Token.INVALID_TOKEN_TYPE) {
      throw new RuntimeException("bad type");
    }

    switch (tree.getType()) {
]%);

    for (Map.Entry<OperatorDecl,Integer> entry : operatormap.entrySet()) {
      OperatorDecl opDecl = entry.getKey();
      String opkey = entry.getValue().toString();
      %match(opDecl) {

        op@OperatorDecl[Prod=Variadic[Sort=domainSort]] -> {
          Code cast = genGetTerm(`domainSort,"tree.getChild(i)");
          Code code =
            `CodeList(
                Code("      case "),
                Code(opkey),
                Code(":\n"),
                Code("        {\n"),
                /* create empty list */
                Code("          res = "),
                Empty(op),
                Code(".make();\n"),
                /* add elements */
                Code("          for(int i = 0; i < tree.getChildCount(); i++) {\n"),
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

      op@OperatorDecl[Prod=prod@!Variadic[]] -> {
        Code code =
          `CodeList(
              Code("      case "),
              Code(opkey),
              Code(":\n"),
              Code("        {\n")
              );
        %match(prod) {
          Slots[Slots=slotList] -> {
            int idx = 0;
            SlotList sList = `slotList;
            int length = sList.length();
            String sCode = %[
          if (tree.getChildCount()!=@length@) {
            throw new RuntimeException("Node " + tree + ": @length@ child(s) expected, but " + tree.getChildCount() + " found");
          }
]%;
            code = `CodeList(code,Code(sCode));

            while(sList.isConsConcSlot()) {
              Slot slot = sList.getHeadConcSlot();
              sList = sList.getTailConcSlot();
              Code cast = genGetTerm(slot.getSort(),"tree.getChild("+idx+")");
              code = `CodeList(code,
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

  public void generateTokenFile(Map<OperatorDecl,Integer> operatormap, Writer writer)
    throws java.io.IOException {
    for (Map.Entry<OperatorDecl,Integer> entry : operatormap.entrySet()) {
      final OperatorDecl decl = entry.getKey();
      %match(OperatorDecl decl) {
        OperatorDecl[Name=name] -> {
          writer.write(`name + "="+entry.getValue().intValue()+"\n");
        }
      }
    }
  }

  static class IntRef {
    IntRef(int val) {
      intValue = val;
    }
    public int intValue;
  }
  %typeterm IntRef { implement { IntRef } }
  %typeterm OperatorsMap { implement { Map<OperatorDecl,Integer> } }
  %strategy CollectOperators(bag:OperatorsMap,intref:IntRef) extends Identity() {
    visit OperatorDecl {
      op@OperatorDecl[] -> {
        bag.put(`op,Integer.valueOf(intref.intValue));
        intref.intValue++;
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
        if ("int".equals(`name)) {
          code = `CodeList(code,
              Code("Integer.parseInt(" + tree + ".getText())"));
        } else if ("long".equals(`name)) {
          code = `CodeList(code,
              Code("Long.parseLong(" + tree + ".getText())"));
        } else if ("String".equals(`name)) {
          code = `CodeList(code,
              Code(tree + ".getText()"));
        } else if ("boolean".equals(`name)) {
          code = `CodeList(code,
              Code("Boolean.valueOf(" + tree + ".getText())"));
        } else {
          throw new RuntimeException("Unsupported builtin "+`name);
        }
      }
    }
    return code;
  }

  protected String genArgsList(SlotList slots) {
    String res = "";
    SlotList sList = slots;
    int idx = 0;
    while (sList.isConsConcSlot()) {
      Slot slot = sList.getHeadConcSlot();
      sList = sList.getTailConcSlot();
      res += "field" + idx;
      if (sList.isConsConcSlot()) {
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
    String filename = (new File(getStreamManager().getOutputFileName())).getName();
    int dotidx = filename.indexOf('.');
    if (-1 != dotidx) {
      filename = filename.substring(0,dotidx);
    }
    return filename;
  }

  protected File tokenFileToGenerate() {
    File output = new File(
        getStreamManager().getDestDir(),
        fullFileName()+"Tokens.tokens");
    return output;
  }

  protected File adaptorFileToGenerate() {
    File output = new File(
        getStreamManager().getDestDir(),
        fullFileName()+"Adaptor.java");
    return output;
  }
}
