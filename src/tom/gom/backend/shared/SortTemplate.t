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

import tom.gom.backend.TemplateHookedClass;
import tom.gom.adt.objects.types.*;

public class SortTemplate extends TemplateHookedClass {
  ClassName abstractType;
  ClassName visitor;
  ClassNameList operatorList;
  SlotFieldList slotList;

  public SortTemplate(ClassName className,
                      ClassName abstractType,
                      ClassName visitor,
                      ClassNameList operatorList,
                      SlotFieldList slots,
                      HookList hooks) {
    super(className,hooks);
    this.abstractType = abstractType;
    this.visitor = visitor;
    this.operatorList = operatorList;
    this.slotList = slots;
  }

  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append(%[
package @getPackage()@;        
@generateImport()@

public abstract class @className()@ extends @fullClassName(abstractType)@ @generateInterface()@{

@generateBlock()@

  public @fullClassName(abstractType)@ accept(@fullClassName(visitor)@ v) throws jjtraveler.VisitFailure {
    return v.@visitMethod(className)@(this);
  }

@generateBody()@

}
]%);
    return out.toString();
  }

  public String generateBody() {
    StringBuffer out = new StringBuffer();

    // methods for each operator
    ClassNameList consum = operatorList;
    while (!consum.isEmptyconcClassName()) {
      ClassName operatorName = consum.getHeadconcClassName();
      consum = consum.getTailconcClassName();

      out.append(%[
  public boolean @isOperatorMethod(operatorName)@() {
    return false;
  }

]%);
    }
    // methods for each slot
    while (!slotList.isEmptyconcSlotField()) {
      SlotField slot = slotList.getHeadconcSlotField();
      slotList = slotList.getTailconcSlotField();

      /* Do not generate "hasOp" methods for now
      out.append("\tpublic boolean "+hasMethod(slot)+"() {\n");
      out.append("\t\treturn false;\n");
      out.append("\t}\n");
      out.append("\n");
      */

      out.append(%[
  public @slotDomain(slot)@ @getMethod(slot)@() {
    throw new UnsupportedOperationException("This @className()@ has no @slot.getname()@");
  }

  public @className()@ @setMethod(slot)@(@slotDomain(slot)@ _arg) {
    throw new UnsupportedOperationException("This @className()@ has no @slot.getname()@");
  }

]%);

    }

    /* fromTerm method, dispatching to operator classes */
    out.append(%[
  public static @fullClassName()@ fromTerm(aterm.ATerm trm) {
    @fullClassName()@ tmp;
@generateFromTerm("trm","tmp")@
    throw new IllegalArgumentException("This is not a @className()@ " + trm);
  }

]%);

    /* length and reverse prototypes, only usable on lists */
    out.append(%[
  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public @fullClassName()@ reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
]%);

    return out.toString();
  }
  
  protected String generateInterface() {
    String interfaces = super.generateInterface();
    if (! interfaces.equals("")) return "implements "+interfaces.substring(1);
    else return interfaces;
  }

  private String generateFromTerm(String trm, String tmp) {
    StringBuffer out = new StringBuffer();
    ClassNameList consum = operatorList;
    while (!consum.isEmptyconcClassName()) {
      ClassName operatorName = consum.getHeadconcClassName();
      consum = consum.getTailconcClassName();
      out.append(%[
    @tmp@ = @fullClassName(operatorName)@.fromTerm(@trm@);
    if (@tmp@ != null) {
      return tmp;
    }
]%);
    }
    return out.toString();
  }

}
