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
  ClassNameList visitorsToAccept;
  ClassNameList operatorList;
  SlotFieldList slotList;

  public SortTemplate(ClassName className,
                      ClassName factoryName,
                      ClassName abstractType,
                      ClassName visitor,
                      ClassNameList visitorsToAccept,
                      ClassNameList operatorList,
                      SlotFieldList slots) {
    super(className);
    this.factoryName = factoryName;
    this.abstractType = abstractType;
    this.visitor = visitor;
    this.visitorsToAccept = visitorsToAccept;
    this.operatorList = operatorList;
    this.slotList = slots;
  }

  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append(%[
package @getPackage()@;        

public abstract class @className()@ extends @fullClassName(abstractType)@ {

  public @fullClassName(abstractType)@ accept(@fullClassName(visitor)@ v) throws jjtraveler.VisitFailure {
    return v.@visitMethod(className)@(this);
  }
@generateAcceptMethods(visitorsToAccept)@

@generateBody()@

}
]%);
    return out.toString();
  }

  public String generateBody() {
    StringBuffer out = new StringBuffer();

    // methods for each operator
    ClassNameList consum = operatorList;
    while (!consum.isEmpty()) {
      ClassName operatorName = consum.getHead();
      consum = consum.getTail();

      out.append(%[
  public boolean @isOperatorMethod(operatorName)@() {
    return false;
  }

]%);
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

      out.append(%[
  public @slotDomain(slot)@ @getMethod(slot)@() {
    throw new UnsupportedOperationException("This @className()@ has no @slot.getName()@");
  }

]%);

      /* Do not generate "setSlot" methods for now
      out.append("\tpublic "+className()+" "+setMethod(slot)+"("+slotDomain(slot)+" _arg) {\n");
      out.append("\t\tthrow new IllegalArgumentException(\"Illegal argument: \" + _arg);\n");
      out.append("\t}\n");
      out.append("\n");
      */

    }

    /* fromTerm method, dispatching to operator classes */
    out.append(%[
  public static @fullClassName()@ fromTerm(aterm.ATerm trm) {
    @fullClassName()@ tmp;
@generateFromTerm("trm","tmp")@
    throw new IllegalArgumentException("This is not a @className()@" + trm);
  }

]%);

    return out.toString();
  }
  
  private String generateFromTerm(String trm, String tmp) {
    StringBuffer out = new StringBuffer();
    ClassNameList consum = operatorList;
    while (!consum.isEmpty()) {
      ClassName operatorName = consum.getHead();
      consum = consum.getTail();
      out.append(%[
    @tmp@ = @fullClassName(operatorName)@.fromTerm(@trm@);
    if (@tmp@ != null) {
      return tmp;
    }
]%);
    }
    return out.toString();
  }

  private String generateAcceptMethods(ClassNameList visitorList) {
    StringBuffer out = new StringBuffer();
    while(!visitorList.isEmpty()) {
      ClassName visitorName = visitorList.getHead();
      visitorList = visitorList.getTail();
      out.append(%[
  public @fullClassName(abstractType)@ accept(@fullClassName(visitorName)@ v) throws jjtraveler.VisitFailure {
    return v.@visitMethod(className)@(this);
  }]%);
    }

    return out.toString();
  }
}
