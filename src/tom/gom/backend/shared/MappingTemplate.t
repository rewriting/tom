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

package tom.gom.backend.shared;

import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.backend.TemplateClass;
import java.io.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class MappingTemplate extends TemplateClass {
  ClassName basicStrategy;
  GomClassList sortClasses;
  GomClassList operatorClasses;

  %include { ../../adt/objects/Objects.tom}

  public MappingTemplate(GomClass gomClass) {
    super(gomClass);
    %match(gomClass) {
      TomMapping[BasicStrategy=basicStrategy,
                 SortClasses=sortClasses,
                 OperatorClasses=ops] -> {
        this.basicStrategy = `basicStrategy;
        this.sortClasses = `sortClasses;
        this.operatorClasses = `ops;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for MappingTemplate: " + gomClass);
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public void generate(java.io.Writer writer) throws java.io.IOException {
    if(GomEnvironment.getInstance().isBuiltinSort("boolean")) {
      writer.write(%[
%include { boolean.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("String")) {
      writer.write(%[
%include { string.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("int")) {
      writer.write(%[
%include { int.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("char")) {
      writer.write(%[
%include { char.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("double")) {
      writer.write(%[
%include { double.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("long")) {
      writer.write(%[
%include { long.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("float")) {
      writer.write(%[
%include { float.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("ATerm")) {
      writer.write(%[
%include { aterm.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("ATermList")) {
      writer.write(%[
%include { atermlist.tom }
]%);
    }

    // generate a %typeterm for each class
    %match(GomClassList sortClasses) {
      concGomClass(_*,
          SortClass[ClassName=sortName],
          _*) -> {
        writer.write(%[
%typeterm @className(`sortName)@ {
  implement { @fullClassName(`sortName)@ }
  equals(t1,t2) { t1.equals(t2) }
  visitor_fwd { @fullClassName(basicStrategy)@ }
}

]%);
      }
    }

    // generate a %op for each operator
    %match(GomClassList operatorClasses) {
      concGomClass(_*,
          OperatorClass[ClassName=opName,
                        SortName=sortName,
                        Slots=slotList],
          _*) -> {
        writer.write("%op "+className(`sortName)+" "+className(`opName)+"(");
        slotDecl(writer,`slotList);
        writer.write(") {\n");
        //writer.write("  is_fsym(t) { (t!=null) && t."+isOperatorMethod(`opName)+"() }\n");
        writer.write("  is_fsym(t) { t instanceof "+fullClassName(`opName)+" }\n");
        %match(SlotFieldList `slotList) {
          concSlotField(_*,slot@SlotField[Name=slotName],_*) -> {
            writer.write("  get_slot("+`slotName+", t) ");
            writer.write("{ t."+getMethod(`slot)+"() }\n");
          }
        }
        writer.write("  make(");
        slotArgs(writer,`slotList);
        writer.write(") { ");
        writer.write(fullClassName(`opName));
        writer.write(".make(");
        slotArgs(writer,`slotList);
        writer.write(")}\n");
        writer.write("}\n");
        writer.write("\n");
      }
    }

    // generate a %oplist for each variadic operator
    %match(GomClassList operatorClasses) {
      concGomClass(_*,
          VariadicOperatorClass[ClassName=opName,
                                SortName=sortName,
                                Empty=OperatorClass[ClassName=emptyClass],
                                Cons=OperatorClass[ClassName=concClass,
                                                   Slots=concSlotField(
                                                           head@SlotField[Domain=headDomain],
                                                           tail)
                                                   ]
                                ],
          _*) -> {
        if(`sortName == `headDomain) { /* handle List = conc(List*) case */
          writer.write(%[
%oplist @className(`sortName)@ @className(`opName)@(@className(`headDomain)@*) {
  is_fsym(t) { t instanceof @fullClassName(`sortName)@ }
  make_empty() { @fullClassName(`emptyClass)@.make() }
  make_insert(e,l) { @fullClassName(`concClass)@.make(e,l) }
  get_head(l) { (l.@isOperatorMethod(`concClass)@())?(l.@getMethod(`head)@()):(l) }
  get_tail(l) { (l.@isOperatorMethod(`concClass)@())?(l.@getMethod(`tail)@()):(@fullClassName(`emptyClass)@.make()) }
  is_empty(l) { l.@isOperatorMethod(`emptyClass)@() }
}
]%);
        } else {
          writer.write(%[
%oplist @className(`sortName)@ @className(`opName)@(@className(`headDomain)@*) {
  is_fsym(t) { t instanceof @fullClassName(`concClass)@ || t instanceof @fullClassName(`emptyClass)@ }
  make_empty() { @fullClassName(`emptyClass)@.make() }
  make_insert(e,l) { @fullClassName(`concClass)@.make(e,l) }
  get_head(l) { l.@getMethod(`head)@() }
  get_tail(l) { l.@getMethod(`tail)@() }
  is_empty(l) { l.@isOperatorMethod(`emptyClass)@() }
}
]%);

        }
      }
    }
  }

  private void slotDecl(java.io.Writer writer, SlotFieldList slotList) throws java.io.IOException {
    int index = 0;
    while(!slotList.isEmptyconcSlotField()) {
      SlotField slot = slotList.getHeadconcSlotField();
      slotList = slotList.getTailconcSlotField();
      if (index>0) { writer.write(", "); }
      %match(SlotField slot) {
        SlotField[Name=slotName,Domain=ClassName[Name=domainName]] -> {
          writer.write(`slotName);
          writer.write(":");
          writer.write(`domainName);
          index++;
        }
      }
    }
  }

  private void slotArgs(java.io.Writer writer, SlotFieldList slotList) throws java.io.IOException {
    int index = 0;
    while(!slotList.isEmptyconcSlotField()) {
      SlotField slot = slotList.getHeadconcSlotField();
      slotList = slotList.getTailconcSlotField();
      if (index>0) { writer.write(", "); }
      // Warning: do not write the 'index' alone, this would be a strange character
      writer.write("t"+index);
      index++;
    }
  }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".tom";
  }

  protected File fileToGenerate() {
    GomStreamManager stream = GomEnvironment.getInstance().getStreamManager();
    File output = new File(stream.getDestDir(),fileName());
    // log the generated mapping file name
    try {
      GomEnvironment.getInstance().setLastGeneratedMapping(output.getCanonicalPath());
    } catch(Exception e) {
      e.printStackTrace();
    }
    return output;
  }
}
