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

  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
package @getPackage()@;        
@generateImport()@

public abstract class @className()@ extends @fullClassName(abstractType)@ @generateInterface()@{

@generateBlock()@

  public @fullClassName(abstractType)@ accept(@fullClassName(visitor)@ v) throws jjtraveler.VisitFailure {
    return v.@visitMethod(className)@(this);
  }
]%);
generateBody(writer);
writer.write(%[
}
]%);
  }

  public void generateBody(java.io.Writer writer) throws java.io.IOException {
    // methods for each operator
    ClassNameList consum = operatorList;
    while (!consum.isEmptyconcClassName()) {
      ClassName operatorName = consum.getHeadconcClassName();
      consum = consum.getTailconcClassName();

      writer.write(%[
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
      writer.write("\tpublic boolean "+hasMethod(slot)+"() {\n");
      writer.write("\t\treturn false;\n");
      writer.write("\t}\n");
      writer.write("\n");
      */

      writer.write(%[
  public @slotDomain(slot)@ @getMethod(slot)@() {
    throw new UnsupportedOperationException("This @className()@ has no @slot.getname()@");
  }

  public @className()@ @setMethod(slot)@(@slotDomain(slot)@ _arg) {
    throw new UnsupportedOperationException("This @className()@ has no @slot.getname()@");
  }

]%);

    }

    /* fromTerm method, dispatching to operator classes */
    writer.write(%[
  public static @fullClassName()@ fromTerm(aterm.ATerm trm) {
    @fullClassName()@ tmp;
]%);
    generateFromTerm(writer,"trm","tmp");
    writer.write(%[
    throw new IllegalArgumentException("This is not a @className()@ " + trm);
  }

]%);

    /* length and reverse prototypes, only usable on lists */
    writer.write(%[
  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public @fullClassName()@ reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
]%);
  }
  
  protected String generateInterface() {
    String interfaces = super.generateInterface();
    if (! interfaces.equals("")) return "implements "+interfaces.substring(1);
    else return interfaces;
  }

  private void generateFromTerm(java.io.Writer writer, String trm, String tmp) throws java.io.IOException {
    ClassNameList consum = operatorList;
    while (!consum.isEmptyconcClassName()) {
      ClassName operatorName = consum.getHeadconcClassName();
      consum = consum.getTailconcClassName();
      writer.write(%[
    @tmp@ = @fullClassName(operatorName)@.fromTerm(@trm@);
    if (@tmp@ != null) {
      return tmp;
    }
]%);
    }
  }

}
