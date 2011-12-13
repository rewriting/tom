/*
 * Gom
 *
 * Copyright (c) 2006-2011, INPL, INRIA
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

import java.io.*;
import java.util.*;

import tom.gom.backend.TemplateClass;
import tom.gom.backend.TemplateHookedClass;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.tools.GomEnvironment;
import tom.platform.OptionManager;

public class SortTemplate extends TemplateHookedClass {
  ClassName abstractType;
  ClassNameList operatorList;
  ClassNameList variadicOperatorList;
  SlotFieldList slotList;
  boolean maximalsharing;

  %include { ../../adt/objects/Objects.tom}

  public SortTemplate(File tomHomePath,
                      OptionManager manager,
                      boolean maximalsharing,
                      List importList, 	
                      GomClass gomClass,
                      TemplateClass mapping,
                      GomEnvironment gomEnvironment) {
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    this.maximalsharing = maximalsharing;
    %match(gomClass) {
      SortClass[AbstractType=abstractType,
                Operators=ops,
                VariadicOperators=variops,
                SlotFields=slots] -> {
        this.abstractType = `abstractType;
        this.operatorList = `ops;
        this.variadicOperatorList = `variops;
        this.slotList = `slots;
        return;
      }
    }
    throw new GomRuntimeException(
        "Bad argument for SortTemplate: " + gomClass);
  }

  protected String generateInterface() {
    String interfaces =  super.generateInterface();
    if (interfaces.equals("")) return "";
    else return %[implements @interfaces.substring(1)@]%;
  }

  public void generateSpec(java.io.Writer writer) throws java.io.IOException {

writer.write(%[

with @getPackage()@;
use @getPackage()@;

]%);


    // methods for each operator
    ClassNameList consum = operatorList;
    while (!consum.isEmptyConcClassName()) {
      ClassName operatorName = consum.getHeadConcClassName();
      consum = consum.getTailConcClassName();

      writer.write(%[

function @isOperatorMethod(operatorName)@(this: @className()@) return Boolean;

 ]%);
    }
    // methods for each slot
    SlotFieldList sl = slotList;
    while (!sl.isEmptyConcSlotField()) {
      SlotField slot = sl.getHeadConcSlotField();
      sl = sl.getTailConcSlotField();

      writer.write(%[

function @getMethod(slot)@(this: @className()@) return @slotDomain(slot)@''Class ]%);

    }

}

  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
with @getPackage()@;
use @getPackage()@;
@generateImport()@

package body @fullClassName(abstractType)@.@className()@ is 

pseudo_Abstract_Called : Exception; 

@generateBlock()@
]%);
generateBody(writer);
writer.write(%[
end @fullClassName(abstractType)@.@className()@ ;
]%);
  }

  public void generateBody(java.io.Writer writer) throws java.io.IOException {
    // methods for each operator
    ClassNameList consum = operatorList;
    while (!consum.isEmptyConcClassName()) {
      ClassName operatorName = consum.getHeadConcClassName();
      consum = consum.getTailConcClassName();

      writer.write(%[

function @isOperatorMethod(operatorName)@(this: @className()@) return boolean is 
begin
	return false;
end;

]%);
    }
    // methods for each slot
    SlotFieldList sl = slotList;
    while (!sl.isEmptyConcSlotField()) {
      SlotField slot = sl.getHeadConcSlotField();
      sl = sl.getTailConcSlotField();

      writer.write(%[

function @getMethod(slot)@(this: @className()@) return @slotDomain(slot)@''Class is
begin
	raise pseudo_Abstract_Called with "This @className()@ has no @slot.getName()@";
	return null;
end;

]%);

    }
}


  public void generateTomMapping(Writer writer) throws java.io.IOException {
    writer.write(%[
%typeterm @className()@ {
  implement { @fullClassName()@ }
  is_sort(t) { ($t in @fullClassName()@) }
]%);
    if(maximalsharing) {
      writer.write(%[
  equals(t1,t2) { ($t1=$t2) }
]%);
    } else {
      writer.write(%[
  equals(t1,t2) { (((@fullClassName()@)$t1).deepEquals($t2)) }
]%);
    }
    writer.write(%[
}
]%);
 }
}
