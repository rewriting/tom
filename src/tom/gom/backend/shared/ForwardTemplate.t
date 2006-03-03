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

public class ForwardTemplate extends TemplateClass {
  ClassName visitor;
  ClassName abstractType;
  GomClassList sortClasses;
  GomClassList operatorClasses;

  %include { ../../adt/objects/Objects.tom}

  public ForwardTemplate(ClassName className, ClassName visitor, ClassName abstractType, GomClassList sortClasses, GomClassList operatorClasses) {
    super(className);
    this.visitor = visitor;
    this.abstractType = abstractType;
    this.sortClasses = sortClasses;
    this.operatorClasses = operatorClasses;
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public String generate() {
    StringBuffer out = new StringBuffer();

    /*
    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public class "+className()+" implements "+className(visitor) + ", jjtraveler.Visitor {\n");
    out.append("\tprotected jjtraveler.Visitor any;\n");
    out.append("\n");
    out.append("\tpublic "+className()+"(jjtraveler.Visitor v) {\n");
    out.append("\t\tthis.any = v;\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {\n");
    out.append("\t\tif (v instanceof "+fullClassName(abstractType)+") {\n");
    out.append("\t\t\treturn (("+fullClassName(abstractType)+") v).accept(this);\n");
    out.append("\t\t} else {\n");
    out.append("\t\t\treturn any.visit(v);\n");
    out.append("\t\t}\n");
    out.append("\t}\n");
    */

    out.append(%"
package "%+getPackage()+%";

public class "%+className()+%" implements "%+ className(visitor) +%", jjtraveler.Visitor {
  protected jjtraveler.Visitor any;

  public "%+className()+%"(jjtraveler.Visitor v) {
    this.any = v;
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {
    if (v instanceof "%+fullClassName(abstractType)+%") {
      return (("%+fullClassName(abstractType)+%") v).accept(this);
    } else {
      return any.visit(v);
    }
  }

"%+generateVisitMethods()+%"

}"%);

    return out.toString();
  }
  private String generateVisitMethods() {
    StringBuffer out = new StringBuffer();
    // generate a visit for each sort
    %match(GomClassList sortClasses) {
      concGomClass(_*,SortClass[className=sortName],_*) -> {

        /*
        out.append("\tpublic "+fullClassName(`sortName)+" "+visitMethod(`sortName)+"("+fullClassName(`sortName)+" arg) throws jjtraveler.VisitFailure {\n");
        out.append("\t\treturn ("+fullClassName(`sortName)+") any.visit(arg);\n");
        out.append("\t}\n");
        out.append("\n");
        */

        out.append(%"
  public "%+ fullClassName(`sortName) +%" "%+visitMethod(`sortName)+%"("%+fullClassName(`sortName)+%" arg) throws jjtraveler.VisitFailure {
    return ("%+fullClassName(`sortName)+%") any.visit(arg);
  }
"%);
      }
    }
    return out.toString();
  }

}
