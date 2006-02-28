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

    String classBody = %"
package "%+getPackage()+%";

public class "%+className()+%" extends "%+fullClassName(sortName)+%" {
  private static "%+className()+%" proto = new "%+className()+%"();
  private int hashCode;
  private "%+className()+%"() {};
    
"%+generateMembers()+%"

"%+generateBody()+%"

}"%;
    
    return classBody;
  }

  private String generateBody() {
    StringBuffer out = new StringBuffer();

    /* static constructor */
    out.append(%"
  public static "%+className()+%" make("%+childListWithType()+%") {
    proto.initHashCode("%+childList()+%");
    return ("%+className()+%") shared.SingletonSharedObjectFactory.getInstance().build(proto);
  }
  
  private void init("%+childListWithType()+%", int hashCode) {
"%+generateMembersInit()+%"
    this.hashCode = hashCode;
  }

  private void initHashCode("%+childListWithType()+%") {
"%+generateMembersInit()+%"
  this.hashCode = this.hashFunction();
  }

  /* private name and arity */
  private String getName() {
    return ""%+className()+%"";
  }

  private int getArity() {
    return "%+slotList.getLength()+%";
  }

  /* shared.SharedObject */
  public int hashCode() {
    return this.hashCode;
  }

  public shared.SharedObject duplicate() {
    "%+className()+%" clone = new "%+className()+%"();
    clone.init("%+childList()+%", hashCode);
    return clone;
  }

  public boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof "%+className()+%") {
      "%+className()+%" peer = ("%+className()+%") obj;
      return "%+generateMembersEqualityTest("peer")+%";
    }
    return false;
  }

    "%);


    // TODO !!
    return out.toString();
  }

  private String generateMembers() {
    String res="";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domainClass],_*) -> {
        res += "  private "+fullClassName(`domainClass)+" "+fieldName(`fieldName)+";\n";
      }
    }
    return res;
  }
  private String generateMembersInit() {
    String res = "";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domainClass],_*) -> {
        res += "    this."+fieldName(`fieldName)+" = "+fieldName(`fieldName)+";\n";
      }
    }
    return res;
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
