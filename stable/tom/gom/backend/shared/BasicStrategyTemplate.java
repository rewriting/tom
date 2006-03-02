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
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;

public class BasicStrategyTemplate extends TemplateClass {
  ClassName fwd;

  public BasicStrategyTemplate(ClassName className, ClassName fwd) {
    super(className);
    this.fwd = fwd;
  }

  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public class "+className()+" extends "+className(fwd) + " implements jjtraveler.reflective.VisitableVisitor {\n");
    out.append("\n");
    out.append("\tpublic int getChildCount() {\n");
    out.append("\t\treturn 1;\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic jjtraveler.Visitable getChildAt(int i) {\n");
    out.append("\t\tswitch (i) {\n");
    out.append("\t\tcase 0: return (jjtraveler.Visitable) any;\n");
    out.append("\t\tdefault: throw new IndexOutOfBoundsException();\n");
    out.append("\t\t}\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic jjtraveler.Visitable setChildAt(int i, jjtraveler.Visitable child) {\n");
    out.append("\t\tswitch (i) {\n");
    out.append("\t\tcase 0: any = (jjtraveler.reflective.VisitableVisitor) child; return this;\n");
    out.append("\t\tdefault: throw new IndexOutOfBoundsException();\n");
    out.append("\t\t}\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic "+className()+"(jjtraveler.reflective.VisitableVisitor any) {\n");
    out.append("\t\tsuper(any);\n");
    out.append("\t}\n");
    out.append("}");
    
    return out.toString();
  }
}
