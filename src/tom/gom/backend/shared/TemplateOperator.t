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

public class TemplateOperator extends TemplateClass {
  ClassName factoryName;
  ClassName abstractType;
  ClassName sortName;
  ClassName visitor;
  SlotFieldList slotList;

  %include { ../../adt/objects/Objects.tom}

  public TemplateOperator(ClassName className, ClassName factoryName, ClassName abstractType, ClassName sortName, ClassName visitor, SlotFieldList slots) {
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
    out.append("\tprivate int hashCode;\n");
    out.append("\n");
    generateMembers(out);

    // TODO !!

    out.append("}");
    
    return out.toString();
  }

  private void generateMembers(StringBuffer out) {
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domainClass],_*) -> {
        out.append("\tprivate "+fullClassName(`domainClass)+" "+`fieldName+";\n");
      }
    }
  }
}
