/*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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
import tom.gom.tools.error.GomRuntimeException;

public class ForwardTemplate extends TemplateClass {
  ClassName visitor;
  ClassNameList importedVisitors;
  ClassName abstractType;
  ClassNameList importedAbstractTypes;
  GomClassList sortClasses;
  GomClassList operatorClasses;

  %include { ../../adt/objects/Objects.tom}

  public ForwardTemplate(GomClass gomClass) {
    super(gomClass);
    %match(gomClass) {
      FwdClass[Visitor=visitorClass,
               ImportedVisitors=importedVisitors,
               AbstractType=abstractType,
               ImportedAbstractTypes=imported,
               SortClasses=sortClasses,
               OperatorClasses=ops] -> {
        this.visitor = `visitorClass;
        this.importedVisitors = `importedVisitors;
        this.abstractType = `abstractType;
        this.importedAbstractTypes = `imported;
        this.sortClasses = `sortClasses;
        this.operatorClasses = `ops;
        return;
      }
    }
    throw new GomRuntimeException(
        "Bad argument for ForwardTemplate: " + gomClass);
  }

  /* 
   * We may want to return the stringbuffer itself in the future, or directly
   * write to a Stream
   */
  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
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
]%);
generateDispatch(writer,importedAbstractTypes);
writer.write(%[
    else {
      return any.visit(v);
    }
  }
]%);
generateVisitMethods(writer);
writer.write(%[
}
]%);
  }

  private void generateVisitMethods(java.io.Writer writer) throws java.io.IOException {
    // generate a visit for each sort
    %match(GomClassList sortClasses) {
      concGomClass(_*,SortClass[ClassName=sortName],_*) -> {

        writer.write(%[
  public @ fullClassName(`sortName) @ @visitMethod(`sortName)@(@fullClassName(`sortName)@ arg) throws jjtraveler.VisitFailure {
    return (@fullClassName(`sortName)@) any.visit(arg);
  }
]%);
      }
    }
  }

  private void generateDispatch(java.io.Writer writer, ClassNameList types) throws java.io.IOException {
    while(!types.isEmptyconcClassName()) {
      writer.write(%[    else if (v instanceof @fullClassName(types.getHeadconcClassName())@) {
      return ((@fullClassName(types.getHeadconcClassName())@) v).accept(this);
    }]%);
      types = types.getTailconcClassName();
    }
  }
  
  private String importedVisitorList(ClassNameList list) {
    StringBuffer out = new StringBuffer();
    while(!list.isEmptyconcClassName()) {
      out.append(", "+fullClassName(list.getHeadconcClassName()));
      list = list.getTailconcClassName();
    }
    return out.toString();
  }
}
