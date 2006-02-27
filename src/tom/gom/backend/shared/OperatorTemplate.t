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
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class OperatorTemplate extends TemplateClass {
  ClassName factoryName;
  ClassName abstractType;
  ClassName sortName;
  ClassName visitor;
  SlotFieldList slotList;

  %include { ../../adt/objects/Objects.tom}

  public OperatorTemplate(ClassName className, ClassName factoryName, ClassName abstractType, ClassName sortName, ClassName visitor, SlotFieldList slots) {
    super(className);
    this.factoryName = factoryName;
    this.abstractType = abstractType;
    this.sortName = sortName;
    this.visitor = visitor;
    this.slotList = slots;
  }

  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public class "+className()+" extends "+fullClassName(sortName)+" {\n");
    out.append("\tprivate static "+className()+" proto = new "+className()+"();\n");
    out.append("\tprivate int hashCode;\n");
    out.append("\tprivate "+className()+"() {};\n");
    out.append("\n");
    generateMembers(out);
    out.append("\n");

    /* static constructor */
    out.append("\tpublic static "+className()+" make("+childListWithType()+") {\n");
    out.append("\t\tproto.initHashCode("+childList()+");\n");
    out.append("\t\treturn ("+className()+") shared.SingletonSharedObjectFactory.getInstance().build(proto);\n");
    out.append("\t}\n");
    out.append("\n");

    /* private init */
    out.append("\tprivate void init("+childListWithType()+", int hashCode) {\n");
    generateMembersInit(out); /* this.member = _member; */
    out.append("\t\tthis.hashCode = hashCode;\n");
    out.append("\t}\n");

    out.append("\tprivate void initHashCode("+childListWithType()+") {\n");
    generateMembersInit(out); /* this.member = _member; */
    out.append("\t\tthis.hashCode = this.hashFunction();\n");
    out.append("\t}\n");
    out.append("\n");

    /* private name and arity */
    out.append("\tprivate String getName() {\n");
    out.append("\t\treturn \""+className()+"\";\n");
    out.append("\t}\n");

    out.append("\tprivate int getArity() {\n");
    out.append("\t\treturn "+slotList.getLength()+";\n");
    out.append("\t}\n");
    out.append("\n");

    /* implements shared.SharedObject */
    out.append("\t/* shared.SharedObject */\n");

    out.append("\tpublic int hashCode() {\n");
    out.append("\t\treturn this.hashCode;\n");
    out.append("\t}\n");

    out.append("\tpublic shared.SharedObject duplicate() {\n");
    out.append("\t\t"+className()+" clone = new "+className()+"();\n");
    out.append("\t\tclone.init("+childList()+", hashCode);\n");
    out.append("\t\treturn clone;\n");
    out.append("\t}\n");

    out.append("\tpublic boolean equivalent(shared.SharedObject obj) {\n");
    out.append("\t\tif(obj instanceof "+className()+") {\n");
    out.append("\t\t\t"+className()+" peer = ("+className()+") obj;\n");
    out.append("\t\t\treturn "+generateMembersEqualityTest("peer")+";\n");
    out.append("\t\t}\n");
    out.append("\t\treturn false;\n");
    out.append("\t}\n");
    out.append("\n");


    // TODO !!

    out.append("}");
    
    return out.toString();
  }

  private void generateMembers(StringBuffer out) {
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domainClass],_*) -> {
        out.append("\tprivate "+fullClassName(`domainClass)+" "+fieldName(`fieldName)+";\n");
      }
    }
  }
  private void generateMembersInit(StringBuffer out) {
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domainClass],_*) -> {
        out.append("\t\tthis."+`fieldName+" = "+fieldName(`fieldName)+";\n");
      }
    }
  }
  private String fieldName(String fieldName) {
    return "_"+fieldName;
  }
  private String childListWithType() {
    String res = "";
    SlotFieldList slots = slotList;
    while(!slots.isEmpty()) {
      SlotField head = slotList.getHead();
      slots = slots.getTail();
      %match(SlotField head) {
        SlotField[name=name, domain=domain] -> {
          if (!res.equals("")) {
            res+= ", ";
          }
          res+= fullClassName(`domain) + " "+fieldName(`name);
        }
      }
    }
    return res;
  }     
  private String childList() {
    String res = "";
    SlotFieldList slots = slotList;
    while(!slots.isEmpty()) {
      SlotField head = slotList.getHead();
      slots = slots.getTail();
      %match(SlotField head) {
        SlotField[name=name, domain=domain] -> {
          if (!res.equals("")) {
            res+= ", ";
          }
          res+= " "+fieldName(`name);
        }
      }
    }
    return res;
  }     
  private String generateMembersEqualityTest(String peer) {
    String res = "";
    %match(SlotFieldList slotList) {
      concSlotField(_*,slot@SlotField[name=fieldName],_*) -> {
        if (!res.equals("")) {
          res+= " && ";
        }
        res += fieldName(`fieldName)+"=="+peer+"."+getMethod(`slot)+"()";
      }
    }
    return res;
  }

}
