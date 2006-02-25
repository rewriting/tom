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

public class TemplateVisitor extends TemplateClass {
  GomClassList sortClasses;
  GomClassList operatorClasses;

	%include { ../../adt/objects/Objects.tom}

  public TemplateVisitor(ClassName className, GomClassList sortClasses, GomClassList operatorClasses) {
    super(className);
    this.sortClasses = sortClasses;
    this.operatorClasses = operatorClasses;
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public interface "+className()+" {\n");
    out.append("\n");
    
    // generate a visit for each sort
    %match(GomClassList sortClasses) {
      concGomClass(_*,SortClass[className=sortName],_*) -> {    
        out.append("\tpublic abstract "+fullClassName(`sortName)+" visit_"+className(`sortName)+"("+fullClassName(`sortName)+" arg) throws jjtraveler.VisitFailure;\n");
      }
    }

    // generate a visit for each operator
    %match(GomClassList operatorClasses) {
      concGomClass(_*,OperatorClass[className=opName,sortName=sortName],_*) -> {    
        out.append("\tpublic abstract "+fullClassName(`sortName)+" visit_"+className(`sortName)+"_"+className(`opName)+"("+fullClassName(`opName)+" arg) throws jjtraveler.VisitFailure;\n");
      }
    }

    out.append("\n");
    out.append("}");
    
    return out.toString();
  }
}
