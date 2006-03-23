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
  ClassNameList importedVisitors;
  ClassName abstractType;
  ClassNameList importedAbstractTypes;
  GomClassList sortClasses;
  GomClassList operatorClasses;

  %include { ../../adt/objects/Objects.tom}

  public ForwardTemplate(ClassName className,
                         ClassName visitor,
                         ClassNameList importedVisitors,
                         ClassName abstractType,
                         ClassNameList importedAbstract,
                         GomClassList sortClasses,
                         GomClassList operatorClasses) {
    super(className);
    this.visitor = visitor;
    this.importedVisitors = importedVisitors;
    this.abstractType = abstractType;
    this.importedAbstractTypes = importedAbstract;
    this.sortClasses = sortClasses;
    this.operatorClasses = operatorClasses;
  }

  /* 
   * We may want to return the stringbuffer itself in the future, or directly
   * write to a Stream
   */
  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append(%[
package @getPackage()@;

public class @className()@ implements @ className(visitor)+importedVisitorList(importedVisitors) @, jjtraveler.Visitor {
  protected jjtraveler.Visitor any;

  public @className()@(jjtraveler.Visitor v) {
    this.any = v;
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {
    if (v instanceof @fullClassName(abstractType)@) {
      return ((@fullClassName(abstractType)@) v).accept(this);
    }
@generateDispatch(importedAbstractTypes)@
    else {
      return any.visit(v);
    }
  }

@generateVisitMethods()@

}]%);

    return out.toString();
  }
  private String generateVisitMethods() {
    StringBuffer out = new StringBuffer();
    // generate a visit for each sort
    %match(GomClassList sortClasses) {
      concGomClass(_*,SortClass[className=sortName],_*) -> {

        out.append(%[
  public @ fullClassName(`sortName) @ @visitMethod(`sortName)@(@fullClassName(`sortName)@ arg) throws jjtraveler.VisitFailure {
    return (@fullClassName(`sortName)@) any.visit(arg);
  }
]%);
      }
    }
    return out.toString();
  }

  private String generateDispatch(ClassNameList types) {
    StringBuffer out = new StringBuffer();
    while(!types.isEmpty()) {
      out.append(%[    else if (v instanceof @fullClassName(types.getHead())@) {
      return ((@fullClassName(types.getHead())@) v).accept(this);
    }]%);
      types = types.getTail();
    }
    return out.toString();
  }
  String importedVisitorList(ClassNameList list) {
    StringBuffer out = new StringBuffer();
    while(!list.isEmpty()) {
      out.append(", "+fullClassName(list.getHead()));
      list = list.getTail();
    }
    return out.toString();
  }
}
