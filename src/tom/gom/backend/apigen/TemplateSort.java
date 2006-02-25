/*
 *
 * GOM
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

package tom.gom.backend.apigen;

import tom.gom.backend.TemplateClass;
import tom.gom.adt.objects.types.*;

public class TemplateSort extends TemplateClass {
  ClassName factoryName;
  ClassName abstractType;
  ClassNameList operatorList;
  SlotFieldList slotList;

  public TemplateSort(ClassName className, ClassName factoryName, ClassName abstractType, ClassNameList operatorList, SlotFieldList slots) {
    super(className);
    this.factoryName = factoryName;
    this.abstractType = abstractType;
    this.operatorList = operatorList;
    this.slotList = slots;
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public abstract class "+className()+" extends "+fullClassName(abstractType)+" {\n");
    out.append("\tpublic "+className()+"("+fullClassName(factoryName)+" factory) {\n");
    out.append("\t\tsuper(factory);\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic void init(int hashCode, aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] args) {\n");
    out.append("\t\tsuper.init(hashCode, annos, fun, args);\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] args) {\n");
    out.append("\t\tsuper.initHashCode(annos, fun, args);\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic boolean isEqual("+className()+" peer) {\n");
    out.append("\t\treturn super.isEqual(peer);\n");
    out.append("\t}\n");
    out.append("\n");
    // we shall use a particular method in TemplateClass to generate this
    // function name (and the same in AbstractType)
    out.append("\tpublic boolean isSort"+className()+"() {\n");
    out.append("\t\treturn true;\n");
    out.append("\t}\n");
    out.append("\n");
    
    // methods for each operator
    while (!operatorList.isEmpty()) {
      ClassName operatorName = operatorList.getHead();
      operatorList = operatorList.getTail();

      out.append("\tpublic boolean is"+className(operatorName)+"() {\n");
      out.append("\t\treturn false;\n");
      out.append("\t}\n");
      out.append("\n");
    }
    // methods for each slot
    while (!slotList.isEmpty()) {
      SlotField slot = slotList.getHead();
      slotList = slotList.getTail();

      out.append("\tpublic boolean "+hasMethod(slot)+"() {\n");
      out.append("\t\treturn false;\n");
      out.append("\t}\n");
      out.append("\n");

      out.append("\tpublic "+slotDomain(slot)+" "+getMethod(slot)+"() {\n");
      out.append("\t\tthrow new UnsupportedOperationException(\"This "+className()+" has no "+slot.getName()+"\");\n");
      out.append("\t}\n");
      out.append("\n");

      out.append("\tpublic "+className()+" "+setMethod(slot)+"("+slotDomain(slot)+" _arg) {\n");
      out.append("\t\tthrow new IllegalArgumentException(\"Illegal argument: \" + _arg);\n");
      out.append("\t}\n");
      out.append("\n");
    }

    out.append("}");
    
    return out.toString();
  }
}
