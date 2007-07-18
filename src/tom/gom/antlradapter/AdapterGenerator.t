/*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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
  private List importList = null;
  private String grammarName = "";

  AdapterGenerator(File tomHomePath, List importList, String grammar) {
    this.tomHomePath = tomHomePath;
    this.importList = importList;
    this.grammarName = grammar;
  }

  %include { ../adt/gom/Gom.tom}
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom }
  %typeterm Writer {
    implement { Writer }
    is_sort(t) { t instanceof Writer }
  }

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public void generate(ModuleList moduleList, HookDeclList hookDecls) {
    writeTokenFile(moduleList);
    writeAdapterFile(moduleList);
    writeTreeFile(moduleList);
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

  public int writeTreeFile(ModuleList moduleList) {
    try {
       File output = treeFileToGenerate();
       // make sure the directory exists
       output.getParentFile().mkdirs();
       Writer writer =
         new BufferedWriter(
             new OutputStreamWriter(
               new FileOutputStream(output)));
       generateTreeFile(moduleList, writer);
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
    String packagePrefix =
      environment()
        .getStreamManager()
          .getPackagePath().replace(File.separatorChar,'.');
    return
      (packagePrefix==""?filename():packagePrefix+filename()).toLowerCase();
  }

  public void generateAdapterFile(ModuleList moduleList, Writer writer)
    throws java.io.IOException {
    writer.write(
    %[
package @adapterPkg()@;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

public class @filename()@Adaptor extends CommonTreeAdaptor {

	public Object create(Token payload) {
		return new @filename()@Tree(payload);
	}

}
]%);
  }

  public void generateTreeFile(ModuleList moduleList, Writer writer)
    throws java.io.IOException {
    writer.write(%[
package @adapterPkg()@;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.*;

public class @filename()@Tree extends CommonTree {

  private int termIndex = 0;
  private int childCount = 0;
  /* Use SharedObject as type, as most general type */
  private shared.SharedObject inAstTerm = null;

  public @filename()@Tree(CommonTree node) {
    super(node);
    this.token = node.token;
    initAstTerm(token);
  }

  public @filename()@Tree(Token t) {
    this.token = t;
    initAstTerm(t);
  }

  public shared.SharedObject getTerm() {
    return inAstTerm;
  }

  private void initAstTerm(Token t) {
    if(null==t) {
      return;
    }
    switch (t.getType()) {
]%);
    /* generate case statement for each constructor */
    try {
      `TopDown(GenerateInitCase(writer, grammarName)).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("GenerateInitCase should not fail");
    }
    writer.write(%[
    }
  }
]%);
    /* Add fields for each slot */
    try {
      `TopDown(GenerateSlots(writer)).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("GenerateSlots should not fail");
    }

    writer.write(%[

  public void addChild(Tree t) {
    super.addChild(t);
    if (null==t) {
      return;
    }
    /* Depending on the token number and the child count, fill the correct field */

    termIndex++;
    /* Instantiate the term if needed */
    }
  }
]%);
    writer.write(%[
}        
]%);
  }

  public void generateTokenFile(ModuleList moduleList, Writer writer)
    throws java.io.IOException {
    Collection bag = new HashSet();
    try {
      `TopDown(CollectOperators(bag)).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("CollectOperators should not fail");
    }
    Iterator it = bag.iterator();
    while(it.hasNext()) {
      String op = (String) it.next();
      writer.write(op + ";\n");
    }
  }

  %strategy CollectOperators(bag:Collection) extends Identity() {
    visit OperatorDecl {
      OperatorDecl[Name=name] -> {
        bag.add(`name);
      }
    }
  }

  %strategy GenerateSlots(writer:Writer) extends Identity() {
    visit OperatorDecl {
      OperatorDecl[Prod=Slots[Slots=
                     concSlot(_*,Slot[Name=name,Sort=sortDecl],_*)]] -> {
        Code code =
          `CodeList(
              Code("  "),
              FullSortClass(sortDecl),
              Code(" "),
              Code(name),
              Code(";\n")
           );
        try {
          CodeGen.generateCode(code,writer);
        } catch (IOException e) {
          throw new VisitFailure("IOException " + e);
        }
      }
      op@OperatorDecl[Name=name,Sort=sortDecl,Prod=Variadic[]] -> {
        Code code =
          `CodeList(
              Code("  "),
              FullSortClass(sortDecl),
              Code(" "),
              Code(name),
              Code("List = "),
              Empty(op),
              Code(".make();\n")
           );
        try {
          CodeGen.generateCode(code,writer);
        } catch (IOException e) {
          throw new VisitFailure("IOException " + e);
        }
      }
    }
  }

  %strategy GenerateInitCase(writer:Writer,grammar:String) extends Identity() {
    visit OperatorDecl {
      opDecl@OperatorDecl[Name=opName,
                   Prod=Slots[Slots=
                     slotList@concSlot(_*,Slot[Sort=sortDecl],_*)]] -> {
        Code initTerm = `Code("");
        if(0 == `slotList.length()) {
          initTerm = `CodeList(
              Code("        inAstTerm = "),
              FullOperatorClass(opDecl),
              Code(".make();\n")
              );
        }
        Code code =
          `CodeList(
              Code("      case "+grammar+"Parser."),
              Code(opName),
              Code(":\n"),
              Code("        childCount = "),
              Code(Integer.toString(slotList.length())),
              Code(";\n"),
              initTerm,
              Code("        break;\n")
           );
        try {
          CodeGen.generateCode(code,writer);
        } catch (IOException e) {
          throw new VisitFailure("IOException " + e);
        }
      }
    }
  }

  protected String filename() {
    String filename =
      (new File(environment().getStreamManager().getOutputFileName())).getName();
    int dotidx = filename.indexOf('.');
    if(-1 != dotidx) {
      filename = filename.substring(0,dotidx);
    }
    return filename;
  }

  protected File tokenFileToGenerate() {
    File output = new File(
        environment().getStreamManager().getDestDir(),
        filename()+"TokenList.txt");
    return output;
  }

  protected File adaptorFileToGenerate() {
    File output = new File(
        environment().getStreamManager().getDestDir(),
        filename()+"Adaptor.java");
    return output;
  }

  protected File treeFileToGenerate() {
    File output = new File(
        environment().getStreamManager().getDestDir(),
        filename()+"Tree.java");
    return output;
  }
}
