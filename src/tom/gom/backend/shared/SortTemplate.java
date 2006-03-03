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

import tom.gom.backend.TemplateClass;
import tom.gom.adt.objects.types.*;

public class SortTemplate extends TemplateClass {
  ClassName factoryName;
  ClassName abstractType;
  ClassName visitor;
  ClassNameList operatorList;
  SlotFieldList slotList;

  public SortTemplate(ClassName className,
                      ClassName factoryName,
                      ClassName abstractType,
                      ClassName visitor,
                      ClassNameList operatorList,
                      SlotFieldList slots) {
    super(className);
    this.factoryName = factoryName;
    this.abstractType = abstractType;
    this.visitor = visitor;
    this.operatorList = operatorList;
    this.slotList = slots;
  }

  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public abstract class "+className()+" extends "+fullClassName(abstractType)+" {\n");
    out.append("\n");

    out.append("\tpublic "+fullClassName(abstractType)+" accept("+fullClassName(visitor)+" v) throws jjtraveler.VisitFailure {\n");
    out.append("\t\treturn v."+visitMethod(className)+"(this);\n");
    out.append("\t}\n");

    // methods for each operator
    ClassNameList consum = operatorList;
    while (!consum.isEmpty()) {
      ClassName operatorName = consum.getHead();
      consum = consum.getTail();

      out.append("\tpublic boolean "+isOperatorMethod(operatorName)+"() {\n");
      out.append("\t\treturn false;\n");
      out.append("\t}\n");
      out.append("\n");
    }
    // methods for each slot
    while (!slotList.isEmpty()) {
      SlotField slot = slotList.getHead();
      slotList = slotList.getTail();

      /* Do not generate "hasOp" methods for now
      out.append("\tpublic boolean "+hasMethod(slot)+"() {\n");
      out.append("\t\treturn false;\n");
      out.append("\t}\n");
      out.append("\n");
      */

      out.append("\tpublic "+slotDomain(slot)+" "+getMethod(slot)+"() {\n");
      out.append("\t\tthrow new UnsupportedOperationException(\"This "+className()+" has no "+slot.getName()+"\");\n");
      out.append("\t}\n");
      out.append("\n");

      /* Do not generate "setSlot" methods for now
      out.append("\tpublic "+className()+" "+setMethod(slot)+"("+slotDomain(slot)+" _arg) {\n");
      out.append("\t\tthrow new IllegalArgumentException(\"Illegal argument: \" + _arg);\n");
      out.append("\t}\n");
      out.append("\n");
      */

    }

    /* fromTerm method, dispatching to operator classes */
    out.append("\tpublic static "+fullClassName()+" fromTerm(aterm.ATerm trm) {\n");
    out.append("\t\t"+fullClassName()+" tmp;\n");

    consum = operatorList;
    while (!consum.isEmpty()) {
      ClassName operatorName = consum.getHead();
      consum = consum.getTail();
      out.append("\t\ttmp = "+fullClassName(operatorName)+".fromTerm(trm);\n");
      out.append("\t\tif (tmp != null) {\n");
      out.append("\t\t\treturn tmp;\n");
      out.append("\t\t}\n");
    }
    out.append("\t\tthrow new IllegalArgumentException(\"This is not a "+className()+"\" + trm);\n");
    out.append("\t}\n");

    out.append("}");

    return out.toString();
  }
}
