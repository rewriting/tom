/*
 * Gom
 *
 * Copyright (C) 2006 INRIA
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

public class MappingTemplate extends TemplateClass {
  ClassName basicStrategy;
  GomClassList sortClasses;
  GomClassList operatorClasses;

  %include { ../../adt/objects/Objects.tom}

  public MappingTemplate(ClassName className,
                         ClassName basicStrategy,
                         GomClassList sortClasses,
                         GomClassList operatorClasses) {
    super(className);
    this.basicStrategy = basicStrategy;
    this.sortClasses = sortClasses;
    this.operatorClasses = operatorClasses;
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append(%[
%include { string.tom }
%include { int.tom }
%include { double.tom }
%include { aterm.tom }
%include { atermlist.tom }
]%);

    // generate a %typeterm for each class
    %match(GomClassList sortClasses) {
      concGomClass(_*,
          SortClass[className=sortName],
          _*) -> {
        out.append(%[
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
          OperatorClass[className=opName,
                        sortName=sortName,
                        slots=slotList],
          _*) -> {
        out.append("%op "+className(`sortName)+" "+className(`opName)+"("+slotDecl(`slotList)+") {\n");
        out.append("  is_fsym(t) { (t!=null) && t."+isOperatorMethod(`opName)+"() }\n");
        %match(SlotFieldList `slotList) {
          concSlotField(_*,slot@SlotField[name=slotName],_*) -> {
            out.append("  get_slot("+`slotName+", t) ");
            out.append("{ t."+getMethod(`slot)+"() }\n");
          }
        }
        out.append("  make("+slotArgs(`slotList)+") { "+fullClassName(`opName)+".make("+slotArgs(`slotList)+")}\n");
        out.append("}\n");
        out.append("\n");
      }
    }

    // generate a %oplist for each variadic operator
    %match(GomClassList operatorClasses) {
      concGomClass(_*,
          VariadicOperatorClass[className=opName,
                                sortName=sortName,
                                empty=OperatorClass[className=emptyClass],
                                cons=OperatorClass[className=concClass,
                                                   slots=concSlotField(
                                                           head@SlotField[domain=headDomain],
                                                           tail)
                                                   ]
                                ],
          _*) -> {
        out.append(%[
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

    return out.toString();
  }

  private String slotDecl(SlotFieldList slotList) {
    String res = "";
    while(!slotList.isEmptyconcSlotField()) {
      SlotField slot = slotList.getHeadconcSlotField();
      slotList = slotList.getTailconcSlotField();
      if (!res.equals("")) { res += ", "; }
      %match(SlotField slot) {
        SlotField[name=slotName,domain=ClassName[name=domainName]] -> {
          res += `slotName+":"+`domainName;
        }
      }
    }
    return res;
  }

  private String slotArgs(SlotFieldList slotList) {
    String res = "";
    int index = 0;
    while(!slotList.isEmptyconcSlotField()) {
      SlotField slot = slotList.getHeadconcSlotField();
      slotList = slotList.getTailconcSlotField();
      if (!res.equals("")) { res += ", "; }
      res += "t"+index;
      index++;
    }
    return res;
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
